<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="stripes" uri="http://stripes.sourceforge.net/stripes.tld" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<stripes:layout-render name="../default.jsp">
	
    <stripes:layout-component name="contents">
    <script type="text/javascript" src="${pageContext.request.contextPath}/js/hr.js"></script>
		<div id="sub-nav"><div class="page-title">
			<h1>人事管理</h1>
			<span><a href="javascript:void" title="Layout Options">人事</a> > <a href="javascript:void" title="Two column layout">合同管理</a></span>
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
				</div>
				<hr/>
				
				<!-- Display the table contentes -->
				<div class="inner-page-title">
					合同信息
				</div>
				<div class="hastable">
				
				<table>
					<thead>
					<stripes:form beanclass="com.bus.stripes.actionbean.ContractActionBean" focus="">
						<tr>
							<td colspan=10 style="text-align:left">
								<stripes:submit name="prevpage" value="上页"/> 页码: <stripes:text name="pagenum"/> <stripes:hidden name="totalcount"/>  <stripes:submit name="nextpage" value="下页"/>
								显示数量:<stripes:text name="lotsize"/>
								<stripes:submit name="filter" value="提交"/>
							</td>
						</tr>
						<tr>
							<td colspan=10 style="text-align:left">
								<Label class='selector'>员工名字:</Label><stripes:text name="employeeselector.name"/>
								<Label class='selector'>合同类型:</Label><stripes:select name="contractselector.type"><stripes:option value="">不限</stripes:option><stripes:options-collection collection="${actionBean.contracttype}" label="label" value="value"/></stripes:select>
								<Label class='selector'>日期:</Label>
								<stripes:radio name="contractselector.dateselector" value="0"/>不限日期 
								<stripes:radio name="contractselector.dateselector" value="1"/>结束日期 
								<stripes:radio name="contractselector.dateselector" value="2"/>生效日期
								<stripes:radio name="contractselector.dateselector" value="3"/>试用期
						</tr>
						
					</stripes:form>
						<tr>
						<th></th>
						<th>Id</th>
						<th>合同种类</th>
						<th>员工档案</th>
						<th>员工名称</th>
						<th>试用期限</th>
						<th>转正日期</th>
						<th>开始日期</th>
						<th>结束日期</th>
						<th>生效日期</th>
						</tr>
					</thead>
					<tbody>
					<c:set var="color" value="0" scope="page"/>
					<c:set var="status" value="D" scope="page"/>
					<c:forEach items="${actionBean.contracts}" var="cont" varStatus="loop">
						<c:choose>
							<c:when test="${color%2 == 0}">
								<tr>
							</c:when>
							<c:otherwise>
								<tr class="alt">
							</c:otherwise>
						</c:choose>
							<td>
								<input type="hidden" name="targetId" value="${cont.id}"/>
								<button name="btn_view"  class="btn_contract_view_employee" value="/bms/actionbean/Employee.action">查看员工</button>
								<button name="btn_view_contract" class="btn_contract_view_all" value="/bms/actionbean/Contract.action">所有合同</button>
								<c:choose>
									<c:when test="${cont.status == status}">
										<label>已删除合同</label>
									</c:when>
									<c:otherwise>
										<button name="btn_delete_contract" class="btn_contract_delete" value="/bms/actionbean/Contract.action">删除合同</button>
									</c:otherwise>
								</c:choose>
								
							</td>
							<td>
								${cont.id}
							</td>
							<td>${cont.type}</td>
							<td>${cont.employee.documentkey}</td>
							<td>${cont.employee.fullname}</td>
							<td>${cont.probation}</td>
							<td>${cont.probationdate }</td>
							<td>${cont.startdatestr}</td>
							<td>${cont.enddatestr}</td>
							<td>${cont.activedatestr}</td>
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