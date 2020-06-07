<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>

<jsp:forward page="/index.do" />

<%-- 
<!DOCTYPE html>
<html>
<head>
<jsp:include page="/common/head.jsp" />

<script type="text/javascript">
	if(verifyLogin() == 1) {
		location.href = "/vcenter/vmlist.do";
	} else {
		location.href = "/account/login.do";
	}
</script>
</head>
<body>
</body>
</html> --%>