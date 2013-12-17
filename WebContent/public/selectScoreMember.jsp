<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="stripes" uri="http://stripes.sourceforge.net/stripes.tld" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="ss" uri="/WEB-INF/StripesSecurityManager.tld" %>
<%@taglib prefix="recur" tagdir="/WEB-INF/tags" %>
<html>
<head>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery-1.7.2.min.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/ui/ui.core.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/custom/scoreListBuilder.js"></script>
<link href="${pageContext.request.contextPath}/css/custom/scoreListBuilder.css" rel="stylesheet" title="style"/>
<style type="text/css">
body {
    padding:20px;
}
</style>

<script type="text/javascript">
	$(document).ready(function(){
		toggleCheckbox("select_children");
		accordingList("scoreGroupMemberUList");
	
		$('#quitPeople').click(function(){
			window.close();
		});
		$('#setPeople').click(function(){
			var name = "";
			var workerid = "";
			$("#scoreGroupMemberUList input.child:checked").each(function(){
				if(workerid == "")
					workerid += $(this).val();
				else
					workerid += ","+$(this).val();
				if(name == "")
					name += $(this).next().html();
				else
					name += ","+$(this).next().html();
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
		$('#findPpl').click(function(){
			$('#scoreGroupMemberUList ul').hide();
			$('#scoreGroupMemberUList ul li').hide();

			if($("#findPplStr").val() == ""){
				$('#scoreGroupMemberUList ul li').show();
				return;
			}
			
			$('.empName').each(function(){
				var name = $(this).html()+"";
				if(name.indexOf($('#findPplStr').val()) != -1){
					if($(this).parent().is("li")){
						$(this).parent().show();
						showUlAndLi($(this).parent());
					}
				}
			});
		});
		$('#findPplStr').change(function(){$('#findPpl').click();});
	});

	function showUlAndLi($li){
		if($li == undefined){
			alert("li is undefined");
			return;
		}
		$currentUl = $li.closest("ul");
		if($currentUl != undefined && $currentUl.is("ul")){
			$currentUl.show();
			if($currentUl.parent().is("li")){
				$currentUl.parent().show();
				showUlAndLi($currentUl.parent());
			}
		}
	}
</script>


</head>
<body>
	<div>
		<button id="setPeople">确定名单</button>
		<button id="quitPeople">取消</button>
		<div>
			名称:<input type="text" id="findPplStr"/><button id="findPpl">查找</button>
		</div>
		<div>
			<c:if test="${actionBean.empList != null }">
				<ul id="scoreGroupMemberUList" class="customRootUl">
					<recur:scoreMemberTreeNoButton treeRoot="${actionBean.empList}" />
				</ul>
			</c:if>
		</div>
	</div>
</body>
</html>