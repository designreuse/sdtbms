<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="stripes" uri="http://stripes.sourceforge.net/stripes.tld" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" lang="en" xml:lang="en">
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />
	<title>BMS 登陆</title>
	<script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery-1.7.2.min.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/ui/ui.core.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/superfish.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/live_search.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/tooltip.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/cookie.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/ui/ui.sortable.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/ui/ui.draggable.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/ui/ui.resizable.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/ui/ui.dialog.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/custom.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/ui/ui.tabs.js"></script>

<link href="${pageContext.request.contextPath}/css/ui/ui.base.css" rel="stylesheet" media="all" />

<link href="${pageContext.request.contextPath}/css/themes/black_rose/ui.css" rel="stylesheet" title="style"
	media="all" />

<!--[if IE 6]>
	<link href="${pageContext.request.contextPath}/css/ie6.css" rel="stylesheet" media="all" />
	
	<script src="${pageContext.request.contextPath}/js/pngfix.js"></script>
	<script>
	  /* Fix IE6 Transparent PNG */
	  DD_belatedPNG.fix('.logo, ul#dashboard-buttons li a, .response-msg, #search-bar input');

	</script>
	<![endif]-->
	<!--[if IE 7]>
	<link href="${pageContext.request.contextPath}/css/ie7.css" rel="stylesheet" media="all" />
	<![endif]-->
</head>
<body>
	<div id="page_wrapper">
<script type="text/javascript" src="${pageContext.request.contextPath}/js/ui/ui.tabs.js"></script>
<script type="text/javascript">
$(document).ready(function() {
	// Tabs
	$('#tabs').tabs();
});
</script>
		<div id="sub-nav">
			<div class="page-title">
				<h1>登陆页面</h1>
				<span>请输入您的用户名和密码</span>
			</div>
		
		</div>
		<div class="clear"></div>
		<div id="page-layout">
			<div id="page-content">
				<div id="page-content-wrapper">

				<div id="tabs">
					<ul>

						<li><a href="#login">Login</a></li>
<!-- 						<li><a href="#tabs-2">Register</a></li> -->
<!-- 						<li><a href="#tabs-3">Recover password</a></li> -->
					</ul>
					<div id="login">
						<stripes:form id="login_form" beanclass="com.bus.stripes.actionbean.LoginActionBean">
							<stripes:errors/>
							<ul>
								<li>
									<label for="account.username" class="desc">
										用户名:
									</label>
									<div>
										<stripes:text tabindex="1" maxlength="255" class="field text full" name="account.username" id="username" />
									</div>
								</li>
								<li>
									<label for="password" class="desc">
										密码:
									</label>
				
									<div>
										<stripes:password  tabindex="1" maxlength="255" class="field text full" name="password" id="password" />
									</div>
								</li>
								<li class="buttons">
									<div>
										<stripes:submit name="login"  value ="登陆" class="ui-state-default ui-corner-all float-right ui-button"></stripes:submit>
									</div>
								</li>
							</ul>
						</stripes:form>
					</div>
<!-- 					<div id="tabs-2"> -->
<!-- 						<div class="other-box gray-box ui-corner-all"> -->
<!-- 							<div class="cont ui-corner-all tooltip" title="Example tooltip!"> -->
<!-- 								<h3>Example information message</h3> -->
<!-- 								<p>Sed ut perspiciatis unde omnis iste natus error sit voluptatem accusantium doloremque laudantium.</p> -->
<!-- 							</div> -->
<!-- 						</div> -->
<!-- 						<p>You can put a register form here.</p> -->
<!-- 					</div> -->
<!-- 					<div id="tabs-3"> -->
<!-- 						<form action="dashboard.php"> -->
<!-- 							<ul> -->
<!-- 								<li> -->
<!-- 									<label for="email" class="desc"> -->
<!-- 										Registered Email: -->
<!-- 									</label> -->
<!-- 									<div> -->
<!-- 										<input type="text" tabindex="1" maxlength="255" value="" class="field text full" name="email" id="email" /> -->
<!-- 									</div> -->
<!-- 								</li> -->
<!-- 								<li class="buttons"> -->
<!-- 									<div> -->
<!-- 										<button class="ui-state-default ui-corner-all float-right ui-button" type="submit">Send new password</button> -->
<!-- 									</div> -->
<!-- 								</li> -->
<!-- 							</ul> -->
<!-- 						</form> -->
<!-- 					</div> -->
				</div>



				</div>
				<div class="clear"></div>
			</div>
		</div>
	</div>
</body>
</html>