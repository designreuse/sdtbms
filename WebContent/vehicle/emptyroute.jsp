<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="stripes" uri="http://stripes.sourceforge.net/stripes.tld" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="ss" uri="/WEB-INF/StripesSecurityManager.tld" %>
<stripes:layout-render name="../default.jsp">
	
    <stripes:layout-component name="contents">
    <script type="text/javascript">

    	$(document).ready(function(){
        	$('#joinRoute').click(function(){
            	var num = $(this).prev().prev().val();
            	var detail = $(this).prev().val();
            	if(num == "" || detail == ""){
                	alert("请输入线路号和线路信息（建议复制黏贴)");
                	return;
            	}
				$('#selectListForm').append('<input type="text" name="newRoute.num" value="'+num+'"/><input type="text" name="newRoute.detail" value="'+detail+'"/>');
				$('#selectListForm').append('<input type="submit" id="callToSubmit" name="joinRoute"/>');
				$('#selectListForm #callToSubmit').click();
            });
        });
     </script>
	<style type="text/css">
	</style>
		<div id="sub-nav"><div class="page-title">
			<h1>车辆档案</h1>
			<span><a href="#" title="Layout Options">车辆</a> > <a href="#" title="Two column layout">档案管理</a> > 路线查看</span>
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
						<c:choose>
						<c:when test="${actionBean.route == null}">
							没有选中路线
						</c:when>
						<c:otherwise>
							路线:${actionBean.route.num} &nbsp; --- ${actionBean.route.detail} 
						</c:otherwise>
					</c:choose>
					</div>
					
					<div class="hastable">
				
				<table>
					<thead>
						<tr>
							<td colspan=9 style="text-align:left">
								共有${actionBean.totalcount}台车
							</td>
						</tr>
						<tr>
						<th></th>
						<th>自编号</th>
						<th>车牌号</th>
						<th>登记证号</th>
						<th>车队</th>
						<th>分公司</th>
						<th>报废日期</th>
						<th>强制报废</th>
						<th>总行驶里程</th>
						</tr>
					</thead>
					<tbody>
					
					<c:set var="color" value="0" scope="page"/>
					<c:set var="statusE" value="E" scope="page"/>
					<stripes:form id="selectListForm" beanclass="com.bus.stripes.actionbean.vehicle.VehicleLaneActionBean">
					<c:forEach items="${actionBean.vehicles}" var="vehicle" varStatus="loop">
						<c:choose>
							<c:when test="${vehicle.status == statusE}">
								<tr class="expired">
							</c:when>
							<c:otherwise>
								<tr class="alt">
							</c:otherwise>
						</c:choose>
							<td>
								<stripes:checkbox name="selectedVehicles" value="${vehicle.id}"/>
							</td>
							<td>${vehicle.selfid}</td>
							<td><a href="${pageContext.request.contextPath}/actionbean/VehicleProfile.action?targetId=${vehicle.id}&vehicleDetail=">${vehicle.vid}</a></td>
							<td>${vehicle.recordid}</td>
							<td><a href="${pageContext.request.contextPath}/actionbean/VehicleTeam.action?teamId=${vehicle.team.team.id}">${vehicle.team.team.name}</a></td>
							<td>${vehicle.subcompany}</td>
							<td>${vehicle.throwdateStr}</td>
							<td>${vehicle.dateinvalidateStr}</td>
							<td>${vehicle.totalmiles}</td>
						</tr>
						<c:set var="color" value="${color + 1}" scope="page"/>
					</c:forEach>
					</stripes:form>
					<tr>
						<td colspan=9>
							编号:<input type="text"/> 线路概括:<input type="text"/>
							<button id="joinRoute">加选中车辆入线路</button>
						</td>
					</tr>
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