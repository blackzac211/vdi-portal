<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8">
<title>VDI Portal</title>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=11">
<meta name="viewport" content="width=device-width, initiial-scale=1,maximum-scale=1, user-scalable=no">

<link rel="icon" href="/images/favicon.ico" sizes="32x32">
<link rel="stylesheet" href="/css/base.css" />
<script type="text/javascript" src="/js/jquery-3.2.1.min.js"></script>
<script type="text/javascript" src="/js/library.js"></script>
<script type="text/javascript" src="/js/file.js"></script>
 

<style type="text/css">
	#topicon { margin-top:50px; text-align:center; }
	#main { overflow:hidden; width:980px; margin:0 auto; margin-top:40px; font-size:24px; }
	#main #content { text-align:center; }
	#main div { margin:15px; }
	#main #contact { margin-top:40px; }
	#main #download { margin-top:80px; }
	#main #download span { cursor:pointer; background-color:#43c1c3; padding:10px 15px; color:#fff; border-radius:10px; }
	.c_b { color:blue; }
</style>
<script type="text/javascript">
	function downloadCerts() {
		location.href = "/files/certs.zip";
	}
</script>
</head>
<body>

<div id="topicon">
	<img id="top" alt="" src="/images/warning.png" />
</div>

<div id="main">
	<div id="content">
		<div>웹콘솔 기능을 사용하려면 인증서를 설치해야 합니다.<br/>
			인증서(certs.zip)를 신뢰할 수 있는 루트 인증 기관에 설치하시기 바랍니다.
		</div>
		<div>Require to install the certificate to use Web Console.<br/>
			Please add the certificate(certs.zip) to Trusted Root Certification Authorities.
		</div>
		<div id="contact">Contact: Information Technology Team, <span class="c_b">security@unist.ac.kr</span></div>
		<div id="download"><span onclick="downloadCerts()">DOWNLOAD</span></div>
	</div>
</div>

</body>
</html>
