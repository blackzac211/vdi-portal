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
					str += "<td>"+(i+1)+"</td>";
					str += "<td>"+item.name+"</td>";
					str += "<td>"+item.powerState+"</td>";
					str += "<td><span class='button' onclick='powerOnOff(\""+item.name+"\")'>"+(item.powerState=="POWERED_ON"?"끄기":"켜기")+"</span></td>";
					str += "</tr>";
					$("#common-tbody").append(str);
					hideLoading();
				}
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
</head>
<body>
<jsp:include page="/common/navigation.jsp" /> 

<div id="main">
	<div id="content">
		<div id="common-title">
			VM List
			<span id="contact">
				Contact: Taehoon-Nam(1433), nacan@unist.ac.kr
			</span>
		</div>
		<table id="common-table">
			<colgroup>
				<col width="10%" />
				<col width="50%" />
				<col width="20%" />
				<col width="20%" />
			</colgroup>
			<tr>
				<th>No</th>
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