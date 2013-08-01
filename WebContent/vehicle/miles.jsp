<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="stripes" uri="http://stripes.sourceforge.net/stripes.tld" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="ss" uri="/WEB-INF/StripesSecurityManager.tld" %>
<stripes:layout-render name="../default.jsp">
	
    <stripes:layout-component name="contents">
    
    <script type="text/javascript">
    $(document).ready(function(){
    	$(".none_edit").attr("readonly","readonly");
    });
    </script>
    
    <ss:secure roles="vehicle_mile_edit">
    <script type="text/javascript">
    $(document).ready(function(){
    	$(".none_edit").dblclick(function(){
			var editable = $(this).attr("readonly");
			if(editable == "readonly" || editable == true){
				$(this).removeAttr("readonly");	
			}
        });
    	$(".none_edit").blur(function(){
        	var readonly = $(this).attr('readonly');
        	if(readonly == null || readonly == undefined){
            	$(this).attr("readonly","readonly");
            	var value = $(this).val();
            	if($.trim(value) == "")
                	return;
            	if(!(/^[0-9]+\.{0,1}[0-9]+$/.test(value))){
                	alert("请输入正确的数字");
                	$(this).focus();
                	return;
            	}
            	$.ajax({
					url:$(this).closest("form").attr("action"),
					type:"post",
					dataType:'text',
					data:$(this).closest("form").serialize(),
					success:function(response){
						alert(response);
					},
					error:function(response){
						alert("修改失败");
					}
				});
        	}
        });

        $('.deleteMiles').click(function(){
            var id = $(this).closest("form").children().first().val();
            var year = $(this).prev().val();
			var del = confirm("确认要删除"+year+"年记录？记录ID="+id);
			if(del){
				var action = $(this).closest("form").attr('action');
				var params = "vid="+id + "&deleteVehicleMile=";
				$.ajax({
					url:action,
					type:"post",
					dataType:'text',
					data:params,
					success:function(response){
						alert(response);
						location.reload();
					},
					error:function(response){
						alert("删除失败");
					}
				});
			}
        });
    });
    </script>
    </ss:secure>
	<style type="text/css">
		.like_label{
			border:none !important;
			background-color:inherit !important;
			text-align:center;
		}
		.right_align{
			text-align:right !important;
		}
		table.new_record thead th{
			width:75px;
			text-align:center;
		}
		table.new_record tbody td{
			width:75px;
		}
		table.new_record input{
			width:90%;
		}
		
		table.vehiclelist tbody input{
			width:70px !important;
		}
		table.vehiclelist tbody td{
			text-align:center;
		}
	</style>
		<div id="sub-nav"><div class="page-title">
			<h1>车辆行驶距离</h1>
			<span><a href="#" title="Layout Options">车辆</a> > <a href="#" title="Two column layout">行驶距离</a></span>
		</div></div>
		
		<div id="page-layout"><div id="page-content">
			<div id="page-content-wrapper">
			
			
			
			<!-- Side bar ****************************************************************** -->
			<jsp:include page="/vehicle/sidebar.jsp" />
			
			
			
		<!-- Side bar ****************************************************************** -->
				<!-- Start of main content -->
				<div id="rightcontent">
				
				<!-- 新建档案  Dialog-->
				<div id="hr_top_button_bar" style="height:40px;">&nbsp;  
				
				<a class="btn ui-state-default ui-corner-all" href="javascript:location.reload();">
					刷新
				</a>
				</div>
					
				<br/>
				
				<!--  Display Vehicle List   -->
				<div>
					<div class="inner-page-title">
						<label>车牌号:${actionBean.vp.vid}</label>
						<br/>
						<br/>
						<label>总公里数:${actionBean.vp.totalmiles}公里</label>
						<br/>
						<br/>
					</div>
					<div class="hastable">
					
				<c:forEach items="${actionBean.vp.miles}" var="milel" varStatus="loop">
				<stripes:form beanclass = "com.bus.stripes.actionbean.vehicle.VehicleMilesActionBean">
				<stripes:hidden name="editmile.id" value="${milel.id}"/>
				<table class="vehiclelist">
					<thead>
						<tr>
							<td colspan=14 style="font-size:12pt;color:red;">
								<stripes:text name="editmile.vyear" value="${milel.vyear}" style="font-size:12pt;color:red;" class="none_edit like_label right_align"/>
								年
								&nbsp;
								<ss:secure roles="vehicle_mile_edit">
									<a href="javascript:void;" class="deleteMiles">(删除)</a>
								</ss:secure>
							</td>
						</tr>
						<tr>
						<th>年总公里</th>
						<th>一月</th>
						<th>二月</th>
						<th>三月</th>
						<th>四月</th>
						<th>五月</th>
						<th>六月</th>
						<th>七月</th>
						<th>八月</th>
						<th>九月</th>
						<th>十月</th>
						<th>十一月</th>
						<th>十二月</th>
						</tr>
					</thead>
					<tbody>
						<tr>
							<td>${milel.yeartotal}<input type="hidden" name="updateMile"/></td>
							<td><stripes:text name="editmile.jan" value="${milel.jan}" class="none_edit like_label"/></td>
							<td><stripes:text name="editmile.feb" value="${milel.feb}" class="none_edit like_label"/></td>
							<td><stripes:text name="editmile.mar" value="${milel.mar}" class="none_edit like_label"/></td>
							<td><stripes:text name="editmile.apr" value="${milel.apr}" class="none_edit like_label"/></td>
							<td><stripes:text name="editmile.may" value="${milel.may}" class="none_edit like_label"/></td>
							<td><stripes:text name="editmile.jun" value="${milel.jun}" class="none_edit like_label"/></td>
							<td><stripes:text name="editmile.jul" value="${milel.jul}" class="none_edit like_label"/></td>
							<td><stripes:text name="editmile.aug" value="${milel.aug}" class="none_edit like_label"/></td>
							<td><stripes:text name="editmile.sep" value="${milel.sep}" class="none_edit like_label"/></td>
							<td><stripes:text name="editmile.octo" value="${milel.octo}" class="none_edit like_label"/></td>
							<td><stripes:text name="editmile.nov" value="${milel.nov}" class="none_edit like_label"/></td>
							<td><stripes:text name="editmile.dece" value="${milel.dece}" class="none_edit like_label"/></td>
						</tr>
					</tbody>
				</table>
				</stripes:form>
				</c:forEach>
				</div>
				</div>
				
				<!-- Now is to add new -->
				<br/><br/><hr/>
				<div>
					<ss:secure roles="vehicle_mile_edit">
					<stripes:form beanclass="com.bus.stripes.actionbean.vehicle.VehicleMilesActionBean">
					<stripes:hidden name="mile.vehicle.id" value="${actionBean.vp.id}"/>
					<table class="new_record">
						<thead>
						<tr>
							<td colspan=13 style="height:30px;color:purple; font-size:12pt;">添加新记录</td>
						</tr>
						<tr>
						<th>年份</th>
						<th>一月</th>
						<th>二月</th>
						<th>三月</th>
						<th>四月</th>
						<th>五月</th>
						<th>六月</th>
						<th>七月</th>
						<th>八月</th>
						<th>九月</th>
						<th>十月</th>
						<th>十一月</th>
						<th>十二月</th>
						</tr>
						</thead>
						<tbody>
							<tr>
							<td><stripes:text name="mile.vyear"/></td>
							<td><stripes:text name="mile.jan"/></td>
							<td><stripes:text name="mile.feb"/></td>
							<td><stripes:text name="mile.mar"/></td>
							<td><stripes:text name="mile.apr"/></td>
							<td><stripes:text name="mile.may"/></td>
							<td><stripes:text name="mile.jun"/></td>
							<td><stripes:text name="mile.jul"/></td>
							<td><stripes:text name="mile.aug"/></td>
							<td><stripes:text name="mile.sep"/></td>
							<td><stripes:text name="mile.octo"/></td>
							<td><stripes:text name="mile.nov"/></td>
							<td><stripes:text name="mile.dece"/></td>
							</tr>
							<tr>
								<td colspan=13><stripes:submit name="addMile"/></td>
							<tr/>
							
						</tbody>
					</table>
					</stripes:form>
					</ss:secure>
				</div>
			
				</div>
				<!-- End of main content -->
			</div>
			<div class="clear"></div>
		</div>
	</div>
    </stripes:layout-component>
</stripes:layout-render>