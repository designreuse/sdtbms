package com.bus.stripes.actionbean.score;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.action.UrlBinding;
import net.sourceforge.stripes.integration.spring.SpringBean;

import com.bus.dto.Department;
import com.bus.dto.score.DepartmentScore;
import com.bus.services.CustomActionBean;
import com.bus.services.HRBean;
import com.bus.services.ScoreBean;

@UrlBinding("/actionbean/DepartmentScore.action")
public class DepartmentScoreActionBean extends CustomActionBean{

	private HRBean hrBean;
	public HRBean getHrBean() {
		return hrBean;
	}
	@SpringBean
	public void setHrBean(HRBean bean) {
		this.hrBean = bean;
	}
	private ScoreBean scoreBean;
	public ScoreBean getScoreBean() {
		return this.scoreBean;
	}

	@SpringBean
	public void setScoreBean(ScoreBean bean) {
		this.scoreBean = bean;
	}
	
	List<Department> departments;
	private DepartmentScore ds;
	
	@DefaultHandler
	public Resolution defaultAction(){
		try{
			departments = hrBean.getAllScoreDepartment();
		}catch(Exception e){
			e.printStackTrace();
			departments = new ArrayList<Department>();
		}
		return new ForwardResolution("/score/departmentscore.jsp");
	}
	
	public Resolution addDepartment(){
		try{
			if(ds != null)
				scoreBean.addDepartmentScore(ds);
		}catch(Exception e){
			e.printStackTrace();
		}
		return defaultAction();
	}
	public List<Department> getDepartments() {
		return departments;
	}
	public void setDepartments(List<Department> departments) {
		this.departments = departments;
	}
	public DepartmentScore getDs() {
		return ds;
	}
	public void setDs(DepartmentScore ds) {
		this.ds = ds;
	}
	
}
