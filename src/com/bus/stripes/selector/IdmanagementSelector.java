package com.bus.stripes.selector;

public class IdmanagementSelector implements BMSSelector{

	private int date = -1; //0 不限，1 证件到期
	private String name;
	private String type;
	private String workerid;
	
	@Override
	public String getSelectorStatement() {
		String str = "SELECT q FROM Idmanagement q WHERE q.employee.status!='D' AND" +
				" q.employee.status!='E' ";
		if(name != null){
			str += " AND q.employee.fullname='"+name+"' ";
		}
		if(type != null){
			str += " AND q.type='" + type + "' ";
		}
		if(workerid != null){
			str += " AND q.employee.workerid='"+workerid+"' ";
		}
		if(date >0){
			str += " AND expiredate > now() ORDER BY expiredate ASC";
		}
		return str;
	}
	
	public int getDate() {
		return date;
	}
	public void setDate(int date) {
		this.date = date;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getWorkerid() {
		return workerid;
	}
	public void setWorkerid(String workerid) {
		this.workerid = workerid;
	}
	
}
