package unist.vdi.common;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import unist.vdi.account.service.AccountManager;



@Controller
public class CommonController {
	
    @RequestMapping("/common/verifyLogin.do")
    public void verifyLogin(HttpServletRequest request, HttpServletResponse response, HttpSession session) {
    	try {
    		String verify = "0";
    		if(AccountManager.isLogin(session)) {
    			verify = "1";
    		}
        	JSONObject json = new JSONObject();
        	json.put("verify", verify);
        	response.setContentType("text/json;charset=utf-8");
        	response.getWriter().print(json.toString());
    	} catch(Exception e) {
    		e.printStackTrace();
    	}
    }
    
    
}