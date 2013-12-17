function accordingList(parentId){
	$('#'+parentId+' ul').hide();
	$("#"+parentId+" span.groupName").click(function(){
		if($(this).parent().find('ul').css('display') == "none"){
			$(this).parent().find('ul').first().show();
		}else{
			$(this).parent().find('ul').hide();
		}
	});
}

function toggleCheckbox(className){
	$("."+className).change(function(){
		if($(this).is(":checked")){
			$(this).parent().find(':checkbox').attr('checked', this.checked);
		}else{
			$(this).parent().find(':checkbox').removeAttr('checked');
		}
	});
	
}


function buildUList(json){
	if(json.data == undefined || json.data.length == 0){
		return " ";
	}
	var jArray = json.data;
	var html = "";
	for(var i=0; i<jArray.length;i++){
		html += 	"<li><input type='checkbox' value='"+jArray[i].id+"' class='deptCheckbox'/><span class='groupName'>"+jArray[i].name+ "</span><br/>";
		html += "<ul>";
		html += buildUList(jArray[i]);
		if(jArray[i].emps != undefined && jArray[i].emps.length >0){
			for(var j=0;j<jArray[i].emps.length;j++){
				var str = jArray[i].emps[j];
				if(str != undefined){
					var strArr = str.split(",");
				}
				html += "<li><input type='checkbox' name='empCheckList' value='"+strArr[0]+"' class='nameCheckbox'/><span class='empName'>"  + strArr[1] +  "</span></li>";
			}
		}
		html += "</ul></li>";
	}
	return html;
}

function bindEventToDialogHtmlBody(){
	$("#selectPeopleDialog .ulParent").addClass("customRootUl");
	$("#selectPeopleDialog .ulParent ul").hide();
	$("#selectPeopleDialog span.groupName").click(function(){
		if($(this).parent().find('ul').css('display') == "none"){
			$(this).parent().find('ul').first().show();
		}else{
			$(this).parent().find('ul').hide();
		}
	});
}