<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="stripes" uri="http://stripes.sourceforge.net/stripes.tld" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="ss" uri="/WEB-INF/StripesSecurityManager.tld" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>车辆系统Function</title>
</head>
<body>
<ss:secure roles="administrator_system">
<div>
<stripes:form beanclass="com.bus.stripes.actionbean.vehicle.VehicleFunctionActionBean">
	<div>
		文件夹名字<stripes:text name="picFileName"/>
		<stripes:submit name="sortProfilePic" value="整理新车辆头像图片"/>
	</div>
	<div>
		<stripes:file name="multipleVehicles" />
		<stripes:submit name="importVehicleProfileExcelFiles" value="上传车辆档案excel文件(多车)"/>
	</div>
</stripes:form>

<stripes:form beanclass="com.bus.stripes.actionbean.score.ScoreGroupActionBean">
	<stripes:submit name="getScoreMembersFromDepartment" value="获取积分组员"/>
</stripes:form>
</div>
</ss:secure>
</body>
</html>