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
	public static final int RANK_CHE_DUI_ZHANG = 5;
	
	private Date recordStartDate;
	private Date recordEndDate;
	private Integer scoretype;
	private Integer order;
	private Integer rankGroup;
	private Integer scoreGroup;
	
	@Override
	public String getSelectorStatement() {
		if(recordStartDate == null){
			try{
				Calendar cal = Calendar.getInstance();
				cal.set(Calendar.WEEK_OF_YEAR,1);
				cal.set(Calendar.DAY_OF_WEEK,1);
				recordStartDate = cal.getTime();
			}catch(Exception e){
				recordStartDate = Calendar.getInstance().getTime();
			}
		}
		if(recordEndDate == null){
			recordEndDate = Calendar.getInstance().getTime();
		}
		if(rankGroup == null){
			rankGroup = 4;
		}
		String scoretypeselection = getScoreTypeQuery();
		String rankGroupStatement = getRankGroupQuery();
		String query = "";
		if(rankGroup == 4 && scoreGroup != null){
			query = "SELECT scoresummary.workerid AS workercode,SUM(fixscore) AS fixscore, SUM(score+projectscore) AS tempscore, SUM(performancescore) AS performancescore, SUM(fixscore+score+performancescore+projectscore) AS totalscore, RANK() OVER ("+scoretypeselection+") AS rank, COUNT(scoresummary.workerid) AS count," +
					" employee.fullname AS name, employee.firstworktime AS firstworktime," +
					" position.name AS positionname " +
					" FROM scoresummary LEFT JOIN employee ON scoresummary.workerid=employee.workerid" +
					" LEFT JOIN position ON employee.positionid = position.id" +
					" LEFT JOIN score_group_mapper ON employee.id=score_group_mapper.empid" +
					" WHERE scoresummary.date BETWEEN '"+HRUtil.parseDateToString(recordStartDate)+"' AND '"+HRUtil.parseDateToString(recordEndDate)+"' " +
					" AND score_group_mapper.scoregroupid="+ scoreGroup + 
							" GROUP BY scoresummary.workerid, employee.fullname, employee.firstworktime, position.name";
		}else{
			query = "SELECT scoresummary.workerid AS workercode,SUM(fixscore) AS fixscore, SUM(score+projectscore) AS tempscore, SUM(performancescore) AS performancescore, SUM(fixscore+score+performancescore+projectscore) AS totalscore, RANK() OVER ("+scoretypeselection+") AS rank, COUNT(scoresummary.workerid) AS count," +
					" employee.fullname AS name, employee.firstworktime AS firstworktime," +
					" position.name AS positionname " +
					" FROM scoresummary LEFT JOIN employee ON scoresummary.workerid=employee.workerid" +
					" LEFT JOIN position ON employee.positionid = position.id" +
					" WHERE scoresummary.date BETWEEN '"+HRUtil.parseDateToString(recordStartDate)+"' AND '"+HRUtil.parseDateToString(recordEndDate)+"' " +
					rankGroupStatement + 
							" GROUP BY scoresummary.workerid, employee.fullname, employee.firstworktime, position.name";
		}
//		System.out.println(query);
		return query;
	}

	private String getRankGroupQuery() {
		if(rankGroup == RANK_CHE_DUI_ZHANG){
			return " AND employee.joblevel='中管' AND position.name LIKE '%车队长%'";
		}else if(rankGroup == RANK_FU_WU_YUAN){
			return " AND position.name LIKE '%服务员%'";
		}else if(rankGroup == RANK_GUAN_LI_YUAN){
			return " AND employee.joblevel='管'";
		}else if(rankGroup == RANK_JIA_SHI_YUAN){
			return " AND employee.joblevel!='管' AND position.name LIKE '%驾驶员%'";
		}else if(rankGroup == RANK_ZHU_REN_JI){
			return " AND employee.joblevel='中管' AND position.name NOT LIKE '%车队长%'";
		}else
			return" AND employee.joblevel!='管' AND position.name LIKE '%驾驶员%'"; 
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

	public Integer getOrder() {
		return order;
	}

	public void setOrder(Integer order) {
		this.order = order;
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

	public Integer getScoreGroup() {
		return scoreGroup;
	}

	public void setScoreGroup(Integer scoreGroup) {
		this.scoreGroup = scoreGroup;
	}

	public static int getRankCheDuiZhang() {
		return RANK_CHE_DUI_ZHANG;
	}
	
}
