/**
 * author:	Elan Wang
 * email:	shohokh@gmail.com
 * create:	2013－8-24
 * 
 * this script is used by task_add.html
 */ 

// APIs used by this script
var task_add_api = "/bmp/1/tasks/add";
var task_update_api = "/bmp/1/tasks/update";
var subtask_delete_api = "/bmp/1/subtasks/delete";
var subtask_add_api = "/bmp/1/subtasks/add";
var subtask_update_api = "/bmp/1/subtasks/update";
var account_list_api = "/bmp/1/accounts";

/**
 *  BEGIN 任务操作函数（add, update, delete, add_subtask） ========================================================
 */

$(function(){
	//提交新建的任务
	$("#btn_task_add_submit").click(function(){
		$("#progress-bar").modal("show");
		// 获取表单数据
		var data = '';
		data += "title="+$("#task_title").val();
		data += "&remind_time="+$("#remind_time").val();
		data += "&periodicity="+ $("#periodicity").val();
		if ($("periodicity").val() != "oncely") {
			data += "&expire_time=" + $("#expire_time").val();
		}
		data += "&description=" + $("#task_description").val();
		data += "&start_time=" + $("#task_start_time").val();
		data += "&end_time=" + $("#task_end_time").val();
		$.ajax({
			url: task_add_api,
			type: "post",
			data: data,
			dataType: "JSON",
			error: function(result) {
				$("#progress-bar").modal("hide");
				alert("创建任务失败");
			},
			success: function(result) {
				$("#progress-bar").modal("hide");
				if(typeof(result.error_code) != "undefined"){
					bootbox.alert(result.error_msg);
				} else if(typeof(result.data.id) != "undefined"){
					var task = result.data;
					bootbox.alert("任务添加成功");
					$("#periodicity").attr("disabled", "disabled");
					$("#task_start_time").attr("disabled", "disabled");
					$("#task_end_time").attr("disabled", "disabled");
					$("#expire_time").attr("disabled", "disabled");;
					$("#btn_task_edit_submit").removeClass("hide");
					$("#subtask_add").removeClass("hide");
					$("#btn_task_add_submit").addClass("hide");
					$("#task_id").val(task.id);
					$("#task_gid").val(task.gid);
				}
			}
		});
	});
	
});

function task_edit_submit(type) {
	$("#progress-bar").modal("show");
	// 获取表单数据
	var data = '';
	if (type == "all") {
		data += "task_gid="+$("#task_gid").val();
		data += "&type=all"; 
	} else if (type == 'one') {
		data += "task_id="+$("#task_id").val();
		data += "&type=one";
	}
	data += "&title="+$("#task_title").val();
	data += "&remind_time="+$("#remind_time").val();
	data += "&description=" + $("#task_description").val();
	data += "&start_time=" + $("#task_start_time").val();
	data += "&end_time=" + $("#task_end_time").val();
	$.ajax({
		url: task_update_api,
		type: "post",
		data: data,
		dataType: "JSON",
		error: function(result) {
			$("#progress-bar").modal("show");
			bootbox.alert("修改任务失败");
		},
		success: function(result) {
			$("#progress-bar").modal("hide");
			if(typeof(result.error_code) != "undefined"){
				bootbox.alert(result.error_msg);
			} else if(typeof(result.data.id) != "undefined"){
				bootbox.alert("修改任务成功");
			}
		}
	});
}

function periodicity_change(object) {
	if ($(object).val() == "oncely") {
		$("#expire_time_div").addClass("hide");
	} else {
		$("#expire_time_div").removeClass("hide");
	}
}


/**
 *  END 任务操作函数（add, update, delete, add_subtask）============================================================
 */

/**
 *  BEGIN 子任务操作函数（add, update, delete） ====================================================================
 */

function subtask_add_one_more() {
	$.get("/bmp/views/task/subtask_add.html",{},function(result){
		if ($("#subtask_list").children().length == 0) {
			$("#task_detail_panel").attr("style", "width:720px;display:block;");
			$("#subtask_list").removeClass("hide");
		}
		$("#subtask_list").prepend(result);
	});
}

function get_owners(object) {
	var owners = '';
	$(object.form).find("input[name='owner']:checked").each(function(){
		owners += $(this).val()+",";
	});
	if (owners.length > 0) {
		owners = owners.substr(0, owners.length-1);	
	}
	return owners;
}

function subtask_add_submit(object, type) {
	$("#progress-bar").modal("show");
	//获取表单数据
	var data = '';
	var owners = get_owners(object);
	var formparams = $(object.form).serialize();
	var periodicity= $("#periodicity").val();
	
	data += formparams+"&periodicity="+periodicity;
	data += "&owners="+owners;
	if (type == 'all') {
		data += "&task_gid="+$("#task_gid").val()+"&type=all";
	} else if (type == 'one'){
		data += "&task_id="+$("#task_id").val()+"&type=all";
	} else {
		alert("参数错误，type可以为all或one");
		return;
	}
	
	//提交POST请求
	$.ajax({
		url: subtask_add_api,
		type: "post",
		data: data,
		dataType: "JSON",
		error: function(result) {
			$("#progress-bar").modal("hide");
			bootbox.alert("创建子任务失败");
		},
		success: function(result) {
			$("#progress-bar").modal("hide");
			if(typeof(result.data.id) != "undefined"){
				bootbox.alert("创建子任务成功");
				var subtask = result.data;
				$(object.form).find("input[name='subtask_gid']").val(subtask.gid);
				$(object).addClass("hide");
				$(object).next().removeClass("hide");
			} else if(typeof(result.error_code) != "undefined"){
				bootbox.alert(result.error_msg);
			}
		}
	});
}

function subtask_edit_submit(object, type) {
	$("#progress-bar").modal("show");
	//获取表单数据
	var owners = get_owners(object);
	var formparams = $(object.form).serialize();
	var data = formparams+"&owners="+owners;
	if (type == "all") {
		data += "&type=all";
		data += "&periodicity="+$("#periodicity").val();
	} else if (type == "one"){
		data += "&type=one";
	} else {
		alert("参数错误，type可以为all或one");
		return;
	}
	//提交POST请求
	$.ajax({
		url: subtask_update_api,
		type: "post",
		data: data,
		dataType: "JSON",
		error: function(result) {
			$("#progress-bar").modal("hide");
			bootbox.alert("修改子任务失败");
		},
		success: function(result) {
			$("#progress-bar").modal("hide");
			if(typeof(result.data.id) != "undefined"){
				bootbox.alert("修改子任务成功");
			} else if(typeof(result.error_code) != "undefined"){
				bootbox.alert(result.error_msg);
			}
		}
	});
}


function subtask_delete(object, type) {
	var data = '';
	if (type == "all") {
		var subtask_gid = $(object.form).find("input[name='subtask_gid']").val();
		if (subtask_gid == "undefined" || subtask_gid == "") {
			$(object.form).remove();
			// if no subtask remain
			if ($("#subtask_list").children().length == 0) {
				$("#task_detail_panel").attr("style", "width:320px;display:block;");
				$("#subtask_list").addClass("hide");
			}
			return;
		}
		data += "subtask_gid="+subtask_gid+"&type=all";
	} else if (type == "one") {
		var subtask_id = $(object.form).find("input[name='subtask_id']").val();
		if (subtask_id == "undefined" || subtask_id == "") {
			$(object.form).remove();
			// if no subtask remain
			if ($("#subtask_list").children().length == 0) {
				$("#task_detail_panel").attr("style", "width:320px;display:block;");
				$("#subtask_list").addClass("hide");
			}
			return;
		}
		data += "subtask_id="+subtask_id+"&type=one";
	} else {
		alert("参数错误，type可以为all或one");
	}
	
	if (data != '') {
		$.ajax({
			url: subtask_delete_api,
			type: "post",
			data: data,
			dataType: "JSON",
			success: function (result) {
				if (typeof(result.data.result_code) != "undefined") {
					alert("子任务删除成功");
					$(object.form).remove();
					if ($("#subtask_list").children().length == 0) {
						$("#task_detail_panel").attr("style", "width:320px;display:block;");
						$("#subtask_list").addClass("hide");
					}
				}
			}

		});
	}
}

/**
 *  END 子任务操作函数（add, update, delete） ======================================================================
 */


/**
 *  BEGIN 指派责任人相关的函数 =====================================================================================
 */

function assign_owner(object) {
	var form = $(object).parent().parent().parent().parent();
	form.find(".owner_assign_panel").modal("show");
	form.find("input[name='select_all']").prop("checked", false);
	$.ajax({
		url: account_list_api,//加载责任人的接口
		type:"get",
		cache: false,
		data:{},
		dataType:"JSON",
		success:function(result){
			//有责任人可以添加
			if(typeof(result.data.items) != "undefined" && result.data.items){
				var list_str = '';
				var items = result.data.items;
				for(var i=0;i<items.length;i++){
					var id = items[i].account_id;
					list_str += '<tr>';
					list_str += '<td><input type="checkbox" name="account" value="'+id+'" onclick="verify_check_all(\'account\',\'select_all\'\)" /></td>';
					list_str += '<td>'+items[i].username+'</td>';
					list_str += '<td>'+items[i].department+'</td>';
					list_str += '</tr>';
				}
				form.find(".account_list").html(list_str);
			}
			else{
				form.find(".account_list").html("<tr><td style='text-align:center;' colspan='4'>暂时无人可指派</td></tr>");
			}
		}
	});
}

function assign_owner_submit(object) {
	var owners = '';
	$(object.form).find("input[name='account']:checked").each(function(){
		owners += '<label class="task-bor" style="display: inline-block;"> <input name="owner" checked="checked" type="checkbox" style="margin: -1px 5px 0;" value="';
		owners += $(this).val();
		owners += '">';
		owners += $(this).parent().next().html();
		owners += '</label>';
	});
	$(object.form).find(".subtask_owner_list").html(owners);
	$(object.form).find(".owner_assign_panel").modal("hide");
}

function close_owners_panel(object) {
	$(object.form).find(".owner_assign_panel").modal("hide");
}
/**
 *  END 指派责任人相关的函数 =======================================================================================
 */

