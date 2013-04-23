<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="stripes" uri="http://stripes.sourceforge.net/stripes.tld" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<stripes:layout-render name="../default.jsp">
	
    <stripes:layout-component name="contents">
    <script type="text/javascript" src="${pageContext.request.contextPath}/js/hr.js"></script>
    <script type="text/javascript" src="${pageContext.request.contextPath}/js/ui/ui.datepicker.js"></script>   
    <link href="${pageContext.request.contextPath}/css/hr.css" rel="stylesheet" media="all" /> 
		<div id="sub-nav"><div class="page-title">
			<h1>人事管理</h1>
			<span><a href="javascript:void" title="Layout Options">人事</a> > <a href="javascript:void" title="Two column layout">证件管理</a></span>
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
				<div class="hastable">
				
				<table>
					<thead>
					<stripes:form beanclass="com.bus.stripes.actionbean.IdmanagementActionBean" focus="">
						<tr>
							<td colspan=9 style="text-align:left">
								<stripes:submit name="prevpage" value="上页"/> 页码: <stripes:text name="pagenum"/> / ${actionBean.totalcount} <stripes:submit name="nextpage" value="下页"/>
								显示数量:<stripes:text name="lotsize"/>
								<stripes:submit name="filter" value="提交"/>
							</td>
						</tr>
						<tr>
							<td colspan=9 style="text-align:left">
								<Label class='selector'>员工名字:</Label><stripes:text name="selector.name"/>
								<Label class='selector'>员工工号:</Label><stripes:text name="selector.workerid"/>
								<Label class='selector'>证件类型:</Label><stripes:select name="selector.type"><stripes:option value="">不限</stripes:option><stripes:options-collection collection="${actionBean.idcardtypes}" label="label" value="value"/></stripes:select>
								<Label class='selector'>时间帅选:</Label>
								<stripes:radio name="selector.date" value="0"/>不限日期 
								<stripes:radio name="selector.date" value="1"/>结束日期 
						</tr>
						
					</stripes:form>
						<tr>
						<th>Id</th>
						<th>名字</th>
						<th>类型</th>
						<th>号码</th>
						<th>有效期</th>
						<th>失效日期</th>
						<th>图片</th>
						<th>注释</th>
						</tr>
					</thead>
					<c:set var="color" value="0" scope="page"/>
					<c:forEach items="${actionBean.idmanagements}" var="card" varStatus="loop">
						<c:choose>
							<c:when test="${color%2 == 0}">
								<tr>
							</c:when>
							<c:otherwise>
								<tr class="alt">
							</c:otherwise>
						</c:choose>
							<td>
								${card.id}
							</td>
							<td>${card.employee.fullname}</td>
							<td>${card.type}</td>
							<td>${card.number}</td>
							<td>${card.validfrom}</td>
							<td>${card.expiredate}</td>
							<td>${card.image}</td>
							<td>${card.remark}</td>
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