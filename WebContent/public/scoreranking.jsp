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
<script type="text/javascript" src="${pageContext.request.contextPath}/js/custom/scoreRank.js"></script>
<link href="${pageContext.request.contextPath}/css/ui/ui.base.css" rel="stylesheet" media="all" />   
<link href="${pageContext.request.contextPath}/css/custom_general.css" rel="stylesheet" media="all" />
<link href="${pageContext.request.contextPath}/css/custom/scoreRank.css" rel="stylesheet" media="all" />   
<title>员工积分详细查看</title>

</head>
<body>
<div id="empSearchBox">
		名称:<input type="text" name="name"/><br/>
		工号:<input type="text" name="workerid"/><br/>
		<button id="findEmp" class="searchBtn">查找</button>
		<div id="empResult">
			
		</div>
</div>
<div id="maincontent">
	<div id="title"><div id="imageTitle"></div><span class='nameTitle'>积分排名查询</span></div>
	<stripes:form id="formSearch" beanclass="com.bus.stripes.actionbean.score.ScoreViewPublicActionBean">
	<div id="nav">
		<div class="backToScoreSystem"><a href="${pageContext.request.contextPath}/actionbean/Scoreitems.action">点击返回系统</a></div>
		分组:
		<stripes:select name="scoreSelector.rankGroup" id="scoreMasterGroupSelect">
					<stripes:option value="0">主任级</stripes:option>
					<stripes:option value="1">管理人员</stripes:option>
<%-- 					<stripes:option value="2">维修工</stripes:option> --%>
					<stripes:option value="3">服务员</stripes:option>
					<stripes:option value="4">驾驶员</stripes:option>
					<stripes:option value="5">车队长</stripes:option>
		</stripes:select>
		<stripes:select name="scoreSelector.scoreGroup" id="scoreGroupSelect">
				<stripes:option value=""></stripes:option>
				<stripes:options-collection collection="${actionBean.scoreGroups}" value="id" label="name"/>
		</stripes:select>
		日期(从):<stripes:text name="scoreSelector.recordStartDate" formatPattern="yyyy-MM-dd" class="datepickerClass"/>
				(到):<stripes:text name="scoreSelector.recordEndDate" formatPattern="yyyy-MM-dd" class="datepickerClass"/>
		<stripes:submit name="ranking" class="searchBtn" value="排名搜索"/>
	</div>
	<div>
		分值类型:
		<stripes:select name="scoreSelector.scoretype">
			<stripes:option value="0">临时分</stripes:option>
			<stripes:option value="1">固定分</stripes:option>
			<stripes:option value="2">绩效分</stripes:option>
			<stripes:option value="3">总分</stripes:option>
		</stripes:select>
		顺序:
		<stripes:radio value="0" name="scoreSelector.order"/>高到底
		<stripes:radio value="1" name="scoreSelector.order"/>低到高
	</div>
	</stripes:form>
	
	<div class="data">
								<table>
									<thead>
										<tr>
											<th>排名</th>
											<th>百分比</th>
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
												<td class="rank"><strong>${summary.rank}</strong></td>
												<td class="rank">${summary.percent}%</td>
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
		