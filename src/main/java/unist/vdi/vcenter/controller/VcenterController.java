package unist.vdi.vcenter.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.vmware.vim25.ManagedObjectReference;
import com.vmware.vim25.VirtualMachineTicket;

import unist.vdi.account.service.AccountManager;
import unist.vdi.account.service.UserVO;
import unist.vdi.common.CommonSecurity;
import unist.vdi.vcenter.service.VMService;
import unist.vdi.vcenter.service.PowerService;
import unist.vdi.vcenter.service.VDIConnection;
import unist.vdi.vcenter.service.CustomVM;


@Controller
public class VcenterController {
	
	@RequestMapping("/vcenter/vmlist.do")
    public String vmlist(HttpSession session) throws Exception {
		if(!AccountManager.isLogin(session)) {
			return "/account/redirect_login";
		} else {
			return "/vcenter/vmlist";
		}
    }
	
	@RequestMapping("/vcenter/console.do")
    public String console(Model model, String vmId, HttpServletRequest request, HttpSession session) throws Exception {
		if(!AccountManager.isLogin(session)) {
			return "/account/redirect_login";
		} else {
			VDIConnection conn = VDIConnection.getInstance();
			String vmName = new VMService().getVMName(vmId, conn);
			UserVO user = (UserVO)session.getAttribute("user");
			
			if(vmName.startsWith(user.getId())) {
				model.addAttribute("vmName", vmName);
				return "/vcenter/console";
			}
			return "/common/error";
		}
    }
	
	@RequestMapping("/vcenter/console_cert.do")
    public String console_cert(HttpServletRequest request, HttpSession session) throws Exception {
		if(!AccountManager.isLogin(session)) {
			return "/account/redirect_login";
		} else {
			return "/vcenter/console_cert";
		}
    }
    
    @RequestMapping("/vcenter/selectVMListByUser.do")
    public void selectVMListByUser(HttpServletRequest request, HttpServletResponse response, HttpSession session) {
    	try {
    		if(!CommonSecurity.checkReferer(request)) {
    			throw new Exception("Exploiting cross-site scripting in Referer header.");
    		}
    		if(!AccountManager.isLogin(session)) {
    			throw new Exception("isLogin");
    		}
    		UserVO user = (UserVO)session.getAttribute("user");
    		VDIConnection conn = VDIConnection.getInstance();
    		List<CustomVM> list = new VMService().getVMList(user.getId(), conn);
    		
    		JSONObject json = new JSONObject();
        	json.put("list", list);
        	response.setContentType("text/json;charset=utf-8");
        	response.getWriter().print(json.toString());
    	} catch(Exception e) {
    		e.printStackTrace();
    	}
    }
    
    
    @RequestMapping("/vcenter/powerOn.do")
    public void powerOn(String vmId, HttpServletRequest request, HttpServletResponse response, HttpSession session) {
    	try {
    		if(!CommonSecurity.checkReferer(request)) {
    			throw new Exception("Exploiting cross-site scripting in Referer header.");
    		}
    		if(!AccountManager.isLogin(session)) {
    			throw new Exception();
    		}
    		VDIConnection conn = VDIConnection.getInstance();
    		new PowerService().powerOn(vmId, conn);
    		
    		JSONObject json = new JSONObject();
        	json.put("result", "The VM is turning on");
        	response.setContentType("text/json;charset=utf-8");
        	response.getWriter().print(json.toString());
    	} catch(Exception e) {
    		e.printStackTrace();
    	}
    }
    
    @RequestMapping("/vcenter/powerOff.do")
    public void powerOff(String vmId, HttpServletRequest request, HttpServletResponse response, HttpSession session) {
    	try {
    		if(!CommonSecurity.checkReferer(request)) {
    			throw new Exception("Exploiting cross-site scripting in Referer header.");
    		}
    		if(!AccountManager.isLogin(session)) {
    			throw new Exception();
    		}
    		VDIConnection conn = VDIConnection.getInstance();
    		new PowerService().powerOff(vmId, conn);
    		
    		JSONObject json = new JSONObject();
    		json.put("result", "The VM is turning off");
        	response.setContentType("text/json;charset=utf-8");
        	response.getWriter().print(json.toString());
    	} catch(Exception e) {
    		e.printStackTrace();
    	}
    }
    
    @RequestMapping("/vcenter/reset.do")
    public void reset(String vmId, HttpServletRequest request, HttpServletResponse response, HttpSession session) {
    	try {
    		if(!CommonSecurity.checkReferer(request)) {
    			throw new Exception("Exploiting cross-site scripting in Referer header.");
    		}
    		if(!AccountManager.isLogin(session)) {
    			throw new Exception();
    		}
    		VDIConnection conn = VDIConnection.getInstance();
    		new PowerService().reset(vmId, conn);
    		
    		JSONObject json = new JSONObject();
    		json.put("result", "The VM is restarting");
        	response.setContentType("text/json;charset=utf-8");
        	response.getWriter().print(json.toString());
    	} catch(Exception e) {
    		e.printStackTrace();
    	}
    }
    
    @RequestMapping("/vcenter/acquireMksTicket.do")
    public void acquireMksTicket(String vmId, HttpServletRequest request, HttpServletResponse response, HttpSession session) {
    	try {
    		if(!CommonSecurity.checkReferer(request)) {
    			throw new Exception("Exploiting cross-site scripting in Referer header.");
    		}
    		if(!AccountManager.isLogin(session)) {
    			throw new Exception();
    		}
    		VDIConnection conn = VDIConnection.getInstance();
    		ManagedObjectReference mor = new ManagedObjectReference();
        	mor.setType("VirtualMachine");
        	mor.setValue(vmId);
        	VirtualMachineTicket vmTicket = conn.getVimAuthHelper().getVimPort().acquireTicket(mor, "webmks");
        	
    		JSONObject json = new JSONObject();
    		json.put("host", vmTicket.getHost());
    		json.put("port", vmTicket.getPort());
    		json.put("ticket", vmTicket.getTicket());
    		
    		response.setContentType("text/json;charset=utf-8");
        	response.getWriter().print(json.toString());
    	} catch(Exception e) {
    		e.printStackTrace();
    	}
    }
}

