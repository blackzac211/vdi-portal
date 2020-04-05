package unist.vdi.vcenter.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import unist.vdi.account.service.AccountManager;
import unist.vdi.account.service.UserVO;
import unist.vdi.common.CommonSecurity;
import unist.vdi.vcenter.service.ListVMsService;
import unist.vdi.vcenter.service.PowerService;
import unist.vdi.vcenter.service.PowerShellService;
import unist.vdi.vcenter.service.VDIConnection;
import unist.vdi.vcenter.service.CustomVM;


@Controller
public class VcenterController {
	
	@RequestMapping("/vcenter/vmlist.do")
    public String list(HttpSession session) throws Exception {
		if(!AccountManager.isLogin(session)) {
			return "/account/redirect_login";
		} else {
			return "/vcenter/vmlist";
		}
    }
	
	@RequestMapping("/vcenter/console.do")
    public String console(String vmId, HttpServletRequest request, HttpSession session) throws Exception {
		if(!AccountManager.isLogin(session)) {
			return "/account/redirect_login";
		} else {
			UserVO user = (UserVO)session.getAttribute("user");
			// if(vmName.startsWith(user.getId())) {
				return "/vcenter/console";
			// }
			// return "/common/error";
		}
    }
    
    @RequestMapping("/vcenter/selectVMListByUser.do")
    public void selectVMListByUser(int nav, HttpServletRequest request, HttpServletResponse response, HttpSession session) {
    	try {
    		if(!CommonSecurity.checkReferer(request)) {
    			throw new Exception("Exploiting cross-site scripting in Referer header.");
    		}
    		if(!AccountManager.isLogin(session)) {
    			throw new Exception();
    		}
    		
    		UserVO user = (UserVO)session.getAttribute("user");
    		VDIConnection conn = null;
    		if(nav == 0) {
    			conn = (VDIConnection)session.getAttribute("vdiConn");
    		} else {
    			conn = (VDIConnection)session.getAttribute("vdiInConn");
    		}
    		List<CustomVM> list = new ListVMsService().getVMList(user.getId(), conn);
    		
    		JSONObject json = new JSONObject();
        	json.put("list", list);
        	response.setContentType("text/json;charset=utf-8");
        	response.getWriter().print(json.toString());
    	} catch(Exception e) {
    		e.printStackTrace();
    	}
    }
    
    
    @RequestMapping("/vcenter/powerOn.do")
    public void powerOn(int nav, String vmId, HttpServletRequest request, HttpServletResponse response, HttpSession session) {
    	try {
    		if(!CommonSecurity.checkReferer(request)) {
    			throw new Exception("Exploiting cross-site scripting in Referer header.");
    		}
    		if(!AccountManager.isLogin(session)) {
    			throw new Exception();
    		}
    		VDIConnection conn = null;
    		if(nav == 0) {
    			conn = (VDIConnection)session.getAttribute("vdiConn");
    		} else {
    			conn = (VDIConnection)session.getAttribute("vdiInConn");
    		}
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
    public void powerOff(int nav, String vmId, HttpServletRequest request, HttpServletResponse response, HttpSession session) {
    	try {
    		if(!CommonSecurity.checkReferer(request)) {
    			throw new Exception("Exploiting cross-site scripting in Referer header.");
    		}
    		if(!AccountManager.isLogin(session)) {
    			throw new Exception();
    		}
    		VDIConnection conn = null;
    		if(nav == 0) {
    			conn = (VDIConnection)session.getAttribute("vdiConn");
    		} else {
    			conn = (VDIConnection)session.getAttribute("vdiInConn");
    		}
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
    public void reset(int nav, String vmId, HttpServletRequest request, HttpServletResponse response, HttpSession session) {
    	try {
    		if(!CommonSecurity.checkReferer(request)) {
    			throw new Exception("Exploiting cross-site scripting in Referer header.");
    		}
    		if(!AccountManager.isLogin(session)) {
    			throw new Exception();
    		}
    		VDIConnection conn = null;
    		if(nav == 0) {
    			conn = (VDIConnection)session.getAttribute("vdiConn");
    		} else {
    			conn = (VDIConnection)session.getAttribute("vdiInConn");
    		}
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
    		
    		JSONObject json = new JSONObject();
    		json.put("map", new PowerShellService().acquireMksTicket(vmId));
        	response.setContentType("text/json;charset=utf-8");
        	response.getWriter().print(json.toString());
    	} catch(Exception e) {
    		e.printStackTrace();
    	}
    }
}

