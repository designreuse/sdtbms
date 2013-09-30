package com.bus.stripes.actionbean.score;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import security.action.Secure;
import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.HandlesEvent;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.action.StreamingResolution;
import net.sourceforge.stripes.action.UrlBinding;
import net.sourceforge.stripes.integration.spring.SpringBean;

import com.bus.dto.score.Scorerecord;
import com.bus.services.CustomActionBean;
import com.bus.services.ScoreBean;
import com.bus.util.HRUtil;
import com.bus.util.Roles;
import com.google.gson.JsonObject;

@UrlBinding("/actionbean/ScoreApprove.action")
public class ScoreApproveActionBean extends CustomActionBean{
	
	private ScoreBean scoreBean;
	public ScoreBean getScoreBean() {return this.scoreBean;}
	@SpringBean
	public void setScoreBean(ScoreBean bean) {this.scoreBean = bean;}
	
	private List<Scorerecord> displayList;
	private List<String> selected;
	private String selectPeriod;
	private Integer mode;
	private Long countWaitting;
	private Long countReject;
	private Long countCreated;
	
	private Date startDate;
	private Date endDate;
	
	@DefaultHandler
	@Secure(roles=Roles.SCORE_APPROVE_ITEMS)
	public Resolution defaultAction(){
		try{
			if(selectPeriod == null){
				selectPeriod = context.getRequest().getParameter("selectPeriod");
			}
			countWaitting = scoreBean.countWaittingApprove(context.getUser());
			if(mode == null){
				displayList = scoreBean.getWaittingApprove(context.getUser(),selectPeriod);
			}else if(mode < 0){
				displayList = scoreBean.getApprovedListByTime(context.getUser(), selectPeriod, startDate, endDate);
			}
		}catch(Exception e){
			e.printStackTrace();
			countWaitting = 0L;
			displayList = new ArrayList<Scorerecord>();
		}
		return new ForwardResolution("/score/approve.jsp");
	}
	
	@Secure(roles=Roles.SCORE_APPROVE_ITEMS)
	@HandlesEvent(value="approveAction")
	public Resolution approveAction(){
		JsonObject json = new JsonObject();
		try{
			if(selected == null){
				json.addProperty("result", "0");
				json.addProperty("msg", "未选上任何项目");
			}else{
				scoreBean.approveScoreRecords(selected, context.getUser());
				json.addProperty("result", "1");
				json.addProperty("msg", "审核了"+selected.size()+"条项目");
			}
		}catch(Exception e){
			e.printStackTrace();
			json.addProperty("result", "0");
			json.addProperty("msg", "审核出错。错误"+ HRUtil.parseDateToString(new Date()) +":"+e.getMessage());
		}
		return new StreamingResolution("text/charset=uft-8",json.toString());
	}

	@Secure(roles=Roles.SCORE_APPROVE_ITEMS)
	@HandlesEvent(value="rejectAction")
	public Resolution rejectAction(){
		JsonObject json = new JsonObject();
		try{
			if(selected == null){
				json.addProperty("result", "0");
				json.addProperty("msg", "未选上任何项目");
			}else{
				scoreBean.rejectScoreRecords(selected, context.getUser());
				json.addProperty("result", "1");
				json.addProperty("msg", "审核了"+selected.size()+"条项目");
			}
		}catch(Exception e){
			e.printStackTrace();
			json.addProperty("result", "0");
			json.addProperty("msg", "审核出错。错误"+ HRUtil.parseDateToString(new Date()) +":"+e.getMessage());
		}
		return new StreamingResolution("text/charset=uft-8",json.toString());
	}
	
	@Secure(roles = Roles.SCORE_APPROVE_SUBMIT_ITEMS)
	@HandlesEvent(value="preapprove")
	public Resolution preapprove(){
		try{
			countWaitting = scoreBean.countWaitingApproveRecords(context.getUser());
			countReject = scoreBean.countRejectedRecords(context.getUser());
			countCreated = scoreBean.countCreatedRecords(context.getUser());
			if(mode == null){//等待审核页面
				displayList = scoreBean.getWaitingApproveRecords(context.getUser());
			}else if(mode.intValue() == Scorerecord.REJECTED){//拒绝的页面
				displayList = scoreBean.getRejectedRecords(context.getUser());
			}else if(mode.intValue() == Scorerecord.CREATED){
				displayList = scoreBean.getCreatedRecords(context.getUser());
			}else if(mode.intValue() < 0){
				displayList = scoreBean.getApprovedListByTime(context.getUser(), selectPeriod, startDate,endDate);
			}
		}catch(Exception e){
			e.printStackTrace();
			if(countWaitting == null) countWaitting=0L;
			if(countReject == null) countReject=0L;
			if(countCreated == null) countCreated = 0L;
			displayList = new ArrayList<Scorerecord>();
		}
		return new ForwardResolution("/score/preapprove.jsp");
	}
	
	@Secure(roles = Roles.SCORE_APPROVE_SUBMIT_ITEMS)
	@HandlesEvent(value="deleteAction")
	public Resolution deleteAction(){
		JsonObject json = new JsonObject();
		try{
			if(selected == null){
				json.addProperty("result", "0");
				json.addProperty("msg", "未选上任何项目");
			}else{
				scoreBean.deleteScoreRecords(selected, context.getUser());
				json.addProperty("result", "1");
				json.addProperty("msg", "删除了"+selected.size()+"条项目");
			}
		}catch(Exception e){
			e.printStackTrace();
			json.addProperty("result", "0");
			json.addProperty("msg", "删除出错。错误"+ HRUtil.parseDateToString(new Date()) +":"+e.getMessage());
		}
		return new StreamingResolution("text/charset=uft-8",json.toString());
	}
	
	@Secure(roles=Roles.SCORE_DETAIL_REMOVE_RECORD)
	@HandlesEvent(value="deleteApprovedAction")
	public Resolution deleteApprovedAction(){
		JsonObject json = new JsonObject();
		try{
			if(selected == null){
				json.addProperty("result", "0");
				json.addProperty("msg", "未选上任何项目");
			}else{
				scoreBean.deleteScoreRecords(selected, context.getUser());
				json.addProperty("result", "1");
				json.addProperty("msg", "删除了"+selected.size()+"条项目");
			}
		}catch(Exception e){
			e.printStackTrace();
			json.addProperty("result", "0");
			json.addProperty("msg", "删除出错。错误"+ HRUtil.parseDateToString(new Date()) +":"+e.getMessage());
		}
		return new StreamingResolution("text/charset=uft-8",json.toString());
	}
	
	@Secure(roles = Roles.SCORE_APPROVE_SUBMIT_ITEMS)
	@HandlesEvent(value="getbackAction")
	public Resolution getbackAction(){
		JsonObject json = new JsonObject();
		try{
			if(selected == null){
				json.addProperty("result", "0");
				json.addProperty("msg", "未选上任何项目");
			}else{
				scoreBean.getBackScoreRecords(selected, context.getUser());
				json.addProperty("result", "1");
				json.addProperty("msg", "下放了"+selected.size()+"条项目");
			}
		}catch(Exception e){
			e.printStackTrace();
			json.addProperty("result", "0");
			json.addProperty("msg", "审核出错。错误"+ HRUtil.parseDateToString(new Date()) +":"+e.getMessage());
		}
		return new StreamingResolution("text/charset=uft-8",json.toString());
	}
	
	@Secure(roles = Roles.SCORE_APPROVE_SUBMIT_ITEMS)
	@HandlesEvent(value="resubmitAction")
	public Resolution resubmitAction(){
		JsonObject json = new JsonObject();
		try{
			if(selected == null){
				json.addProperty("result", "0");
				json.addProperty("msg", "未选上任何项目");
			}else{
				scoreBean.reSubmitScoreRecords(selected, context.getUser());
				json.addProperty("result", "1");
				json.addProperty("msg", "提交了"+selected.size()+"条项目");
			}
		}catch(Exception e){
			e.printStackTrace();
			json.addProperty("result", "0");
			json.addProperty("msg", "审核出错。错误"+ HRUtil.parseDateToString(new Date()) +":"+e.getMessage());
		}
		return new StreamingResolution("text/charset=uft-8",json.toString());
	}
	
	
	public String getSelectPeriod() {
		return selectPeriod;
	}

	public void setSelectPeriod(String selectPeriod) {
		this.selectPeriod = selectPeriod;
	}

	public Long getCountWaitting() {
		return countWaitting;
	}

	public void setCountWaitting(Long countWaitting) {
		this.countWaitting = countWaitting;
	}
	public List<Scorerecord> getDisplayList() {
		return displayList;
	}
	public void setDisplayList(List<Scorerecord> displayList) {
		this.displayList = displayList;
	}
	public List<String> getSelected() {
		return selected;
	}
	public void setSelected(List<String> selected) {
		this.selected = selected;
	}
	public Long getCountReject() {
		return countReject;
	}
	public void setCountReject(Long countReject) {
		this.countReject = countReject;
	}
	public Integer getMode() {
		return mode;
	}
	public void setMode(Integer mode) {
		this.mode = mode;
	}
	public Long getCountCreated() {
		return countCreated;
	}
	public void setCountCreated(Long countCreated) {
		this.countCreated = countCreated;
	}
	public Date getStartDate() {
		return startDate;
	}
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}
	public Date getEndDate() {
		return endDate;
	}
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}
	
}
