//获取当前用户可操作的部门API
var get_handle_departments_api = "/bmp/1/payments/get_handle_departments";
//出纳待出账API
var teller_pay_list_api = "/bmp/1/payments/teller_pay_list";
/**
 * 页面加载时执行
 */
$(function(){
	getHandleDepartments();//加载部门筛选项
	init(teller_pay_list_api);//设置列表数据查询条件
	$("#btn_search_payment").click(function(){//经理查询按钮
		var condition = get_search_condition({"conditions":["title","urgency","id","department_id","order"]});
		init(teller_pay_list_api + "?" +condition);
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
function get_finance_table_str(items){//填充出账列表
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