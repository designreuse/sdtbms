package com.bus.stripes.selector;

import com.bus.util.HRUtil;

public class EmployeeSelector implements BMSSelector{

	private String name;
	private String pob;
	private String workertype;
	private int position = 0;
	private int department = 0;
	private String qualification;
	private int age = 0;
	private int set = 0;
	
	@Override
	public String getSelectorStatement() {
		String query = "SELECT q FROM Employee q WHERE";
		if(name != null){
			query += " fullname LIKE '%"+name+"%'";
			set++;
		}
		if(pob != null){
			if(set > 0)
				query += " AND";
			query += " pob LIKE '%"+pob+"%'";
			set++;
		}
		if(workertype != null){
			if(set >0)
				query += " AND";
			query += " workertype ='" +workertype+"'";
			set++;
		}
		if(position != 0){
			if(set > 0)
				query += " AND";
			query += " q.position.id ="+position;
			set++;
		}
		if(department != 0){
			if(set>0)
				query += " AND";
			query += " q.department.id="+department;
			set++;
		}
		if(qualification != null){
			if(set > 0)
				query += " AND";
			query += " qualification LIKE '%"+qualification+"%'";
			set++;
		}
		return query;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getWorkertype() {
		return workertype;
	}
	public void setWorkertype(String workertype) {
		this.workertype = workertype;
	}
	public String getPob() {
		return pob;
	}
	public void setPob(String pob) {
		this.pob = pob;
	}
	public String getQualification() {
		return qualification;
	}
	public void setQualification(String qualification) {
		this.qualification = qualification;
	}
	public int getPosition() {
		return position;
	}
	public void setPosition(int position) {
		this.position = position;
	}
	public int getDepartment() {
		return department;
	}
	public void setDepartment(int department) {
		this.department = department;
	}
	public int getAge() {
		return age;
	}
	public void setAge(int age) {
		this.age = age;
	}

}
