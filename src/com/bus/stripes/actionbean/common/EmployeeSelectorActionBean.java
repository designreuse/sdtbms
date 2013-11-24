package com.bus.stripes.actionbean.common;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import com.bus.dto.Department;
import com.bus.dto.Employee;
import com.bus.dto.vehicleprofile.VehicleTeam;
import com.bus.services.CustomActionBean;
import com.bus.services.HRBean;
import com.bus.stripes.actionbean.MyActionBeanContext;
import com.bus.util.EmpDepartments;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;

import net.sourceforge.stripes.action.ActionBean;
import net.sourceforge.stripes.action.ActionBeanContext;
import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.HandlesEvent;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.action.StreamingResolution;
import net.sourceforge.stripes.action.UrlBinding;
import net.sourceforge.stripes.integration.spring.SpringBean;

@UrlBinding(value="/actionbean/EmployeeSelector.action")
public class EmployeeSelectorActionBean implements ActionBean{

	protected MyActionBeanContext context;
	public MyActionBeanContext getContext() { return context; }
	public void setContext(ActionBeanContext context) { this.context = (MyActionBeanContext)context; }
	
	protected HRBean hrBean;
	@SpringBean
	protected void setBean(HRBean bean){this.hrBean = bean;}
	protected HRBean getBean(){return this.hrBean;}
	
	
	private List<EmpDepartments> departments;
	private String eleIdOne;
	private String eleIdTwo;
	private String extra;
	private Boolean multi;
	private String score;
	
	@DefaultHandler
	public Resolution defaultAction(){
		try{
			if(eleIdOne == null)
				eleIdOne  = context.getRequest().getParameter("eleIdOne");
			if(eleIdTwo == null)
				eleIdTwo = context.getRequest().getParameter("eleIdTwo");
			if(extra == null)
				extra = context.getRequest().getParameter("extra");
			if(multi == null){
				if(context.getRequest().getParameter("multi")!= null)
					multi = true;
				else
					multi = false;
			}
			if(score == null)
				score = context.getRequest().getParameter("score");
			
			List<Department> depts = hrBean.getAllActiveDepartment();
			for(Department d:depts){
				if(departments == null){
					departments = new ArrayList<EmpDepartments>();
				}
				EmpDepartments temD = new EmpDepartments();
				temD.setDept(d.getName());
				temD.setDeptId(d.getId()+"");
				
				List<Department> children = hrBean.getDepartmentChildren(d.getId());
				if(children != null){
					List<EmpDepartments> driverteams = new ArrayList<EmpDepartments>();
					for(Department cd:children){
						EmpDepartments temD2 = new EmpDepartments();
						temD2.setDept(cd.getName());
						temD2.setDeptId(cd.getId()+"");
						List<Employee> temD2Employee = getEmployeeByDepartmentIdForScore(cd.getId());
						temD2.setSize(temD2Employee.size());
						temD2.setEmps(temD2Employee);
						driverteams.add(temD2);
					}
					List<Employee> empNoTeam = getEmployeeByDepartmentIdForScore(d.getId());
					temD.setEmps(empNoTeam);
					int total = empNoTeam.size();
					for(EmpDepartments te:driverteams){
						total += te.getEmps().size();
					}
					temD.setSize(total);
					temD.setExtras(driverteams);
				}else{
					List<Employee> employees = getEmployeeByDepartmentIdForScore(d.getId());
					temD.setSize(employees.size());
					temD.setEmps(employees);
				}
				departments.add(temD);
			}
			return new ForwardResolution("/public/selEmp.jsp");
		}catch(Exception e){
			e.printStackTrace();
			return new ForwardResolution("/public/selEmp.jsp");
		}
	}
	
	private List<Employee> getEmployeeByDepartmentIdForScore(Integer departId) throws Exception{
		if(score != null)
			return hrBean.getEmployeeByDepartmentIdForScore(departId);
		else
			return hrBean.getEmployeeByDepartmentId(departId);
	}
	
	@HandlesEvent(value="getEmployeeSelectionList")
	public Resolution getEmployeeSelectionList(){
		JsonObject json = new JsonObject();
		try{
			List<Department> depts = hrBean.getAllActiveDepartment();
			for(Department d:depts){
				if(departments == null){
					departments = new ArrayList<EmpDepartments>();
				}
				EmpDepartments temD = new EmpDepartments();
				temD.setDept(d.getName());
				temD.setDeptId(d.getId()+"");
				
				List<Department> children = hrBean.getDepartmentChildren(d.getId());
				if(children != null){
					List<EmpDepartments> subDept = new ArrayList<EmpDepartments>();
					for(Department cd:children){
						EmpDepartments temD2 = new EmpDepartments();
						temD2.setDept(cd.getName());
						temD2.setDeptId(cd.getId()+"");
						List<Employee> temD2Employee = hrBean.getEmployeeByDepartmentId(cd.getId());
						temD2.setSize(temD2Employee.size());
						temD2.setEmps(temD2Employee);
						subDept.add(temD2);
					}
					List<Employee> empNoTeam = hrBean.getEmployeeByDepartmentId(d.getId());
					temD.setEmps(empNoTeam);
					int total = empNoTeam.size();
					for(EmpDepartments te:subDept){
						total += te.getEmps().size();
					}
					temD.setSize(total);
					temD.setExtras(subDept);
				}else{
					List<Employee> employees = hrBean.getEmployeeByDepartmentId(d.getId());
					temD.setSize(employees.size());
					temD.setEmps(employees);
				}
				departments.add(temD);
			}
			JsonArray jArray = new JsonArray();
			jArray = getJsonObjectForEmployeeSelection(departments,jArray);
			json.addProperty("result", "1");
			json.add("data", jArray);
			json.addProperty("name", "员工名单");
			json.addProperty("id", "0");
		}catch(Exception e){
			e.printStackTrace();
			json.addProperty("result", "0");
			json.addProperty("msg","获取失败."+e.getMessage());
		}
		return new StreamingResolution("text/charset=utf-8;",json.toString());
	}
	
	/**
	 * 把EmpDepartments转为jsonString返回
	 * @param departments
	 * @param json
	 * @return
	 * @throws Exception
	 */
	private JsonArray getJsonObjectForEmployeeSelection(
			List<EmpDepartments> departments,JsonArray jArray) throws Exception{
		if(departments == null || departments.size() <= 0) return jArray;
		for(EmpDepartments d:departments){
			JsonObject jGroup = new JsonObject();
			jGroup.addProperty("name", d.getDept());
			jGroup.addProperty("id", d.getDeptId());
			if(d.getEmps() != null && d.getEmps().size() > 0){
				JsonArray ja = new JsonArray();
				for(Employee e:d.getEmps()){
					ja.add(new JsonPrimitive(e.getWorkerid()+","+e.getFullname()));
				}
				jGroup.add("emps", ja);
			}
			if(d.getExtras() != null){
				JsonArray ja = new JsonArray();
				ja = getJsonObjectForEmployeeSelection(d.getExtras(),ja);
				jGroup.add("data", ja);
			}
			jArray.add(jGroup);
		}
		return jArray;
	}
	public String getEleIdOne() {
		return eleIdOne;
	}
	public void setEleIdOne(String eleIdOne) {
		this.eleIdOne = eleIdOne;
	}
	public String getEleIdTwo() {
		return eleIdTwo;
	}
	public void setEleIdTwo(String eleIdTwo) {
		this.eleIdTwo = eleIdTwo;
	}
	public String getExtra() {
		return extra;
	}
	public void setExtra(String extra) {
		this.extra = extra;
	}
	public List<EmpDepartments> getDepartments() {
		return departments;
	}
	public void setDepartments(List<EmpDepartments> departments) {
		this.departments = departments;
	}
	
	public boolean isMulti() {
		return multi;
	}
	public void setMulti(boolean multi) {
		this.multi = multi;
	}

	public String getScore() {
		return score;
	}
	public void setScore(String score) {
		this.score = score;
	}

}
