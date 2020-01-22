package unist.vdi.vcenter.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.vmware.vcenter.VMTypes.Summary;

import unist.vdi.account.service.AccountManager;
import unist.vdi.account.service.UserVO;
import unist.vdi.common.CommonSecurity;
import unist.vdi.vcenter.service.VM;
import vmware.samples.vcenter.vm.list.ListVMs;
import vmware.samples.vcenter.vm.power.PowerLifeCycle;


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
	
    
    @RequestMapping("/vcenter/selectVMListByUser.do")
    public void selectVMListByUser(HttpServletRequest request, HttpServletResponse response, HttpSession session) {
    	try {
    		if(!CommonSecurity.checkReferer(request)) {
    			throw new Exception("Exploiting cross-site scripting in Referer header.");
    		}
    		if(!AccountManager.isLogin(session)) {
    			throw new Exception();
    		}
    		
    		String str = "--server 10.4.1.205 --username administrator --password Un!s7##vdiad --skip-server-verification true";
    		String[] args = str.split(" ");
        	
    		ListVMs listVMs = new ListVMs();
    		List<Summary> vmList = listVMs.getVMList(args);
    		UserVO user = (UserVO)session.getAttribute("user");
    		List<VM> result = new ArrayList<>();
    		
    		for(Summary vmSummary : vmList) {
            	if(vmSummary.getName().startsWith(user.getId() + "-")) {
            		VM vm = new VM();
            		vm.setVm(vmSummary.getVm());
            		vm.setName(vmSummary.getName());
            		vm.setPowerState(vmSummary.getPowerState().toString());
            		result.add(vm);
            	}
            }
    		JSONObject json = new JSONObject();
        	json.put("list", result);
        	response.setContentType("text/json;charset=utf-8");
        	response.getWriter().print(json.toString());
    	} catch(Exception e) {
    		e.printStackTrace();
    	}
    }
    
    @RequestMapping("/vcenter/powerOnOff.do")
    public void powerOnOff(String vmName, HttpServletRequest request, HttpServletResponse response, HttpSession session) {
    	try {
    		if(!CommonSecurity.checkReferer(request)) {
    			throw new Exception("Exploiting cross-site scripting in Referer header.");
    		}
    		if(!AccountManager.isLogin(session)) {
    			throw new Exception();
    		}
    		
    		String str = "--server 10.4.1.205 --username administrator --password Un!s7##vdiad --skip-server-verification true --vmname " + vmName;
    		String[] args = str.split(" ");
    		PowerLifeCycle power = new PowerLifeCycle();
    		power.executePower(args);
    		
        	JSONObject json = new JSONObject();
       		json.put("result", "처리 완료");
        	response.setContentType("text/json;charset=utf-8");
        	response.getWriter().print(json.toString());
    	} catch(Exception e) {
    		e.printStackTrace();
    	}
    }
}

