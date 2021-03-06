<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="stripes" uri="http://stripes.sourceforge.net/stripes.tld" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="ss" uri="/WEB-INF/StripesSecurityManager.tld" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
<title>${actionBean.scoretype.reference}</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />

<script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery-1.7.2.min.js"></script>
<script type="text/javascript">
function editScoretype(){
	var formaction = $('#form_edit_item').attr('action');
	var params = $('#form_edit_item').serialize() + "&editscoretype=";
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
	<stripes:form id="form_edit_item" beanclass="com.bus.stripes.actionbean.score.ScoreitemsActionBean">
						<div class='hastable'>
						<table>
								<tr>
									<td>编号:</td><td>
									<stripes:hidden name="scoretype.id"/>
									<stripes:text name="scoretype.reference" class="required"/></td>
								</tr>
								<tr>
									<td>类型:</td><td><stripes:select name="scoretype.type"  class="required"><stripes:option value="0">临时分</stripes:option><stripes:option value="1">固定分</stripes:option></stripes:select></td>
								</tr>
								<tr>
									<td>条例:</td><td><stripes:textarea name="scoretype.description"  class="required"/></td>
								</tr>
								<tr>	
									<td>分值:</td><td><stripes:text name="scoretype.score"  class="required" formatType="number"></stripes:text></td>
								</tr>
								<tr>	
									<td>周期:</td><td><stripes:text name="scoretype.period"  class="required"></stripes:text></td>
								</tr>
								<tr>	
									<td>考核部门:</td><td><stripes:text name="scoretype.examine"  class="required"></stripes:text></td>
								</tr>
								<tr>	
									<td>评分对象:</td><td><stripes:text name="scoretype.scoreobject"  class="required"></stripes:text></td>
								</tr>
								<tr>	
									<td>备注:</td>
									<td>
										<stripes:textarea name="scoretype.remark" cols="40" rows="7"/>
									</td>
								</tr>
						</table>
						</div>
						<ss:secure roles="score_items_edit">
						<a href="javascript:editScoretype();">修改</a>
						</ss:secure>
					</stripes:form>
</div>

</body>
</html>