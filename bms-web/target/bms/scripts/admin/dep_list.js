/**
 * author:	Elan Wang
 * email:	shohokh@gmail.com
 * create:	2013－8-21
 * 
 * this script is used by account_list.html
 */ 

// APIs used by this script

var department_add_api = "/bmp/1/departments/add";
var department_update_api = "/bmp/1/departments/update";
var department_view_api = "/bmp/1/departments/view";
var department_delete_api = "/bmp/1/departments/destroy";
var all_department_api = "/bmp/1/departments/all_department";
var current_available_api= "/bmp/1/vaults/get_current_available_vaults_by_dep_id";
var assign_vaults_api = "/bmp/1/departments/assign_vaults";

$(function(){
	// 页面加载时触发，加载部门列表
	init(all_department_api);

	// 筛选
	$("#btn_department_filter").click(function(){
		var data = get_filter_data();
		init(all_department_api+"?"+data);
	});
	//表单验证
	$('#department_detail_form').validate({
		errorElement: 'label',
		errorClass: 'help-inline',
		rules: {
			dep_name:{
				required: true
			}
		},
		messages: {
			dep_name:{
				required: "请输入部门名称"
			}
		},
		highlight:function(element){
			$(element).closest('.control-group').removeClass('success').addClass('error');
		},
		success:function(element){
			element.closest('.control-group').removeClass('error').addClass('success');
		}
	});
	
	//  点击新增部门
	$("#btn_department_add").click(function(){		
		$("#btn_department_add_submit").removeClass("hide");
		$("#btn_department_edit_submit").addClass("hide");
		$("#dep_name").val("");
		$("#address").val("");
		$("#dep_level").val("0");
	});

	// 新增部门确认时触发，创建新部门
	$("#btn_department_add_submit").click(function(){
		if($("#department_detail_form").validate().form()){
			var dep_name = $("#dep_name").val();
			var address = $("#address").val();
			var dep_level = $("#dep_level").val();
			$("#progress-bar").modal("show");
			$.ajax({
				url: department_add_api,
				type: "post",
				data: "dep_name="+dep_name+"&address="+address+"&dep_level="+dep_level,
				dataType: "JSON",
				success: function(result) {
					$("#progress-bar").modal("hide");
					$("#department_add_panel").modal("hide");
					var result_code = result.data.result_code;
					if(typeof(result_code) != "undefined" && parseInt(result_code) == 10000) {
						bootbox.alert("新增部门成功");
						// 刷新Tab
						refresh_tab();
					} else if (typeof(result.error_code) != "undefined") {
						bootbox.alert(result.error_msg);
					}
				}
			});
		}
	});

	// 编辑部门确认时触发，更新部门
	$("#btn_department_edit_submit").click(function(){
		if($("#department_detail_form").validate().form()){
			var dep_id = $("#dep_id").val();
			var dep_name = $("#dep_name").val();
			var address = $("#address").val();
			var dep_level = $("#dep_level").val();
			$("#progress-bar").modal("show");
			$.ajax({
				url: department_update_api,
				type: "post",
				data: "dep_id="+dep_id+"&dep_name="+dep_name+"&address="+address+"&dep_level="
					  +dep_level,
				dataType: "JSON",
				success: function(result) {
					$("#progress-bar").modal("hide");
					var result_code = result.data.result_code;
					if(typeof(result_code) != "undefined" && parseInt(result_code) == 10000) {
						$("#department_add_panel").modal("hide");
						bootbox.alert("部门信息修改成功");
						// 刷新Tab
						refresh_tab();
					} else if (typeof(result.error_code) != "undefined") {
						bootbox.alert(result.error_msg);
					}
				}
			});
		}
	});

	// 分配账户时触发，保存
	$("#btn_save_dep_vault").click(function(){
		var vaults = "";
		$("input[name='vault[]']:checked").each(function(){
			vaults += $(this).val()+",";
		});
		vaults = vaults.substr(0, vaults.length-1);
		var dep_id = $("#dep_id").val();
		$.ajax({
			url: assign_vaults_api,
			type: "post",
			data: "dep_id="+dep_id+"&vaults="+vaults,
			dataType: "JSON",
			success: function(result) {
				var result_code = result.data.result_code;
				if (typeof(result_code) != "undefined" && result_code == 10000) {
					bootbox.alert("账户分配成功");
				} else if (typeof(result.error_code) != "undefined") {
					bootbox.alert(result.error_msg);
				}
			} 
		});
		
	});
	
});

//获取筛选项
function get_filter_data() {
	var data = '';
	if ($("#filter_dep_name").val() != "") {
		data += "&dep_name="+$("#filter_dep_name").val();
	}
	if ($("#filter_dep_level").val() != "") {
		data += "&dep_level="+$("#filter_dep_level").val();
	}
	if (data.length > 0) {
		data = data.substring(1, data.length);
	}
	return data;
}

//加载部门列表
function init(url) {
	$("#department_list").html(loading);
	$.ajax({
		url: url,
		type: "get",
		data: {},
		dataType: "JSON",
		success: function (result) {
			if(typeof(result.error_code) != "undefined"){
				$("#department_list").html('<tr><td colspan="4" style="text-align:center;"><span style="color:red;">'+result.error_msg+'</span></td></tr>');
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
					var departments = '';
					for (var i=0; i<items.length; i++) {
						departments += '<tr><td>'+items[i].dep_name+'</td>';
						departments += '<td>'+items[i].dep_level+'</td>';
						departments += '<td>'+items[i].address+'</td>';
						departments += '<td><a class="edit" href="javascript:void(0);" onclick="edit_department('+items[i].dep_id+');return false;" >编辑</a>';
						departments += '<a class="edit" href="javascript:void(0);" onclick="delete_department('+items[i].dep_id+');return false;" >删除</a>';
						departments += '<a class="edit config" href="javascript:void(0);" onclick="assign_vaults('+items[i].dep_id+',\''+items[i].dep_name+'\');return false;" data-toggle="modal">分配账户</a>';
						departments += '</td></tr>';
					}
					$("#department_list").html(departments);
				} else {
					$("#pagination").html("");
					$("#department_list").html(no_data);
				}
			}
		}
	});
}


/**
 * 编辑部门信息
 * 
 * @param dep_id
 */
function edit_department(dep_id) {
	$.ajax({
		url: department_view_api,
		type: "get",
		data: "dep_id="+dep_id,
		dataType: "JSON",
		success: function(result) {
			if(typeof(result.data.dep_id) != "undefined"){
				$("#department_add_panel").modal("show");
				$("#btn_department_edit_submit").removeClass("hide");
				$("#btn_department_add_submit").addClass("hide");
				var department = result.data;
				$("#dep_id").val(department.dep_id);
				$("#dep_name").val(department.dep_name);
				$("#address").val(department.address);
				$("#dep_level").val(department.dep_level);
			}
		}
	});
}

/**
 * 删除部门
 * 
 * @param id 待删除部门编号
 */
function delete_department(dep_id) {
	bootbox.confirm("删除该部门将删除和该部门相关的所有信息，确认删除?", function(result) {
		if (result) {
			$("#progress-bar").modal("show");
			$.ajax({
				url: department_delete_api,
				type: "post",
				data: {dep_id: dep_id},
				dataType: "JSON",
				success: function(result) {
					$("#progress-bar").modal("hide");
					var result_code = result.data.result_code;
					if (typeof(result_code) != "undefined" && result_code == 10000) {
						bootbox.alert("部门删除成功");
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
 * 点击“分配账号”时触发
 *  
 * @param
 */
function assign_vaults(dep_id, dep_name) {
	$("#department_vault_assign_panel_header").html("分配账户"+"["+dep_name+"]");
	$("#department_vault_assign_panel").modal();
	$("#dep_id").val(dep_id);
	$.ajax({
		url: current_available_api,
		type: "get",
		data: {dep_id: dep_id},
		dataType: "JSON",
		success: function(result) {
			if(typeof(result.data.items) != "undefined" && result.data.items){
				var vault_list = '';
				var items = result.data.items;
				for (var i=0; i<items.length; i++) {
					vault_list += '<label class="task-bor"><input type="checkbox" name="vault[]" value="';
					vault_list += items[i].v_number+'" ';
					if (items[i].checked) {
						vault_list += 'checked="checked"';
					}
					vault_list += 'style="margin: -1px 5px 0;">';
					vault_list += items[i].alias;
					vault_list += '</label>';
				}	
				$("#vault_list").html(vault_list);
			}
		}
	});
}
