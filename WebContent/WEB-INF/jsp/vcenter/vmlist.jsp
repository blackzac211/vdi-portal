<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<!DOCTYPE html>
<html>
<head>
<jsp:include page="/common/head.jsp" />

<style type="text/css">
	#common-table { width:100%; }
	#common-table th { font-weight:bold; font-size:16px; text-align:center; border:1px solid #dddddd; padding:10px; }
	#common-table td { text-align:left; color:#2e2d2f; font-size:14px; border-bottom:1px solid #dddddd; padding:10px; } 
	#common-table td.t_c { text-align:center; } 
	#common-table td .powerimg { position:relative; top:4px; width:20px; }
	#common-btn-area { margin:20px 0 20px 0; text-align:center; }
	
	@media ( max-width: 980px ) {
		#common-table td .powerimg { display:none; }
		#common-table td .button { display:block; }
	}
	@media ( max-width: 480px ) {
		#common-table td .powerimg { display:none; }
		#common-table td .button { display:block; }
	}		
</style>

<script type="text/javascript">
	$(function() {
		showList();
		setInterval(showList, 60000);
	});
	
	function clickTree(id) {
		location.href = "/release/tree.do?id_up="+id;
	}
	
	function showList() {
		showLoading();
		$.ajax({
			type:"post",
			url:"/vcenter/selectVMListByUser.do",
			dataType:"json",
			async:true,
			success:function(data) {
				var list = data.list;
				var map = data.map;
				var str = "";
				
				for(var i = 0; i < list.length; i++) {
					var item = list[i];
					var powerimg;
					var powerstate;
					var buttons;
					var desc = map[item.name];
					if(typeof desc == "undefined") {
						desc = "";
					}
					
					if(item.powerState=="POWERED_ON") {
						powerimg = "<img class='powerimg' src='/images/power_on.png' /> ";
						powerstate = "Power On";
						buttons = "<span class='button' onclick='powerOff(\""+item.vm+"\")'>Turn Off</span> ";
					} else {
						powerimg = "<img class='powerimg' src='/images/power_off.png' /> ";
						powerstate = "Power Off";
						buttons = "<span class='button' onclick='powerOn(\""+item.vm+"\")'>Turn On</span> ";
					}
					str += "<tr>";
					str += "<td>" + powerimg + item.name + "</td>";
					str += "<td class='t_c'>"+item.ipAddress+"</td>";
					str += "<td class='t_c'>"+powerstate+"</td>";
					// str += "<td class='t_c'><span class='button' onclick='downloadFile(\""+item.ipAddress+"\")'>Connect</span></td>";
					str += "<td class='t_c'>"+desc+"</td>";
					str += "<td class='t_c'>";
					str += "<span class='button' onclick='reset(\""+item.vm+"\")'>Restart</span> ";
					str += buttons;
					str += "<span class='button' onclick='console(\""+item.vm+"\")'>Console</span></td>";
					str += "</tr>";
				}
				$("#common-tbody").html(str); 
				hideLoading();
			}
		});
	}
		
	function powerOff(vmId) {
		if(confirm("Do you want to turn off forcibly?")) {
			showLoading();
			$.ajax({
				type:"post",
				url:"/vcenter/powerOff.do",
				data:"vmId="+vmId,
				dataType:"json", 
				async:true,
				success:function(data) {
					var result = data.result;
					// alert(result);
					hideLoading();
					showList();
				}
			});
		}
	}
	
	function powerOn(vmId) {
		if(confirm("Do you want to turn on?")) {
			showLoading();
			$.ajax({
				type:"post",
				url:"/vcenter/powerOn.do",
				data:"vmId="+vmId,
				dataType:"json", 
				async:true,
				success:function(data) {
					var result = data.result;
					// alert(result);
					hideLoading();
					showList();
				}
			});
		}
	}
	
	function reset(vmId) {
		if(confirm("Do you want to restart forcibly?")) {
			showLoading();
			$.ajax({
				type:"post",
				url:"/vcenter/reset.do",
				data:"vmId="+vmId,
				dataType:"json", 
				async:true,
				success:function(data) {
					var result = data.result;
					// alert(result);
					hideLoading();
					showList();
				}
			});
		}
	}
	
	function console(vmId) {
		$("#console_form").remove();
		var openWin = window.open("about:blank", "newWindow");
		var form = $("<form id='console_form' method='post' action='/vcenter/console.do' target='newWindow'></form>");
		$("body").append(form);
		form.append("<input type='hidden' name='vmId' value='"+vmId+"' />");
		form.submit();
	}
</script>
</head>
<body>
<jsp:include page="/common/navigation.jsp" /> 

<div id="main">
	<div id="content">
		<table id="common-table">
			<colgroup>
				<col width="22%" />
				<col width="14%" />
				<col width="14%" />
				<col width="25%" />
				<col width="25%" />
			</colgroup>
			<tr>
				<th>PC Name</th>
				<th>IP</th>
				<th>State</th>
				<th>Description</th>
				<th>Control</th>
			</tr>
			<tbody id="common-tbody">
			
			</tbody>
		</table>
	</div>
</div> 

</body>
</html>