<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="stripes" uri="http://stripes.sourceforge.net/stripes.tld" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="ss" uri="/WEB-INF/StripesSecurityManager.tld" %>
<stripes:layout-render name="../default.jsp">
	
    <stripes:layout-component name="contents">
    <script type="text/javascript">
		$(document).ready(function(){
			$("#editform").submit(function(){
				var np1 = $('#np1').val();
				var np2 = $('#np2').val();
				if(np1 == "")
					alert("请输入新密码");
				else if(np1 != np2)
					alert("两次输入的新密码不一样，请重新输入");
				else
					return true;
				return false;
			});
		});
    </script>
		<div id="sub-nav"><div class="page-title">
			<h1>修改密码</h1>
		</div></div>
		
		<div id="page-layout"><div id="page-content">
			<div id="page-content-wrapper">
			
			
			
		<!-- Side bar ****************************************************************** -->
				<!-- Start of main content -->
				<div id="rightcontent">
					
					<c:if test="${actionBean.errMsg != null}">
						<Label style="color:red;">${actionBean.errMsg}</Label>
					</c:if>
				
					<stripes:form id="editform" beanclass="com.bus.stripes.actionbean.account.ChangePasswdActionBean">
						用户名:<stripes:text name="username" value="${actionBean.username}" readonly="true"/>
						<br/>
						旧密码:<stripes:password name="oldpasswd"/>
						<br/>
						新密码:<stripes:password id="np1" name="newpasswd"/>
						<br/>
						再次输入新密码:<input type="password" id="np2" name="newpasswd2"/>
						<br/>
						<stripes:submit name="changepasswd" value="修改"/>
					</stripes:form>
					
				
				</div>
				<!-- End of main content -->
			</div>
			<div class="clear"></div>
		</div>
	</div>
    </stripes:layout-component>
</stripes:layout-render>