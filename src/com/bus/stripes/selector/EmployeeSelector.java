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
	private String sex;
	private String ethnic;
	private String politicalstatus;
	private int date=-1; //0 nothing,1 入职日期
	private String marriage;
	private String joblevel;
	private String placebelong;
	private String domiciletype;
	private String army;
	private String status;
	
	
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
		if(sex != null){
			if(set > 0)
				query += " AND";
			query += " sex='"+sex+"'";
			set++;
		}
		if(ethnic != null){
			if(set > 0)
				query += " AND";
			query += " ethnic='"+ethnic +"'";
			set++;
		}
		if(politicalstatus != null){
			if(set > 0)
				query += " AND";
			query += " politicalstatus='"+politicalstatus+"'";
			set++;
		}
		if(marriage != null){
			if(set > 0)
				query += " AND";
			query += " marriage='"+marriage+"'";
			set++;
		}
		if(joblevel != null){
			if(set > 0)
				query += " AND";
			query += " joblevel='"+joblevel+"'";
			set++;
		}
		if(domiciletype != null){
			if(set >0)
				query = " AND";
			query += " domiciletype='"+domiciletype+"'";
			set++;
		}
		if(army != null){
			if(set >0)
				query = " AND";
			query += " army='"+army+"'";
			set++;
		}
		if(placebelong != null){
			if(set >0)
				query = " AND";
			query += " placebelong LIKE '%"+placebelong+"%'";
			set++;
		}
		if(status != null){
			if(set > 0)
				query += " AND";
			String[] stats = status.split(",");
			if(stats.length >= 2)
				query += " (";
			for(int i=0; i<stats.length;i++){
				if(i != 0)
					query += " OR";
				query += " status='"+stats[i]+"'";
				set++;
			}
			if(stats.length >= 2)
				query +=")";
		}else{
			if(set > 0)
				query += " AND";
			query += " status='A'";
		}
		if(date > 0){
			if(set >0){
				if(date == 1){
					query += " ORDER BY firstworktime ASC";
				}
			}else{
				query = "SELECT q FROM Employee q ORDER BY firstworktime ASC";
			}
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
	public String getSex() {
		return sex;
	}
	public void setSex(String sex) {
		this.sex = sex;
	}
	public String getEthnic() {
		return ethnic;
	}
	public void setEthnic(String ethnic) {
		this.ethnic = ethnic;
	}
	public String getPoliticalstatus() {
		return politicalstatus;
	}
	public void setPoliticalstatus(String politicalstatus) {
		this.politicalstatus = politicalstatus;
	}
	public int getDate() {
		return date;
	}
	public void setDate(int date) {
		this.date = date;
	}
	public String getMarriage() {
		return marriage;
	}
	public void setMarriage(String marriage) {
		this.marriage = marriage;
	}
	public String getJoblevel() {
		return joblevel;
	}
	public void setJoblevel(String joblevel) {
		this.joblevel = joblevel;
	}
	public String getPlacebelong() {
		return placebelong;
	}
	public void setPlacebelong(String placebelong) {
		this.placebelong = placebelong;
	}
	public String getDomiciletype() {
		return domiciletype;
	}
	public void setDomiciletype(String domiciletype) {
		this.domiciletype = domiciletype;
	}
	public String getArmy() {
		return army;
	}
	public void setArmy(String army) {
		this.army = army;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}


}
