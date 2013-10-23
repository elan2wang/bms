var invoice_title_add_api = "/bmp/1/invoice_title/add";
var invoice_title_update_api = "/bmp/1/invoice_title/update";
var invoice_title_delete_api = "/bmp/1/invoice_title/destroy";
var invoice_title_view_api = "/bmp/1/invoice_title/view";
var invoice_title_list_api = "/bmp/1/invoice_title/list";
var invoice_title_switch_api = "/bmp/1/invoice_title/switch";

$(function(){
	// 页面加载时触发，加载发票抬头列表
	init(invoice_title_list_api);
	
	// 筛选
	$("#btn_invoice_title_filter").click(function(){
		var data = get_filter_data();
		init(invoice_title_list_api+"?"+data);
	});
	
	$("#btn_invoice_title_add").click(function(){
		$("#btn_invoice_title_add_submit").removeClass("hide");
		$("#btn_invoice_title_edit_submit").addClass("hide");
	});
	
	// 提交添加的请求，添加新的发票抬头
	$("#btn_invoice_title_add_submit").click(function(){
		$("#invoice_title_add_panel").modal("hide");
		bootbox.confirm("您确定要添加该发票抬头?", function(result) {
			if (result) {
				var title = $("#title").val();
				$.ajax({
					url: invoice_title_add_api,
					type: "post",
					data: "title="+title,
					dataType: "JSON",
					success: function(result) {
						var result_code = result.data.result_code;
						if(typeof(result_code) != "undefined" && parseInt(result_code) == 10000) {
							bootbox.alert(result.data.result_msg);
							// 刷新tab
							refresh_tab();
						}
					}
				});
			}
		});
	});
	
	// 提交修改发票抬头请求，更新发票抬头
	$("#btn_invoice_title_edit_submit").click(function(){
		$("#invoice_title_add_panel").modal("hide");
		bootbox.confirm("您确定要修改该发票抬头?", function(result) {
			if (result) {
				var id = $("#id").val();
				var title = $("#title").val();
				$.ajax({
					url: invoice_title_update_api,
					type: "post",
					data: "id="+id+"&title="+title,
					dataType: "JSON",
					success: function(result) {
						var result_code = result.data.result_code;
						if(typeof(result_code) != "undefined" && parseInt(result_code) == 10000) {
							bootbox.alert(result.data.result_msg);
							// 刷新tab
							refresh_tab();
						}
					}
				});
			}
		});
	});
});

function get_filter_data() {
	var data = '';
	if ($("#filter_enable").val() != "") {
		data += "&enable="+$("#filter_enable").val();
	}
	if ($("#filter_title").val() != "") {
		data += "&title="+$("#filter_title").val();
	}
	if (data.length > 0) {
		data = data.substring(1, data.length);
	}
	return data;
}

function init(url) {
	$("#invoice_title_list").html(loading);
	$.ajax({
		url: url,
		type: "get",
		data: {},
		dataType: "JSON",
		success: function (result) {
			if(typeof(result.error_code) != "undefined"){
				$("#invoice_title_list").html('<tr><td colspan="8" style="text-align:center;"><span style="color:red;">'+result.error_msg+'</span></td></tr>');
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
					var items = data.items;
					var titles = '';
					for (var i=0; i<items.length; i++) {
						var id = items[i].id;
						titles += '<tr>';
						titles += '<td>'+(i+1)+'</td>';
						titles += '<td>'+items[i].title+'</td>';
						var state, op_info, enable;
						if (items[i].enable == true) {
							state = '<i class="icon-ok-circle"></i>';
							op_info = '禁用该抬头';
							enable = false;
						} else {
							state = '<i class="icon-ban-circle"></i>';
							op_info = '启用该抬头';
							enable = true;
						}
						titles += '<td style="text-align:center;"><a class="edit" title="'+op_info+'" href="javascript:void(0);" onclick="switch_enable('+id+','+enable+');return false;" >'+state+'</a></td>';
						titles += '<td><a class="edit" title="编辑" href="javascript:void(0);" onclick="edit_invoice_title('+id+');return false;" ><i class="icon-pencil"></i></a>  ';
						titles += '<a class="edit" title="删除" href="javascript:void(0);" onclick="delete_invoice_title('+id+');return false;" ><i class="icon-trash"></i></a>';
						titles += '</td></tr>';
					}
					$("#invoice_title_list").html(titles);
				} else {
					$("#pagination").html("");
					$("#invoice_title_list").html(no_data);
				}
			}
		}
	});
}

function switch_enable(id, enable) {
	bootbox.confirm("您确定要更改可用性?", function(result) {
		if (result) {
			$.ajax({
				url: invoice_title_switch_api,
				type: "post",
				data: "id="+id+"&enable="+enable,
				dataType: "JSON",
				success: function(result) {
					var result_code = result.data.result_code;
					if (typeof(result_code) != "undefined" && parseInt(result_code) == 10000) {
						bootbox.alert(result.data.result_msg);
						// 刷新tab
						refresh_tab();
					}
				}
			});
		}
	});
}

function delete_invoice_title(id) {
	bootbox.confirm("您确定要删除该发票抬头么?", function(result) {
		if (result) {
			$.ajax({
				url: invoice_title_delete_api,
				type: "post",
				data: "id="+id,
				dataType: "JSON",
				success: function(result) {
					var result_code = result.data.result_code;
					if (typeof(result_code) != "undefined" && parseInt(result_code) == 10000) {
						bootbox.alert(result.data.result_msg);
						// 刷新tab
						refresh_tab();
					}
				}
			});
		}
	});
}

function edit_invoice_title(id) {
	$.ajax({
		url: invoice_title_view_api,
		type: "get",
		data: "id="+id,
		dataType: "JSON",
		success: function(result) {
			if(typeof(result.data.id) != "undefined"){
				$("#invoice_title_add_panel").modal("show");
				$("#btn_invoice_title_edit_submit").removeClass("hide");
				$("#btn_invoice_title_add_submit").addClass("hide");
				var title = result.data;
				$("#id").val(title.id);
				$("#title").val(title.title);
			}
		}
	});
}