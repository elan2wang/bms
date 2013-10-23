//获取我的出账申请API
var my_apply_list_api = "/bmp/1/payments/my_apply_list";
/**
 * 页面加载时执行
 */
$(function(){
	init(my_apply_list_api);//设置列表数据查询条件
	$("#btn_search_payment").click(function(){
		var condition = get_search_condition({"conditions":["title","pay_type","urgency","invoice_need","id","invoice_state","state","order"]});
		init(my_apply_list_api + "?" +condition);
	});
	$.validator.addMethod("checkTransfer",function(value){
		if($("#pay_type").val() == "transfer" && value.length > 0){
			return true;
		}
		return false;
	});
	$('#payment_detail_form').validate({
		onsubmit: true,
		onfocusout: false,
		errorElement: 'label',
		errorClass: 'help-inline',
		rules: {
			title:{
				required: true
			},
			money:{
				required: true,
				number:true
			},
			dst_card_num: {
				checkTransfer: true,
				digits:true
			},
			dst_bank_name: {
				checkTransfer: true
			},
			dst_account_name: {
				checkTransfer: true
			}
		},
		messages: {
			title:{
				required: "请输入主题"
			},
			money: {
				required: "请输入金额",
				number: "请输入数字"
			},
			dst_card_num: {
				checkTransfer: "请填写卡号",
				digits:"请填写正确的卡号"
			},
			dst_bank_name: {
				checkTransfer: "请填写开户行"
			},
			dst_account_name: {
				checkTransfer: "请填写账户名"
			}
		},
		highlight:function(element){
			$(element).closest('.control-group').removeClass('success').addClass('error');
		},
		unhighlight:function(element){
			$(element).closest('.control-group').removeClass('error');
		}
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
 * 
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
			list_str += '<td style="text-align:center;"><a class="edit" title="查看出账详情" href="javascript:void(0);" onclick="payment_detail_view('+id+');return false;"><i class="icon-eye-open"></i></a>';
			list_str += '</td>';
			list_str += '</tr>';
		}
	}
	return list_str;
}