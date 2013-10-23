/**
 * author:	Elan Wang
 * email:	shohokh@gmail.com
 * create:	2013－08-23
 * 
 * this script is used by msg_list.html
 */ 

// APIs used by this script
var msg_list_api = "/bmp/1/messages";
var msg_view_api = "/bmp/1/messages/view";
var msg_read_api = "/bmp/1/messages/read";

/**
 * 页面加载时执行
 */
$(function(){
	var data = get_filter_data();
	if (typeof(data) == "undefined") {
		//加载消息列表
		init(msg_list_api);
	} else {
		//加载消息列表
		init(msg_list_api+"?"+data);
	}
	
	$("#btn_msg_filter").click(function(){
		var title = '';
		if ($("#filter_msg_type").val() == "") {
			title += "所有";
			if ($("#filter_state").val() != "") {
				title += $("#filter_state").find("option:selected").text();
			}
			title += "消息";
		} else {
			if ($("#filter_state").val() == "") {
				title += "所有";
			} else {
				title += $("#filter_state").find("option:selected").text();
			}
			title += $("#filter_msg_type").find("option:selected").text();
		}
		$("#legend").html(title);
		var data = get_filter_data();
		init(msg_list_api+"?"+data);
	});

	
});

function get_filter_data() {
	var data = '';
	if ($("#filter_state").val() != "") {
		data += "&state="+$("#filter_state").val();
	}
	if ($("#filter_msg_type").val() != "") {
		data += "&msg_type="+$("#filter_msg_type").val();
	}
	if (data.length > 0) {
		data = data.substring(1, data.length);
	}
	return data;
}

/**
 * 刷新页面时加载数据
 */ 
function init(url) {
	$("#msg_list").html(loading);
	$.ajax({
		url: url,
		type: "get",
		data: {},
		dataType: "JSON",
		success: function(result) {
			if(typeof(result.error_code) != "undefined"){
				$("#msg_list").html('<tr><td colspan="8" style="text-align:center;"><span style="color:red;">'+result.error_msg+'</span></td></tr>');
			}
			else if(typeof(result.data) != "undefined" && result.data){
				var data = result.data;
				var currentItemCount = data.currentItemCount;
				if (currentItemCount > 0) {
					// 设置分页信息
					var itemsPerPage = data.itemsPerPage;
					var currentPage = Math.ceil(data.startIndex/data.itemsPerPage);
					var totalPages = Math.ceil(data.totalItems/data.itemsPerPage);
					set_pagination(currentPage, totalPages, itemsPerPage, data.nextLink);
					// 封装列表信息
					var items = result.data.items;
					var msgs = wrap_messages(items);
					$("#msg_list").html(msgs);
				} else {
					$("#pagination").html("");
					$("#msg_list").html(no_data);
				}
			}
		}
	});
	
	$('#msg_detail_panel').on('hidden', function () {
        //alert("hidden");
        //刷新列表
        refresh_tab();
    });
}


/**
 * 将返回的消息数据封装成HTML
 * 
 * @param items 消息列表，JSON格式的对象数组
 * @returns 封装好的HTML代码
 */
function wrap_messages(items){//填充消息列表
	var list_str = '';
	if(items.length > 0){
		for(var i=0;i<items.length;i++){
			var id = items[i].id;
			var is_new = false;
			var new_tag = "";
			if(items[i].state == "new"){
				is_new = true;
				new_tag = '<span class="redFlag"><span>NEW</span></span>';
			}
			list_str += '<tr onclick="view_message_detail('+id+','+is_new+',\''+items[i].msg_type+'\')"><td>'+(i+1)+'</td>';
			list_str += '<td>'+get_msg_type(items[i].msg_type)+'</td>';
			list_str += '<td>'+items[i].msg_content+new_tag+'</td>';
			list_str += '<td>'+items[i].create_time+'</td></tr>';
		}
	}
	return list_str;
}

function view_message_detail(id, is_new, msg_type){
	if (is_new) {
		read_message(id, msg_type);
	}
	get_message_detail(id);
}

/**
 * 查看未读消息时，消息状态，并修网页head上的消息提醒
 * 
 * @param id 消息编号
 * @param msg_type 消息类型
 */
function read_message(id, msg_type) {
	$.ajax({
		url: msg_read_api,
		type: "post",
		data: {msg_id: id},
		dataType: "JSON",
		success: function(result) {
			if(typeof(result.data.result_code) != "undefined" && parseInt(result.data.result_code) == 10000){
				var msg_count = parseInt($(".msg_count").html());
				if (msg_count > 1) {
					msg_count = msg_count - 1;
				} else {
					msg_count = 0;
					$("#message_reminder_badge").addClass("hide");
				}
				$(".msg_count").html(msg_count);

				var specific_msg_count = parseInt($("#"+msg_type+"_count").html());
				specific_msg_count = specific_msg_count - 1;
				$("#"+msg_type+"_count").html(specific_msg_count);
			}
		}
	});
}

/**
 * 获取消息详情
 * 
 * @param id 消息编号
 */
function get_message_detail(id) {
	$.ajax({
		url: msg_view_api,
		type:"get",
		data:{msg_id:id},
		dataType:"JSON",
		success:function(result){
			if(typeof(result.error_code) != "undefined"){
				msg = result.error_msg;
			}
			else if(typeof(result.data) != "undefined" && result.data){
				var infos = result.data;
				var info_containers = $(".container");
				for(var i=0;i<info_containers.length;i++){
					var info_name = $(info_containers[i]).attr("name");
					if(typeof(infos[info_name]) != "undefined"){
						$(".container[name='"+info_name+"']").val(infos[info_name]);
					}
					if(info_name == "msg_type") {
						$(".container[name='"+info_name+"']").val(get_msg_type(infos[info_name]));
					}
				}
				$("#msg_detail_panel").modal("show");
			}
		},
		error:function(msg){

		}
	});
}