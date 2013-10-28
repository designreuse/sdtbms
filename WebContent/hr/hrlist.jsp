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
			<span><a href="#" title="Layout Options">人事</a> > <a href="#" title="Two column layout">属性设置</a></span>
		</div></div>
		
		<div id="page-layout"><div id="page-content">
			<div id="page-content-wrapper">
			
			
			
			<!-- Side bar ****************************************************************** -->
			<jsp:include page="/hr/hrsidebar.jsp" />
			
			
		<!-- Side bar ****************************************************************** -->
				<!-- Start of main content -->
				<div id="rightcontent">
				
								
				<!--  File Upload -->
				<div class="clear"></div>
			
				<!--  Display Employee   -->
				<div>
					<div class="inner-page-title">
						属性内容设置
					</div>
					<div class="hastable">
				
				<table>
					<thead>
						<tr>
						<th>属性名字</th>
						<th>属性内容</th>
						<th>新增内容</th>
						<th>删除/添加</th>
						</tr>
					</thead>
					<tbody>
					
					<c:set var="color" value="0" scope="page"/>
					<c:forEach items="${actionBean.fixoptions}" var="opt" varStatus="loop">
					<stripes:form beanclass="com.bus.stripes.actionbean.HRListEditorActionBean">
						<c:choose>
							<c:when test="${color%2 == 0}">
								<tr>
							</c:when>
							<c:otherwise>
								<tr class="alt">
							</c:otherwise>
						</c:choose>
							<td>${opt.name}<input type="hidden" name="optionlistid" value="${opt.id}"/></td>
							<td><stripes:select name="optionlistselectedvalue"><stripes:options-collection collection="${opt.optionList}" label="label" value="value"/></stripes:select></td>
							<td><stripes:text name="optionlistnewdata"/></td>
							<td>
								<ss:secure roles="employee_property_list_rm">
								<stripes:submit name="deletenewproperty" value="删除"/>
								</ss:secure>
								
								<ss:secure roles="employee_property_list_add">
								<stripes:submit name="createnewproperty" value="添加"/>
								</ss:secure>
							</td>
						</tr>
						</stripes:form>
						<c:set var="color" value="${color + 1}" scope="page"/>
					</c:forEach>
					<stripes:form beanclass="com.bus.stripes.actionbean.HRListEditorActionBean">
					<tr>
						<td>职称</td>
							<td><stripes:select name="optionlistselectedvalue"><stripes:options-collection collection="${actionBean.workertypes}" label="label" value="value"/></stripes:select></td>
							<td><stripes:text name="optionlistnewdata"/></td>
							<td>
								<ss:secure roles="employee_property_list_rm">
								<stripes:submit name="deleteworkertype" value="删除"/>
								</ss:secure>
								
								<ss:secure roles="employee_property_list_add">
								<stripes:submit name="createnewworkertype" value="添加"/>
								</ss:secure>
							</td>
					</tr>
					</stripes:form>
					<stripes:form beanclass="com.bus.stripes.actionbean.HRListEditorActionBean">
					<tr>
						<td>文化程度(学历)</td>
							<td><stripes:select name="optionlistselectedvalue"><stripes:options-collection collection="${actionBean.qualifications}" label="name" value="id"/></stripes:select></td>
							<td><stripes:text name="optionlistnewdata"/></td>
							<td>
								<ss:secure roles="employee_property_list_rm">
								<stripes:submit name="deleteQualification" value="删除"/>
								</ss:secure>
								
								<ss:secure roles="employee_property_list_add">
								<stripes:submit name="createQualification" value="添加"/>
								</ss:secure>
							</td>
					</tr>
					</stripes:form>
					</tbody>
				</table>
				</div>
				</div>
				
				</div>
				<!-- End of main content -->
			</div>
			<div class="clear"></div>
		</div>
	</div>
    </stripes:layout-component>
</stripes:layout-render>