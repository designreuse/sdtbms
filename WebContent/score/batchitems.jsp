<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="stripes" uri="http://stripes.sourceforge.net/stripes.tld" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<stripes:layout-render name="../default.jsp">
	
    <stripes:layout-component name="contents">
    <script type="text/javascript" src="${pageContext.request.contextPath}/js/hr.js"></script>

		<div id="sub-nav"><div class="page-title">
			<h1>人事管理</h1>
			<span><a href="#" title="Layout Options">人事</a> > <a href="#" title="Two column layout">档案管理</a> > 查看</span>
		</div></div>
		
		<div id="page-layout"><div id="page-content">
			<div id="page-content-wrapper">
			
			
			
			<!-- Side bar ****************************************************************** -->
			<jsp:include page="/score/sidebar.jsp" />
			
			
			
		<!-- Side bar ****************************************************************** -->
				<!-- Start of main content -->
				<div id="rightcontent">
				
			
				<!--  File Upload -->
				<div class="clear"></div>
				<div>
				文件上传
				<stripes:form id="file_submit_form" beanclass="com.bus.stripes.actionbean.score.ScoreFileUploadActionBean">
					<Label>条例 文件(csv):</Label><stripes:file name="itemsfile" id="file_scoreitem" /><stripes:submit name="itemsupload" value="提交"/>
					<br/>
					<Label>给分文件(csv):</Label><stripes:file name="scorefile"  id="file_score" /><stripes:submit name="scoreupload" value="提交"/>
					<br/>
					<script type="text/javascript">
					$("#file_submit_form").submit(function(){
						var employee_csv =$('#file_scoreitem').val().trim();
						var employee_check = $('#file_score').val().trim();
						if(employee_csv != ""){
							var ext = employee_csv.split('.').pop().toLowerCase();
							if($.inArray(ext, ['csv']) == -1) {
						    	alert('条例文件不是合法的csv文件');
						    	return false;
							}
						}else if(employee_check != ""){
							var ext = employee_check.split('.').pop().toLowerCase();
							if($.inArray(ext, ['csv']) == -1) {
						    	alert('得分文件不是合法的txt文件');
						    	return false;
							}
						}
						return true;
					});
					</script>
				</stripes:form>
			
			</div>
			<div class="clear"></div>
		</div>
	</div>
    </stripes:layout-component>
</stripes:layout-render>