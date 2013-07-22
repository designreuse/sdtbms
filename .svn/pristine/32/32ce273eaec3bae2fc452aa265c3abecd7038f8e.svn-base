package com.bus.stripes.selector;

import java.util.Date;

import com.bus.util.HRUtil;

public class DriverExamSelector implements BMSSelector{

	private Integer roadexam;
	private Integer zhuangexam;
	private Date examdate;
	private String name;
	
	@Override
	public String getSelectorStatement() {
		int set = 0;
		String query = "SELECT q FROM DrivingExam q WHERE";
		if(name != null){
			query += " q.applicant.name='"+name+"'";
			set++;
		}
		if(examdate != null){
			if(set >0)
				query+= " AND";
			query += " examdate='"+HRUtil.parseDateToString(examdate)+"'";
			set++;
		}
		if(roadexam != null && !roadexam.equals("")){
			if(set >0)
				query += " AND";
			query += " roadPass="+roadexam;
			set++;
		}
		if(zhuangexam != null && !zhuangexam.equals("")){
			if(set >0)
				query += " AND";
			query += " zhuangPass="+zhuangexam;
			set++;
		}
		query += " ORDER BY q.applicant.name";
		return query;
	}


	public Integer getRoadexam() {
		return roadexam;
	}

	public void setRoadexam(Integer roadexam) {
		this.roadexam = roadexam;
	}

	public Integer getZhuangexam() {
		return zhuangexam;
	}

	public void setZhuangexam(Integer zhuangexam) {
		this.zhuangexam = zhuangexam;
	}

	public Date getExamdate() {
		return examdate;
	}

	public void setExamdate(Date examdate) {
		this.examdate = examdate;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
