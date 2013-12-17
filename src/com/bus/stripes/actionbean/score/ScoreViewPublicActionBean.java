package com.bus.stripes.actionbean.score;

import java.math.BigInteger;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.HandlesEvent;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.action.StreamingResolution;
import net.sourceforge.stripes.action.UrlBinding;
import net.sourceforge.stripes.integration.spring.SpringBean;

import com.bus.dto.Employee;
import com.bus.dto.score.ScoreDivGroup;
import com.bus.dto.score.ScoreMemberRank;
import com.bus.dto.score.Scoregroup;
import com.bus.dto.score.Scoremember;
import com.bus.dto.score.Scorerecord;
import com.bus.dto.score.Scoresummary;
import com.bus.services.CustomActionBean;
import com.bus.services.HRBean;
import com.bus.services.ScoreBean;
import com.bus.stripes.selector.EmployeeSelector;
import com.bus.stripes.selector.ScoreViewPublicSelector;
import com.bus.util.HRUtil;
import com.bus.util.SelectBoxOption;
import com.bus.util.SelectBoxOptions;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

@UrlBinding("/actionbean/Empscore.action")
public class ScoreViewPublicActionBean extends CustomActionBean{

	private ScoreBean scoreBean;
	public ScoreBean getScoreBean(){return this.scoreBean;}
	@SpringBean
	public void setScoreBean(ScoreBean bean){this.scoreBean = bean;}
	
	private HRBean hrBean;
	public HRBean getHrBean(){return hrBean;}
	@SpringBean
	public void setHrBean(HRBean bean){this.hrBean = bean;}
	
	private Scoremember member;
	private EmployeeSelector selector;
	private List<Employee> founds;
	private List<Scorerecord> records;
	private List<ScoreMemberRank> summarys;
	private List<Scoregroup> scoregroups;
	private Integer rankGroup;
	private Date recordDate;
	private Date recordEndDate;
	
	private ScoreViewPublicSelector scoreSelector;
	
	private int pagenum;
	private int lotsize = 20;
	private Long totalcount;
	private Long recordsTotal;
	
	private List<SelectBoxOption> departments;
	private List<SelectBoxOption> positions;
	private List<ScoreDivGroup> scoreGroups;
	private Integer scoretype = 0;
	private ScoreRanking scoreRanking;
	
	
	@DefaultHandler
	public Resolution defaultAction(){
		if(founds == null ){
			founds = new ArrayList<Employee>();
		}
		if(records == null){
			records = new ArrayList<Scorerecord>();
		}
		getDriverScoreGroups();
		return new ForwardResolution("/public/empscoredetail.jsp");
	}
	
	private void getDriverScoreGroups() {
		try{
			scoreGroups = scoreBean.getDriverScoreGroups();
		}catch(Exception e){
			e.printStackTrace();
			scoreGroups = new ArrayList<ScoreDivGroup>();
		}
	}
	@HandlesEvent(value="getMembers")
	public Resolution getMembers(){
		JsonObject json = new JsonObject();
		try{
			if(selector == null)
				selector = new EmployeeSelector();
			String name = context.getRequest().getParameter("name");
			String workerid = context.getRequest().getParameter("workerid");
			if(name != null && !name.trim().equals(""))
				selector.setName(name);
			if(workerid != null && !workerid.trim().equals(""))
				selector.setWorkerid(workerid);
			if(selector.getName() == null && selector.getWorkerid() == null){
				json.addProperty("result", "0");
				json.addProperty("msg", "请输入名字或工号查询");
				return new StreamingResolution("text/charset=utf-8;",json.toString());
			}
			System.out.println(selector.getSelectorStatement());
			Map map = hrBean.getEmployeesBySelector(0, 0, selector.getSelectorStatement());
			founds = (List<Employee>) map.get("list");
			JsonArray jArray = new JsonArray();
			for(Employee e:founds){
				JsonObject jo = new JsonObject();
				jo.addProperty("name", e.getFullname());
				jo.addProperty("workerid", e.getWorkerid());
				jArray.add(jo);
			}
			json.add("data", jArray);
			json.addProperty("result", "1");
		}catch(Exception e){
			e.printStackTrace();
			json.addProperty("result", "0");
			json.addProperty("msg", "获取失败，名字或工号不存在");
		}
		return new StreamingResolution("text/charset=utf-8;",json.toString());
	}
	
	@HandlesEvent(value="memberDetail")
	public Resolution memberDetail(){
		String workerid = context.getRequest().getParameter("workerid");
		if(workerid == null)
			return defaultAction();
		try{
			if(recordDate == null)
				recordDate = HRUtil.parseDate("2013-01-01", "yyyy-MM-dd");
			if(recordEndDate == null)
				recordEndDate = Calendar.getInstance().getTime();
			member = scoreBean.getScoreMemberByWorkerid(workerid);
			scoreRanking = setScoreRankingContent(member);
			if(member == null){
				System.out.println("Cannot find member!" + workerid);
			}else{
				records = scoreBean.getRecords(member,recordDate, recordEndDate);
				if(records.size() == 0)
					records = null;
			}
			return defaultAction();
		}catch(Exception e){
			e.printStackTrace();
			return context.errorResolution("获取员工详细积分错误","请联系管理员."+e.getMessage());
		}
	}
	
	private ScoreRanking setScoreRankingContent(Scoremember member2) throws Exception{
		if(scoreRanking == null){
			scoreRanking = new ScoreRanking();
		}
		List<Integer> results = (List<Integer>) scoreBean.runQuery("SELECT m.scoregroupid FROM score_group_mapper m LEFT JOIN employee e ON m.empid=e.id WHERE e.workerid='"+member2.getEmployee().getWorkerid()+"'");
		if(results == null || results.size() == 0){
			scoreRanking.setGroupId(null);
		}else{
			scoreRanking.setGroupId((Integer) results.get(0));
		}
		if(scoreRanking.getGroupId() != null){
			//年总分
			List<Float> floatResults = (List<Float>) scoreBean.runQuery("SELECT SUM(score+projectscore) AS total FROM scoresummary WHERE workerid='"+member2.getEmployee().getWorkerid()+"' AND EXTRACT(year FROM date)="+Calendar.getInstance().get(Calendar.YEAR));
			if(floatResults != null && floatResults.size() > 0)
				scoreRanking.setYearScore(floatResults.get(0));
			//历史总分
			floatResults = (List<Float>) scoreBean.runQuery("SELECT SUM(score+projectscore) AS total FROM scoresummary WHERE workerid='"+member2.getEmployee().getWorkerid()+"'");
			if(floatResults != null && floatResults.size() > 0)
				scoreRanking.setTotalScore(floatResults.get(0));
			//年组内排名
			List<BigInteger> bigIntResults = (List<BigInteger>) scoreBean.runQuery("SELECT t.rank FROM (SELECT e.workerid as wid,SUM(ss.score+ss.projectscore) AS total ,RANK() OVER (ORDER BY SUM(ss.score+ss.projectscore) DESC) as RANK FROM scoresummary ss LEFT JOIN employee e ON ss.workerid=e.workerid LEFT JOIN score_group_mapper m ON e.id=m.empid WHERE m.scoregroupid="+scoreRanking.getGroupId()+" AND EXTRACT(year FROM ss.date)="+Calendar.getInstance().get(Calendar.YEAR)+" GROUP BY wid) AS t WHERE t.wid='"+member2.getEmployee().getWorkerid()+"'");
			if(bigIntResults != null && bigIntResults.size() > 0)
				scoreRanking.setGroupYearRank(bigIntResults.get(0).intValue());
			//月组内排名
//			bigIntResults = (List<BigInteger>) scoreBean.runQuery("SELECT t.rank FROM (SELECT e.workerid as wid,SUM(ss.score+ss.projectscore) AS total ,RANK() OVER (ORDER BY SUM(ss.score+ss.projectscore) DESC) as RANK FROM scoresummary ss LEFT JOIN employee e ON ss.workerid=e.workerid LEFT JOIN score_group_mapper m ON e.id=m.empid WHERE m.scoregroupid="+scoreRanking.getGroupId()+" AND EXTRACT(year FROM ss.date)="+Calendar.getInstance().get(Calendar.YEAR)+" AND EXTRACT(month FROM ss.date)="+(Calendar.getInstance().get(Calendar.MONTH)+1)+" GROUP BY wid) AS t WHERE t.wid='"+member2.getEmployee().getWorkerid()+"'");
//			if(bigIntResults != null && bigIntResults.size() > 0)
//				scoreRanking.setGroupMonthRank(bigIntResults.get(0).intValue());
			//设置组驾驶员数
			bigIntResults = (List<BigInteger>) scoreBean.runQuery("SELECT count(*) FROM score_group_mapper m LEFT JOIN employee e ON m.empid=e.id WHERE m.scoregroupid="+scoreRanking.getGroupId());
			if(bigIntResults != null && bigIntResults.size() > 0)
				scoreRanking.setGroupDriver(bigIntResults.get(0).intValue());
			
			//历史排名
			bigIntResults = (List<BigInteger>) scoreBean.runQuery("SELECT t.rank FROM (SELECT e.workerid as wid,SUM(ss.score+ss.projectscore) AS total ,RANK() OVER (ORDER BY SUM(ss.score+ss.projectscore) DESC) as RANK FROM scoresummary ss LEFT JOIN employee e ON ss.workerid=e.workerid LEFT JOIN position p ON p.id=e.positionid WHERE "+getGroupQuery(member2)+" GROUP BY wid) AS t WHERE t.wid='"+member2.getEmployee().getWorkerid()+"'");
			if(bigIntResults != null && bigIntResults.size() > 0)
				scoreRanking.setTotalHisRank(bigIntResults.get(0).intValue());
			//年总排名
			bigIntResults = (List<BigInteger>) scoreBean.runQuery("SELECT t.rank FROM (SELECT e.workerid as wid,SUM(ss.score+ss.projectscore) AS total ,RANK() OVER (ORDER BY SUM(ss.score+ss.projectscore) DESC) as RANK FROM scoresummary ss LEFT JOIN employee e ON ss.workerid=e.workerid LEFT JOIN position p ON p.id=e.positionid WHERE "+getGroupQuery(member2)+" AND EXTRACT(year FROM ss.date)="+Calendar.getInstance().get(Calendar.YEAR)+" GROUP BY wid) AS t WHERE t.wid='"+member2.getEmployee().getWorkerid()+"'");
			if(bigIntResults != null && bigIntResults.size() > 0)
				scoreRanking.setTotalYearRank(bigIntResults.get(0).intValue());
			//月总排名
//			bigIntResults = (List<BigInteger>) scoreBean.runQuery("SELECT t.rank FROM (SELECT e.workerid as wid,SUM(ss.score+ss.projectscore) AS total ,RANK() OVER (ORDER BY SUM(ss.score+ss.projectscore) DESC) as RANK FROM scoresummary ss LEFT JOIN employee e ON ss.workerid=e.workerid LEFT JOIN position p ON p.id=e.positionid WHERE "+getGroupQuery(member2)+" AND EXTRACT(year FROM ss.date)="+Calendar.getInstance().get(Calendar.YEAR)+" AND EXTRACT(month FROM ss.date)="+(Calendar.getInstance().get(Calendar.MONTH)+1)+" GROUP BY wid) AS t WHERE t.wid='"+member2.getEmployee().getWorkerid()+"'");
//			if(bigIntResults != null && bigIntResults.size() > 0)
//				scoreRanking.setTotalMonthRank(bigIntResults.get(0).intValue());
			//设置总驾驶员数
			bigIntResults = (List<BigInteger>) scoreBean.runQuery("SELECT count(*) FROM (SELECT e.workerid FROM scoresummary ss LEFT JOIN employee e ON ss.workerid=e.workerid LEFT JOIN position p ON p.id=e.positionid WHERE "+getGroupQuery(member2)+" AND EXTRACT(year FROM ss.date)="+Calendar.getInstance().get(Calendar.YEAR) + " AND e.status='A' GROUP BY e.workerid) AS ppl");
			if(bigIntResults != null && bigIntResults.size() > 0)
				scoreRanking.setTotalDriver(bigIntResults.get(0).intValue());
//			System.out.println("Found total ppl:"+scoreRanking.getTotalDriver());
		}
		return scoreRanking;
	}
	private String getGroupQuery(Scoremember member2) throws Exception {
		String pname = member2.getEmployee().getPosition().getName();
		String joblevel = member2.getEmployee().getJoblevel();
		if(joblevel != null && joblevel.contains("中管")){
			if(pname.contains("车队长"))
				return " e.joblevel='中管' AND p.name LIKE'%车队长%'";
			else
				return " e.joblevel='中管' AND p.name NOT LIKE'%车队长%'";
		}else if(joblevel != null && joblevel.contains("非管")){
			if(pname.contains("驾驶员"))
				return " e.joblevel='非管' AND p.name LIKE'%驾驶员%'";
			else if(pname.contains("服务员"))
				return " e.joblevel='非管' AND p.name LIKE'%服务员%'";
			else
				return " e.joblevel='非管'";
		}else if(joblevel != null && joblevel.contains("管")){
			return " e.joblevel='管'";
		}else{
			return "";
		}
	}
	
	private void initData(String statement){
		loadOptionList();
		if(pagenum <= 0){
			pagenum = 1;
		}
		getFromStatement(statement);
		if(pagenum > totalcount)
			pagenum = Integer.parseInt(totalcount.toString());
	}
	
	private void loadOptionList(){
		try{
			scoregroups = scoreBean.getAllScoreGroup();
			departments = SelectBoxOptions.getScoregroups(scoreBean.getAllScoreGroups());
			positions = SelectBoxOptions.getPosition(hrBean.getAllPosition());
		}catch(Exception e){
			scoregroups = new ArrayList<Scoregroup>();
		}
	}
	
	private void getFromStatement(String statement) {
		try{
//			System.out.println("Using statement:"+statement);
			Map map = scoreBean.getSummaryByStatement(pagenum,lotsize,statement);
			setSummarys((List<ScoreMemberRank>) map.get("list"));
			setRecordsTotal(new Long((Integer)map.get("count")));
			for(int i=0;i<summarys.size();i++)
				summarys.get(i).setCountsum(recordsTotal.intValue());
		}catch(Exception e){
			System.out.println(e.getMessage());
			setRecordsTotal(0L);
		}
		if(recordDate == null){
			recordDate = new Date();
		}
		if(records == null){
			records = new ArrayList<Scorerecord>();
		}
		setTotalcount(getRecordsTotal()/lotsize +1);
	}
	
	@HandlesEvent(value="ranking")
	public Resolution rankingpage(){
		if(scoreSelector == null)
			scoreSelector = new ScoreViewPublicSelector();
		initData(scoreSelector.getSelectorStatement());
		getDriverScoreGroups();
		return new ForwardResolution("/public/scoreranking.jsp").addParameter("pagenum", pagenum)
				.addParameter("scoretype", scoretype);
	}
	
	public Date getRecordDate() {
		return recordDate;
	}
	public void setRecordDate(Date recordDate) {
		this.recordDate = recordDate;
	}
	public EmployeeSelector getSelector() {
		return selector;
	}
	public void setSelector(EmployeeSelector selector) {
		this.selector = selector;
	}
	public Scoremember getMember() {
		return member;
	}
	public void setMember(Scoremember member) {
		this.member = member;
	}
	public List<Employee> getFounds() {
		return founds;
	}
	public void setFounds(List<Employee> founds) {
		this.founds = founds;
	}
	public List<Scorerecord> getRecords() {
		return records;
	}
	public void setRecords(List<Scorerecord> records) {
		this.records = records;
	}
	public String getRecordsSum(){
		float sum = 0L;
		if(records == null)
			return sum+"";
		else{
			for(Scorerecord r:records){
				sum += r.getScore();
			}
		}
		return HRUtil.formatFloatNumberComma(new Float(sum).toString());
	}
	public int getPagenum() {
		return pagenum;
	}
	public void setPagenum(int pagenum) {
		this.pagenum = pagenum;
	}
	public int getLotsize() {
		return lotsize;
	}
	public void setLotsize(int lotsize) {
		this.lotsize = lotsize;
	}
	public Long getTotalcount() {
		return totalcount;
	}
	public void setTotalcount(Long totalcount) {
		this.totalcount = totalcount;
	}
	public Long getRecordsTotal() {
		return recordsTotal;
	}
	public void setRecordsTotal(Long recordsTotal) {
		this.recordsTotal = recordsTotal;
	}
	public ScoreViewPublicSelector getScoreSelector() {
		return scoreSelector;
	}
	public void setScoreSelector(ScoreViewPublicSelector scoreSelector) {
		this.scoreSelector = scoreSelector;
	}
	public List<SelectBoxOption> getDepartments() {
		return departments;
	}
	public void setDepartments(List<SelectBoxOption> departments) {
		this.departments = departments;
	}
	public List<SelectBoxOption> getPositions() {
		return positions;
	}
	public void setPositions(List<SelectBoxOption> positions) {
		this.positions = positions;
	}
	public List<ScoreMemberRank> getSummarys() {
		return summarys;
	}
	public void setSummarys(List<ScoreMemberRank> summarys) {
		this.summarys = summarys;
	}
	public Integer getScoretype() {
		return scoretype;
	}
	public void setScoretype(Integer scoretype) {
		this.scoretype = scoretype;
	}
	public List<Scoregroup> getScoregroups() {
		return scoregroups;
	}
	public void setScoregroups(List<Scoregroup> scoregroups) {
		this.scoregroups = scoregroups;
	}
	public Integer getRankGroup() {
		return rankGroup;
	}
	public void setRankGroup(Integer rankGroup) {
		this.rankGroup = rankGroup;
	}
	public Date getRecordEndDate() {
		return recordEndDate;
	}
	public void setRecordEndDate(Date recordEndDate) {
		this.recordEndDate = recordEndDate;
	}
	
	public ScoreRanking getScoreRanking() {
		return scoreRanking;
	}
	public void setScoreRanking(ScoreRanking scoreRanking) {
		this.scoreRanking = scoreRanking;
	}

	public List<ScoreDivGroup> getScoreGroups() {
		return scoreGroups;
	}
	public void setScoreGroups(List<ScoreDivGroup> scoreGroups) {
		this.scoreGroups = scoreGroups;
	}

	public class ScoreRanking{
		public Float totalScore;
		public Float yearScore;
		public Integer totalHisRank;
		public Integer totalYearRank;
		public Integer totalMonthRank;
		public Integer groupYearRank;
		public Integer groupMonthRank;
		public Integer groupId;
		public Integer totalDriver;
		public Integer groupDriver;
		
		public Integer getTotalDriver() {
			return totalDriver;
		}
		public void setTotalDriver(Integer totalDriver) {
			this.totalDriver = totalDriver;
		}
		public Float getTotalScore() {
			return totalScore;
		}
		public void setTotalScore(Float totalScore) {
			this.totalScore = totalScore;
		}
		public Float getYearScore() {
			return yearScore;
		}
		public void setYearScore(Float yearScore) {
			this.yearScore = yearScore;
		}
		public Integer getTotalHisRank() {
			return totalHisRank;
		}
		public void setTotalHisRank(Integer totalHisRank) {
			this.totalHisRank = totalHisRank;
		}
		public Integer getTotalYearRank() {
			return totalYearRank;
		}
		public void setTotalYearRank(Integer totalYearRank) {
			this.totalYearRank = totalYearRank;
		}
		public Integer getTotalMonthRank() {
			return totalMonthRank;
		}
		public void setTotalMonthRank(Integer totalMonthRank) {
			this.totalMonthRank = totalMonthRank;
		}
		public Integer getGroupYearRank() {
			return groupYearRank;
		}
		public void setGroupYearRank(Integer groupYearRank) {
			this.groupYearRank = groupYearRank;
		}
		public Integer getGroupMonthRank() {
			return groupMonthRank;
		}
		public void setGroupMonthRank(Integer groupMonthRank) {
			this.groupMonthRank = groupMonthRank;
		}
		public Integer getGroupId() {
			return groupId;
		}
		public void setGroupId(Integer groupId) {
			this.groupId = groupId;
		}
		
		public Integer getGroupDriver() {
			return groupDriver;
		}
		public void setGroupDriver(Integer groupDriver) {
			this.groupDriver = groupDriver;
		}
		public Integer getTotalRankPercent(){
			Float result = (float)this.totalYearRank/(float)this.totalDriver * 100F;
			if(result < 1)
				result += 1;
			return Math.round(result);
		}
		
		public Integer getYearRankPercent(){
			Float result = (float)this.groupYearRank/(float)this.groupDriver * 100F;
			if(result < 1)
				result += 1;
			return Math.round(result);
		}
	}
}
