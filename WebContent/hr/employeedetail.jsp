<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="stripes" uri="http://stripes.sourceforge.net/stripes.tld" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="ss" uri="/WEB-INF/StripesSecurityManager.tld" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
<title>${actionBean.employee.fullname}</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />

<script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery-1.7.2.min.js"></script>
<%-- <script type="text/javascript" src="${pageContext.request.contextPath}/js/hr.js"></script> --%>
<script type="text/javascript">
function editEmployee(){
	var formaction = $('#form_edit_employee').attr('action');
	var params = $('#form_edit_employee').serialize() + "&edit=";
	$.ajax({
		url:formaction,
		type:"post",
		dataType:'text',
		data:params,
		success:function(response){
			console.log("ajax response = "+response);
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
</script>
</head>
<body>

<div>
	<stripes:form id="form_edit_employee" beanclass="com.bus.stripes.actionbean.EmployeeActionBean" focus="">
					<stripes:errors/>
					<table>
						<tr>
							<td>姓名:</td><td><stripes:text name="employee.fullname"/></td>
							<td>籍贯:</td><td><stripes:text name="employee.pob"/></td>
								<td rowspan="8">照片:</td><td rowspan="7">&nbsp;<stripes:hidden name="employee.hrimage.id"/><stripes:hidden name="employee.id"/></td>
						</tr>
						<tr>
							<td>编号:</td><td><stripes:text name="employee.documentkey"/></td>
							<td>工号:</td><td><stripes:text name="employee.workerid"/></td>
						
						</tr>
						<tr>
							<td>性别:</td><td><stripes:radio name="employee.sex" value="男"/>男 &nbsp; &nbsp; <stripes:radio name="employee.sex" value="女"/>女</td>
							<td>身份证:</td><td><stripes:text name="employee.identitycode"/></td>
						</tr>
						<tr>
							<td>出生年月:</td><td><stripes:text name="employee.dob"  formatPattern="yyyy-MM-dd" class="datepickerClass"/></td>
							<td>民族:</td><td><stripes:select name="employee.ethnic"><stripes:option value="">请选择....</stripes:option><stripes:options-collection collection="${actionBean.ethnic}" label="label" value="value"/></stripes:select></td>
						</tr>
						<tr>
							<td>婚姻状况:</td><td><stripes:select name="employee.marriage"><stripes:option value="">请选择....</stripes:option><stripes:options-collection collection="${actionBean.marriage}" label="label" value="value"/></stripes:select></td>
							<td>政治面貌:</td><td><stripes:select name="employee.politicalstatus"><stripes:option value="">请选择....</stripes:option><stripes:options-collection collection="${actionBean.politicalStatus}" label="label" value="value"/></stripes:select></td>
						</tr>
						<tr>
							<td>联系电话:</td><td><stripes:text name="employee.homenumber"/></td>
							<td>手机号码:</td><td><stripes:text name="employee.mobilenumber"/></td>
						</tr>
						<tr>
							<td>电子邮箱:</td><td><stripes:text name="employee.email"/></td>
							<td>其它联系方式:</td><td><stripes:text name="employee.othercontact"/></td>
						</tr>
						<tr>
							<td>家庭地址:</td><td><stripes:text name="employee.homeaddress"/></td>
							<td>邮编:</td><td><stripes:text name="employee.postcode"/></td>
						</tr>
						<tr>
							<td>入党时间:</td><td><stripes:text name="employee.timeofjoinrpc" formatPattern="yyyy-MM-dd" class="datepickerClass"/></td>
							<td>参加工作时间:</td><td><stripes:text name="employee.firstworktime"  formatPattern="yyyy-MM-dd" class="datepickerClass"/></td>
							<td>职级:</td><td><stripes:select name="employee.joblevel"><stripes:option value="">请选择....</stripes:option><stripes:options-collection collection="${actionBean.joblevel}" label="label" value="value"/></stripes:select></td>
						</tr>
						<tr>
							<td>文化程度:</td><td><stripes:select name="employee.qualification"><stripes:option value="">请选择....</stripes:option><stripes:options-collection collection="${actionBean.qualification}" label="label" value="value"/></stripes:select></td>
							<td>部门:</td><td><stripes:select name="employee.department.id"><stripes:option value="">请选择....</stripes:option><stripes:options-collection collection="${actionBean.department}" label="label" value="value"/></stripes:select></td>
							<td>职位:</td><td><stripes:select name="employee.position.id"><stripes:option value="">请选择....</stripes:option><stripes:options-collection collection="${actionBean.position}" label="label" value="value"/></stripes:select></td>
						</tr>
						<tr>
							<td>所属镇街:</td><td><stripes:text name="employee.placebelong"/></td>
							<td>户籍类型:</td><td><stripes:select name="employee.domiciletype"><stripes:option value="">请选择...</stripes:option><stripes:options-collection collection="${actionBean.domiciletypes}" label="label" value="value"/></stripes:select></td>
							<td>军人:</td><td><stripes:radio name="employee.army" value="是"/>是<stripes:radio name="employee.army" value="否"/>否</td>
						</tr>
						<tr>
							<td>最后毕业学校:</td><td><stripes:text name="employee.colleage"></stripes:text></td>
							<td>专业:</td><td colspan=3><stripes:text name="employee.major"/></td>
						</tr>
						<tr>
							<td>调入时间:</td>
							<td><stripes:text name="employee.transfertime" formatPattern="yyyy-MM-dd" class="datepickerClass"/></td>
							<td>职称:</td>
							<td colspan=3><stripes:select name="employee.workertype"><stripes:option value="">请选择....</stripes:option><stripes:options-collection collection="${actionBean.workertype}" label="label" value="value"/></stripes:select></td>
						</tr>
						<tr>
							<td>注释:</td>
							<td colspan=5><stripes:textarea name="employee.remark"></stripes:textarea></td>
						</tr>
					</table>
					<div>
						<ss:secure roles="employee_edit">
						<a href="javascript:editEmployee();">修改</a>
						</ss:secure>
					</div>
				</stripes:form>
</div>

</body>
</html>