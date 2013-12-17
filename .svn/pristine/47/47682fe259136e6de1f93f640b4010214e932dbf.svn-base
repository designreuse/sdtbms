<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="stripes" uri="http://stripes.sourceforge.net/stripes.tld" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="ss" uri="/WEB-INF/StripesSecurityManager.tld" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
<title>部门积分刷新</title>
</head>
<body>
<div>
<stripes:form beanclass="com.bus.stripes.actionbean.score.ScorehomeActionBean">
<table>
	<tr>
		<td>ID</td>
		<td>部门ID</td>
		<td>部门名字</td>
		<td>部门基础分</td>
		<td>部门新总分</td>
	</tr>
	<c:set var="color" value="0" scope="page"/>
	<c:forEach items="${actionBean.departScores}" var="dep" varStatus="loop">
		<tr>
			<td>${dep.id }<input type="hidden" name="depS[${color}].id" value="${dep.id }"/></td>
			<td>${dep.department.id }</td>
			<td>${dep.department.name }</td>
			<td>${dep.basescore }<input type="hidden" name="depS[${color}].basescore" value="${dep.basescore }"/></td>
			<td><input type="text" name="depS[${color}].available" value="${dep.available }"/></td>
		</tr>
		<c:set var="color" value="${color + 1}" scope="page"/>
	</c:forEach>
	<tr>
		<td colspan=5>
			<stripes:submit name="submitDepScore" value="确认更新"/>
		</td>
	</tr>
</table>
</stripes:form>
</div>
</body>
</html>