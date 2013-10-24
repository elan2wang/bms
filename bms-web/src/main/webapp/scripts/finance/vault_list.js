/**
 * author:	Elan Wang
 * email:	shohokh@gmail.com
 * create:	2013－8-21
 * 
 * this script is used by role_list.html
 */ 

// APIs used by this script
var vault_add_api = "/bmp/1/vaults/add";
var vault_update_api = "/bmp/1/vaults/update";
var vault_delete_api = "/bmp/1/vaults/destroy";
var vault_view_api = "/bmp/1/vaults/view";
var vault_all_api = "/bmp/1/vaults/all_vaults";
var switch_enable_api = "/bmp/1/vaults/switch_vault_enable";

$(function(){
	// 页面加载时触发，加载角色列表
	init(vault_all_api);
	
	// 筛选
	$("#btn_vault_filter").click(function(){
		var data = get_filter_data();
		init(vault_all_api+"?"+data);
	});
	$.validator.addMethod("checkBank",function(value){
		if($("#type").val() == "bank" && value.length > 0){
			return true;
		}
		return false;
	});
	$("#vault_detail_form").validate({
		onsubmit: true,
		errorElement: 'label',
		errorClass: 'help-inline',
		rules: {
			v_number:{
				required: true,
				digits: true
			},
			alias:{
				required: true
			},
			card_bank: {
				checkBank: true
			},
			card_owner: {
				checkBank: true
			}
		},
		messages: {
			v_number:{
				required: "请填写卡号",
				digits: "格式错误"
			},
			alias: {
				required: "请填写别称"
			},
			card_bank: {
				checkBank: "请填写开户行"
			},
			card_owner: {
				checkBank: "请填写户名"
			}
		},
		highlight:function(element){
			$(element).closest('.control-group').removeClass('success').addClass('error');
		},
		unhighlight:function(element){
			$(element).closest('.control-group').removeClass('error');
		}
	});
	
	$("#btn_vault_add").click(function(){
		$(".control-group").removeClass("error");//取消控件错误标示
		$(".help-inline").hide();//取消控件错误标示
		$("#btn_vault_edit_submit").addClass("hide");
		$("#btn_cancel_update").addClass("hide");
		$("#btn_vault_add_submit").removeClass("hide");
		$("#btn_reset").removeClass("hide");
		$("#v_number").val("");
		$("#v_number").attr("disabled", false);
		$("#type").attr("disabled", false);
		$("#alias").val("");
		$("#card_bank").val("");
		$("#card_owner").val("");
		$("#comment").val("");
		$("#vault_info_panel").modal("show");
	});
	
	// 提交创建的请求，创建新账户
	$("#btn_vault_add_submit").click(function(){
		if($("#vault_detail_form").validate().form()){
			$("#vault_info_panel").modal("hide");
			var data= '';
			var v_number = $("#v_number").val();
			var type = $("#type").val();
			var alias = $("#alias").val();
			var card_bank = $("#card_bank").val();
			var card_owner = $("#card_owner").val();
			var comment = $("#comment").val();
			data += "v_number="+v_number+"&type="+type+"&alias="+alias+"&card_bank="+card_bank
			+"&card_owner="+card_owner+"&comment="+comment;
			$("#progress-bar").modal("show");
			$.post(vault_add_api,data,function(result){
				$("#progress-bar").modal("hide");
				var msg = "";
				if(typeof(result.error_code) != "undefined"){
					msg = result.error_msg;
				}
				else if(typeof(result.data.result_code) != "undefined" && parseInt(result.data.result_code) == 10000){
					msg = result.data.result_msg;
					$("#vault_info_panel").modal("hide");
					var cur_url = $("#pagination ul li.active a").prop("href");//获得当前页列表的url
					init(cur_url);//重新加载列表
				}
				bootbox.alert(msg);
			},"JSON");
		}
	});
	
	// 提交修改账户信息请求，更新账户
	$("#btn_vault_edit_submit").click(function(){
		if($("#vault_detail_form").validate().form()){
			bootbox.confirm("确认修改？", function(result){
				if(result){
					$("#progress-bar").modal("show");
					var v_number = $("#v_number").val();
					var type = $("#type").val();
					var alias = $("#alias").val();
					var card_bank = $("#card_bank").val();
					var card_owner = $("#card_owner").val();
					var comment = $("#comment").val();
					var data = "v_number="+v_number+"&type="+type+"&alias="+alias+"&card_bank="+card_bank
					+"&card_owner="+card_owner+"&comment="+comment;
					$.post(vault_update_api,data,function(result){
						$("#progress-bar").modal("hide");
						var msg = "";
						if(typeof(result.error_code) != "undefined"){
							msg = result.error_msg;
						}
						else if(typeof(result.data.result_code) != "undefined" && parseInt(result.data.result_code) == 10000){
							msg = result.data.result_msg;
							$("#vault_info_panel").modal("hide");
							var cur_url = $("#pagination ul li.active a").prop("href");//获得当前页列表的url
							init(cur_url);//重新加载列表
						}
						bootbox.alert(msg);
					},"JSON");
				}
			});
		}
	});
	
	//取消修改
	$("#btn_cancel_update").click(function(){
		var v_number = $("#v_number").val();
		edit_vault(v_number);
	});
	
	//重置
	$("#btn_reset").click(function(){
		$("#v_number").val("");
		$("#alias").val("");
		$("#card_bank").val("");
		$("#card_owner").val("");
		$("#comment").val("");
	});
	
	App.init();//app.js的应用初始化
});

function get_filter_data() {
	var data = '';
	if ($("#filter_type").val() != "") {
		data += "&type="+$("#filter_type").val();
	}
	if ($("#filter_v_number").val() != "") {
		data += "&v_number="+$("#filter_v_number").val();
	}
	if ($("#filter_alias").val() != "") {
		data += "&alias="+$("#filter_alias").val();
	}
	if (data.length > 0) {
		data = data.substring(1, data.length);
	}
	return data;
}

/**
 * 加载数据列表
 * 
 * @param url 请求API
 */
function init(url) {
	$("#vault_list").html(loading);
	$.ajax({
		url: url,
		type: "get",
		data: {},
		dataType: "JSON",
		success: function (result) {
			if(typeof(result.error_code) != "undefined"){
				$("#vault_list").html('<tr><td colspan="7" style="text-align:center;"><span style="color:red;">'+result.error_msg+'</span></td></tr>');
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
					var vaults = '';
					for (var i=0; i<items.length; i++) {
						vaults += '<tr><td>'+(i+1)+'</td>';
						vaults += '<td>'+items[i].v_number+'</td>';
						vaults += '<td>'+items[i].alias+'</td>';
						if(items[i].type == 'bank'){
							vaults += '<td>银行账户</td>';
						}
						if(items[i].type == 'vault'){
							vaults += '<td>保险箱</td>';
						}
						vaults += '<td>'+items[i].card_bank+'</td>';
						vaults += '<td>'+items[i].card_owner+'</td>';
						vaults += '<td>'+items[i].comment+'</td>';
						var state, op_info, enable;
						if (items[i].vault_enable == true) {
							state = '<i class="icon-ok-circle"></i>';
							op_info = '禁用该账户';
							enable = false;
						} else {
							state = '<i class="icon-ban-circle"></i>';
							op_info = '启用该账户';
							enable = true;
						}
						vaults += '<td style="text-align:center;"><a class="edit" title="'+op_info+'" href="javascript:void(0);" onclick="switch_enable(\''+items[i].v_number+'\','+enable+');return false;" >'+state+'</a></td>';
						vaults += '<td><a class="edit" href="javascript:void(0);" title="编辑账户信息" onclick="edit_vault(\''+items[i].v_number+'\')";return false;"><i class="icon-pencil"></i></a> ';
						vaults += '<a class="edit" href="javascript:void(0);" title="删除账户" onclick="delete_vault(\''+items[i].v_number+'\')";return false;"><i class="icon-trash"></i></a>';
						vaults += '</td></tr>';
					}
					$("#vault_list").html(vaults);
				} else {
					$("#pagination").html("");
					$("#vault_list").html(no_data);
				}
			}
		}
	});
}

/**
 * 删除账号
 * 
 * @param v_number 账号编号
 */
function delete_vault(v_number) {
	bootbox.confirm("您确定要删除该账号么?", function(result) {
		if (result) {
			$("#progress-bar").modal("show");
			$.ajax({
				url: vault_delete_api,
				type: "post",
				data: "v_number="+v_number,
				dataType: "JSON",
				success: function(result) {
					$("#progress-bar").modal("hide");
					var result_code = result.data.result_code;
					if (typeof(result_code) != "undefined" && result_code == 10000) {
						bootbox.alert(result.data.result_msg);
						// 刷新tab
						refresh_tab();
					}
				}
			});
			
		}
	});
}

/**
 * 点击编辑按钮触发，获取账号信息
 * 
 * @param v_number 账号编号
 */
function edit_vault(v_number) {
	$.ajax({
		url: vault_view_api,
		type: "get",
		data: "v_number="+v_number,
		dataType: "JSON",
		success: function(result) {
			if(typeof(result.data.v_number) != "undefined"){
				var vault = result.data;
				$("#v_number").attr("disabled", true);
				$("#type").attr("disabled", true);
				if(vault.type === "bank"){
					$("#v_number").val(vault.v_number);
					$("#alias").val(vault.alias);
					$("#type").val(vault.type);
					$("#card_bank").val(vault.card_bank);
					$("#card_owner").val(vault.card_owner);
					$(".bank_control").show();
				}else{
					$(".bank_control").hide();
					$("#v_number").val(vault.v_number);
					$("#alias").val(vault.alias);
					$("#type").val(vault.type);
				}
				$(".control-group").removeClass("error");//取消控件错误标示
				$(".help-inline").hide();//取消控件错误标示
				$("#btn_vault_edit_submit").removeClass("hide");
				$("#btn_cancel_update").removeClass("hide");
				$("#btn_vault_add_submit").addClass("hide");
				$("#btn_reset").addClass("hide");
				$("#vault_info_panel").modal("show");
			}
		}
	});
	
}

/**
 * 禁用启用账户
 */
function switch_enable(v_number, enable) {
	$.ajax({
		url: switch_enable_api,
		type: "post",
		data: "v_number="+v_number+"&enable="+enable,
		dataType: "JSON",
		success: function(result) {
			var result_code = result.data.result_code;
			if (typeof(result_code) != "undefined" && result_code == 10000) {
				bootbox.alert(result.data.result_msg);
				// 刷新tab
				refresh_tab();
			}
		}
	});
}


/**
 * 根据账户类型控制表单
 * @param value
 */
function switch_bank_control(value){
	if(value === "bank"){
		$(".bank_control").show();
	}
	else{
		$(".bank_control").hide();
	}
}
