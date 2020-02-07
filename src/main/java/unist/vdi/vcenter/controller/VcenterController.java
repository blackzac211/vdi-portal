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
import unist.vdi.vcenter.service.GuestPowerLifeCycle;
import unist.vdi.vcenter.service.CustomVM;
import vmware.samples.vcenter.vm.list.ListVMs;
import vmware.samples.vcenter.vm.power.PowerLifeCycle;


@Controller
public class VcenterController {
	private String[] server = new String[2];
	private String[] username = new String[2];
	private String[] password = new String[2];
	
	public VcenterController() {
		// 인터넷망 vcenter 정보
		server[0] = "10.4.1.205";
		username[0] = "administrator";
		password[0] = "Un!s7##vdiad";
		// 업무망 vcenter 정보
		server[1] = "10.4.1.196";
		username[1] = "administrator";
		password[1] = "Un!s7##vdiad";
	}
	
	@RequestMapping("/vcenter/vmlist.do")
    public String list(HttpSession session) throws Exception {
		if(!AccountManager.isLogin(session)) {
			return "/account/redirect_login";
		} else {
			return "/vcenter/vmlist";
		}
    }
	
    
    @RequestMapping("/vcenter/selectVMListByUser.do")
    public void selectVMListByUser(int t, HttpServletRequest request, HttpServletResponse response, HttpSession session) {
    	try {
    		if(!CommonSecurity.checkReferer(request)) {
    			throw new Exception("Exploiting cross-site scripting in Referer header.");
    		}
    		if(!AccountManager.isLogin(session)) {
    			throw new Exception();
    		}
    		
    		String str = "--server "+server[t]+" --username "+username[t]+" --password "+password[t]+" --skip-server-verification true";
    		String[] args = str.split(" ");
        	
    		ListVMs listVMs = new ListVMs();
    		UserVO user = (UserVO)session.getAttribute("user");
    		List<CustomVM> list = listVMs.getVMList(args, user.getId());
    		
    		JSONObject json = new JSONObject();
        	json.put("list", list);
        	response.setContentType("text/json;charset=utf-8");
        	response.getWriter().print(json.toString());
    	} catch(Exception e) {
    		e.printStackTrace();
    	}
    }
    
    @RequestMapping("/vcenter/powerOnOff.do")
    public void powerOnOff(int t, String vmName, HttpServletRequest request, HttpServletResponse response, HttpSession session) {
    	try {
    		if(!CommonSecurity.checkReferer(request)) {
    			throw new Exception("Exploiting cross-site scripting in Referer header.");
    		}
    		if(!AccountManager.isLogin(session)) {
    			throw new Exception();
    		}
    		
    		String str = "--server "+server[t]+" --username "+username[t]+" --password "+password[t]+" --skip-server-verification true --vmname " + vmName;
    		String[] args = str.split(" ");
    		PowerLifeCycle power = new PowerLifeCycle();
    		power.executePower(args);
    		
        	JSONObject json = new JSONObject();
       		json.put("result", "완료");
        	response.setContentType("text/json;charset=utf-8");
        	response.getWriter().print(json.toString());
    	} catch(Exception e) {
    		e.printStackTrace();
    	}
    }
    
    @RequestMapping("/vcenter/restart.do")
    public void restart(int t, String vmId, HttpServletRequest request, HttpServletResponse response, HttpSession session) {
    	try {
    		if(!CommonSecurity.checkReferer(request)) {
    			throw new Exception("Exploiting cross-site scripting in Referer header.");
    		}
    		if(!AccountManager.isLogin(session)) {
    			throw new Exception();
    		}
    		
    		String str = "--server "+server[t]+" --username "+username[t]+" --password "+password[t]+" --skip-server-verification true";
    		System.out.println("args: " + str);
    		String[] args = str.split(" ");
    		GuestPowerLifeCycle power = new GuestPowerLifeCycle();
    		power.restart(args, vmId);
    		
        	JSONObject json = new JSONObject();
       		json.put("result", "재부팅 시작");
        	response.setContentType("text/json;charset=utf-8");
        	response.getWriter().print(json.toString());
    	} catch(Exception e) {
    		e.printStackTrace();
    	}
    }
}

