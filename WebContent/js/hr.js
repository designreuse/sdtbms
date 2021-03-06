$(document).ready(function() {
	//download the employees information as csv file
	$('#btn_download_employees').click(function(){
		var url = $(this).children('input').val();
		document.location.href= url +"?employees=";
	});
	
	$('#btn_download_drivers').click(function(){
		var url = $(this).children('input').val();
		document.location.href= url +"?drivers=";
	});
	
	//New Employee button and dialog
    $("#btn_new_document_link").click(function() {
    	$('#btn_new_document_dialog').dialog('open');
		return true;
    });
    buildBasicCreateDialog('form_new_employee','btn_new_document_dialog',800,550,'&create=创建&employee.account.id=1');
    
    //New department button and dialog
    $("#btn_new_department_link").click(function() {
    	$('#btn_new_department_dialog').dialog('open');
		return true;
    });
    buildBasicCreateDialog('form_new_department','btn_new_department_dialog',650,150,"&create=创建");
    
    //New position button and dialog
    $("#btn_new_position_link").click(function() {
    	$('#btn_new_position_dialog').dialog('open');
		return true;
    });
    buildBasicCreateDialog('form_new_position','btn_new_position_dialog',650,150,"&create=创建");
    
    //New coordinate button
    $("#btn_new_coordinate_link").click(function() {
    	$('#btn_new_coordinate_dialog').dialog('open');
		return true;
    });
    buildBasicCreateDialog('form_new_coordinate','btn_new_coordinate_dialog',500,350,"&create=创建");
    
    $(".form_function_list").each(function(){
    	$(this).submit(function(){
    		var submit = true;
    		$(this).find("input[clicked=true]").each(function(){
    			var name = $(this).attr("name");
            	var value= "";
            	var url= "";
            	if(name == "detail"){
            		name = $(this).parent().children().first().attr("name");
            		value = $(this).parent().children().first().attr("value");
            		url = window.location.pathname + "?"+name+"="+value+"&detail=";
//            		window.open(url, "Employee Detail ID:"+value, ["width=800,height=600,scrollbars=yes"]);
            		window.open(url);
            		submit = false;
            	}
            	else if(name == "contract"){
            		value = $(this).parent().children().first().attr("value");
            		$('#contract_target_id').val(value);
            		$('#contract_label_name').html("员工:" + $(this).parents('td').next().next().html());
            		$('#btn_new_contract_dialog').dialog('open');
            		submit=false;
            	}
            	else if(name == "idcards"){
            		name = $(this).parent().children().first().attr("name");
            		value = $(this).parent().children().first().attr("value");
            		
//            		name = $(this).prev().prev().prev().prev().attr("name");
//            		value = $(this).prev().prev().prev().prev().attr("value");
            		var pathname = window.location.pathname;
            		var newpath = pathname.substring(0, pathname.indexOf("Employee.action", 0));
            		url = newpath +"IDCards.action" + "?"+name+"="+value;
//            		window.open(url, "Employee identity cards:"+value, ["width=800,height=400,scrollbars=yes"]);
            		window.open(url);
            		submit =false;
            	}
            	else if(name == "getresign"){
            		value = $(this).parent().children().first().attr("value");
            		$('#resignemployeeId').val(value);
            		$('#btn_resign_date_dialog').dialog("open");
            		submit = false;
            	}
            	else if(name == "getrejoin"){
            		value = $(this).parent().children().first().attr("value");
            		$('#rejoinemployeeId').val(value);
            		$('#btn_rejoin_date_dialog').dialog("open");
            		submit = false;
            	}
    		});
    		return submit;
    	});
    });  

    $(".form_function_list input[type=submit]").click(function() {
        $("input[type=submit]", $(this).parents("form")).removeAttr("clicked");
        $(this).attr("clicked", "true");
    });
    
    
    $('#btn_download_month_report').click(function(){
    	$('#btn_download_report_dialog').dialog("open");
    	return true;
    });
    
    $('#btn_download_report_dialog').dialog({
    	autoOpen: false,
		bgiframe: true,
		resizable: true,
		height:400,
		width:500,
		modal: true,
		overlay: {
			backgroundColor: '#000',
			opacity: 0.5
		},
		buttons: {
			'取消': function() {
				$(this).dialog('close');
			}
		}
    });

    $('#btn_resign_date_dialog').dialog({
    	autoOpen: false,
		bgiframe: true,
		resizable: true,
		height:125,
		width:400,
		modal: true,
		overlay: {
			backgroundColor: '#000',
			opacity: 0.5
		},
		buttons: {
			'取消': function() {
				$(this).dialog('close');
			}
		}
    });
    
    $('#btn_rejoin_date_dialog').dialog({
    	autoOpen: false,
		bgiframe: true,
		resizable: true,
		height:125,
		width:400,
		modal: true,
		overlay: {
			backgroundColor: '#000',
			opacity: 0.5
		},
		buttons: {
			'取消': function() {
				$(this).dialog('close');
			}
		}
    });
    
    $("#getNameById").click(function(){
    	var getNameByIdUrl = $('#getNameById').next().val();
        var workerId = $('#getNameById').prev().val();
        getNameByIdUrl += "&workerid="+workerId;
        $.ajax({
    		url:getNameByIdUrl,
    		success:function(response){
//    			alert(response);
    			$('.employeenamefromid').val(response.trim());
    		},
    		error:function(response){
    			alert("员工工号不存在或存在重复");
    		}
    	});
    });
    
    $('#getNewWorkerId').click(function(){
    	var getGenerationUrl = $(this).next().val();
    	$.ajax({
    		url:getGenerationUrl,
    		success:function(response){
    			alert(response);
//    			$('.employeenamefromid').val(response);
    		},
    		error:function(response){
    			alert("员工工号生成失败");
    		}
    	});
    });
    
    $('#checkWorkerId').click(function(){
    	var getGenerationUrl = $(this).next().val();
    	var workerId = $(this).prev().val();
    	$.ajax({
    		url:getGenerationUrl+"&workerid="+workerId,
    		success:function(response){
    			if(response.trim() == "1")
    				alert(response.trim() + " 工号已经存在");
    			else
    				alert(response.trim() + "工号可用");
    		},
    		error:function(response){
    			alert("工号检查失败,请手动检查");
    		}
    	});
    });
 });


$(document).ready(function(){
    $(".btn_contract_view_employee").click(function(){
    	var url = $(this).val();
    	var targetId = $(this).parent().children().first().val();
    	url += "?targetId="+targetId +"&detail=";
    	window.open(url, "", "width=650,height=400,scrollbars=yes");
    });
	$(".btn_contract_view_all").click(function(){
		var url = $(this).val();
    	var targetId = $(this).parent().children().first().val();
    	url += "?employeeId="+targetId +"&viewall=";
    	window.open(url, ""+targetId, ["width=1200,height=400,scrollbars=yes"]);
	});
	$(".btn_contract_delete").click(function(){
		var url = $(this).val();
		var targetId = $(this).parent().children().first().next().val();
		var params = "targetId=" + targetId + "&delete=";
		$.ajax({
			url:url,
			type:"post",
			dataType:'text',
			data:params,
			success:function(response){
				var json = $.parseJSON(response);
				alert(json.msg);
				if(json.result=="1")
					location.reload();
			},
			error:function(response){
				alert("删除合同失败");
			}
		});
	});
	$(".btn_contract_resign").click(function(){
		var url = $(this).val();
		
		var targetId = $(this).parent().children().first().next().val();
		alert("sending :"+targetId);
		var params = "targetId=" + targetId + "&resignContract=";
		$.ajax({
			url:url,
			type:"post",
			dataType:'text',
			data:params,
			success:function(response){
//				console.log("ajax response = "+response);
				alert(response);
				location.reload();
			},
			error:function(response){
				alert("结束合同失败");
			}
		});
	});
});

$(document).ready(function() {
	$('.textnotedit').each(function(){
		$(this).attr('readonly',true);
	});
});

$(document).ready(function() {
	$("#btn_new_contract_dialog").dialog({
		autoOpen: false,
		bgiframe: true,
		resizable: true,
		height:250,
		width:500,
		modal: true,
		overlay: {
			backgroundColor: '#000',
			opacity: 0.5
		},
		buttons: {
			'新建':function(){
					var form = $('#form_new_contract');
					var params = $('#form_new_contract').serialize() + "&createcontract=";
					$.ajax({
						url:form.action,
						type:"post",
						dataType:'text',
						data:params,
						success:function(response){
							console.log("ajax response = "+response);
							$("#btn_new_contract_dialog").dialog('close');
							alert(response);
							clearFormTextBox("form_new_contract");
						},
						error:function(response){
							alert("errors");
						}
					});
			},
			'取消': function() {
				$(this).dialog('close');
			}
		}
	});
});

function clearFormTextBox(form){
//	alert("clearing");
	$('#'+form + ' input[type=text],select').each(function(){
//		alert($(this).val());
		$(this).val('');
	});
}

function buildBasicCreateDialog(formId, dialogId, width, height, extraParam){
	$("#"+dialogId).dialog({
		autoOpen: false,
		bgiframe: true,
		resizable: true,
		height:height,
		width:width,
		modal: true,
		overlay: {
			backgroundColor: '#000',
			opacity: 0.5
		},
		buttons: {
			'新建':function(){
					$("#empIdCardTable input:text").each(function(){
						if($.trim($(this).val()) == "")
							$(this).val("0");
					});
					if(!hrNewDocumentValidation(formId)){
						return;
					}
					var form = $('#'+formId);
					var params = $('#'+formId).serialize() + extraParam;
					$.ajax({
						url:form.action,
						type:"post",
						dataType:'text',
						data:params,
						success:function(response){
							var json = $.parseJSON(response);
							if(json != null && json != undefined){
								alert(json.msg);
								if(json.result == "1"){
									$("#"+dialogId).dialog('close');
									clearFormTextBox(formId);
									location.reload();
								}
							}
						},
						error:function(response){
							alert("errors");
						}
					});
			},
			'取消': function() {
				$(this).dialog('close');
			}
		}
	});
}

function deleteDepartment(dpid){
	alert("delete "+dpid);
	$.ajax({
		url:form.action,
		type:"post",
		dataType:'text',
		data:params,
		success:function(response){
			console.log("ajax response = "+response);
			$("#"+dialogId).dialog('close');
			alert(response);
			clearFormTextBox(formId);
		},
		error:function(response){
			alert("errors");
		}
	});
}

function hrNewDocumentValidation(formid){
	var text = "";
	var valid = true;
	$('#'+formid).find('input').each(function(){
		if($(this).hasClass('required')){
			var value = $(this).val();
			if($.trim(value) == ""){
				valid = false;
				var name = $(this).parent().prev().html();
				text += name + ",";
			}
		}
	});
	
	$('#'+formid).find('select').each(function(){
		if($(this).hasClass('required')){
			var value = $(this).val();
			if($.trim(value) == ""){
				valid = false;
				var name = $(this).parent().prev().html();
				text += name + ",";
			}
		}
	});
	
	if(text != "")
		alert("请输入 " + text);
	return valid;
	
	var ifblur = true;
	if(ifblur = validate('job',0,'6','请输入正确的员工工号')){
		if(ifblur = validate('idcode',1,'','请输入15或者18位的身份证号')){
			if(ifblur = validate('email',2,'','请输入有效的邮箱地址')){
				if(ifblur = validate('postcode',3,'6','请输入6位邮政编码')){
					
				}
			}	
		}
	}
	if(!ifblur)
		return false;
}

//验证工号、身份证、邮箱、邮编
function validate(inputId,checkType,check,errorMsg){
	var ele = $('#'+inputId);
	if(ele == null || ele == undefined)
		return true;
	if(checkType == 0){
		if($(ele).val() != "" && $(ele).val().length != check){
			alert(errorMsg);
			return false;
		}
	}
	else if(checkType == 1){
		if($(ele).val().length != 15 && $(ele).val().length != 18){
			alert(errorMsg);
			return false;
		}
	}else if(checkType == 2){
		var myreg = /^([a-zA-Z0-9]+[_|\_|\.]?)*[a-zA-Z0-9]+@([a-zA-Z0-9]+[_|\_|\.]?)*[a-zA-Z0-9]+\.[a-zA-Z]{2,3}$/;
		if($(ele).val() != "" && !myreg.test($(ele).val())){ //检查邮箱是否按照表达式的格式填写
				alert(errorMsg);
			return false;
		}
	}else if(checkType == 3){
		if($(ele).val() != "" && $(ele).val().length != check){
			alert(errorMsg);
			return false;
		}
	}else if(checkType > 3 || checkType < 0){
		if(showDialog)
			alert(checkType + ' 不是有效的检查类型');
	return false;
}
return true;
}
