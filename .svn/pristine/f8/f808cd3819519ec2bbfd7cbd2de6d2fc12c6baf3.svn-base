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
		$("#form_applicant_edit").submit(function(){
			var params = $('#form_applicant_edit').serialize() + "&editApplication=";
			var action = $('#form_applicant_edit').attr('action');
			$.ajax({
				url:action,
				type:"post",
				dataType:'text',
				data:params,
				success:function(response){
					if(confirm(response + " ,是否关闭窗口?")){
						window.close();
					}else{

					}
				},
				error:function(response){
					alert("errors");
				}
			});
			return false;
		});
	});
	</script>   
		<div id="sub-nav"><div class="page-title">
			<h1>修改报名档案:${actionBean.editApplicant.name}</h1>
		</div></div>
		
		<div id="page-layout"><div id="page-content">
			<div id="page-content-wrapper">
		
				<!-- Start of main content -->
				<div id="rightcontent" style="width:860px !important;">
				
				<stripes:form id="form_applicant_edit" beanclass="com.bus.stripes.actionbean.application.EmploymentActionBean">
						<div class='hastable'>
						<stripes:hidden name="editApplicant.id"/>
						<table>
								<tr>
									<td>部门:</td><td><stripes:select name="editApplicant.department.id"><stripes:options-collection collection="${actionBean.departments}" label="name" value="id"/></stripes:select></td>
									<td>职位:</td><td><stripes:select name="editApplicant.position.id"><stripes:options-collection collection="${actionBean.positions}" label="name" value="id"/></stripes:select></td>
									<td rowspan=6>
										<c:forEach items="${actionBean.idcards}" var="card" varStatus="loop">
											<stripes:checkbox name="selectedIdCards" value="${card.id}" checked="${actionBean.assignedCards}"></stripes:checkbox>${card.name}<br/>
										</c:forEach>
									</td>
								</tr>
								<tr>
									<td>填表日期:</td><td><stripes:text name="editApplicant.applyDate" formatPattern="yyyy-MM-dd" class="datepickerClass required"/></td>
									<td>姓名:</td><td><stripes:text name="editApplicant.name" class="required"/></td>
								</tr>
								<tr>
									<td>电话:</td><td><stripes:text name="editApplicant.mobile" class="required"/></td>
									<td>邮箱:</td><td><stripes:text name="editApplicant.email"/></td>
								</tr>
								<tr>
									<td>出生日期:</td><td><stripes:text name="editApplicant.dob" formatPattern="yyyy-MM-dd" class="datepickerClass required"/></td>
									<td>驾驶员:</td><td><stripes:radio name="editApplicant.driver" value="0"/>否<stripes:radio name="editApplicant.driver" value="1"/>是</td>
								</tr>
								<tr>
									<td>户口城市:</td><td><stripes:text name="editApplicant.pol"/></td>
									<td>现住城市:</td><td><stripes:text name="editApplicant.residence"/></td>
								</tr>
								<tr>
									<td>是否住宿:</td><td><stripes:radio name="editApplicant.dorm" value="0"/>否<stripes:radio name="editApplicant.dorm" value="1"/>是</td>
									<td>备注:</td><td><stripes:text name="editApplicant.remark"/><stripes:hidden name="editApplicant.creator.id" value="${actionBean.context.user.id}"/></td>
								</tr>
						</table>
						</div>
						<ss:secure roles="employment_application_edit">
							<button>修改</button>
						</ss:secure>
					</stripes:form>
				</div>
				
			</div>
	</div>
    </stripes:layout-component>
</stripes:layout-render>