package com.bus.stripes.actionbean.score;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.bus.dto.Account;
import com.bus.dto.Accountgroup;
import com.bus.dto.Actiongroup;
import com.bus.dto.logger.ScoreLog;
import com.bus.services.AccountBean;
import com.bus.services.CustomActionBean;
import com.bus.services.HRBean;
import com.bus.services.ScoreBean;
import com.bus.stripes.actionbean.MyActionBeanContext;
import com.bus.stripes.actionbean.Permission;

import net.sourceforge.stripes.action.ActionBean;
import net.sourceforge.stripes.action.ActionBeanContext;
import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.HandlesEvent;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.action.UrlBinding;
import net.sourceforge.stripes.integration.spring.SpringBean;

@UrlBinding("/actionbean/Scorehome.action")
public class ScorehomeActionBean extends CustomActionBean implements Permission{

	private ScoreBean scoreBean;
	public ScoreBean getScoreBean(){return this.scoreBean;}
	@SpringBean
	public void setScoreBean(ScoreBean bean){this.scoreBean = bean;}
	
	private List<ScoreLog> logs; 
	private Date logdate;
	
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
	public Resolution defaultAction(){
		if(!getPermission(context.getUser(),"scorehome_view")){
			return context.errorResolution("权限错误","你没有权限进行该操作,请联系管理员");
		}
		loadLogs();
		return new ForwardResolution("/score/home.jsp");
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
	
	
}
