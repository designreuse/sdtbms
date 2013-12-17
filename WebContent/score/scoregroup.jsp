<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="stripes" uri="http://stripes.sourceforge.net/stripes.tld" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="recur" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="ss" uri="/WEB-INF/StripesSecurityManager.tld" %>

<stripes:layout-render name="../default.jsp">

    <stripes:layout-component name="contents">
   		<script type="text/javascript" src="${pageContext.request.contextPath}/js/custom/scoreListBuilder.js"></script>
   		<script type="text/javascript">
<!--
$(document).ready(function(){
	$("#newGroupDialog").dialog({
		autoOpen: false,bgiframe: true,resizable: true,height:150,width:300,modal: true,
		buttons:{
			'新建':function(){
				var link = $("#newGroupForm").attr("action");
				var params = $('#newGroupForm').serialize()+"&addGroup=";
				$.ajax({url:link,type:"post",dataType:'text',data:params,
					success:function(response){
						var json = $.parseJSON(response);
						if(json != null && json != undefined){
							alert(json.msg);
							if(json.result="1"){
								$("#newGroupDialog").dialog('close');
								$("#newGroupForm input").each(function(){$(this).val("");});
								location.reload();
							}
						}
					}
				});
			},
			'取消':function(){$(this).dialog('close');}
		}
	});

	$("#selectPeopleDialog").dialog({
		autoOpen: false,bgiframe: true,resizable: true,modal: true,
		buttons:{'取消':function(){$(this).dialog('close');}}
	});
	
	$(".btnNewGroup").click(function(){
		$("#newGroupDialog").dialog('open');
		var gid = $(this).parent().children().first().val();
		$("#formGroupId").val(gid);
		return true;
	});

	$(".btnDelGroup").click(function(){
		var gid = $(this).parent().children().first().val();
		var group = $(this).parent().children().first().next().next().html();
		if(confirm("确定要删除组:"+group)){
			var link = $("#newGroupForm").attr("action");
			$.ajax({
				url:link,
				type:"post",
				dataType:'text',
				data:"gid="+gid+"&delGroup=",
				success:function(response){
					var json = $.parseJSON(response);
					if(json != null && json != undefined){
						alert(json.msg);
						if(json.result="1"){
							location.reload();
						}
					}
				}
			});
			return;
		}
	});

	$('.btnEditGroup').click(function(){
		var groupId = $(this).parent().children().first().val();
		var link = "${pageContext.request.contextPath}/actionbean/EmployeeSelector.action";
		$.ajax({
			url:link,
			type:"post",
			dataType:'text',
			data:"getEmployeeSelectionList=",
			success:function(response){
				var json = $.parseJSON(response);
				if(json != null && json != undefined){
					if(json.result=="0"){
						alert(json.msg);
						$("#selectPeopleDialog").dialog("close");
					}else{
						//Build HTML
						var html = "<ul class='ulParent'>"+buildUList(json)+"</ul>";
						html = "<button class='empJoinGroup'>添加</button>" +
							"<input type='hidden' id='gid' value=''/>" +
							html + "<button class='empJoinGroup'>添加</button>";
						//Set html to dialog
						$("#selectPeopleDialog").html(html);
						//Set the group id
						$('#gid').val(groupId);
						//Bind event to clickable button
						bindEventToDialogHtmlBody();
						toggleCheckbox("deptCheckbox");
						$('.empJoinGroup').click(function(){
							var checkedStr = "";
							$("#selectPeopleDialog input.nameCheckbox:checked").each(function(){
								checkedStr += $(this).val()+",";
							});
							var link2 =  $("#newGroupForm").attr("action");
							var params = "workerids="+checkedStr+"&gid="+groupId+"&joinGroup=";
							$.ajax({
								url:link2,
								type:'post',
								dataType:'text',
								data:params,
								success:function(response){
									var json2 = $.parseJSON(response);
									alert(json2.msg);
									if(json2.result == "1"){
										$("#selectPeopleDialog").dialog("close");
										location.reload();
									}
								}
							});
							return true;
						});
						//Open dialog
						$("#selectPeopleDialog").dialog("open");
					}
				}
			}
		});		
	});

	$('#btnRemoveMemberFromGroup').click(function(){
		var checkedStr = "";
		$("#scoreGroupMemberUList input.child:checked").each(function(){
			checkedStr += $(this).val()+",";
		});
		var link =  $("#newGroupForm").attr("action");
		var params = "ids="+checkedStr+"&quitGroup=";
		$.ajax({
			url:link,
			type:'post',
			dataType:'text',
			data:params,
			success:function(response){
				var json2 = $.parseJSON(response);
				alert(json2.msg);
				if(json2.result == "1"){
					location.reload();
				}
			}
		});
		return true;
	});
	
	toggleCheckbox("select_children");
	accordingList("scoreGroupMemberUList");
});
//-->
</script>

<link href="${pageContext.request.contextPath}/css/custom/scoreListBuilder.css" rel="stylesheet" title="style"/>
<style>
<!--

-->
</style>
   		
   		
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
						<div>
							<c:if test="${actionBean.empList != null }">
								<div class="mainContentTitle">积分制组编制</div>
								<ul id="scoreGroupMemberUList" class="customRootUl">
									<recur:scoreMemberTree treeRoot="${actionBean.empList}"/>
								</ul>
							</c:if>
						</div>
						<div>
							<button id="btnRemoveMemberFromGroup" class="btnRemove">选中员工退出积分组</button>
						</div>
					</div>
				</div>
				<!-- End of main content -->
				<div id="newGroupDialog">
					<form id="newGroupForm" action="${pageContext.request.contextPath}/actionbean/ScoreGroup.action">
						<input type="hidden" id="formGroupId" name="gid"/>
						名字 :<input type="text" name="scoreNewGroup.name"/><br/>
						基础分值:<input type="text" name="scoreNewGroup.basescore"/>
					</form>
				</div>
				<div id="selectPeopleDialog">
				</div>
			</div>
			<div class="clear"></div>
		</div>
	</div>
    </stripes:layout-component>
</stripes:layout-render>