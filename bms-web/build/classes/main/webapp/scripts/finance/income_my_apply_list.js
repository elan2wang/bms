var income_my_apply_list_api = "/bmp/1/incomes/my_apply_list";

$(function(){
	init(income_my_apply_list_api);
	$("#btn_search_income").click(function(){
		var condition = get_search_condition({"conditions":["id","title","income_type","bring_type","invoice_type","invoice_state","state","order_type"]});
		init(income_my_apply_list_api + "?" +condition);
	});
	$.validator.addMethod("checkBringType",function(value){//添加入账方式验证
		if($("#bring_type").val() == "deposit" && value.length > 0){
			return true;
		}
		if($("#bring_type").val() == "transfer" && value.length > 0){
			return true;
		}
		return false;
	});
	$.validator.addMethod("checkInvoiceTitle",function(value){//添加发票抬头验证
		if($("#invoice_type").val() != "invoice_no" && value.length > 0){
			return true;
		}
		return false;
	});
	$.validator.addMethod("checkTime",function(value){//添加时间验证
		var reg_time = /\d{4}-\d{2}-\d{2}[\s]+\d{2}:\d{2}:\d{2}/;
		if(reg_time.test(value)){
			return true;
		}
		return false;
	});
	$('#payment_detail_form').validate({
		errorElement: 'label',
		errorClass: 'help-inline',
		rules: {
			income_type:{
				required: true
			},
			title:{
				required: true
			},
			generate_time:{
				required: true,
				checkTime: true
			},
			money:{
				required: true,
				number: true
			},
			invoice_title:{
				checkInvoiceTitle: true
			},
			bank_card:{
				required: true,
				digits: true
			},
			serial_num:{
				checkBringType: true
			}
		},
		messages: {
			income_type:{
				required: "请选择类别"
			},
			title: {
				required: "请输入主题"
			},
			generate_time:{
				required: "请填写时间",
				checkTime: "请填写正确的格式(yyyy-mm-dd HH:ii:ss)"
			},
			money:{
				required: "请填写金额",
				number: "请填写数字"
			},
			invoice_title:{
				checkInvoiceTitle: "请填写发票抬头"
			},
			bank_card:{
				required: "请填写银行卡号",
				digits: "请填写正确的银行卡号"
			},
			serial_num:{
				checkBringType: "请填写存现流水号"
			}
		},
		highlight:function(element){
			$(element).closest('.control-group').removeClass('success').addClass('error');
		},
		success:function(element){
			element.closest('.control-group').removeClass('error').addClass('success');
		}
	});
});

function init(url) {
	$("#income_my_apply_list").html('<tr><td colspan="12" style="text-align:center;"><img alt="正在加载" src="../images/loading.gif" />正在加载列表，请稍等...</td></tr>');
	$.ajax({
		url: url,
		type: "get",
		data: {},
		dataType: "JSON",
		success: function(result) {
			if(typeof(result.error_code) != "undefined"){
				$("#income_my_apply_list").html('<tr><td colspan="12" style="text-align:center;"><span style="color:red;">'+result.error_msg+'</span></td></tr>');
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
					$("#income_my_apply_list").html(table_str);
				} else {
					$("#pagination").html("");
					$("#income_my_apply_list").html('<tr><td colspan="12" style="text-align:center;">暂无数据</td></tr>');
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
			list_str += '<td>'+item.title+'</td>';
			list_str += '<td>'+item.money+'</td>';
			list_str += '<td>'+item.income_type+'</td>';
			var bring_type = "--";
			if(item.bring_type == "transfer"){
				bring_type = "转账";
			}
			else if(item.bring_type == "deposit"){
				bring_type = "存现";
			}
			list_str += '<td>'+bring_type+'</td>';
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
				invoice_state = "未确认";
			}
			else if(item.invoice_state == "closed"){
				invoice_state = "已确认";
			}
			list_str += '<td>'+invoice_state+'</td>';
			var state = "--";
			switch(item.state){
				case "new":
					state = "新创建";
					break;
				case "audited":
					state = "对账结束";
					break;
				case "pending":
					state = "挂起";
					break;
				case "closed":
					state = "关闭";
					break;
				default:
					break;
			}
			list_str += '<td>'+state+'</td>';
			list_str += '<td>'+item.manager_comment+'</td>';
			list_str += '<td>'+item.creator_comment+'</td>';
			list_str += '<td>'+item.create_time+'</td>';
			list_str += '<td>';
			list_str += '<button class="btn" type="button" onclick="income_detail_view('+id+',1)">查看详情</button><br />';
			list_str += '</td>';
			list_str += '</tr>';
		}
	}
	return list_str;
}