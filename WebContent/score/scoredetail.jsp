<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="stripes" uri="http://stripes.sourceforge.net/stripes.tld" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<stripes:layout-render name="../default.jsp">
	
    <stripes:layout-component name="contents">
    <script type="text/javascript" src="${pageContext.request.contextPath}/js/score.js"></script>
   
    
		<div id="sub-nav"><div class="page-title">
			<h1>个人详细积分页面</h1>
		</div></div>
		
		<div id="page-layout"><div id="page-content">
			<div id="page-content-wrapper">
			
			
			
			<!-- Side bar ****************************************************************** -->
			<jsp:include page="/score/sidebar.jsp" />
			
			
			
		<!-- Side bar ****************************************************************** -->
				<!-- Start of main content -->
				<div id="rightcontent">
		
					<div>
						<stripes:form beanclass="com.bus.stripes.actionbean.score.ScoredetailActionBean">
						<Label class='selector'>查找:</Label>名称:<stripes:text name="selector.name"/>工号:<stripes:text name="selector.workerid"/>
						<stripes:submit name="getMembers" value="查找"/>
						</stripes:form>
					</div>
		
					<div>
						<hr/>
						<ul>
							<c:forEach items="${actionBean.founds}" var="e" varStatus="loop">
								<li>
								<a href="${pageContext.request.contextPath}/actionbean/Scoredetail.action?memberDetail=&workerid=${e.workerid}">
									${e.fullname}---${e.workerid}
								</a>
								</li>
							</c:forEach>
						</ul>
						<hr/>
					</div>
		
					<div>
						<c:if test="${actionBean.member != null}">
							<div>名字:${actionBean.member.employee.fullname} &nbsp;&nbsp;&nbsp; 工龄:${actionBean.member.employee.workage}</div>
							<br/><div>总分:${actionBean.member.historytotal }</div>
							<br/><div>月得分:${actionBean.member.monthlyscore }</div>
							<br/><div>月可分配分:${actionBean.member.monthlytotal }</div>
							<br/><div>月剩下可分配分:${actionBean.member.monthlyremain }</div>
							<hr/>
							<stripes:form beanclass="com.bus.stripes.actionbean.score.ScoredetailActionBean">
								<input name="workerid" value="${actionBean.member.employee.workerid}" type="hidden"/>
								日期:<stripes:text name="recordDate" formatPattern="yyyy-MM-dd" class="datepickerClass"/>
								<stripes:submit name="memberDetail" value="查询"/>
								<stripes:submit name="deleteRecords" value="删除选择"/>
								
							<div class="hastable">
								<table>
									<thead>
										<tr>
											<td>当月总分:${actionBean.recordsSum}</td>
											<td>得分日期</td>
											<td>条例编号</td>
											<td>分值</td>
											<td>类型</td>
											<td>注释</td>
										</tr>
									</thead>
									<tbody>
										<c:set var="color" value="0" scope="page"/>
										<c:forEach items="${actionBean.records}" var="record" varStatus="loop">
											<c:choose>
												<c:when test="${color%2 == 0}">
												<tr>
												</c:when>
												<c:otherwise>
												<tr class="alt">
												</c:otherwise>
											</c:choose>
												<td>
													<input name="selectedRecord[${color}].id" type="checkbox" value="${record.id}"/>
												</td>
												<td>${record.scoredatestr}</td>
												<td>${record.scoretype.reference }</td>
												<td>${record.score }</td>
												<td>${record.scoretype.typestr }</td>
												<td>${record.scoretype.description }</td>
											</tr>
											<c:set var="color" value="${color + 1}" scope="page"/>
										</c:forEach>
									</tbody>
								</table>
							</div>
							</stripes:form>
						</c:if>
					</div>
				</div>
				<!-- End of main content -->
			</div>
			<div class="clear"></div>
		</div>
	</div>
    </stripes:layout-component>
</stripes:layout-render>