package com.bus.stripes.actionbean.common;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import com.bus.dto.Department;
import com.bus.dto.Employee;
import com.bus.services.CustomActionBean;
import com.bus.services.HRBean;
import com.bus.stripes.actionbean.MyActionBeanContext;

import net.sourceforge.stripes.action.ActionBean;
import net.sourceforge.stripes.action.ActionBeanContext;
import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.Resolution;
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
			
			List<Department> depts = hrBean.getAllActiveDepartment();
			for(Department d:depts){
//				System.out.println("printing time for "+d.getName()+": " + Calendar.getInstance().getTimeInMillis());
				if(departments == null){
					departments = new ArrayList<EmpDepartments>();
				}
				EmpDepartments temD = new EmpDepartments();
				temD.setDept(d.getName());
				temD.setDeptId(d.getId()+"");
				List<Employee> employees = hrBean.getEmployeeByDepartmentId(d.getId());
				temD.setSize(employees.size());
//				System.out.println("done "+ Calendar.getInstance().getTimeInMillis());
				List<SimpleEmployee> ses = new ArrayList<SimpleEmployee>();
				for(Employee e:employees){
					SimpleEmployee se = new SimpleEmployee();
					se.setName(e.getFullname());
					se.setWorkerId(e.getWorkerid());
					ses.add(se);
				}
				temD.setEmp(ses);
//				temD.setEmps(employees);
				departments.add(temD);
			}
			return new ForwardResolution("/public/selEmp.jsp");
		}catch(Exception e){
			e.printStackTrace();
			return new ForwardResolution("/public/selEmp.jsp");
		}
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

	public class EmpDepartments {
		private String deptId;
		private String dept;
		private Integer size;
		public List<SimpleEmployee> emp;
		public List<Employee> emps;
		
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
		public List<SimpleEmployee> getEmp() {
			return emp;
		}
		public void setEmp(List<SimpleEmployee> emp) {
			this.emp = emp;
		}
		public List<Employee> getEmps() {
			return emps;
		}
		public void setEmps(List<Employee> emps) {
			this.emps = emps;
		}
		public Integer getSize() {
			return size;
		}
		public void setSize(Integer size) {
			this.size = size;
		}
		
	}
	
	public class SimpleEmployee{
		private String name;
		private String workerId;
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
		public String getWorkerId() {
			return workerId;
		}
		public void setWorkerId(String workerId) {
			this.workerId = workerId;
		}
		
		
	}
}
