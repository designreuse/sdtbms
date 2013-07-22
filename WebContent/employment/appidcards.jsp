<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="stripes" uri="http://stripes.sourceforge.net/stripes.tld" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="ss" uri="/WEB-INF/StripesSecurityManager.tld" %>

<stripes:layout-render name="../default.jsp">
	
    <stripes:layout-component name="contents">
    <script type="text/javascript" src="${pageContext.request.contextPath}/js/employment.js"></script>
    <script type="text/javascript">
    $(document).ready(function(){
        callAjaxForEdit("deleteIdCard");
    });

    </script>
		<div id="sub-nav"><div class="page-title">
			<h1>招聘主页</h1>
		</div></div>
		
		<div id="page-layout"><div id="page-content">
			<div id="page-content-wrapper">
			
			
			
			<!-- Side bar ****************************************************************** -->
			<jsp:include page="/employment/empsidebar.jsp" />
			
		<!-- Side bar ****************************************************************** -->
		
				<!-- Start of main content -->
				<div id="rightcontent">
				
				<div id="items_top_button_bar" style="height:40px;">&nbsp;  
				<a class="btn ui-state-default ui-corner-all" href="javascript:location.reload();">
					刷新
				</a>
				
				</div>
				<hr/>
					
					
					<div class="hastable">
						<table>
							<thead>
								<tr>
								<th>Id</th>
								<th>名称</th>
								<th></th>
								</tr>
							</thead>
							<tbody>
								<c:set var="color" value="0" scope="page"/>
								<c:forEach items="${actionBean.idcards}" var="card" varStatus="loop">
								<c:choose>
									<c:when test="${color%2 == 0}">
										<tr>
									</c:when>
									<c:otherwise>
										<tr class="alt">
									</c:otherwise>
								</c:choose>
								<td>${card.id}</td>
								<td>${card.name}</td>
								<td>
									<ss:secure roles="employment_application_idcard_setting">
										<a class="deleteIdCard" href="javascript:void;">删除</a><input type="hidden" value="${pageContext.request.contextPath}/actionbean/AppIDCards.action?deleteIdCard=&targetId=${card.id}"/>
									</ss:secure>
								</td>
							</tr>
						<c:set var="color" value="${color + 1}" scope="page"/>
					</c:forEach>
							</tbody>
						</table>
					</div>
				</div>
				<!-- End of main content -->
				<div>
					<stripes:form beanclass="com.bus.stripes.actionbean.application.EmploymentIdCardActionBean">
						名称:<stripes:text name="card.name"/> <stripes:submit name="createIdCard"/>
					</stripes:form>
				</div>
			</div>
			<div class="clear"></div>
		</div>
	</div>
    </stripes:layout-component>
</stripes:layout-render>