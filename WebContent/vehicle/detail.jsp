<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="stripes" uri="http://stripes.sourceforge.net/stripes.tld" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="ss" uri="/WEB-INF/StripesSecurityManager.tld" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
<title>${actionBean.profile.vid}</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />

<script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery-1.7.2.min.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/ui/ui.core.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/ui/ui.tabs.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/ui/ui.dialog.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/ui/ui.datepicker.js"></script> 

<link href="${pageContext.request.contextPath}/css/themes/black_rose/ui.css" rel="stylesheet" title="style" media="all" />
<link href="${pageContext.request.contextPath}/css/ui/ui.base.css" rel="stylesheet" media="all" />
<link href="${pageContext.request.contextPath}/css/custom_general.css" rel="stylesheet" media="all" />
<%-- <script type="text/javascript" src="${pageContext.request.contextPath}/js/hr.js"></script> --%>
<script type="text/javascript">
function editEmployee(){
	var formaction = $('#form_edit_vehicle').attr('action');
	var params = $('#form_edit_vehicle').serialize() + "&editVehicle=";
	$.ajax({
		url:formaction,
		type:"post",
		dataType:'text',
		data:params,
		success:function(response){
			if(confirm(response + " ,是否关闭窗口?")){
				window.close();
			}else{

			}
		},
		error:function(response){
			alert("errors");
		}
	});
}

$(document).ready(function(){
	$(".datepickerClass").datepicker({
    	changeMonth: true,
		changeYear: true,
		dateFormat: 'yy-mm-dd' 
    });
    
	$('#fileTabs').tabs();

});

function clearFormTextBox(form){
	$('#'+form + ' input[type=text],select').each(function(){
		$(this).val('');
	});
}
</script>

<style type="text/css" >
div#detail_main{
	padding: 0px;
	margin:0px;
	width:1024px;
	border-width:1px;
	border-style:solid;
}
div#detail_left{
	display:inline-block;
	width:100%;
	height:100%;
 	border: 1px solid;
	vertical-align:top;
	text-align:left;
}
/* div#detail_right{ */
/* 	display:inline-block; */
/* 	width:29%;	 */
/* 	background-color:white; */
/* 	height:100%; */
/* 	vertical-align:top; */
/* } */

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
div#detail_left>div{
	width:100%;
}
div#detail_left table.vehicleDetail{
	text-align:left;
	margin:0 auto;
	width:800px;
}
div#detail_left table.normal tr td{
	border: 1px solid black;
	border-collapse:collapse;
	padding:5px;
	font-size:12pt;
}
div#fileTabs{
	width:98% !important;
}
label.tabSubTitle{
	color:#851A1A;
	font-size:12pt;
}
.button{
	font: bold 15pt Arial;
    text-decoration: none;
    background-color: #EEEEEE;
    color: #333333;
    padding: 2px 6px 2px 6px;
    border-top: 1px solid #CCCCCC;
    border-right: 1px solid #333333;
    border-bottom: 1px solid #333333;
    border-left: 1px solid #CCCCCC;
}
</style>
</head>
<body>

<div id='detail_main'>
	<div id='detail_left'>
		<div>
	<stripes:form id="form_edit_vehicle" beanclass="com.bus.stripes.actionbean.vehicle.VehicleProfileActionBean">
					<stripes:errors/>
					<table class="vehicleDetail">
						<tr>
							<td colspan=4 style="height:50px; font-size: 40pt; border:none;text-align:center;color:red;">
								<stripes:hidden name="profile.id"/>
								车辆档案资料
							</td>
						</tr>
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
						<ss:secure roles="vehicle_profile_edit">
						<tr>
							<td colspan=4 style="text-align: center;border:none; padding:5px;"><a class="button" href="javascript:editEmployee();">修改</a></td>
						</tr>
						</ss:secure>
					</table>
				</stripes:form>
</div>
	
	<br/>
	<br/>
	<br/>
	
	<c:set var="returnLink" value="/actionbean/VehicleProfile.action?targetId=${actionBean.profile.id}&vehicleDetail=" scope="page"/>
	<hr/>
	
	
	<br/>
	<br/>
	
<!-- 	use for status comparison -->
	<div id="fileTabs">
		<ul>
				<li><a href="#fileTabs-1">合同附件</a></li>
				<li><a href="#fileTabs-2">证件附件</a></li>
		</ul>
		<div id="fileTabs-1">
			<label class="tabSubTitle">合同附件</label><br/>
			<br/>
			<table class="normal">
				<tr>
					<td>Id</td>
					<td>开始日期</td>
					<td>结束日期</td>
					<td>图片连接</td>
					<td>图片</td>
				</tr>
			
			</table>
		</div>
		
		<div id="fileTabs-2">
			<label class="tabSubTitle">证件附件</label><br/>
			<br/>
			<table class="normal">
				<tr>
					<td>Id</td>
					<td>类型</td>
					<td>号码</td>
					<td>有效日期</td>
					<td>图片连接</td>
					<td>图片</td>
				</tr>
				
			</table>
		</div>
	</div>
	
	<hr/>
	
<!-- 	end of detail_left -->
	</div>
	
<!-- 	<div id='detail_right'> -->
<!-- 		b -->
<!-- 	</div> -->

</div>

<!-- extra dialogs -->

</body>
</html>