<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib prefix="stripes"
	uri="http://stripes.sourceforge.net/stripes.tld"%>
<%@ taglib uri="/WEB-INF/StripesSecurityManager.tld" prefix="ss"%>

<stripes:layout-definition>
	<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
	<html xmlns="http://www.w3.org/1999/xhtml" lang="en" xml:lang="en">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title>BMS System</title>
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

<script type="text/javascript" src="${pageContext.request.contextPath}/js/ui/ui.datepicker.js"></script>   
<link href="${pageContext.request.contextPath}/css/custom_general.css" rel="stylesheet" media="all" />
<!--[if IE 6]>
	<link href="${pageContext.request.contextPath}/css/ie6.css" rel="stylesheet" media="all" />
	
	<script src="${pageContext.request.contextPath}/js/pngfix.js"></script>
	<script>
	  /* Fix IE6 Transparent PNG */
	  DD_belatedPNG.fix('.logo, ul#dashboard-buttons li a, .response-msg, #search-bar input');

	</script>
	<![endif]-->
	<script type="text/javascript">
$(document).ready(function(){
    $(".datepickerClass").datepicker({
    	changeMonth: true,
		changeYear: true,
		dateFormat: 'yy-mm-dd' 
    });

});

</script>
<stripes:layout-component name="html_head" />
</head>
<body id="sidebar-left">
	<stripes:layout-component name="header">
		<jsp:include page="/layout/header.jsp" />
	</stripes:layout-component>

	<div class="pageContent">
		<stripes:layout-component name="contents" />
	</div>

	<stripes:layout-component name="footer">
		<jsp:include page="/layout/footer.jsp" />
	</stripes:layout-component>
</body>
</html>
</stripes:layout-definition>