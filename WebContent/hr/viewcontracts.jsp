<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="stripes" uri="http://stripes.sourceforge.net/stripes.tld" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
<title>${actionBean.contracts[0].employee.fullname}</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />

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


<link href="${pageContext.request.contextPath}/css/hr.css" rel="stylesheet" media="all" /> 
<script type="text/javascript">
$(document).ready(function(){
	disableAll();
    $(".datepickerClass").datepicker({
    	changeMonth: true,
		changeYear: true,
		dateFormat: 'yy-mm-dd' 
    });

	$("form[name=form_all_contract]").each(function(){
		$(this).submit(function(){
			return false;
		})
	});
    $(".btneditcontract").click(function(){
    	var value = $(this).attr('value');
    	if(value == "none"){
			$(this).attr("value","editting");
			$(this).parent().parent().children("td").each(function(){
				$(this).children("input").each(function(){
					$(this).prop("disabled",false);
					
				});
				$(this).children("select").each(function(){
					$(this).prop("disabled",false);
				});
			});
        }else{
			//submitting
			var params = $(this).parent().parent().prev().serialize();
			var url = $(this).parent().parent().prev().attr("action") + "?edit=";
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
			
			$(this).attr('value',"none");
			$(this).parent().parent().children("td").each(function(){
					$(this).children("input").each(function(){
						$(this).prop("disabled",true);
					});
					$(this).children("select").each(function(){
						$(this).prop("disabled",true);
					});
			});
			
        }
    });

   
    
});

function disableAll(){
	$("input").each(function(){
		$(this).prop("disabled",true);
	});

	$("select").each(function(){
		$(this).prop("disabled",true);		
	});
}
</script>


</head>
<body>
<div >
	<div class="hastable">
				
				<table>
					<thead>
						<tr>
						<th ></th>
						<th >Id</th>
						<th >合同种类</th>
						<th >员工档案</th>
						<th >员工名称</th>
						<th >试用期限</th>
						<th >开始日期</th>
						<th >结束日期</th>
						<th >生效日期</th>
						<th >注释 </th>
						</tr>
					</thead>
					<tbody>
					<c:set var="color" value="0" scope="page"/>
					<c:set var="statusD" value="D" scope="page"/>
					<c:set var="statusE" value="E" scope="page"/>
					<c:forEach items="${actionBean.contracts}" var="cont" varStatus="loop">
						<stripes:form name="form_all_contract" beanclass="com.bus.stripes.actionbean.ContractActionBean" focus="">
						<c:choose>
							<c:when test="${color%2 == 0}">
								<tr class="">
							</c:when>
							<c:otherwise>
								<tr class="alt">
							</c:otherwise>
						</c:choose>
							<td>
								<c:choose>
									<c:when test="${cont.status == statusD}">
										<label>已删除合同</label>
									</c:when>
									<c:when test="${cont.status == statusE}">
										<label>辞职,失效合同</label>
									</c:when>
									<c:otherwise>
										<input type="hidden" name="targetId" value="${cont.employee.id}"/>
										<button name="edit" value="none" class="btneditcontract">修改</button>
									</c:otherwise>
								</c:choose>
							</td>
							<td >
								<label>${cont.id}</label><stripes:hidden name="contract.id" value="${cont.id}"/>
							</td>
							<td ><stripes:select name="contract.type"><stripes:option value="${cont.type}">${cont.type}</stripes:option><stripes:options-collection collection="${actionBean.contracttype}" label="label" value="value"/></stripes:select></td>
							<td >${cont.employee.documentkey}</td>
							<td >${cont.employee.fullname}</td>
							<td ><stripes:text name="contract.probation" value="${cont.probation}"/></td>
							<td ><stripes:text name="contract.startdate"  formatPattern="yyyy-MM-dd" value="${cont.startdate}" class="datepickerClass"/></td>
							<td ><stripes:text name="contract.enddate"  formatPattern="yyyy-MM-dd" value="${cont.enddate}" class="datepickerClass"/></td>
							<td ><stripes:text name="contract.activedate"  formatPattern="yyyy-MM-dd" value="${cont.activedate}" class="datepickerClass"/></td>
							<td ><stripes:text name="contract.remark"  value="${cont.remark}"/></td>
						</tr>
						<c:set var="color" value="${color + 1}" scope="page"/>
						</stripes:form>
					</c:forEach>
					</tbody>
				</table>
			</div>
</div>
</body>
</html>