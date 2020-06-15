package unist.vdi.common;

import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

import unist.vdi.account.service.UserVO;

public class CustomSessionListener implements HttpSessionListener {
	
	@Override
	public void sessionCreated(HttpSessionEvent se) {
		
	}
	
	@Override
	public void sessionDestroyed(HttpSessionEvent se) {
		try {
			Object obj = se.getSession().getAttribute("user");
			if(obj == null) {
				return;
			}
			if((obj instanceof UserVO)) {
				se.getSession().setAttribute("user", null);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
