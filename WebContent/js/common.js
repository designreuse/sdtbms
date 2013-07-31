function formValidation(formid){
	var text = "";
	var valid = true;
	$('#'+formid).find('input').each(function(){
		if($(this).hasClass('required')){
			var value = $(this).val();
			if(value.trim() == ""){
				valid = false;
				var name = $(this).parent().prev().html();
				text += name + ",";
			}
		}
	});
	$('#'+formid).find('select').each(function(){
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