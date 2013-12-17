package com.bus.stripes.actionbean.score;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.quartz.CronScheduleBuilder;
import org.quartz.CronTrigger;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerFactory;
import org.quartz.SimpleTrigger;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.impl.StdSchedulerFactory;

import security.action.Secure;

import com.bus.dto.Account;
import com.bus.dto.Accountgroup;
import com.bus.dto.Actiongroup;
import com.bus.dto.logger.ScoreLog;
import com.bus.dto.score.DepartmentScore;
import com.bus.dto.score.ScoreDivGroup;
import com.bus.scheduler.HelloJob;
import com.bus.scheduler.job.CheckScoreGroupMemberJob;
import com.bus.services.AccountBean;
import com.bus.services.CustomActionBean;
import com.bus.services.HRBean;
import com.bus.services.ScoreBean;
import com.bus.stripes.actionbean.MyActionBeanContext;
import com.bus.stripes.actionbean.Permission;
import com.bus.util.Roles;

import net.sourceforge.stripes.action.ActionBean;
import net.sourceforge.stripes.action.ActionBeanContext;
import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.HandlesEvent;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.action.UrlBinding;
import net.sourceforge.stripes.integration.spring.SpringBean;


@UrlBinding("/actionbean/Scorehome.action")
@Secure(roles = Roles.SCORE_SYSTEM)
public class ScorehomeActionBean extends CustomActionBean{

	private ScoreBean scoreBean;
	public ScoreBean getScoreBean(){return this.scoreBean;}
	@SpringBean
	public void setScoreBean(ScoreBean bean){this.scoreBean = bean;}
	
	
	private List<ScoreLog> logs; 
	private Date logdate;
	
	private List<ScoreDivGroup> scoreGroups;
	private List<ScoreDivGroup> saveScoreGroups;
	
//	
//	private List<DepartmentScore> departScores;
//	private List<DepartmentScore> depS;
	
	private Float addScore;
	
	private void loadLogs(){
		try{
			if(logdate == null)
				logdate = new Date();
			logs = scoreBean.getScoreLogs(logdate);
		}catch(Exception e){
			e.printStackTrace();
			logs = new ArrayList<ScoreLog>();
		}
	}
	
	@DefaultHandler
	@Secure(roles=Roles.SCORE_HOME_VIEW)
	public Resolution defaultAction(){
		loadLogs();
		return new ForwardResolution("/score/home.jsp");
	}

	@HandlesEvent(value="resetScores")
	@Secure(roles=Roles.ADMINISTRATOR)
	public Resolution resetScores(){
		try{
			scoreGroups = scoreBean.getAllScoreGroups();
			for(ScoreDivGroup sdg:scoreGroups){
				int count = scoreBean.getScoreMemberCount(sdg.getId());
				if(sdg.getBasescore() == null)
					sdg.setAvailable(0F);
				if(sdg.getBasescore() == null)
					sdg.setBasescore(25F);
				sdg.setAvailable(sdg.getBasescore() * count);
			}
			return new ForwardResolution("/score/departmentscoretable.jsp");
		}catch(Exception e){
			e.printStackTrace();
			return defaultAction();
		}
	}
	
	@HandlesEvent(value="addScoresToDepartment")
	@Secure(roles=Roles.ADMINISTRATOR)
	public Resolution addScoresToDepartment(){
		try{
			if(addScore == null)
				return defaultAction();
			scoreGroups = scoreBean.getAllScoreGroups();
			for(ScoreDivGroup s:scoreGroups){
				int count = scoreBean.getScoreMemberCount(s.getId());
				s.setAvailable(s.getAvailable() + addScore*count);
			}
			return new ForwardResolution("/score/departmentscoretable.jsp");
		}catch(Exception e){
			e.printStackTrace();
			return defaultAction();
		}
	}
	
	@HandlesEvent(value="setBaseScore")
	@Secure(roles=Roles.ADMINISTRATOR)
	public Resolution setBaseScore(){
		try{
			if(addScore == null)
				return defaultAction();
			scoreGroups = scoreBean.getAllScoreGroups();
			for(ScoreDivGroup s:scoreGroups){
				s.setBasescore(addScore);
			}
			scoreBean.updateScoreGroups(scoreGroups);
			return defaultAction();
		}catch(Exception e){
			e.printStackTrace();
			return defaultAction();
		}
	}
	
	@HandlesEvent(value="submitDepScore")
	@Secure(roles=Roles.ADMINISTRATOR)
	public Resolution submitDepScore(){
		try{
			scoreBean.updateScoreGroups(saveScoreGroups);
		}catch(Exception e){
			e.printStackTrace();
			return context.errorResolution("更新部门分失败", e.getMessage());
		}
		return defaultAction();
	}
	
	@HandlesEvent(value="filter")
	public Resolution filter(){
		return defaultAction();
	}
	
	public List<ScoreLog> getLogs() {
		return logs;
	}
	public void setLogs(List<ScoreLog> logs) {
		this.logs = logs;
	}
	public Date getLogdate() {
		return logdate;
	}
	public void setLogdate(Date logdate) {
		this.logdate = logdate;
	}
//	public List<DepartmentScore> getDepartScores() {
//		return departScores;
//	}
//	public void setDepartScores(List<DepartmentScore> departScores) {
//		this.departScores = departScores;
//	}
//	public List<DepartmentScore> getDepS() {
//		return depS;
//	}
//	public void setDepS(List<DepartmentScore> depS) {
//		this.depS = depS;
//	}
	public Float getAddScore() {
		return addScore;
	}
	public void setAddScore(Float addScore) {
		this.addScore = addScore;
	}
	public List<ScoreDivGroup> getScoreGroups() {
		return scoreGroups;
	}
	public void setScoreGroups(List<ScoreDivGroup> scoreGroups) {
		this.scoreGroups = scoreGroups;
	}
	public List<ScoreDivGroup> getSaveScoreGroups() {
		return saveScoreGroups;
	}
	public void setSaveScoreGroups(List<ScoreDivGroup> saveScoreGroups) {
		this.saveScoreGroups = saveScoreGroups;
	}
	
	
}
