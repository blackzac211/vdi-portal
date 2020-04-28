package unist.vdi.common;

import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

import unist.vdi.vcenter.service.VDIConnection;

public class CustomSessionListener implements HttpSessionListener {
	
	@Override
	public void sessionCreated(HttpSessionEvent se) {
		
	}
	
	@Override
	public void sessionDestroyed(HttpSessionEvent se) {
		try {
			Object obj = se.getSession().getAttribute("vdiConn");
			if(obj == null) {
				return;
			}
			
			if((obj instanceof VDIConnection)) {
				VDIConnection conn = (VDIConnection)obj;
				conn.logout();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
