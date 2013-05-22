<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="stripes" uri="http://stripes.sourceforge.net/stripes.tld" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
<title>${actionBean.group.name}</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />

<script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery-1.7.2.min.js"></script>
<script type="text/javascript">

</script>
</head>
<body>

<div>
	<div>用户组:${actionBean.group.name} 有以下权限</div>
	<hr/>
	<c:set var="count" value="0" scope="page"/>
	<stripes:form beanclass="com.bus.stripes.actionbean.account.AccountActionBean">
	<input type="hidden" name="targetId" value="${actionBean.group.id}"/>
	<c:forEach items="${actionBean.allactions}" var="action" varStatus="loop">
		<c:choose>
			<c:when test="${(count % 10) == 0}">
				<br/>
			</c:when>
			<c:otherwise>
			
			</c:otherwise>
		</c:choose>
		<c:choose>
			<c:when test="${action.checked == true}">
				<input type="checkbox" name="groupactions[${count}].id" value="${action.id}" checked="checked"/>
			</c:when>
			<c:otherwise>
				<input type="checkbox" name="groupactions[${count}].id" value="${action.id}"/>
			</c:otherwise>
		</c:choose>
				${action.description}
		<c:set var="count" value="${count + 1}" scope="page"/>
	</c:forEach>
	<hr/>
	<stripes:submit name="assignactionstogroup" value="更新权限"/>
	</stripes:form>
</div>

</body>
</html>