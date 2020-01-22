package unist.vdi.common;

import javax.servlet.http.HttpServletRequest;

public class CommonSecurity {
	public static boolean checkReferer(HttpServletRequest request) {
		String referer = request.getHeader("Referer");
		if(referer.startsWith("http://10.51.15.100")) {
			return true;
		}
		if(referer.startsWith("https://10.51.15.100")) {
			return true;
		}
		return true;
	}
}
