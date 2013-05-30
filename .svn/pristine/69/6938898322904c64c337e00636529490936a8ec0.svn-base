package com.bus.stripes.actionbean.score;

import java.util.ArrayList;
import java.util.List;

import security.action.Secure;

import com.bus.dto.Position;
import com.bus.dto.score.Scoregroup;
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

@UrlBinding(value="/actionbean/Rankgroup.action")
public class RankgroupActionBean extends CustomActionBean{

	private HRBean hrBean;
	public HRBean getHrBean(){return hrBean;}
	@SpringBean
	public void setHrBean(HRBean bean){this.hrBean = bean;}
	
	private ScoreBean scoreBean;
	public ScoreBean getScoreBean(){return this.scoreBean;}
	@SpringBean
	public void setScoreBean(ScoreBean bean){this.scoreBean = bean;}
	
	
	private Scoregroup group;
	private List<Scoregroup> groups;
	private Integer groupSelected;
	private List<Position> groupPositions;
	private List<Position> positions;
	private List<Integer> groupedPosSelected;
	private List<Integer> posSelected;
	private Scoregroup editGroup;
	
	private void loadOptionsList(){
		try{
			positions = hrBean.getAllPosition();
			groups = scoreBean.getAllScoreGroup();
		}catch(Exception e){
			e.printStackTrace();
			if(positions == null)
				positions = new ArrayList<Position>();
			if(groupPositions == null)
				groupPositions = new ArrayList<Position>();
			if(groups == null)
				groups = new ArrayList<Scoregroup>();
		}
		
	}
	
	@DefaultHandler
	@Secure(roles=Roles.SCORE_RANK_GROUP_VIEW)
	public Resolution defaultAction(){
		loadOptionsList();
		return new ForwardResolution("/score/rankgroup.jsp");
	}
	
	@HandlesEvent(value="createGroup")
	@Secure(roles=Roles.SCORE_RANK_GROUP_EDIT)
	public Resolution createGroup(){
		try{
			if(group != null)
				scoreBean.saveGroup(context.getUser(),group);
		}catch(Exception e){
			System.out.println(e.getMessage());
		}
		return defaultAction();
	}
	
	@HandlesEvent(value="editGroupDetail")
	@Secure(roles = Roles.SCORE_RANK_GROUP_EDIT)
	public Resolution editGroupDetail(){
		try{
			if(groupSelected != null && editGroup != null)
				scoreBean.editGroup(context.getUser(),groupSelected,editGroup);
		}catch(Exception e){
			System.out.println(e.getMessage());
		}
		return filterGroup();
	}
	
	@HandlesEvent(value="filterGroup")
	public Resolution filterGroup(){
		try{
			if(groupSelected != null){
				groupPositions = scoreBean.getGroupedPositions(groupSelected);
			}
		}catch(Exception e){
			System.out.println(e.getMessage());
		}
		return defaultAction();
	}
	
	@HandlesEvent(value="deleteGroup")
	@Secure(roles = Roles.SCORE_RANK_GROUP_EDIT)
	public Resolution deleteGroup(){
		try{
			if(groupSelected != null){
				scoreBean.removeScoreGroup(context.getUser(),groupSelected);
			}
		}catch(Exception e){
			System.out.println(e.getMessage());
		}
		return defaultAction();
	}
	
	@HandlesEvent(value="joinGroup")
	@Secure(roles=Roles.SCORE_RANK_GROUP_EDIT)
	public Resolution joinGroup(){
		try{
			if(groupSelected != null){
				for(Integer i:posSelected){
					if(i != null){
						if(!scoreBean.isGroupedPositionExist(i, groupSelected)){
							scoreBean.assignPosToGroup(context.getUser(),i, groupSelected);
						}
					}
				}
			}
		}catch(Exception e){
			System.out.println(e.getMessage());
		}
		return filterGroup();
	}
	
	@HandlesEvent(value="quitGroup")
	@Secure(roles=Roles.SCORE_RANK_GROUP_EDIT)
	public Resolution quitGroup(){
		try{
			if(groupSelected != null){
				for(Integer i:groupedPosSelected){
					if(i != null){
						if(scoreBean.isGroupedPositionExist(i, groupSelected)){
							scoreBean.quitPosFromGroup(context.getUser(),i, groupSelected);
						}
					}
				}
			}
		}catch(Exception e){
			System.out.println(e.getMessage());
		}
		return filterGroup();
	}

	public Scoregroup getGroup() {
		return group;
	}

	public void setGroup(Scoregroup group) {
		this.group = group;
	}

	public Integer getGroupSelected() {
		return groupSelected;
	}

	public void setGroupSelected(Integer groupSelected) {
		this.groupSelected = groupSelected;
	}

	public List<Position> getGroupPositions() {
		return groupPositions;
	}

	public void setGroupPositions(List<Position> groupPositions) {
		this.groupPositions = groupPositions;
	}

	public List<Position> getPositions() {
		return positions;
	}

	public void setPositions(List<Position> positions) {
		this.positions = positions;
	}

	public List<Integer> getGroupedPosSelected() {
		return groupedPosSelected;
	}

	public void setGroupedPosSelected(List<Integer> groupedPosSelected) {
		this.groupedPosSelected = groupedPosSelected;
	}

	public List<Integer> getPosSelected() {
		return posSelected;
	}

	public void setPosSelected(List<Integer> posSelected) {
		this.posSelected = posSelected;
	}

	public Scoregroup getEditGroup() {
		return editGroup;
	}

	public void setEditGroup(Scoregroup editGroup) {
		this.editGroup = editGroup;
	}
	public List<Scoregroup> getGroups() {
		return groups;
	}
	public void setGroups(List<Scoregroup> groups) {
		this.groups = groups;
	}
}
