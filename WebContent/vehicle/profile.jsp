<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="stripes" uri="http://stripes.sourceforge.net/stripes.tld" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="ss" uri="/WEB-INF/StripesSecurityManager.tld" %>
<stripes:layout-render name="../default.jsp">
	
    <stripes:layout-component name="contents">
    
    <script type="text/javascript">
    $(document).ready(function(){
    	$("#btn_new_vehicle_link").click(function() {
    		$('#btn_new_vehicle_dialog').dialog('open');
			return true;
    	});

    	$('#btn_new_vehicle_dialog').dialog({
    		autoOpen: false,
    		bgiframe: true,
    		resizable: true,
    		height:600,
    		width:750,
    		modal: true,
    		overlay: {
    			backgroundColor: '#000',
    			opacity: 0.5
    		},
    		buttons: {
    			'新建':function(){
    					if(!formValidation('form_new_vehicle')){
    						return;
    					}
    					var formaction = $('#form_new_vehicle').attr('action');
    					var params = $('#form_new_vehicle').serialize() + "&addVehicle=";
    					$.ajax({
    						url:formaction,
    						type:"post",
    						dataType:'text',
    						data:params,
    						success:function(response){
    							$('#btn_new_vehicle_dialog').dialog('close');
    							alert(response);
    							location.reload();
    						},
    						error:function(response){
    							alert("errors");
    						}
    					});
    			},
    			'取消': function() {
    				$(this).dialog('close');
    			}
    		}
    	});

    	$('#vehicleListFunctionForm').submit(function(){
			return false;
        });

        $('#btnDeleteVehicle').click(function(){
        		var deleteV = confirm("确定删除车辆资料？");
        		if(deleteV){
            		var link = $('#vehicleListFunctionForm').attr("action");
            		var params = $('#vehicleListFunctionForm').serialize() + "&deleteVehicle=";
        			$.ajax({
						url:link,
						type:"post",
						dataType:'text',
						data:params,
						success:function(response){
							alert(response);
							location.reload();
						},
						error:function(response){
							alert("errors");
						}
					});
        		}
        });

        $('#btnVehicleDetail').click(function(){
			var targetId = $(this).parent().children().first().val();
			var link = "${pageContext.request.contextPath}/actionbean/VehicleProfile.action?targetId="+targetId+"&vehicleDetail=";
			window.open(link);
        });

        $('#btnMiles').click(function(){
        	var targetId = $(this).parent().children().first().val();
        	var link = "${pageContext.request.contextPath}/actionbean/VehicleMiles.action?vid="+targetId;
        	window.location = link;			
       	});
    });
    </script>
	<style type="text/css">
		table.vehicleDetail tr td{ 
			border: 1px solid rgb(24, 59, 240);
			border-collapse:collapse;
			padding:3px;
			height:25px;
			vertical-align:middle;
		}
		table.vehicleDetail input{ 
			width:90%;
		}
		table td.subtitle{
			text-align:center;
			font-size:12pt;
			padding-top:10px !important;
			color:red;
		}
		table.vehiclelist tbody td{
			text-align:center;
		}
	</style>
		<div id="sub-nav"><div class="page-title">
			<h1>车辆档案</h1>
			<span><a href="#" title="Layout Options">车辆</a> > <a href="#" title="Two column layout">档案管理</a> > 查看</span>
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
				
				<ss:secure roles="vehicle_profile_edit">
				<a id="btn_new_vehicle_link" class="btn ui-state-default ui-corner-all" href="#">
						<span class="ui-icon ui-icon-person"></span>
						新建档案
				</a>
				<div id="btn_new_vehicle_dialog" title="新建档案">
				<!-- The Form to submit new employee data -->
				<stripes:form id="form_new_vehicle" beanclass="com.bus.stripes.actionbean.vehicle.VehicleProfileActionBean" focus="">
					<stripes:errors/>
					<table class="vehicleDetail">
						<tr>
							<td class="subtitle" colspan=4>车辆基本资料<hr/></td>
						</tr>
						<tr>
							<td>车牌号码:</td><td><stripes:text  class="required" name="profile.vid"/></td>
							<td>入户日期:</td><td><stripes:text  name="profile.datejoin"  formatPattern="yyyy-MM-dd" class="required datepickerClass"/></td>
						</tr>
						<tr>
							<td>使用性质:</td><td><stripes:text  class="required" name="profile.servicetype"/></td>
							<td>运行日期:</td><td><stripes:text  name="profile.dateuse"  formatPattern="yyyy-MM-dd" class="required datepickerClass"/></td>
						</tr>
						<tr>
							<td>车辆来源:</td><td><stripes:text  class="required" name="profile.source"/></td>
							<td>购买日期:</td><td><stripes:text  name="profile.datepurchase"  formatPattern="yyyy-MM-dd" class="required datepickerClass"/></td>
						</tr>
						<tr>
							<td>车价(元):</td><td><stripes:text  class="required" name="profile.vehicleprice"/></td>
							<td>购置费(元):</td><td><stripes:text  class="required" name="profile.subprice"/></td>
						</tr>
						<tr>
							<td>车属单位:</td><td colspan=3><stripes:text  class="required" name="profile.company"/></td>
						</tr>
						<tr>
							<td>单位地址:</td><td colspan=3><stripes:text  class="required" name="profile.companyaddr"/></td>
						</tr>
						<tr>
							<td>车辆类型:</td><td><stripes:text  class="required" name="profile.vtype"/></td>
							<td>评定等级:</td><td><stripes:text  class="required" name="profile.vlevel"/></td>
						</tr>
						<tr>
							<td class="subtitle" colspan=4>车辆参数配置<hr/></td>
						</tr>
						<tr>
							<td>车辆型号:</td><td><stripes:text name="profile.model"/></td>
							<td>车辆配置:</td><td><stripes:text name="profile.subsides"/></td>
						</tr>
						<tr>
							<td>车辆产地:</td><td><stripes:text name="profile.madein"/></td>
							<td>车辆出厂日期:</td><td><stripes:text name="profile.dateproduction"  formatPattern="yyyy-MM-dd" class="datepickerClass"/></td>
						</tr>
						<tr>
							<td>车辆制造企业名称:</td><td colspan=3><stripes:text name="profile.productionaddr"/></td>
						</tr>
						<tr>
							<td>车身颜色:</td><td><stripes:text name="profile.color"/></td>
							<td>座位(座/总):</td><td><stripes:text name="profile.sits"/></td>
						</tr>
						<tr>
							<td>轮胎数:</td><td><stripes:text name="profile.tyrenum"/></td>
							<td>轮胎型号:</td><td><stripes:text name="profile.tyremodel"/></td>
						</tr>
						<tr>
							<td>燃料:</td><td><stripes:text name="profile.fueltype"/></td>
							<td>外形尺寸:</td><td><stripes:text name="profile.bodysize"/></td>
						</tr>
						<tr>
							<td>最高车速(km/h):</td><td><stripes:text name="profile.vspeed"/></td>
							<td>排量/功率:</td><td><stripes:text name="profile.vpower"/></td>
						</tr>
						<tr>
							<td>总质量(kg):</td><td><stripes:text name="profile.totalweight"/></td>
							<td>整车备质量(kg):</td><td><stripes:text name="profile.subweight"/></td>
						</tr>
						<tr>
							<td>前轮距(mm):</td><td><stripes:text name="profile.frontwheel"/></td>
							<td>后轮距(mm):</td><td><stripes:text name="profile.backwheel"/></td>
						</tr>
						<tr>
							<td>轴距(mm):</td><td><stripes:text name="profile.wheelbase"/></td>
							<td>驱动形式:</td><td><stripes:text name="profile.drivemode"/></td>
						</tr>
						<tr>
							<td>发动机号:</td><td><stripes:text name="profile.enginenum"/></td>
							<td>发动机型号:</td><td><stripes:text name="profile.enginemodel"/></td>
						</tr>
						<tr>
							<td>车架号码:</td><td><stripes:text name="profile.framenum"/></td>
							<td>底盘型号:</td><td><stripes:text name="profile.basednum"/></td>
						</tr>
						<tr>
							<td>转向形式:</td><td><stripes:text name="profile.turntype"/></td>
							<td>转向器式:</td><td><stripes:text name="profile.turnmethod"/></td>
						</tr>
						<tr>
							<td>行车制动形式:</td><td><stripes:text name="profile.movebreak"/></td>
							<td>驻车制动形式:</td><td><stripes:text name="profile.stopbreak"/></td>
						</tr>
						<tr>
							<td>悬挂形式:</td><td colspan=3><stripes:text name="profile.hangmodel"/></td>
						</tr>
						<tr>
							<td>空调:</td><td colspan=3><stripes:text name="profile.aircond"/></td>
						</tr>
					</table>
				</stripes:form>
				</div>
				</ss:secure>
				</div>
					
				
				<!--  Display Vehicle List   -->
				<div>
					<div class="inner-page-title">
						车辆档案信息
					</div>
					<div class="hastable">
				
				<table class="vehiclelist">
					<thead>
					<stripes:form beanclass="com.bus.stripes.actionbean.vehicle.VehicleProfileActionBean" focus="">
						<tr>
							<td colspan=13 style="text-align:left">
								<stripes:submit name="prevpage" value="上页"/> 页码: <stripes:text name="pagenum"/>/<stripes:label name="${actionBean.totalcount}"/>  <stripes:submit name="nextpage" value="下页"/>
							
							显示数量:<stripes:text name="lotsize"/>
							<label>总数:${actionBean.recordsTotal} 行</label>
							</td>
						</tr>
						<tr>
							<td colspan=11 style="text-align:left">
								<stripes:submit name="filter" value="提交"/>
							</td>
						</tr>
					</stripes:form>
						<tr>
						<th>操作</th>
						<th>Id</th>
						<th>车牌号</th>
						<th>购进日期</th>
						<th>运行日期</th>
						<th>使用性质</th>
						<th>总行驶里程</th>
						<th>当前驾驶员</th>
						</tr>
					</thead>
					<tbody>
					
					<c:set var="color" value="0" scope="page"/>
					<c:forEach items="${actionBean.profiles}" var="veh" varStatus="loop">
						<c:choose>
							<c:when test="${color%2 == 0}">
								<tr>
							</c:when>
							<c:otherwise>
								<tr class="alt">
							</c:otherwise>
						</c:choose>
							<td>
								<stripes:form id="vehicleListFunctionForm" beanclass="com.bus.stripes.actionbean.vehicle.VehicleProfileActionBean">
										<input type="hidden" name="targetId" value="${veh.id}"/>
										<ss:secure roles="vehicle_profile_edit">
											<stripes:submit id="btnDeleteVehicle" name="deleteVehicle" value="删除"/>
										</ss:secure>
										<ss:secure roles="vehicle_mile_view">
											<button id="btnMiles">公里数</button>
										</ss:secure>
										<stripes:submit id="btnVehicleDetail" name="VehicleDetail" value="详细资料"/>
								</stripes:form>
							</td>
							<td>
								${veh.id}
							</td>
							<td>${veh.vid}</td>
							<td>${veh.datepurchaseStr}</td>
							<td>${veh.dateuseStr}</td>
							<td>${veh.servicetype}</td>
							<td>${veh.totalmiles}</td>
							<td>${veh.driverStr}</td>
						</tr>
						<c:set var="color" value="${color + 1}" scope="page"/>
					</c:forEach>
					</tbody>
				</table>
				</div>
				</div>
				
			
				</div>
				<!-- End of main content -->
			</div>
			<div class="clear"></div>
		</div>
	</div>
    </stripes:layout-component>
</stripes:layout-render>