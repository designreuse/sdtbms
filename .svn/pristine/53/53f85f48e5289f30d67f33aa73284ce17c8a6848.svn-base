<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="stripes" uri="http://stripes.sourceforge.net/stripes.tld" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
<title>${actionBean.account.username}</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />

<script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery-1.7.2.min.js"></script>
<script type="text/javascript">

</script>
</head>
<body>

<div>
	<div>用户:${actionBean.account.username} , 工号:${actionBean.account.employee} 已经加入以下组群</div>
	<hr/>
	<c:forEach items="${actionBean.usergroups}" var="groups" varStatus="loop">
		<div>
			<stripes:form beanclass="com.bus.stripes.actionbean.account.AccountActionBean">
				<input type="hidden" name="usergroupid" value="${groups.value}"/>
				<input type="hidden" name="targetId" value="${actionBean.account.id}"/>
				${groups.label}
				<stripes:submit name="removeusergroup" value="移除"/>
			</stripes:form>
		</div>
	</c:forEach>
</div>

</body>
</html>