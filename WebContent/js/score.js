$(document).ready(function(){
				//New Item button and dialog
    $("#btn_new_item_link").click(function() {
    		$('#btn_new_item_dialog').dialog('open');
    			return true;
    		});
    buildNewItemDialog('form_new_item','btn_new_item_dialog',450,300,'&createscoretype=');
    
    		//New item sheet button and dialog
    	$('#btn_new_itemsheet_link').click(function(){
    			$('#btn_new_itemsheet_dialog').dialog('open');
    			return true;
    		});
    buildNewItemDialog('form_new_itemsheet','btn_new_itemsheet_dialog',450,300,'&createSheet=');
    	
    $('#checkWorkerId').click(function(){
    	var getGenerationUrl = $(this).next().val();
    	var workerId = $(this).prev().val();
    	$.ajax({
    		url:getGenerationUrl+"&workerid="+workerId,
    		success:function(response){
    			if(response == "1")
    				alert("工号存在");
    			else
    				alert("工号不存在");
    		},
    		error:function(response){
    			alert("工号检查失败,请手动检查");
    		}
    	});
    });
    
    $("#getNameById1").click(function(){
    	var getNameByIdUrl = $(this).next().val();
        var workerId = $(this).prev().prev().prev().val();
        getNameByIdUrl += "&workerid="+workerId;
        $.ajax({
    		url:getNameByIdUrl,
    		success:function(response){
    			$('#employeenamefromid1').val(response);
    		},
    		error:function(response){
    			alert("员工工号不存在或存在重复");
    		}
    	});
    });
    $("#getNameById2").click(function(){
    	var getNameByIdUrl = $(this).next().val();
        var workerId = $(this).prev().prev().prev().val();
        getNameByIdUrl += "&workerid="+workerId;
        $.ajax({
    		url:getNameByIdUrl,
    		success:function(response){
    			$('#employeenamefromid2').val(response);
    		},
    		error:function(response){
    			alert("员工工号不存在或存在重复");
    		}
    	});
    });
    

    $("#getNameById3").click(function(){
    	var getNameByIdUrl = $(this).next().val();
        var workerId = $(this).prev().val();
        getNameByIdUrl += "&workerid="+workerId;
        $.ajax({
    		url:getNameByIdUrl,
    		success:function(response){
    			$('#employeenamefromid1').val(response);
    		},
    		error:function(response){
    			alert("员工工号不存在或存在重复");
    		}
    	});
    });
});


function buildNewItemDialog(formId, dialogId, width, height, extra){
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
					if(!scoreNewItemValidation(formId)){
						return;
					}
					var form = $('#'+formId);
					var params = $('#'+formId).serialize() + extra;
					$.ajax({
						url:form.action,
						type:"post",
						dataType:'text',
						data:params,
						success:function(response){
							$("#"+dialogId).dialog('close');
							alert(response);
							location.reload();
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

function scoreNewItemValidation(formId){
	var text = "";
	var valid = true;
	$('#'+formId).find('input').each(function(){
		if($(this).hasClass('required')){
			var value = $(this).val();
			if(value.trim() == ""){
				valid = false;
				var name = $(this).parent().prev().html();
				text += name + ",";
			}
		}
	});
	$('#'+formId).find('select').each(function(){
		if($(this).hasClass('required')){
			var value = $(this).val();
			if(value.trim() == ""){
				valid = false;
				var name = $(this).parent().prev().html();
				text += name + ",";
			}
		}
	});
	if(text != "")
		alert("请输入 " + text);
	return valid;
}

function deleteVoucher(vlurl){
	$.ajax({
		url:vlurl,
		type:"post",
		dataType:'text',
		success:function(response){
			alert(response);
			location.reload();
		},
		error:function(response){
			alert("删除失败");
		}
	});
}