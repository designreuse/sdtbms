<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="ss" uri="/WEB-INF/StripesSecurityManager.tld" %>
<%-- <%@ taglib prefix="stripes" uri="http://stripes.sourceforge.net/stripes.tld" %> --%>

<div id="page_wrapper">
		<div id="page-header">
			<div id="page-header-wrapper">
				<div id="top">
					<div>
					<div style="float:left;width:350px;">
						<div><a href="${pageContext.request.contextPath}/actionbean/Employee.action" title="HRSystem v1.0" class="logo" style="height:50px !important;width:50px !important;">&nbsp;</a></div>
						<div style="color: rgb(187,255,169);font-size:36px;padding-top:10px;">顺汽内部管理系统</div>
					</div>
					<div class="welcome" style="float: right">
						<span class="note">欢迎, <a href="#" title="Welcome, ${actionBean.context.user.username}">${actionBean.context.user.username}</a></span>
						
						<ss:secure roles="account_system">
						<a class="btn ui-state-default ui-corner-all" href="${pageContext.request.contextPath}/actionbean/account/Account.action">
							<span class="ui-icon ui-icon-person"></span>
							我的账号
						</a>
						</ss:secure>
						
						<a class="btn ui-state-default ui-corner-all" href="${pageContext.request.contextPath}/actionbean/Login.action?logout=">
							<span class="ui-icon ui-icon-power"></span>
							退出登录
						</a>						
					</div>
					</div>
				</div>
				<ul id="navigation">
					
					<li>
						<a href="${pageContext.request.contextPath}/actionbean/Employee.action" class="sf-with-ul">人事系统</a>
					</li>
					
					<ss:secure roles="score_system">
					<li>
						<a href="${pageContext.request.contextPath}/actionbean/Scorehome.action" class="sf-with-ul">积分系统</a>
					</li>
					</ss:secure>
					</ul>
			</div>
		</div>