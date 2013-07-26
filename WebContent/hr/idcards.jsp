<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="stripes" uri="http://stripes.sourceforge.net/stripes.tld" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="ss" uri="/WEB-INF/StripesSecurityManager.tld" %>

<html>
<head>
<title>证件管理</title>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery-1.7.2.min.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/ui/ui.core.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/superfish.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/live_search.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/tooltip.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/cookie.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/ui/ui.sortable.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/ui/ui.draggable.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/ui/ui.resizable.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/ui/ui.dialog.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/custom.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/ui/ui.tabs.js"></script>

<link href="${pageContext.request.contextPath}/css/ui/ui.base.css" rel="stylesheet" media="all" />

<link href="${pageContext.request.contextPath}/css/themes/black_rose/ui.css" rel="stylesheet" title="style"
	media="all" />
<script type="text/javascript" src="${pageContext.request.contextPath}/js/ui/ui.datepicker.js"></script>   
<link href="${pageContext.request.contextPath}/css/custom_general.css" rel="stylesheet" media="all" />


<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<script type="text/javascript">
$(document).ready(function(){
    $(".datepickerClass").datepicker({
    	changeMonth: true,
		changeYear: true,
		dateFormat: 'yy-mm-dd' 
    });

    $("form[class=form_all_idcards]").each(function(){
		$(this).submit(function(){
			return false;
		});
	});

	$(".btndeleteidcard").click(function(){
		var value = $(this).attr('value');
		var href =  window.location.href;
		var targetId = $(this).prev().val();
		$.ajax({
			url:value,
			type:"post",
			dataType:'text',
			success:function(response){
				alert(response);
				if(href.indexOf("targetId") != -1)
					window.location.href = href;
				else
					window.location.href = href+"?targetId="+targetId;
			},
			error:function(response){
				alert(response);
			}
		});
	});
    
    $(".btneditidcards").click(function(){
    		var params = $(this).parent().prev().serialize();
    		var url = $(this).parent().prev().attr("action") + "?edit=";
    		$.ajax({
    			url:url,
    			type:"post",
    			dataType:'text',
    			data:params,
    			success:function(response){
    				console.log("ajax response = "+response);
    				alert(response);
    			},
    			error:function(response){
    				alert("errors");
    			}
    		});
    });
    
	$('.view_jpg').click(function(){
		var val = $(this).next().val();
// 		alert(val);
		window.open(val);
	});

	callAjaxForEdit("delete_image");
});


function callAjaxForEdit(hyperLinkClassName){
	$('.'+hyperLinkClassName).click(function(){
		var elem = $(this);
		var action = $(this).next().val();
		$.ajax({
			url:action,
			type:"post",
			dataType:'text',
			success:function(response){
				alert(response);
// 				location.reload();
				$(elem).parent().parent().children(":first-child").html("");
			},
			error:function(response){
				alert(response);
				location.reload();
			}
		});
    });
}
</script>


<style type="text/css">
.centertext{
text-align:center;
}
</style>

</head>
<body>

	<div class="hastable">
		<div><Label>${actionBean.idcards[0].employee.fullname}</labeL></div>
		<table>
			<thead>
						<tr>
						<th></th>
						<th>Id</th>
						<th>类型</th>
						<th>号码</th>
						<th>初次获得证件日期</th>
						<th>有效日期</th>
						<th>注释</th>
						<th>图片</th>
						</tr>
					</thead>
					<tbody>
					
					<c:set var="color" value="0" scope="page"/>
					<c:forEach items="${actionBean.idcards}" var="card" varStatus="loop">
						
						<c:choose>
							<c:when test="${color%2 == 0}">
								<tr>
							</c:when>
							<c:otherwise>
								<tr class="alt">
							</c:otherwise>
						</c:choose>
							<stripes:form class="form_all_idcards" beanclass="com.bus.stripes.actionbean.IDCardsActionBean">
							<td>
										<stripes:hidden name="idcard.id" value="${card.id}"/>
										<stripes:hidden name="targetId"/>
										<ss:secure roles="employee_idcards_rm">
											<button value="${pageContext.request.contextPath}/actionbean/IDCards.action?delete=&idcard.id=${card.id}" class="btndeleteidcard">删除</button>
										</ss:secure>
										<ss:secure roles="employee_idcards_add">
											<button name="edit" value="none" class="btneditidcards">修改</button>
										</ss:secure>
								
							</td>
							<td>
								${card.id}<stripes:hidden name="idcard.id" value="${card.id}"/>
							</td>
							<td>${card.type}</td>
							<td><stripes:text name="idcard.number" value="${card.number}"/></td>
							<td><stripes:text name="idcard.validfrom" value="${card.validfromstr}"  formatPattern="yyyy-MM-dd" class="datepickerClass"/></td>
							<td><stripes:text name="idcard.expiredate" value="${card.expiredatestr}"  formatPattern="yyyy-MM-dd" class="datepickerClass"/></td>
							<td><stripes:text name="idcard.remark" value="${card.remark}"/></td>
							</stripes:form>
							<td>
								<c:if test="${card.image != null && card.image.name != null}">
									<a class="view_jpg" href="javascript:void;">查看</a><input type="hidden" value="${actionBean.context.hrhostidfile}${card.type}/${card.image.name}"/>
								</c:if>
								<ss:secure roles="employee_idcards_file_upload">
								<stripes:form beanclass="com.bus.stripes.actionbean.IDCardsActionBean">
									<input type="hidden" name="cardId" value="${card.id}"/>
									<stripes:hidden name="targetId"/>
									<stripes:file name="idfile"/><a class="delete_image" href="javascript:void;">删除</a><input type="hidden" value="${pageContext.request.contextPath}/actionbean/IDCards.action?targetId=${actionBean.targetId}&cardId=${card.id}&idfileDelete="/><stripes:submit name="idfileUpload" value="上传"/>
								</stripes:form>
								</ss:secure>
							</td>
						</tr>
						
						<c:set var="color" value="${color + 1}" scope="page"/>
					</c:forEach>
					</tbody>
				</table>
				
				<br/>
				<br/>
				<br/>
				
				<div class="hastable">
				<table>
					<thead>
						<tr>
						<th></th>
						<th>类型</th>
						<th>号码</th>
						<th>初次获得证件日期</th>
						<th>有效日期</th>
						<th>注释</th>
						</tr>
					</thead>
					<tbody>
					<tr>
						<stripes:form beanclass="com.bus.stripes.actionbean.IDCardsActionBean">
							<td>
								<stripes:hidden name="targetId"/>
								<ss:secure roles="employee_idcards_add">
								<stripes:submit name="create" value="增加"/>
								</ss:secure>
							</td>
							<td>
								<stripes:select name="newidcard.type"><stripes:option value="">请选择....</stripes:option><stripes:options-collection collection="${actionBean.typeoptions}" label="label" value="value"/></stripes:select>
							</td>
							<td><stripes:text name="newidcard.number" /></td>
							<td><stripes:text name="newidcard.validfrom"  formatPattern="yyyy-MM-dd" class="datepickerClass"/></td>
							<td><stripes:text name="newidcard.expiredate"  formatPattern="yyyy-MM-dd" class="datepickerClass"/></td>
							<td><stripes:text name="newidcard.remark" /></td>
						</stripes:form>
						</tr>
						</tbody>
				</table>
				</div>
	</div>
	
</body>
</html>