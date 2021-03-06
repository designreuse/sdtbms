package com.bus.stripes.actionbean;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.Calendar;
import java.util.Date;

import org.apache.commons.io.FileUtils;

import security.action.Secure;

import com.bus.dto.Account;
import com.bus.services.CustomActionBean;
import com.bus.services.HRBean;
import com.bus.stripes.selector.EmployeeSelector;
import com.bus.util.ExcelFileWriter;
import com.bus.util.Roles;

import net.sourceforge.stripes.action.ActionBean;
import net.sourceforge.stripes.action.ActionBeanContext;
import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.HandlesEvent;
import net.sourceforge.stripes.action.RedirectResolution;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.action.StreamingResolution;
import net.sourceforge.stripes.action.UrlBinding;
import net.sourceforge.stripes.integration.spring.SpringBean;

@UrlBinding("/actionbean/Filedownload.action")
public class FiledownloadActionBean extends CustomActionBean implements Permission{
	
	private Date startdate;
	private Date enddate;
	private String coortype;
	
	public HRBean getBean() {
		return bean;
	}
	@SpringBean
	public void setBean(HRBean bean) {
		this.bean = bean;
	}

	private HRBean bean;
	
	
	@DefaultHandler
	public Resolution defaultAction(){
		return new StreamingResolution("text/html","没有文件可供下载");
	}
	
	@HandlesEvent(value="employees")
	@Secure(roles=Roles.EMPLOYEE_DATA_DOWNLOAD)
	public Resolution downloadEmployee(){
			ExcelFileWriter writer = new ExcelFileWriter();
			String content = writer.writeEmployees(bean,"A");
			
			StreamingResolution sr = new StreamingResolution("text/csv",content);
			sr.setAttachment(true);
			sr.setCharacterEncoding("UTF-8");
			sr.setFilename("employees.csv");
			return sr;
	}
	
	@HandlesEvent(value="drivers")
	@Secure(roles=Roles.EMPLOYEE_DRIVER_DATA_DOWNLOAD)
	public Resolution downloadDrivers(){
			ExcelFileWriter writer = new ExcelFileWriter();
			String content = writer.writeDrivers(bean);
			StreamingResolution sr = new StreamingResolution("text/csv",content);
			sr.setAttachment(true);
			sr.setCharacterEncoding("UTF-8");
			sr.setFilename("drivers.csv");
			return sr;
	}
	
	@HandlesEvent(value="monthreport")
	@Secure(roles=Roles.EMPLOYEE_COOR_DATA_DOWNLOAD)
	public Resolution monthreport(){
		ExcelFileWriter writer = new ExcelFileWriter();
		String content = writer.writeCoordination(bean,startdate, enddate, coortype);
		StreamingResolution sr = new StreamingResolution("text/csv",content);
		sr.setAttachment(true);
		sr.setCharacterEncoding("UTF-8");
		Calendar c1 = Calendar.getInstance();
		c1.setTime(enddate);
		String name = coortype+c1.get(Calendar.YEAR)+"-"+(c1.get(Calendar.MONDAY)+1)+"月.csv";
		sr.setFilename(name);
		return sr;
	}
	
	public Date getStartdate() {
		return startdate;
	}
	public void setStartdate(Date startdate) {
		this.startdate = startdate;
	}
	public Date getEnddate() {
		return enddate;
	}
	public void setEnddate(Date enddate) {
		this.enddate = enddate;
	}
	public String getCoortype() {
		return coortype;
	}
	public void setCoortype(String coortype) {
		this.coortype = coortype;
	}

	
}
