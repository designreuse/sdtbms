<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="stripes" uri="http://stripes.sourceforge.net/stripes.tld" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="ss" uri="/WEB-INF/StripesSecurityManager.tld" %>
<stripes:layout-render name="../default.jsp">
	
    <stripes:layout-component name="contents">
    <script type="text/javascript" src="${pageContext.request.contextPath}/js/score.js"></script>
    <link href="${pageContext.request.contextPath}/css/custom_general.css" rel="stylesheet" media="all" />
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
				<!-- 新建条例  Dialog-->
				<div id="items_top_button_bar" style="height:40px;">&nbsp;  
				<a class="btn ui-state-default ui-corner-all" href="javascript:location.reload();">
					刷新
				</a>
				
				<ss:secure roles="scoreitems_create">
				<a id="btn_new_item_link" class="btn ui-state-default ui-corner-all" href="#">
						<span class="ui-icon ui-icon-person"></span>
						新建条例
				</a>
				
				<div id="btn_new_item_dialog" title="新建条例">
					<stripes:form id="form_new_item" beanclass="com.bus.stripes.actionbean.score.ScoreitemsActionBean">
						<div class='hastable'>
						<table>
								<tr>
									<td>编号:</td><td><stripes:text name="scoretype.reference" class="required"/></td>
								</tr>
								<tr>
									<td>类型:</td><td><stripes:select name="scoretype.type"  class="required"><stripes:option value="0">临时分</stripes:option><stripes:option value="1">固定分</stripes:option></stripes:select></td>
								</tr>
								<tr>
									<td>注释:</td><td><stripes:textarea name="scoretype.description"  class="required"/></td>
								</tr>
								<tr>	
									<td>分值:</td><td><stripes:text name="scoretype.score"  class="required" formatType="number" formatPattern="integer"></stripes:text></td>
								</tr>
						</table>
						</div>
					</stripes:form>
				</div>
				</ss:secure>
				</div>
				<hr/>
				
				<!-- Show items and pages options -->
			<div>
				<div class="hastable">
						<div class="inner-page-title">
						条例信息
						</div>
				<stripes:form beanclass="com.bus.stripes.actionbean.score.ScoreitemsActionBean">
				<table>
					<thead>
					
						<tr>
							<td colspan=6 style="text-align:left">
								<stripes:submit name="prevpage" value="上页"/> 页码: <stripes:text name="pagenum"/> <stripes:hidden name="totalcount"/>  <stripes:submit name="nextpage" value="下页"/>
								显示数量:<stripes:text name="lotsize"/>
								<stripes:submit name="filter" value="筛选"/>
							</td>
						</tr>
						<tr>
							<td colspan=6 style="text-align:left">
								<ss:secure roles="scoreitems_givescore">
								<Label class='selector'>给分人:</Label>名称:<stripes:text name="employee.fullname" id="employeenamefromid1"/>工号:<stripes:text name="employee.workerid"/><a href="javascript:void;" id="checkWorkerId">(查)</a><input type="hidden" value="${pageContext.request.contextPath}/actionbean/Employee.action?checkworkerid="/>
								<a href="javascript:void;" id="getNameById1">获取</a><input type="hidden" value="${pageContext.request.contextPath}/actionbean/Employee.action?getnamebyid="/>
								<Label class='selector'>自拟定分值:</Label><stripes:text name="score"/>
								<br/>
								<Label class='selector'>受分人:</Label>名称:<stripes:text name="scorer.fullname" id="employeenamefromid2"/>工号:<stripes:text name="scorer.workerid"/><a href="javascript:void;" id="checkWorkerId">(查)</a><input type="hidden" value="${pageContext.request.contextPath}/actionbean/Employee.action?checkworkerid="/>
								<a href="javascript:void;" id="getNameById2">获取</a><input type="hidden" value="${pageContext.request.contextPath}/actionbean/Employee.action?getnamebyid="/>
								<stripes:submit name="givescores" value="赋值"/>
								<br/>
								</ss:secure>
								
							</td>
						</tr>
						<tr>
							<td colspan=6 style="text-align:left">
								<Label class='selector'>编号:</Label><stripes:text name="selector.reference"/>
								<Label class='selector'>类型:</Label><stripes:select name="selector.type"><stripes:option value="">不限</stripes:option><stripes:option value="0">临时分</stripes:option><stripes:option value="1">固定分</stripes:option></stripes:select>
								<Label class='selector'>条例单:</Label><stripes:select name="itemlist"><stripes:option value=""></stripes:option>不限<stripes:options-collection collection="${actionBean.sheetList}" label="name" value="id"/></stripes:select>
								<br/>
								<ss:secure roles="scoreitems_edit">
									<stripes:submit name="deletescoretype" value="删除"/>
								</ss:secure>
							<ss:secure roles="score_sheet_add_st">
									<stripes:submit name="assignToScoreSheet" value="添加到积分表单"/>
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
					<c:set var="color" value="0" scope="page"/>
					<c:forEach items="${actionBean.scoretypes}" var="st" varStatus="loop">
						<c:choose>
							<c:when test="${color%2 == 0}">
								<tr>
							</c:when>
							<c:otherwise>
								<tr class="alt">
							</c:otherwise>
						</c:choose>
							<td>
								<input type="checkbox" name="selectedScoreTypes[${color}].id" value="${st.id}"/>
								<ss:secure roles="scoreitems_edit">
										<a target="_blank" href="${pageContext.request.contextPath}/actionbean/Scoreitems.action?editscoretype=&targetId=${st.id}">修改</a>
								</ss:secure>
							</td>
							<td>${st.id}</td>
							<td>${st.reference}</td>
							<td>${st.typestr}</td>
							<td>${st.score}</td>
							<td>${st.description}</td>
						</tr>
						<c:set var="color" value="${color + 1}" scope="page"/>
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