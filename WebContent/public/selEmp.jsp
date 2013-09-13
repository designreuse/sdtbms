<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="stripes" uri="http://stripes.sourceforge.net/stripes.tld" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="ss" uri="/WEB-INF/StripesSecurityManager.tld" %>
<html>
<head>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery-1.7.2.min.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/ui/ui.core.js"></script>

<style type="text/css">
body {
    padding:20px;
}
.open {
    display:block;
}
ul ul {
    margin-left:20px;
}
.choosable{
	cursor:pointer;
    color:blue;
    text-decoration:underline;
}
.choosable:hover{
	color:red;
}
</style>

<script type="text/javascript">
var nameSeries = "";
var workeridSeries = "";

$(document).ready(function(){
//get a reference to the nav <ul>
$nav = $("#nav");

//hide all but the first sub list
$nav.find("ul").hide();
$nav.find("li").show();

//mark the first sub list as open
// $nav.find("ul:first").addClass("open");

//receive clicks on the section links
$nav.on("click", "li>a", function(e) {

	  //get a reference to the nav <ul>
    $nav = $("#nav");
	$nav.find("ul").hide();
	$nav.find("li").show();
	
    //get a reference to the currently clicked link and its list item
    $this = $(this);
    $currentLi = $this.closest("li");

    //if our current section is open, just return
    if ($currentLi.find(".open").is("ul")) {
        //if you want a section link to be able to collapse itself, uncomment this line:
        $currentLi.find("ul.open").slideUp().removeClass("open")
        return;
    }


    
    //close all open lists
    $nav.find("ul.open").removeClass("open");

    //open the submenu that belongs to this section
    $currentLi.find("ul").slideDown().addClass("open");
});

$(".choosable").click(function(){
	var name = $(this).html();
	var workerid = $(this).next().val();
	window.opener.document.getElementById('${actionBean.eleIdOne}').value = name;
	var eleTwo = window.opener.document.getElementById('${actionBean.eleIdTwo}');
	eleTwo.value = workerid;
	eleTwo.readOnly = true;
	eleTwo.className = eleTwo.className + " readonly";
	window.close();
});

$(".selectAll").click(function(){
	if($(this).is(":checked")){
		$(this).parent().find(':checkbox').attr('checked', this.checked);
	}else{
		$(this).parent().find(':checkbox').removeAttr('checked');
	}
});

$("#setPeople").click(function(){
	var name = "";
	var workerid = "";
	$("ul#nav > li > ul > li").each(function(){
		if($(this).children().first().is(":checked")){
			if(name == "")
				name += $(this).children().first().next().html();
			else
				name += ","+$(this).children().first().next().html();
			if(workerid == "")
				workerid += $(this).children().first().next().next().val();
			else
				workerid += ","+$(this).children().first().next().next().val(); 
		}
	});
	if(name != "" && workerid != ""){
		window.opener.document.getElementById('${actionBean.eleIdOne}').value = name;
		var eleTwo = window.opener.document.getElementById('${actionBean.eleIdTwo}');
		eleTwo.value = workerid;
		eleTwo.readOnly = true;
		eleTwo.className = eleTwo.className + " readonly";
	}
	window.close();
});
$('#quitPeople').click(function(){
	window.close();
});

$('#findPpl').change(function(){
	$nav = $("#nav");
	$nav.find("ul").hide();
	$nav.find("li").show();

	if($(this).val() == "")
		return;
	
	$('.choosable').each(function(){
		var name = $(this).html()+"";
		if(name.indexOf($('#findPpl').val()) != -1){
			$currentUl = $(this).closest("ul");
			$currentUl.find("li").hide();
			$(this).parent().show();
			//open the submenu that belongs to this section
		    $currentUl.css("display","block");
		}
	});


});

});
</script>

</head>
<body>
<input type="hidden" id="multi" value="false"/>
<button id="setPeople">确定</button>
<button id="quitPeople">取消</button>
<div>
	查找名称:<input type="text" id="findPpl"/>
</div>
<ul id="nav">
<c:forEach items="${actionBean.departments}" var="dept" varStatus="loop">
	<li>
		<c:if test="${actionBean.multi}">
			<input type="checkbox" class="selectAll"/>
		</c:if>
		<a href="#">${dept.dept}(${dept.size})</a><input type="hidden" value="${dept.deptId}"/>
		<br/>
		<ul>
		<c:if test="${dept.extras != null}">
			<c:forEach items="${dept.extras}" var="ext" varStatus="loop">
				<li>
					<c:if test="${actionBean.multi}">
						<input type="checkbox" class="selectAll"/>
					</c:if>
				<a href="#">${ext.dept}(${ext.size})</a><input type="hidden" value="${ext.deptId}"/>
				<br/>
				<ul>
				<c:forEach items="${ext.emps}" var="extemp" varStatus="loop">
					<li>
						<c:if test="${actionBean.multi}">
							<input type="checkbox"/>
						</c:if>
						<span class="choosable">${extemp.fullname}</span><input type="hidden" value="${extemp.workerid}"/>
					</li>
				</c:forEach>
				</ul>
				</li>
			</c:forEach>
		</c:if>
		<c:forEach items="${dept.emps}" var="empy" varStatus="loop">
			<li>
				<c:if test="${actionBean.multi}">
					<input type="checkbox"/>
				</c:if>
				<span class="choosable">${empy.fullname}</span><input type="hidden" value="${empy.workerid}"/>
			</li>
		</c:forEach>
	</ul>
	</li>
</c:forEach>
</ul>
</body>
</html>