var destroy_api = "/bmp/1/payments/destroy";//删除
var audit_api = "/bmp/1/payments/audit";//审批
var accountant_pay_api = "/bmp/1/payments/accountant_pay";//财务出账
var confirm_invoice_api = "/bmp/1/payments/confirm_invoice";//确认发票
var teller_pay_api = "/bmp/1/payments/teller_pay";//出纳出账
var view_api = "/bmp/1/payments/view";//查看
var get_related_tellers_api = "/bmp/1/payments/get_related_tellers";//获取相关出纳
var get_vaults_api = "/bmp/1/vaults/get_income_payment_vaults";//获取出入账所需账户
var get_invoice_title_api = "/bmp/1/invoice_title/get_titles";//获取发票抬头
/*弹框的操作*/
$(function(){
	/*设置弹窗标题样式*/
	$("#payment_info_panel .modal-header").css("padding","15px");
	//$("#payment_info_panel .modal-body").css("","");
	$("#again_payment").click(function(){//重新申请按钮
		$("#payment_detail_form .disabled").removeAttr("disabled");
		$("#form_state").val("1");
		$("#update_payment,#cancel_update").show();
		$("#delete_payment,#again_payment").hide();
	});
	$("#cancel_update").click(function(){//取消修改申请按钮
		$("#payment_detail_form .disabled").attr("disabled", true);
		$("#form_state").val("0");
		$("#again_payment,#delete_payment").show();
		$("#update_payment,#cancel_update").hide();
	});
	/**************修改申请********************/
	$("#update_payment").click(function(){//更新申请信息
		bootbox.confirm("确认修改？", function(result){
			if(result){
				var form_state = parseInt($("#form_state").val());
				if(form_state == 1){
					if($('#payment_detail_form').validate().form()){
						$("#progress-bar").modal("show");
						var data = $("#payment_detail_form").serialize();
						var id = $("#id").val();
						data = data + "&id=" + id;
						$.post("/bmp/1/payments/update",data,function(result){
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
						},"JSON");
					}
				}
			}
		});
	});
	/**************删除申请********************/
	$("#delete_payment").click(function(){
		bootbox.confirm("确认删除？", function(result){
			if(result){
				$("#progress-bar").modal("show");
				var id = $("#id").val();
				$("#payment_info_panel").modal("hide");
				$.post(destroy_api,{id:id},function(result){
					$("#progress-bar").modal("hide");
					var msg = "";
					if(typeof(result.error_code) != "undefined"){
						msg = result.error_msg;
					}
					else if(typeof(result.data.result_code) != "undefined"){
						msg = result.data.result_msg;
						var cur_url = $("#pagination ul li.active a").prop("href");//获得当前页列表的url
						init(cur_url);//重新加载列表
					}
					bootbox.alert(msg);
				},"JSON");
			}
		});
	});
	/**************审批申请********************/
	$("#btn_payment_audit").click(function(){//经理审核申请
		bootbox.confirm("确定审批？", function(result){
			if(result){
				$("#progress-bar").modal("show");
				var id = $("#id").val();
				var data = $("#manager_audit_form").serialize();
				data = data + "&id=" + id;
				$.post(audit_api,data,function(result){
					var msg = "";
					$("#progress-bar").modal("hide");
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
				},"JSON");
			}
		});
	});
	/**************财务出账********************/
	$("#btn_accountant_pay").click(function(){//财务出账
		bootbox.confirm("确定出账？", function(result){
			if(result){
				$("#progress-bar").modal("show");
				var id = $("#id").val();
				var data = "id=" + id;
				if($(".cash-pay-control").hasClass("cur_pay_type") == true){
					var src_account = $("#src_account_cash").val();
					var teller_id = $("#teller_id").val();
					data = data + "&src_account=" + src_account + "&teller_id="+teller_id;
				}
				else if($(".transfer-pay-control").hasClass("cur_pay_type") == true){
					var src_account = $("#src_account_transfer").val();
					data = data + "&src_account=" + src_account;
				}
				var accountant_comment = $("#accountant_comment").val();
				data = data + "&accountant_comment=" + accountant_comment;
				var invoice_title = $("#accountant_invoice_title").val();
				data += "&invoice_title=";
				data += invoice_title;
				$.post(accountant_pay_api,data,function(result){
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
				},"JSON");
			}
		});
	});
	/**************确认发票********************/
	$("#btn_accountant_confirm_invoice").click(function(){//财务确认发票
		bootbox.confirm("确认已收发票？", function(result){
			if(result){
				$("#progress-bar").modal("show");
				var id = $("#id").val();
				$.post(confirm_invoice_api,{id:id},function(result){
					$("#progress-bar").modal("hide");
					var msg = "";
					if(typeof(result.error_code) != "undefined"){
						msg = result.error_msg;
					}
					else if(typeof(result.data.result_code) != "undefined"){
						msg = result.data.result_msg;
						$("#payment_info_panel").modal("hide");
						var cur_url = $("#pagination ul li.active a").prop("href");//获得当前页列表的url
						init(cur_url);//重新加载列表
					}
					bootbox.alert(msg);
				},"JSON");
			}
		});
	});
	/**************出纳出账********************/
	$("#btn_teller_pay").click(function(){//出纳现金支付
		bootbox.confirm("确认出账？", function(result){
			$("#progress-bar").modal("show");
			var id = $("#id").val();
			var data = $("#teller_pay_form").serialize();
			data = data + "&id=" + id;
			$.post(teller_pay_api,data,function(result){
				$("#progress-bar").modal("hide");
				var msg = "";
				if(typeof(result.error_code) != "undefined"){
					msg = result.error_msg;
				}
				else if(typeof(result.data.result_code) != "undefined"){
					msg = result.data.result_msg;
					$("#payment_info_panel").modal("hide");
					var cur_url = $("#pagination ul li.active a").prop("href");//获得当前页列表的url
					init(cur_url);//重新加载列表
				}
				bootbox.alert(msg);
			},"JSON");
		});
	});
});
function payment_detail_view(id){
	$(".modal-body").animate({scrollTop:0});//将弹框滚动条置顶
	//初始化详情表单状态
	$(".transfer_control").hide();//默认隐藏转账相关控件
	$(".update_delete_operate").hide();//默认隐藏”删除和修改“按钮
	$(".invoice_title_control").hide();//默认隐藏发票抬头
	$("#payment_detail_form .disabled").attr("disabled",true);
	$("#form_state").val("0");
	$("#again_payment,#delete_payment").show();//显示重新申请,删除申请按钮
	$("#cancel_update,#update_payment").hide();//隐藏修改申请,取消申请按钮
	
	
	$(".portlet-body").hide();//
	$(".tools a").removeClass("expand").addClass("collapse");//设置右边操作面板箭头
	$(".default-open").show();//
	$(".default-open").closest(".portlet").find(".tools a").removeClass("collapse").addClass("expand");//设置右边操作面板箭头
	if($("#manager_audit_form input[name='audit_state'][value='disagreed']") != "undefined"){//默认总经理审核不同意
		$("#manager_audit_form input[name='audit_state'][value='disagreed']").prop("checked",true);
	}
	$(".cash-pay-control").hide();
	$(".transfer-pay-control").show();
	$("#state1").checked = false;
	$("#state2").checked = true;
	
	//my_apply_list默认隐藏其除“状态”外的所有面板
	$(".my_apply_list_manager_operate").hide();
	$(".my_apply_list_accountant_operate").hide();
	$(".my_apply_list_teller_operate").hide();
	
	//all默认隐藏其除“状态”外的所有面板
	$(".all_manager_operate").hide();
	$(".all_accountant_operate").hide();
	$(".all_teller_operate").hide();
	
	//invoice_list默认隐藏“出纳”操作面板
	$(".invoice_list_teller_operate").hide();
	//var html = $("#payment_info_panel").html();
	//$("#payment_info_panel").html(html);
	set_payment_info(id);
}
//设置出账申请信息
function set_payment_info(id){
	$.ajax({
		url:view_api,
		type:"get",
		data:{id:id},
		dataType:"JSON",
		success:function(result){
			var msg = "";
			var state_content = "";//显示在”状态&发票状态栏中的信息“
			var invoice_state_content = "";
			var invoice_title = "";
			if(typeof(result.error_code) != "undefined"){
				msg = result.error_msg;
			}
			else if(typeof(result.data) != "undefined" && result.data){
				var infos = result.data;
				var info_containers = $(".info_container");
				for(var i=0;i<info_containers.length;i++){
					var info_name = $(info_containers[i]).attr("name");
					if(typeof(infos[info_name]) != "undefined"){
						var cur_contain = $("#payment_detail_form input[name='"+info_name+"']");
						if($(cur_contain).attr("type") != "undefined" && $(cur_contain).attr("type") == "radio"){
							$("#payment_detail_form .info_container[name='"+info_name+"'][value='"+infos[info_name].toString()+"']").prop("checked",true);	
						}
						else{
							$("#payment_detail_form .info_container[name='"+info_name+"']").val(infos[info_name].toString());
						}
					}
				}
				if(typeof(infos.pay_type) != "undefined" && infos.pay_type == "transfer"){
					$("#manager_audit_form .invoice_title_control").removeClass("no_invoice");
					$(".transfer_control").show();
				}
				if(typeof(infos.invoice_need) != "undefined" && infos.invoice_need.toString() == "true"){
					$("#accountant_pay_form .invoice_title_control").show();
					$("#manager_audit_form .invoice_title_control").removeClass("no_invoice");
				}
				else{
					$("#manager_audit_form .invoice_title_control").addClass("no_invoice");
				}
				if(typeof(infos.state) != "undefined"){
					var state = infos.state;
					var payment_schedule = 0;
					if(infos.invoice_need == false){
						invoice_state_content = "无发票";
					}else{
						if(infos.invoice_state == "open"){
							invoice_state_content = "发票未收";
						}else{
							invoice_state_content = "发票已收";
						}
					}
					var manager_audit_state_class = "label-success";
					if(state == "new"){//新创建的申请
						payment_schedule = 0;//新创建的申请
						state_content = "新创建";
					}
					else if(state == "disagreed"){//经理审核未通过
						manager_audit_state_class = "label-important";
						payment_schedule = 1;
						state_content = "审批未通过";
					}
					else if(state == "agreed"){
						payment_schedule = 2;
						state_content = "审批通过";
					}
					else if(state == "accountant_payed"){//财务出账结束
						payment_schedule = 3;//财务出账结束
						state_content = "财务已出账";
						if(infos.invoice_need.toString() == "true"){
							invoice_title = infos.invoice_title;
						}
						if(infos.pay_type == "transfer"){
							if($("#accountant_pay_again") != "undefined"){//显示重新出账按钮
								$("#accountant_pay_again").show();
							}
						}
						
					}
					else if(state == "teller_payed"){//出纳出账结束
						payment_schedule = 4;//出纳出账结束
						state_content = "出纳已出账";
					}
					if(state == "pending"){//挂起
					}
					else if(state == "closed"){//关闭
						payment_schedule = 5;//申请已经关闭
						state_content = "已关闭";
						if(infos.pay_type == "transfer"){
							if($("#accountant_pay_again") != "undefined"){//显示重新出账按钮
								$("#accountant_pay_again").show();
							}
						}
					}
					
					//设置状态和发票状态的内容
					var state_invoice_state_content = "";
					state_invoice_state_content += "状态：";
					state_invoice_state_content += state_content;
					state_invoice_state_content += "</br>发票状态：";
					state_invoice_state_content += invoice_state_content;
					if(invoice_title != ""){
						state_invoice_state_content += "</br>发票抬头：";
						state_invoice_state_content += invoice_title;
					}
					$("#state-and-invoice_state").html("<span style='visibility:hidden;'>状态</span>状态: <label class='label "+manager_audit_state_class+"'>" + state_content + "</label></br>"
							+ "发票状态: " + invoice_state_content);
					var comment = "无";

					
					
					
					/**************新创建的申请*********************/
					if(payment_schedule == 0){
						$(".update_delete_operate").show();//显示个人操作按钮面板
						if(infos.invoice_need.toString() == "true"){
							get_invoice_title();//加载发票抬头
						}
					}else{
						$(".update_delete_operate").show();//显示个人操作按钮面板
						$(".manager_operate").show();//展示经理操作面板
						/***************审批未通过  显示经理操作结果****************************/
						if(payment_schedule == 1){
							if(infos.manager_comment){
								comment = infos.manager_comment;
							}
							$("#manager_portlet_body").html("审批时间: " + infos.audit_time + "<br/>"
									+"审批结果: <label class='label label-important'>审批不通过</label>" + "<br/>"
									+"<span style='visibility:hidden;'>备注</span>备注: " + comment);

						}
						/***************审批通过  显示经理操作结果  禁用修改和删除按钮****************************/
						if(payment_schedule >= 2){
							$(".update_delete_operate").hide();//删除个人操作按钮面板
							var audit_content = "审批时间: " + infos.audit_time + "<br/>" 
							+"<span style='visibility:hidden;'>状态</span>状态: <label class='label label-success'>审批通过</label>" 
							+ "<br/>";
							if($("#payment_info_panel").hasClass("update_delete_operate") == false){
								if(typeof(infos.invoice_title) != "undefined" && infos.invoice_title){
									audit_content += "发票抬头: " + infos.invoice_title + "<br/>";
								}
							}
							if(typeof(infos.manager_comment) != "undefined" && infos.manager_comment){
								audit_content += "<span style='visibility:hidden;'>备注</span>备注: " + infos.manager_comment;

							}
							$("#manager_portlet_body").html(audit_content);
							if(infos.invoice_need.toString() == "true"){
								get_invoice_title();//加载发票抬头
							}
							if(infos.pay_type == 'cash'){
								get_related_tellers(infos.id);//为财务加载出纳
								get_vaults("payment", infos.pay_type);//为财务加载账户
								$(".cash-pay-control").addClass("cur_pay_type");
								$(".transfer-pay-control").removeClass("cur_pay_type");
								$(".cash-pay-control").show();
								$(".transfer-pay-control").hide();
							}
							else if(infos.pay_type == 'transfer'){
								get_vaults("payment", infos.pay_type);//为财务加载账户
								$(".cash-pay-control").removeClass("cur_pay_type");
								$(".transfer-pay-control").addClass("cur_pay_type");
								$(".transfer-pay-control").show();
								$(".cash-pay-control").hide();
							}
						}
						/***************财务已出账  显示财务操作结果****************************/
						if(payment_schedule >=3){
							$(".accountant_operate").show();//展示财务操作面板
							var accountant_content = "出账时间: " + infos.accountant_pay_time + "</br>";
							if(typeof(infos.invoice_title) != "undefined" && infos.invoice_title){
								accountant_content += "发票抬头: " + infos.invoice_title + "<br/>";
							}
							if(typeof(infos.accountant_comment) != "undefined" && infos.accountant_comment){
								accountant_content += "<span style='visibility:hidden;'>备注</span>备注: " 
									+ infos.accountant_comment;
							}
							$("#accountant_portlet_body").html(accountant_content);
							
						}
						/***************出纳已出账  显示财务操作结果****************************/
						if(payment_schedule >=4){
							if(infos.pay_type == 'cash'){
								$(".teller_operate").show();
								var teller_content = "出账时间: " + infos.teller_pay_time;
								if(typeof(infos.teller_comment) != "undefined" && infos.teller_comment){
									teller_content += "<span style='visibility:hidden;'>备注</span>备注: " 
										+ infos.teller_comment;

								}
								$("#teller_portlet_body").html(teller_content);
								
							}
						}
					}
			}
			if(msg.length > 0){
				$("#payment_info_panel").html(msg);
			}
			}
			
			$("#payment_info_panel").modal("show");
		},
		error:function(msg){

		}
	});
}

function get_related_tellers(id){
	$.ajax({
		url:get_related_tellers_api,
		type:"get",
		data:{id:id},
		dataType:"JSON",
		success:function(result){
			if(typeof(result.error_code) != "undefined"){
				//$("#payment_detail_lists").html('<tr><td colspan="10" style="text-align:center;"><span style="color:red;">'+result.error_msg+'</span></td></tr>');
			}
			else if(typeof(result.data) != "undefined" && result.data){
				var data = result.data;
				var items = data.items;
				var tellers_content = "";
				for(var i=0; i<items.length; i++){
					tellers_content += "<option value='" + items[i].account_id + "'>"
						+ items[i].name + "</option>";
				}
				$("#teller_id").html(tellers_content);
			}
		},
		error:function(msg){

		}
	});
}
//获取账户
function get_vaults(type, pay_type){
	$.ajax({
		url:get_vaults_api,
		type:"get",
		data:{type:type},
		dataType:"JSON",
		success:function(result){
			if(typeof(result.error_code) != "undefined"){
				//$("#payment_detail_lists").html('<tr><td colspan="10" style="text-align:center;"><span style="color:red;">'+result.error_msg+'</span></td></tr>');
			}
			else if(typeof(result.data) != "undefined" && result.data){
				var data = result.data;
				var items = data.items;
				var vaults = "";
				if(pay_type == "transfer"){//出账方式为“划账”，加载账户类型为“bank”
					for(var i=0; i<items.length; i++){
						if(items[i].type == "bank"){
							vaults += "<option value='" + items[i].v_number + "'>"
								+ items[i].alias + "</option>";
						}
					}
					$("#src_account_transfer").html(vaults);
				}
				if(pay_type == "cash"){//出账方式为“现金”，加载账户类型为“vault”
					for(var i=0; i<items.length; i++){
						if(items[i].type == "vault"){
							vaults += "<option value='" + items[i].v_number + "'>"
								+ items[i].alias + "</option>";
						}
					}
					$("#src_account_cash").html(vaults);
				}
				
			}
		},
		error:function(msg){

		}
	});
}

//获取发票抬头
function get_invoice_title(){
	$.ajax({
		url: get_invoice_title_api,
		type:"get",
		data:{},
		dataType:"JSON",
		success:function(result){
			if(typeof(result.error_code) != "undefined"){
				
			}
			else if(typeof(result.data) != "undefined" && result.data){
				var data = result.data;
				var items = data.items;
				var invoice_title = "";
				for(var i = 0; i < items.length; i++){
					invoice_title += "<option vaule='";
					invoice_title += items[i].id;
					invoice_title += "'>";
					invoice_title += items[i].title;
					invoice_title += "</option>";
				}
				$("#manager_invoice_title").html(invoice_title);
				$("#accountant_invoice_title").html(invoice_title);
			}
		},
		error:function(msg){
			
		}
	});
}

function switch_transfer_control(value){
	if(value == "transfer"){
		$(".transfer_control").show();
	}
	else{
		$(".transfer_control").hide();
	}
}
function switch_invoice_title_control(value){
	if($(".manager_operate .invoice_title_control").hasClass("no_invoice") == false){
		if(value == "agreed"){
			$(".manager_operate .invoice_title_control").show();
		}
		else{
			$(".manager_operate .invoice_title_control").hide();
		}
	}
}