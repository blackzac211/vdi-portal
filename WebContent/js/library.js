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