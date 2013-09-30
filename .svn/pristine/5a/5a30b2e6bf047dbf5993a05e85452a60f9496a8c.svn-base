<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="stripes" uri="http://stripes.sourceforge.net/stripes.tld" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="ss" uri="/WEB-INF/StripesSecurityManager.tld" %>
<stripes:layout-render name="../default.jsp">
	 <stripes:layout-component name="contents">
	 <style type="text/css">
		table.data thead tr th{
			border: 1px solid rgb(0, 0, 0);
			border-collapse:collapse;
			background-color:#616161;
			color:black;
			padding:5px;
			font-size:15pt;
			text-align:center;
		}
		table.data tbody tr td{
			border: 1px solid rgb(0, 0, 0);
			border-collapse:collapse;
			padding:5px;
			font-size:12pt;
			height:25px;
			vertical-align:middle;
			text-align:center;
		}
		table.data tr.alt{
			background-color:rgb(200,200,200);
		}
		table.data tbody tr:hover{
			background-color:yellow;
		}
		
	</style>
	<script type="text/javascript">
		
	</script>
	  <div id="sub-nav"><div class="page-title">
			<h1>审核条例</h1>
		</div></div>
		
		<div id="page-layout"><div id="page-content">
			<div id="page-content-wrapper">
			
			
			
			<!-- Side bar ****************************************************************** -->
			<jsp:include page="/score/sidebar.jsp" />
			
			
			
		<!-- Side bar ****************************************************************** -->
				<!-- Start of main content -->
				<div id="rightcontent">
					<div>
						<stripes:form beanclass="com.bus.stripes.actionbean.score.DepartmentSummaryActionBean">
							月份:<stripes:text name="month" value="${actionBean.month }"/>
							<stripes:submit name="defaultAction" value="更改月份"/>
						</stripes:form>
						部门:${actionBean.departmentName }<br/><br/>
						部门月总得分:${actionBean.departmentMonthScore }<br/><br/>
						部门周剩下分:${actionBean.departmentWeekRemain }
					</div>
					<br/><br/>
					<div>
						当月员工总结表:
						<br/><br/>
						<table class='data'>
							<thead>
								<th>工号</th>
								<th>名字</th>
								<th>临时分</th>
								<th>项目分</th>
								<th>固定分</th>
								<th>绩效分</th>
							</thead>
							
							<tbody>
								<c:set var="count" value="0"  scope="page"/>
								<c:forEach items="${actionBean.summary }" var="list" varStatus="loop">
									<c:if test="${count%2==0}">
											<tr class="alt">
									</c:if>
									<c:if test="${count%2!=0}">
										<tr>
									</c:if>
										<td>${list.employee.workerid }</td>
										<td>${list.employee.fullname }</td>
										<td>${list.score }</td>
										<td>${list.projectscore }</td>
										<td>${list.fixscore }</td>
										<td>${list.performancescore }</td>
									</tr>
									<c:set var="count" value="${count + 1}" scope="page"/>
								</c:forEach>
							</tbody>
						</table>
					</div>
					<br/><br/>
					<div>
					 	月员工详细得分表:<br/><br/>
					<table class="data">
								<thead>
									<tr>
										<th>Id</th>
										<th>员工</th>
										<th>职位</th>
										<th>日期</th>
										<th>分值</th>
										<th>条例编号</th>
										<th>条例详细</th>
									</tr>
								</thead>
								<tbody>
									<c:set var="count" value="0"  scope="page"/>
									<c:forEach items="${actionBean.records}" var="list" varStatus="loop">
										<c:if test="${count%2==0}">
											<tr class="alt">
										</c:if>
										<c:if test="${count%2!=0}">
											<tr>
										</c:if>
											<td>${list.id }</td>
											<td>${list.receiver.employee.fullname }</td>
											<td>${list.receiver.employee.position.name }</td>
											<td>${list.scoredatestr }</td>
											<td>${list.score }</td>
											<td>${list.scoretype.reference }</td>
											<td>${list.scoretype.description}</td>
										</tr>
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