//获取当前用户可操作的部门API
var get_handle_departments_api = "/bmp/1/payments/get_handle_departments";
//获取所有出账申请的API
var payments_all_api =  "/bmp/1/payments/all";
//var payments_all_api = "/bmp/views/payment_all.json";
//总经理要求财务重新出账API
var ask_accountant_repay_api ="/bmp/1/payments/ask_accountant_to_repay";
/**
 * 页面加载时执行
 */
$(function(){
	getHandleDepartments();//加载部门筛选项
	init(payments_all_api);//设置列表数据查询条件
	$("#btn_search_payment").click(function(){//经理查询按钮
		var condition = get_search_condition({"conditions":["title","pay_type","urgency","invoice_need","id","department_id","invoice_state","state","order"]});
		init(payments_all_api + "?" +condition);
	});
	$("#accountant_pay_again").click(function(){//财务重新出账按钮
		bootbox.confirm("您确定要重新出账吗?", function(result) {
			if (result) {
				$("#progress-bar").modal("show");
				var id = $("#id").val();
				$.post(ask_accountant_repay_api,{id:id},function(result){
					$("#progress-bar").modal("hide");
					var msg = "";
					if(typeof(result.error_code) != "undefined"){
						msg = result.error_msg;
					}
					else if(typeof(result.data.result_code) != "undefined" && parseInt(result.data.result_code) == 10000){
						msg = result.data.result_msg;	
						$("#payment_info_panel").modal("hide");
						var cur_url = $("#pagination ul li.active a").prop("href");//获得当前页列表的url
						init(cur_url);//重新加载列表
					}
					bootbox.alert(msg);
				});
			}
		});
	});
});
/**
 * 刷新页面时加载数据
 */
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
function get_finance_table_str(items){
	var list_str = '';
	if(items.length > 0){
		for(var i=0;i<items.length;i++){
			var id = items[i].id;
			list_str += '<tr>';
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

			var state = "--";
			state = get_finance_payments_state(items[i].state);
			list_str += '<td>'+state+'</td>';
			var invoice_state = "--";
			invoice_state = get_finance_payments_invoice_state(items[i].invoice_state);
			list_str += '<td>'+invoice_state+'</td>';
			list_str += '<td>'+items[i].apply_time+'</td>';
			list_str += '<td><a class="edit" href="javascript:void(0);" onclick="payment_detail_view('+id+'); return false;"><i class="icon-eye-open"></i></a></td>';
			list_str += '</tr>';
		}
	}
	return list_str;
}
/**
 * 加载当前用户可操作的部门至下拉框
 */
function getHandleDepartments(){
	$.get(get_handle_departments_api,function(result){
		if(typeof(result.error_code) != "undefined"){
			//$("#payment_detail_lists").html('<tr><td colspan="10" style="text-align:center;"><span style="color:red;">'+result.error_msg+'</span></td></tr>');
		}
		else if(typeof(result.data) != "undefined" && result.data){
			var data = result.data;
			var items = data.items;
			var departmentListHtml = "<option value= ''>所有部门</option>";
			for(var i=0; i<items.length; i++){
				departmentListHtml += "<option value='" + items[i].dep_id + "'>"
					+ items[i].dep_name + "</option>";
			}
			$("#search_department_id").html(departmentListHtml);
		}
	});		
};
