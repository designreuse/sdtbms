<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="stripes" uri="http://stripes.sourceforge.net/stripes.tld" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="ss" uri="/WEB-INF/StripesSecurityManager.tld" %>
<stripes:layout-render name="../default.jsp">
	
    <stripes:layout-component name="contents">
    <script type="text/javascript" src="${pageContext.request.contextPath}/js/hr.js"></script>
		<div id="sub-nav"><div class="page-title">
			<h1>人事管理</h1>
			<span><a href="javascript:void" title="Layout Options">人事</a> > <a href="javascript:void" title="Two column layout">部门管理</a> > 查看部门</span>
		</div></div>
		
		<div id="page-layout"><div id="page-content">
			<div id="page-content-wrapper">
			
			
			
			<!-- Side bar ****************************************************************** -->
			<jsp:include page="hrsidebar.jsp" />
		<!-- Side bar ****************************************************************** -->
				<!-- Start of main content -->
				<div id="rightcontent">
				
				<!-- 新建档案  Dialog-->
				<div id="hr_top_button_bar" style="height:40px;">&nbsp;  
				
				<a class="btn ui-state-default ui-corner-all" href="javascript:location.reload();">
					刷新
				</a>
				<ss:secure roles="employee_dept_add">
				<a id="btn_new_department_link" class="btn ui-state-default ui-corner-all" href="javascript:void">
						<span class="ui-icon ui-icon-suitcase"></span>
						新建部门
				</a>
				<div id="btn_new_department_dialog" title="新建部门">
				<!-- The Form to submit new employee data -->
				<stripes:form id="form_new_department" beanclass="com.bus.stripes.actionbean.DepartmentActionBean" focus="">
					<stripes:errors/>
					<table>
						<tr>
							<td>名字:</td><td><stripes:text name="department.name"/></td>
							<td>注释:</td><td><stripes:text name="department.remark"/></td>
						<tr>
					</table>
				</stripes:form>
				</div>
				</ss:secure>
				</div>
				<hr/>
				
				<!-- Display the table contentes -->
				<div class="inner-page-title">
					部门信息
				</div>
				<div class="hastable">
				
				<table>
					<thead>
						<tr>
						<th>Id</th>
						<th>名称</th>
						<th>注释</th>
						<th>操作</th>
						</tr>
					</thead>
					<tbody>
					<c:set var="color" value="0" scope="page"/>
					<c:forEach items="${actionBean.departments}" var="dpment" varStatus="loop">
						<c:choose>
							<c:when test="${color%2 == 0}">
								<tr>
							</c:when>
							<c:otherwise>
								<tr class="alt">
							</c:otherwise>
						</c:choose>
							<td>
								${dpment.id}
							</td>
							<td>${dpment.name}</td>
							<td>${dpment.remark}</td>
							<td>
								<stripes:form beanclass="com.bus.stripes.actionbean.DepartmentActionBean">
									<div>
										<input type="hidden" name="targetId" value="${dpment.id}"/>
										<ss:secure roles="employee_dept_rm">
										<stripes:submit name="delete" value="删除"/>
										</ss:secure>
									</div>
								</stripes:form>
							</td>
						</tr>
						<c:set var="color" value="${color + 1}" scope="page"/>
					</c:forEach>
					</tbody>
				</table>
				</div>
				</div>
				<!-- End of main content -->
			</div>
			<div class="clear"></div>
		</div>
	</div>
    </stripes:layout-component>
</stripes:layout-render>