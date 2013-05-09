<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="stripes" uri="http://stripes.sourceforge.net/stripes.tld" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<stripes:layout-render name="../default.jsp">
	
    <stripes:layout-component name="contents">
    <script type="text/javascript" src="${pageContext.request.contextPath}/js/acc.js"></script>
		<div id="sub-nav"><div class="page-title">
			<h1>账号管理</h1>
		</div></div>
		
		<div id="page-layout"><div id="page-content">
			<div id="page-content-wrapper">
			
			
			
			<!-- Side bar ****************************************************************** -->
			<jsp:include page="/acc/accsidebar.jsp" />
			
			
			
		<!-- Side bar ****************************************************************** -->
				<!-- Start of main content -->
				<div id="rightcontent">
				
				
				<div>
					<div class="hastable">
					<div>新建用户</div>
				<stripes:form beanclass="com.bus.stripes.actionbean.account.AccountActionBean">
				<table>
					<tr>
						<td>工号</td><td><stripes:text name="empworkerid"/><a href="javascript:void;" id="checkworkerid">查</a><input type="hidden" value="${pageContext.request.contextPath}/actionbean/Employee.action?checkworkerid="/></td>
					</tr>
					<tr>
						<td>用户名</td><td><stripes:text name="username" class="required"/></td>
					</tr>
					<tr>
						<td>密码</td><td><stripes:password name="password" class="required"/></td>
					</tr>
					<tr>
						<td colspan=2>
							<stripes:submit name="createaccount"/>
						</td>
					</tr>
				</table>
				</stripes:form>
				</div>
				</div>
				
				<hr/>
				<!-- create account group -->
				<div>
					<div class="hastable">
					<div>新建用户组</div>
				<stripes:form beanclass="com.bus.stripes.actionbean.account.AccountActionBean">
				<table>
					<tr>
						<td>用户组名</td><td><stripes:text name="groupname" class="required"/></td>
					</tr>
					<tr>
						<td colspan=2>
							<stripes:submit name="creategroup"/>
						</td>
					</tr>
				</table>
				</stripes:form>
				</div>
				</div>
				
				<hr/>
				<!-- assign users to group -->
				<div class="hastable">
					<div>添加用户到用户组</div>
					<stripes:form beanclass="com.bus.stripes.actionbean.account.AccountActionBean">	
					<table>
						<tr>
							<td>用户</td><td>用户组</td><td>添加/删除</td>
						</tr>
						<tr>
							<td>
								<stripes:select name="userids" multiple="multiple" style="height:200px;width:250px;"><stripes:options-collection collection="${actionBean.users}" label="label" value="value"/></stripes:select>
								<a href="javascript:void;" id="accountgroups">查看编辑已归入组</a><input type="hidden" value="${pageContext.request.contextPath}/actionbean/account/Account.action?accountgroups="/>
							</td>
							<td>
								<stripes:select name="groupids" multiple="multiple" style="height:200px;width:250px;"><stripes:options-collection collection="${actionBean.groups}" label="label" value="value"/></stripes:select>
								<a href="javascript:void;" id="groupactions">查看编辑权限</a><input type="hidden" value="${pageContext.request.contextPath}/actionbean/account/Account.action?groupactions="/>
							</td>
							<td>
								<stripes:submit name="assigngroups" value="入组"/>
								<stripes:submit name="removeusers" value="删除用户"/>
								<br/>
								<stripes:submit name="resignusers" value="辞去用户"/>
								<stripes:submit name="removegroups" value="删除组"/>
							</td>
						</tr>
					</table>
					</stripes:form>
				</div>
				
				</div>
				<!-- End of main content -->
			</div>
			<div class="clear"></div>
		</div>
	</div>
    </stripes:layout-component>
</stripes:layout-render>