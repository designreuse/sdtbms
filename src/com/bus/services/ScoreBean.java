package com.bus.services;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.springframework.transaction.annotation.Transactional;

import com.bus.dto.Account;
import com.bus.dto.Department;
import com.bus.dto.Employee;
import com.bus.dto.Position;
import com.bus.dto.logger.ScoreLog;
import com.bus.dto.score.DepartmentScore;
import com.bus.dto.score.Positiongroup;
import com.bus.dto.score.ScoreDivGroup;
//import com.bus.dto.score.ScoreDivGroup;
import com.bus.dto.score.ScoreExceptionList;
import com.bus.dto.score.ScoreGroupMapper;
import com.bus.dto.score.ScoreMemberRank;
import com.bus.dto.score.Scoreapprover;
import com.bus.dto.score.Scoregroup;
import com.bus.dto.score.Scoremember;
import com.bus.dto.score.Scorerecord;
import com.bus.dto.score.ScorerecordRemark;
import com.bus.dto.score.Scoresheetmapper;
import com.bus.dto.score.Scoresheets;
import com.bus.dto.score.Scoresummary;
import com.bus.dto.score.Scoretype;
import com.bus.dto.score.Voucherlist;
import com.bus.util.EmpDepartments;
import com.bus.util.HRUtil;
import com.bus.util.LoggerAction;
import com.bus.util.ScoreExcelFileProcessor;
import com.bus.util.dtoconverter.ScoreConvertor;
import com.sun.org.apache.bcel.internal.generic.INVOKEVIRTUAL;



public class ScoreBean  extends EMBean{
	

	/**
	 * Get the score types from giving page, if page or lotsize = -1, it will select all records
	 * <br/>
	 * returns a map with
	 * ('list',List<Scoretype>)
	 * <br/>
	 * ('count',Long)
	 * @param pagenum
	 * @param lotsize
	 * @return
	 * @throws Exception
	 */
	public Map getAllScoreTypes(int pagenum, int lotsize) throws Exception{
		List<Scoretype> list=null;
		if(pagenum == -1 || lotsize == -1)
			list = em.createQuery("SELECT q FROM Scoretype q WHERE q.status=1 ORDER BY reference").getResultList();
		else
			list = em.createQuery("SELECT q FROM Scoretype q WHERE q.status=1 ORDER BY reference")
				.setFirstResult(pagenum * lotsize - lotsize).setMaxResults(lotsize).getResultList();
		Long count = (Long) em.createQuery("SELECT count(q) FROM Scoretype q").getSingleResult();
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("list", list);
		map.put("count", count);
		return map;
	}

	/**
	 * Return the scoretypes inside given scoresheet
	 * @param itemlist
	 * @return
	 * @throws Exception
	 */
	public List<Scoretype> getScoretypesFromSheet(String itemlist) throws Exception{
		Scoresheets sheet = em.find(Scoresheets.class, Integer.parseInt(itemlist));
		List<Scoretype> stlist = new ArrayList<Scoretype>();
		List<Scoresheetmapper> mappers = null;
		if(sheet.getParent() != null){
			mappers = em.createQuery("SELECT q FROM Scoresheetmapper q WHERE (q.sheet=:sh OR q.sheet=:sh2) AND q.type.status=1 ORDER BY q.type.reference")
					.setParameter("sh", sheet).setParameter("sh2", sheet.getParent()).getResultList();
		}else{
			mappers = em.createQuery("SELECT q FROM Scoresheetmapper q WHERE q.sheet=:sh AND q.type.status=1 ORDER BY q.type.reference").setParameter("sh", sheet).getResultList();
		}
		for(Scoresheetmapper mapper:mappers){
			stlist.add(mapper.getType());
		}
		return stlist;
	}

	/**
	 * Get scoretypes from statement with page and lotsize, if page or lotsize = -1,
	 * <br/>default will be set pagenum=1, lotsize=20;
	 * <br/>returns a map with
	 * ('list',List<Scoretype>)
	 * <br/>
	 * ('count',Long)
	 * @param pagenum
	 * @param lotsize
	 * @param statement
	 * @return
	 * @throws Exception
	 */
	public Map getScoretypeByStatement(int pagenum, int lotsize,String statement) throws Exception{
		List<Scoretype> list=null;
		if(pagenum == -1 || lotsize == -1)
			pagenum =1;lotsize = 60;
		list = em.createQuery(statement)
				.setFirstResult(pagenum * lotsize - lotsize).setMaxResults(lotsize).getResultList();
		String countstatement = "SELECT count(q) " + statement.substring(statement.indexOf("FROM"),statement.indexOf("ORDER BY"));
		Long count = (Long) em.createQuery(countstatement).getSingleResult();
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("list", list);
		map.put("count", count);
		return map;
	}

	/**
	 * Create a new scoretype and log to score logger
	 * @param user
	 * @param scoretype
	 * @throws Exception
	 */
	@Transactional(rollbackFor=Exception.class)
	public void saveScoretype(Account user, Scoretype scoretype) throws Exception{
		scoretype.setCreatedate(Calendar.getInstance().getTime());
		scoretype.setAccount(user);
		em.persist(scoretype);
		em.flush();
		em.persist(LoggerAction.createScoreType(user,scoretype.getId()+""));
	}

	/**
	 * Delete score type and make a record to log
	 * @param user
	 * @param st
	 * @throws Exception
	 */
	@Transactional
	public void removeScoreType(Account user, Scoretype st) throws Exception{
		st = em.find(Scoretype.class, st.getId());
		try{
			List<Scoresheetmapper> mappers = (List<Scoresheetmapper>) em.createQuery("SELECT q FROM Scoresheetmapper q WHERE scoretypeid=:st")
					.setParameter("st", st).getResultList();
			for(Scoresheetmapper s:mappers)
				em.remove(s);
		}catch(Exception e){
			e.printStackTrace();
		}
		em.persist(LoggerAction.removeScoreType(user, st));
		em.remove(st);
	}

	/**
	 * Get score type by id
	 * @param targetId
	 * @return
	 * @throws Exception
	 */
	public Scoretype getScoreTypeById(String targetId) throws Exception{
		return em.find(Scoretype.class, Integer.parseInt(targetId));
	}

	/**
	 * Edit update a score type with old createdate and old creator account
	 * @param user
	 * @param scoretype
	 * @throws Exception
	 */
	@Transactional
	public void updateScoreType(Account user, Scoretype scoretype) throws Exception{
		Scoretype st = getScoreTypeById(scoretype.getId()+"");
		scoretype.setCreatedate(st.getCreatedate());
		scoretype.setAccount(st.getAccount());
		em.merge(scoretype);
		em.persist(LoggerAction.editScoreType(user, scoretype));
	}

	/**
	 * Get newest 100 logs of given day
	 * @param logdate
	 * @return
	 * @throws Exception
	 */
	public List<ScoreLog> getScoreLogs(Date logdate) throws Exception{
		Calendar c = Calendar.getInstance();
		c.setTime(logdate);
		c.add(Calendar.DAY_OF_MONTH, -1);
		Date prev = c.getTime();
		c.add(Calendar.DAY_OF_MONTH, +2);
		Date now = c.getTime();
		return (List<ScoreLog>)em.createQuery("SELECT q FROM ScoreLog q WHERE createtime > ? AND createtime < ? ORDER BY createtime DESC")
		.setParameter(1, prev).setParameter(2, now).setMaxResults(100).getResultList();
				
	}

	/**
	 * Check whether scoremember is exist in the table, mapped by workerid
	 * @param workerid
	 * @return
	 * @throws Exception
	 */
	public boolean isScoreMemberExist(String workerid) throws Exception{
		try{
			Scoremember member = (Scoremember) em.createQuery("SELECT q FROM Scoremember q WHERE workerid=?")
					.setParameter(1, workerid).getSingleResult();
			if(member != null)
				return true;
			else
				return false;
		}catch(Exception e){
			return false;
		}
	}

	/**
	 * Get scoremember by score id, return null if no one found
	 * @param workerid
	 * @return
	 */
	public Scoremember getScoreMemberByWorkerid(String workerid) {
		try{
			Scoremember sm = (Scoremember) em.createQuery("SELECT q FROM Scoremember q WHERE q.employee.workerid='"+workerid+"'").getSingleResult();
			return sm;
		}catch(Exception e){
			return null;
		}
	}
	
	/**
	 * Assign score type to scorer from emp,
	 * record entry by user
	 * @param user
	 * @param emp
	 * @param scorer
	 * @param st
	 * @throws Exception
	 */
	@Transactional(rollbackFor=Exception.class)
	public void assignScoreTypeToScoreMember(Account user, String emp,String scorer, Scoretype st, Date scoredate, Float score, ScorerecordRemark remark) throws Exception{
		Scoremember scorersm = getScoreMemberByWorkerid(scorer); //受分人
		Scoremember empsm = getScoreMemberByWorkerid(emp); //给分人
		Date today = Calendar.getInstance().getTime();
		Scorerecord record = new Scorerecord();
		st = getScoreTypeById(st.getId()+"");
		if(Scoretype.SCORE_TYPE_TEMP == st.getType() && (score == null || score == 0)){ //如果分值为0，侧采用默认的条例分值
			score = st.getScore();
			if(score == 0){
				throw new Exception("项目分值不能为0，请独立对0分值的条例打分。");
			}
		}
		record.setCreator(user);
		record.setCreatedate(today);
		record.setReceiver(scorersm);
		record.setSender(empsm);
		record.setScoretype(st);
		record.setScoredate(scoredate);
		record.setScore(score);
		record.setStatus(Scorerecord.WAITING);//初次新建，自动提交到审核页面
		
		//设置注释
		if(remark != null){	 
			ScorerecordRemark scoreRe = new ScorerecordRemark();
			scoreRe.setRemark(remark.getRemark());
			em.persist(scoreRe);
			em.flush();
			record.setRemark(scoreRe);
		}
			
		
		em.persist(record);
		em.flush();
		
		//扣掉积分,以及部门积分
		takeAwayScore(record);
		
		if(isUserApprover(user.getEmployee())){//如果新建用户为审核人，侧直接通过审核，直接相加积分
//			System.out.println("设为已审核");
			record.setStatus(Scorerecord.APPROVED);
			em.merge(record);
			em.flush();
			addScore(record); 
		}
		em.persist(LoggerAction.assignScoretype(user, record.getId(), st, scorersm.getEmployee()));
	}
	
	/**
	 * 重设部门分值,最后更新日期少于本周开始日期则更新。
	 * @param record
	 * @throws Exception
	 */
	@Transactional(rollbackFor=Exception.class)
	public void toResetDepartmentScores(Employee scorer, Date createdate) throws Exception{
		if(HRUtil.isDateInCurrentWeek(createdate)){
			Calendar cal = Calendar.getInstance();
			Calendar cal2 = Calendar.getInstance();
			cal.set(Calendar.DAY_OF_WEEK,cal.getFirstDayOfWeek());
			cal2.setTime(cal.getTime());
			cal2.add(Calendar.WEEK_OF_YEAR, 1); 
			ScoreGroupMapper sgm = (ScoreGroupMapper) em.createQuery("SELECT q FROM ScoreGroupMapper q WHERE q.employee.id="+scorer.getId()).getSingleResult();
			Date date = sgm.getGroup().getLastUpdateDate();
			if(date.after(cal.getTime())){
				return;
			}
			ScoreDivGroup group = sgm.getGroup();
			int count = getScoreMemberCount(group.getId());
			if(group.getBasescore() == null)
				group.setBasescore(0F);
			group.setAvailable(group.getBasescore() * count);
			group.setLastUpdateDate(Calendar.getInstance().getTime());
			em.persist(group);
		}
	}
	
	/**
	 * Count department scoring employee number
	 * @param departmentid
	 * @return
	 * @throws Exception
	 */
	public Integer getScoreEmployeeCount(int departmentid) throws Exception{
		String query = "SELECT count(q) FROM employee q LEFT JOIN scoreexceptionlist s ON q.positionid = s.positionid WHERE (s.status IS NULL OR (s.status IS NOT NULL AND s.status="+ScoreExceptionList.HAS_UPPER_SCORE_LIMIT+")) AND q.departmentid="+ departmentid+" AND "
				+ "(q.joblevel='中管' OR q.joblevel='管') AND q.status='A'";
		BigInteger count = (BigInteger) em.createNativeQuery(query).getSingleResult();
		return count.intValue();
	}

	/**
	 * 
	 * @param employee
	 * @return
	 * @throws Exception
	 */
	private DepartmentScore getDepartmentScore(Employee employee) throws Exception {
		DepartmentScore ds = null;
		try{
			ds = (DepartmentScore) em.createQuery("SELECT q FROM DepartmentScore q WHERE q.vehicleteam.id=?")
					.setParameter(1, employee.getTeam().getId()).getSingleResult();
		}catch(Exception e){
			System.out.println(e.getMessage());
			ds = (DepartmentScore) em.createQuery("SELECT q FROM DepartmentScore q WHERE q.department.id=?")
					.setParameter(1, employee.getDepartment().getId()).getSingleResult();
		}
		return ds;
	}

	/**
	 * 减去已经用掉的积分
	 * @param record
	 * @throws Exception
	 */
	private void takeAwayScore(Scorerecord record) throws Exception{
		Scoretype st = record.getScoretype();
		if(st.getType() == Scoretype.SCORE_TYPE_TEMP){
			try{
				ScoreExceptionList excep = null;
				try{
					excep = (ScoreExceptionList) em.createQuery("SELECT q FROM ScoreExceptionList q WHERE q.position.id=?")
						.setParameter(1, record.getReceiver().getEmployee().getPosition().getId()).getSingleResult();
//					System.out.println("excep found."+excep.getPosition().getName());
				}catch(Exception e){
//					System.out.println("Exception not found for position."+e.getMessage());
					excep = null;
				}
				if(excep != null && excep.getStatus() == ScoreExceptionList.NO_UPPER_SCORE_LIMIT){
					return;
				}
				ScoreDivGroup sdg = (ScoreDivGroup) em.createNativeQuery("SELECT s.* FROM score_group_mapper m LEFT JOIN score_group s ON m.scoregroupid=s.id " +
						"WHERE m.empid="+record.getReceiver().getEmployee().getId(), ScoreDivGroup.class).getSingleResult();
				sdg.setAvailable(sdg.getAvailable()-record.getScore());
				sdg.setLastUpdateDate(Calendar.getInstance().getTime());
				em.merge(sdg);
			}catch(Exception e){
				e.printStackTrace();
			}
		}
	}

	/**
	 * 为员工 添加分值/减去分值
	 * @param recordId
	 * @throws Exception
	 */
	public void addScore(Scorerecord record) throws Exception{
		//更新员工总积分
		Scoremember scorer = record.getReceiver();
		scorer.setHistorytotal(scorer.getHistorytotal() + record.getScore());
		em.merge(scorer);
				
		//更新月积分，如果该月积分还没开始统计，创建一个新的该月份的积分
		Scoresummary summary=this.getScoreSummary(scorer, record.getScoredate());
		Scoretype st = record.getScoretype();
		Float score = record.getScore();
		if(summary == null){ //没找到该月积分
			summary = new Scoresummary();
			summary.setEmployee(scorer.getEmployee());
			summary.setDate(record.getCreatedate());
			if(st.getType() == Scoretype.SCORE_TYPE_FIX)
				summary.setFixscore(new Float(score));
			else if(st.getType() == Scoretype.SCORE_TYPE_TEMP)
				summary.setScore(new Float(score));
			else if(st.getType() == Scoretype.SCORE_TYPE_PERFORMENCE)
				summary.setPerformancescore(new Float(score));
			else if(st.getType() == Scoretype.SCORE_TYPE_PROJECT)
				summary.setProjectscore(new Float(score));
			em.persist(summary);
			em.flush();
			em.persist(LoggerAction.createNewScoreSummary(record.getCreator(),summary));
		}else{ //找到该月积分，并相加
			if(st.getType() == Scoretype.SCORE_TYPE_FIX)
				summary.setFixscore(summary.getFixscore() + new Float(score));
			else if(st.getType() == Scoretype.SCORE_TYPE_TEMP)
				summary.setScore(summary.getScore() + new Float(score));
			else if(st.getType() == Scoretype.SCORE_TYPE_PERFORMENCE)
				summary.setPerformancescore(summary.getPerformancescore() + new Float(score));
			else if(st.getType() == Scoretype.SCORE_TYPE_PROJECT)
				summary.setProjectscore(summary.getProjectscore() + new Float(score));
			em.merge(summary);
			em.persist(LoggerAction.updateScoreSummary(record.getCreator(),summary));
		}
	}
	
	/**
	 * 用户是否审核人，需提供工号
	 * @param employee
	 * @return
	 * @throws Exception
	 */
	public boolean isUserApprover(String workerid) throws Exception{
		Employee ep = (Employee) em.createQuery("SELECT q FROM Employee q WHERE workerid=?").setParameter(1, workerid).getSingleResult();
		try{
			Long approvers = (Long) em.createQuery("SELECT count(q) FROM Scoreapprover q WHERE q.approver=:emp AND q.isapprover='"+Scoreapprover.APPROVER+"'")
					.setParameter("emp", ep).getSingleResult();
//			System.out.println("approver rights size:"+approvers);
			if(approvers > 0)
				return true;
			else
				return false;
		}catch(Exception e){
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * Remove score record given the id of record
	 * @param user
	 * @param recordId
	 * @throws Exception
	 */
	@Transactional(rollbackFor=Exception.class)
	public void removeScoreTypeToScoreMember(Account user, String recordId) throws Exception{
		Scorerecord record = em.find(Scorerecord.class, Integer.parseInt(recordId));
		if(record.getScoretype().getType() == Scoretype.SCORE_TYPE_TEMP && 
				HRUtil.isDateInCurrentWeek(record.getCreatedate())){
			//如果是临时积分，返回积分到部门总分，因为是当周非配的分值
			addBackDepartmentScores(record);
		}
		addBackMemberScore(record,user);
		ScorerecordRemark srr = record.getRemark();
		em.persist(LoggerAction.removeScoreRecord(user,record));
		em.remove(record);
		if(srr != null){
			em.remove(srr);
		}
	}

	/**
	 * 返回条例的分值
	 * @param record
	 * @param user
	 * @throws Exception
	 */
	private void addBackMemberScore(Scorerecord record, Account user) throws Exception{
		Scoremember member = record.getReceiver();
		Float score = record.getScore();
		
		Scoresummary summary  =  this.getScoreSummary(record.getReceiver(), record.getScoredate());
		summary = takeAwayScore(record, summary, score);
		em.merge(summary);
		em.persist(LoggerAction.updateScoreSummary(user,summary));
		member.setHistorytotal(member.getHistorytotal() - new Float(score));
		em.merge(member);
	}

	private Scoresummary takeAwayScore(Scorerecord record,
			Scoresummary summary, Float score) throws Exception{
		if(record.getScoretype().getType() == Scoretype.SCORE_TYPE_FIX)
			summary.setFixscore(summary.getFixscore() - new Float(score));
		else if(record.getScoretype().getType() == Scoretype.SCORE_TYPE_TEMP)
			summary.setScore(summary.getScore() - new Float(score));
		else if(record.getScoretype().getType() == Scoretype.SCORE_TYPE_PERFORMENCE)
			summary.setPerformancescore(summary.getPerformancescore() - new Float(score));
		else if(record.getScoretype().getType() == Scoretype.SCORE_TYPE_PROJECT)
			summary.setProjectscore(summary.getProjectscore() - new Float(score));
		return summary;
	}
	
	private Scoresummary addSummaryScore(Scorerecord record,
			Scoresummary summary, Float score) throws Exception{
		if(record.getScoretype().getType() == Scoretype.SCORE_TYPE_FIX)
			summary.setFixscore(summary.getFixscore() + new Float(score));
		else if(record.getScoretype().getType() == Scoretype.SCORE_TYPE_TEMP)
			summary.setScore(summary.getScore() + new Float(score));
		else if(record.getScoretype().getType() == Scoretype.SCORE_TYPE_PERFORMENCE)
			summary.setPerformancescore(summary.getPerformancescore() + new Float(score));
		else if(record.getScoretype().getType() == Scoretype.SCORE_TYPE_PROJECT)
			summary.setProjectscore(summary.getProjectscore() + new Float(score));
		return summary;
	}

	/**
	 * 返回部门总积分
	 * @param record
	 * @throws Exception
	 */
	private void addBackDepartmentScores(Scorerecord record) throws Exception{
		ScoreExceptionList excep = null;
		try{
			excep = (ScoreExceptionList) em.createQuery("SELECT q FROM ScoreExceptionList q WHERE q.position.id=?")
				.setParameter(1, record.getReceiver().getEmployee().getPosition().getId()).getSingleResult();
		}catch(Exception e){
			System.out.println("Exception not found for position."+e.getMessage());
			excep = null;
		}
		if(excep != null && excep.getStatus() == ScoreExceptionList.NO_UPPER_SCORE_LIMIT){
			return;
		}
		ScoreDivGroup sdg = getScoreDivGroupByWorkerId(record.getReceiver().getEmployee().getWorkerid());
		sdg.setAvailable(sdg.getAvailable()+record.getScore());
		sdg.setLastUpdateDate(Calendar.getInstance().getTime());
		em.merge(sdg);
	}

	/**
	 * Create the scoremember from existing employee, must have an
	 * employee profile
	 * @param employee
	 * @throws Exception
	 */
	@Transactional
	public void createScoreMember(Account user, Employee employee) throws Exception {
		Employee e = (Employee) em.createQuery("SELECT q FROM Employee q WHERE fullname=? AND workerid=?").setParameter(1, employee.getFullname())
				.setParameter(2, employee.getWorkerid()).getSingleResult();
		Scoremember member = new Scoremember();
		member.setEmployee(e);
		member.setHistorytotal(0F);
		member.setMonthlyremain(0F);
		member.setMonthlyscore(0F);
		member.setMonthlytotal(0F);
		member.setVoucherscore(0L);
		em.persist(member);
		em.flush();
		em.persist(LoggerAction.createScoreMember(user, member));
	}

	/**
	 * get records for a given member and month
	 * @param member
	 * @param recordDate
	 * @return
	 * @throws Exception
	 */
	public List<Scorerecord> getRecords(Scoremember member, Date createDate, Date endDate) throws Exception{
		Calendar c = Calendar.getInstance();
		c.setTime(createDate);
		List<Scorerecord> sList = new ArrayList<Scorerecord>();
		String query = "SELECT re.id as reid, re.scoredate as resdate, re.createdate as recdate, re.score as rescore, e.workerid recwid, e.fullname as recname, " +
				"			sender.fullname, sender.workerid, re.status as restatus, t.id as tid, t.reference as tref, t.description as tdesp, t.type as ttype, r.remark as remark FROM scorerecord re " +
				" LEFT JOIN employee e ON re.receiverid = e.workerid " +
				" LEFT JOIN employee sender ON sender.workerid=re.senderid "+
				" LEFT JOIN scoretype t ON re.scoretypeid=t.id " +
				" LEFT JOIN scorerecord_remark r ON re.remarkid = r.id "+
				" WHERE  e.workerid='" + member.getEmployee().getWorkerid() +"'" + 
				" AND re.status="+Scorerecord.APPROVED +
				" AND re.createdate BETWEEN '"+HRUtil.parseDateToString(createDate)+"' AND '"+HRUtil.parseDateToString(endDate)+"' " +
				" ORDER BY re.id DESC";
//		System.out.println(query);
		List<Object[]> results  = em.createNativeQuery(query).getResultList();
		
		for(Object[] obj:results){
			Scorerecord r = new Scorerecord();
			r.setId(((Integer)obj[0]).intValue());
			r.setScoredate((Date)obj[1]);
			r.setCreatedate((Date)obj[2]);
			r.setScore(((Float)obj[3]).floatValue());
			r.setReceiver(convertToScoremember((String)obj[4], (String)obj[5], ""));
			r.setSender(convertToScoremember((String)obj[7], (String)obj[6], ""));
			r.setStatus(((Short)obj[8]).intValue());
			r.setScoretype(converToScoretype((Integer)obj[9], (String)obj[10], (String)obj[11]));
			r.getScoretype().setType(((Short)obj[12]).intValue());
			r.setRemark(convertToScorerecordRemark((String)obj[13]));
			sList.add(r);
		}
		
		return sList;
	}

	/**
	 * Get scoretype by reference, return null if not exsit
	 * @param ref
	 * @return
	 * @throws Exception
	 */
	public Scoretype getScoreTypeByReference(String ref) throws Exception{
		try{
			Scoretype st = (Scoretype) em.createQuery("SELECT q FROM Scoretype q WHERE reference=?")
					.setParameter(1, ref).getSingleResult();
			return st;
		}catch(Exception e){
			return null;
		}
	}

	/**
	 * Get score summary from a scoredate and workerid
	 * @param workerid
	 * @param scoredate
	 * @return
	 */
	public Scoresummary getScoreSummary(Scoremember member, Date scoredate){
		try{
			Calendar c = Calendar.getInstance();
			c.setTime(scoredate);
			int year = c.get(Calendar.YEAR);
			int month = c.get(Calendar.MONTH)+1;
			return (Scoresummary) em.createQuery("SELECT q FROM Scoresummary q WHERE workerid='"+member.getEmployee().getWorkerid()+"' AND EXTRACT(month FROM date)=? AND EXTRACT(year FROM date)=?")
			.setParameter(1, month).setParameter(2, year)
			.getSingleResult();
		}catch(Exception e){
			e.printStackTrace();
			return null;
		}
	}

	/**
	 *  Get score rank directly, returns a virtual table ScoreMemberRank data.<br/>
	 *   List<ScoreMemberRank>
	 * @param pagenum
	 * @param lotsize
	 * @param statement
	 * @return
	 * @throws Exception
	 */
	public Map getSummaryByStatement(int pagenum, int lotsize, String statement) throws Exception{
		Map ret = new HashMap<String,Object>();
		List<ScoreMemberRank> list = null;
		list = em.createNativeQuery(statement,ScoreMemberRank.class).getResultList();
		ret.put("list", list);
		ret.put("count", list.size());
		return ret;
	}

	/**
	 * Get the selected sheet infomation by its sheet id
	 * @param selectedSheet
	 * @return
	 */
	public Scoresheets getScoreSheetById(Integer selectedSheet) {
		try{
			return em.find(Scoresheets.class, selectedSheet);
		}catch(Exception e){
			return null;
		}
	}

	/**
	 * Get all score sheets available in database
	 * @return
	 */
	public List<Scoresheets> getAllScoreSheets() throws Exception{
		return em.createQuery("SELECT q FROM Scoresheets q ORDER BY q.name").getResultList();
	}

	/**
	 * Create new score sheet and log, check sheet name existence first
	 * @param user
	 * @param sheet
	 * @throws Exception
	 */
	@Transactional
	public void createScoreSheet(Account user, Scoresheets sheet) throws Exception{
		if(isScoreSheetNameExist(sheet.getName()))
			return;
		em.persist(sheet);
		em.flush();
		em.persist(LoggerAction.createScoreSheet(user, sheet));
	}
	
	/**
	 * CHeck whether a score sheet name exsit
	 * @param name
	 * @return
	 */
	public boolean isScoreSheetNameExist(String name){
		try{
			List<Scoresheets> s = (List<Scoresheets>) em.createQuery("SELECT q FROM Scoresheets q WHERE name=?")
					.setParameter(1, name).getResultList();
			if(s.size() > 0)
				return true;
			else
				return false;
		}catch(Exception e){
			return false;
		}
	}

	/**
	 * Delete a score sheet and log
	 * @param user
	 * @param selectedSheet
	 * @throws Exception
	 */
	@Transactional
	public void removeScoreSheet(Account user, Integer selectedSheet) throws Exception{
		Scoresheets s = em.find(Scoresheets.class, selectedSheet);
		em.persist(LoggerAction.removeScoreSheet(user,s));
		em.remove(s);
	}

	/**
	 * remove score type from sheet, given both ids
	 * @param user
	 * @param parseInt
	 * @param id
	 */
	@Transactional
	public void removeScoreTypeFromSheet(Account user, Integer sheetId, Integer scoretypeId) throws Exception{
		Scoresheets sheet = em.find(Scoresheets.class, sheetId);
		Scoretype st = em.find(Scoretype.class, scoretypeId);
		Scoresheetmapper mapper = (Scoresheetmapper) em.createQuery("SELECT q FROM Scoresheetmapper q WHERE sheetid =:sheet AND scoretypeid=:st")
				.setParameter("sheet", sheet).setParameter("st", st).getSingleResult();
		em.persist(LoggerAction.removeScoreTypeFromSheet(user,sheet,st));
		em.remove(mapper);
	}

	/**
	 * CHeck if the score type already assign to sheet provided
	 * @param scoretypeId
	 * @param sheetId
	 * @return
	 */
	public boolean isScoretypeExistForSheet(Integer scoretypeId, int sheetId){
		try{
			Scoresheets sheet = em.find(Scoresheets.class, sheetId);
			Scoretype st = em.find(Scoretype.class, scoretypeId);
			Scoresheetmapper mapper = (Scoresheetmapper) em.createQuery("SELECT q FROM Scoresheetmapper q WHERE sheetid =:sheet AND scoretypeid=:st")
					.setParameter("sheet", sheet).setParameter("st", st).getSingleResult();
			if(mapper != null)
				return true;
			return false;
		}catch(Exception e){
			return false;
		}
	}

	/**
	 * Assign score type to a score sheet
	 * @param user
	 * @param id
	 * @param itemlist
	 */
	@Transactional
	public void assignScoreTypeToSheet(Account user, Integer scoretypeId, int sheetId) throws Exception{
		Scoresheets sheet = em.find(Scoresheets.class, sheetId);
		Scoretype st = em.find(Scoretype.class, scoretypeId);
		Scoresheetmapper mapper = new Scoresheetmapper();
		mapper.setSheet(sheet);
		mapper.setType(st);
		em.persist(mapper);
		em.flush();
		em.persist(LoggerAction.assignScoreTypeToSheet(user, st, sheet));
	}

	/**
	 * Get positions belongs to given group
	 * @return
	 */
	public List<Position> getGroupedPositions(Integer groupid) throws Exception {
		List<Positiongroup> posg = em.createQuery("SELECT q FROM Positiongroup q WHERE" +
				" scoregroupid=? ORDER BY q.position.name").setParameter(1, groupid).getResultList();
		List<Position> pos = new ArrayList<Position>();
		for(Positiongroup g:posg){
			pos.add(g.getPosition());
		}
		return pos;
	}

	/**
	 * Create score group
	 * @param group
	 * @throws Exception
	 */
	@Transactional
	public void saveGroup(Account user, Scoregroup group) throws Exception{
		em.persist(group);
		em.flush();
		em.persist(LoggerAction.createScoreGroup(user, group));
	}

	/**
	 * 
	 * @param positionid
	 * @param groupSelected
	 * @return
	 */
	public boolean isGroupedPositionExist(Integer positionid, Integer groupSelected) throws Exception {
		try{
			Positiongroup pg = (Positiongroup) em.createQuery("SELECT q FROM Positiongroup q WHERE " +
					" scoregroupid=? AND positionid=?").setParameter(1, groupSelected).setParameter(2, positionid).getSingleResult();
			if(pg != null)
				return true;
			else
				return false;
		}catch(Exception e){
			return false;
		}
	}

	/**
	 * 
	 * @param i
	 * @param groupSelected
	 */
	@Transactional
	public void assignPosToGroup(Account user, Integer posid, Integer groupSelected) throws Exception{
			Position p = em.find(Position.class, posid);
			Scoregroup sg = em.find(Scoregroup.class, groupSelected);
			Positiongroup pg = new Positiongroup();
			pg.setPosition(p);
			pg.setScoreGroup(sg);
			em.persist(pg);
			em.flush();
			em.persist(LoggerAction.assginToScoreGroup(user, pg));
	}

	/**
	 * 
	 * @param user
	 * @param posid
	 * @param groupSelected
	 * @throws Exception
	 */
	@Transactional
	public void quitPosFromGroup(Account user, Integer posid, Integer groupSelected) throws Exception{
		Position p = em.find(Position.class, posid);
		Scoregroup sg = em.find(Scoregroup.class, groupSelected);
		Positiongroup pg = (Positiongroup) em.createQuery("SELECT q FROM Positiongroup q WHERE " +
				" q.position=:p AND q.scoreGroup=:sg").setParameter("p", p).setParameter("sg",sg).getSingleResult();
		em.persist(LoggerAction.quitScoreGroup(user, pg));
		em.remove(pg);
	}

	/**
	 * 返回所有的排位组
	 * @return
	 */
	public List<Scoregroup> getAllScoreGroup() throws Exception{
		List<Scoregroup> list = em.createQuery("SELECT q FROM Scoregroup q ORDER BY q.id").getResultList();
		return list;
	}

	/**
	 * Edit the group information for created group
	 * @param user
	 * @param groupSelected
	 * @param editGroup
	 */
	@Transactional
	public void editGroup(Account user, Integer groupSelected,
			Scoregroup editGroup) throws Exception{
		Scoregroup sg = em.find(Scoregroup.class, groupSelected);
		if(editGroup.getName() != null && editGroup.getName().trim() != ""){
			sg.setName(editGroup.getName());
		}
		if(editGroup.getRemark() != null && editGroup.getRemark().trim() != ""){
			sg.setRemark(editGroup.getRemark());
		}
		em.merge(sg);
		em.persist(LoggerAction.editScoreGroupDetail(user, sg));
	}

	/**
	 * Remove the score group, only if no positions are assigned
	 * @param user
	 * @param groupSelected
	 */
	@Transactional
	public void removeScoreGroup(Account user, Integer groupSelected) throws Exception{
		Scoregroup sg = em.find(Scoregroup.class, groupSelected);
		em.persist(LoggerAction.removeScoreGroup(user,sg));
		em.remove(sg);
	}

	/**
	 * Get vouchers from company and make records
	 * @param user
	 * @param workerid
	 * @param vl
	 */
	@Transactional
	public void giveVouchers(Account user, Voucherlist vl) throws Exception{
		Scoremember sm = vl.getScoremember();
		sm.setVoucherscore(sm.getVoucherscore() + new Long(vl.getScore()));
		em.merge(sm);
		em.persist(vl);
		em.flush();
		em.persist(LoggerAction.giveVoucher(user,vl));
	}

	/**
	 * Get voucher records by worker's workerId
	 * @param filterworkerid
	 * @return
	 */
	public List<Voucherlist> getVouchersByWorkerId(String filterworkerid) throws Exception {
		return em.createQuery("SELECT q FROM Voucherlist q WHERE workerid=? ORDER BY date DESC")
				.setParameter(1, filterworkerid).getResultList();
	}

	/**
	 * Get voucher records by worker's name
	 * @param filtername
	 * @return
	 */
	public List<Voucherlist> getVouchersByName(String filtername) throws Exception{
		return em.createQuery("SELECT q FROM Voucherlist q WHERE q.scoremember.employee.fullname LIKE '%"+filtername+"%' ORDER BY date DESC")
				.getResultList();
	}

	/**
	 * Delete voucher record given the voucher record id
	 * @param targetId
	 * @throws Exception
	 */
	@Transactional
	public void deleteVoucherRecord(Account user, Integer targetId) throws Exception{
		Voucherlist vl = em.find(Voucherlist.class, targetId);
		Scoremember sm = vl.getScoremember();
		sm.setVoucherscore(sm.getVoucherscore() - new Long(vl.getScore()));
		em.merge(sm);
		em.persist(LoggerAction.deleteVoucherRecord(user, vl));
		em.remove(vl);
	}

	/**
	 * 添加新的审核人到积分组
	 * @param newApprover
	 * @param user
	 * @throws Exception
	 */
	@Transactional(rollbackFor=Exception.class)
	public void addApprover(Scoreapprover newApprover, Account user) throws Exception{
		try{
			if(newApprover.getApprover().getWorkerid() != null){
//				System.out.println(newApprover.getApprover().getWorkerid());
				Employee e = (Employee) em.createQuery("SELECT q FROM Employee q WHERE workerid=?")
						.setParameter(1, newApprover.getApprover().getWorkerid()).getSingleResult();
				newApprover.setApprover(e);
				if(!isApproverExist(newApprover)){
					em.persist(newApprover);
					em.flush();
					em.persist(LoggerAction.addApprover(user, newApprover));
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	/**
	 * 检查审核人有没有已经分配积分组
	 * @param approver
	 * @return
	 */
	public boolean isApproverExist(Scoreapprover approver) {
		try{
			List<Scoreapprover> sas = null;
			sas = em.createQuery("SELECT q FROM Scoreapprover q WHERE q.approver.id=? AND q.group.id=?")
						.setParameter(1, approver.getApprover().getId())
						.setParameter(2, approver.getGroup().getId())
						.getResultList();
			if(sas != null && sas.size()>0){
				return true;
			}else
				return false;
		}catch(Exception e){
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * 获取审核人的管核范围
	 * @param e
	 * @return
	 */
	public List<Scoreapprover> getApproverSections(Employee e) throws Exception{
		return em.createQuery("SELECT q FROM Scoreapprover q WHERE q.approver.id="+e.getId() +" ORDER BY id ASC").getResultList();
	}

	/**
	 * 删除审核人被允许审核的部门或者车队的记录
	 * @param targetId
	 * @param user
	 * @throws Exception
	 */
	@Transactional
	public void delApproverSection(String targetId,Account user) throws Exception{
		Scoreapprover sa = em.find(Scoreapprover.class,Integer.parseInt(targetId));
		ScoreLog sl = new ScoreLog();
		sl.setAction(ScoreLog.DELETE);
		sl.setCreatetime(Calendar.getInstance().getTime());
		sl.setRecordid(sa.getId()+"");
		sl.setWho(user);
		sl.setRemark("删除了"+sa.getClass().getName());
		em.persist(sl);
		em.remove(sa);
	}

	/**
	 * 更改审核人状态，只修改是否为改部门的审核人
	 * @param targetId
	 * @param user
	 */
	@Transactional
	public void toggleApproverStatus(String targetId, Account user) {
		Scoreapprover sa = em.find(Scoreapprover.class,Integer.parseInt(targetId));
		if(sa.getIsapprover() == null){
			sa.setIsapprover("N");
		}else if(sa.getIsapprover().equals("N")){
			sa.setIsapprover("Y");
		}else if(sa.getIsapprover().equals("Y")){
			sa.setIsapprover("N");
		}
		em.merge(sa);
		em.flush();
		ScoreLog sl = new ScoreLog();
		sl.setAction(ScoreLog.UPDATE);
		sl.setRecordid(sa.getId()+"");
		sl.setWho(user);
		sl.setRemark("更改了"+sa.getClass().getName()+" ID"+sa.getId()+" 的状态到"+sa.getIsapprover());
		sl.setCreatetime(Calendar.getInstance().getTime());
		em.persist(sl);
	}

	/**
	 * 获取审核人名单，主要是看名单和管核部门的统计
	 * @return
	 */
	public List<Scoreapprover> getDistinctApprovers() throws Exception{
		return em.createQuery("SELECT q FROM Scoreapprover q WHERE q.approver.status='A' ORDER BY q.approver.fullname").getResultList();
	}

	/**
	 * 计数 等待审核的条例数目
	 * @param user
	 * @return
	 */
	public Long countWaittingApprove(Account user) throws Exception{
		Employee e = (Employee) em.createQuery("SELECT q FROM Employee q WHERE q."
				+ "workerid=?").setParameter(1,user.getEmployee()).getSingleResult();
		String query = "SELECT count(distinct re.id) FROM scorerecord re LEFT JOIN employee e ON re.receiverid = e.workerid " +
				" LEFT JOIN score_group_mapper m ON e.id=m.empid " +
				" WHERE m.scoregroupid " + 
				" IN (SELECT scoregroupid FROM scoreapprover WHERE approver=" + e.getId()+") AND re.status="+Scorerecord.WAITING;
//		System.out.println(query);
		BigInteger depart =  (BigInteger) em.createNativeQuery(query).getSingleResult();
		return depart.longValue();
	}

	/**
	 * 获取 某周期内的审核条例详细资料，仅限指定审核人的条例
	 * @param user
	 * @param selectPeriod
	 * @return
	 */
	public List<Scorerecord> getWaittingApprove(Account user,
			String selectPeriod) throws Exception{
		Employee e = (Employee) em.createQuery("SELECT q FROM Employee q WHERE workerid=?").setParameter(1,user.getEmployee()).getSingleResult();
		List<Scorerecord> records = new ArrayList<Scorerecord>();
		Calendar cal = Calendar.getInstance();
		Calendar cal2 = Calendar.getInstance();
		if(selectPeriod == null)
			selectPeriod = "w"; //Default display week records
		if(selectPeriod.equals("w".toLowerCase())){
			cal.set(Calendar.DAY_OF_WEEK,cal.getFirstDayOfWeek());
			cal2.setTime(cal.getTime());
			cal2.add(Calendar.WEEK_OF_YEAR, 1);
		}else if(selectPeriod.equals("m".toLowerCase())){
			cal.set(Calendar.DAY_OF_MONTH, 1);
			cal2.setTime(cal.getTime());
			cal2.add(Calendar.MONTH, 1);
		}else if(selectPeriod.equals("a".toLowerCase())){
			cal.setTime(HRUtil.parseDate("2012-11-11", "yyyy-MM-dd"));
			cal2.setTime(Calendar.getInstance().getTime());
		}
		String query = "SELECT re.id as reid, re.scoredate as resdate, re.createdate as recdate, re.score as rescore, e.workerid recwid, e.fullname as recname, p.name as pname, " +
				"			sender.workerid as senderwid, sender.fullname as sendername,re.status as restatus, t.id as tid, t.reference as tref, t.description as tdesp FROM scorerecord re " +
				" LEFT JOIN employee e ON re.receiverid = e.workerid " +
				" LEFT JOIN employee sender ON re.senderid = sender.workerid " + 
				" LEFT JOIN position p ON p.id=e.positionid " + 
				" LEFT JOIN scoretype t ON re.scoretypeid=t.id " +
				" LEFT JOIN score_group_mapper m ON e.id=m.empid " +
				" WHERE m.scoregroupid " + 
				" IN (SELECT scoregroupid FROM scoreapprover WHERE approver=" + e.getId()+") AND re.status="+Scorerecord.WAITING +
				" AND re.createdate BETWEEN '"+ HRUtil.parseDateToString(cal.getTime())+"' AND '" +  HRUtil.parseDateToString(cal2.getTime()) + "' " +
				" ORDER BY re.id DESC";
//		System.out.println(query);
		List<Object[]> results = em.createNativeQuery(query).getResultList();
		for(Object[] obj:results){
			Scorerecord r = new Scorerecord();
			r.setId(((Integer)obj[0]).intValue());
			r.setScoredate((Date)obj[1]);
			r.setCreatedate((Date)obj[2]);
			r.setScore(((Float)obj[3]).floatValue());
			r.setReceiver(convertToScoremember((String)obj[4], (String)obj[5], (String)obj[6]));
			r.setSender(convertToScoremember((String)obj[7], (String)obj[8], ""));
			r.setStatus(((Short)obj[9]).intValue());
			r.setScoretype(converToScoretype((Integer)obj[10], (String)obj[11], (String)obj[12]));
			records.add(r);
		}
		return records;
	}

	/**
	 * 审核已经提交的积分项目
	 * @param selected
	 * @param user
	 * @throws Exception
	 */
	@Transactional(rollbackFor=Exception.class)
	public void approveScoreRecords(List<String> selected, Account user) throws Exception{
		boolean D = true;
		String approveIds = "";
		for(String id:selected){
			if(id!= null)
				approveIds += id+",";
		}
		approveIds = approveIds.substring(0,approveIds.length()-1);
		
		List<Scorerecord> sList = new ArrayList<Scorerecord>();
		Map<String,Scoresummary> sMap = new HashMap<String, Scoresummary>();
		List<Scoresummary> sums = new ArrayList<Scoresummary>();
		String query = "SELECT re.id as reid, re.status as restatus, re.score as rescore, sm.id as smid, sm.historytotal as smtotal, sm.workerid as smw,"+
				" st.id as stid, st.reference as stref, st.type as sttype, st.score as stscore, e.id as eid, re.createdate as recdate" + //6
		" FROM scorerecord re LEFT JOIN scoretype st ON re.scoretypeid=st.id " + 
				" LEFT JOIN scoremember sm ON re.receiverid=sm.workerid "+
				" LEFT JOIN employee e ON e.workerid=re.receiverid " +
		" WHERE re.id IN("+approveIds+")";
		if(D) System.out.println(query);
		List<Object[]> result  = em.createNativeQuery(query).getResultList();
		for(Object[] obj:result){
			Scoremember m = new Scoremember();
			m.setId((Integer)obj[3]);
			m.setHistorytotal((Float) obj[4]);
			Employee e = new Employee();
			e.setId((Integer) obj[10]);
			e.setWorkerid((String) obj[5]);
			m.setEmployee(e);
			
			Scorerecord s = new Scorerecord();
			s.setId((Integer)obj[0]);
			s.setStatus(((Short)obj[1]).intValue());
			s.setScore((Float) obj[2]);
			s.setReceiver(m);
			s.setCreatedate((Date) obj[11]);
			
			Scoretype st = new Scoretype();
			st.setId((Integer) obj[6]);
			st.setReference((String) obj[7]);
			st.setType(((Short)obj[8]).intValue());
			st.setScore((Float) obj[9]);
			s.setScoretype(st);
			sList.add(s);
		}
		
		query = "SELECT sum.id as sumid, sum.workerid as sumwid, sum.date as sumdate, sum.score as sumscore, sum.fixscore as sumfscore, sum.performancescore as sumpscore, sum.projectscore as sumpjscore"+
				" FROM scorerecord re LEFT JOIN scoresummary sum ON re.receiverid=sum.workerid " +
				" WHERE EXTRACT(month FROM re.createdate)=EXTRACT(month FROM sum.date) AND EXTRACT(year FROM re.createdate)=EXTRACT(year FROM sum.date)" +
				" AND re.id IN("+approveIds+")";
		if(D) System.out.println(query);
		result = em.createNativeQuery(query).getResultList();
		for(Object[] obj:result){
			if(sMap.get((String)obj[1]) == null){
				Scoresummary ssum = new Scoresummary();
				ssum.setId((Integer) obj[0]);
				ssum.setDate((Date) obj[2]);
				ssum.setScore((Float) obj[3]);
				ssum.setFixscore((Float) obj[4]);
				ssum.setPerformancescore((Float) obj[5]);
				ssum.setProjectscore((Float) obj[6]);
				Employee e = new Employee();
				e.setWorkerid((String)obj[1]);
				ssum.setEmployee(e);
				sMap.put(e.getWorkerid(), ssum);
			}
		}
		
		String memberQuery = "";
		String summaryQuery = "";
		String approveQuery="";
		for(Scorerecord re:sList){
			re.getReceiver().setHistorytotal(re.getReceiver().getHistorytotal()+re.getScore());
			memberQuery += "(" + re.getReceiver().getId()+ "," + re.getReceiver().getHistorytotal()+"),";
			Scoresummary summary  = sMap.get(re.getReceiver().getEmployee().getWorkerid());
			if(summary == null){
				summary  = new Scoresummary();
				summary.setEmployee(re.getReceiver().getEmployee());
				summary.setDate(re.getCreatedate());
			}
			summary = addSummaryScore(re, summary, re.getScore());
			if(summary.getId() == null){
				sums.add(summary);
			}else{
				summaryQuery += "("+summary.getId()+","+summary.getScore()+","+summary.getFixscore()+","+summary.getPerformancescore()+","+summary.getProjectscore()+"),";
			}
			approveQuery  += "("+re.getId()+","+Scorerecord.APPROVED+"),";
		}
		
		//add Member score
		if(memberQuery.length() > 0){
			query = "UPDATE scoremember as sm SET historytotal=tem.total FROM ("+
					" VALUES "+memberQuery.substring(0,memberQuery.length()-1) + ") AS tem(id,total) WHERE sm.id=tem.id";
			if(D) System.out.println(query);
			em.createNativeQuery(query).executeUpdate();
		}
		
		//add Summary
		if(summaryQuery.length() > 0){
			query = "UPDATE scoresummary AS sm SET score=tem.score, fixscore=tem.fixscore, performancescore=tem.performancescore,projectscore=tem.projectscore FROM (" +
					" VALUES "+ summaryQuery.substring(0,summaryQuery.length()-1) +") AS tem(id,score,fixscore,performancescore,projectscore) WHERE sm.id=tem.id";
			if(D) System.out.println(query);
			em.createNativeQuery(query).executeUpdate();
		}
		if(sums.size() > 0){
			if(D) System.out.println("Create "+sums.size()+ " new Summarys...");
			for(Scoresummary sum:sums){
				em.persist(sum);
			}
		}
		
		//approve records
		if(approveQuery.length()>0){
			query = "UPDATE scorerecord AS s SET status=tem.status FROM (" +
					" VALUES "+ approveQuery.substring(0,approveQuery.length()-1) +") AS tem(id,status) WHERE s.id=tem.id";
			if(D) System.out.println(query);
			em.createNativeQuery(query).executeUpdate();
		}
		
		ScoreLog sl = new ScoreLog();
		sl.setAction(ScoreLog.UPDATE);
		sl.setCreatetime(Calendar.getInstance().getTime());
		sl.setWho(user);
		sl.setRemark("工号:"+user.getEmployee()+"审核了"+selected.size()+"条项目");
		em.persist(sl);
	}

	/**
	 * 拒绝不合格的积分项目
	 * @param selected
	 * @param user
	 * @throws Exception
	 */
	@Transactional
	public void rejectScoreRecords(List<String> selected, Account user) throws Exception{
		for(String cur:selected){
			Scorerecord se = em.find(Scorerecord.class, Integer.parseInt(cur));
			se.setStatus(Scorerecord.REJECTED);
			em.merge(se);
		}
		em.flush();
		ScoreLog sl = new ScoreLog();
		sl.setAction(ScoreLog.UPDATE);
		sl.setCreatetime(Calendar.getInstance().getTime());
		sl.setWho(user);
		sl.setRemark("工号:"+user.getEmployee()+"拒绝了"+selected.size()+"条项目");
		em.persist(sl);
	}

	/**
	 * 统计用户创建的有多少条项目是正在等待审批的
	 * @param user
	 * @return
	 * @throws Exception
	 */
	public Long countWaitingApproveRecords(Account user) throws Exception {
		BigInteger depart =  (BigInteger) em.createNativeQuery("SELECT count(distinct re.id) FROM scorerecord re WHERE status=? AND creator = " + user.getId())
				.setParameter(1, Scorerecord.WAITING).getSingleResult();
		return depart.longValue();
	}

	/**
	 * 统计用户创建的有多少条项目是被拒绝的
	 * @param user
	 * @return
	 * @throws Exception
	 */
	public Long countRejectedRecords(Account user) throws Exception{
		BigInteger depart =  (BigInteger) em.createNativeQuery("SELECT count(distinct re.id) FROM scorerecord re WHERE status=? AND creator = " + user.getId())
				.setParameter(1, Scorerecord.REJECTED).getSingleResult();
		return depart.longValue();
	}
	
	/**
	 * 统计用户创建的有多少条项目是未提交审核的
	 * @param user
	 * @return
	 * @throws Exception
	 */
	public Long countCreatedRecords(Account user) throws Exception{
		BigInteger depart =  (BigInteger) em.createNativeQuery("SELECT count(distinct re.id) FROM scorerecord re WHERE status=? AND creator = " + user.getId())
				.setParameter(1, Scorerecord.CREATED).getSingleResult();
		return depart.longValue();
	}

	/**
	 * 获取用户创建的正在审核的项目
	 * @param user
	 * @return
	 * @throws Exception
	 */
	public List<Scorerecord> getWaitingApproveRecords(Account user) throws Exception{
		String query = "SELECT re.id as reid, re.scoredate as resdate, re.createdate as recdate, re.score as rescore, e.workerid recwid, e.fullname as recname, p.name as pname, " +
				"			sender.workerid as senderwid, sender.fullname as sendername,re.status as restatus, t.id as tid, t.reference as tref, t.description as tdesp FROM scorerecord re " +
				" LEFT JOIN employee e ON re.receiverid = e.workerid " +
				" LEFT JOIN employee sender ON re.senderid = sender.workerid " + 
				" LEFT JOIN position p ON p.id=e.positionid " + 
				" LEFT JOIN scoretype t ON re.scoretypeid=t.id " +
				" WHERE re.status="+Scorerecord.WAITING +
				" AND re.creator="+user.getId() + 
				" ORDER BY re.id DESC";
//		List<Scorerecord> records = em.createQuery("SELECT q FROM Scorerecord q WHERE q.creator.id=? AND q.status=? ORDER BY q.id DESC")
//				.setParameter(1, user.getId()).setParameter(2, Scorerecord.WAITING).getResultList();
		List<Object[]> result = em.createNativeQuery(query).getResultList();
		List<Scorerecord> sList = new ArrayList<Scorerecord>();
		
		for(Object[] obj:result){
			Scorerecord r = new Scorerecord();
			r.setId(((Integer)obj[0]).intValue());
			r.setScoredate((Date)obj[1]);
			r.setCreatedate((Date)obj[2]);
			r.setScore(((Float)obj[3]).floatValue());
			r.setReceiver(convertToScoremember((String)obj[4], (String)obj[5], (String)obj[6]));
			r.setSender(convertToScoremember((String)obj[7], (String)obj[8], ""));
			r.setStatus(((Short)obj[9]).intValue());
			r.setScoretype(converToScoretype((Integer)obj[10], (String)obj[11], (String)obj[12]));
			sList.add(r);
		}
		return sList;
	}

	/**
	 * 获取用户创建的拒绝了的项目
	 * @param user
	 * @return
	 * @throws Exception
	 */
	public List<Scorerecord> getRejectedRecords(Account user) throws Exception{
		List<Scorerecord> records = em.createQuery("SELECT q FROM Scorerecord q WHERE q.creator.id=? AND q.status=? ORDER BY q.id DESC")
				.setParameter(1, user.getId()).setParameter(2, Scorerecord.REJECTED).getResultList();
		return records;
	}
	
	/**
	 * 获取用户创建的未提交的项目
	 * @param user
	 * @return
	 * @throws Exception
	 */
	public List<Scorerecord> getCreatedRecords(Account user) throws Exception{
		List<Scorerecord> records = em.createQuery("SELECT q FROM Scorerecord q WHERE q.creator.id=? AND q.status=? ORDER BY q.id DESC")
				.setParameter(1, user.getId()).setParameter(2, Scorerecord.CREATED).getResultList();
		return records;
	}

	/**
	 * 删除审核项目
	 * @param selected
	 * @param user
	 * @throws Exception
	 */
	@Transactional(rollbackFor=Exception.class)
	public void deleteScoreRecords(List<String> selected, Account user) throws Exception{
		System.out.println("delete "+ selected.size()+ " records.");
		String delIds = "";
		//获取要删除的IDs
		for(String cur:selected){
			if(cur != null)
				delIds += cur+",";
		}
		delIds = delIds.substring(0,delIds.length()-1);
		
		
		HashMap<Integer,ScoreDivGroup> dMap = new HashMap<Integer,ScoreDivGroup>();
		Map<String,Scoresummary> sMap = new HashMap<String,Scoresummary>();
		HashMap<Integer,ScoreDivGroup> updateddMap = new HashMap<Integer,ScoreDivGroup>();
		
		//Cache要删除的记录信息 department,scoretype, score,position, scoreexceptionlist,member history,remark
		String query = "SELECT re.id as reid, re.createdate as recreatedate,re.score as rescore, re.status as restatus,re.remarkid as reremarkid, excep.status as excepstatus," +
				" sm.id as smid, sm.workerid as smworkerid, sm.historytotal as smhistorytotal," + //6
				" sg.id as sgid, sg.available as sgavailable," + //9
				" st.id as stid, st.reference as stref, st.type as sttype, st.description as stdes" +//11
		" FROM scorerecord re LEFT JOIN scoretype st ON re.scoretypeid=st.id" +
				" LEFT JOIN scorerecord_remark remark ON remark.id=re.remarkid "+
				" LEFT JOIN employee rec ON re.receiverid=rec.workerid " +
				" LEFT JOIN position p ON rec.positionid=p.id "+
				" LEFT JOIN score_group_mapper m ON rec.id=m.empid "+
				" LEFT JOIN score_group sg ON sg.id=m.scoregroupid "+
				" LEFT JOIN scoreexceptionlist excep ON p.id=excep.positionid "+
				" LEFT JOIN scoremember sm ON re.receiverid=sm.workerid "+
		" WHERE re.id IN("+delIds+")";
		System.out.println(query);
		List<Scorerecord> sList = new ArrayList<Scorerecord>();
		List<Object[]> result = em.createNativeQuery(query).getResultList();
		for(Object[] obj:result){
			Scorerecord r = new Scorerecord();
			r.setId((Integer) obj[0]);
			r.setCreatedate((Date) obj[1]);
			r.setScore((Float) obj[2]);
			r.setStatus(((Short) obj[3]).intValue());
			ScorerecordRemark srr = new ScorerecordRemark();
			srr.setId((Integer) obj[4]);
			r.setRemark(srr);
			if(obj[5] == null)
				r.setExcepStatus(ScoreExceptionList.HAS_UPPER_SCORE_LIMIT);
			else
				r.setExcepStatus((Integer) obj[5]);
			
			Scoretype st = converToScoretype((Integer)obj[11], (String)obj[12], (String)obj[14]);
			st.setType(((Short)obj[13]).intValue());
			
			Employee e = new Employee();
			Scoremember s = new Scoremember();
			e.setWorkerid((String) obj[7]);
			Department d = new Department();
			d.setId((Integer) obj[9]);
			e.setDepartment(d);
			s.setId((Integer) obj[6]);s.setEmployee(e);s.setHistorytotal((Float) obj[8]);
			
			r.setReceiver(s);
			r.setScoretype(st);
			
			if(dMap.get((Integer)obj[9]) == null){
				ScoreDivGroup sdg = new ScoreDivGroup();
				sdg.setId((Integer) obj[9]);
				sdg.setAvailable((Float) obj[10]);
				dMap.put(sdg.getId(), sdg);
			}
			
			sList.add(r);
		}
		query = "SELECT sum.id as sumid, sum.workerid as sumwid, sum.date as sumdate, sum.score as sumscore, sum.fixscore as sumfscore, sum.performancescore as sumpscore, sum.projectscore as sumpjscore"+
				" FROM scorerecord re LEFT JOIN scoresummary sum ON re.receiverid=sum.workerid " +
				" WHERE EXTRACT(month FROM re.createdate)=EXTRACT(month FROM sum.date) AND EXTRACT(year FROM re.createdate)=EXTRACT(year FROM sum.date)" +
				" AND re.id IN("+delIds+")";
		if(D) System.out.println(query);
		result = em.createNativeQuery(query).getResultList();
		for(Object[] obj:result){
			if(sMap.get((String)obj[1]) == null){
				Scoresummary ssum = new Scoresummary();
				ssum.setId((Integer) obj[0]);
				ssum.setDate((Date) obj[2]);
				ssum.setScore((Float) obj[3]);
				ssum.setFixscore((Float) obj[4]);
				ssum.setPerformancescore((Float) obj[5]);
				ssum.setProjectscore((Float) obj[6]);
				Employee e = new Employee();
				e.setWorkerid((String)obj[1]);
				ssum.setEmployee(e);
				sMap.put(e.getWorkerid(), ssum);
			}
		}
		
		//减去记录所加的分
		String memberUpdateQuery = "";
		String remarkIds = "";
		for(Scorerecord re:sList){
			if(re.getScoretype().getType() ==Scoretype.SCORE_TYPE_TEMP &&
					re.getExcepStatus() == ScoreExceptionList.HAS_UPPER_SCORE_LIMIT
					&& HRUtil.isDateInCurrentWeek(re.getCreatedate())){
				System.out.println(dMap.get(re.getReceiver().getEmployee().getDepartment().getId()).getAvailable());
				dMap.get(re.getReceiver().getEmployee().getDepartment().getId())
				.setAvailable(dMap.get(re.getReceiver().getEmployee().getDepartment().getId()).getAvailable()+re.getScore());
				System.out.println(re.getScore());
				System.out.println(dMap.get(re.getReceiver().getEmployee().getDepartment().getId()).getAvailable());
				updateddMap.put(re.getReceiver().getEmployee().getDepartment().getId(), dMap.get(re.getReceiver().getEmployee().getDepartment().getId()));
				System.out.println(updateddMap.get(re.getReceiver().getEmployee().getDepartment().getId()));
			}
			if(re.getStatus() == Scorerecord.APPROVED){
				Scoresummary sm = sMap.get(re.getReceiver().getEmployee().getWorkerid());
				re.getReceiver().setHistorytotal(re.getReceiver().getHistorytotal()-re.getScore());
				sm = takeAwayScore(re, sm, re.getScore());
				sMap.put(re.getReceiver().getEmployee().getWorkerid(), sm);
			}
			memberUpdateQuery += "("+re.getReceiver().getId()+","+re.getReceiver().getHistorytotal()+"),";
			if(re.getRemark() != null)
				remarkIds += re.getRemark().getId()+",";
		}
		
		//Save updated Scoremember
		query = "UPDATE scoremember as sm SET historytotal=tem.total FROM ("+
		" VALUES "+memberUpdateQuery.substring(0,memberUpdateQuery.length()-1) + ") AS tem(id,total) WHERE sm.id=tem.id";
		System.out.println(query);
		int resultUpdate = em.createNativeQuery(query).executeUpdate();
		//Save updated score_group
		String scoreGroupQueryString="";
		for(ScoreDivGroup sdg:updateddMap.values()){
			scoreGroupQueryString += "("+sdg.getId()+","+sdg.getAvailable()+"),";
		}
		if(scoreGroupQueryString.length()>0){
			query = "UPDATE score_group AS sg SET available=tem.available, lastupdatedate='"+HRUtil.parseDateToString(Calendar.getInstance().getTime())+"' FROM (" +
					" VALUES "+ scoreGroupQueryString.substring(0,scoreGroupQueryString.length()-1) +") AS tem(id,available) WHERE sg.id=tem.id";
			System.out.println(query);
			resultUpdate = em.createNativeQuery(query).executeUpdate();
		}
		//Save updated Scoresummary
		String summaryQuery = "";
		for(Scoresummary sm:sMap.values()){
			summaryQuery += "("+sm.getId()+","+sm.getScore()+","+sm.getFixscore()+","+sm.getPerformancescore()+","+sm.getProjectscore()+"),";
		}
		if(!summaryQuery.trim().equals("")){
			summaryQuery = summaryQuery.substring(0,summaryQuery.length()-1);
			query = "UPDATE scoresummary AS sm SET score=tem.score, fixscore=tem.fixscore, performancescore=tem.performancescore,projectscore=tem.projectscore FROM (" +
				" VALUES "+ summaryQuery +") AS tem(id,score,fixscore,performancescore,projectscore) WHERE sm.id=tem.id";
			System.out.println(query);
			resultUpdate = em.createNativeQuery(query).executeUpdate();
		}
		//Delete records
		query  = "DELETE FROM scorerecord WHERE id IN("+delIds+")";
		System.out.println(query);
		resultUpdate = em.createNativeQuery(query).executeUpdate();
		//Delete remarks
		if(!remarkIds.trim().equals("")){
			remarkIds = remarkIds.substring(0,remarkIds.length()-1);
			query  = "DELETE FROM scorerecord_remark WHERE id IN("+remarkIds+")";
			System.out.println(query);
			resultUpdate = em.createNativeQuery(query).executeUpdate();
		}
		//Log
		ScoreLog sl = new ScoreLog();
		sl.setAction(ScoreLog.DELETE);
		sl.setCreatetime(Calendar.getInstance().getTime());
		sl.setWho(user);
		sl.setRemark("工号:"+user.getEmployee()+"删除了"+selected.size()+"条项目");
		em.persist(sl);
	}

	/**
	 * 撤回审核项目
	 * @param selected
	 * @param user
	 * @throws Exception
	 */
	@Transactional
	public void getBackScoreRecords(List<String> selected, Account user) throws Exception{
		for(String cur:selected){
			Scorerecord se = em.find(Scorerecord.class, Integer.parseInt(cur));
			se.setStatus(Scorerecord.CREATED);
			em.merge(se);
		}
		em.flush();
		ScoreLog sl = new ScoreLog();
		sl.setAction(ScoreLog.UPDATE);
		sl.setCreatetime(Calendar.getInstance().getTime());
		sl.setWho(user);
		sl.setRemark("工号:"+user.getEmployee()+"撤回了"+selected.size()+"条项目");
		em.persist(sl);
	}

	/**
	 * 重新提交审核项目
	 * @param selected
	 * @param user
	 * @throws Exception
	 */
	@Transactional
	public void reSubmitScoreRecords(List<String> selected, Account user) throws Exception{
		for(String cur:selected){
			Scorerecord se = em.find(Scorerecord.class, Integer.parseInt(cur));
			se.setCreatedate(Calendar.getInstance().getTime());
			se.setStatus(Scorerecord.WAITING);
			em.merge(se);
		}
		em.flush();
		ScoreLog sl = new ScoreLog();
		sl.setAction(ScoreLog.UPDATE);
		sl.setCreatetime(Calendar.getInstance().getTime());
		sl.setWho(user);
		sl.setRemark("工号:"+user.getEmployee()+"提交了"+selected.size()+"条项目");
		em.persist(sl);
	}

	/**
	 * 计算现存分值够不够分配<br/>
	 * List[0][0] = 部门名字<br/>
	 * List[0][1] = 扣除后剩余分值
	 * @param totalscore
	 * @param workerids
	 * @return
	 * @throws Exception
	 */
	public List<List<String>> getDepartmentScores(Float totalscore,
			String workerids) throws Exception{
		String query = "SELECT q.name,q.available-count(e)*" + totalscore +" AS result " +
				" FROM score_group q LEFT JOIN score_group_mapper s ON q.id=s.scoregroupid RIGHT JOIN employee e ON e.id=s.empid LEFT JOIN scoreexceptionlist  p ON e.positionid=p.positionid " +
				" WHERE e.workerid IN ("+workerids+") " +
				" AND (p.status IS NULL OR (p.status IS NOT NULL AND p.status=" + ScoreExceptionList.HAS_UPPER_SCORE_LIMIT+ "))  " +
				" GROUP BY q.name,q.available";
		List<Object[]> result = em.createNativeQuery(query).getResultList();
		List<List<String>> res = new ArrayList<List<String>>();
		for(Object[] objs:result){
			List<String> strlist = new ArrayList<String>();
			strlist.add((String)objs[0]);
			strlist.add((Double)objs[1]+"");
			res.add(strlist);
		}
		return res;
	}

	/**
	 * 创建部门基础分和初次分配分值
	 * @param ds
	 * @throws Exception
	 */
	@Transactional
	public void addDepartmentScore(DepartmentScore ds) throws Exception{
		em.persist(ds);
	}
	
	/**
	 * 保存批量打分纪录
	 * @param records
	 * @param user
	 */
	@Transactional(rollbackFor=Exception.class)
	public void saveMassScoresFromList(List<Scorerecord> records, Account user,HRBean hrBean) throws Exception{
		Map<String,Scoremember> memberMap = new HashMap<String,Scoremember>(); //用工号获取member
		Map<String,Integer> memberExceptionState = new HashMap<String,Integer>(); //用工号获取员工是否有积分上限
		Map<Integer,ScoreDivGroup> gMap = new HashMap<Integer,ScoreDivGroup>(); //同组id来获取积分组信息
		Map<Integer,String> approverMap = new HashMap<Integer,String>();//用组id来获取是否有权限打分给组
		Map<String,Scoretype> stMap = new HashMap<String,Scoretype>();//用ref来获取条例信息
		Map<String,Scoresummary> sumMap = new HashMap<String,Scoresummary>();//用工号来获取旧的summary
		Map<Integer,ScorerecordRemark> remarkMap = new LinkedHashMap<Integer,ScorerecordRemark>();//用record的Index来获取remark
		List<Scoresummary> newSum = new ArrayList<Scoresummary>();//用来保存新的summary
		boolean isapprover = false;
		Calendar today = Calendar.getInstance();
		
		//为未有记录的员工创建scoremember记录
		String createNoneExistMembers = "INSERT INTO scoremember (workerid,historytotal) SELECT e.workerid, 0.0 as history FROM score_group_mapper m LEFT JOIN employee e ON m.empid=e.id LEFT JOIN scoremember sm ON e.workerid=sm.workerid WHERE sm.id IS NULL";
		em.createNativeQuery(createNoneExistMembers).executeUpdate();
		
		//更新积分组周积分
		String query = "UPDATE score_group scoreg SET available=tem.available, lastupdatedate='" + HRUtil.parseDateToString(today.getTime()) +"'" +
				" FROM ( SELECT sgm.scoregroupid as gid, sg.name as name,sg.basescore*count(e.id) as available " +
				"		FROM score_group_mapper sgm " +
				"				LEFT JOIN score_group sg ON sgm.scoregroupid=sg.id " +
				"				LEFT JOIN employee e ON sgm.empid=e.id " +
				"				LEFT JOIN scoreexceptionlist s ON e.positionid=s.positionid " +
				" 		WHERE s.status IS NULL OR s.status=2 GROUP BY sgm.scoregroupid,sg.name,sg.basescore) AS tem " +
				" WHERE tem.gid=scoreg.id AND scoreg.lastupdatedate <'" + HRUtil.parseDateToString(today.getTime()) +"'";
		if(D) System.out.println(query);
		em.createNativeQuery(query).executeUpdate();
		
		//Cache All receivers,exceptionlist,scoregroups
		String receivers = "";
		String scoreTypesQuery = "";
		for(Scorerecord record:records){
			if(record.getReceiver().getEmployee().getWorkerid() != null)
				receivers += "'"+record.getReceiver().getEmployee().getWorkerid()+"',";
			if(record.getScoretype().getReference() != null){
				scoreTypesQuery += "'"+record.getScoretype().getReference()+"',";
			}
		}
		String members = receivers + "'" +user.getEmployee()+"'";
		query = "SELECT mem.id as one, mem.workerid as two, mem.historytotal as three," +
				" sg.id as four, sg.name as five, sg.basescore as six, sg.available as seven, sg.lastupdatedate as eight," +
				" excep.id as nine, excep.status as ten, e.id as eleven, e.fullname as twel"+
				" FROM scoremember mem LEFT JOIN employee e ON mem.workerid=e.workerid" +
				" LEFT JOIN score_group_mapper m ON e.id=m.empid"+
				" LEFT JOIN score_group sg ON m.scoregroupid=sg.id"+
				" LEFT JOIN scoreexceptionlist excep ON excep.positionid=e.positionid"+
				" WHERE mem.workerid IN ("+members+")";
		if(D) System.out.println(query);
		List<Object[]> results = em.createNativeQuery(query).getResultList();
		for(Object[] obj:results){
			Scoremember smember = new Scoremember();
			ScoreDivGroup group = new ScoreDivGroup();
			Employee e = new Employee();
			e.setId((Integer) obj[10]);
			e.setWorkerid((String) obj[1]);
			e.setFullname((String) obj[11]);
			if(obj[3] != null){
				Department d = new Department();
				d.setId((Integer) obj[3]);
				e.setDepartment(d);
			}
			smember.setId((Integer) obj[0]);
			smember.setHistorytotal((Float) obj[2]);
			smember.setEmployee(e);
			
			memberMap.put(e.getWorkerid(), smember);
			
			if(obj[8] != null){
				memberExceptionState.put(e.getWorkerid(), (Integer) obj[9]);
			}else{
				memberExceptionState.put(e.getWorkerid(), ScoreExceptionList.HAS_UPPER_SCORE_LIMIT);
			}
			
			if(obj[3] != null && gMap.get(obj[3]) == null){
				group.setId((Integer) obj[3]);
				group.setName((String) obj[4]);
				group.setBasescore((Float) obj[5]);
				group.setAvailable((Float) obj[6]);
				group.setLastUpdateDate((Date) obj[7]);
				gMap.put(group.getId(), group);
			}
		}
		
		//Cache scoresummary
		query = "SELECT sum.id as thirten, sum.date as fourten, sum.score fiften, sum.fixscore sixten, sum.performancescore as seventen, sum.projectscore as eighten, sum.workerid as sumwid" +
				" FROM scoresummary sum WHERE EXTRACT(month FROM sum.date)="+ (today.get(Calendar.MONTH)+1)+" AND EXTRACT(year FROM sum.date)="+today.get(Calendar.YEAR)+" AND sum.workerid IN ("+members+")";
		if(D) System.out.println(query);
		results = em.createNativeQuery(query).getResultList();
		for(Object[] obj:results){
			if(sumMap.get((String)obj[6]) == null){
				Scoresummary sum = new Scoresummary();
				sum.setId((Integer) obj[0]);
				sum.setDate((Date) obj[1]);
				sum.setScore((Float) obj[2]);
				sum.setFixscore((Float) obj[3]);
				sum.setPerformancescore((Float) obj[4]);
				sum.setProjectscore((Float) obj[5]);
				sumMap.put((String)obj[6], sum);
			}
		}
		//Cache all approver rights for user account
		Scoremember accountMember = memberMap.get(user.getEmployee());
		query = "SELECT id, scoregroupid, isapprover FROM scoreapprover WHERE approver="+accountMember.getEmployee().getId();
		if(D) System.out.println(query);
		results = em.createNativeQuery(query).getResultList();
		for(Object[] obj:results){
			approverMap.put((Integer)obj[1], (String)obj[2]);
			if(((String)obj[2]).equals(Scoreapprover.APPROVER)){
				if(D) System.out.println("Is approver");
				isapprover = true;
			}
		}
		//Cache all scoretypes for record
		if(scoreTypesQuery.length() > 0)
			scoreTypesQuery = scoreTypesQuery.substring(0,scoreTypesQuery.length()-1);
		query  = "SELECT id,reference,description,score,type,period,examine,scoreobject FROM scoretype WHERE status=1 AND reference IN ("+scoreTypesQuery+")";
		if(D) System.out.println(query);
		results = em.createNativeQuery(query).getResultList();
		for(Object[] obj:results){
			Scoretype st = ScoreConvertor.convertToScoreType((Integer)obj[0], (String)obj[1], (String)obj[2], (Float)obj[3], ((Short)obj[4]).intValue(), (String)obj[5], (String)obj[6], (String)obj[7],null,null,null);
			stMap.put(st.getReference(), st);
		}
		
		String err = "";
		String recordQuery = "";
		String summaryQuery = "";
		String memberQuery = "";
		String remarkQuery = "";
		String groupQuery = "";
		try{
			for(int i=0;i<records.size();i++){
				try{
				Scorerecord re = records.get(i);
				//检查受分人是否存在
				String receiver = re.getReceiver().getEmployee().getWorkerid();
				if(memberMap.get(receiver) == null)
					throw new Exception("用户工号:"+receiver+"还未编入积分组");
				if(memberMap.get(receiver) == null)
					throw new Exception("没有找到员工工号:"+receiver);
				if(stMap.get(re.getScoretype().getReference()) == null){
					records.set(i, null);
					throw new Exception("找不到条例编号:"+re.getScoretype().getReference());
				}
				//设置分值
				Scoretype st = stMap.get(re.getScoretype().getReference());
				Float score= 0F;
				if(st.getType() == Scoretype.SCORE_TYPE_TEMP){
					if(re.getScore() != null && re.getScore() == 0 && st.getScore() == 0){
						records.set(i,null);
						continue; //忽略0分的临时分记录
					}
				}
				if(st.getScore() == 0){
					score = re.getScore(); //如果不是临时分，则如果条例分值为0就自拟定分，否则用条例分
				}else{
					score = st.getScore();
				}
				if(score == null) score = 0F; //如果分值未设定侧当0分处理
				
				//检查权限
				Integer groupId = memberMap.get(receiver).getEmployee().getDepartment().getId();
				if(!isapprover){
					String approverState = approverMap.get(groupId);
					if(approverState == null)
						throw new Exception("用户:"+accountMember.getEmployee().getFullname()+"没有权限打分给积分组<"+gMap.get(groupId).getName()+">"+"的"+memberMap.get(receiver).getEmployee().getFullname());
				}
				
				//输入记录打分
				re.setReceiver(memberMap.get(receiver));//受分人
				re.setSender(accountMember); //给分人
				if(re.getScoredate() == null)
					re.setScoredate(today.getTime());
				re.setScoretype(st);
				re.setScore(score);
				re.setCreator(user);
				re.setCreatedate(today.getTime());
				if(approverMap.get(groupId).equals(Scoreapprover.APPROVER)){
					re.setStatus(Scorerecord.APPROVED);//初次新建，自动提交到审核页面
				}else{
					re.setStatus(Scorerecord.WAITING);
				}
				records.set(i,re);
				
				//保存注释
				if(re.getRemark() != null){
					remarkMap.put(i, re.getRemark());
					remarkQuery += "('" + re.getRemark().getRemark() + "'),";
				}
				
				//奖分和扣分
				if(st.getType() == Scoretype.SCORE_TYPE_TEMP && memberExceptionState.get(receiver) == ScoreExceptionList.HAS_UPPER_SCORE_LIMIT){
					gMap.get(groupId).setAvailable(gMap.get(groupId).getAvailable()-score); //有上限的人部门扣分
				}
				if(re.getStatus() == Scorerecord.APPROVED){
					memberMap.get(receiver).setHistorytotal(memberMap.get(receiver).getHistorytotal()+score);
					Scoresummary summary = sumMap.get(receiver);
					if(summary == null){
						summary = new Scoresummary();
						summary.setDate(re.getCreatedate());
					}
					summary = addSummaryScore(re, summary, score);
					sumMap.put(receiver, summary);
				}
				}catch(Exception e){
					e.printStackTrace();
					err += e.getMessage()+"<br/>";
				}
			}
			//检查部门分够不够,建立更新Query
			String errGroup = "";
			for(ScoreDivGroup sdg:gMap.values()){
				if(sdg == null) continue;
				if(sdg.getAvailable() == null){
					if(D) System.out.println(sdg.getName() + " available is NULL");
				}
				if(sdg.getAvailable() < 0)
					errGroup += "积分组:"+sdg.getName()+"没有足够的分值("+sdg.getAvailable()+")<br/>";
				else
					groupQuery += "("+sdg.getId()+","+sdg.getAvailable()+"),";
			}
			if(!errGroup.equals(""))
				throw new Exception(errGroup);
				
			//Save remark
			if(remarkQuery.length() > 0){
			remarkQuery = remarkQuery.substring(0,remarkQuery.length()-1);
			query = "INSERT INTO scorerecord_remark (remark) VALUES "+remarkQuery+" RETURNING id";
			if(D) System.out.println(query);
			List<Object> singleResults = em.createNativeQuery(query).getResultList();
			int temI = 0;
			for(Integer key:remarkMap.keySet()){
				remarkMap.get(key).setId((Integer) singleResults.get(temI));
				temI++;
			}
			}
			
			//Build records, summary ,member query strings
			for(int i=0;i<records.size();i++){
				if(records.get(i)==null) continue;
				Scorerecord re = records.get(i);
				if(re.getCreator() == null || re.getScoretype() == null || re.getScoredate() == null 
						|| re.getReceiver().getEmployee() == null || re.getSender().getEmployee() == null){
					throw new Exception(re.getScoretype().getReference() + "有些记录信息提供不完整  检查工号："+re.getReceiver().getEmployee().getWorkerid());
				}else{
					recordQuery += "("+re.getReceiver().getEmployee().getWorkerid()+","+re.getSender().getEmployee().getWorkerid()+","+
								re.getScoretype().getId()+","+re.getCreator().getId()+",'"+HRUtil.parseDateToString(re.getScoredate())+"',"+
								re.getScore() + ",'" + HRUtil.parseDateToString(re.getCreatedate())+"',"+re.getStatus()+",";
				}
				if(remarkMap.get(i) == null)
					recordQuery += "null),";
				else
					recordQuery += remarkMap.get(i).getId()+"),";
			}
			recordQuery = recordQuery.substring(0,recordQuery.length()-1);
			
			for(Scoresummary sum:sumMap.values()){
				if(sum.getId() == null)
					newSum.add(sum);
				else
					summaryQuery += "("+sum.getId()+","+sum.getScore()+","+sum.getFixscore()+","+sum.getPerformancescore()+","+sum.getProjectscore()+"),";
			}
			
			for(Scoremember mem:memberMap.values()){
				memberQuery += "("+mem.getId()+","+mem.getHistorytotal()+"),";
			}
			memberQuery = memberQuery.substring(0,memberQuery.length()-1);
			
			//Update records, summary ,member scoregroup using built query
			query = "INSERT INTO scorerecord (receiverid,senderid,scoretypeid,creator,scoredate,score,createdate,status,remarkid) VALUES "+recordQuery;
			if(D) System.out.println(query);
			em.createNativeQuery(query).executeUpdate();
			
			query = "UPDATE scoremember AS sm SET historytotal=tem.total FROM ( VALUES "+memberQuery+") AS tem(id,total) WHERE sm.id=tem.id";
			if(D) System.out.println(query);
			em.createNativeQuery(query).executeUpdate();
			
			if(summaryQuery.length()>0){
				summaryQuery = summaryQuery.substring(0,summaryQuery.length()-1);
				query = "UPDATE scoresummary AS sm SET score=tem.score, fixscore=tem.fixscore, performancescore=tem.performancescore,projectscore=tem.projectscore FROM (" +
						" VALUES "+ summaryQuery +") AS tem(id,score,fixscore,performancescore,projectscore) WHERE sm.id=tem.id";
				if(D) System.out.println(query);
				em.createNativeQuery(query).executeUpdate();
			}
			for(Scoresummary sum:newSum)
				em.persist(sum);
			
			if(groupQuery.length()>0){
				groupQuery = groupQuery.substring(0,groupQuery.length()-1);
				query = "UPDATE score_group AS sg SET available=tem.available FROM (" +
					" VALUES "+ groupQuery +") AS tem(id,available) WHERE sg.id=tem.id";
				if(D) System.out.println(query);
				em.createNativeQuery(query).executeUpdate();
			}
			
			ScoreLog sl = new ScoreLog();
			sl.setAction(ScoreLog.CREATE);
			sl.setCreatetime(today.getTime());
			sl.setRemark("新添加了"+records.size()+"条打分记录");
			sl.setWho(user);
			em.persist(sl);
		}catch(Exception e){
			e.printStackTrace();
			err += e.getMessage()+"<br/>";
		}
		
		//如果出错则取消所有修改并返回错误
		if(!err.equals(""))
			throw new Exception(err);
	}
	
	/**
	 * 检查员工部门是否有足够的分值飞给该员工
	 * @param scorer
	 * @param score
	 * @return
	 * @throws Exception
	 */
	private boolean isDepartmentScoreEnoughForEmployee(Employee scorer,
			Float score) throws Exception{
		if(isScorerUnlimited(scorer))
			return true;
		DepartmentScore ds = getDepartmentScore(scorer);
		Float result = ds.getAvailable() - score;
		if(result < 0)
			return false;
		return true;
	}

	/**
	 * 检查受分人是否没上限
	 * @param scorer
	 * @return
	 */
	public boolean isScorerUnlimited(Employee scorer){
		try{
			ScoreExceptionList se = (ScoreExceptionList) em.createQuery("SELECT q FROM ScoreExceptionList q WHERE q.position.id="+scorer.getPosition().getId()+" AND q.status="+ScoreExceptionList.NO_UPPER_SCORE_LIMIT).getSingleResult();
			if(se != null)
				return true;
			else
				return false;
		}catch(Exception e){
			return false;
		}
	}

	/**
	 * 根据时间获取该用户提交的并且已经通过审核的条例。
	 * @param user
	 * 用户
	 * @param selectPeriod
	 * null 为今天
	 * <br/>w为周
	 * <br/>m为月
	 * <br/>a为自选时间段
	 * @param startDate
	 * 当选择为a时才使用，开始日期
	 * @param endDate
	 * 当选择为a时才使用，结束日期
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Scorerecord> getApprovedListByTime(Account user,String selectPeriod, Date startDate, Date endDate) throws Exception {
		Calendar cal = Calendar.getInstance();
		Calendar cal2 = Calendar.getInstance();
		Employee e = (Employee) em.createQuery("SELECT distinct q FROM Employee q WHERE q.workerid=?").setParameter(1, user.getEmployee()).getSingleResult();
		
		if(selectPeriod == null || selectPeriod.equals("")){
			selectPeriod = "w"; // 设置默认获取本周的记录为默认
		}
		
		if(selectPeriod.equals("w")){
			cal.set(Calendar.DAY_OF_WEEK,cal.getFirstDayOfWeek());
			cal2.setTime(cal.getTime());
			cal2.add(Calendar.WEEK_OF_YEAR, 1);
		}else if(selectPeriod.equals("m")){
			cal.set(Calendar.DAY_OF_MONTH, 1);
			cal2.setTime(cal.getTime());
			cal2.add(Calendar.MONTH, 1);
		}else if(selectPeriod.equals("a")){
			if(startDate != null)
				cal.setTime(startDate);
			if(endDate != null)
				cal2.setTime(endDate);
		}
		String query = "SELECT re.id as reid, re.scoredate as resdate, re.createdate as recdate, re.score as rescore, e.workerid recwid, e.fullname as recname, p.name as pname, " +
				"			sender.workerid as senderwid, sender.fullname as sendername,re.status as restatus, t.id as tid, t.reference as tref, t.description as tdesp FROM scorerecord re " +
				" LEFT JOIN employee e ON re.receiverid = e.workerid " +
				" LEFT JOIN employee sender ON re.senderid = sender.workerid " + 
				" LEFT JOIN position p ON p.id=e.positionid " + 
				" LEFT JOIN scoretype t ON re.scoretypeid=t.id " +
				" LEFT JOIN score_group_mapper m ON e.id=m.empid " +
				" WHERE m.scoregroupid " + 
				" IN (SELECT scoregroupid FROM scoreapprover WHERE approver=" + e.getId()+") AND re.status="+Scorerecord.APPROVED +
				" AND re.createdate BETWEEN '"+ HRUtil.parseDateToString(cal.getTime())+"' AND '" +  HRUtil.parseDateToString(cal2.getTime()) + "' " +
				" ORDER BY re.id DESC";
		
		List<Object[]> result = em.createNativeQuery(query).getResultList();
										
		List<Scorerecord> sList = new ArrayList<Scorerecord>();
		
		
		for(Object[] obj:result){
			Scorerecord r = new Scorerecord();
			r.setId(((Integer)obj[0]).intValue());
			r.setScoredate((Date)obj[1]);
			r.setCreatedate((Date)obj[2]);
			r.setScore(((Float)obj[3]).floatValue());
			r.setReceiver(convertToScoremember((String)obj[4], (String)obj[5], (String)obj[6]));
			r.setSender(convertToScoremember((String)obj[7], (String)obj[8], ""));
			r.setStatus(((Short)obj[9]).intValue());
			r.setScoretype(converToScoretype((Integer)obj[10], (String)obj[11], (String)obj[12]));
			sList.add(r);
		}
		return sList;
	}

	public List<Scorerecord> getUserApprovedListByTime(Account user, Date date1, Date date2) throws Exception{
//		if(date1 == null) date1= new Date();
//		if(date2 == null) date2 = new Date();
//		String query = "SELECT q FROM Scorerecord q WHERE q.creator.id=? AND q.status=? " +
//				" AND q.createdate BETWEEN '"+HRUtil.parseDateToString(date1)+"' AND '"+HRUtil.parseDateToString(date2)+"' ORDER BY q.id DESC";
//		System.out.println(query);
//		List<Scorerecord> records = em.createQuery(query)
//				.setParameter(1, user.getId()).setParameter(2, Scorerecord.APPROVED).getResultList();
		
		
		String query = "SELECT re.id as reid, re.scoredate as resdate, re.createdate as recdate, re.score as rescore, e.workerid recwid, e.fullname as recname, p.name as pname, " +
				"			sender.workerid as senderwid, sender.fullname as sendername,re.status as restatus, t.id as tid, t.reference as tref, t.description as tdesp FROM scorerecord re " +
				" LEFT JOIN employee e ON re.receiverid = e.workerid " +
				" LEFT JOIN employee sender ON re.senderid = sender.workerid " + 
				" LEFT JOIN position p ON p.id=e.positionid " + 
				" LEFT JOIN scoretype t ON re.scoretypeid=t.id " +
				" LEFT JOIN score_group_mapper m ON e.id=m.empid " +
				" WHERE re.status="+Scorerecord.APPROVED +
				" AND re.creator="+ user.getId()+" AND re.createdate BETWEEN '"+ HRUtil.parseDateToString(date1)+"' AND '" +  HRUtil.parseDateToString(date2) + "' " +
				" ORDER BY re.id DESC";
		
		List<Object[]> result = em.createNativeQuery(query).getResultList();
										
		List<Scorerecord> sList = new ArrayList<Scorerecord>();
		
		
		for(Object[] obj:result){
			Scorerecord r = new Scorerecord();
			r.setId(((Integer)obj[0]).intValue());
			r.setScoredate((Date)obj[1]);
			r.setCreatedate((Date)obj[2]);
			r.setScore(((Float)obj[3]).floatValue());
			r.setReceiver(convertToScoremember((String)obj[4], (String)obj[5], (String)obj[6]));
			r.setSender(convertToScoremember((String)obj[7], (String)obj[8], ""));
			r.setStatus(((Short)obj[9]).intValue());
			r.setScoretype(converToScoretype((Integer)obj[10], (String)obj[11], (String)obj[12]));
			sList.add(r);
		}
		return sList;
	}
	/**
	 * 转换成 Scoremember 类型
	 * @param workerid
	 * @param fullname
	 * @param position
	 * @return Scoremember
	 * @throws Exception
	 */
	public Scoremember convertToScoremember(String workerid,String fullname,String position) throws Exception{
		Scoremember sm = new Scoremember();
		Employee e = new Employee();
		Position p = new Position();
		e.setWorkerid(workerid);
		e.setFullname(fullname);
		p.setName(position);
		e.setPosition(p);
		
		sm.setEmployee(e);	
		return sm;				
	}
	
	
	/**
	 * 转换成 Scoretype 类型
	 * @param scoretypeid
	 * @param reference
	 * @param description
	 * @return Scoretype
	 * @throws Exception
	 */
	public Scoretype converToScoretype(Integer scoretypeid,String reference,String description) throws Exception{
		Scoretype st = new Scoretype();
		st.setId(scoretypeid);
		st.setReference(reference);
	    st.setDescription(description);	
		
		return st;
	}

	/**
	 * 转换成ScorerecordRemark对象
	 * @param remark
	 * @return
	 */
	public ScorerecordRemark convertToScorerecordRemark(String remark){
		ScorerecordRemark remarkObj = new ScorerecordRemark();
		remarkObj.setRemark(remark);
		return remarkObj;
	}

	/**
	 * 获取当月月供得分详情
	 * @param user
	 * @param month
	 * @return
	 * @throws Exception
	 */
	public List<Scoresummary> getDepartmentScoreSummary(Account user,Integer month,Integer departmentId) throws Exception{
		List<Scoresummary> summary  = em.createQuery("SELECT q FROM Scoresummary q WHERE q.employee.department.id=? AND" +
				" EXTRACT(month FROM q.date)=? ORDER BY q.score DESC").setParameter(1, departmentId).setParameter(2, month).getResultList();
		return summary;
	}

	/**
	 * 用用户账号或者工号来获取部门得分
	 * @param user
	 * @param workerid
	 * @return
	 */
	public DepartmentScore getDepartmentScoresByDepartmetnIdORWorkerId(Integer dsId, String workerid) throws Exception{
		DepartmentScore ds = null;
		if(dsId != null){
			ds = (DepartmentScore) em.createQuery("SELECT q FROM DepartmentScore q WHERE q.department.id=?").setParameter(1, dsId).getSingleResult();
		}else{
			Employee e = (Employee) em.createQuery("SELECT q FROM Employee q WHERE q.workerid=?").setParameter(1, workerid).getSingleResult();
			ds = (DepartmentScore) em.createQuery("SELECT q FROM DepartmentScore q WHERE q.department.id=?").setParameter(1, e.getDepartment().getId()).getSingleResult();
		}
		return ds;
	}

	/**
	 * 获取部门时间段内的详细审核了的项目
	 * @param id
	 * @param time
	 * @param time2
	 * @return
	 */
	public List<Scorerecord> getApprovedListByTimeForDepartment(Integer id, Date time, Date time2) throws Exception{
		List<Scorerecord> records = em.createNativeQuery("SELECT q.* FROM scorerecord q LEFT JOIN employee e ON q.receiverid=e.workerid " +
				" RIGHT JOIN score_group_mapper m ON m.empid=e.id WHERE m.scoregroupid="+id+ " AND q.status="+Scorerecord.APPROVED+
				" AND q.createdate >='"+HRUtil.parseDateToString(time)+"' AND q.createdate < '"+HRUtil.parseDateToString(time2)+"' " +
				" ORDER BY q.id DESC", Scorerecord.class).getResultList();
		return records;
	}

	/**
	 * 添加职位过滤,积分制里的有些职位不用参加积分制，有些没有上限
	 * @param exception
	 * @param user
	 * @throws Exception
	 */
	@Transactional
	public void addPositionException(ScoreExceptionList exception, Account user) throws Exception{
		em.persist(exception);
		em.flush();
		ScoreLog sl = new ScoreLog();
		sl.setAction(ScoreLog.CREATE);
		sl.setCreatetime(Calendar.getInstance().getTime());
		sl.setRecordid(exception.getId()+"");
		sl.setWho(user);
		sl.setRemark("添加了一个职位过滤");
		em.persist(sl);
	}

	/**
	 * 获取所有过滤的职位信息
	 * @return
	 * @throws Exception
	 */
	public List<ScoreExceptionList> getAllPositionExceptions() throws Exception{
		return em.createQuery("SELECT q FROM ScoreExceptionList q ORDER BY q.position.name").getResultList();
	}

	/**
	 * 检查员工是否可以打分给指定员工
	 * @param e
	 * @param curUser
	 * @return
	 */
	public boolean checkEmployeeAllowToScore(Employee e, Employee curUser) {
//		String query = "SELECT count(q) FROM Scoreapprover q, ScoreGroupMapper m " +
//				" WHERE q.group.id=m.group.id AND q.approver.id="+curUser.getId() + " AND m.employee.id="+e.getId();
		String query  =  "SELECT count(q) FROM score_group_mapper m LEFT JOIN scoreapprover q ON q.scoregroupid=m.scoregroupid " +
				" AND q.approver="+curUser.getId()+" AND m.empid="+e.getId();
//		System.out.println(query);
		Long count =  ((BigInteger) em.createNativeQuery(query).getSingleResult()).longValue();
		if(count >= 1)
			return true;
		else 
			return false;
	}

	/**
	 * 检查用户是否打分的审核人
	 * @param curUser
	 * @return
	 * @throws Exception
	 */
	public boolean isUserScoreApprover(Employee curUser) throws Exception{
		String query = "SELECT count(q) FROM Scoreapprover q WHERE q.approver.id="+curUser.getId()+" AND q.isapprover='"+Scoreapprover.APPROVER+"'";
		Long count  = (Long) em.createQuery(query).getSingleResult();
		if(count >=1)
			return true;
		else
			return false;
	}

	/**
	 * 获取用户所在 和所管核的部门
	 * @param user
	 * @return
	 */
	public List<ScoreDivGroup> getAllManageDeparmentsByUser(Account user) throws Exception{
		String query = "SELECT s.* FROM score_group s WHERE " +
				"s.id IN (SELECT a.scoregroupid FROM scoreapprover a LEFT JOIN employee e ON a.approver=e.id WHERE e.workerid='"+user.getEmployee()+"') " +
				" OR s.id IN (SELECT m.scoregroupid FROM score_group_mapper m LEFT JOIN employee e ON m.empid=e.id WHERE e.workerid='"+user.getEmployee()+"')" ;
		return em.createNativeQuery(query, ScoreDivGroup.class).getResultList();
	}

	public List<DepartmentScore> getAllDepartmentScores() throws Exception{
		return em.createQuery("SELECT q FROM DepartmentScore q ORDER BY q.id ASC").getResultList();
	}

	/**
	 * 更新部门可用总分
	 * @param depS
	 * @throws Exception
	 */
	@Transactional(rollbackFor=Exception.class)
	public void updateDepartmentScores(List<DepartmentScore> depS) throws Exception{
		for(DepartmentScore ds:depS){
			DepartmentScore d = em.find(DepartmentScore.class, ds.getId());
			d.setBasescore(ds.getBasescore());
			d.setAvailable(ds.getAvailable());
			em.merge(d);
		}
	}

	/**
	 * 获取部门等待审核的积分总和
	 * @param id
	 * @param time
	 * @param time2
	 * @return
	 */
	public Float getDepartmentWaittingScores(Integer id, Date time, Date time2) throws Exception{
//		List<Integer> list = em.createNativeQuery("SELECT positionid FROM scoreexceptionlist WHERE status="+ScoreExceptionList.NO_UPPER_SCORE_LIMIT).getResultList();
		String query = "SELECT SUM(q.score) FROM scorerecord q LEFT JOIN employee e ON q.receiverid=e.workerid " +
				" LEFT JOIN scoretype st on q.scoretypeid=st.id " +
				" RIGHT JOIN score_group_mapper m ON m.empid=e.id WHERE m.scoregroupid="+id+ " AND q.status="+Scorerecord.WAITING+
				" AND st.type="+Scoretype.SCORE_TYPE_TEMP+"AND q.createdate >='"+HRUtil.parseDateToString(time)+"' AND q.createdate < '"+HRUtil.parseDateToString(time2)+"' " +
						"AND e.positionid NOT IN (SELECT positionid FROM scoreexceptionlist WHERE status="+ScoreExceptionList.NO_UPPER_SCORE_LIMIT+")"; 
		Float bint = (Float) em.createNativeQuery(query).getSingleResult();
		return bint;
	}

	/**
	 * 获取部门被拒绝的条例总分
	 * @param id
	 * @param time
	 * @param time2
	 * @return
	 */
	public Float getDepartmentRejectedScores(Integer id, Date time, Date time2) throws Exception{
		Float bint = (Float) em.createNativeQuery("SELECT SUM(q.score) FROM scorerecord q LEFT JOIN employee e ON q.receiverid=e.workerid " +
				" RIGHT JOIN score_group_mapper m ON m.empid=e.id WHERE m.scoregroupid="+id+ " AND q.status="+Scorerecord.REJECTED+
				" AND q.createdate >='"+HRUtil.parseDateToString(time)+"' AND q.createdate < '"+HRUtil.parseDateToString(time2)+"' "+
				" AND e.positionid NOT IN (SELECT positionid FROM scoreexceptionlist WHERE status="+ScoreExceptionList.NO_UPPER_SCORE_LIMIT+")"
				).getSingleResult();
		return bint;
	}
	
	/**
	 * 获取部门未提交的条例总分
	 * @param id
	 * @param time
	 * @param time2
	 * @return
	 */
	public Float getDepartmentNotSubmitScores(Integer id, Date time, Date time2) throws Exception{
		Float bint = (Float) em.createNativeQuery("SELECT SUM(q.score) FROM scorerecord q LEFT JOIN employee e ON q.receiverid=e.workerid " +
				" RIGHT JOIN score_group_mapper m ON m.empid=e.id WHERE m.scoregroupid="+id+ " AND q.status="+Scorerecord.CREATED+
				" AND q.createdate >='"+HRUtil.parseDateToString(time)+"' AND q.createdate < '"+HRUtil.parseDateToString(time2)+"' "+
				" AND e.positionid NOT IN (SELECT positionid FROM scoreexceptionlist WHERE status="+ScoreExceptionList.NO_UPPER_SCORE_LIMIT+")"
				).getSingleResult();
		return bint;
	}

	/**
	 * 获取积分组的最开始父类组
	 * @return
	 */
	public List<ScoreDivGroup> getParentScoreGroup() throws Exception{
		return em.createNativeQuery("SELECT * FROM score_group WHERE pid is NULL ORDER BY name DESC",ScoreDivGroup.class).getResultList();
	}

	/**
	 * 添加积分组
	 * @param scoreNewGroup
	 */
	@Transactional(rollbackFor=Exception.class)
	public void addScoreDivGroup(ScoreDivGroup scoreNewGroup,Account user) throws Exception{
		if(scoreNewGroup.getName() == null)
			throw new Exception("没有提供组名字");
		scoreNewGroup.setLastUpdateDate(new Date());
		em.persist(scoreNewGroup);
		em.flush();
		ScoreLog sl = new ScoreLog();
		sl.setWho(user);
		sl.setRecordid(scoreNewGroup.getId()+"");
		sl.setAction(ScoreLog.CREATE);
		sl.setRemark("添加新的积分组");
		sl.setCreatetime(Calendar.getInstance().getTime());
		em.persist(sl);
	}

	/**
	 * 给一个积分组，获取它的子组合子组员工
	 * @param e
	 * @return
	 * @throws Exception
	 */
	public EmpDepartments getAllChildrenAndEmployeeForScoreGroup(
			EmpDepartments e) throws Exception{
		String query = "select sg.id as sgid,sg.name as sdname ,e.id as eid,e.fullname as efullname, e.workerid as wid from score_group sg left join score_group_mapper sgm on sg.id=sgm.scoregroupid left join employee e on sgm.empid=e.id " +
				" WHERE sg.pid=" + e.getDeptId() + " order by sg.id";
		if(e.getDeptId().equals("0")){
			query = "select sg.id as sgid,sg.name as sdname ,e.id as eid,e.fullname as efullname , e.workerid as wid from score_group sg left join score_group_mapper sgm on sg.id=sgm.scoregroupid left join employee e on sgm.empid=e.id " +
					" WHERE sg.pid is NULL order by sg.id";
		}
		List<Object[]> results = em.createNativeQuery(query).getResultList();
		List<EmpDepartments> extras = new ArrayList<EmpDepartments>();
		List<Employee> empList = new ArrayList<Employee>();
		EmpDepartments group = new EmpDepartments();
		for(Object[] obj:results){
			Integer id = (Integer) obj[0];
			if(id.toString().equals(group.getDeptId())){
				if(obj[2] != null){
					Employee emp = new Employee();
					emp.setId((Integer)obj[2]);
					emp.setFullname((String) obj[3]);
					emp.setWorkerid((String)obj[4]);
					empList.add(emp);
				}
			}else{
				if(group != null && group.getDeptId()!=null && !group.getDeptId().trim().equals("")){
					if(empList.size()>0)
						group.setEmps(empList);
					extras.add(group);
				}
				group = new EmpDepartments();
				group.setDeptId(id.toString());
				group.setDept((String) obj[1]);
				empList = new ArrayList<Employee>();
				if(obj[2] != null){
					Employee emp = new Employee();
					emp.setId((Integer)obj[2]);
					emp.setFullname((String) obj[3]);
					emp.setWorkerid((String)obj[4]);
					empList.add(emp);
				}
			}
		}
		//添加最后一行记录
		if(group != null && group.getDeptId()!=null && !group.getDeptId().trim().equals("")){
			if(empList.size()>0)
				group.setEmps(empList);
			extras.add(group);
		}
		if(extras.size()>0)
			e.setExtras(extras);
		return e;
	}

	/**
	 * 删除一个积分组和组成员
	 * @param scoreNewGroup
	 * @param user
	 */
	@Transactional
	public void delScoreDivGroup(String gid, Account user) throws Exception {
		Integer id = Integer.parseInt(gid);
		ScoreDivGroup sdg = em.find(ScoreDivGroup.class, id);
		//创建删除记录
		ScoreLog sl = new ScoreLog();
		sl.setWho(user);
		sl.setAction(ScoreLog.CREATE);
		sl.setRemark("删除了组:"+sdg.getName());
		sl.setCreatetime(Calendar.getInstance().getTime());
		
		em.createNativeQuery("DELETE FROM score_group_mapper WHERE scoregroupid="+id).executeUpdate();
		em.createNativeQuery("DELETE FROM scoreapprover WHERE scoregroupid="+id).executeUpdate();
		em.createNativeQuery("DELETE FROM score_group WHERE pid="+id).executeUpdate();
		em.createNativeQuery("DELETE FROM score_group WHERE id="+id).executeUpdate();
		
		em.persist(sl);
	}

	/**
	 * 添加员工到积分组
	 * @param workerids
	 * 逗号分隔的工号
	 * @param gid
	 * 积分组id
	 * @throws Exception
	 */
	@Transactional(rollbackFor=Exception.class)
	public void joinEmployeeToScoreGroup(String workerids, String gid,Account user) throws Exception{
		String[] wIds = workerids.split(",",-1);
		for(String wid :wIds){
			if(wid != null && !wid.trim().equals("")){
				Employee e = (Employee) em.createQuery("SELECT q FROM Employee q WHERE q.workerid='"+wid+"'").getSingleResult();
				ScoreDivGroup sdg  = new ScoreDivGroup();
				sdg.setId(Integer.parseInt(gid));
				ScoreGroupMapper sgm = new ScoreGroupMapper();
				sgm.setEmployee(e);
				sgm.setGroup(sdg);
				try{
					ScoreGroupMapper sgmTemp = (ScoreGroupMapper) em.createQuery("SELECT q FROM ScoreGroupMapper q WHERE q.employee.id=?")
					.setParameter(1, e.getId()).getSingleResult();
					if(sgmTemp != null){
						em.remove(sgmTemp);
						em.flush();
					}
				}catch(Exception err){
					System.out.println(err.getMessage());
				}
				em.persist(sgm);
				ScoreLog sl = new ScoreLog();
				sl.setAction(ScoreLog.CREATE);
				sl.setCreatetime(Calendar.getInstance().getTime());
				sl.setRemark(e.getFullname()+"加入组ID"+sdg.getId());
				sl.setWho(user);
				em.persist(sl);
			}
		}
	}

	/**
	 * 检查积分组是否已经存在
	 * @param id
	 * @return
	 * @throws Exception
	 */
	public boolean scoreDivGroupExist(Integer id) throws Exception{
		try{
			ScoreDivGroup sdg = em.find(ScoreDivGroup.class, id);
			if(sdg != null)
				return true;
			else
				return false;
		}catch(Exception e){
			return false;
		}
	}

	/**
	 * 用ID查找积分组
	 * @param id
	 * @return
	 * @throws Exception
	 */
	public ScoreDivGroup getScoreDivGroupById(Integer id) throws Exception{
		return em.find(ScoreDivGroup.class, id);
	}

	/**
	 * 用组名字查找积分组
	 * @param name
	 * @return
	 * @throws Exception
	 */
	public ScoreDivGroup getScoreDivGroupByName(String name) throws Exception{
		try{
			return (ScoreDivGroup) em.createQuery("SELECT q FROM ScoreDivGroup q WHERE q.name='"+name+"'").getSingleResult();
		}catch(Exception e){
			System.out.println("Score group "+name+" not found.");
			return null;
		}
	}

	/**
	 * 用工号查找积分组
	 * @param employee
	 * @return
	 */
	public ScoreDivGroup getScoreDivGroupByWorkerId(String employee) {
		try{
			return (ScoreDivGroup) em.createNativeQuery("SELECT s.* FROM score_group_mapper m LEFT JOIN employee e ON m.empid=e.id " +
					"LEFT JOIN score_group s ON m.scoregroupid=s.id WHERE e.workerid='"+employee+"'",ScoreDivGroup.class).getSingleResult();
		}catch(Exception e){
			return null;
		}
	}
	
	/**
	 * 指定员工退出积分组编制
	 * @param ids
	 * 逗号分隔的ID
	 * @param user
	 * @throws Exception
	 */
	@Transactional(rollbackFor=Exception.class)
	public void quitEmployeeFromScoreGroup(String ids, Account user) throws Exception{
		String[] wids = ids.split(",",-1);
		for(String id:wids){
			if(id == null || id.trim().equals(""))
				continue;
			Employee e  = (Employee) em.find(Employee.class, Integer.parseInt(id));
//			System.out.println(e.getId() + " "+e.getWorkerid()+" "+e.getFullname());
			ScoreGroupMapper sgm = (ScoreGroupMapper) em.createQuery("SELECT q FROM ScoreGroupMapper q WHERE q.employee.id="+e.getId()).getSingleResult();
			ScoreLog sl = new ScoreLog();
			sl.setAction(ScoreLog.DELETE);
			sl.setCreatetime(Calendar.getInstance().getTime());
			sl.setRecordid(sgm.getId()+"");
			sl.setRemark("员工"+e.getFullname()+"退出了组"+sgm.getGroup().getName());
			sl.setWho(user);
			em.remove(sgm);
			em.persist(sl);
		}
	}

	/**
	 * 返回所有的积分组
	 * @return
	 * @throws Exception
	 */
	public List<ScoreDivGroup> getAllScoreGroups() throws Exception{
		return em.createQuery("SELECT q FROM ScoreDivGroup q ORDER BY q.id ASC").getResultList();
	}

	/**
	 * 返回组里有效的成员数
	 * @param id
	 * @return
	 */
	public int getScoreMemberCount(Integer id) {
		String query = "select count(*) from employee e " +
							"LEFT JOIN scoreexceptionlist s ON e.positionid=s.positionid " +
							"LEFT JOIN score_group_mapper sgm ON e.id=sgm.empid " +
							"WHERE sgm.scoregroupid="+id+" AND (s.status IS NULL OR s.status="+ScoreExceptionList.HAS_UPPER_SCORE_LIMIT+")";
		BigInteger b = (BigInteger) em.createNativeQuery(query).getSingleResult();
		return b.intValue();
	}

	/**
	 * 只更新积分组的基础分和现有的分
	 * @param saveScoreGroups
	 * @throws Exception
	 */
	@Transactional(rollbackFor=Exception.class)
	public void updateScoreGroups(List<ScoreDivGroup> saveScoreGroups) throws Exception{
		for(ScoreDivGroup g:saveScoreGroups){
			ScoreDivGroup s = em.find(ScoreDivGroup.class, g.getId());
			s.setBasescore(g.getBasescore());
			s.setAvailable(g.getAvailable());
			s.setLastUpdateDate(Calendar.getInstance().getTime());
			em.merge(s);
		}
	}

	/**
	 * 获取驾驶员的积分组
	 * @return
	 * @throws Exception
	 */
	public List<ScoreDivGroup> getDriverScoreGroups() throws Exception{
		List<ScoreDivGroup> parents = em.createNativeQuery("SELECT * FROM score_group WHERE  name LIKE '%驾驶员%'",ScoreDivGroup.class).getResultList();
		return parents;
	}

	/**
	 * 获取积分单内所有条例项目
	 * @param sheetName
	 * @return
	 */
	public Map<String,Scoretype> getScoretypesBySheetName(String sheetName) throws Exception{
		Map<String,Scoretype> list = new HashMap<String,Scoretype>();
		String query = "SELECT st.id, st.reference, st.score,st.examine,st.scoreobject,st.period," +
				" st.type, st.remark, st.creator,st.createdate, st.status,st.description" +
				" FROM scoresheetmapper m LEFT JOIN scoretype st ON m.scoretypeid=st.id " +
				" LEFT JOIN scoresheets sheet ON m.sheetid=sheet.id WHERE sheet.name LIKE '%" + sheetName + "%'";
		if(D) System.out.println(query);
		List<Object[]> results = em.createNativeQuery(query).getResultList();
		for(Object[] obj:results){
			Scoretype st = new Scoretype();
			st.setId((Integer) obj[0]);
			st.setReference((String) obj[1]);
			st.setScore((Float) obj[2]);
			st.setExamine((String) obj[3]);
			st.setScoreobject((String) obj[4]);
			st.setPeriod((String) obj[5]);
			st.setType(((Short) obj[6]).intValue());
			st.setRemark((String) obj[7]);
			Account a = new Account();
			a.setId((Integer) obj[8]);
			st.setAccount(a);
			st.setCreatedate((Date) obj[9]);
			st.setStatus((Integer) obj[10]);
			st.setDescription((String) obj[11]);
			list.put(st.getReference(),st);
		}
		return list;
	}

	/**
	 * 保存Scorettypes.如果ID为0则新建
	 * @param updatedTypes
	 * @param user
	 * @throws Exception
	 */
	@Transactional(rollbackFor=Exception.class)
	public void saveUpdatedScoretypes(Map<String, Scoretype> updatedTypes,
			Account user, String sheetName) throws Exception{
		List<Scoretype> newTypes = new ArrayList<Scoretype>();
		String updateQuery = "";
		for(Scoretype st:updatedTypes.values()){
			if(st.getId() == null){
				newTypes.add(st);
				continue;
			}else{
				updateQuery += "("+st.getId()+",'"+st.getReference()+"','"+st.getDescription()+"',"+st.getScore()+",'"+st.getExamine()+"','"+
						 st.getScoreobject()+"','"+st.getPeriod()+"',"+st.getType()+",'"+st.getRemark()+"',"+
						st.getAccount().getId()+",now(),"+st.getStatus()+"),";
			}
		}
		if(updateQuery.length() > 0){
			updateQuery = updateQuery.substring(0,updateQuery.length()-1);
			String query = "UPDATE scoretype st SET score=tem.score,description=tem.description,examine=tem.examine," +
					"scoreobject=tem.scoreobject,period=tem.period,type=tem.type,remark=tem.remark," +
					"createdate=tem.createdate,status=tem.status FROM ( VALUES "+updateQuery+") AS tem(id,reference,description,score,examine," +
							"scoreobject,period,type,remark,creator,createdate,status) WHERE st.id=tem.id";
			if(D) System.out.println(query);
			em.createNativeQuery(query).executeUpdate();
		}
		Scoresheets sheet = (Scoresheets) em.createNativeQuery("SELECT * FROM scoresheets WHERE name LIKE '%"+sheetName+"%'",Scoresheets.class).getSingleResult();
		if(sheet == null)
			throw new Exception("找不到表:"+sheetName);
		for(Scoretype st:newTypes){
			Scoresheetmapper mapper = new Scoresheetmapper();
			em.persist(st);
			em.flush();
			mapper.setSheet(sheet);
			mapper.setType(st);
			em.persist(mapper);
		}
		
		ScoreLog sl = new ScoreLog();
		sl.setAction(ScoreLog.CREATE);
		sl.setCreatetime(Calendar.getInstance().getTime());
		sl.setRemark("更新了积分单"+sheetName);
		sl.setWho(user);
		em.persist(sl);
	}
}
