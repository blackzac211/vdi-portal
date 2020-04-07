<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<!DOCTYPE html>
<html>
<head>
<jsp:include page="/common/head.jsp" />

<link rel="stylesheet" href="/css/console/style.css" type="text/css" />
<link rel="stylesheet" href="/css/console/wmks-all.css"	type="text/css" />
<script	type="text/javascript" src="/js/console/wmks.min.js" charset="utf-8"></script>	

<script type="text/javascript">
	var vmId = "<%=request.getParameter("vmId")%>";

	if (!window.console) {
		console = {log: function() {} };
	}
	
	$(function() {
		function layout() {
			var w = $(window).width();
			var h = $(window).height();
			if(!wmks.isFullScreen()) {
				container.css({
					top: bar.outerHeight() + "px"
				});
				container.width(w).height(h - bar.outerHeight());
				wmks.updateScreen();
			} else {
				container.css({
					top: 0,
					left: 0
				});
				container.width(w).height(h);
			}
		}

		function showMessage(message) {
			container.html(message);
			bar.slideDown("fast", layout);
			spinner.hide();
		}

		function getKeyboardLayout() {
			var locale = "ko-KR".
			replace("-", "_");
			switch (locale) {
				case "de": case "de_DE":
					return "de-DE";
				case "de_CH":
					return "de-CH";
				case "ja": case "ja_JP":
					return "ja-JP_106/109";
				case "it": case "it_IT":
					return "it-IT";
				case "es": case "es_ES":
					return "es-ES";
				case "pt": case "pt_PT":
					return "pt-PT";
				case "fr": case "fr_FR":
					return "fr-FR";
				case "fr_CH":
					return "fr-CH";
				case "sv": case "sv_SE":
					return "sv_SE";
				case "en_UK":
					return "en_UK";
				case "en-UK":
					return "en_UK";	
				default:
					return "en-US";
			}
		}

		var bar = $("#bar");
		var cad = $("#cad");
		var container = $("#container");
		var fullscreen = $("#fullscreen");
		var keyboard = $("#keyboard");
		var spinner = $("#spinner");

		var wmks = WMKS.createWMKS("container", {
			keyboardLayoutId: getKeyboardLayout()
		});
		wmks.register(WMKS.CONST.Events.CONNECTION_STATE_CHANGE, function(evt, data) {
			switch (data.state) {
				case WMKS.CONST.ConnectionState.CONNECTING:
					console.log("The console is connecting");
					bar.slideUp("slow", layout);
					break;
				case WMKS.CONST.ConnectionState.CONNECTED:
					console.log("The console has been connected");
					spinner.hide();
					bar.slideDown("fast", layout);
					break;
				case WMKS.CONST.ConnectionState.DISCONNECTED:
					console.log("The console has been disconnected");
					showMessage("콘솔 연결이 끊어졌습니다. 이 창을 닫고 콘솔을 다시 시작하여 다시 연결하십시오.");
					break;
			}
		});
		wmks.register(WMKS.CONST.Events.ERROR, function(evt, data) {
			console.log("Error: " + data.errorType);
			location.href = "/files/certs.zip";
			var str = "<div>비정상적인 접근 입니다.</div>";
			str += "<div>신뢰할 수 있는 루트 인증 기관에 루트 인증서(certs.zip)를 추가하시기 바랍니다.</div>";
			str += "<br/><br/>";
			str += "<div>문의: 정보기술팀(1437), security@unist.ac.kr</div>";
			$("body").css("font-size", "30px");
			$("body").html(str);
		});
		wmks.register(WMKS.CONST.Events.REMOTE_SCREEN_SIZE_CHANGE, function(evt, data) {
			layout();
		});

		cad.on("click", function() {
			wmks.sendCAD();
		});

		if (wmks.canFullScreen()) {
			fullscreen.on("click", function (evt) {
				wmks.enterFullScreen();
			});
		} else {
			fullscreen.hide();
		}

		keyboard.on("click", function (evt) {
			var fixANSIEquivalentKeys = keyboard.data("toggle") === "true";
			var label = keyboard.html();
			wmks.setOption("fixANSIEquivalentKeys", !fixANSIEquivalentKeys);
			keyboard.html(keyboard.data("alt"));
			keyboard.data("toggle", !fixANSIEquivalentKeys);
			keyboard.data("alt", label);
		});

		//listen for window events
		$(window).on("resize", layout);
		
		// VM 티켓값 가져오기
		showLoading();
		var map = null;
		$.ajax({
			type:"post",
			url:"/vcenter/acquireMksTicket.do",
			data:"vmId="+vmId,
			dataType:"json", 
			async:true,
			success:function(data) {
				wmks.connect("wss://"+data.host+":"+data.port+"/ticket/" + data.ticket);
				hideLoading();
			}
		});
	});
</script>
</head>
<body>

<!-- Page bar -->
<div id="bar">
	<div id="buttonBar">
		<div class="buttonC">
			<button id="keyboard" data-toggle="false" data-alt="US 키보드 레이아웃 적용 중지">
			US 키보드 레이아웃 적용
			</button>
			<button id="fullscreen">
				전체 화면 보기
			</button>
			<button id="cad">
				Ctrl+Alt+Delete 보내기
			</button>
		</div>
	</div>
	<div id="vmName">
		<span id="vmTitle">${vmName}</span>
	</div>
</div>

<!-- WMKS container -->
<div id="container"></div>

<!-- Spinner markup -->
<div id="spinner">
	<div class="bar1"></div>
	<div class="bar2"></div>
	<div class="bar3"></div>
	<div class="bar4"></div>
	<div class="bar5"></div>
	<div class="bar6"></div>
	<div class="bar7"></div>
	<div class="bar8"></div>
	<div class="bar9"></div>
	<div class="bar10"></div>
	<div class="bar11"></div>
	<div class="bar12"></div>
</div>
</body>
</html>
