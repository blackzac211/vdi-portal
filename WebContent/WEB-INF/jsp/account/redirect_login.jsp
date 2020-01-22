<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<!DOCTYPE html>
<html>
<head>
<jsp:include page="/common/head.jsp" />

<script type="text/javascript">
	alert("Authentication Required.");
	location.replace("/account/login.do");
</script>
</head>
<body>

</body>
</html>