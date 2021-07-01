package unist.vdi.account.controller;

import java.util.List;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.opensaml.saml2.core.Attribute;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.saml.SAMLCredential;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import unist.vdi.account.service.UserVO;
import unist.vdi.common.CommonUtil;


@Controller
public class AccountController {
	@RequestMapping("/account/login.do")
    public void login(HttpServletResponse response, HttpSession session) throws Exception {
		try {
			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			SAMLCredential credential = (SAMLCredential) authentication.getCredentials();
			List<Attribute> list = credential.getAttributes();
			
			UserVO user = new UserVO();
			user.setId(authentication.getName());
			
			for(int i = 0; i < list.size(); i++) {
				String attName = list.get(i).getName();
				String[] arr = credential.getAttributeAsStringArray(attName);
				
				if(attName.equals("name")) {
					user.setName(arr[0]);
				} else if(attName.equals("erpid")) {
					user.setErpid(arr[0]);
				} else if(attName.equals("ip")) {
					user.setIp(arr[0]);
				}
			}
			session.setAttribute("user", user);
			response.sendRedirect("/vcenter/vmlist.do");
		} catch(Exception e) {
			CommonUtil.writeErrorLogs("login exception: " + e.getMessage());
		}
    }
	
	/*
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
	*/
}
