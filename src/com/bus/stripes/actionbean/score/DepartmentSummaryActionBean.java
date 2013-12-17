package com.bus.stripes.actionbean.score;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.action.UrlBinding;
import net.sourceforge.stripes.integration.spring.SpringBean;

import com.bus.dto.Department;
import com.bus.dto.score.ScoreDivGroup;
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
	private Float departmentWeekReject = 0F;
	private Float departmentWeekWaitting= 0F;
	private Float departmentWeekTotal = 0F;
	private Float departmentWeekNotSubmit = 0F;
	private Integer departmentWeekEmployee = 0;
	
	private List<Scorerecord> records;
	private List<Scoresummary> summary;
	
	private List<ScoreDivGroup> scoreGroups;
	private Integer selectDepartment;
	private String departmentName;
	private Integer month;
	private Date weekdate;
	private Integer weekNumber;
	
	@DefaultHandler
	public Resolution defaultAction(){
		try{
			scoreGroups = scoreBean.getAllManageDeparmentsByUser(context.getUser());
			if(month == null){
				month = Calendar.getInstance().get(Calendar.MONTH)+1;
			}
			Calendar cal = Calendar.getInstance();
			Calendar cal2 = Calendar.getInstance();
			
			//周总得分
			if(weekdate == null){
				cal.set(Calendar.DAY_OF_WEEK,cal.getFirstDayOfWeek());
				cal2.setTime(cal.getTime());
				cal2.add(Calendar.WEEK_OF_YEAR, 1);
				weekNumber = cal.get(Calendar.WEEK_OF_YEAR);
			}else{
				cal.setTime(weekdate);
				cal.set(Calendar.DAY_OF_WEEK, cal.getFirstDayOfWeek());
				cal2.setTime(cal.getTime());
				cal2.add(Calendar.WEEK_OF_YEAR, 1);
				weekNumber = cal.get(Calendar.WEEK_OF_YEAR);
			}
			
			
			
			ScoreDivGroup sg;
			if(selectDepartment == null)
				sg = scoreBean.getScoreDivGroupByWorkerId(context.getUser().getEmployee());
			else
				sg = scoreBean.getScoreDivGroupById(selectDepartment);
			departmentWeekRemain = sg.getAvailable();
			departmentName = sg.getName();
			records = scoreBean.getApprovedListByTimeForDepartment(sg.getId(), cal.getTime(), cal2.getTime());
			
			//获取部门待审核分值和被拒绝的分值,设置部门总分显示,总人数
			departmentWeekEmployee = scoreBean.getScoreMemberCount(sg.getId());
			departmentWeekTotal = sg.getBasescore() * (float)departmentWeekEmployee;
			departmentWeekWaitting = scoreBean.getDepartmentWaittingScores(sg.getId(),cal.getTime(),cal2.getTime());
			departmentWeekReject  = scoreBean.getDepartmentRejectedScores(sg.getId(),cal.getTime(),cal2.getTime());
			departmentWeekNotSubmit = scoreBean.getDepartmentNotSubmitScores(sg.getId(), cal.getTime(), cal2.getTime());
			
			Map<String,Scoresummary> summaryMap = new HashMap<String,Scoresummary>();
			Map<String,Float> scoreMap = new HashMap<String,Float>();
			for(Scorerecord sr :records){
				if(sr.getScoretype().getType() == Scoretype.SCORE_TYPE_TEMP){
					if(!scoreBean.isScorerUnlimited(sr.getReceiver().getEmployee()))
						departmentMonthScore += sr.getScore();
				}
				Scoresummary temSum = null;
				Float tempScore = null;
				if(summaryMap.get(sr.getReceiver().getEmployee().getWorkerid()) == null){
					temSum = new Scoresummary();
					temSum.setEmployee(sr.getReceiver().getEmployee());
					temSum.setFixscore(new Float(0));
					temSum.setPerformancescore(new Float(0));
					temSum.setProjectscore(new Float(0));
					temSum.setScore(new Float(0));
				}else{
					temSum = summaryMap.get(sr.getReceiver().getEmployee().getWorkerid());
				}
				if(scoreMap.get(sr.getReceiver().getEmployee().getWorkerid()) == null){
					tempScore = new Float(0);
				}else{
					tempScore = scoreMap.get(sr.getReceiver().getEmployee().getWorkerid());
				}
				if(sr.getScoretype().getType() == Scoretype.SCORE_TYPE_FIX){
					temSum.setFixscore(temSum.getFixscore() + sr.getScore());
				}else if(sr.getScoretype().getType() == Scoretype.SCORE_TYPE_PERFORMENCE){
					temSum.setPerformancescore(temSum.getPerformancescore() + sr.getScore());
				}else if(sr.getScoretype().getType() == Scoretype.SCORE_TYPE_PROJECT){
					temSum.setProjectscore(temSum.getProjectscore() + sr.getScore());
				}else if(sr.getScoretype().getType() == Scoretype.SCORE_TYPE_TEMP){
					temSum.setScore(temSum.getScore() + sr.getScore());
				}
				tempScore  = temSum.getScore();
				summaryMap.put(sr.getReceiver().getEmployee().getWorkerid(), temSum);
				scoreMap.put(sr.getReceiver().getEmployee().getWorkerid(), tempScore);
			}
			scoreMap = HRUtil.sortByComparator(scoreMap);
			summary  = new ArrayList<Scoresummary>();
			for (Map.Entry entry : scoreMap.entrySet()) {
				summary.add(summaryMap.get(entry.getKey()));
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
//	public List<Department> getDepartments() {
//		return departments;
//	}
//	public void setDepartments(List<Department> departments) {
//		this.departments = departments;
//	}
	public Integer getSelectDepartment() {
		return selectDepartment;
	}
	public void setSelectDepartment(Integer selectDepartment) {
		this.selectDepartment = selectDepartment;
	}
	public Date getWeekdate() {
		return weekdate;
	}
	public void setWeekdate(Date weekdate) {
		this.weekdate = weekdate;
	}
	public Integer getWeekNumber() {
		return weekNumber;
	}
	public void setWeekNumber(Integer weekNumber) {
		this.weekNumber = weekNumber;
	}
	public Float getDepartmentWeekReject() {
		return departmentWeekReject;
	}
	public void setDepartmentWeekReject(Float departmentWeekReject) {
		this.departmentWeekReject = departmentWeekReject;
	}
	public Float getDepartmentWeekWaitting() {
		return departmentWeekWaitting;
	}
	public void setDepartmentWeekWaitting(Float departmentWeekWaitting) {
		this.departmentWeekWaitting = departmentWeekWaitting;
	}
	public Float getDepartmentWeekTotal() {
		return departmentWeekTotal;
	}
	public void setDepartmentWeekTotal(Float departmentWeekTotal) {
		this.departmentWeekTotal = departmentWeekTotal;
	}
	public Integer getDepartmentWeekEmployee() {
		return departmentWeekEmployee;
	}
	public void setDepartmentWeekEmployee(Integer departmentWeekEmployee) {
		this.departmentWeekEmployee = departmentWeekEmployee;
	}
	public Float getDepartmentWeekNotSubmit() {
		return departmentWeekNotSubmit;
	}
	public void setDepartmentWeekNotSubmit(Float departmentWeekNotSubmit) {
		this.departmentWeekNotSubmit = departmentWeekNotSubmit;
	}
	public List<ScoreDivGroup> getScoreGroups() {
		return scoreGroups;
	}
	public void setScoreGroups(List<ScoreDivGroup> scoreGroups) {
		this.scoreGroups = scoreGroups;
	}	
	
	
}
