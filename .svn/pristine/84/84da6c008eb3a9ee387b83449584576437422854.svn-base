<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="stripes" uri="http://stripes.sourceforge.net/stripes.tld" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="ss" uri="/WEB-INF/StripesSecurityManager.tld" %>
<stripes:layout-render name="../default.jsp">
	
    <stripes:layout-component name="contents">
    <script type="text/javascript">

    	$(document).ready(function(){
        
        });
     </script>
	<style type="text/css">
	</style>
		<div id="sub-nav"><div class="page-title">
			<h1>车辆档案</h1>
			<span><a href="#" title="Layout Options">车辆</a> > <a href="#" title="Two column layout">档案管理</a> > 查看</span>
		</div></div>
		
		<div id="page-layout"><div id="page-content">
			<div id="page-content-wrapper">
			
			
			
			<!-- Side bar ****************************************************************** -->
			<jsp:include page="/vehicle/sidebar.jsp" />
			
			
			
		<!-- Side bar ****************************************************************** -->
				<!-- Start of main content -->
				<div id="rightcontent">
				
				<!-- 新建档案  Dialog-->
				<div id="hr_top_button_bar" style="height:40px;">&nbsp;  
				
				<a class="btn ui-state-default ui-corner-all" href="javascript:location.reload();">
					刷新
				</a>
				</div>
				
				
				<!--  Display Vehicle List   -->
				<div>
				<div class="inner-page-title">
						路线信息
						<br/>
						
					</div>
					
					<div class="hastable">
				
				<table >
					<thead>
						<tr>
							<td colspan=4 style="text-align:left">
								共找到${actionBean.totalcount}条线路
							</td>
						</tr>
						<tr>
						<th>Id</th>
						<th>线路编号</th>
						<th>线路概括</th>
						<th>总车辆数</th>
						</tr>
					</thead>
					<tbody>
					
					<c:set var="color" value="0" scope="page"/>
					<c:set var="statusE" value="E" scope="page"/>
					<tr>
						<td></td>
						<td></td>
						<td><a href="${pageContext.request.contextPath}/actionbean/VehicleLane.action?emptyRoute=">点击查看没安排路线的车辆</a></td>
						<td></td>
					</tr>
					
					<c:forEach items="${actionBean.routes}" var="lane" varStatus="loop">
						<c:choose>
							<c:when test="${color%2 == 0}">
								<tr>
							</c:when>
							<c:otherwise>
								<tr class="alt">
							</c:otherwise>
						</c:choose>
							<td>${lane.id}</td>
							<td>${lane.num}</td>
							<td><a href="${pageContext.request.contextPath}/actionbean/VehicleLane.action?targetId=${lane.id}&routeDetail=">${lane.detail}</a></td>
							<td>${lane.vehicleCount}</td>
						</tr>
						<c:set var="color" value="${color + 1}" scope="page"/>
					</c:forEach>
					</tbody>
				</table>
				</div>
				</div>
				
				<ss:secure roles="vehicle_route_edit">
					<div>
						<div>
						--添加路线--
						<stripes:form beanclass="com.bus.stripes.actionbean.vehicle.VehicleLaneActionBean">
							编号:<stripes:text name="newRoute.num"/> 线路概括:<stripes:text name="newRoute.detail"/>
							<stripes:submit name="newRouteAction" value="添加"/>
						</stripes:form>
						</div>
						<div>
						--删除路线--
						<stripes:form beanclass="com.bus.stripes.actionbean.vehicle.VehicleLaneActionBean">
							编号:<stripes:text name="delRoute.num"/> 线路概括:<stripes:text name="delRoute.detail"/>
							<stripes:submit name="deleteRouteAction" value="删除"/>
						</stripes:form>
						</div>
					</div>
				</ss:secure>
			
				
				</div>
				<!-- End of main content -->
			</div>
			<div class="clear"></div>
		</div>
	</div>
    </stripes:layout-component>
</stripes:layout-render>