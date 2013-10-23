/**
 * author:	Elan Wang
 * email:	shohokh@gmail.com
 * create:	2013－8-21
 * 
 * this script is used by account_list.html
 */ 

// APIs used by this script
var department_list_api = "/bmp/1/departments";
var role_list_api = "/bmp/1/roles";
var account_list_api = "/bmp/1/accounts";
var account_assign_dep_api = "/bmp/1/accounts/assign_dep";
var account_assign_role_api = "/bmp/1/accounts/assign_role";
var account_add_api = "/bmp/1/accounts/add";
var account_update_api = "/bmp/1/accounts/update";
var account_delete_api = "/bmp/1/accounts/delete";
var account_switch_api = "/bmp/1/accounts/switch";
var account_view_api = "/bmp/1/accounts/view";
var account_reset_psd_api = "/bmp/1/accounts/reset";

$(function(){
	// 页面加载时触发，加载帐号列表
	init(account_list_api);

	// 筛选
	$("#btn_account_filter").click(function(){
		var data = get_filter_data();
		init(account_list_api+"?"+data);
	});
	
	//  点击新增帐号按钮时触发，加载部门列表
	$("#btn_account_add").click(function(){		
		$("#btn_account_add_submit").removeClass("hide");
		$("#btn_account_edit_submit").addClass("hide");
		load_departments("department");
	});

	// 新增帐号确认时触发，创建新帐号
	$("#btn_account_add_submit").click(function(){
		var username = $("#account_name").val();
		var email = $("#email").val();
		var mobile = $("#mobile").val();
		var department = $("#department").val();
		var password = md5("123456");
		$("#progress-bar").modal("show");
		$.ajax({
			url: account_add_api,
			type: "post",
			data: "username="+username+"&email="+email+"&mobile="+mobile+"&department="+
				  department+"&password="+password,
			dataType: "JSON",
			success: function(result) {
				$("#progress-bar").modal("hide");
				$("#account_add_panel").modal("hide");
				var result_code = result.data.result_code;
				if(typeof(result_code) != "undefined" && parseInt(result_code) == 10000) {
					bootbox.alert(result.data.result_msg);
					// 刷新Tab
					refresh_tab();
				} else if (typeof(result.error_code) != "undefined") {
					bootbox.alert(result.error_msg);
				}
			}
		});
	});

	// 编辑帐号确认时触发，更新帐号
	$("#btn_account_edit_submit").click(function(){
		var account_id = $("#account_id").val();
		var username = $("#account_name").val();
		var email = $("#email").val();
		var mobile = $("#mobile").val();
		var department = $("#department").val();
		$("#progress-bar").modal("show");
		$.ajax({
			url: account_update_api,
			type: "post",
			data: "account_id="+account_id+"&username="+username+"&email="+email+"&mobile="
				  +mobile+"&department="+department,
			dataType: "JSON",
			success: function(result) {
				$("#progress-bar").modal("hide");
				var result_code = result.data.result_code;
				if(typeof(result_code) != "undefined" && parseInt(result_code) == 10000) {
					$("#account_add_panel").modal("hide");
					bootbox.alert(result.data.result_msg);
					// 刷新Tab
					refresh_tab();
				} else if (typeof(result.error_code) != "undefined") {
					bootbox.alert(result.error_msg);
				}
			}
		});
	});
	
	// 分配角色时触发，保存
	$("#btn_save_role").click(function(){
		var roles = "";
		$("input[name='role[]']:checked").each(function(){
			roles += $(this).val()+",";
		});
		roles = roles.substr(0, roles.length-1);
		var account_id = $("#account_id").val();
		$.ajax({
			url: account_assign_role_api,
			type: "post",
			data: "account_id="+account_id+"&roles="+roles,
			dataType: "JSON",
			success: function(result) {
				var result_code = result.data.result_code;
				if (typeof(result_code) != "undefined" && result_code == 10000) {
					bootbox.alert(result.data.result_msg);
				} else if (typeof(result.error_code) != "undefined") {
					bootbox.alert(result.error_msg);
				}
			} 
		});
	});

	// 分配部门时触发，保存
	$("#btn_save_dep").click(function(){
		var deps = "";
		$("input[name='dep[]']:checked").each(function(){
			deps += $(this).val()+",";
		});
		deps = deps.substr(0, deps.length-1);
		var account_id = $("#account_id").val();
		$.ajax({
			url: account_assign_dep_api,
			type: "post",
			data: "account_id="+account_id+"&deps="+deps,
			dataType: "JSON",
			success: function(result) {
				var result_code = result.data.result_code;
				if (typeof(result_code) != "undefined" && result_code == 10000) {
					bootbox.alert(result.data.result_msg);
				} else if (typeof(result.error_code) != "undefined") {
					bootbox.alert(result.error_msg);
				}
			} 
		});
	});
	
});

function get_filter_data() {
	var data = '';
	if ($("#filter_account_enable").val() != "") {
		data += "&account_enable="+$("#filter_account_enable").val();
	}
	if ($("#filter_dep_id").val() != "") {
		data += "&dep_id="+$("#filter_dep_id").val();
	}
	if ($("#filter_username").val() != "") {
		data += "&username="+$("#filter_username").val();
	}
	if (data.length > 0) {
		data = data.substring(1, data.length);
	}
	return data;
}

function init(url) {
	$("#account_list").html(loading);
	var selected_dep = $("#filter_dep_id").val();
	load_departments("filter_dep_id", selected_dep);
	$.ajax({
		url: url,
		type: "get",
		data: {},
		dataType: "JSON",
		success: function (result) {
			if(typeof(result.error_code) != "undefined"){
				$("#auth_list").html('<tr><td colspan="8" style="text-align:center;"><span style="color:red;">'+result.error_msg+'</span></td></tr>');
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
					var accounts = '';
					for (var i=0; i<items.length; i++) {
						var id = items[i].account_id;
						var username = items[i].username;
						accounts += '<tr><td>'+(i+1)+'</td>';
						accounts += '<td>'+username+'</td>';
						accounts += '<td>'+items[i].email+'</td>';
						accounts += '<td>'+items[i].mobile+'</td>';
						accounts += '<td>'+items[i].department+'</td>';
						var state, op_info, enable;
						if (items[i].account_enable == true) {
							state = '<i class="icon-ok-circle"></i>';
							op_info = '禁用该用户';
							enable = false;
						} else {
							state = '<i class="icon-ban-circle"></i>';
							op_info = '启用该用户';
							enable = true;
						}
						accounts += '<td style="text-align:center;"><a class="edit" title="'+op_info+'" href="javascript:void(0);" onclick="switch_enable('+id+','+enable+');return false;" >'+state+'</a></td>';
						accounts += '<td><a class="edit" title="编辑用户信息" href="javascript:void(0);" onclick="edit_account('+id+');return false;" ><i class="icon-pencil"></i></a>  ';
						accounts += '<a class="edit" title="查看用户档案" href="javascript:void(0);" onclick="view('+id+');return false;" ><i class="icon-eye-open"></i></a>  ';
						accounts += '<a class="edit" title="重置用户密码" href="javascript:void(0);" onclick="reset_psd('+id+',\''+username+'\');return false;" ><i class="icon-key"></i></a>  ';
						accounts += '<a class="edit" title="删除该用户" href="javascript:void(0);" onclick="delete_account('+id+');return false;" ><i class="icon-trash"></i></a>  ';
						accounts += '<a class="edit" title="角色和部门分配" config" href="javascript:void(0);" onclick="assing_role_dep('+id+',\''+items[i].username+'\');return false;" data-toggle="modal"><i class="icon-certificate"></i></a>';
						accounts += '</td></tr>';
					}
					$("#account_list").html(accounts);
				} else {
					$("#pagination").html("");
					$("#account_list").html(no_data);
				}
			}
		}
	});
}

/**
 * 添加和编辑帐号时加载部门列表
 */
function load_departments(tag_id, my_dep) {
	$.ajax({
		url: department_list_api,
		type: "get",
		data: {itemsPerPage: 10000},
		dataType: "JSON",
		success: function(result) {
			//有责任人可以添加
			if(typeof(result.data.items) != "undefined" && result.data.items){
				var options = '';
				var items = result.data.items;
				for(var i=0;i<items.length;i++){
					if (my_dep != "undefined" && my_dep == items[i].dep_id) {
						options += '<option selected="selected" value="';
					} else {
						options += '<option value="';
					}
					options += items[i].dep_id;
					options += '">';
					options += items[i].dep_name;
					options += '</option>';
				}
				$("#"+tag_id).html(options);
				if (tag_id == "filter_dep_id") {
					$("#filter_dep_id").prepend('<option value="">所有部门</option>');
				}
			}
		}
	});
}

/**
 * 编辑帐号信息
 * 
 * @param id
 */
function edit_account(id) {
	$.ajax({
		url: account_view_api,
		type: "get",
		data: "account_id="+id,
		dataType: "JSON",
		success: function(result) {
			if(typeof(result.data.account_id) != "undefined"){
				$("#account_add_panel").modal("show");
				$("#btn_account_edit_submit").removeClass("hide");
				$("#btn_account_add_submit").addClass("hide");
				var account = result.data;
				$("#account_id").val(account.account_id);
				$("#account_name").val(account.username);
				$("#email").val(account.email);
				$("#mobile").val(account.mobile);
				load_departments("department", account.dep_id);
			}
		}
	});
}

/**
 * 查看用户的个人详细信息
 * 
 * @param id 帐号编号
 */
function view(id) {
	$.get("/bmp/views/admin/profile.html",{},function(result){
		$("#profile_panel").html(result);
		$.ajax({
			url: view_profile_api,
			type: "get",
			data: {account_id: id},
			dataType: "JSON",
			success: function(result) {
				if (typeof(result.error_code) != "undefined") {
					if (result.error_code == 20701) {
						bootbox.alert("该用户尚未填写个人信息!");
					} else {
						bootbox.alert(result.error_msg);
					}
					return;
				} else if (typeof(result.data.profile_id) != "undefined"){
					var profile = result.data;
					$("#realname").val(profile.realname);
					$("#age").val(profile.age);
					$("#nationality").val(profile.nationality);
					$("#language").val(profile.language);
					$("#gender").val(profile.gender);
					$("#birthday").val(profile.birthday);
					$("#idtype").val(profile.idtype);
					$("#idnum").val(profile.idnum);
					$("#department").val(profile.department);
					$("#position").val(profile.position);
					$("#address").val(profile.address);
					$("#description").val(profile.description);
					
					$("#btn_add_profile_submit").addClass("hide");
					$("#btn_edit_profile_submit").addClass("hide");
					$("#profile_panel").find("input").attr("disabled", true);
					$("#profile_panel").find("textarea").attr("disabled", true);
					$("#profile_panel").find("select").attr("disabled", true);
					$("#profile_panel").modal("show");
				}
			}
		});
	});
}

/**
 * 删除帐号
 * 
 * @param id 待删除帐号编号
 */
function delete_account(id) {
	bootbox.confirm("您确定要删除该用户么?", function(result) {
		if (result) {
			$.ajax({
				url: account_delete_api,
				type: "post",
				data: {account_id: id},
				dataType: "JSON",
				success: function(result) {
					var result_code = result.data.result_code;
					if (typeof(result_code) != "undefined" && result_code == 10000) {
						bootbox.alert(result.data.result_msg);
						// 刷新Tab
						refresh_tab();
					} else if (typeof(result.error_code) != "undefined") {
						bootbox.alert(result.error_msg);
					}
				}
			});
		}
	});
}

/**
 * 密码重置
 * 
 * @param id 帐号编号
 */
function reset_psd(id, username) {
	bootbox.confirm("您确定要重置["+username+"]的密码么?", function(result) {
		if (result) {
			$.ajax({
				url: account_reset_psd_api,
				type: "post",
				data: {account_id: id},
				dataType: "JSON",
				success: function(result) {
					var result_code = result.data.result_code;
					if (typeof(result_code) != "undefined" && result_code == 10000) {
						bootbox.alert(result.data.result_msg);
					} else if (typeof(result.error_code) != "undefined") {
						bootbox.alert(result.error_msg);
					}
				}
			});
		}
	});
}

/**
 * 启用／禁用帐号
 * 
 * @param id 帐号编号
 * @param enable true表示启用帐号，false表示禁用帐号
 */
function switch_enable(id, enable) {
	$.ajax({
		url: account_switch_api,
		type: "post",
		data: "account_id="+id+"&enable="+enable,
		dataType: "JSON",
		success: function(result) {
			var result_code = result.data.result_code;
			if (typeof(result_code) != "undefined" && result_code == 10000) {
				bootbox.alert(result.data.result_msg);
				// 刷新Tab
				refresh_tab();
			}
		}
	});
}

/**
 * 点击“分配角色和部门”时触发，加载角色和部门列表
 *  
 * @param id 帐号的编号
 */
function assing_role_dep(id, username) {
	$("#role_dep_assign_panel_header").html("分配部门和角色"+"["+username+"]");
	$("#role_dep_assign_panel").modal();
	$("#account_id").val(id);
	$.ajax({
		url: role_list_api,
		type: "get",
		data: {account_id: id, itemsPerPage: 10000},
		dataType: "JSON",
		success: function(result) {
			if(typeof(result.data.items) != "undefined" && result.data.items){
				//<label class="task-bor"><input type="checkbox" value="1" style="margin: -1px 5px 0;">门店经理</label>
				var roles1 = '', roles2 = '', roles3 ='';
				var items = result.data.items;
				for (var i=0; i<items.length; i++) {
					if (parseInt(items[i].role_level) == 1) {
						roles1 += '<label class="task-bor"><input type="checkbox" name="role[]" value="';
						roles1 += items[i].role_id+'" ';
						if (items[i].checked) {
							roles1 += 'checked="checked" ';
						}
						roles1 += 'style="margin: -1px 5px 0;">';
						roles1 += items[i].role_name;
						roles1 += '</label>';
					} else if (parseInt(items[i].role_level) == 2) {
						roles2 += '<label class="task-bor"><input type="checkbox" name="role[]" value="';
						roles2 += items[i].role_id+'" ';
						if (items[i].checked) {
							roles2 += 'checked="checked" ';
						}
						roles2 += 'style="margin: -1px 5px 0;">';
						roles2 += items[i].role_name;
						roles2 += '</label>';
					} else if (parseInt(items[i].role_level) == 3) {
						roles3 += '<label class="task-bor"><input type="checkbox" name="role[]" value="';
						roles3 += items[i].role_id+'" ';
						if (items[i].checked) {
							roles3 += 'checked="checked" ';
						}
						roles3 += 'style="margin: -1px 5px 0;">';
						roles3 += items[i].role_name;
						roles3 += '</label>';
					}
				}
				$("#role_list1").html(roles1);
				$("#role_list2").html(roles2);
				$("#role_list3").html(roles3);
			}
		}
	});
	$.ajax({
		url: department_list_api,
		type: "get",
		data: {account_id: id, itemsPerPage: 10000},
		dataType: "JSON",
		success: function(result) {
			if(typeof(result.data.items) != "undefined" && result.data.items){
				var deps1 = '', deps2 = '', deps3 ='';
				var items = result.data.items;
				for (var i=0; i<items.length; i++) {
					if (parseInt(items[i].dep_level) == 1) {
						deps1 += '<label class="task-bor"><input type="checkbox" name="dep[]" value="';
						deps1 += items[i].dep_id+'" ';
						if (items[i].checked) {
							deps1 += 'checked="checked" ';
						}
						deps1 += 'style="margin: -1px 5px 0;">';
						deps1 += items[i].dep_name;
						deps1 += '</label>';
					} else if (parseInt(items[i].dep_level) == 2) {
						deps2 += '<label class="task-bor"><input type="checkbox" name="dep[]" value="';
						deps2 += items[i].dep_id+'" ';
						if (items[i].checked) {
							deps2 += 'checked="checked" ';
						}
						deps2 += 'style="margin: -1px 5px 0;">';
						deps2 += items[i].dep_name;
						deps2 += '</label>';
					} else if (parseInt(items[i].dep_level) == 3) {
						deps3 += '<label class="task-bor"><input type="checkbox" name="dep[]" value="';
						deps3 += items[i].dep_id+'" ';
						if (items[i].checked) {
							deps3 += 'checked="checked" ';
						}
						deps3 += 'style="margin: -1px 5px 0;">';
						deps3 += items[i].dep_name;
						deps3 += '</label>';
					}
				}
				$("#dep_list1").html(deps1);
				$("#dep_list2").html(deps2);
				$("#dep_list3").html(deps3);
			}
		}
	});
}
