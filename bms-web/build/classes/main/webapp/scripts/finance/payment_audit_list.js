//获取待审批列表API
var audit_list_api = "/bmp/1/payments/audit_list";
//var audit_list_api = "/bmp/views/payment_audit_list.json";
//获取当前用户可操作的部门API
var get_handle_departments_api = "/bmp/1/payments/get_handle_departments";
//批量审批API
var audit_batch_api = "/bmp/1/payments/audit_batch";

$(function(){
	getHandleDepartments();//加载部门筛选项
	getHandleDepartments();//加载部门筛选项
	init(audit_list_api);//设置列表数据查询条件
	$("#btn_search_payment").click(function(){//经理查询按钮
		var condition = get_search_condition({"conditions":["title","pay_type","urgency","invoice_need","id","department_id","order"]});
		init(audit_list_api + "?" +condition);
	});
	$("#audit_payment_batch").click(function(){//批量审核按钮
		bootbox.confirm("批量审批将同意选中项，确定审批?", function(result) {
			if (result) {
				var audit_ids = "";
				$("input[name='audit_payment[]']:checked").each(function(){
					audit_ids += ","+$(this).val();
				});
				if(audit_ids.length == 0){
					myDialog.show_dialog({
						title:'错误信息',
						has_title:1,
						content:'抱歉，您们没有选则任何出账申请！',
						minHeight:80,
						selector:'waiting_submit'
					});
				}
				else{
					$("#progress-bar").modal("show");
					audit_ids = audit_ids.substr(1);
					$.post(audit_batch_api,{audit_ids:audit_ids},function(result){
						$("#progress-bar").modal("hide");
						var msg = "";
						if(typeof(result.error_code) != "undefined"){
							msg = result.error_msg;
						}
						else if(typeof(result.data.result_code) != "undefined"){
							msg = result.data.result_msg;		
							var cur_url = $("#pagination ul li.active a").prop("href");//获得当前页列表的url
							init(cur_url);
						}
						bootbox.alert(msg);
					},"JSON");
				}
			}
		});
	});
});
function init(url) {
	$("#payment_detail_lists").html(loading);
	$.ajax({
		url: url,
		type: "get",
		data: {},
		dataType: "JSON",
		success: function(result) {
			if(typeof(result.error_code) != "undefined"){
				$("#payment_detail_lists").html('<tr><td colspan="10" style="text-align:center;"><span style="color:red;">'+result.error_msg+'</span></td></tr>');
			}
			else if(typeof(result.data) != "undefined" && result.data){
				var data = result.data;
				var currentItemCount = data.currentItemCount;
				var items = data.items;
				if(items.length > 0 && parseInt(currentItemCount) > 0){
					//设置分页
					var itemsPerPage = data.itemsPerPage;
					var currentPage = Math.ceil(data.startIndex/data.itemsPerPage);
					var totalPages = Math.ceil(data.totalItems/data.itemsPerPage);
					set_pagination(currentPage,totalPages,itemsPerPage,data.nextLink);
					//封装列表
					var table_str = get_finance_table_str(items);
					$("#payment_detail_lists").html(table_str);
				} else {
					$("pagination").html("");
					$("#payment_detail_lists").html(no_data);
				}
			}
		}
	});
}
//填充列表
/*
 * @param {object} items	列表数据
 * @returns {string} list_str 列表表格字符串
 */
function get_finance_table_str(items){//填充出账列表
	var list_str = '';
	if(items.length > 0){
		for(var i=0;i<items.length;i++){
			var id = items[i].id;
			list_str += '<tr>';
			list_str += '<td><input name="audit_payment" type="checkbox" onclick="verify_check_all(\'audit_payment\',\'audit_payment_all\'\)" value="'+id+'" /></td>';
			list_str += '<td>'+id+'</td>';
			var urgency = "--";
			if(items[i].urgency == true){
				urgency = "是";
			}
			else if(items[i].urgency == false){
				urgency = "否";
			}
			list_str += '<td>'+urgency+'</td>';
			list_str += '<td>'+items[i].applicant+'</td>';
			list_str += '<td>'+items[i].department+'</td>';
			list_str += '<td>'+items[i].title+'</td>';
			list_str += '<td>'+items[i].money+'</td>';
			var pay_type = "--";
			pay_type = get_finance_payments_pay_type(items[i].pay_type);
			list_str += '<td>'+pay_type+'</td>';
			var invoice_need = "--";
			if(items[i].invoice_need == true){
				invoice_need = "是";
			}
			else if(items[i].invoice_need == false){
				invoice_need = "否";
			}
			list_str += '<td>'+invoice_need+'</td>';
			list_str += '<td>'+items[i].apply_time+'</td>';
			list_str += '<td><button class="btn" type="button" onclick="payment_detail_view('+id+')">查看详情</button></td>';
			list_str += '</tr>';
		}
	}
	return list_str;
}
//加载部门筛选项
function getHandleDepartments(){
	$.get(get_handle_departments_api,function(result){
		if(typeof(result.error_code) != "undefined"){
			//$("#payment_detail_lists").html('<tr><td colspan="10" style="text-align:center;"><span style="color:red;">'+result.error_msg+'</span></td></tr>');
		}
		else if(typeof(result.data) != "undefined" && result.data){
			var data = result.data;
			var items = data.items;
			var departmentListHtml = "<option value= ''>不限</option>";
			for(var i=0; i<items.length; i++){
				departmentListHtml += "<option value='" + items[i].dep_id + "'>"
					+ items[i].dep_name + "</option>";
			}
			$("#search_department_id").html(departmentListHtml);
		}
	});		
};