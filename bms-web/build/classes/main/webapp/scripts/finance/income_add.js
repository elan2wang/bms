function switch_bring_type_control(value){
	if(value == "deposit"){
		$(".bring_type_deposit").show();
	}
	else{
		$(".bring_type_deposit").hide();
	}
}
function switch_invoice_control(value){
	if(value == "invoice_no"){
		$(".invoice_title_control").hide();
	}
	else{
		$(".invoice_title_control").show();
	}
}
$(function(){
	getVaults();
	$.validator.addMethod("checkBringType",function(value){//添加入账方式验证
		if($("#bring_type").val() == "deposit" && value.length > 0){
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
	$('#income_app_form').validate({
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
	$(".confirm").click(function(){
		bootbox.confirm("您确定要提交该入账申请?", function(result) {
			if (result) {
				if($('#income_app_form').validate().form()){
					$("#progress-bar").modal("show");
					var data = $("#income_app_form").serialize();
					$.post("/bmp/1/incomes/add",data,function(result){
						$("#progress-bar").modal("hide");
						if(typeof(result.error_code) != "undefined"){
							bootbox.alert(result.error_msg);
						}
						else if(typeof(result.data.result_code) != "undefined" && parseInt(result.data.result_code) == 10000){
							$("button[type='reset']").trigger("click");
							$(".control-group").removeClass("success").removeClass("error");
							bootbox.alert("入账申请提交成功");
							// 刷新Tab
							refresh_tab();
						}
					},"JSON");
				}
			}
		});
	});
});

function getVaults(){
	$.get("/bmp/1/vaults/get_income_payment_vaults",{type:"income"},function(result){
		if(typeof(result.error_code) != "undefined"){
			$("#bank_card").html(result.error_msg);
		}
		else if(typeof(result.data) != "undefined" && result.data){
			var data = result.data;
			var items = data.items;
			var vaultsHtml = "";
			for(var i=0; i<items.length; i++){
				if(items[i].type == "bank"){
					vaultsHtml += "<option value='" + items[i].v_number + "'>"
					+ items[i].alias + "</option>";
				}
			}
			$("#bank_card").html(vaultsHtml);
		}
	});		
};