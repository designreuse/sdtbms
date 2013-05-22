package com.bus.stripes.selector;

import java.util.Date;

import com.bus.util.HRUtil;

public class CoordinationSelector implements BMSSelector{
	
	private String name;
	private String workerid;
	private String type;
	private Date startdate;
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

	private Date enddate;
	
	@Override
	public String getSelectorStatement() {
		String query = "SELECT q FROM Promoandtransfer q WHERE";
		int set = 0;
		if(name != null){
			query += " q.employee.fullname LIKE '%"+name+"%'";
			set++;
		}
		if(workerid != null){
			if(set >0)
				query += " AND";
			query += " q.employee.workerid='"+workerid+"'";
			set++;
		}
		if(type != null){
			if(set>0)
				query += " AND";
			query += " type='"+type+"'";
			set++;
		}
		if(startdate != null && enddate != null){
			if(set > 0)
				query += " AND";
			query += " activedate BETWEEN '"+HRUtil.parseDateToString(startdate)+"' AND" +
					" '"+ HRUtil.parseDateToString(enddate)+"'";
			set++;
		}
		query += " ORDER BY createdate DESC";
		return query;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getWorkerid() {
		return workerid;
	}

	public void setWorkerid(String workerid) {
		this.workerid = workerid;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
}
