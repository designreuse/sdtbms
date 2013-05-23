<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="stripes" uri="http://stripes.sourceforge.net/stripes.tld" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="ss" uri="/WEB-INF/StripesSecurityManager.tld" %>

<stripes:layout-render name="../default.jsp">
	
    <stripes:layout-component name="contents">
   
		<div id="sub-nav"><div class="page-title">
			<h1>积分主页</h1>
		</div></div>
		
		<div id="page-layout"><div id="page-content">
			<div id="page-content-wrapper">
			
			
			
			<!-- Side bar ****************************************************************** -->
			<jsp:include page="/score/sidebar.jsp" />
			
		<!-- Side bar ****************************************************************** -->
		
				<!-- Start of main content -->
				<div id="rightcontent">
				
					<div>
					<stripes:form beanclass="com.bus.stripes.actionbean.score.ScorehomeActionBean">
						日期:<stripes:text name="logdate"  formatPattern="yyyy-MM-dd" class="datepickerClass"/>
						<stripes:submit name="filter"/>
					</stripes:form>
					</div>
					<div class="hastable">
						<table>
							<thead>
								<th></th>
								<th>时间</th>
								<th>用户</th>
								<th>行为</th>
								<th>详情</th>
							</thead>
							<tbody>
								<c:set var="color" value="0" scope="page"/>
								<c:forEach items="${actionBean.logs}" var="log" varStatus="loop">
								<c:choose>
									<c:when test="${color%2 == 0}">
										<tr>
									</c:when>
									<c:otherwise>
										<tr class="alt">
									</c:otherwise>
								</c:choose>
								<td>${log.id}</td>
								<td>${log.createtimestr}</td>
								<td>${log.who.username}</td>
								<td>${log.actionstr}</td>
								<td>${log.remark}</td>
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