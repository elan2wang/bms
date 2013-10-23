/**
 * author:	Elan Wang
 * email:	shohokh@gmail.com
 * create:	2013－8-26
 * 
 * this script is cited by index.html
 * 
 */

var last_health = -1;
var health_timeout = 1000*60;

/**
 * 建立web socket连接，实时接收消息提醒
 * 
 * @param uid 用户ID
 */
function establish_socket(uid) {
	var url = "ws://localhost:8080/bmp/socket_msg?account="+uid;
	var MessageSocket = new WebSocket(url);

	//连接打开成功回调函数
	MessageSocket.onopen = function(event){
		heartbeat_timer = setInterval(function(){keepalive(MessageSocket);}, 1000*30 );
	};

	//错误发生
	MessageSocket.onerror = function(event){
		console.log("消息连接出错，为了能及时收到消息提醒，请重新登录");
	};
	
	MessageSocket.onclose = function(event){
		alert("您当前的消息连接已经超时，为了能及时收到消息提醒，请刷新页面或重新登录");
		window.location.href = "/bmp/views/login.html";
	};
	
	//接收到消息的回调函数
	MessageSocket.onmessage = function(event){
		// 如果接收到心跳信息
		if (event.data == 'pong') {
			console.log("receive pong");
			var now = new Date();
			last_health = now.getTime();
		} else { // 接收到消息
			var result = $.parseJSON(event.data);
			var msg_count = parseInt($(".msg_count").html());
			var task_msg_count = parseInt($("#task_msg_count").html());
			var payment_msg_count = parseInt($("#payment_msg_count").html());
			var income_msg_count = parseInt($("#income_msg_count").html());
			var sysadmin_msg_count = parseInt($("#sysadmin_msg_count").html());

			if(typeof(result.data) != "undefined" && result.data){
				var items = result.data.items;
				var count = result.data.msg_count;
				if (count > 0){
					msg_count += count;
					for(var i=0;i<count;i++){
						if (items[i].msg_type == "task_msg") {
							task_msg_count += 1;
						} else if (items[i].msg_type == "payment_msg") {
							payment_msg_count += 1;
						} else if (items[i].msg_type == "income_msg") {
							income_msg_count += 1;
						} else if (items[i].msg_type == "sysadmin_msg") {
							sysadmin_msg_count += 1;
						}
					}
				}
				if (msg_count > 0) {
					$(".msg_count").html(msg_count);
					$("#task_msg_count").html(task_msg_count);
					$("#payment_msg_count").html(payment_msg_count);
					$("#income_msg_count").html(income_msg_count);
					$("#sysadmin_msg_count").html(sysadmin_msg_count);
					$("#message_reminder_badge").removeClass("hide");
				} else {
					$("#message_reminder_badge").addClass("hide");
				}
			}
		}
	};
}

/**
 * 心跳检测函数，保持Socket在线
 * 
 * @param ws WebSocket对象
 */
function keepalive(ws){
	var time = new Date();
	if( last_health != -1 && ( time.getTime() - last_health > health_timeout ) ){
		alert("消息连接已断开，为确保及时收到消息提醒，请重新登录");
		ws.close();
	} else{
		ws.send( 'ping' );
	}
}