<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="stripes" uri="http://stripes.sourceforge.net/stripes.tld" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery-1.7.2.min.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/ui/ui.core.js"></script>
<%-- <script type="text/javascript" src="${pageContext.request.contextPath}/js/score.js"></script> --%>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/ui/ui.datepicker.js"></script>
<link href="${pageContext.request.contextPath}/css/ui/ui.base.css" rel="stylesheet" media="all" />   
<link href="${pageContext.request.contextPath}/css/custom_general.css" rel="stylesheet" media="all" />
<title>员工积分详细查看</title>

<style>
.title{
font-size:46px;
font-weight:bold;
color:#FF0000;
padding:10px;
text-align:center;
}

.searchdivsummary{
text-align:center;
}

.searchdiv{
padding-top:10px;
padding-bottom:10px;
}

.data table{
font-size:18px;
}

.data table thead th{
padding:20px;
font-size:25px;
text-align:center;
color:RGB(71,75,163);
border-bottom: 1px solid black !important;
}

.data table tbody td{
padding:10px;
text-align:center;
border-bottom: 1px solid black !important;
}

.data table tbody tr:hover{
background-color: yellow;
}

.highlight{
font-weight: bold;
color:purple;
}

.rank{
color:red;
font-style:italic;
font-weight:bold;
}
</style>
</head>
<body>
<script type="text/javascript">
$(document).ready(function(){
    $(".datepickerClass").datepicker({
    	changeMonth: true,
		changeYear: true,
		dateFormat: 'yy-mm-dd' 
    });

});
</script>
<div class="maincontent">
	<div class="title">积分排名查询</div>
	<hr/>
	<div class="searchdivsummary">
		<stripes:form beanclass="com.bus.stripes.actionbean.score.ScoreViewPublicActionBean">
			<div class="searchdiv">
				选择日期:<stripes:text name="scoreSelector.recordDate" formatPattern="yyyy-MM-dd" class="datepickerClass"/>&nbsp;&nbsp;&nbsp;
				<stripes:radio value="0" name="scoreSelector.selecttype"/>按历史总分排
				<stripes:radio value="1" name="scoreSelector.selecttype"/>按年总分排
				<stripes:radio value="2" name="scoreSelector.selecttype"/>按月总分排
			</div>
			<div class="searchdiv">
				分值类型:<stripes:select name="scoreSelector.scoretype">
					<stripes:option value="0">临时分</stripes:option>
					<stripes:option value="1">固定分</stripes:option>
					<stripes:option value="2">绩效分</stripes:option>
					<stripes:option value="3">总分</stripes:option>
				</stripes:select>
			</div>
			<div class="searchdiv">
				自选时间段:<stripes:text name="scoreSelector.recordStartDate" formatPattern="yyyy-MM-dd" class="datepickerClass"/>-->
				<stripes:text name="scoreSelector.recordEndDate" formatPattern="yyyy-MM-dd" class="datepickerClass"/>
			</div>
			<div class="searchdiv">
				部门:<stripes:select name="scoreSelector.department"><stripes:option value="">请选择....</stripes:option><stripes:options-collection collection="${actionBean.departments}" label="label" value="value"/></stripes:select>
				职位:<stripes:text name="scoreSelector.position"/>
<%-- 				分组:<stripes:select name="scoreSelector.selectedGroup"><stripes:option value="">不限....</stripes:option><stripes:options-collection collection="${actionBean.scoregroups}" label="name" value="id"/></stripes:select> --%>
				分组:<stripes:select name="scoreSelector.rankGroup">
					<stripes:option value="0">主任级</stripes:option>
					<stripes:option value="1">管理人员</stripes:option>
					<stripes:option value="2">维修工</stripes:option>
					<stripes:option value="3">服务员</stripes:option>
					<stripes:option value="4">驾驶员</stripes:option>
				</stripes:select>
			</div>
			<div class="searchdiv">
				顺序:<stripes:radio value="0" name="scoreSelector.order"/>高到底
				<stripes:radio value="1" name="scoreSelector.order"/>低到高
				<stripes:submit name="getRankingRecords" value="提交"/>&nbsp;&nbsp;<stripes:submit name="getRankingRecordsInTimeRange" value="按自选时间段提交"/>
			</div>
		</stripes:form>		
	</div>
	<hr/>
	
	<div class="data">
								<table>
									<thead>
										<tr>
											<th>排名</th>
											<th>工号</th>
											<th>姓名</th>
											<th>工龄</th>
											<th>职位</th>
											<th>固定分总分</th>
											<th>临时分总分</th>
											<th>绩效分总分</th>
											<th>总分</th>
										</tr>
									</thead>
									<tbody>
										<c:set var="color" value="0" scope="page"/>
										<c:forEach items="${actionBean.summarys}" var="summary" varStatus="loop">
											<c:choose>
												<c:when test="${color%2 == 0}">
												<tr>
												</c:when>
												<c:otherwise>
												<tr class="alt">
												</c:otherwise>
											</c:choose>
												<td class="rank">${summary.rank}</td>
												<td>${summary.workerid}</td>
												<td><a href="/bms/actionbean/Empscore.action?memberDetail=&workerid=${summary.workerid}">${summary.name}</a></td>
												<td>${summary.workage}</td>
												<td>${summary.positionName}</td>
												<c:choose>
													<c:when test="${actionBean.scoretype == 1}">
														<td class="highlight">${summary.fixscore }</td>
														<td>${summary.tempscore }</td>
														<td>${summary.performancescore }</td>
														<td>${summary.totalscore }</td>
													</c:when>
													<c:when test="${actionBean.scoretype == 0}">
														<td>${summary.fixscore }</td>
														<td class="highlight">${summary.tempscore }</td>
														<td>${summary.performancescore }</td>
														<td>${summary.totalscore }</td>
													</c:when>
													<c:when test="${actionBean.scoretype == 2}">
														<td>${summary.fixscore }</td>
														<td>${summary.tempscore }</td>
														<td class="highlight">${summary.performancescore }</td>
														<td>${summary.totalscore }</td>
													</c:when>
													<c:otherwise>
														<td>${summary.fixscore }</td>
														<td>${summary.tempscore }</td>
														<td>${summary.performancescore }</td>
														<td class="highlight">${summary.totalscore }</td>
													</c:otherwise>
												</c:choose>
												
												
											</tr>
											<c:set var="color" value="${color + 1}" scope="page"/>
										</c:forEach>
									</tbody>
								</table>
	</div>
</div>
</body>
</html>
		