<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="stripes" uri="http://stripes.sourceforge.net/stripes.tld" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="ss" uri="/WEB-INF/StripesSecurityManager.tld" %>
<stripes:layout-render name="../default.jsp">
	
    <stripes:layout-component name="contents">
    
		<div id="sub-nav"><div class="page-title">
			<h1>部门积分管理</h1>
		</div></div>
		
		<div id="page-layout"><div id="page-content">
			<div id="page-content-wrapper">
			
			
			
			<!-- Side bar ****************************************************************** -->
			<jsp:include page="/score/sidebar.jsp" />
			
			
			
		<!-- Side bar ****************************************************************** -->
				<!-- Start of main content -->
				<div id="rightcontent">
					<stripes:form beanclass="com.bus.stripes.actionbean.score.DepartmentScoreActionBean">
						新建:<stripes:select name="ds.department.id">
								<stripes:options-collection collection="${actionBean.departments }" label="name" value="id"/>
							</stripes:select>
						基础分:<stripes:text name="ds.basescore"/>
						总分:<stripes:text name="ds.available"/>
						<stripes:submit name="addDepartment"/>
					</stripes:form>
				</div>
				<!-- End of main content -->
			</div>
			<div class="clear"></div>
		</div>
	</div>
    </stripes:layout-component>
</stripes:layout-render>