<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="stripes"
	uri="http://stripes.sourceforge.net/stripes.tld"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="ss" uri="/WEB-INF/StripesSecurityManager.tld"%>
<stripes:layout-render name="../default.jsp">

	<stripes:layout-component name="contents">
		<script type="text/javascript"
			src="${pageContext.request.contextPath}/js/score.js"></script>
		<link href="${pageContext.request.contextPath}/css/custom_general.css"
			rel="stylesheet" media="all" />
		<div id="sub-nav">
			<div class="page-title">
				<h1>条例表单</h1>
			</div>
		</div>

		<div id="page-layout">
			<div id="page-content">
				<div id="page-content-wrapper">



					<!-- Side bar ****************************************************************** -->
					<jsp:include page="/score/sidebar.jsp" />



					<!-- Side bar ****************************************************************** -->
					<!-- Start of main content -->
					<div id="rightcontent">
						<!-- 新建条例  Dialog-->
						<div id="items_top_button_bar" style="height: 40px;">
							&nbsp; <a class="btn ui-state-default ui-corner-all"
								href="javascript:location.reload();"> 刷新 </a>

							<ss:secure roles="score_sheet_create">
								<a id="btn_new_itemsheet_link"
									class="btn ui-state-default ui-corner-all" href="#"> <span
									class="ui-icon ui-icon-person"></span> 新建条例表单
								</a>

								<div id="btn_new_itemsheet_dialog" title="新建条例">
									<stripes:form id="form_new_itemsheet"
										beanclass="com.bus.stripes.actionbean.score.ScoresheetActionBean">
										<div class='hastable'>
											<table>
												<tr>
													<td>条例表单名称:</td>
													<td><stripes:text name="sheet.name"
															class="required" /></td>
												</tr>
												<tr>
													<td>注释:</td>
													<td><stripes:textarea name="sheet.name"
															class="required" /></td>
												</tr>
											</table>
										</div>
									</stripes:form>
								</div>
							</ss:secure>
						</div>
						<hr />

						<!-- Show items and pages options -->
						<div>
							<div class="hastable">
								<div class="inner-page-title">表单信息</div>
								<stripes:form
									beanclass="com.bus.stripes.actionbean.score.ScoresheetActionBean">
									<table>
										<thead>
											<tr>
												<td colspan=6 style="text-align: left">条例表单：<stripes:select
														name="selectedSheet">
														<stripes:options-collection
															collection="${actionBean.sheetList}" label="name"
															value="id" />
													</stripes:select> 
													
													<stripes:submit name="selectSheet" value="确认" /> 
													
													<ss:secure roles="score_sheet_remove">
													<stripes:submit
														name="removeSheet" value="删除" />
												  </ss:secure>
												</td>
											</tr>
											<tr>
												<td colspan=6 style="text-align: left">
													<ss:secure roles="score_sheet_rm_st">
														<input type="hidden" name="targetId" value="${actionBean.selectedSheet}"/>
														<stripes:submit name="removeSelectedScoreTypes"
															value="移除条例" />
													</ss:secure>
													</td>
											</tr>
											<tr>
												<th></th>
												<th>Id</th>
												<th>编号</th>
												<th>类型</th>
												<th>分值</th>
												<th>注释</th>
											</tr>
										</thead>
										<tbody>
												<c:set var="color" value="0" scope="page" />
												<c:forEach items="${actionBean.scoretypes}" var="type"
													varStatus="loop">
													<c:choose>
														<c:when test="${color%2 == 0}">
															<tr>
														</c:when>
														<c:otherwise>
															<tr class="alt">
														</c:otherwise>
													</c:choose>
													<td><input type="checkbox"
														name="selectedScoreTypes[${color}].id" value="${type.id}" />
													</td>
													<td>${type.id}</td>
													<td>${type.reference}</td>
													<td>${type.typestr}</td>
													<td>${type.score}</td>
													<td>${type.description}</td>
													</tr>
													<c:set var="color" value="${color + 1}" scope="page" />
												</c:forEach>
										</tbody>
									</table>
								</stripes:form>
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