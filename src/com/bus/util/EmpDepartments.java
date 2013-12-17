package com.bus.util;

import java.util.List;

import com.bus.dto.Employee;

public class EmpDepartments {
	private String deptId;
	private String dept;
	private Integer size=0;
	public List<Employee> emps;
	private List<EmpDepartments> extras = null;
	
	public String getDeptId() {
		return deptId;
	}
	public void setDeptId(String deptId) {
		this.deptId = deptId;
	}
	public String getDept() {
		return dept;
	}
	public void setDept(String dept) {
		this.dept = dept;
	}
	public List<Employee> getEmps() {
		return emps;
	}
	public void setEmps(List<Employee> emps) {
		this.emps = emps;
	}
	public Integer getSize() {
		int count = 0;
		if(emps != null)
			count += emps.size();
		if(extras != null){
			for(EmpDepartments ed : extras){
				count += ed.getSize();
			}
		}
		return count;
	}
	public void setSize(Integer size) {
		this.size = size;
	}
	public List<EmpDepartments> getExtras() {
		return extras;
	}
	public void setExtras(List<EmpDepartments> extras) {
		this.extras = extras;
	}
	
}