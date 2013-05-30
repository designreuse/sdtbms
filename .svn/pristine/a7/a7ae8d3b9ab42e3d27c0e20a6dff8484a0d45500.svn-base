<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="stripes" uri="http://stripes.sourceforge.net/stripes.tld" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="ss" uri="/WEB-INF/StripesSecurityManager.tld" %>

<stripes:layout-render name="../default.jsp">
	
    <stripes:layout-component name="contents">
   
		<div id="sub-nav"><div class="page-title">
			<h1>排位组管理</h1>
		</div></div>
		
		<div id="page-layout"><div id="page-content">
			<div id="page-content-wrapper">
			
			
			
			<!-- Side bar ****************************************************************** -->
			<jsp:include page="/score/sidebar.jsp" />
			
		<!-- Side bar ****************************************************************** -->
		
				<!-- Start of main content -->
				<div id="rightcontent">
				
					<div>
					创建组
					<hr/>
					<stripes:form beanclass="com.bus.stripes.actionbean.score.RankgroupActionBean">
						<div>
							名字:<stripes:text name="group.name"/>
							<br/>
							附录:<stripes:textarea name="group.remark"/>
							<br/>
							<stripes:submit name="createGroup" value="创建组"/>
						</div>
					</stripes:form>
					<hr/>
					</div>
					
					<div>
						<stripes:form beanclass="com.bus.stripes.actionbean.score.RankgroupActionBean">
						修改名称：<stripes:text name="editGroup.name"/>
						<br/>
						修改注释：<stripes:textarea name="editGroup.remark"/>
						<br/>
						<stripes:submit name="editGroupDetail" value="修改组资料"/>
						<hr/>
						<br/><br/>
						<div>
						选择组:<stripes:select name="groupSelected"><stripes:options-collection collection="${actionBean.groups}" label="name" value="id"/></stripes:select>
						<br/>
						<stripes:submit name="filterGroup" value="选择"/>
						<stripes:submit name="deleteGroup" value="删除组"/>
						</div>
						
						<div>
							<table class="hastable">
								<tr>
									<td>
										选中的组里职位：
									</td>
								</tr>
								<tr>
									<td>
										<stripes:select name="groupedPosSelected" multiple="true" style="height:150px;"><stripes:options-collection collection="${actionBean.groupPositions}" label="name" value="id"/></stripes:select>
									</td>
								</tr>
								
								<tr>
									<td>
										<stripes:submit name="joinGroup" value="添加选中职位到组中"/>
										<stripes:submit name="quitGroup" value="移除选中的组中职位"/>
										<hr/>
									</td>
								</tr>

								<tr>
									<td>
										现有的职位：
									</td>
								</tr>
								<tr>
									<td>
										<stripes:select name="posSelected" multiple="true" style="height:300px;"><stripes:options-collection collection="${actionBean.positions}" label="name" value="id"/></stripes:select>
									</td>
								</tr>
							</table>
						</div>
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