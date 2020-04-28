package unist.vdi.common.file.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Calendar;

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
	    	SimpleDateFormat df = new SimpleDateFormat("yyMMddHHmmss");
	    	Calendar cal = Calendar.getInstance();
	    	String fileName = df.format(cal.getTime());
	    	
    		File file = new File("E:\\temp\\"+fileName+".bat");
	    	FileWriter writer = new FileWriter(file, false);
	    	writer.write("@echo off\n");
	    	// 자격 증명 삭제
	    	writer.write("cmdkey.exe /list > \"%TEMP%\\vdi_list.txt\"\n");
	    	writer.write("findstr.exe target %TEMP%\\vdi_list.txt > \"%TEMP%\\vdi_temp.txt\"\n");
	    	writer.write("for /f \"delims== tokens=1,2\" %%a in (%TEMP%\\vdi_temp.txt) do cmdkey.exe /delete %%b\n");
	    	// 신규 자격 증명 추가
	    	writer.write("cmdkey /generic:\""+ipAddress+"\" /user:\".\\"+user.getId()+"@unist.ac.kr\"\n");
	    	writer.write("start mstsc /v:"+ipAddress+":38888\n");
	    	writer.write("exit");
	    	writer.flush();
	    	writer.close();
	    	/*
	    	// EXE 파일 생성
	    	File dir = new File("E:\\converter\\" + dirName);
	    	if(!dir.exists())
	    		dir.mkdir();
	    	String path = "";
			path += "E:\\converter\\Bat_To_Exe_Converter.exe -bat ";
			path += "E:\\converter\\ConnectPC.bat -save ";
			path += "E:\\converter\\"+dirName+"\\ConnectPC.exe -icon ";
			path += "E:\\converter\\logo.ico";
			Process process = Runtime.getRuntime().exec(path);
			process.getErrorStream().close();
			process.getInputStream().close();
			process.getOutputStream().close();
			process.waitFor();
			
			File exeFile = new File("E:\\converter\\"+dirName+"\\ConnectPC.exe");
			*/
			response.setContentType("application/octet-stream");
			response.setContentLengthLong((long)file.length());
			response.setHeader("Content-Transfer-Encoding", "binary");
			response.setHeader("Content-Disposition", "attachment; fileName=\"ConnectVM.bat\";");
		
			OutputStream out = response.getOutputStream();
			FileInputStream in = new FileInputStream(file);
			FileCopyUtils.copy(in, out);
			in.close();
			out.flush();
			
			// 10초 후 파일 삭제
			Thread.sleep(1000 * 10);
			file.delete();
		} catch(Exception e) {
			try {
				response.setContentType("text/html; charset=utf-8");
				PrintWriter out = response.getWriter();
				out.println("<script>alert('Unexpected error occurred in connect task');</script>");
			} catch(Exception ex) {}
		}
    }
}
