/**
 * 정보기술팀원 계정으로 로그인했는지 확인(Portal)
 * 맞으면 = 1, 틀리면 = 0
 */
function verifyAdmin() {
	var verify = 0;
	$.ajax({
		type:"post",
		url:"/common/select_userid.do",
		dataType:"json",
		async:false,
		success:function(data) {
			var userid = data.userid;
			var members = new Array("blackzac");
			if(userid == null) {
				return 0;
			}
			for(var i = 0; i < members.length; i++) {
				if(members[i] == userid) {
					verify = 1;
				}
			}
		}
	});
	return verify;
}

/**
 * 로그인했는지 확인(Portal)
 * 맞으면 = 1, 틀리면 = 0
 */
function verifyLogin() {
	var verify = 0;
	$.ajax({
		type:"post",
		url:"/common/verifyLogin.do",
		dataType:"json",
		async:false,
		success:function(data) {
			verify = data.verify;
		}
	});
	return verify;
}

function showLoading() {
	var tag = $("<img id='loading' alt='loading' src='/images/common/loading.gif' />");
	tag.css({"width":"50px", "height":"50px", "position":"absolute"});
	var left = ($("body").width() / 2) - 25;
	var top = ($("body").height() / 2) - 25;
	tag.css({"left":left, "top":top});
	
	$("body").append(tag);
}

function hideLoading() {
	$("#loading").remove();
}