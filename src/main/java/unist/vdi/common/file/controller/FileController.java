package unist.vdi.common.file.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.OutputStream;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.RequestMapping;

import unist.vdi.account.service.AccountManager;
import unist.vdi.account.service.UserVO;
import unist.vdi.common.CommonSecurity;

@Controller
public class FileController {
	@RequestMapping("/common/file/downloadFile.do")
    public void connectVM(String ipAddress, HttpServletRequest request, HttpServletResponse response, HttpSession session) {
    	try {
    		if(!CommonSecurity.checkReferer(request)) {
    			throw new Exception("Exploiting cross-site scripting in Referer header.");
    		}
    		if(!AccountManager.isLogin(session)) {
    			throw new Exception();
    		}

    		UserVO user = (UserVO)session.getAttribute("user");
    		
			// 로그 기록
			// insertDownloadLog(vo, session);
			
	    	// 파일 생성
    		File file = new File("Connect_VM.bat");
	    	FileWriter writer = new FileWriter(file, false);
	    	writer.write("@echo off\n");
	    	writer.write("cmdkey /generic:\""+ipAddress+":38888\" /user:\".\\"+user.getId()+"@unist.ac.kr\"\n");
	    	writer.write("start mstsc /v:"+ipAddress+":38888\n");
	    	writer.write("exit");
	    	writer.flush();
	    	writer.close();
		
			response.setContentType("application/octet-stream");
			response.setContentLengthLong((long)file.length());
			response.setHeader("Content-Transfer-Encoding", "binary");
			response.setHeader("Content-Disposition", "attachment; fileName=\"Connect_VM.bat\";");
		
			OutputStream out = response.getOutputStream();
			FileInputStream in = new FileInputStream(file);
			FileCopyUtils.copy(in, out);
			in.close();
			out.flush();
		} catch(Exception e) {
			try {
				response.setContentType("text/html; charset=utf-8");
				PrintWriter out = response.getWriter();
				out.println("<script>alert('Unexpected error occurred in connect task');</script>");
			} catch(Exception ex) {}
		}
    }
}
