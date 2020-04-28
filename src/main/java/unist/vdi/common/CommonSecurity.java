package unist.vdi.common;

import javax.servlet.http.HttpServletRequest;

public class CommonSecurity {
	public static boolean checkReferer(HttpServletRequest request) {
		String referer = request.getHeader("Referer");
		if(referer.startsWith("http://vdi.unist.ac.kr")) {
			return true;
		}
		if(referer.startsWith("https://vdi.unist.ac.kr")) {
			return true;
		}
		return false;
	}
}
