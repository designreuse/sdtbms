<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="stripes" uri="http://stripes.sourceforge.net/stripes.tld" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="ss" uri="/WEB-INF/StripesSecurityManager.tld" %>
<%@ taglib tagdir="/WEB-INF/tags" prefix="recur" %>

<c:if test="${treeMap != null }">
	<c:choose>
		<c:when test="${treeMap.extras == null && treeMap.emps == null }">
			<li>
				${treeMap.dept}<input type="hidden" value="${treeMap.deptId}"/>
			</li>
		</c:when>
		<c:otherwise>
			<li><input type="checkbox" class="select_children"/>${treeMap.dept}(${treeMap.size})<input type="hidden" value="${treeMap.deptId}"/>
				<ul>
					<c:if test="${treeMap.emps != null }">
						<c:forEach var="treeMapEmp" items="${treeMap.emps}">
							<li><input type="checkbox" value="${treeMapEmp.id }" class="child"/>${treeMapEmp.fullname}</li>
						</c:forEach>
					</c:if>
					<c:if test="${treeMap.extras != null }">
						<c:forEach var="treeMapUnit" items="${treeMap.extras}">
							
						</c:forEach>
					</c:if>
				</ul>
			</li>
		</c:otherwise>
	</c:choose>
</c:if>
