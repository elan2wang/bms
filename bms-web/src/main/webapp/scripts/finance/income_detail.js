/*弹框的操作*/
$(function(){
	/*设置弹窗标题样式*/
	$("#income_info_panel .modal-header").css("padding","15px");
	//$("#income_info_panel .modal-body").css("","");
	$("#again_income").click(function(){//重新申请按钮
		$("#income_detail_form .disabled").removeAttr("disabled");
		$("#form_state").val("1");
		$("#update_my_income,#cancel_update").show();
		$("#delete_income,#again_income").hide();
	});
	$("#cancel_update").click(function(){//取消修改申请按钮
		$("#income_detail_form .disabled").attr("disabled", true);
		$("#form_state").val("0");
		$("#again_income,#delete_income").show();
		$("#update_my_income,#cancel_update").hide();
	});
	$("#update_my_income").click(function(){//更新申请信息
		$("#income_info_panel").modal("hide");
		bootbox.confirm("确定修改该入账申请？", function(result) {
			if (result) {
				var form_state = parseInt($("#form_state").val());
				if(form_state == 1){
					if($('#income_detail_form').validate().form()){
						$("#progress-bar").modal("show");
						var data = $("#income_detail_form").serialize();
						$.post("/bmp/1/incomes/update",data,function(result){
							$("#progress-bar").modal("hide");
							if(typeof(result.error_code) != "undefined"){
								bootbox.alert(result.error_msg);
							}
							else if(typeof(result.data.result_code) != "undefined" && parseInt(result.data.result_code) == 10000){
								bootbox.alert(result.data.result_msg);
								refresh_tab();
							}
						},"JSON");
					}
				}
			}
		});
	});
	$("#delete_income").click(function(){//删除申请信息
		$("#income_info_panel").modal("hide");
		bootbox.confirm("确定删除该入账申请？", function(result) {
			if (result) {
				$("#progress-bar").modal("show");
				var id = $("#id").val();
				$.post("/bmp/1/incomes/destroy",{id:id},function(result){
					$("#progress-bar").modal("hide");
					if(typeof(result.error_code) != "undefined"){
						bootbox.alert(result.error_msg);
					}
					else if(typeof(result.data.result_code) != "undefined" && parseInt(result.data.result_code) == 10000){
						bootbox.alert(result.data.result_msg);
						refresh_tab();
					}
				},"JSON");
			}
		});
	});
	
	$("#btn_handle_exception").click(function(){//经理处理异常申请
		$("#income_info_panel").modal("hide");
		bootbox.confirm("确定处理该异常入账申请？", function(result) {
			if (result) {
				$("#progress-bar").modal("show");
				var id = $("#id").val();
				var data = $("#manager_handle_exception_form").serialize();
				data = data + "&id=" + id;
				$.post("/bmp/1/incomes/handle_exception",data,function(result){
					$("#progress-bar").modal("hide");
					if(typeof(result.error_code) != "undefined"){
						bootbox.alert(result.error_msg);
					}
					else if(typeof(result.data.result_code) != "undefined" && parseInt(result.data.result_code) == 10000){
						bootbox.alert(result.data.result_msg);
						refresh_tab();
					}
				},"JSON");
			}
		});
	});
	
	$("#btn_incomes_invoice").click(function(){//财务开发票
		$("#income_info_panel").modal("hide");
		bootbox.confirm("确定为该入账申请开发票？", function(result) {
			if (result) {
				$("#progress-bar").modal("show");
				var id = $("#id").val();
				var data = $("#accountant_incomes_invoice_form").serialize();
				data = data + "&id=" + id;
				$.post("/bmp/1/incomes/invoice",data,function(result){
					$("#progress-bar").modal("hide");
					if(typeof(result.error_code) != "undefined"){
						bootbox.alert(result.error_msg);
					}
					else if(typeof(result.data.result_code) != "undefined" && parseInt(result.data.result_code) == 10000){
						bootbox.alert(result.data.result_msg);
						refresh_tab();
					}
				},"JSON");
			}
		});
	});
	
	$("#btn_account_checking").click(function(){//出纳对账
		$("#income_info_panel").modal("hide");
		bootbox.confirm("确定完成对账？", function(result) {
			if (result) {
				$("#progress-bar").modal("show");
				var id = $("#id").val();
				var data = $("#teller_account_checking_form").serialize();
				data = data + "&id=" + id;
				$.post("/bmp/1/incomes/account_checking",data,function(result){
					$("#progress-bar").modal("hide");
					if(typeof(result.error_code) != "undefined"){
						bootbox.alert(result.error_msg);
					}
					else if(typeof(result.data.result_code) != "undefined" && parseInt(result.data.result_code) == 10000){
						bootbox.alert(result.data.result_msg);
						refresh_tab();
					}
				},"JSON");
			}
		});
	});
	
});
//初始化详情表单状态
function income_detail_view(id,is_view){
	$(".modal-body").animate({scrollTop:0});//将弹框滚动条置顶
	$("#income_info_panel").css({"width":"960px","left":"15%"});
	$("#income_detail_form .disabled").attr("disabled","true");
	$("#form_state").val("0");
	$("#again_income,#delete_income").show();//显示重新申请,删除申请按钮
	$("#cancel_update,#update_my_income").hide();//隐藏修改申请,取消申请按钮
	$(".bring_type_deposit").hide();//默认存现流水号隐藏
	$(".invoice_title_control").hide();//默认发票信息隐藏
	$(".portlet-body").show();//显示右边面板
	$(".tools a").removeClass("collapse").addClass("expand");//设置右边操作面板箭头
	
	$("#id").val(id);
	set_income_info(id,is_view);
	$("#income_info_panel").modal("show");
}
//设置出账申请信息
function set_income_info(id,is_view){
	var url = "/bmp/1/incomes/view";
	//var url = "/bmp/views/income_detail.json";
	$.ajax({
		url:url,
		type:"get",
		data:{id:id},
		dataType:"JSON",
		success:function(result){
			var msg = "";
			if(typeof(result.error_code) != "undefined"){
				msg = result.error_msg;
			}
			else if(typeof(result.data) != "undefined" && result.data){
				var infos = result.data;
				var info_containers = $(".info_container");
				for(var i=0;i<info_containers.length;i++){
					var info_name = $(info_containers[i]).attr("name");
					if(typeof(infos[info_name]) != "undefined"){
						$(".info_container[name='"+info_name+"']").val(infos[info_name]);
					}
				}
				if(typeof(infos.bring_type) != "undefined" && infos.bring_type == "deposit"){
					$(".bring_type_deposit").show();//显示存现流水号
				}
				if(typeof(infos.invoice_type) != "undefined" && infos.invoice_type != "invoice_no"){				
					$(".invoice_title_control").hide();//显示发票信息
				}
				if(is_view == 1){
					if(typeof(infos.state) != "undefined"){
						var state = infos.state;
						if(state != "new"){
							$(".update_delete_operate").hide();//非最新状态，隐藏个人操作申请详情按钮
						}
						if(infos.invoice_type != "invoice_no" && infos.invoice_state != "open"){
							$(".update_delete_operate").hide();//不要发票且发票为关闭，隐藏个人操作申请详情按钮
						}
						if(infos.invoice_type != "invoice_no"){
							$(".invoice_title_control").show();//显示发票抬头
						}
						//控制3个操作框中的显示内容
						$(".teller_operate").show();
						$(".accountant_operate").show();
						$(".manager_operate").show();
						if(state == "new"){
							if(infos.invoice_type != "invoice_first"){
								$("#accountant_portlet_body").html("未开发票");
							}else{
								if(infos.invoice_state == "open"){
									$("#teller_portlet_body").html("未进行对账");
								}else{
									$("#accountant_portlet_body").html("开发票时间：" + infos.invoice_time + "<br/>" + "财务备注：" + infos.accountant_comment);
								}
							}
							if(infos.handle_time != ""){
								$("#manager_portlet_body").html("异常处理时间：" + infos.handle_time + "<br/>" + "总经理备注：" + infos.manager_comment);
							}else{
								$("#manager_portlet_body").html("未进行异常处理");
							}
						}else{
							if(state == "audited"){
								$("#teller_portlet_body").html("对账时间：" + infos.audit_time + "<br/>" + "出纳备注：" + infos.teller_comment);
								if(infos.handle_time != ""){
									$("#manager_portlet_body").html("异常处理时间：" + infos.handle_time + "<br/>" + "总经理备注：" + infos.manager_comment);
								}else{
									$("#manager_portlet_body").html("未进行异常处理");
								}
							}else{
								if(state == "pending"){
									$("#teller_portlet_body").html("对账时间：" + infos.audit_time + "<br/>" + "出纳备注：" + infos.teller_comment);
									if(infos.invoice_type == "invoice_first"){
										$("#accountant_portlet_body").html("开发票时间：" + infos.invoice_time + "<br/>" + "财务备注：" + infos.accountant_comment);
									}else{
										$("#accountant_portlet_body").html("未开发票");
									}
								}else{
									$("#teller_portlet_body").html("对账时间：" + infos.audit_time + "<br/>" + "出纳备注：" + infos.teller_comment);
									if(infos.invoice_type == "invoice_no"){
										$("#accountant_portlet_body").html("未开发票");
									}else{
										$("#accountant_portlet_body").html("开发票时间：" + infos.invoice_time + "<br/>" + "财务备注：" + infos.accountant_comment);
									}
									$("#manager_portlet_body").html("未进行异常处理");
								}
							}
						}
						//控制状态信息框显示内容
						$(".state_info").show();
						var state_content = "";
						switch(state){
							case "new": state_content = "<label class='label label-success'>新创建</label>"; break;
							case "audited": state_content = "<label class='label label-success'>已对账</label>"; break;
							case "pending": state_content = "<label class='label label-important'>挂起</label>"; break;
							case "closed": state_content = "<label class='label label-success'>关闭</label>"; break;
							default: break;
						}
						var invoice_state_content = "";
						if(infos.invoice_state == "open"){
							invoice_state_content = "未开票";
						}else {
							invoice_state_content = "已开票";
						}
						var content = "入账状态：" + state_content + "<br/>" + "发票状态：" + invoice_state_content;
						if(state != "new"){
							content += "<br/>" + "<br/>" + "对账时间：" + infos.audit_time + "<br/>" + "出纳备注：" + infos.teller_comment;
						}else{
							if(infos.handle_time != ""){
								content += "<br/>" + "<br/>" + "异常处理时间：" + infos.handle_time + "<br/>" + "总经理备注：" + infos.manager_comment;
							}
						}
						if(infos.invoice_type != "invoice_no" && infos.invoice_state == "closed"){
							content += "<br/>" + "<br/>" + "开发票时间：" + infos.invoice_time + "<br/>" + "财务备注：" + infos.accountant_comment;
						}
						$("#state_portlet_body").html(content);
					}
				}
			}
			if(msg.length > 0){
				$("#income_info_panel").html(msg);
			}
		},
		error:function(msg){

		}
	});
}
function switch_bring_type_control(value){
	$("div[class*='bring_type']").hide();
	$(".bring_type_"+value).show();
}
function switch_invoice_control(value){
	if(value == "invoice_no"){
		$(".invoice_title_control").hide();
	}
	else{
		$(".invoice_title_control").show();
	}
}