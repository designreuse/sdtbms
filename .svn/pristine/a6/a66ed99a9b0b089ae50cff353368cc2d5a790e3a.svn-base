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

	$('.delVC').click(function(){
		var id = $(this).closest("tr").children().first().children().first().next().val();
		var del = confirm("真的要删除ID:"+id+"?");
		var formurl =  "${pageContext.request.contextPath}/actionbean/VehicleProfile.action?checkId="+id+"&deleteVechileCheck=";
		if(!del)
			return;
		$.ajax({
			url:formurl,
			type:"post",
			dataType:'text',
			success:function(response){
				location.reload();
			},
			error:function(response){
				alert("errors");
			}
		});
	});
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
							<td>自编号:</td><td><stripes:text  class="required" name="profile.selfid"/></td>
							<td>登记证号:</td><td><stripes:text  class="required" name="profile.recordid"/></td>
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
							<td>购置凭证税号:</td><td><stripes:text  class="required" name="profile.ptaxnumber"/></td>
							<td>强制报废日期:</td><td><stripes:text  name="profile.dateinvalidate" formatPattern="yyyy-MM-dd" class="required datepickerClass"/></td>
						</tr>
						<tr>
							<td>分公司:</td>
							<td>
								<stripes:select name="profile.subcompany" class="required">
									<stripes:option value="公交一">公交一</stripes:option>
									<stripes:option value="公交二">公交二</stripes:option>
									<stripes:option value="长途">长途</stripes:option>
									<stripes:option value="其它">其它</stripes:option>
								</stripes:select>
							</td>
							<td>出厂编号:</td><td><stripes:text name="profile.productioncode"/></td>
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
				<li><a href="#fileTabs-1">一保&二保</a></li>
				<li><a href="#fileTabs-2">维修记录</a></li>
				<li><a href="#fileTabs-3">综合检测</a></li>
				<li><a href="#fileTabs-4">年审</a></li>
				<li><a href="#fileTabs-5">附加证件</a></li>
		</ul>
		<div id="fileTabs-1">
			<label class="tabSubTitle">一保&二保</label><br/>
			<br/>
			<div>
				<ss:secure roles="vehicle_file_maintenance">
				<stripes:form beanclass="com.bus.stripes.actionbean.vehicle.VehicleProfileActionBean">
				<input type="hidden" name="returnLink" value="${returnLink}"/>
				<input type="hidden" name="targetId" value="${actionBean.profile.id}"/>
					<table class="normalAdd">
						<tr>
							<td>类型</td>
							<td>间隔公里数</td>
							<td>日期</td>
							<td>注释</td>
							<td>文件</td>
							<td></td>
						</tr>
						<tr>
							<td><stripes:select name="check.ctype"><stripes:option value="一保">一保</stripes:option><stripes:option value="二保">二保</stripes:option></stripes:select></td>
							<td><stripes:text name="check.miles"/></td>
							<td><stripes:text name="check.cdate" formatPattern="yyyy-MM-dd" class="required datepickerClass"/></td>
							<td><stripes:text name="check.remark"/></td>
							<td><stripes:file name="checkFile" /></td>
							<td><stripes:submit name="addVehicleCheck" value="添加"/></td>
						</tr>
					</table>
				</stripes:form>
				</ss:secure>
			</div>
			<br/>
			<br/>
			<table class="normal">
				<tr>
					<td>Id</td>
					<td>类型</td>
					<td>间隔公里数</td>
					<td>日期</td>
					<td>文件</td>
					<td>注释</td>
					<td></td>
				</tr>
				<c:forEach items="${actionBean.maintenances}" var="maint" varStatus="loop">
				<stripes:form beanclass="com.bus.stripes.actionbean.vehicle.VehicleProfileActionBean">
					<tr>
						<td>${maint.id}
							<input type="hidden" name="returnLink" value="${returnLink}"/>
							<input type="hidden" name="checkId" value="${maint.id}"/>
						</td>
						<td>${maint.ctype}</td>
						<td>${maint.miles}</td>
						<td>${maint.cdateStr}</td>
						<td>
							<a target="_blank" href="${actionBean.context.hrhostidfile}车辆/${maint.ctype}/${maint.image.filename}">查看</a>
							<stripes:file name="checkFile"/>
						</td>
						<td>${maint.remark}</td>
						<td>
							<ss:secure roles="vehicle_file_maintenance">
								<stripes:submit name="updateVehicleCheck" value="更新"/>
								<a class="delVC" href="javascript:void;">删除</a>
							</ss:secure>
						</td>
					</tr>
				</stripes:form>
				</c:forEach>
			</table>
		</div>
		
		<div id="fileTabs-2">
			<label class="tabSubTitle">维修记录</label><br/>
			<br/>
			<div>
				<ss:secure roles="vehicle_file_repair">
				<stripes:form beanclass="com.bus.stripes.actionbean.vehicle.VehicleProfileActionBean">
				<input type="hidden" name="returnLink" value="${returnLink}"/>
				<input type="hidden" name="targetId" value="${actionBean.profile.id}"/>
					<table class="normalAdd">
						<tr>
							<td>类型</td>
							<td>间隔公里数</td>
							<td>日期</td>
							<td>注释</td>
							<td>文件</td>
							<td></td>
						</tr>
						<tr>
							<td><stripes:select name="check.ctype"><stripes:option value="小修">小修</stripes:option><stripes:option value="大修">大修</stripes:option><stripes:option value="中修">中修</stripes:option></stripes:select></td>
							<td><stripes:text name="check.miles"/></td>
							<td><stripes:text name="check.cdate" formatPattern="yyyy-MM-dd" class="required datepickerClass"/></td>
							<td><stripes:text name="check.remark"/></td>
							<td><stripes:file name="checkFile" /></td>
							<td><stripes:submit name="addVehicleCheck" value="添加"/></td>
						</tr>
					</table>
				</stripes:form>
				</ss:secure>
			</div>
			<br/>
			<br/>
			<table class="normal">
				<tr>
					<td>Id</td>
					<td>类型</td>
					<td>间隔公里数</td>
					<td>日期</td>
					<td>文件</td>
					<td>注释</td>
					<td></td>
				</tr>
				<c:forEach items="${actionBean.repairs}" var="rep" varStatus="loop">
				<stripes:form beanclass="com.bus.stripes.actionbean.vehicle.VehicleProfileActionBean">
					<tr>
						<td>${rep.id}
							<input type="hidden" name="returnLink" value="${returnLink}"/>
							<input type="hidden" name="checkId" value="${rep.id}"/>
						</td>
						<td>${rep.ctype}</td>
						<td>${rep.miles}</td>
						<td>${rep.cdateStr}</td>
						<td>
							<a target="_blank" href="${actionBean.context.hrhostidfile}车辆/${rep.ctype}/${rep.image.filename}">查看</a>
							<stripes:file name="checkFile"/>
						</td>
						<td>${rep.remark}</td>
						<td>
							<ss:secure roles="vehicle_file_repair">
								<stripes:submit name="updateVehicleCheck" value="更新"/>
								<a class="delVC" href="javascript:void;">删除</a>
							</ss:secure>
						</td>
					</tr>
				</stripes:form>
				</c:forEach>
			</table>
		</div>
		
		<div id="fileTabs-3">
			<label class="tabSubTitle">综合检测</label><br/>
			<br/>
			<div>
				<ss:secure roles="vehicle_file_fullcheck">
				<stripes:form beanclass="com.bus.stripes.actionbean.vehicle.VehicleProfileActionBean">
				<input type="hidden" name="returnLink" value="${returnLink}"/>
				<input type="hidden" name="targetId" value="${actionBean.profile.id}"/>
					<table class="normalAdd">
						<tr>
							<td>类型</td>
							<td>间隔公里数</td>
							<td>日期</td>
							<td>注释</td>
							<td>文件</td>
							<td></td>
						</tr>
						<tr>
							<td><stripes:text name="check.ctype" value="综合" readonly="readonly"/></td>
							<td><stripes:text name="check.miles"/></td>
							<td><stripes:text name="check.cdate" formatPattern="yyyy-MM-dd" class="required datepickerClass"/></td>
							<td><stripes:text name="check.remark"/></td>
							<td><stripes:file name="checkFile" /></td>
							<td><stripes:submit name="addVehicleCheck" value="添加"/></td>
						</tr>
					</table>
				</stripes:form>
				</ss:secure>
			</div>
			<br/>
			<br/>
			<table class="normal">
				<tr>
					<td>Id</td>
					<td>类型</td>
					<td>间隔公里数</td>
					<td>日期</td>
					<td>文件</td>
					<td>注释</td>
					<td></td>
				</tr>
				<c:forEach items="${actionBean.fullchecks}" var="rep" varStatus="loop">
				<stripes:form beanclass="com.bus.stripes.actionbean.vehicle.VehicleProfileActionBean">
					<tr>
						<td>${rep.id}
							<input type="hidden" name="returnLink" value="${returnLink}"/>
							<input type="hidden" name="checkId" value="${rep.id}"/>
						</td>
						<td>${rep.ctype}</td>
						<td>${rep.miles}</td>
						<td>${rep.cdateStr}</td>
						<td>
							<a target="_blank" href="${actionBean.context.hrhostidfile}车辆/${rep.ctype}/${rep.image.filename}">查看</a>
							<stripes:file name="checkFile"/>
						</td>
						<td>${rep.remark}</td>
						<td>
							<ss:secure roles="vehicle_file_fullcheck">
								<stripes:submit name="updateVehicleCheck" value="更新"/>
								<a class="delVC" href="javascript:void;">删除</a>
							</ss:secure>
						</td>
					</tr>
				</stripes:form>
				</c:forEach>
			</table>
		</div>
		
		<div id="fileTabs-4">
			<label class="tabSubTitle">年审</label><br/>
			<br/>
			<div>
				<ss:secure roles="vehicle_file_annul">
				<stripes:form beanclass="com.bus.stripes.actionbean.vehicle.VehicleProfileActionBean">
				<input type="hidden" name="returnLink" value="${returnLink}"/>
				<input type="hidden" name="targetId" value="${actionBean.profile.id}"/>
					<table class="normalAdd">
						<tr>
							<td>类型</td>
							<td>间隔公里数</td>
							<td>日期</td>
							<td>注释</td>
							<td>文件</td>
							<td></td>
						</tr>
						<tr>
							<td><stripes:text name="check.ctype" value="年审" readonly="readonly"/></td>
							<td><stripes:text name="check.miles"/></td>
							<td><stripes:text name="check.cdate" formatPattern="yyyy-MM-dd" class="required datepickerClass"/></td>
							<td><stripes:text name="check.remark"/></td>
							<td><stripes:file name="checkFile" /></td>
							<td><stripes:submit name="addVehicleCheck" value="添加"/></td>
						</tr>
					</table>
				</stripes:form>
				</ss:secure>
			</div>
			<br/>
			<br/>
			<table class="normal">
				<tr>
					<td>Id</td>
					<td>类型</td>
					<td>间隔公里数</td>
					<td>日期</td>
					<td>文件</td>
					<td>注释</td>
					<td></td>
				</tr>
				<c:forEach items="${actionBean.annul}" var="rep" varStatus="loop">
				<stripes:form beanclass="com.bus.stripes.actionbean.vehicle.VehicleProfileActionBean">
					<tr>
						<td>${rep.id}
							<input type="hidden" name="returnLink" value="${returnLink}"/>
							<input type="hidden" name="checkId" value="${rep.id}"/>
						</td>
						<td>${rep.ctype}</td>
						<td>${rep.miles}</td>
						<td>${rep.cdateStr}</td>
						<td>
							<a target="_blank" href="${actionBean.context.hrhostidfile}车辆/${rep.ctype}/${rep.image.filename}">查看</a>
							<stripes:file name="checkFile"/>
						</td>
						<td>${rep.remark}</td>
						<td>
							<ss:secure roles="vehicle_file_annul">
								<stripes:submit name="updateVehicleCheck" value="更新"/>
								<a class="delVC" href="javascript:void;">删除</a>
							</ss:secure>
						</td>
					</tr>
				</stripes:form>
				</c:forEach>
			</table>
		</div>
		
		<div id="fileTabs-5">
			<label class="tabSubTitle">附加证件</label><br/>
			<br/>
			<div>
				<ss:secure roles="vehicle_file_extras">
				<stripes:form beanclass="com.bus.stripes.actionbean.vehicle.VehicleProfileActionBean">
				<input type="hidden" name="returnLink" value="${returnLink}"/>
				<input type="hidden" name="targetId" value="${actionBean.profile.id}"/>
					<table class="normalAdd">
						<tr>
							<td>类型</td>
							<td>间隔公里数</td>
							<td>日期</td>
							<td>注释</td>
							<td>文件</td>
							<td></td>
						</tr>
						<tr>
							<td><stripes:text name="check.ctype" value="附件" readonly="readonly"/></td>
							<td><stripes:text name="check.miles"/></td>
							<td><stripes:text name="check.cdate" formatPattern="yyyy-MM-dd" class="required datepickerClass"/></td>
							<td><stripes:text name="check.remark"/></td>
							<td><stripes:file name="checkFile" /></td>
							<td><stripes:submit name="addVehicleCheck" value="添加"/></td>
						</tr>
					</table>
				</stripes:form>
				</ss:secure>
			</div>
			<br/>
			<br/>
			<table class="normal">
				<tr>
					<td>Id</td>
					<td>类型</td>
					<td>间隔公里数</td>
					<td>日期</td>
					<td>文件</td>
					<td>注释</td>
					<td></td>
				</tr>
				<c:forEach items="${actionBean.extras}" var="rep" varStatus="loop">
				<stripes:form beanclass="com.bus.stripes.actionbean.vehicle.VehicleProfileActionBean">
					<tr>
						<td>${rep.id}
							<input type="hidden" name="returnLink" value="${returnLink}"/>
							<input type="hidden" class="check_id" name="checkId" value="${rep.id}"/>
						</td>
						<td>${rep.ctype}</td>
						<td>${rep.miles}</td>
						<td>${rep.cdateStr}</td>
						<td>
							<a target="_blank" href="${actionBean.context.hrhostidfile}车辆/${rep.ctype}/${rep.image.filename}">查看</a>
							<stripes:file name="checkFile"/>
						</td>
						<td>${rep.remark}</td>
						<td>
							<ss:secure roles="vehicle_file_extras">
								<stripes:submit name="updateVehicleCheck" value="更新"/>
								&nbsp;&nbsp;
								<a class="delVC" href="javascript:void;">删除</a>
							</ss:secure>
						</td>
					</tr>
				</stripes:form>
				</c:forEach>
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