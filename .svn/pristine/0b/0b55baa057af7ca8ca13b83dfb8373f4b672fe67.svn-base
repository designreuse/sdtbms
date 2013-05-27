package com.bus.stripes.actionbean.score;

import java.util.ArrayList;
import java.util.List;

import security.action.Secure;
import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.HandlesEvent;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.action.StreamingResolution;
import net.sourceforge.stripes.action.UrlBinding;
import net.sourceforge.stripes.integration.spring.SpringBean;

import com.bus.dto.score.Scoresheets;
import com.bus.dto.score.Scoretype;
import com.bus.services.CustomActionBean;
import com.bus.services.ScoreBean;
import com.bus.util.Roles;

@UrlBinding(value = "/actionbean/Scoresheet.action")
public class ScoresheetActionBean extends CustomActionBean {

	private ScoreBean scoreBean;
	public ScoreBean getScoreBean(){return this.scoreBean;}
	@SpringBean
	public void setScoreBean(ScoreBean bean){this.scoreBean = bean;}
	
	private Scoresheets sheet;
	private List<Scoretype> scoretypes;
	private List<Scoresheets> sheetList;
	private Integer selectedSheet;
	
	private List<Scoretype> selectedScoreTypes;
	
	private void loadList(){
		try{
			sheetList = scoreBean.getAllScoreSheets();
		}catch(Exception e){
			sheetList = new ArrayList<Scoresheets>();
		}
		if(scoretypes == null)
			scoretypes = new ArrayList<Scoretype>();
	}
	
	@DefaultHandler
	@Secure(roles = Roles.SCORE_SHEET_VIEW)
	public Resolution defaultAction(){
		loadList();
		return new ForwardResolution("/score/itemsheet.jsp");
	}

	@HandlesEvent(value="selectSheet")
	@Secure(roles = Roles.SCORE_SHEET_VIEW)
	public Resolution selectSheet(){
		try{
			if(selectedSheet != null){
				scoretypes = scoreBean.getScoretypesFromSheet(selectedSheet.toString());
			}
			return defaultAction();
		}catch(Exception e){
			return defaultAction();
		}
	}
	
	@HandlesEvent(value="createSheet")
	@Secure(roles = Roles.SCORE_SHEET_ADD)
	public Resolution createSheet(){
		try{
			scoreBean.createScoreSheet(context.getUser(),sheet);
			return new StreamingResolution("text/html;charset=utf-8;","创建积分表单成功");
		}catch(Exception e){
			return context.errorResolution("创建失败", "创建分表失败，错误:"+e.getMessage());
		}
	}
	
	@HandlesEvent(value="removeSheet")
	@Secure(roles = Roles.SCORE_SHEET_RM)
	public Resolution removeSheet(){
		try{
			if(selectedSheet != null)
				scoreBean.removeScoreSheet(context.getUser(),selectedSheet);
			return defaultAction();
		}catch(Exception e){
			return context.errorResolution("删除失败", "删除积分表失败，检查表单是否已经加入了条例。如果已经添加了条例，清先将条例删除。错误:"+e.getMessage());
		}
	}
	
	@HandlesEvent(value="removeSelectedScoreTypes")
	@Secure(roles=Roles.Score_SHEET_RM_ST)
	public Resolution removeSelectedScoreTypes(){
		try{
			String targetId = context.getRequest().getParameter("targetId");
			if(targetId == null || selectedScoreTypes == null)
				return defaultAction();
			for(Scoretype st:selectedScoreTypes){
				if(st != null)
					scoreBean.removeScoreTypeFromSheet(context.getUser(),Integer.parseInt(targetId),st.getId());
			}
		}catch(Exception e){
			System.out.println(e.getMessage());
		}
		return selectSheet();
	}
	
	public Scoresheets getSheet() {
		return sheet;
	}

	public void setSheet(Scoresheets sheet) {
		this.sheet = sheet;
	}

	public List<Scoresheets> getSheetList() {
		return sheetList;
	}

	public void setSheetList(List<Scoresheets> sheetList) {
		this.sheetList = sheetList;
	}
	public List<Scoretype> getSelectedScoreTypes() {
		return selectedScoreTypes;
	}
	public void setSelectedScoreTypes(List<Scoretype> selectedScoreTypes) {
		this.selectedScoreTypes = selectedScoreTypes;
	}
	public Integer getSelectedSheet() {
		return selectedSheet;
	}
	public void setSelectedSheet(Integer selectedSheet) {
		this.selectedSheet = selectedSheet;
	}
	public List<Scoretype> getScoretypes() {
		return scoretypes;
	}
	public void setScoretypes(List<Scoretype> scoretypes) {
		this.scoretypes = scoretypes;
	}
	
}
