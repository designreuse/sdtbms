package com.bus.stripes.selector;

import java.util.Date;

import com.bus.util.HRUtil;

public class EmpRequestSelector implements BMSSelector {


	private Integer department;
	private Integer position;
	private String extrainfo;
	private Integer numberMatch;
	private Date receiveDate;
	private Integer approveResult;
	
	@Override
	public String getSelectorStatement() {
		int set  = 0;
		String query = "SELECT q FROM EmployeeRequest q WHERE ";
		if(department != null){
			query += " q.department.id="+department;
			set++;
		}
		if(position != null){
			if(set > 0 )
				query += " AND";
			query += " q.position.id="+position;
			set++;
		}
		if(extrainfo != null){
			if(set > 0)
				query += " AND";
			query += " remark LIKE '%" + extrainfo + "%'";
			set++;
		}
		if(numberMatch != null){
			if(numberMatch == 1){
				if(set > 0)
					query += " AND";
				query += " requireNumber!=commitNumber";
				set++;
			}
		}
		if(receiveDate != null){
			if(set > 0)
				query += " AND";
			query += " receiveFormDate>='"+HRUtil.parseDateToString(receiveDate)+"'";
			set++;
		}
		if(approveResult != null){
			if(set > 0)
				query += " AND";
			if(approveResult == 1)
				query += " approveResult=1";
			else if(approveResult == 2)
				query += " approveResult=2";
			else if(approveResult == 0)
				query += " approveResult=0";
			else
				query += " approveResult=null";
			set++;
		}
		query += " ORDER BY receiveFormDate DESC";
		return query;
	}

	
	
	public Integer getDepartment() {
		return department;
	}

	public void setDepartment(Integer department) {
		this.department = department;
	}

	public Integer getPosition() {
		return position;
	}

	public void setPosition(Integer position) {
		this.position = position;
	}

	public String getExtrainfo() {
		return extrainfo;
	}

	public void setExtrainfo(String extrainfo) {
		this.extrainfo = extrainfo;
	}

	public Integer getNumberMatch() {
		return numberMatch;
	}

	public void setNumberMatch(Integer numberMatch) {
		this.numberMatch = numberMatch;
	}

	public Date getReceiveDate() {
		return receiveDate;
	}

	public void setReceiveDate(Date receiveDate) {
		this.receiveDate = receiveDate;
	}

	public Integer getApproveResult() {
		return approveResult;
	}

	public void setApproveResult(Integer approveResult) {
		this.approveResult = approveResult;
	}

	
	
}
