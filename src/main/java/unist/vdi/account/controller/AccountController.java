package unist.vdi.account.controller;

import javax.naming.directory.Attributes;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import unist.vdi.account.service.AccountManager;
import unist.vdi.account.service.LDAPManager;
import unist.vdi.account.service.UserVO;
import unist.vdi.common.CommonSecurity;
import unist.vdi.common.CommonUtil;


@Controller
public class AccountController {
	@RequestMapping("/account/login.do")
    public String login(HttpServletResponse response, HttpSession session) throws Exception {
		if(!AccountManager.isLogin(session)) {
			return "/account/login";
		} else {
			response.sendRedirect("/vcenter/vmlist.do");
			return null;
		}
    }
	
	@RequestMapping("/account/loginProcess.do")
    public void processLogin(String username, String password, HttpServletRequest request, HttpServletResponse response, HttpSession session) {
    	try {
    		if(!CommonSecurity.checkReferer(request)) {
    			throw new Exception("Exploiting cross-site scripting in Referer header.");
    		}
    		LDAPManager ldap = new LDAPManager();
    		if(!ldap.authenticate(username, password)) {
    			throw new Exception("Exception ldap.authenticate()");
    		}
    		
    		Attributes attrs = ldap.searchUser("sAMAccountName", username);
    		UserVO user = new UserVO();
			user.setId(attrs.get("sAMAccountName").get().toString());
			user.setName(attrs.get("sn").get().toString());
			if(attrs.get("employeeNumber") != null) {
				user.setErpid(attrs.get("employeeNumber").get().toString());
			} else {
				user.setErpid("99999");
			}
			user.setIp(CommonUtil.getUserIP(request));
			session.setAttribute("user", user);
			
        	JSONObject json = new JSONObject();
        	response.setContentType("text/json;charset=utf-8");
        	response.getWriter().print(json.toString());
    	} catch(Exception e) {
    		CommonUtil.writeErrorLogs("login exception: " + e.getMessage());
    	}
    }
	
	@RequestMapping("/account/logout.do")
    public void logout(HttpServletResponse response, HttpSession session) throws Exception {
		session.setAttribute("user", null);
		response.sendRedirect("/");
    }
}
