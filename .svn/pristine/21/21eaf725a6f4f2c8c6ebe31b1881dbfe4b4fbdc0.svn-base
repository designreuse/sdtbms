<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="stripes" uri="http://stripes.sourceforge.net/stripes.tld" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="ss" uri="/WEB-INF/StripesSecurityManager.tld" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
<title>${actionBean.coordinate.employee.fullname} 的调动</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />

<script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery-1.7.2.min.js"></script>
<script type="text/javascript">
function editCoor(){
	var formaction = $('#form_edit_coordinate').attr('action');
	var params = $('#form_edit_coordinate').serialize() + "&edit=";
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
	
	<stripes:form id="form_edit_coordinate" beanclass="com.bus.stripes.actionbean.HRCoordinatorActionBean" focus="">
					<stripes:errors/>
					<table>
						<tr>
							<td>类型:</td>
							<td colspan=3>
								<stripes:hidden name="coordinate.id"/>
								<stripes:select name="coordinate.type"><stripes:option value="">请选择....</stripes:option><stripes:options-collection collection="${actionBean.types}" label="label" value="value"/></stripes:select></td>
						</tr>
						<tr>
							<td>姓名:</td><td><stripes:text name="coordinate.employee.fullname" readonly="true"/></td>
							<td>工号:</td><td><stripes:text name="coordinate.employee.workerid" readonly="true"/></td>
						</tr>
						<tr>
							<td>原部门:</td><td><stripes:select name="coordinate.predepartment.id"><stripes:option value="">请选择....</stripes:option><stripes:options-collection collection="${actionBean.departments}" label="label" value="value"/></stripes:select></td>
							<td>原岗位:</td><td><stripes:select name="coordinate.preposition.id"><stripes:option value="">请选择....</stripes:option><stripes:options-collection collection="${actionBean.positions}" label="label" value="value"/></stripes:select></td>
						</tr>
						<tr>
							<td>新部门:</td><td><stripes:select name="coordinate.curdepartment.id"><stripes:option value="">请选择....</stripes:option><stripes:options-collection collection="${actionBean.departments}" label="label" value="value"/></stripes:select></td>
							<td>新岗位:</td><td><stripes:select name="coordinate.curposition.id"><stripes:option value="">请选择....</stripes:option><stripes:options-collection collection="${actionBean.positions}" label="label" value="value"/></stripes:select></td>
						</tr>
						<tr>
							<td>工资停发日:</td><td><stripes:text name="coordinate.movedate"  formatPattern="yyyy-MM-dd" class="datepickerClass required"/></td>
							<td>新工资发放日:</td><td><stripes:text name="coordinate.activedate"  formatPattern="yyyy-MM-dd" class="datepickerClass required"/></td>
						</tr>
						<tr>
							<td>注释:</td><td colspan=3><stripes:textarea name="coordinate.remark"></stripes:textarea></td>
						</tr>
					</table>
					<div>
						<ss:secure roles="employee_coor_edit">
					   <a href="javascript:editCoor();">修改</a>
					   </ss:secure>
					</div>
				</stripes:form>

</body>
</html>