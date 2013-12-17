<%@tag description="display the whole nodeTree" pageEncoding="UTF-8"%>
<%@taglib prefix="recur" tagdir="/WEB-INF/tags" %>
<%@attribute name="treeRoot" type="com.bus.util.EmpDepartments" required="true" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<c:if test="${treeRoot != null }">
	<c:choose>
		<c:when test="${treeRoot.extras == null}">
			<li>
				<input type="hidden" value="${treeRoot.deptId}"/><input type="checkbox" class="select_children"/><span class='groupName'>${treeRoot.dept}(${treeRoot.size})</span>
				<br/>
				<ul>
					<c:if test="${treeRoot.emps != null }">
						<c:forEach var="treeRootEmp" items="${treeRoot.emps}">
							<li><input type="checkbox" value="${treeRootEmp.workerid }" class="child"/><span class="empName">${treeRootEmp.fullname}</span></li>
						</c:forEach>
					</c:if>
				</ul>
			</li>
		</c:when>
		<c:otherwise>
			<li>
				<input type="hidden" value="${treeRoot.deptId}"/><input type="checkbox" class="select_children"/><span class="groupName">${treeRoot.dept}(${treeRoot.size})</span>
				<ul>
					<c:if test="${treeRoot.extras != null }">
						<c:forEach var="treeRootUnit" items="${treeRoot.extras}">
							<recur:scoreMemberTreeNoButton treeRoot="${treeRootUnit}"/>
						</c:forEach>
					</c:if>
					<c:if test="${treeRoot.emps != null }">
						<c:forEach var="treeRootEmp" items="${treeRoot.emps}">
							<li><input type="checkbox" value="${treeRootEmp.workerid }" class="child"/><span class="empName">${treeRootEmp.fullname}</span></li>
						</c:forEach>
					</c:if>
					
				</ul>
			</li>
		</c:otherwise>
	</c:choose>
</c:if>