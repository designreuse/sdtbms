package com.bus.scheduler.job;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import net.sourceforge.stripes.integration.spring.SpringBean;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.JobKey;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

import com.bus.email.BMSEmail;
import com.bus.services.EMBean;
import com.bus.services.ScoreBean;

public class CheckScoreGroupMemberJob implements Job,GeneralJob{

	@Autowired
	private EMBean emBean;
	
	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		// Say Hello to the World and display the date/time
		SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this);
		JobKey jobKey = context.getJobDetail().getKey();
		try{
			String query = "SELECT e.fullname as fullname,d.name as dname,p.name as pname FROM employee e LEFT JOIN score_group_mapper m ON e.id = m.empid "+
					" LEFT JOIN position p ON p.id = e.positionid LEFT JOIN scoreexceptionlist excep ON e.positionid=excep.positionid" +
					" LEFT JOIN department d ON e.departmentid=d.id"+
				" WHERE e.status='A' AND m.id IS NULL AND (excep.status IS NULL OR excep.status != 0) ORDER BY d.name,p.name";
			List<Object[]> result  = (List<Object[]>) emBean.runQuery(query);
			String detail = "以下员工需要编入积分组但还没有编入任何积分组。为方便系统运作，请尽快将以下员工添加到积分组。";
			String table = "<table>";
			for(Object[] ret:result){
				table += "<tr>";
				table += "<td>"+(String)ret[1]+"</td>";
				table += "<td>"+(String)ret[2]+"</td>";
				table += "<td>"+(String)ret[0]+"</td>";
				table += "</tr>";
			}
			table += "</table>";
			List<String> reciplin = new ArrayList<String>();
			reciplin.add("459384937@qq.com");
			BMSEmail email  = new BMSEmail("秀贞",reciplin,"积分组省缺员工",detail+table);
			if(email.sendEmail()){
				if(D) System.out.println("Email Send.");
			}else{
				if(D) System.out.println("Email Not Send.");
			}
		}catch(Exception e){
			e.printStackTrace();
			if(D) System.out.println("Execute job "+jobKey+" fail.");
		}
	}
}
