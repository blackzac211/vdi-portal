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
	#common-table .td-control span { margin:0 5px 0 5px; }
	#common-btn-area { margin:20px 0 20px 0; text-align:center; }
</style>

<script type="text/javascript">
	var t = "<%=request.getParameter("t")%>";
	t = (t == "null") ? 0 : t;

	$(function() {
		showList();
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
			data:"t="+t,
			async:true,
			success:function(data) {
				list = data.list;
				$("#common-tbody").html("");
				for(var i = 0; i < list.length; i++) {
					var item = list[i];
					var str = "<tr>";
					str += "<td><img class='powerimg' src='' /> "+item.name+"</td>";
					str += "<td class='t_c'>"+item.ipAddress+"</td>";
					str += "<td class='t_c'>"+item.powerState+"</td>";
					str += "<td class='td-control t_c'>";
					str += "<span class='button' onclick='downloadFile(\""+item.ipAddress+"\")'>Connect</span>";
					str += "<span class='button' onclick='restart(\""+item.vm+"\")'>Restart</span>";
					str += "<span class='button ctlbtn'></span></td>";
					str += "</tr>";
					$("#common-tbody").append(str);
					if(item.powerState=="POWERED_ON") {
						$(".ctlbtn").eq(i).html("Turn Off")
						$(".powerimg").eq(i).attr("src", "/images/power_on.png");
						$(".ctlbtn").eq(i).click(function() {
							powerOnOff(item.name, 0);
						});
					} else {
						$(".ctlbtn").eq(i).html("Turn On")
						$(".powerimg").eq(i).attr("src", "/images/power_off.png");
						$(".ctlbtn").eq(i).click(function() {
							powerOnOff(item.name, 1);
						});
					}
				}
				hideLoading();
			}
		});
	}
		
	function powerOnOff(vmName, mode) {
		showLoading();
		$.ajax({
			type:"post",
			url:"/vcenter/powerOnOff.do",
			data:"vmName="+vmName+"&mode="+mode+"&t="+t,
			dataType:"json", 
			async:true,
			success:function(data) {
				var result = data.result;
				alert(result)
				hideLoading();
				showList();
			}
		});
	} 
	
	function restart(vmId) {
		showLoading();
		$.ajax({
			type:"post",
			url:"/vcenter/restart.do",
			data:"vmId="+vmId+"&t="+t,
			dataType:"json", 
			async:true,
			success:function(data) {
				var result = data.result;
				alert(result)
				hideLoading();
				showList();
			}
		});
	}
</script>
</head>
<body>
<jsp:include page="/common/navigation.jsp" /> 

<div id="main">
	<div id="content">
		<table id="common-table">
			<colgroup>
				<col width="35%" />
				<col width="20%" />
				<col width="15%" />
				<col width="30%" />
			</colgroup>
			<tr>
				<th>VM Name</th>
				<th>IP</th>
				<th>State</th>
				<th>Control</th>
			</tr>
			<tbody id="common-tbody">
			
			</tbody>
		</table>
	</div>
</div> 

</body>
</html>