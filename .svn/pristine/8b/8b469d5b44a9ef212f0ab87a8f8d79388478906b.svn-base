package com.bus.stripes.actionbean.score;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.action.UrlBinding;
import net.sourceforge.stripes.integration.spring.SpringBean;

import com.bus.dto.score.DepartmentScore;
import com.bus.dto.score.Scorerecord;
import com.bus.dto.score.Scoresummary;
import com.bus.dto.score.Scoretype;
import com.bus.services.CustomActionBean;
import com.bus.services.HRBean;
import com.bus.services.ScoreBean;
import com.bus.util.HRUtil;

@UrlBinding("/actionbean/DepartSum.action")
public class DepartmentSummaryActionBean extends CustomActionBean{

	private HRBean hrBean;
	public HRBean getHrBean() {return hrBean;}
	@SpringBean
	public void setHrBean(HRBean bean) {this.hrBean = bean;}
	private ScoreBean scoreBean;
	public ScoreBean getScoreBean() {return this.scoreBean;}
	@SpringBean
	public void setScoreBean(ScoreBean bean) {this.scoreBean = bean;}
	
	private Float departmentWeekRemain =0F;
	private Float departmentMonthScore=0F;
	private List<Scorerecord> records;
	private List<Scoresummary> summary;
	private String departmentName;
	private Integer month;
	
	@DefaultHandler
	public Resolution defaultAction(){
		try{
			if(month == null){
				month = Calendar.getInstance().get(Calendar.MONTH)+1;
//				System.out.println(month);
			}
			Calendar cal = Calendar.getInstance();
			Calendar cal2 = Calendar.getInstance();
			cal.set(Calendar.MONTH, month-1);
			cal.set(Calendar.DAY_OF_MONTH, 1);
			
			cal2.set(Calendar.MONTH, month-1);
			cal2.set(Calendar.DAY_OF_MONTH, 1);
			cal2.add(Calendar.MONTH, 1);
			
//			System.out.println(HRUtil.parseDateToString(cal.getTime()));
//			System.out.println(HRUtil.parseDateToString(cal2.getTime()));
			
			DepartmentScore  ds = scoreBean.getDepartmentScoresByDepartmetnIdORWorkerId(null,context.getUser().getEmployee());
			departmentWeekRemain = ds.getAvailable();
			departmentName = ds.getDepartment().getName();
			summary = scoreBean.getDepartmentScoreSummary(context.getUser(),month,ds.getDepartment().getId());
			records = scoreBean.getApprovedListByTimeForDepartment(ds.getDepartment().getId(), cal.getTime(), cal2.getTime());
			for(Scorerecord sr :records){
				if(sr.getScoretype().getType() == Scoretype.SCORE_TYPE_TEMP)
					departmentMonthScore += sr.getScore();
			}
		}catch(Exception e){
			e.printStackTrace();
			summary  = new ArrayList<Scoresummary>();
			records = new ArrayList<Scorerecord>();
		}
		return new ForwardResolution("/score/departsum.jsp");
	}
	
	public void setDepartmentWeekRemain(Float departmentWeekRemain) {
		this.departmentWeekRemain = departmentWeekRemain;
	}
	public Float getDepartmentMonthScore() {
		return departmentMonthScore;
	}
	public void setDepartmentMonthScore(Float departmentMonthScore) {
		this.departmentMonthScore = departmentMonthScore;
	}
	public List<Scorerecord> getRecords() {
		return records;
	}
	public void setRecords(List<Scorerecord> records) {
		this.records = records;
	}
	public List<Scoresummary> getSummary() {
		return summary;
	}
	public void setSummary(List<Scoresummary> summary) {
		this.summary = summary;
	}
	public String getDepartmentName() {
		return departmentName;
	}
	public void setDepartmentName(String departmentName) {
		this.departmentName = departmentName;
	}
	public Integer getMonth() {
		return month;
	}
	public void setMonth(Integer month) {
		this.month = month;
	}
	public Float getDepartmentWeekRemain() {
		return departmentWeekRemain;
	}	
}
