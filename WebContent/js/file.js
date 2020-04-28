function uploadFile(parent_id, callback) {
	if ($("#file_name").val() == "") {
		alert("Please, enter file name.");
		return;
	}
	if ($("#mfile")[0].files[0] == null) {
		alert("Please, upload a file.");
		return;
	}
	var formData = new FormData();
	formData.append("parent_id", parent_id);
	formData.append("file_name", $("#file_name").val());
	formData.append("mfile", $("#mfile")[0].files[0]);
	showLoading();
	$.ajax({
		url : '/common/file/uploadFile.do',
		data : formData,
		processData : false,
		contentType : false,
		type : "post",
		async : true,
		success : function(data) {
			hideLoading();
			alert("The file has been uploaded successfully.");
			callback(parent_id);
		},
		error : function() {
			alert("An error has occured.");
		}
	});
}

function downloadFile(ipAddress) {
	$("#download-ifrm").remove();
	var ifrm = $("<iframe id='download-ifrm' name='download-ifrm' style='width:0;height:0;'></iframe>");
	$("body").append(ifrm);
	var form = $("<form method='post' action='/common/file/downloadFile.do' target='download-ifrm'></form>");
	$("body").append(form);
	form.append("<input type='hidden' name='ipAddress' value='"+ipAddress+"' />");

	form.submit();
}

function deleteFile(id) {
	$.ajax({
		type : "post",
		url : "/common/file/deleteFile.do",
		dataType : "json",
		data : "id=" + id,
		type : "post",
		async : false,
		success : function(data) {
			alert("successfully deleted.");
		},
		error : function() {
			alert("An error has occured.");
		}
	});
}

function selectFileByParentId(parentId) {
	var list = null;
	$.ajax({
		type:"post",
		url:"/common/file/selectFileByParentId.do",
		dataType:"json",
		data:"parent_id="+parentId,
		type:"post",
		async:false,
		success:function(data) {
			list = data.list;
		}
	});
	return list;
}
