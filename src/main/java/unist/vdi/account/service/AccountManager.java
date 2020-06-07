package unist.vdi.account.service;

import javax.servlet.http.HttpSession;

public class AccountManager {
	public static UserVO getUserByEmployeeNumber() {
		UserVO user = new UserVO();
		
		return user;
	}
	
	public static boolean isLogin(HttpSession session) {
		try {
			// VDIConnection vdiConn = (VDIConnection)session.getAttribute("vdiConn");
			UserVO user = (UserVO)session.getAttribute("user");
			// TmaxSSOToken token = (TmaxSSOToken)session.getAttribute("__tmax_eam_token__");
			// if(user != null && vdiConn != null) {
			
			if(user != null) {
				return true;
			}
			return false;
		} catch(Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	
	public static boolean isAdmin(HttpSession session) {
		try {
			// TmaxSSOToken token = (TmaxSSOToken)session.getAttribute("__tmax_eam_token__");
			UserVO user = (UserVO)session.getAttribute("user");
			if(user != null) {
				// String erpid = token.getCommon();
				if(user.getErpid().equals("24209"))
					return true;
			}
			return false;
		} catch(Exception e) {
			e.printStackTrace();
			return false;
		}
	}
}
