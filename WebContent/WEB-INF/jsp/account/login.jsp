<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<!DOCTYPE html>
<html>
<head>
<jsp:include page="/common/head.jsp" />

<style type="text/css">
	#loginform { margin-top:20px; float:left; }
	#loginform li { overflow:hidden; margin:5px 0 5px 0; }
	#loginform label { float:left; width:80px; margin-right:5px; text-align:right; }
	#loginform li input { width:150px; }
	#btn-area { text-align:center; margin:10px 0 10px 0; }
</style>

<script type="text/javascript">
	var user = "${user}";
	if(user != null && user != "") {
		location.href = "/release/tree.do";
	}

	function processLogin() {
		var params = $("#loginform").serialize();
		
		$.ajax({
			type: "post",
			url: "/account/loginProcess.do",
			data: params,
			dataType: "json",
			async: false,
			success: function(data) {
				location.href = "/vcenter/vmlist.do";
			},
            error: function() {
            	alert("Incorrect username or password.");
            }
		});
		return false;
	}
</script>
</head>
<body>
<jsp:include page="/common/navigation.jsp" /> 

<div id="main">
	<div id="content">
		<div id="common-title">
			Login
		</div>
		<form id="loginform" action="" onsubmit="return processLogin()">
			<ul>
				<li>
					<label for="username">ID</label>
					<input type="text" id="username" name="username" placeholder="Your portal ID" required autofocus />
				</li>
				<li>
					<label for="password">Password</label>
					<input type="password" id="password" name="password" placeholder="Your portal Password" required />
				</li>
			</ul>
			<div id="btn-area"><input type="submit" class="button" value="Sign" /></div>
		</form>
	</div>
</div> 

</body>
</html>