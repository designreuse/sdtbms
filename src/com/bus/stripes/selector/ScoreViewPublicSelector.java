package com.bus.stripes.selector;

import java.util.Calendar;
import java.util.Date;

import com.bus.util.HRUtil;

public class ScoreViewPublicSelector implements BMSSelector{
	
	private final static int TYPE_BY_HISTORY = 0;
	private final static int TYPE_BY_YEAR = 1;
	private final static int TYPE_BY_MONTH= 2;
	
	private final static int TYPE_BY_FIX_SCORE = 1;
	private final static int TYPE_BY_TEMP_SCORE = 0;
	private final static int TYPE_BY_TOTAL_SCORE = 3;
	private final static int TYPE_BY_PERFORMANCESCORE =2;
	
	public static final int RANK_ZHU_REN_JI = 0;
	public static final int RANK_GUAN_LI_YUAN = 1;
	public static final int RANK_WEI_XIU_GONG = 2;
	public static final int RANK_FU_WU_YUAN = 3;
	public static final int RANK_JIA_SHI_YUAN = 4;
	
	private Date recordDate;
	
	private Date recordStartDate;
	private Date recordEndDate;
	private Integer selecttype;
	private Integer scoretype;
	private Integer order;
	private Integer rankGroup;
	
	private Integer selectedGroup;
	
	private Integer department;
	private String position;
	
	@Override
	public String getSelectorStatement() {
		if(recordDate == null){
			recordDate = new Date();
		}
		Calendar c = Calendar.getInstance();
		c.setTime(recordDate);
		String scoretypeselection = getScoreTypeQuery();
		String query = "SELECT scoresummary.workerid AS workercode,SUM(fixscore) AS fixscore, SUM(score+projectscore) AS tempscore, SUM(performancescore) AS performancescore, SUM(fixscore+score+performancescore+projectscore) AS totalscore, RANK() OVER ("+scoretypeselection+") AS rank, COUNT(scoresummary.workerid) AS count," +
				" employee.fullname AS name, employee.firstworktime AS firstworktime," +
				" position.name AS positionname " +
				" FROM scoresummary JOIN employee ON scoresummary.workerid=employee.workerid" +
				" JOIN position ON employee.positionid = position.id" +
				" WHERE EXTRACT(month FROM date)="+(c.get(Calendar.MONTH)+1) +
						" GROUP BY scoresummary.workerid, employee.fullname, employee.firstworktime, position.name";
		return query;
	}

	private String getOrderString() {
		if(order == null)
			order = 0;
		if(order == 1){
			return "ASC";
		}else{
			order = 0;
			return "DESC";
		}
	}

	private String getScoreTypeQuery() {
		String scoretypeselection = "";
		if(scoretype == null)
			scoretype = 0;
		if(scoretype == TYPE_BY_FIX_SCORE){
			scoretypeselection = "ORDER BY SUM(fixscore) "+getOrderString();
		}else if(scoretype == TYPE_BY_TEMP_SCORE){
			scoretypeselection = "ORDER BY SUM(score+projectscore) "+getOrderString();
		}else if (scoretype == TYPE_BY_PERFORMANCESCORE){
			scoretypeselection = "ORDER BY SUM(performancescore) "+getOrderString();
		}else if(scoretype == TYPE_BY_TOTAL_SCORE){
			scoretypeselection = "ORDER BY SUM(fixscore+score+performancescore+projectscore) " +getOrderString();
		}else{
			scoretypeselection = "ORDER BY SUM(score) "+getOrderString();
		}
		return scoretypeselection;
	}

	public String getNormalStatement(){
		if(recordDate == null){
			recordDate = new Date();
		}
		Calendar c = Calendar.getInstance();
		c.setTime(recordDate);
		String scoretypeselection = getScoreTypeQuery();
		String query = "SELECT scoresummary.workerid AS workercode,SUM(fixscore) AS fixscore, SUM(score+projectscore) AS tempscore, SUM(fixscore+score+performancescore+projectscore) AS totalscore, SUM(performancescore) AS performancescore, RANK() OVER ("+scoretypeselection+") AS rank, COUNT(scoresummary.workerid) AS count," +
				" employee.fullname AS name, employee.firstworktime AS firstworktime," +
				" position.name AS positionname " +
				" FROM scoresummary LEFT JOIN employee ON scoresummary.workerid=employee.workerid"+
				" LEFT JOIN position ON employee.positionid = position.id";
//		if(selectedGroup != null){
//			query += 	" JOIN positiongroup ON positiongroup.scoregroupid=" +selectedGroup+ " AND positiongroup.positionid = employee.positionid";
//		}
		
		if(selecttype == null)
			selecttype = 2;
		if(selecttype == TYPE_BY_HISTORY){
			//order in history
			query += " WHERE position.name IS NOT NULL ";
		}else if(selecttype == TYPE_BY_YEAR){
			//order in year
			query += "  WHERE EXTRACT(year FROM date)="+c.get(Calendar.YEAR); 
		}else{
			//order in month
			query += "  WHERE EXTRACT(month FROM date)="+(c.get(Calendar.MONTH)+1);
		}
		if(rankGroup != null){
			if(rankGroup == RANK_ZHU_REN_JI){
				query += " AND employee.joblevel='中管'";
			}else if(rankGroup == RANK_GUAN_LI_YUAN){
				query += " AND employee.joblevel='管'";
			}else if(rankGroup == RANK_JIA_SHI_YUAN){
				query += " AND position.name LIKE '%驾驶员%'";
			}else if(rankGroup == RANK_WEI_XIU_GONG){
//				query += " AND position.name LIKE '%驾驶员%'";
			}else if(rankGroup == RANK_FU_WU_YUAN){
				query += " AND position.name LIKE '%服务员%'";
			}
		}
		
		if(department != null){
			query += " AND employee.departmentid="+department;
		}
		if(position != null){
			query += " AND position.name LIKE '%"+position+"%'";
		}
		
		query += " GROUP BY scoresummary.workerid, employee.fullname, employee.firstworktime, position.name";
		return query;
	}
	
	public String getInTimeRangeStatement(){
		if(recordStartDate == null || recordEndDate == null){
			return getNormalStatement();
		}else{
			Calendar start = Calendar.getInstance();
			start.setTime(recordStartDate);
			Calendar end = Calendar.getInstance();
			end.setTime(recordEndDate);
			String query = "SELECT scoresummary.workerid AS workercode,SUM(fixscore) AS fixscore, SUM(score+projectscore) AS tempscore, SUM(performancescore) AS performancescore, SUM(fixscore+score+performancescore+projectscore) AS totalscore, RANK() OVER ("+getScoreTypeQuery()+") AS rank, COUNT(scoresummary.workerid) AS count," +
					" employee.fullname AS name, employee.firstworktime AS firstworktime," +
					" position.name AS positionname " +
					" FROM scoresummary JOIN employee ON scoresummary.workerid=employee.workerid" +
					" JOIN position ON employee.positionid = position.id";
			if(selectedGroup != null){
				query += 	" JOIN positiongroup ON positiongroup.scoregroupid=" +selectedGroup+ " AND positiongroup.positionid = employee.positionid";
			}
			query +=" WHERE EXTRACT(month FROM date)>="+(start.get(Calendar.MONTH)+1)+
					" AND EXTRACT(month FROM date)<="+(end.get(Calendar.MONTH)+1)+
					" AND EXTRACT(year FROM date)>="+start.get(Calendar.YEAR)+
					" AND EXTRACT(year FROM date)<="+end.get(Calendar.YEAR);
			if(selectedGroup == null && department != null){
				query += " AND employee.departmentid="+department;
			}
			if(selectedGroup == null && position != null){
				query += " AND position.name LIKE '%"+position+"%'";
			}
			query += " GROUP BY scoresummary.workerid, employee.fullname, employee.firstworktime, position.name";
			return query;
		}
	}
	
	public Date getRecordDate() {
		return recordDate;
	}

	public void setRecordDate(Date recordDate) {
		this.recordDate = recordDate;
	}

	public Date getRecordStartDate() {
		return recordStartDate;
	}

	public void setRecordStartDate(Date recordStartDate) {
		this.recordStartDate = recordStartDate;
	}

	public Date getRecordEndDate() {
		return recordEndDate;
	}

	public void setRecordEndDate(Date recordEndDate) {
		this.recordEndDate = recordEndDate;
	}

	public Integer getSelecttype() {
		return selecttype;
	}

	public void setSelecttype(Integer selecttype) {
		this.selecttype = selecttype;
	}

	public Integer getOrder() {
		return order;
	}

	public void setOrder(Integer order) {
		this.order = order;
	}

	public Integer getDepartment() {
		return department;
	}

	public void setDepartment(Integer department) {
		this.department = department;
	}

	public String getPosition() {
		return position;
	}

	public void setPosition(String position) {
		this.position = position;
	}

	public static int getTypeByHistory() {
		return TYPE_BY_HISTORY;
	}

	public static int getTypeByYear() {
		return TYPE_BY_YEAR;
	}

	public static int getTypeByMonth() {
		return TYPE_BY_MONTH;
	}

	public static int getTypeByFixScore() {return TYPE_BY_FIX_SCORE;}
	public static int getTypeByTempScore() {return TYPE_BY_TEMP_SCORE;}
	public static int getTypeByTotalScore() {return TYPE_BY_TOTAL_SCORE;}
	

	public Integer getScoretype() {
		return scoretype;
	}

	public void setScoretype(Integer scoretype) {
		this.scoretype = scoretype;
	}

	public Integer getSelectedGroup() {
		return selectedGroup;
	}

	public void setSelectedGroup(Integer selectedGroup) {
		this.selectedGroup = selectedGroup;
	}

	public Integer getRankGroup() {
		return rankGroup;
	}

	public void setRankGroup(Integer rankGroup) {
		this.rankGroup = rankGroup;
	}

	public static int getTypeByPerformancescore() {
		return TYPE_BY_PERFORMANCESCORE;
	}

	public static int getRankZhuRenJi() {
		return RANK_ZHU_REN_JI;
	}

	public static int getRankGuanLiYuan() {
		return RANK_GUAN_LI_YUAN;
	}

	public static int getRankWeiXiuGong() {
		return RANK_WEI_XIU_GONG;
	}

	public static int getRankFuWuYuan() {
		return RANK_FU_WU_YUAN;
	}

	public static int getRankJiaShiYuan() {
		return RANK_JIA_SHI_YUAN;
	}
	
}
