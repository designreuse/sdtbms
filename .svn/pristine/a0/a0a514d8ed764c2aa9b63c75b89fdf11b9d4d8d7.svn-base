package com.bus.stripes.actionbean.score;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.HandlesEvent;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.action.UrlBinding;
import net.sourceforge.stripes.integration.spring.SpringBean;

import com.bus.dto.Employee;
import com.bus.dto.score.ScoreMemberRank;
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
	private Date recordDate;
	
	private ScoreViewPublicSelector scoreSelector;
	
	private int pagenum;
	private int lotsize = 20;
	private Long totalcount;
	private Long recordsTotal;
	
	private List<SelectBoxOption> departments;
	private List<SelectBoxOption> positions;
	private Integer scoretype = 0;
	
	@DefaultHandler
	public Resolution defaultAction(){
		if(founds == null ){
			founds = new ArrayList<Employee>();
		}
		if(records == null){
			records = new ArrayList<Scorerecord>();
		}
		return new ForwardResolution("/public/empscoredetail.jsp");
	}
	
	@HandlesEvent(value="getMembers")
	public Resolution getMembers(){
		if(selector == null)
			return defaultAction();
		Map map = hrBean.getEmployeesBySelector(0, 0, selector.getSelectorStatement());
		founds = (List<Employee>) map.get("list");
		return defaultAction();
	}
	
	@HandlesEvent(value="memberDetail")
	public Resolution memberDetail(){
		String workerid = context.getRequest().getParameter("workerid");
		if(workerid == null)
			return defaultAction();
		try{
			if(recordDate == null)
				recordDate = new Date();
			member = scoreBean.getScoreMemberByWorkerid(workerid);
			records = scoreBean.getRecords(member,recordDate);
			return defaultAction();
		}catch(Exception e){
			return context.errorResolution("获取员工详细积分错误","请联系管理员."+e.getMessage());
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
		departments = SelectBoxOptions.getDepartment(hrBean.getAllDepartment());
		positions = SelectBoxOptions.getPosition(hrBean.getAllPosition());
	}
	
	private void getFromStatement(String statement) {
		try{
			Map map = scoreBean.getSummaryByStatement(pagenum,lotsize,statement);
			System.out.println("Using statement:"+statement);
			setSummarys((List<ScoreMemberRank>) map.get("list"));
			setRecordsTotal((Long)map.get("count"));
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
			initData(new ScoreViewPublicSelector().getSelectorStatement());
		else
			initData(scoreSelector.getSelectorStatement());
		//process data to sum up
		return new ForwardResolution("/public/scoreranking.jsp").addParameter("pagenum", pagenum)
				.addParameter("scoretype", scoretype);
	}
	
	@HandlesEvent(value="getRankingRecords")
	public Resolution getRankingRecords(){
		if(scoreSelector == null)
			initData(new ScoreViewPublicSelector().getSelectorStatement());
		else
			initData(scoreSelector.getNormalStatement());
		if(scoreSelector.getScoretype() != null)
			scoretype = scoreSelector.getScoretype();
		return new ForwardResolution("/public/scoreranking.jsp").addParameter("pagenum", pagenum)
				.addParameter("scoretype", scoretype);
	}
	
	@HandlesEvent(value="getRankingRecordsInTimeRange")
	public Resolution getRankingRecordsInTimeRange(){
		if(scoreSelector == null)
			initData(new ScoreViewPublicSelector().getSelectorStatement());
		else
			initData(scoreSelector.getInTimeRangeStatement());
		//process data
		if(scoreSelector.getScoretype() != null)
			scoretype = scoreSelector.getScoretype();
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
		long sum = 0L;
		if(records == null)
			return sum+"";
		else{
			for(Scorerecord r:records){
				sum += (long)r.getScoretype().getScore();
			}
		}
		return HRUtil.formatNumberComma(new Long(sum).toString());
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
}
