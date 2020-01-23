<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<!DOCTYPE html>
<html>
<head>
<jsp:include page="/common/head.jsp" />

<script type="text/javascript">
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
			async:true,
			success:function(data) {
				list = data.list;
				for(var i = 0; i < list.length; i++) {
					var item = list[i];
					var str = "<tr>";
					str += "<td><img class='powerimg' src='' /> "+item.name+"</td>";
					str += "<td>"+item.powerState+"</td>";
					str += "<td class='ctlwrap'><span class='button ctlbtn' onclick='powerOnOff(\""+item.name+"\")'></span></td>";
					str += "</tr>";
					$("#common-tbody").append(str);
					
					if(item.powerState=="POWERED_ON") {
						$(".ctlbtn").eq(i).html("Turn Off")
						$(".powerimg").eq(i).attr("src", "/images/power_on.png");
					} else {
						$(".ctlbtn").eq(i).html("Turn On")
						$(".powerimg").eq(i).attr("src", "/images/power_off.png");
					}
				}
				hideLoading();
			}
		});
	}
		
	function powerOnOff(vmName) {
		showLoading();
		$.ajax({
			type:"post",
			url:"/vcenter/powerOnOff.do",
			data:"vmName="+vmName,
			dataType:"json", 
			async:true,
			success:function(data) {
				var result = data.result;
				alert(result)
				hideLoading();
			}
		});
	}
</script>

<style type="text/css">
	#common-table { width:100%; }
	#common-table th { font-weight:bold; font-size:16px; text-align:center; border:1px solid #dddddd; padding:10px; }
	#common-table td { text-align:left; color:#2e2d2f; font-size:14px; border-bottom:1px solid #dddddd; padding:10px; } 
	#common-table td .powerimg { position:relative; top:4px; width:20px; }
	#common-table td.ctlwrap { text-align:center; }
	#common-btn-area { margin:20px 0 20px 0; text-align:center; }
</style>
</head>
<body>
<jsp:include page="/common/navigation.jsp" /> 

<div id="main">
	<div id="content">
		<table id="common-table">
			<colgroup>
				<col width="60%" />
				<col width="20%" />
				<col width="20%" />
			</colgroup>
			<tr>
				<th>VM Name</th>
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