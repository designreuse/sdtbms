$(document).ready(function() {
    $('#checkworkerid').click(function(){
    	var getGenerationUrl = $(this).next().val();
    	var workerId = $(this).prev().val();
    	alert(workerId);
    	$.ajax({
    		url:getGenerationUrl+"&workerid="+workerId,
    		success:function(response){
    			if(response.trim() == "1")
    				alert("员工存在");
    			else
    				alert("员工不存在");
    		},
    		error:function(response){
    			alert("工号检查失败,请手动检查");
    		}
    	});
    });
    
    $('#accountgroups').click(function(){
    	var value = $(this).prev().children('option:selected').val();
    	var url = $(this).next().val();
    	url = url + "&targetId="+value;
    	window.open(url, "accountdetails:"+value, ["width=650,height=400,scrollbars=yes"]);
    });
    
    $('#groupactions').click(function(){
    	var value = $(this).prev().children('option:selected').val();
    	var url = $(this).next().val();
    	url = url + "&targetId="+value;
    	window.open(url, "groupactions:"+value, ["width=650,height=400,scrollbars=yes"]);
    })
 });