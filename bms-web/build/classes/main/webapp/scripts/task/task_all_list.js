/**
 * author:	Elan Wang
 * email:	shohokh@gmail.com
 * create:	2013－08-23
 * 
 * this script is used by task_all_list.html
 */ 

var task_all_list_api = "/bmp/1/tasks/all_list";
var task_view_api = "/bmp/1/tasks/view";
var task_delete_api = "/bmp/1/tasks/delete";

$(function(){
	// 页面加载时执行，获取所有任务列表
	$("#filter_end_time").val(getCurTime());
	var data = get_filter_data();
	if (typeof(data) == "undefined") {
		init(task_all_list_api);
	} else {
		init(task_all_list_api+"?"+data);
	}
	
	// 点击筛选时执行
	$("#btn_task_filter").click(function(){
		var data = get_filter_data();
		init(task_all_list_api+"?"+data);
	});
	
	// 执行删除一条纪录
	$("#btn_delete_one").click(function(){
		var id = $("#task_id").val();
		doDelete(id, "one");
	});

	// 执行删除多条纪录
	$("#btn_delete_all").click(function(){
		var id = $("#task_id").val();
		doDelete(id, "all");
	});
	
	// 点击创建任务按钮
	$("#btn_task_add").click(function(){
		$.get("/bmp/views/task/task_add.html",{},function(result){
			$("#task_detail_panel").html(result);
			$("#task_detail_panel").attr("style", "width:320px;display:block;");
			$("#btn_task_edit_submit").addClass("hide");
			$("#btn_task_add_submit").removeClass("hide");
			$("#task_detail_panel").modal("show");
		});
	});
	
	// 详情面板隐藏后刷新列表
    $('#task_detail_panel').on('hidden', function () {
        //刷新列表
        refresh_tab();
    });
    
});

function get_filter_data() {
	var data = '';
	if ($("#filter_state").val() != "") {
		data += "&state="+$("#filter_state").val();
	}
	if ($("#filter_start_time").val() != "") {
		data += "&filter_start_time="+$("#filter_start_time").val();
	}
	if ($("#filter_end_time").val() != "") {
		data += "&filter_end_time="+$("#filter_end_time").val();
	}
	if ($("#filter_title").val() != "") {
		data += "&filter_title="+$("#filter_title").val();
	}
	if (data.length > 0) {
		data = data.substring(1, data.length);
	}
	return data;
}

/**
 * 加载列表数据
 * 
 * @param url 请求的API
 */
function init(url) {
	$("#task_all_list").html(loading);
	$.ajax({
		url: url,
		type:"get",
		data:{},
		dataType:"JSON",
		success:function(result){
			if(typeof(result.error_code) != "undefined"){
				$("#task_all_list").html('<tr><td colspan="8" style="text-align:center;"><span style="color:red;">'+result.error_msg+'</span></td></tr>');
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
					var tasks = result.data.items;
					var task_str = '';
					for(var i=0;i<tasks.length;i++){
						var task = tasks[i];
						task_str += '<tr>';
						task_str += '<td>'+(i+1)+'</td>';
						task_str += '<td>'+task.title+'</td>';
						task_str += '<td>'+task.creator_username+'</td>';
						task_str += '<td>'+task.start_time+'</td>';
						task_str += '<td>'+task.end_time+'</td>';
						task_str += '<td>'+get_periodicity(task.periodicity)+'</td>';
						task_str += '<td>'+get_state(task.state)+'</td>';
						task_str += '<td>';
						task_str += '<a class="edit" title="查看任务详情" href="javascript:void(0);" onclick="view_detail('+task.id+')"><i class="icon-eye-open"></i></a>  ';
						if (parseInt($("#uid").html()) == task.creator) {
							if (task.state != "finished") {
								task_str += '<a class="edit" title="编辑该任务" href="javascript:void(0);" onclick="edit_task('+task.id+')"><i class="icon-pencil"></i></a>  ';
							}
							task_str += '<a class="edit" title="删除任务" data-toggle="modal" href="javascript:void(0);" onclick="delete_task('+task.id+')"><i class="icon-trash"></i></a>';
						}
						task_str += '</tr>';
					}
					$("#task_all_list").html(task_str);
				} else {
					$("#pagination").html("");
					$("#task_all_list").html(no_data);
				}
			}
		},
	});
}

/**
 * 查看任务详情
 * 
 * @param id
 */
function view_detail(id) {
	$.get("/bmp/views/task/task_add.html",{},function(result){
		$("#task_detail_panel").html(result);
		$.ajax({
			url: task_view_api,
			type: "get",
			data: {task_id: id},
			dataType: "JSON",
			success: function(result) {
				if (typeof(result.data.id) != "undefined") {
					var task = result.data;
					$("#task_title").val(task.title);
					$("#remind_time").val(parseInt(task.remind_time));
					$("#periodicity").val(task.periodicity);
					$("#task_description").val(task.description);
					$("#creator").val(task.creator);
					$("#task_start_time").val(task.start_time);
					$("#task_end_time").val(task.end_time);
					var subtasks = task.subtasks;
					if (subtasks.length > 0) {
						subtask_panels = '';
						for (var i=0; i<subtasks.length; i++) {
							var start_time = subtasks[i].start_time;
							var end_time = subtasks[i].end_time;
							var title = subtasks[i].title;
							var owners = subtasks[i].owners;
							subtask_panels += add_subtask_panel(start_time, end_time, title, owners);
						}
						$("#subtask_list").html(subtask_panels);
					}
					$("#creator").parent().parent().removeClass("hide");
					$("#task_detail_panel").find("input").attr("disabled", true);
					$("#task_detail_panel").find("textarea").attr("disabled", true);
					$("#task_detail_panel").find("select").attr("disabled", true);
					$(".btn_group").remove();
					if ($("#subtask_list").children().length != 0) {
						$("#subtask_list").removeClass("hide");
						$("#task_detail_panel").attr("style", "width:720px;display:block;");
					}
					$("#task_detail_panel").modal("show");
				}
			}
		});
	});
}

/**
 * 点击编辑时触发，获得任务及其子任务的详细信息
 * 
 * @param id 任务编号
 */
function edit_task(id) {
	$.get("/bmp/views/task/task_add.html",{},function(result){
		$("#task_detail_panel").html(result);
		$.ajax({
			url: task_view_api,
			type: "get",
			data: {task_id: id},
			dataType: "JSON",
			success: function(result) {
				if (typeof(result.data.id) != "undefined") {
					var task = result.data;
					// 设置任务面板的信息
					$("#task_id").val(task.id);
					$("#task_gid").val(task.gid);
					$("#task_title").val(task.title);
					$("#remind_time").val(parseInt(task.remind_time));
					$("#periodicity").val(task.periodicity);
					$("#periodicity").attr("disabled", "disabled");
					$("#task_description").val(task.description);
					$("#creator").val(task.creator);
					$("#task_start_time").val(task.start_time);
					$("#task_end_time").val(task.end_time);
					// 控制任务面板的按钮
					$("#btn_task_edit_submit").attr("onclick", "task_edit_submit('one')");
					$("#btn_task_edit_submit").removeClass("hide");
					$("#btn_task_add_submit").addClass("hide");
					$("#subtask_add").removeClass("hide");
					// 加载子任务面板
					var subtasks = task.subtasks;
					if (subtasks.length > 0) {
						$.get("/bmp/views/task/subtask_add.html",{},function(result){
							for (var j=0; j<subtasks.length; j++) {
								$("#subtask_list").append(result);
							}
							// 设置子任务面板的信息
							var id_inputs = $("#subtask_list").find("input[name='subtask_id']");
							var gid_inputs = $("#subtask_list").find("input[name='subtask_gid']");
							var start_time_inputs = $("#subtask_list").find("input[name='start_time']");
							var end_time_inputs = $("#subtask_list").find("input[name='end_time']");
							var title_textareas = $("#subtask_list").find("textarea[name='title']");
							var owner_list = $("#subtask_list").find(".subtask_owner_list");
							for (var j=0; j<subtasks.length; j++) {
								$(id_inputs[j]).val(subtasks[j].id);
								$(gid_inputs[j]).val(subtasks[j].gid);
								$(start_time_inputs[j]).val(subtasks[j].start_time);
								$(end_time_inputs[j]).val(subtasks[j].end_time);
								$(title_textareas[j]).val(subtasks[j].title);
								var owners = subtasks[j].owners;
								if (owners != "undefined" && owners.length > 0) {
									var owners_str = '';
									for (var i=0; i<owners.length; i++) {
										owners_str += '<label class="checkbox" style="display: inline-block;"> <input name="owner" checked="checked" type="checkbox"';
										owners_str += 'style="margin: 4px 4px 0 0" value="'+owners[i].owner+'">'+owners[i].owner_name+'</label>';
									}
									$(owner_list[j]).html(owners_str);
								}
							}
							// 控制子任务面板的按钮
							$("button[name='btn_subtask_edit_submit']").removeClass("hide");
							$("button[name='btn_subtask_add_submit']").addClass("hide");
							$("button[name='btn_subtask_delete']").attr("onclick", "subtask_delete(this, 'one')");
							$("button[name='btn_subtask_add_submit']").attr("onclick", "subtask_add_submit(this, 'one')");
							$("button[name='btn_subtask_edit_submit']").attr("onclick", "subtask_edit_submit(this, 'one')");
							// 显示详情面板
							$("#subtask_list").removeClass("hide");
							$("#task_detail_panel").attr("style", "width:720px;display:block;");
							$("#task_detail_panel").modal("show");
						});
					} else {
						// 显示详情面板
						$("#subtask_add").removeClass("hide");
						$("#task_detail_panel").modal("show");
					}
				}
			}
		});
	});
}

function delete_task(id) {
	$("#task_delete_pannel").modal("show");
	$("#task_id").val(id);
}

/**
 * 执行任务删除
 * 
 * @param id
 * @param type
 */
function doDelete(id, type) {
	$.ajax({
		url: task_delete_api,
		type:"post",
		data:{task_id: id, type: type},
		dataType:"JSON",
		success:function(result){
			$("#task_delete_pannel").modal("hide");
			var result_code = result.data.result_code;
			if (typeof(result_code) != "undefined" && result_code == 10000) {
				alert(result.data.result_msg);
				// 刷新Tab
				refresh_tab();
			} else if (typeof(result.error_code) != "undefined") {
				alert(result.error_msg);
			} 
		}
	});
}

/**
 * 查看任务详情时调用，生成子任务信息面板HTML
 * 
 * @param start_time
 * @param end_time
 * @param title
 * @param owners
 * @returns {String}
 */
function add_subtask_panel(start_time, end_time, title, owners) {
	var subtask_panel = '';
	// title
	subtask_panel += '<div class="control-group margin-bottom-10"><div class="controls1">';
	if (title == "undefined") {
		subtask_panel += '<textarea style="width:350px" rows="2" placeholder="请填写子任务"></textarea></div></div>';
	} else {
		subtask_panel += '<textarea style="width:350px" rows="2" placeholder="请填写子任务">'+title+'</textarea></div></div>';
	}
	// time
	subtask_panel += '<div class="control-group margin-bottom-10">';
	subtask_panel += '<div class="controls1">';
	subtask_panel += '<div class="input-append date form_datetime">';
	if (start_time == "undefined") {
		subtask_panel += '<input class="input-135" type="text" size="16" value="">';
	} else {
		subtask_panel += '<input class="input-135" type="text" size="16" value="'+start_time+'">';
	}
	subtask_panel += '<span class="add-on"> <i class="icon-th"></i></span></div> - ';
	subtask_panel += '<div class="input-append date form_datetime">';
	if (end_time == "undefined") {
		subtask_panel += '<input class="input-135" type="text" size="16" value="">';
	} else {
		subtask_panel += '<input class="input-135" type="text" size="16" value="'+end_time+'">';	
	}
	subtask_panel += '<span class="add-on"> <i class="icon-th"></i></span></div></div></div>';
	// owners
	subtask_panel += '<div class="control-group margin-bottom-10"><div class="controls1">';
	subtask_panel += '<div class="controls" style="margin-left: 0">';
	subtask_panel += '<label class="checkbox"><i class="icon-group"></i></label>';
	if (owners != "undefined" && owners.length > 0) {
		for (var i=0; i<owners.length; i++) {
			var state = get_state(owners[i].state);
			var name = owners[i].owner_name;
			subtask_panel += '<label class="checkbox">'+ name + '(' + state +')</label>';
		}
	}
	subtask_panel += '</div></div></div><hr class="line">';
	return subtask_panel;
}