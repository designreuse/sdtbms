package com.bus.stripes.actionbean.score;

import java.util.ArrayList;
import java.util.List;

import security.action.Secure;
import sun.org.mozilla.javascript.internal.Context;

import com.bus.dto.Position;
import com.bus.dto.score.ScoreExceptionList;
import com.bus.services.CustomActionBean;
import com.bus.services.HRBean;
import com.bus.services.ScoreBean;
import com.bus.util.Roles;

import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.HandlesEvent;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.action.UrlBinding;
import net.sourceforge.stripes.integration.spring.SpringBean;

@UrlBinding(value="/actionbean/JoinScoreSystem.action")
public class JoinScoreSystemActionBean extends CustomActionBean{

	private HRBean hrBean;
	public HRBean getHrBean() {return hrBean;}
	@SpringBean
	public void setHrBean(HRBean bean) {this.hrBean = bean;}
	private ScoreBean scoreBean;
	public ScoreBean getScoreBean() {return this.scoreBean;}
	@SpringBean
	public void setScoreBean(ScoreBean bean) {this.scoreBean = bean;}

	private List<ScoreExceptionList> displayList; 
	private ScoreExceptionList exception;
	private List<Position> positions;
	private ScoreExceptionList editException;
	
	@DefaultHandler
	public Resolution defaultAction(){
		try{
			positions = hrBean.getActivePositions();
			displayList = scoreBean.getAllPositionExceptions();
		}catch(Exception e){
			positions  = new ArrayList<Position>();
		}
		return new ForwardResolution("/score/exceptionlist.jsp");
	}
	
	@HandlesEvent(value="createException")
	@Secure(roles = Roles.ADMINISTRATOR)
	public Resolution createException(){
		try{
			if(exception != null){
				scoreBean.addPositionException(exception, context.getUser());
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return defaultAction();
	}
	
	public List<ScoreExceptionList> getDisplayList() {
		return displayList;
	}
	public void setDisplayList(List<ScoreExceptionList> displayList) {
		this.displayList = displayList;
	}
	public ScoreExceptionList getException() {
		return exception;
	}
	public void setException(ScoreExceptionList exception) {
		this.exception = exception;
	}
	public List<Position> getPositions() {
		return positions;
	}
	public void setPositions(List<Position> positions) {
		this.positions = positions;
	}
	public ScoreExceptionList getEditException() {
		return editException;
	}
	public void setEditException(ScoreExceptionList editException) {
		this.editException = editException;
	}
}
