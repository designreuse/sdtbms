package com.bus.stripes.selector;

import java.util.Date;

import com.bus.util.HRUtil;

public class EmploymentSelector implements BMSSelector{

	private Date applyDateA;
	private Date applyDateB;
	private String name;
	private String position;
	private Integer driver;
	private String domicile;
	private Integer dorm;
	private Integer bodyCheckNoti;
	private Date joinDateA;
	private Date joinDateB;
	private Integer approveResult;
	private Integer	bodyCheck;
	private Integer interview;
	private Integer driverexam;
	

	@Override
	public String getSelectorStatement() {
		int set = 0;
		String query = "SELECT q FROM HRApplication q WHERE ";
		if(name != null){
			query += " name LIKE '%"+name+"%'";
			set++;
		}
		if(applyDateA != null || applyDateB != null){
			if(set > 0)
				query += " AND";
			if(applyDateA == null){
				query += " applyDate<='"+HRUtil.parseDateToString(applyDateB)+"'";
			}else if(applyDateB == null){
				query += " applyDate>='"+HRUtil.parseDateToString(applyDateA)+"'";
			}else if(applyDateA.getTime() == applyDateB.getTime()){
				query += " applyDate='"+HRUtil.parseDateToString(applyDateA)+"'";
			}else{
				query += " applyDate<='"+HRUtil.parseDateToString(applyDateB) + "'"+
						" AND applyDate>='"+HRUtil.parseDateToString(applyDateA)+"'";
			}
			set++;
		}
		if(position != null){
			if(set>0)
				query += " AND";
			query += " q.position.name LIKE '%"+ position +"%'";
			set++;
		}
		if(driver != null){
			if(set>0)
				query += " AND";
			if(driver == 1)
				query += " driver="+driver;
			else
				query += " (driver!=1 OR driver=null)";
			set++;
		}
		if(domicile != null){
			if(set>0)
				query += " AND";
			query += " pol LIKE '%"+domicile+"%'";
			set++;
		}
		if(dorm != null){
			if(set >0)
				query += " AND";
			if(dorm == 1)
				query += " dorm="+dorm;
			else
				query += " (dorm!=1 OR dorm=null)";
			set++;
		}
		if(bodyCheckNoti != null){
			if(set >0)
				query += " AND";
			if(bodyCheckNoti == 0)
				query += " (bodyCheckNoti=0 OR bodyCheckNoti=null)";
			else
				query += " bodyCheckNoti="+bodyCheckNoti;
			set++;
		}
		if(joinDateA != null || joinDateB != null){
			if(set >0)
				query += " AND";
			if(joinDateA == null){
				query += " joinDate<='"+HRUtil.parseDateToString(joinDateB)+"'";
			}else if(joinDateB == null){
				query += " joinDate>='"+HRUtil.parseDateToString(joinDateA)+"'";
			}else if(joinDateA.getTime() == joinDateB.getTime()){
				query += " joinDate='"+HRUtil.parseDateToString(joinDateB)+"'";
			}else{
				query += " joinDate<='"+HRUtil.parseDateToString(joinDateB)+"'" +
						" AND joinDate>='"+HRUtil.parseDateToString(joinDateA)+"'";
			}
			set++;
		}
		if(approveResult != null){
			if(set>0)
				query += " AND";
			if(approveResult == 0)
				query += " (approveResult=0 OR　approveResult=null)";
			else
				query += " approveResult="+approveResult;
			set++;
		}
		if(bodyCheck != null){
			if(set>0)
				query += " AND";
			query += " bodyCheckPass="+bodyCheck;
			set++;
		}
		if(interview != null){
			if(set>0)
				query += " AND";
			if(interview == 0)
				query += " (interviewResult=0 OR interviewResult=null)";
			else
				query += " interviewResult="+interview;
			set++;
		}
		if(driverexam != null){
			if(set>0)
				query += " AND";
			if(driverexam == 0)
				query += " (driverexamresult=0 OR driverexamresult=null)";
			else
				query += " driverexamresult="+driverexam;
			set++;
		}
		query += " ORDER BY applyDate DESC";
		return query;
	}
	
	public Date getApplyDateA() {
		return applyDateA;
	}

	public void setApplyDateA(Date applyDateA) {
		this.applyDateA = applyDateA;
	}

	public Date getApplyDateB() {
		return applyDateB;
	}

	public void setApplyDateB(Date applyDateB) {
		this.applyDateB = applyDateB;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPosition() {
		return position;
	}

	public void setPosition(String position) {
		this.position = position;
	}

	public Integer getDriver() {
		return driver;
	}

	public void setDriver(Integer driver) {
		this.driver = driver;
	}

	public String getDomicile() {
		return domicile;
	}

	public void setDomicile(String domicile) {
		this.domicile = domicile;
	}

	public Integer getDorm() {
		return dorm;
	}

	public void setDorm(Integer dorm) {
		this.dorm = dorm;
	}

	public Integer getBodyCheckNoti() {
		return bodyCheckNoti;
	}

	public void setBodyCheckNoti(Integer bodyCheckNoti) {
		this.bodyCheckNoti = bodyCheckNoti;
	}

	public Date getJoinDateA() {
		return joinDateA;
	}

	public void setJoinDateA(Date joinDateA) {
		this.joinDateA = joinDateA;
	}

	public Date getJoinDateB() {
		return joinDateB;
	}

	public void setJoinDateB(Date joinDateB) {
		this.joinDateB = joinDateB;
	}

	public Integer getApproveResult() {
		return approveResult;
	}

	public void setApproveResult(Integer approveResult) {
		this.approveResult = approveResult;
	}

	public Integer getBodyCheck() {
		return bodyCheck;
	}

	public void setBodyCheck(Integer bodyCheck) {
		this.bodyCheck = bodyCheck;
	}

	public Integer getInterview() {
		return interview;
	}

	public void setInterview(Integer interview) {
		this.interview = interview;
	}

	public Integer getDriverexam() {
		return driverexam;
	}

	public void setDriverexam(Integer driverexam) {
		this.driverexam = driverexam;
	}


}
