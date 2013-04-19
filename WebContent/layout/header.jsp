<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%-- <%@ taglib prefix="stripes" uri="http://stripes.sourceforge.net/stripes.tld" %> --%>

<div id="page_wrapper">
		<div id="page-header">
			<div id="page-header-wrapper">
				<div id="top">
					<a href="/actionbean/Employee.action" class="logo" title="HRSystem v1.0">HRSystem v1.0</a>
					<div class="welcome">
						<span class="note">欢迎, <a href="#" title="Welcome, ${actionBean.context.user.username}">${actionBean.context.user.username}</a></span>
						<a class="btn ui-state-default ui-corner-all" href="#">
							<span class="ui-icon ui-icon-wrench"></span>
							设置
						</a>
						<a class="btn ui-state-default ui-corner-all" href="#">
							<span class="ui-icon ui-icon-person"></span>
							我的账号
						</a>
						<a class="btn ui-state-default ui-corner-all" href="${pageContext.request.contextPath}/actionbean/Login.action?logout=">
							<span class="ui-icon ui-icon-power"></span>
							退出登录
						</a>						
					</div>
				</div>
				<ul id="navigation">
					<li>
						<a href="${pageContext.request.contextPath}/actionbean/Employee.action" class="sf-with-ul">人事系统</a>
						
					</li>
					<li>
						<a href="javascript:void;" class="sf-with-ul">积分系统</a>
					</li>
			</div>
		</div>