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

    	buildBasicDatePickDialog('date_select_dialog',"200","150");
        
    	$('.del_application').click(function(){
			var action = $(this).next().val();
			if(confirm("是否删除报名?")){
				$.ajax({
					url:action,
					type:"post",
					dataType:'text',
					success:function(response){
						alert(response);
						location.reload();
					},
					error:function(response){
						alert("errors");
					}
				});
			}
        });

    	
        callAjaxForEdit("noti_bodycheck");
        callAjaxForEdit("bodycheck_fail");
        callAjaxForEdit("bodycheck_success");
        callAjaxForEdit("interview_fail");
        callAjaxForEdit("interview_success");
        callAjaxForEdit("interview_consider");
        callAjaxForEdit("approve_fail");
        callAjaxForEdit("approve_consider");
        callAjaxForEdit("approve_success");

        callAjaxForEditWithDatePicker("approve_date");
        callAjaxForEditWithDatePicker("join_date");
    });

    function callAjaxForEdit(hyperLinkClassName){
    	$('.'+hyperLinkClassName).click(function(){
			var action = $(this).next().val();
			$.ajax({
				url:action,
				type:"post",
				dataType:'text',
				success:function(response){
// 					alert(response);
					location.reload();
				},
				error:function(response){
					alert("errors");
				}
			});
        });
    }

    function callAjaxForEditWithDatePicker(hyperLinkClassName){
    	$('.'+hyperLinkClassName).click(function(){
			var action = $(this).next().val();
			$('#opener').val(action);
			$('#date_select_dialog').dialog('open');
        });
    }

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
				
				<ss:secure roles="employment_application_create">
				<a id="btn_new_applicant_link" class="btn ui-state-default ui-corner-all" href="#">
						<span class="ui-icon ui-icon-person"></span>
						报名
				</a>
				
				<div id="btn_new_applicant_dialog" title="新建增补">
					<stripes:form id="form_applicant_req" beanclass="com.bus.stripes.actionbean.application.EmploymentActionBean">
						<div class='hastable'>
						<table>
								<tr>
									<td>部门:</td><td><stripes:select name="applicant.department.id"><stripes:options-collection collection="${actionBean.departments}" label="name" value="id"/></stripes:select></td>
									<td>职位:</td><td><stripes:select name="applicant.position.id"><stripes:options-collection collection="${actionBean.positions}" label="name" value="id"/></stripes:select></td>
									<td rowspan=6><stripes:select name="selectedIdCards" multiple="multiple" ><stripes:options-collection collection="${actionBean.idcards}" label="name" value="id"/></stripes:select></td>
								</tr>
								<tr>
									<td>填表日期:</td><td><stripes:text name="applicant.applyDate" formatPattern="yyyy-MM-dd" class="datepickerClass required"/></td>
									<td>姓名:</td><td><stripes:text name="applicant.name" class="required"/></td>
								</tr>
								<tr>
									<td>电话:</td><td><stripes:text name="applicant.mobile" class="required"/></td>
									<td>邮箱:</td><td><stripes:text name="applicant.email"/></td>
								</tr>
								<tr>
									<td>出生日期:</td><td><stripes:text name="applicant.dob" formatPattern="yyyy-MM-dd" class="datepickerClass required"/></td>
									<td>驾驶员:</td><td><stripes:radio name="applicant.driver" value="0"/>否<stripes:radio name="applicant.driver" value="1"/>是</td>
								</tr>
								<tr>
									<td>户口城市:</td><td><stripes:text name="applicant.pol"/></td>
									<td>现住城市:</td><td><stripes:text name="applicant.residence"/></td>
								</tr>
								<tr>
									<td>是否住宿:</td><td><stripes:radio name="applicant.dorm" value="0"/>否<stripes:radio name="applicant.dorm" value="1"/>是</td>
									<td>备注:</td><td><stripes:text name="applicant.remark"/><stripes:hidden name="applicant.creator.id" value="${actionBean.context.user.id}"/></td>
								</tr>
						</table>
						</div>
					</stripes:form>
				</div>
				</ss:secure>
				</div>
				<hr/>
					
					
					<div class="hastable">
						<table>
							<thead>
								<stripes:form beanclass="com.bus.stripes.actionbean.application.EmploymentActionBean">
								<tr>
									<td colspan=14 style="text-align: left !important;">
										<label class="selector">填表日期:</label><stripes:text name="selector.applyDateA"/> 到<stripes:text name="selector.applyDateB"/>
										<br/><label class="selector">姓名:</label><stripes:text name="selector.name"/> &nbsp;&nbsp;&nbsp; <label class="selector">驾驶员:</label><stripes:radio name="selector.driver" value=""/>不限<stripes:radio name="selector.driver" value="1"/>是<stripes:radio name="selector.driver" value="2"/>否
										<br/><label class="selector">职位:</label><stripes:text name="selector.position"/>
										<br/><label class="selector">户籍地:</label><stripes:text name="selector.domicile"/>&nbsp;&nbsp;&nbsp; <label class="selector">住宿:</label><stripes:radio name="selector.dorm" value=""/>不限<stripes:radio name="selector.dorm" value="1"/>是<stripes:radio name="selector.dorm" value="2"/>否
										<br/><label class="selector">体检通知:</label><stripes:radio name="selector.bodyCheckNoti" value=""/>不限<stripes:radio name="selector.bodyCheckNoti" value="1"/>已发<stripes:radio name="selector.bodyCheckNoti" value="0"/>未发
										<br/><label class="selector">审批结果:</label><stripes:radio name="selector.approveResult" value=""/>不限 <stripes:radio name="selector.approveResult" value="0"/>未批<stripes:radio name="selector.approveResult" value="1"/>录取<stripes:radio name="selector.approveResult" value="2"/>后备<stripes:radio name="selector.approveResult" value="3"/>不录
										<br/><label class="selector">入职日期:</label><stripes:text name="selector.joinDateA"/> 到<stripes:text name="selector.joinDateB"/>
										<br/><label class="selector">体检:</label><stripes:radio name="selector.bodyCheck" value=""/>不限 <stripes:radio name="selector.bodyCheck" value="1"/>合格<stripes:radio name="selector.bodyCheck" value="2"/>不合格
										<br/><label class="selector">面试:</label><stripes:radio name="selector.interview" value=""/>不限 <stripes:radio name="selector.interview" value="1"/>合格<stripes:radio name="selector.interview" value="2"/>待定<stripes:radio name="selector.interview" value="3"/>不合格<stripes:radio name="selector.interview" value="0"/>未面
										<br/><label class="selector">考试:</label><stripes:radio name="selector.driverexam" value=""/>不限 <stripes:radio name="selector.driverexam" value="1"/>合格<stripes:radio name="selector.driverexam" value="2"/>不合格<stripes:radio name="selector.driverexam" value="0"/>未报
									</td>
								</tr>
								<tr>
										<td colspan=14 style="text-align:left !important">
											<stripes:submit name="prevpage" value="上页"/> 页码: <stripes:text name="pagenum"/> / ${actionBean.pagecount}<stripes:hidden name="totalcount"/>  <stripes:submit name="nextpage" value="下页"/>
											显示数量:<stripes:text name="lotsize"/>${actionBean.totalcount}行记录
											<stripes:submit name="filter" value="提交"/>
										</td>
								</tr>
								</stripes:form>
								<tr>
								<th>填表日期</th>
								<th>姓名</th>
								<th>电话</th>
								<th>职位</th>
								<th>住宿</th>
								<th>年龄</th>
								<th>驾驶员</th>
								<th>考试</th>
								<th>体检通知</th>
								<th>体检</th>
								<th>面试</th>
								<th>送审日期</th>
								<th>审批</th>
								<th>入职日期</th>
								</tr>
							</thead>
							<tbody>
								<c:set var="color" value="0" scope="page"/>
								<c:forEach items="${actionBean.applications}" var="app" varStatus="loop">
								<c:choose>
									<c:when test="${color%2 == 0}">
										<tr>
									</c:when>
									<c:otherwise>
										<tr class="alt">
									</c:otherwise>
								</c:choose>
								<td><ss:secure roles="employment_application_edit">
									<a class="del_application" href="javascript:void;">(删)</a><input type="hidden" name="" value="${pageContext.request.contextPath}/actionbean/Employment.action?targetId=${app.id}&deleteApplication="/>
								</ss:secure>${app.applyDateStr}
								
								</td>
								<td>${app.name}<ss:secure roles="employment_application_edit">
										<a target="_blank" href="${pageContext.request.contextPath}/actionbean/Employment.action?targetId=${app.id}&openEdit=">(改)</a>
									</ss:secure>
								</td>
								<td>${app.mobile}</td>
								<td>${app.position.name}</td>
								<td>${app.dormStr}</td>
								<td>${app.age}</td>
								<td>${app.driverStr}</td>
								<td>
									${app.driverexamresultStr}
									<ss:secure roles="employment_apply_exam">
									<stripes:form class="driver_exam_app" style="display:inline !important" beanclass="com.bus.stripes.actionbean.application.EmploymentActionBean">
										<input type="hidden" name="appId" value="${app.id}"/>
										<c:choose>
									  	<c:when test="${app.driverexamresult == null || app.driverexamresult == 0}">
									  		(<input type="submit" name="applyExam" value="报名"/>)
									  	</c:when>
									  	<c:when test="${app.driverexamresult == 2}">
									  		(<input type="submit" name="applyExam" value="重报"/>)
									  	</c:when>
									  	<c:when test="${app.driverexamresult == 3}">
									  		(<input type="submit" name="applyExam" value="重报"/>)
									  	</c:when>
									  	<c:otherwise>
									  	
									  	</c:otherwise>
									  </c:choose>
									  </stripes:form>
									</ss:secure>
								</td>
								<td>${app.bodyCheckNotiStr}<ss:secure roles="employment_application_edit">
									<a class="noti_bodycheck" href="javascript:void;">(发)</a><input type="hidden" value="${pageContext.request.contextPath}/actionbean/Employment.action?targetId=${app.id}&notiBodyCheck="/>
									</ss:secure>
								</td>
								<td>${app.bodyCheckPassStr}<ss:secure roles="employment_application_edit">
									<a class="bodycheck_fail" href="javascript:void;">(N</a><input type="hidden" value="${pageContext.request.contextPath}/actionbean/Employment.action?targetId=${app.id}&bodyCheckFail="/>/
									<a class="bodycheck_success" href="javascript:void;">Y)</a><input type="hidden" value="${pageContext.request.contextPath}/actionbean/Employment.action?targetId=${app.id}&bodyCheckSuccess="/>
									</ss:secure>
								</td>
								<td>${app.interviewResultStr}<ss:secure roles="employment_application_edit">
									<a class="interview_fail" href="javascript:void;">(N</a><input type="hidden" value="${pageContext.request.contextPath}/actionbean/Employment.action?targetId=${app.id}&interviewFail="/>/
									<a class="interview_consider" href="javascript:void;">C</a><input type="hidden" value="${pageContext.request.contextPath}/actionbean/Employment.action?targetId=${app.id}&interviewConsider="/>/
									<a class="interview_success" href="javascript:void;">Y)</a><input type="hidden" value="${pageContext.request.contextPath}/actionbean/Employment.action?targetId=${app.id}&interviewSuccess="/>
									</ss:secure>
								</td>
								<td>${app.approveDateStr}<ss:secure roles="employment_application_edit">
									<a class="approve_date" href="javascript:void;">(设)</a><input type="hidden" value="${pageContext.request.contextPath}/actionbean/Employment.action?targetId=${app.id}&approveDate="/>
									</ss:secure>
								</td>
								<td>${app.approveResultStr}<ss:secure roles="employment_application_edit">
									<a class="approve_fail" href="javascript:void;">(N</a><input type="hidden" value="${pageContext.request.contextPath}/actionbean/Employment.action?targetId=${app.id}&approveResult=&approveStatus=N"/>/
									<a class="approve_consider" href="javascript:void;">C</a><input type="hidden" value="${pageContext.request.contextPath}/actionbean/Employment.action?targetId=${app.id}&approveResult=&approveStatus=C"/>/
									<a class="approve_success" href="javascript:void;">Y)</a><input type="hidden" value="${pageContext.request.contextPath}/actionbean/Employment.action?targetId=${app.id}&approveResult=&approveStatus=Y"/>
									</ss:secure>
								</td>
								<td>${app.joinDateStr}<ss:secure roles="employment_application_edit">
									<a class="join_date" href="javascript:void;">(设)</a><input type="hidden" value="${pageContext.request.contextPath}/actionbean/Employment.action?targetId=${app.id}&joinDate="/>
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
			</div>
			<div class="clear"></div>
			
			<ss:secure roles="employment_apply_exam">
				<div id="btn_new_driverexam_dialog" title="新建增补">
					<stripes:form id="form_driverexam" beanclass="com.bus.stripes.actionbean.application.EmploymentActionBean">
						<div class='hastable'>
						<table>
								<tr>
									<td>报考日期:</td><td><stripes:text name="examdate" formatPattern="yyyy-MM-dd" class="datepickerClass required"/></td>
								</tr>
								<tr>
									<td>注释:</td>
									<td>
										<input type="text" name="remark"/>
										<input type="hidden" id="exam_app_id" name="appId"/>
									</td>
								</tr>
						</table>
						</div>
					</stripes:form>
				</div>
			</ss:secure>
			<div id="date_select_dialog" title="选择日期">
				<label>日期:</label><input type="text" id="date_select_box" class="datepickerClass"/>
				<input type="hidden" id="opener" value=""/>
			</div>
		</div>
	</div>
    </stripes:layout-component>
</stripes:layout-render>