package com.bus.services;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLDecoder;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.management.Query;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceUtil;
import javax.persistence.SqlResultSetMapping;
import javax.persistence.spi.PersistenceProvider;

import org.springframework.transaction.annotation.Transactional;

import com.bus.dto.Account;
import com.bus.dto.Contract;
import com.bus.dto.Department;
import com.bus.dto.Employee;
import com.bus.dto.Ethnic;
import com.bus.dto.Fixoptions;
import com.bus.dto.Hrimage;
import com.bus.dto.Idmanagement;
import com.bus.dto.Position;
import com.bus.dto.Promoandtransfer;
import com.bus.dto.Qualification;
import com.bus.dto.Workertype;
import com.bus.dto.vehicleprofile.VehicleTeam;
import com.bus.util.EmployeeStatus;
import com.bus.util.HRUtil;

public class HRBean{

	@PersistenceContext
	protected EntityManager em;

	public EntityManager getEntityManager() {
		return this.em;
	}

	public void setEntityManager(EntityManager entityManager) {
		this.em = entityManager;
	}

	public HRBean() {
	}

	public Object getObjectByClassAndId(Class cls, int id) {
		Object obj = em.find(cls, id);
		return obj;
	}

	public Account login(Account account) {
		// String pass = HRUtil.getStringMD5(account.getPassword() +
		// accountKey);
		Account acc;
		try {
			acc = (Account) em
					.createQuery(
							"SELECT a FROM Account a WHERE username=? AND password=?")
					.setParameter(1, account.getUsername())
					.setParameter(2, account.getPassword()).getSingleResult();
			if (acc == null) {
				acc = new Account();
			}
			return acc;
		} catch (Exception e) {
			// e.printStackTrace();
			System.out.println("No user found");
			return new Account();
		}
	}

	@Transactional
	public void saveEmployee(Employee employee) {
		Department department = em.find(Department.class, employee
				.getDepartment().getId());
		Position position = em.find(Position.class, employee.getPosition()
				.getId());
		if(employee.getTeam().getId() != null){
			VehicleTeam team = em.find(VehicleTeam.class, employee.getTeam().getId());
			employee.setTeam(team);
		}
		Account account = em.find(Account.class, employee.getAccount().getId());
//		Hrimage image = em.find(Hrimage.class, employee.getHrimage().getId());
		employee.setAccount(account);
		employee.setPosition(position);
		employee.setDepartment(department);
//		employee.setHrimage(image);
		employee.setCreatetime(new Date());
		employee.setStatus("A");
		em.persist(employee);
	}

	@Transactional
	public boolean deleteEmployee(Employee e) {
		Employee emp = (Employee) em.find(Employee.class, e.getId());
		emp.setStatus("D");
		em.merge(emp);
		return true;
	}

	/**
	 * Cocy contains into new employee
	 * @param e
	 */
	@Transactional
	public void updateEmployee(Employee e) {
		Employee now = getEmployeeById(e.getId() + "");
		now.copy(e);
		em.merge(now);
	}
	
	@Transactional
	public void updateEmployeeOriginal(Employee e) throws Exception{
		em.merge(e);
	}

	@Transactional
	public Employee createEmployeeAndContract(Employee e, Contract c)
			throws Exception {
			Department d = getDepartmentByName(e.getDepartment().getName());
			e.setDepartment(d);
			Position p = getPositionByName(e.getPosition().getName());
			e.setPosition(p);
			Hrimage image = em.find(Hrimage.class, 0);
			e.setImage(image);
			if (e.getStatus() == null || e.getStatus().equals(""))
				e.setStatus("A");
			e.setCreatetime(e.getFirstworktime() == null ? new Date() : e
					.getFirstworktime());
			em.persist(e);
			em.flush();

			if (c != null) {
				if (c.getStatus() == null)
					c.setStatus("A");
				c.setEmployee(e);
				c.setCreatedate(new Date());
				em.persist(c);
			}
			return e;
	}

	public List<Employee> getAllEmployee(String type) {
		try {
			List<Employee> list = (List<Employee>) em
					.createQuery("SELECT q FROM Employee q WHERE status=?")
					.setParameter(1, type).getResultList();
			return list;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public Employee employeeExist(String name, String workerid) {
		try {
			if (workerid != null) {
				List<Employee> elist = (List<Employee>) em
						.createQuery(
								"SELECT q FROM Employee q WHERE fullname=? AND workerid=?")
						.setParameter(1, name).setParameter(2, workerid)
						.getResultList();
				if (elist.size() < 1) {
					return null;
				} else {
					Employee e = elist.get(0);
					if (e == null)
						return null;
					else
						return e;
				}
			} else {
				Employee e = (Employee) em
						.createQuery(
								"SELECT q FROM Employee q WHERE fullname=?")
						.setParameter(1, name).getSingleResult();
				if (e != null)
					return e;
				else
					return null;
			}
		} catch (Exception err) {
			return null;
		}
	}

	public List<String> getCurrentDocumentIds(){
		try{
			List<Employee> es = (List<Employee>) em.createQuery("SELECT q FROM Employee q").getResultList();
			if(es.size() < 1)
				return null;
			List<String> strs = new ArrayList<String>();
			for(Employee e:es){
				strs.add(e.getDocumentkey());
			}
			return strs;
		}catch(Exception e){
			return null;
		}
	}
	
	public boolean isEmployeeWorkerIdExist(String workerid) {
		try {
			List<Employee> elist = (List<Employee>) em
					.createQuery("SELECT q FROM Employee q WHERE workerid=?")
					.setParameter(1, workerid).getResultList();
			if (elist.size() < 1) {
				return false;
			} else {
				Employee e = elist.get(0);
				if (e == null)
					return false;
				else
					return true;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	public List<Employee> getEmployees(int pagenum, int lotsize) {
		List<Employee> list = em
				.createQuery(
						"SELECT q FROM Employee q WHERE status='A' ORDER BY createtime DESC")
				.setFirstResult(pagenum * lotsize - lotsize).setMaxResults(lotsize).getResultList();
		return list;
	}

	public Employee getEmployeeById(String _id) {
		Integer id = Integer.parseInt(_id);
		if (id == null)
			return null;
		Employee e = em.find(Employee.class, id);
		return e;
	}

	public Employee getEmployeeByWorkerId(String _id){
		try{
			Employee e = (Employee) em.createQuery("SELECT q FROM Employee q WHERE workerid=?").setParameter(1, _id).getSingleResult();
			return e;
		}catch(Exception e){
			return null;
		}
	}
	
	public List<Employee> getEmployeeByName(String name, boolean all) {
		try {
			List<Employee> list = null;
			if (!all)
				list = em
						.createQuery(
								"SELECT q FROM Employee q WHERE status=? AND fullname LIKE ?")
						.setParameter(1, "A").setParameter(2, "%" + name + "%")
						.getResultList();
			else
				list = em
						.createQuery(
								"SELECT q FROM Employee q WHERE fullname LIKE ?")
						.setParameter(1, "%" + name + "%").getResultList();
			return list;
		} catch (Exception e) {
			// System.out.println(e.getMessage());
			return null;
		}

	}

	public Employee getEmployeeByNameAndBod(String name, Date dob) {
		try {
			Employee e = (Employee) em
					.createQuery(
							"SELECT q FROM Employee q WHERE fullname=? AND dob=?")
					.setParameter(1, name).setParameter(2, dob)
					.getSingleResult();
			return e;
		} catch (Exception e) {
			System.out.println(e.getMessage());
			return null;
		}
	}

	public Employee getEmployeeByNameAndWorkerid(String name, String workerid) {
		try {
			Employee e = (Employee) em
					.createQuery(
							"SELECT q FROM Employee q WHERE fullname=? AND workerid=?")
					.setParameter(1, name).setParameter(2, workerid)
					.getSingleResult();
			return e;
		} catch (Exception e) {
			// System.out.println(e.getMessage());
			return null;
		}
	}

	public Map<String, Object> getEmployeesBySelector(int pagenum, int lotsize,
			String statement) {
		List<Employee> list = null;
		if(pagenum == -1 && lotsize == -1)
			list = em.createQuery(statement).getResultList();
		else
			list = em.createQuery(statement)
				.setFirstResult(pagenum * lotsize - lotsize)
				.setMaxResults(lotsize).getResultList();
		String str = "";
		if (statement.indexOf("ORDER BY") == -1)
			str = statement.substring(statement.indexOf("FROM"));
		else
			str = statement.substring(statement.indexOf("FROM"),
					statement.indexOf("ORDER BY"));
		str = "SELECT COUNT(q) " + str;
		Long count = (Long) em.createQuery(str).getSingleResult();
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("count", count);
		map.put("list", list);
		return map;
	}

	public Long countEmployees() {
		Long count = (Long) em
				.createQuery("SELECT COUNT(q) FROM Employee q WHERE status=?")
				.setParameter(1, "A").getSingleResult();
		return count;
	}

	@Transactional
	public Department saveDepartment(Department department) throws Exception {
		try {
			Department d = getDepartmentByName(department.getName());
			return d;
		} catch (Exception e) {
			try {
				em.persist(department);
				return department;
			} catch (Exception e2) {
				throw new Exception("Department not saved." + e2.getMessage());
			}
		}
	}

	@Transactional
	public boolean deleteDepartment(Department department) {
		List<Employee> list = em
				.createQuery("SELECT d FROM Employee d WHERE departmentid=?")
				.setParameter(1, department.getId()).setMaxResults(1)
				.getResultList();
		if (list == null || list.size() == 0) {
			Department entity = (Department) em.find(Department.class,
					department.getId());
			em.remove(entity);
			return true;
		} else {
			return false;
		}
	}

	public List<Department> getAllDepartment() {
		List<Department> departments = em.createQuery(
				"SELECT d FROM Department d").getResultList();
		return departments;
	}

	public Department getDepartmentByName(String name) throws Exception {
		try {
			Department d = (Department) em
					.createQuery("SELECT d FROM Department d WHERE name=?")
					.setParameter(1, name).getSingleResult();
			return d;
		} catch (Exception e) {
			throw new Exception("No Deparment Found " + e.getMessage());
		}
	}

	@Transactional
	public Position savePosition(Position position) throws Exception {
		em.persist(position);
		return position;
	}

	@Transactional
	public boolean deletePosition(Position position) {
		List<Employee> list = em
				.createQuery("SELECT d FROM Employee d WHERE positionid=?")
				.setParameter(1, position.getId()).setMaxResults(1)
				.getResultList();
		if (list == null || list.size() == 0) {
			Position entity = (Position) em.find(Position.class,
					position.getId());
			em.remove(entity);
			return true;
		} else {
			return false;
		}
	}

	public List<Position> getAllPosition() {
		List<Position> positions = em.createQuery("SELECT p FROM Position p ORDER BY p.name")
				.getResultList();
		return positions;
	}

	public Position getPositionByName(String name) throws Exception {
		try {
			Position p = (Position) em
					.createQuery("SELECT d FROM Position d WHERE name=?")
					.setParameter(1, name).getSingleResult();
			return p;
		} catch (Exception e) {
			throw new Exception("get position fail.Not exist." + e.getMessage());
		}
	}

	@Transactional
	public void saveContract(Contract contract) {
		Employee e = em.find(Employee.class, contract.getEmployee().getId());
		contract.setCreatedate(new Date());
		contract.setStatus("A");
		contract.setEmployee(e);
		em.persist(contract);
	}

	@Transactional
	public void updateContract(Contract contract) {
		Contract c = em.find(Contract.class, contract.getId());
		c.copy(contract);
		em.merge(c);
	}

	@Transactional
	public void removeContract(int id) {
		Contract c = em.find(Contract.class, id);
		c.setStatus("D");
		em.merge(c);
	}

	public Long countContracts() {
		Long count = (Long) em
				.createQuery("SELECT COUNT(q) FROM Contract q WHERE status=?")
				.setParameter(1, "A").getSingleResult();
		return count;
	}

	public List<Contract> getContracts(int pagenum, int lotsize) {
		List<Contract> list = em
				.createQuery(
						"SELECT q FROM Contract q WHERE status=? AND q.employee.status != 'D' ORDER BY createdate DESC")
				.setFirstResult(pagenum * lotsize - lotsize)
				.setParameter(1, "A").setMaxResults(lotsize).getResultList();
		return list;
	}

	public List<Contract> getContractsByEmployeeId(int id) {
		List<Contract> list = em
				.createQuery(
						"SELECT q FROM Contract q WHERE employeeid=? ORDER BY createdate DESC")
				.setParameter(1, id).getResultList();
		return list;
	}

	public List<Contract> getContractsByName(int pagenum, int lotsize,
			String ename) {
		List<Employee> employees = getEmployeeByName(ename, false);
		List<Contract> tempc = null;
		if (employees == null) {
			return new ArrayList<Contract>();
		} else {
			List<Contract> contracts = new ArrayList<Contract>();
			for (int i = 0; i < employees.size(); i++) {
				tempc = getContractsByEmployeeId(employees.get(i).getId());
				contracts.addAll(tempc);
			}
			return contracts;
		}
	}

	public Map getContractsBySelector(int pagenum, int lotsize,
			String statement) throws Exception {
		List<Contract> list = new ArrayList<Contract>();
			list = em.createQuery(statement).setFirstResult(pagenum * lotsize - lotsize).setMaxResults(lotsize)
					.getResultList();
			int endIndex = statement.length();
			if(statement.indexOf("ORDER BY") != -1)
				endIndex = statement.indexOf("ORDER BY");
			String substatement = statement.substring(statement.indexOf("FROM"), endIndex);
			substatement = "SELECT count(*) "+ substatement;
			Long count = (Long) em.createQuery(substatement).getSingleResult();
			HashMap<String,Object> map = new HashMap<String,Object>();
			map.put("list", list);
			map.put("count", count);
		return map;
	}

	@Transactional
	public void saveIdcard(Idmanagement idcard) {
		em.persist(idcard);
	}

	public List<Idmanagement> getIdcardsByEmployeeId(String id) {
		try {
			List<Idmanagement> list = em
					.createQuery(
							"SELECT q FROM Idmanagement q WHERE employeeid=? ORDER BY id ASC")
					.setParameter(1, Integer.parseInt(id)).getResultList();
			return list;
		} catch (Exception e) {
			return new ArrayList<Idmanagement>();
		}
	}

	/**
	 * Get id card from a given type and number
	 * 
	 * @param type
	 * @param num
	 * @return
	 * @throws Exception 
	 */
	public Idmanagement getIdCard(String type, String num) throws Exception {
		try {
			Idmanagement id = (Idmanagement) em
					.createQuery(
							"SELECT q FROM Idmanagement q WHERE type=? AND number=?")
					.setParameter(1, type).setParameter(2, num)
					.getSingleResult();
			return id;
		} catch (Exception e) {
			return null;
		}
	}

	@Transactional
	public void deleteIdcard(String cardId) throws Exception{
			Idmanagement idcard = em.find(Idmanagement.class,
					Integer.parseInt(cardId));
			em.remove(idcard);
	}

	public Long countIdcards() {
		try {
			Long count = (Long) em.createQuery(
					"SELECT count(q) FROM Idmanagement q").getSingleResult();
			return count;
		} catch (Exception e) {
			System.out.println(e.getMessage());
			return null;
		}
	}

	public Map<String, Object> getIdmanagements(int pagenum, int lotsize) {
		try {
			Long count = (Long) em
					.createQuery(
							"SELECT count(q) FROM Idmanagement q WHERE q.employee.status != 'D' AND q.employee.status != 'E'")
					.getSingleResult();
			List<Idmanagement> list = em
					.createQuery(
							"SELECT q FROM Idmanagement q WHERE q.employee.status != 'D' AND q.employee.status != 'E'")
					.setFirstResult(pagenum * lotsize - lotsize)
					.setMaxResults(lotsize).getResultList();
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("count", count);
			map.put("list", list);
			return map;
		} catch (Exception e) {
			System.out.println(e.getMessage());
			return null;
		}
	}

	public Map<String, Object> getIdcardsBySelector(int pagenum, int lotsize,
			String statement) {
		try {
			String str = null;
			if (statement.indexOf("ORDER BY") != -1)
				str = statement.substring(statement.indexOf("FROM"),
						statement.indexOf("ORDER BY"));
			else
				str = statement.substring(statement.indexOf("FROM"));
			str = "SELECT COUNT(q) " + str;
			Long count = (Long) em.createQuery(str).getSingleResult();
			List<Idmanagement> list = em.createQuery(statement)
					.setFirstResult(pagenum * lotsize - lotsize)
					.setMaxResults(lotsize).getResultList();
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("count", count);
			map.put("list", list);
			return map;
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println(e.getMessage());
			return null;
		}
	}

	@Transactional
	public String saveCoordination(Promoandtransfer coordinate) {
		String str = "";
		try {
			Employee e = (Employee) em
					.createQuery(
							"SELECT q FROM Employee q WHERE fullname=? AND workerid=?")
					.setParameter(1, coordinate.getEmployee().getFullname())
					.setParameter(2, coordinate.getEmployee().getWorkerid())
					.getSingleResult();
			Position prep = null;
			Position curp = null;
			Department pred = null;
			Department curd = null;
			if (coordinate.getPreposition() != null)
				prep = (Position) em.find(Position.class, coordinate
						.getPreposition().getId());
			if (coordinate.getCurposition() != null)
				curp = (Position) em.find(Position.class, coordinate
						.getCurposition().getId());
			if (coordinate.getPredepartment() != null)
				pred = (Department) em.find(Department.class, coordinate
						.getPredepartment().getId());
			if (coordinate.getCurdepartment() != null)
				curd = (Department) em.find(Department.class, coordinate
						.getCurdepartment().getId());
			coordinate.setPredepartment(pred);
			coordinate.setCurdepartment(curd);
			coordinate.setPreposition(prep);
			coordinate.setCurposition(curp);
			coordinate.setEmployee(e);
			coordinate.setCreatedate(new Date());

			if (isCoordinatorExist(coordinate) == null){
				e.setDepartment(curd);
				e.setPosition(curp);
				em.merge(e);
				em.flush();
				em.persist(coordinate);
			}
			
			str = "创建成功";
			
		} catch (Exception e) {
			e.printStackTrace();
			str = "创建失败，可能员工不存在." + e.getMessage();
		}
		return str;
	}

	private Promoandtransfer isCoordinatorExist(Promoandtransfer c) {
		try {
			if (c.getId() == null) {
				Promoandtransfer coor = (Promoandtransfer) em
						.createQuery(
								"SELECT q FROM Promoandtransfer q WHERE "
										+ "employeeid=? AND predepartmentid=? AND curdepartmentid=?"
										+ " AND prepositionid=? AND curpositionid=?")
						.setParameter(1, c.getEmployee().getId())
						.setParameter(2, c.getPredepartment().getId())
						.setParameter(3, c.getCurdepartment().getId())
						.setParameter(4, c.getPreposition().getId())
						.setParameter(5, c.getCurposition().getId())
						.getSingleResult();
				// System.out.println(coor);
				return coor;
			} else {
				Promoandtransfer coor = em.find(Promoandtransfer.class,
						c.getId());
				return coor;
			}
		} catch (Exception e) {
			// System.out.println(e.getMessage() + ". Coordination not exist.");
			return null;
		}
	}

	public Promoandtransfer isCoordinatorExistForResign(Promoandtransfer c) {
		try {
			if (c.getId() == null) {
				Promoandtransfer coor = (Promoandtransfer) em
						.createQuery(
								"SELECT q FROM Promoandtransfer q WHERE "
										+ "q.employee.fullname=? AND q.type='离职' AND activedate=?")
						.setParameter(1, c.getEmployee().getFullname())
						.setParameter(2, c.getActivedate())
						.getResultList().get(0);
				return coor;
			} else {
				Promoandtransfer coor = em.find(Promoandtransfer.class,
						c.getId());
				return coor;
			}
		} catch (Exception e) {
//			System.out.println(e.getMessage() + ". Coordination not exist.");
			return null;
		}
	}

	public Map<String,Object> getAllCoordinators(int pagenum, int lotsize) {
		try {
			Long count = (Long) em.createQuery("SELECT count(q) FROM Promoandtransfer q").getSingleResult();
			List<Promoandtransfer> list = em
					.createQuery(
							"SELECT q FROM Promoandtransfer q ORDER BY createdate DESC")
					.setFirstResult(pagenum * lotsize - lotsize)
					.setMaxResults(lotsize).getResultList();
			Map<String,Object> map = new HashMap<String,Object>();
			map.put("count", count);
			map.put("list", list);
			return map;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * From file driver license
	 * 
	 * @param driverLicense
	 * @param serviceLicense
	 * @param service
	 * @throws Exception 
	 */
	@Transactional
	public void saveIdsFromFile(Idmanagement driverLicense,
			Idmanagement serviceLicense, Idmanagement service) throws Exception {
		try {
			if (driverLicense != null
					&& getIdCard(driverLicense.getType(),
							driverLicense.getNumber()) == null) {
				em.persist(driverLicense);
			}
			if (serviceLicense != null
					&& getIdCard(serviceLicense.getType(),
							serviceLicense.getNumber()) == null) {
				em.persist(serviceLicense);
			}
			if (service != null
					&& getIdCard(service.getType(), service.getNumber()) == null) {
				em.persist(service);
			}
		} catch (Exception e) {
			throw new Exception("Cannot create IDs."+e.getMessage());
		}
	}

	/**
	 * Get the option list provided id in database
	 * 
	 * @param id
	 * @return
	 */
	public Fixoptions getOptionListById(int id) {
		try {
			Fixoptions fo = em.find(Fixoptions.class, id);
			return fo;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * Get the option list for qualification
	 * 
	 * @return
	 */
	public List<Qualification> getQualificationList() {
		try {
			List<Qualification> list = (List<Qualification>) em.createQuery(
					"SELECT q FROM Qualification q").getResultList();
			return list;
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * Get the option list for workertype
	 * 
	 * @return
	 */
	public List<Workertype> getWorkertypeList() {
		try {
			List<Workertype> list = (List<Workertype>) em.createQuery(
					"SELECT q FROM Workertype q").getResultList();
			return list;
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * get the option list for ethnic
	 * 
	 * @return
	 */
	public List<Ethnic> getEthnicList() {
		try {
			List<Ethnic> list = (List<Ethnic>) em.createQuery(
					"SELECT q FROM Ethnic q").getResultList();
			return list;
		} catch (Exception e) {
			return null;
		}
	}

	public List<Employee> getAllDrivers() {
		try {
			List<Employee> list = (List<Employee>) em
					.createQuery(
							"SELECT q FROM Employee q WHERE q.position.name LIKE ?")
					.setParameter(1, "%驾驶%").getResultList();
			return list;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public List<Promoandtransfer> getResignEmployeeByCoordinationDate(
			Date startdate, Date enddate) {
		try {
			//不用检查q.employee.status因为直接由记录在promoandtransfer里
			List<Promoandtransfer> list = (List<Promoandtransfer>) em
					.createQuery(
							"SELECT q FROM Promoandtransfer q WHERE type=? AND"
									+ " activedate BETWEEN ? AND ?")
					.setParameter(1, "离职")
					.setParameter(2, startdate).setParameter(3, enddate)
					.getResultList();
			return list;
		} catch (Exception e) {
			e.printStackTrace();
			return new ArrayList<Promoandtransfer>();
		}
	}

	public List<Employee> getEmployeeOfferredByDate(Date startdate, Date enddate) {
		try {
			List<Employee> list = (List<Employee>) em
					.createQuery(
							"SELECT q FROM Employee q WHERE status=? "
									+ " AND firstworktime BETWEEN ? AND ?")
					.setParameter(1, "A").setParameter(2, startdate)
					.setParameter(3, enddate).getResultList();
			return list;
		} catch (Exception e) {
			e.printStackTrace();
			return new ArrayList<Employee>();
		}
	}

	public List<Promoandtransfer> getCoordinationsByDate(Date startdate,
			Date enddate) {
		try {
			List<Promoandtransfer> list = (List<Promoandtransfer>) em
					.createQuery(
							"SELECT q FROM Promoandtransfer q WHERE type=? AND"
									+ " activedate BETWEEN ? AND ?")
					.setParameter(1, "调动").setParameter(2, startdate)
					.setParameter(3, enddate).getResultList();
			return list;
		} catch (Exception e) {
			e.printStackTrace();
			return new ArrayList<Promoandtransfer>();
		}
	}

	@Transactional
	public void resignEmployee(String targetId, Date resigndate, String remark) {
		try {
			Employee e = em.find(Employee.class, Integer.parseInt(targetId));
			if (e == null)
				throw new Exception("Employee not exist with target id:"
						+ targetId);
			Promoandtransfer coor = new Promoandtransfer();
			coor.setEmployee(e);
			coor.setType("离职");
			coor.setMovedate(resigndate);
			coor.setActivedate(resigndate);
			coor.setCreatedate(new Date());
			coor.setRemark(remark);
			em.persist(coor);
			e.setStatus(EmployeeStatus.RESIGN);
			for (Contract c : e.getContracts()) {
				if (c.getStatus().equals(EmployeeStatus.ACTIVE)) {
					c.setStatus(EmployeeStatus.RESIGN);
					em.merge(c);
				}
			}
			em.merge(e);
		} catch (Exception e) {
			e.printStackTrace();
			return;
		}

	}
	
	@Transactional
	public void reJoinEmployee(String targetId, Date resigndate) {
		try {
			Employee e = em.find(Employee.class, Integer.parseInt(targetId));
			if (e == null)
				throw new Exception("Employee not exist with target id:"
						+ targetId);
			e.setStatus(EmployeeStatus.ACTIVE);
			e.setTransfertime(resigndate);
			e.setFirstworktime(resigndate);
			em.merge(e);
		} catch (Exception e) {
			e.printStackTrace();
			return;
		}
	}

	public boolean confirmDuplicated(Employee e) {
		try{
			List<Employee> list = (List<Employee>)em.createQuery("SELECT q FROM Employee q WHERE fullname=?").setParameter(1, e.getFullname());
			if(list.size()>1)
				return true;
			return false;
		}catch(Exception err){
			return false;
		}
	}

	public Map<String, Object> getCoordinationBySelector(int pagenum,
			int lotsize, String statement) {
		List<Promoandtransfer> list = (List<Promoandtransfer>)em.createQuery(statement)
				.setFirstResult(pagenum * lotsize - lotsize)
				.setMaxResults(lotsize).getResultList();
		String str = "";
		if (statement.indexOf("ORDER BY") == -1)
			str = statement.substring(statement.indexOf("FROM"));
		else
			str = statement.substring(statement.indexOf("FROM"),
					statement.indexOf("ORDER BY"));
		str = "SELECT COUNT(q) " + str;
		Long count = (Long) em.createQuery(str).getSingleResult();
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("count", count);
		map.put("list", list);
		return map;
	}

	@Transactional
	public void saveFixOptions(Fixoptions data) throws Exception{
		em.merge(data);
	}

	public List<Fixoptions> getAllFixOptions() throws Exception{
		return em.createQuery("SELECT q FROM Fixoptions q ORDER BY id").getResultList();
	}

	@Transactional
	public void saveWorkertype(Workertype wt) throws Exception{
		em.persist(wt);
	}

	@Transactional
	public void deleteWorkerTypeByName(String optionlistselectedvalue) throws Exception{
		Workertype wt = (Workertype) em.createQuery("SELECT q FROM Workertype q WHERE name=?").setParameter(1, optionlistselectedvalue).getSingleResult();
		em.remove(wt);
	}

	@Transactional
	public void deleteFixoptionContent(int optionlistid,
			String optionlistselectedvalue) throws Exception{
		Fixoptions fo = em.find(Fixoptions.class, optionlistid);
		String data = fo.getContent();
		int startindex = 0;
		int lastindex = data.indexOf(optionlistselectedvalue) + optionlistselectedvalue.length();
		if(data.indexOf(optionlistselectedvalue) > 0)
			startindex = data.indexOf(optionlistselectedvalue) -1;
		if(startindex == 0 && lastindex < data.length())
			lastindex++;
		String newdata = data.substring(0,startindex) + 
				data.substring(lastindex);
		fo.setContent(newdata);
		em.merge(fo);
	}

	public boolean isWorkerExist(Employee employee) {
		try{
			Employee e = (Employee) em.createQuery("SELECT q FROM Employee q WHERE fullname=? AND workerid=?").setParameter(1, employee.getFullname())
			.setParameter(2, employee.getWorkerid()).getSingleResult();
			return true;
		}catch(Exception e){
			return false;
		}
	}

	@Transactional
	public void removeCoordination(int targetId) throws Exception{
		Promoandtransfer pt = em.find(Promoandtransfer.class, targetId);
		Employee e  = pt.getEmployee();
		if(pt.getType().equals("调动")){
			if(e.getDepartment().getId() == pt.getCurdepartment().getId()){
				if(e.getPosition().getId() == pt.getCurposition().getId()){
					e.setDepartment(pt.getPredepartment());
					e.setPosition(pt.getPreposition());
					em.merge(e);
					em.flush();
				}
			}
		}
		em.remove(pt);
	}

	public Promoandtransfer getCoordinationsById(int targetId) throws Exception{
		return em.find(Promoandtransfer.class, targetId);
	}

	@Transactional
	public void editCoordination(Account user, Promoandtransfer coordinate) throws Exception{
		Promoandtransfer pt = em.find(Promoandtransfer.class, coordinate.getId());
		pt.setActivedate(coordinate.getActivedate());
		pt.setMovedate(coordinate.getMovedate());
		pt.setCurdepartment(coordinate.getCurdepartment());
		pt.setPredepartment(coordinate.getPredepartment());
		pt.setCurposition(coordinate.getCurposition());
		pt.setPreposition(coordinate.getPreposition());
		pt.setType(coordinate.getType());
		pt.setRemark(coordinate.getRemark());
		
		Employee e  = pt.getEmployee();
		e.setDepartment(coordinate.getCurdepartment());
		e.setPosition(coordinate.getCurposition());
		em.merge(e);
		em.flush();
		em.merge(pt);
	}

	/**
	 * Update id card information, only validfrom, expire, remark, number can be update
	 * @param idcard
	 */
	@Transactional
	public void updateIdCatd(Idmanagement idcard) throws Exception{
		Idmanagement card = em.find(Idmanagement.class, idcard.getId());
		card.setExpiredate(idcard.getExpiredate());
		card.setValidfrom(idcard.getValidfrom());
		card.setNumber(idcard.getNumber());
		card.setRemark(idcard.getRemark());
		em.merge(card);
	}

	/**
	 * Resign a contract
	 * @param id
	 * @throws Exception
	 */
	@Transactional
	public void resignContract(int id) throws Exception{
		Contract c = em.find(Contract.class, id);
		c.setStatus("E");
		em.merge(c);
	}

	/**
	 * Get id card from idmanagement by id
	 * @param id
	 * @return
	 */
	public Idmanagement getIdCardById(Integer id) throws Exception{
		Idmanagement card = em.find(Idmanagement.class, id);
		return card;
	}

	/**
	 * Save or update the image path
	 * @param idcard2
	 */
	@Transactional
	public void saveIdCardImage(Idmanagement idcard2) throws Exception{
		Hrimage image = idcard2.getImage();
		if(image.getId() == null){
			em.persist(idcard2.getImage());
			em.flush();
			em.merge(idcard2);
			return;
		}else{
			em.merge(image);
			return;
		}
	}

	/**
	 * Delete the image and its path
	 * @param cardId
	 */
	@Transactional
	public String deleteIDCardImage(String cardId) throws Exception{
		Idmanagement card = em.find(Idmanagement.class, Integer.parseInt(cardId));
		Hrimage image = card.getImage();
		String ipath = image.getIpath();
		card.setImage(null);
		em.merge(card);
		em.flush();
		em.remove(image);
		em.flush();
		return ipath;
	}

	/**
	 * Get contract by contract id
	 * @param id
	 * @return
	 * @throws Exception
	 */
	public Contract getContractById(int id) throws Exception {
		return em.find(Contract.class, id);
	}

	/**
	 * Get all active departments. by native query
	 * @return
	 */
	public List<Department> getAllActiveDepartment() throws Exception{
		String statement = "SELECT distinct department.id AS id, department.name AS name, department.remark AS remark" +
				" FROM employee JOIN department ON employee.departmentid = department.id " +
				" WHERE employee.status='A' ORDER BY department.name";
		List<Department> departments = em.createNativeQuery(statement, Department.class).getResultList();
		return departments;
	}

	/**
	 * Get employee by department Id
	 * @param id
	 * @return
	 */
	public List<Employee> getEmployeeByDepartmentId(Integer id) throws Exception {
//		List<Employee> emp = em
//				.createQuery("SELECT q FROM Employee q WHERE q.department.id="+id+" AND q.status='A' ORDER BY q.fullname")
//				.getResultList();
		List<Employee> emp = new ArrayList<Employee>();
		List<Object[]> results = em.createNativeQuery("SELECT fullname,workerid FROM employee WHERE departmentid="+id+" AND status='A' ORDER BY fullname").getResultList();
		System.out.println("size is :"+results.size());
		for(Object[] map:results){
			Employee e = new Employee();
			e.setFullname((String) map[0]);
			e.setWorkerid((String)map[1]);
			emp.add(e);
		}
		return emp;
	}

	/**
	 * Get driver team by name
	 * @param string
	 * @return
	 */
	public VehicleTeam getDriverTeamByName(String name){
		try{
			return (VehicleTeam) em.createQuery("SELECT q FROM VehicleTeam q WHERE name=?").setParameter(1, name).getSingleResult();
		}catch(Exception e){
			System.out.println("No such driver team exist:"+name);
			return null;
		}
	}

	public List<VehicleTeam> getAllVehicleTeams() {
		return em.createQuery("SELECT q FROM VehicleTeam q").getResultList();
	}

	/**
	 * Get all vehicle teams for a department
	 * @param id
	 * @return
	 * @throws Exception
	 */
	public List<VehicleTeam> getExistingTeamsForDepartment(Integer id) throws Exception{
		return em.createQuery("SELECT distinct q FROM VehicleTeam q,Employee e WHERE e.department.id=? AND e.team.id=q.id AND e.status='A'")
				.setParameter(1, id).getResultList();
	}

	/**
	 * Get all employees for specific department team
	 * @param id
	 * @param id2
	 * @return
	 * @throws Exception
	 */
	public List<Employee> getEmployeeByDepartmentIdAndTeamId(Integer id,
			Integer id2) throws Exception{
		List<Object[]> results = em.createNativeQuery("SELECT distinct fullname,workerid FROM employee WHERE departmentid="+id+" AND teamid="+id2+" AND status='A'" ).getResultList();
		List<Employee> ret = new ArrayList<Employee>();
		for(Object[] o : results){
			Employee e = new Employee();
			e.setFullname((String) o[0]);
			e.setWorkerid((String) o[1]);
			ret.add(e);
		}
		return ret;
	}

	/**
	 * Get all employees that have no team in a department
	 * @param id
	 * @return
	 */
	public List<Employee> getEmployeeByDepartmentIdAndNoTeam(Integer id) {
		List<Object[]> results = em.createNativeQuery("SELECT distinct fullname,workerid FROM employee  WHERE departmentid='"+id+"' AND teamid IS NULL AND status='A'").getResultList();
		List<Employee> ret = new ArrayList<Employee>();
		for(Object[] o : results){
			Employee e = new Employee();
			e.setFullname((String) o[0]);
			e.setWorkerid((String) o[1]);
			ret.add(e);
		}
		return ret;
	}

}
