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
						<stripes:form beanclass="com.bus.stripes.actionbean.score.JoinScoreSystemActionBean">
							<p>添加新的积分制管理职位，可设置不参加，或无积分上限或有上限，默认不设置的为参加积分制并有上限</p>
							职位:<stripes:select name="exception.position.id">
								<stripes:options-collection collection="${actionBean.positions }" label="name" value="id"/>
							</stripes:select>
							状态:<select name="exception.status">
								<option value="0">不参加</option>
								<option value="1">积分无上限</option>
								<option value="2">积分有上限</option>
							</select>
							<stripes:submit name="createException"/>

						</stripes:form>
					</div>		
					<div>
						<p>
							已经过滤的职位
						</p>
						<table class="data">
							<thead>
								<th>ID</th>
								<th>职位</th>
								<th>状态</th>
							</thead>
							<tbody>
								<c:forEach items="${actionBean.displayList }" var="list" varStatus="loop">
									<tr>
										<td>${list.id }</td>
										<td>${list.position.name }</td>
										<td>${list.statusStr}</td>
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