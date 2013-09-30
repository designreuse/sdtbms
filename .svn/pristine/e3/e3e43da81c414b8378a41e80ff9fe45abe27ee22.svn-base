package com.bus.services;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.transaction.annotation.Transactional;

import com.bus.dto.Account;
import com.bus.dto.Department;
import com.bus.dto.Position;
import com.bus.dto.application.ApplicationConstants;
import com.bus.dto.application.ApplicationIdCards;
import com.bus.dto.application.DrivingExam;
import com.bus.dto.application.EmployeeRequest;
import com.bus.dto.application.HRApplication;
import com.bus.dto.application.IdCardsMapper;
import com.bus.dto.score.Scoretype;

public class EmpApplicationBean  extends EMBean{

	/**
	 * Get employment request form by default 
	 * @param pagenum
	 * @param lotsize
	 * @return
	 */
	public Map<String, Object> getRequests(int pagenum, int lotsize) throws Exception{
		List<EmployeeRequest> list=null;
		if(pagenum == -1 || lotsize == -1)
			list = em.createQuery("SELECT q FROM EmployeeRequest q ORDER BY receiveFormDate DESC").getResultList();
		else
			list = em.createQuery("SELECT q FROM EmployeeRequest q ORDER BY receiveFormDate DESC")
				.setFirstResult(pagenum * lotsize - lotsize).setMaxResults(lotsize).getResultList();
		Long count = (Long) em.createQuery("SELECT count(q) FROM EmployeeRequest q").getSingleResult();
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("list", list);
		map.put("count", count);
		return map;
	}

	/**
	 * Get employment request form by statement
	 * @param pagenum
	 * @param lotsize
	 * @param statement
	 * @return
	 */
	public Map<String, Object> getRequests(int pagenum, int lotsize, String statement) throws Exception {
		List<EmployeeRequest> list=null;
		list = em.createQuery(statement)
				.setFirstResult(pagenum * lotsize - lotsize).setMaxResults(lotsize).getResultList();
		String sub_statement = statement.substring(statement.indexOf("FROM"));
		if(sub_statement.indexOf("ORDER BY") != -1)
			sub_statement = sub_statement.substring(0,sub_statement.indexOf("ORDER BY"));
		Long count = (Long) em.createQuery("SELECT count(q) "+sub_statement).getSingleResult();
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("list", list);
		map.put("count", count);
		return map;
	}

	/**
	 * Create employment request form
	 * @param empRequest
	 * @throws Exception
	 */
	@Transactional
	public void createEmpRequest(EmployeeRequest empRequest) throws Exception{
		//Check department and positions
		Department d = em.find(Department.class, empRequest.getDepartment().getId());
		Position p = em.find(Position.class, empRequest.getPosition().getId());
		empRequest.setDepartment(d);
		empRequest.setPosition(p);
		em.persist(empRequest);
	}

	/**
	 * Get applications by default setting
	 * @param pagenum
	 * @param lotsize
	 * @return
	 */
	public Map<String, Object> getApplications(int pagenum, int lotsize) throws Exception{
		List<HRApplication> list=null;
		if(pagenum == -1 || lotsize == -1)
			list = em.createQuery("SELECT q FROM HRApplication q ORDER BY applyDate DESC").getResultList();
		else
			list = em.createQuery("SELECT q FROM HRApplication q ORDER BY applyDate DESC")
				.setFirstResult(pagenum * lotsize - lotsize).setMaxResults(lotsize).getResultList();
		Long count = (Long) em.createQuery("SELECT count(q) FROM HRApplication q").getSingleResult();
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("list", list);
		map.put("count", count);
		return map;
	}

	/**
	 * Get applications by statement
	 * @param pagenum
	 * @param lotsize
	 * @param statement
	 * @return
	 */
	public Map<String, Object> getApplications(int pagenum, int lotsize,
			String statement) throws Exception{
		List<HRApplication> list=null;
		list = em.createQuery(statement)
				.setFirstResult(pagenum * lotsize - lotsize).setMaxResults(lotsize).getResultList();
		String sub_statement = statement.substring(statement.indexOf("FROM"));
		if(sub_statement.indexOf("ORDER BY") != -1)
			sub_statement = sub_statement.substring(0,sub_statement.indexOf("ORDER BY"));
		Long count = (Long) em.createQuery("SELECT count(q) "+sub_statement).getSingleResult();
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("list", list);
		map.put("count", count);
		return map;
	}

	/**
	 * Create new application for applicant
	 * @param applicant
	 */
	@Transactional
	public Integer createApplicant(HRApplication applicant) throws Exception{
		if(applicant.getDepartment() != null){
			Department d = em.find(Department.class, applicant.getDepartment().getId());
			applicant.setDepartment(d);
		}
		if(applicant.getPosition() != null){
			Position p = em.find(Position.class, applicant.getPosition().getId());
		applicant.setPosition(p);
		}
		Account a = em.find(Account.class, applicant.getCreator().getId());
		applicant.setCreator(a);
		em.persist(applicant);
		em.flush();
		em.refresh(applicant);
		return applicant.getId();
	}

	/**
	 * Get default driver exam list
	 * @param pagenum
	 * @param lotsize
	 * @return
	 */
	public Map<String, Object> getDriverExams(int pagenum, int lotsize) {
		List<DrivingExam> list=null;
		if(pagenum == -1 || lotsize == -1)
			list = em.createQuery("SELECT q FROM DrivingExam q ORDER BY examdate DESC").getResultList();
		else
			list = em.createQuery("SELECT q FROM DrivingExam q ORDER BY examdate DESC")
				.setFirstResult(pagenum * lotsize - lotsize).setMaxResults(lotsize).getResultList();
		Long count = (Long) em.createQuery("SELECT count(q) FROM DrivingExam q").getSingleResult();
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("list", list);
		map.put("count", count);
		return map;
	}

	/**
	 * Get driver exam list by statement
	 * @param pagenum
	 * @param lotsize
	 * @param statement
	 * @return
	 */
	public Map<String, Object> getDriverExams(int pagenum, int lotsize,
			String statement) {
		List<DrivingExam> list=null;
		list = em.createQuery(statement)
				.setFirstResult(pagenum * lotsize - lotsize).setMaxResults(lotsize).getResultList();
		String sub_statement = statement.substring(statement.indexOf("FROM"));
		if(sub_statement.indexOf("ORDER BY") != -1)
			sub_statement = sub_statement.substring(0,sub_statement.indexOf("ORDER BY"));
		Long count = (Long) em.createQuery("SELECT count(q) "+sub_statement).getSingleResult();
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("list", list);
		map.put("count", count);
		return map;
	}

	/**
	 * Get the Application by ID
	 * @param appId
	 * @return
	 */
	public HRApplication getApplicationById(String appId) throws Exception{
		HRApplication app = em.find(HRApplication.class, Integer.parseInt(appId));
		return app;
	}

	/**
	 * Apply for a exam, if pre-exam exists examtime plus 1 and update exams content
	 * @param exam
	 */
	@Transactional
	public void applyExam(DrivingExam exam) throws Exception{
		if(exam.getApplicant() == null)
			throw new Exception("No applicant specified.");
		
		DrivingExam preExam = null;
		try{
			preExam = (DrivingExam) em.createQuery("SELECT q FROM DrivingExam q WHERE q.applicant.id = ?").setParameter(1, exam.getApplicant().getId()).getSingleResult();
		}catch(Exception e){
			preExam = null;
		}
		if(preExam != null){
			exam.setId(preExam.getId());
			exam.setExamtimes(preExam.getExamtimes()+1);
			em.merge(exam);
		}else{
			em.persist(exam);
		}
		em.flush();
		HRApplication applicant = exam.getApplicant();
		applicant.setDriverexamresult(3);
		em.merge(applicant);
		return;
	}

	/**
	 * Update the application infomation
	 * @param applicant
	 */
	@Transactional
	public void mergeApplication(HRApplication applicant) throws Exception{
		em.merge(applicant);
	}

	/**
	 * Delete an application 
	 * @param targetId
	 */
	@Transactional
	public String deleteApplication(String targetId) throws Exception{
		HRApplication app = em.find(HRApplication.class, Integer.parseInt(targetId));
		em.createQuery("DELETE FROM DrivingExam q WHERE q.applicant.id=?").setParameter(1, app.getId()).executeUpdate();
		em.createQuery("DELETE FROM IdCardsMapper q WHERE q.applicant.id=?").setParameter(1, app.getId()).executeUpdate();
		em.createQuery("DELETE FROM Training q WHERE q.applicant.id=?").setParameter(1, app.getId()).executeUpdate();
//		em.createQuery("DELETE FROM WorkArrangement q WHERE q.applicant.id=?").setParameter(1, app.getId()).executeUpdate();
		String name = app.getName();
		em.remove(app);
		return name;
	}

	/**
	 * Get emp request by iD
	 * @param targetId
	 * @return
	 * @throws Exception
	 */
	public EmployeeRequest getEmpRequestById(String targetId) throws Exception {
		return em.find(EmployeeRequest.class, Integer.parseInt(targetId));
	}

	/**
	 * Update Employee request information
	 * @param empRequest
	 * @throws Exception
	 */
	@Transactional
	public void mergeEmpRequest(EmployeeRequest empRequest) throws Exception{
		if(empRequest.getId() == null)
			throw new Exception("Id cannot be null");
		em.merge(empRequest);
	}

	/**
	 * Get DrivingExam instance by id
	 * @param targetId
	 * @return
	 * @throws Exception
	 */
	public DrivingExam getDriverExamById(String targetId) throws Exception{
		return em.find(DrivingExam.class, Integer.parseInt(targetId));
	}

	/**
	 * Update DrivingExam instance
	 * @param de
	 * @throws Exception
	 */
	@Transactional
	public void mergeDriverExam(DrivingExam de) throws Exception{
		if(de.getId() == null)
			throw new Exception("Id is not given.");
		em.merge(de);
	}

	/**
	 * Delete driver exam
	 * @param de
	 * @throws Exception
	 */
	@Transactional
	public void removeDriverExam(String targetId) throws Exception{
		if(targetId == null)
			return;
		DrivingExam de = em.find(DrivingExam.class, Integer.parseInt(targetId));
		HRApplication hra = de.getApplicant();
		hra.setDriverexamresult(0);
		em.merge(hra);
		em.remove(de);
	}

	/**
	 * Update the exam status of applicant on merge of driving exam
	 * @param de
	 * @throws Exception
	 */
	@Transactional
	public void updateAppExamStatus(DrivingExam de) throws Exception{
		if(de == null || de.getZhuangPass() == null || de.getRoadPass() == null)
			return;
		HRApplication app = de.getApplicant();
		if(de.getZhuangPass() == ApplicationConstants.EXAM_PASS && de.getRoadPass() == ApplicationConstants.EXAM_PASS){
			app.setDriverexamresult(ApplicationConstants.EXAM_PASS);
			em.merge(app);
		}else if(de.getZhuangPass() == ApplicationConstants.EXAM_FAIL || 
				de.getRoadPass() == ApplicationConstants.EXAM_FAIL){
			app.setDriverexamresult(ApplicationConstants.EXAM_FAIL);
			em.merge(app);
		}
		return;
	}

	/**
	 * Delete a employee request form by id
	 * @param targetId
	 * @throws Exception
	 */
	@Transactional
	public void removeEmployeeRequest(String targetId) throws Exception{
		if (targetId == null)
			throw new Exception("Id can not be null.");
		EmployeeRequest er = em.find(EmployeeRequest.class, Integer.parseInt(targetId));
		em.remove(er);
	}

	/**
	 * Get all id cards 
	 * @return
	 * @throws Exception
	 */
	public List<ApplicationIdCards> getApplicationIDCards() throws Exception{
		List<ApplicationIdCards> cards = null;
		try{
			cards = em.createQuery("SELECT q FROM ApplicationIdCards q ORDER BY name").getResultList();
		}catch(Exception e){
			System.out.println(e.getMessage());
			cards = new ArrayList<ApplicationIdCards>();
		}
		return cards;
	}

	/**
	 * Add a card to database
	 * @param card
	 */
	@Transactional
	public void createIdCard(ApplicationIdCards card) throws Exception{
		if(card.getName() == null || card.getName().equals(""))
			throw new Exception("Name must not empty");
		em.persist(card);
	}

	/**
	 * Delete a card
	 * @param targetId
	 * @throws Exception
	 */
	@Transactional
	public void deleteIdCard(String targetId) throws Exception{
		if(targetId == null)
			throw new Exception("Id must be given");
		ApplicationIdCards card = em.find(ApplicationIdCards.class, Integer.parseInt(targetId));
		em.remove(card);
	}

	/**
	 * Add idcard to applicant
	 * @param id
	 * @param card
	 */
	@Transactional
	public void addCardToApplicant(Integer id, String card) throws Exception{
		if(id == null || card == null)
			return;
		ApplicationIdCards idcard = em.find(ApplicationIdCards.class, Integer.parseInt(card));
		HRApplication applicant = em.find(HRApplication.class, id);
		IdCardsMapper mapper = null;
		try{
			mapper = (IdCardsMapper) em.createQuery("SELECT q FROM IdCardsMapper q WHERE idCard=:idcard AND applicant=:app")
					.setParameter("idcard", idcard).setParameter("app", applicant).getSingleResult();
		}catch(Exception e){
			System.out.println(e.getMessage());
			mapper = null;
		}
		if(mapper == null){
			mapper  = new IdCardsMapper();
			mapper.setApplicant(applicant);
			mapper.setIdCard(idcard);
			em.persist(mapper);
		}
		return;
	}

	/**
	 * Get all assign id cards for given applicant
	 * @return
	 * @throws Exception
	 */
	public List<String> getAssignCards(HRApplication applicant){
		try{
		List<IdCardsMapper> mappers = em.createQuery("SELECT q FROM IdCardsMapper q WHERE applicant=:app")
				.setParameter("app", applicant).getResultList();
		List<String> ret = new ArrayList<String>();
		for(IdCardsMapper map : mappers){
			ret.add(map.getIdCard().getId()+"");
		}
		return ret;
		}catch(Exception e){
			return new ArrayList<String>();
		}
	}

	/**
	 * Update a card status to the applicant
	 * @param id
	 * @param card
	 * @throws Exception
	 */
	@Transactional
	public void updateCardToApplicant(Integer id, List<String> cards) throws Exception{
		if(cards == null){
			cards = new ArrayList<String>();
		}
		HRApplication applicant = em.find(HRApplication.class, id);
		List<String> assignedCards = this.getAssignCards(applicant);
		for(String aId:assignedCards){
			boolean exists = false;
			for(String newId:cards){
				if(aId.equals(newId)){
					exists = true;
					break;
				}
			}
			if(!exists){
				ApplicationIdCards idcard = em.find(ApplicationIdCards.class, Integer.parseInt(aId));
				IdCardsMapper mapper = (IdCardsMapper) em.createQuery("SELECT q FROM IdCardsMapper q WHERE idCard=:idcard AND applicant=:app")
						.setParameter("idcard", idcard).setParameter("app", applicant).getSingleResult();
				em.remove(mapper);
			}
		}
		for(String newId:cards){
			boolean exists = false;
			for(String aId:assignedCards){
				if(newId.equals(aId)){
					exists = true;
					break;
				}
			}
			if(!exists){
				IdCardsMapper mapper  = new IdCardsMapper();
				ApplicationIdCards idcard = em.find(ApplicationIdCards.class, Integer.parseInt(newId));
				mapper.setApplicant(applicant);
				mapper.setIdCard(idcard);
				em.persist(mapper);
			}
		}
	}
}
