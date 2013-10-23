var account_checking_list_api = "/bmp/1/incomes/account_checking_list";
//var account_checking_list_api = "/bmp/views/account_checking_list.json";

var get_handle_departments_api = "/bmp/1/incomes/get_handle_departments";

$(function(){
	getHandleDepartments();
	init(account_checking_list_api);
	$("#btn_search_income").click(function(){
		var condition = get_search_condition({"conditions":["id","bring_type","invoice_type","invoice_state","department","order_type"]});
		init(account_checking_list_api + "?" +condition);
	});
});

function init(url) {
	$("#account_checking_list").html('<tr><td colspan="12" style="text-align:center;"><img alt="正在加载" src="../images/loading.gif" />正在加载列表，请稍等...</td></tr>');
	$.ajax({
		url: url,
		type: "get",
		data: {},
		dataType: "JSON",
		success: function(result) {
			if(typeof(result.error_code) != "undefined"){
				$("#account_checking_list").html('<tr><td colspan="12" style="text-align:center;"><span style="color:red;">'+result.error_msg+'</span></td></tr>');
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
					var table_str = get_income_table_str(items);
					$("#account_checking_list").html(table_str);
				} else {
					$("#pagination").html("");
					$("#account_checking_list").html('<tr><td colspan="12" style="text-align:center;">暂无数据</td></tr>');
				}
			}
		}
	});
}

function get_income_table_str(items){//填充出账列表
	var list_str = '';
	if(items.length > 0){
		for(var i=0;i<items.length;i++){
			var item = items[i];
			var id = item.id;
			list_str += '<tr>';
			list_str += '<td>'+id+'</td>';
			list_str += '<td>'+item.creator+'</td>';
			list_str += '<td>'+item.department+'</td>';
			list_str += '<td>'+item.money+'</td>';
			var bring_type = "--";
			if(item.bring_type == "transfer"){
				bring_type = "转账";
			}
			else if(item.bring_type == "deposit"){
				bring_type = "存现";
			}
			list_str += '<td>'+bring_type+'</td>';
			list_str += '<td>'+item.bank_card+'</td>';
			list_str += '<td>'+item.serial_num+'</td>';
			var invoice_type = "--";
			switch(item.invoice_type){
				case "invoice_no":
					invoice_type = "无发票";
					break;
				case "invoice_first":
					invoice_type = "先票后款";
					break;
				case "invoice_last":
					invoice_type = "先款后票";
					break;
				default:
					break;
			}
			list_str += '<td>'+invoice_type+'</td>';
			var invoice_state = "--";
			if(item.invoice_state == "open"){
				invoice_state = "未开票";
			}
			else if(item.invoice_state == "closed"){
				invoice_state = "已开票";
			}
			list_str += '<td>'+invoice_state+'</td>';
			list_str += '<td>'+item.create_time+'</td>';
			list_str += '<td style="text-align:center;"><a class="edit" title="查看详情" href="javascript:void(0);" onclick="income_detail_view('+id+',1);" ><i class="icon-eye-open"></i></a></td>';
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
			var departmentListHtml = "<option value= ''>部门</option>";
			for(var i=0; i<items.length; i++){
				departmentListHtml += "<option value='" + items[i].dep_id + "'>"
					+ items[i].dep_name + "</option>";
			}
			$("#search_department").html(departmentListHtml);
		}
	});		
};