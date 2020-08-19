<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<!DOCTYPE html>
<html>
<head>
<jsp:include page="/common/head.jsp" />

<script type="text/javascript">
	function processLogin() {
		var params = $("#loginform").serialize();
		showLoading();
		$.ajax({
			type: "post",
			url: "/account/loginProcess.do",
			data: params,
			dataType: "json",
			async: true,
			success: function(data) {
				location.href = "/vcenter/vmlist.do";
			},
            error: function() {
            	alert("Incorrect username or password.");
            	hideLoading();
            }
		});
		return false;
	}
</script>

<style type="text/css">
	#content { margin-top:100px; }
	#left-area { float:left; width:400px; }
	#left-area header { margin-top:120px; font-size:52px; }
	#left-area #logo { width:200px; margin:20px 0 0 20px; }
	#loginwrap { float:right; width:480px; height:460px; border:1px solid #dddddd; }
	#loginwrap form { margin:60px 70px 60px 70px; }
	#loginwrap header { font-size:26px; margin-bottom:27px; }
	#loginwrap li input { padding-left:10px; width:calc(100% - 10px); font-size:16px; height:45px; border:0; border-bottom:2px solid #dddddd; }
	#loginwrap li input:focus { outline:none; border-color:#000; } 
	#loginwrap #loginbtn { margin-top:42px; width:100%; height:50px; border:0; border-radius:5px; font-size:18px; background-color:#001C54; color:#ffffff; }
	
	@media ( max-width: 980px ) {
		#content { margin-top:0; }
		#loginwrap { float:left; }
	}
	
	@media ( max-width: 480px ) {
		#content { margin-top:0; overflow:hidden; }
		#left-area header { margin-top:0; }
		#loginwrap { float:none; overflow:hidden; margin:0 auto; margin-top:40px; width:100%; }
	}
</style>
</head>
<body>

<div id="main">
	<div id="content">
		<div id="left-area">
			<header>VDI Portal</header>
		</div>
		
		<div id="loginwrap">
			<form id="loginform" action="" onsubmit="return processLogin()">
				<header>UNIST</header>
				<ul>
					<li><input type="text" id="username" name="username" placeholder="Your portal ID" required autofocus /></li>
					<li><input type="password" id="password" name="password" placeholder="Your portal Password" autocomplete="off" required /></li>
				</ul>
				<input id="loginbtn" type="submit" class="button" value="Sign in" />
			</form>

			<form action="https://vdi.unist.ac.kr/saml/login?disco=true" method="get">
				<div><input type="hidden" name="idp" value="http://sts.unist.ac.kr/adfs/services/trust" /></div>
				<div><input type="submit" value="Login" /></div>
			</form>
		</div>
	</div>
</div> 

</body>
</html>