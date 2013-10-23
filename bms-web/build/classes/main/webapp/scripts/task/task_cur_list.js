/**
 * author:	Jie Shang
 * email:	741914101@qq.com
 * create:	2013－08-21
 * modified: 2013-8-23 Elan Wang
 * 
 * this script is used by task_cur_list.html
 */ 
var task_current_list_api = "/bmp/1/tasks/current_list";
var task_finish_api = "/bmp/1/tasks/finish";
var task_confirm_api = "/bmp/1/tasks/confirm";

$(function(){
	// 页面加载时，加载当前任务列表
	init(task_current_list_api+"?itemsPerPage=4");

	// 完成任务时，提交备注
	$("#btn_finish_subtask_submit").click(function(){
		var subtask_id = $("#subtask_id").val();
		var comment = $("#comment").val();
		$("#finish_subtask_pannel").modal('hide');
		$.ajax({
			url: task_finish_api,
			type:"post",
			data:{subtask_id:subtask_id,comment:comment},
			dataType:"JSON",
			success:function(result){
				if(typeof(result.data) != "undefined" && parseInt(result.data.result_code) == 10000){
					alert("提交成功");
					// 刷新列表
					refresh_tab();
				} else if(typeof(result.error_code) != "undefined"){
					alert(result.error_msg);
				}
			},
			error:function(msg){
				alert("请求提交失败");
			}
		});
	});
});

/**
 * 加载列表数据
 * 
 * @param url 请求的API
 */
function init(url) {
	$("#task_current_list").html(loading);
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
					var tasks = data.items;
					var fieldset_str = wrap_current_tasks(tasks);
					$("#task_current_list").html(fieldset_str);
				} else {
					$("#pagination").html("");
					$("#task_current_list").html('<h2>您的任务都已经完成了</h2>');
				}
			}
		},
	});
}
/**
 * 根据返回的Task数据，封装成HTML页面
 * 
 * @param tasks 返回的当前任务列表
 * @returns {String} 拼接好的HTML代码
 */
function wrap_current_tasks(tasks){
	var fieldset_str = '';
	if(tasks.length > 0){
		for(var i=0;i<tasks.length;i++){
			var need_confirm_btn = false;
			var task_id = tasks[i].id;
			var task_title = tasks[i].title;
			var task_start_time = tasks[i].start_time;
			fieldset_str += '<fieldset>';
			fieldset_str += '<legend>'+task_title;
			fieldset_str += '<span class="pull-right"><a title="任务开始时间" class="f14">'+task_start_time+'</a></span></legend>';
			if(typeof(tasks[i].subtasks) != "undefined" && tasks[i].subtasks){
				var subtasks = tasks[i].subtasks;
				fieldset_str += '<table class="table table-hover table-bordered">';
				fieldset_str += '<thead><tr><th>#</th><th>子任务</th><th>责任人</th><th>开始时间</th><th>结束时间</th><th>当前状态</th></tr></thead><tbody>';
				for(var j=0;j<subtasks.length;j++){
					var need_finish_btn = false;
					var subtask = subtasks[j];
					var sub_title = subtask.title;
					var sub_start_time = subtask.start_time;
					var sub_end_time = subtask.end_time;
					var subtask_state = get_state(subtask.state);//子任务状态
					fieldset_str += '<tr>';
					fieldset_str += '<td>'+(j+1)+'</td>';
					fieldset_str += '<td>'+sub_title+'</td>';
					fieldset_str += '<td>';
					if(typeof(subtask.owners) != "undefined" && subtask.owners){
						var owners = subtask.owners;
						for(var k=0;k<owners.length;k++){
							var owner = owners[k];
							var owner_name = owner.name;
							var owner_state = get_state(owner.state);
							fieldset_str += owner_name+'('+owner_state+')';
							if (parseInt($("#uid").html()) == owner.id) {
								if (owner.state == "not_start") {
									need_confirm_btn = true;
								}
								if (owner.state == "confirmed") {
									need_finish_btn = true;
								}
							}
						}
					}
					fieldset_str += '</td>';
					fieldset_str += '<td>'+sub_start_time+'</td>';
					fieldset_str += '<td>'+sub_end_time+'</td>';
					if (need_finish_btn) {
						fieldset_str += '<td><button type="submit" class="btn btn-success" id="btn_finish_subtask_'+subtask.id+'" onclick="finish_subtask('+subtask.id+')">完成</button></div>';
					} else {
						fieldset_str += '<td>'+subtask_state;
					}
					fieldset_str += '</td></tr>';
				}
				fieldset_str += '</tbody></table>';
			}
			if (need_confirm_btn) {
				fieldset_str += '<div class="controls" style=" margin-left:0">';
				fieldset_str += '<button type="button" class="btn btn-success" id="btn_confirm_task_'+task_id+'" onclick="corfirm_task('+task_id+')">确认</button></div>';
			}
			fieldset_str += '</fieldset>';
		}
	}
	return fieldset_str;
}

/**
 * 确认收到任务
 * 
 * @param task_id 任务编号
 */
function corfirm_task(task_id){
	$.ajax({
		url: task_confirm_api,//确认任务接口
		type: "post",
		data:{task_id:task_id},
		dataType:"JSON",
		success:function(result){
			if(typeof(result.error_code) !== "undefined"){
				alert("确认失败");
			} else if(typeof(result.data.result_code) != "undefined" && parseInt(result.data.result_code) == 10000){
				alert("您已成功确认该任务，请及时执行");
				// 刷新列表
				refresh_tab();
			}
		},
		error:function(msg){
			alert("请求提交失败");
		}
	});
}

/**
 * 完成子任务
 * 
 * @param task_id
 */
function finish_subtask(subtask_id){
	$("#subtask_id").val(subtask_id);
	$("#finish_subtask_pannel").modal('show');
}
