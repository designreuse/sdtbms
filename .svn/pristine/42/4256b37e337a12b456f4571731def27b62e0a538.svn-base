package com.bus.services;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.management.Query;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.transaction.annotation.Transactional;

import com.bus.dto.Account;
import com.bus.dto.Contract;
import com.bus.dto.Department;
import com.bus.dto.Employee;
import com.bus.dto.Hrimage;
import com.bus.dto.Idmanagement;
import com.bus.dto.Position;
import com.bus.dto.Promoandtransfer;
import com.bus.util.HRUtil;

public class HRBean {

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

	public Object getObjectByClassAndId(Class cls, int id){
		Object obj = em.find(cls, id);
		return obj;
	}
	
	public Account login(Account account){
//		String pass = HRUtil.getStringMD5(account.getPassword() + accountKey);
		Account acc ;
		try{
			acc = (Account) em.createQuery("SELECT a FROM Account a WHERE username=? AND password=?").setParameter(1, account.getUsername()).setParameter(2, account.getPassword()).getSingleResult();
			if(acc == null) {
				acc = new Account();
			}
			return acc;
		}catch(Exception e){
//			e.printStackTrace();
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
		Account account = em.find(Account.class, employee.getAccount().getId());
		Hrimage image = em.find(Hrimage.class, employee.getHrimage().getId());
		employee.setAccount(account);
		employee.setPosition(position);
		employee.setDepartment(department);
		employee.setHrimage(image);
		employee.setCreatetime(new Date());
		employee.setStatus("A");
		em.persist(employee);
	}
	
	@Transactional
	public boolean deleteEmployee(Employee e){
		Employee emp = (Employee)em.find(Employee.class, e.getId());
		emp.setStatus("D");
		em.merge(emp);
		return true;
	}
	
	@Transactional
	public void updateEmployee(Employee e){
		Employee now = getEmployeeById(e.getId()+"");
		now.copy(e);
		em.merge(now);
	}
	
	@Transactional
	public void createEmployeeAndContract(Employee e, Contract c){
		Department d = getDepartmentByName(e.getDepartment().getName());
		e.setDepartment(d);
		Position p = getPositionByName(e.getPosition().getName());
		e.setPosition(p);
		Hrimage image = em.find(Hrimage.class, 0);
		e.setHrimage(image);
		if(e.getStatus() == null || e.getStatus().equals(""))
			e.setStatus("A");
		e.setCreatetime(e.getFirstworktime()==null?new Date():e.getFirstworktime());
		em.persist(e);
		em.flush();
		
		if(c != null && c.getStartdate() != null && c.getEnddate() != null){
			if(c.getStatus() == null || c.getStatus().equals("")){
				if(c.getStatus() == null || c.getStatus().equals(""))
					c.setStatus("A");
			}
			c.setEmployee(e);
			em.persist(c);
		}else{
			System.out.println("Contract not added for empoloyee:" + e.getId() + ","+e.getFullname());
		}
	}
	
	public boolean employeeExist(String name, String documentkey){
		try{
			List<Employee> elist = (List<Employee>) em.createQuery("SELECT q FROM Employee q WHERE fullname=? AND documentkey=?").setParameter(1, name).setParameter(2, documentkey).getResultList();
			if(elist.size() < 1){
				return false;
			}else{
				Employee e  = elist.get(0);
				if(e == null)
					return false;
				else return true;
			}
		}catch(Exception e){
			e.printStackTrace();
			return false;
		}
	}
	
	public boolean isEmployeeWorkerIdExist(String workerid){
		try{
			List<Employee> elist = (List<Employee>) em.createQuery("SELECT q FROM Employee q WHERE workerid=?").setParameter(1, workerid).getResultList();
			if(elist.size() < 1){
				return false;
			}else{
				Employee e  = elist.get(0);
				if(e == null)
					return false;
				else return true;
			}
		}catch(Exception e){
			e.printStackTrace();
			return false;
		}
	}
	
	public List<Employee> getEmployees(int pagenum, int lotsize){
		List<Employee> list = em.createQuery("SELECT q FROM Employee q WHERE status=? ORDER BY createtime DESC").setFirstResult(pagenum*lotsize-lotsize).setParameter(1, "A").setMaxResults(lotsize).getResultList();
		return list;
	}
	
	public Employee getEmployeeById(String _id){
		Integer id = Integer.parseInt(_id);
		if(id == null)
			return null;
		Employee e  = em.find(Employee.class, id);
		return e;
	}
	
	public List<Employee> getEmployeeByName(String name){
		try{
			List<Employee> list = em.createQuery("SELECT q FROM Employee q WHERE status=? AND fullname LIKE ?").setParameter(1, "A").setParameter(2, "%"+name+"%").getResultList();
			return list;
		}catch(Exception e){
			System.out.println(e.getMessage());
			return null;
		}
		
	}
	
	public Employee getEmployeeByNameAndBod(String name, Date dob){
		try{
			Employee e = (Employee) em.createQuery("SELECT q FROM Employee q WHERE fullname=? AND dob=?").setParameter(1, name)
					.setParameter(2, dob).getSingleResult();
			return e;
		}catch(Exception e){
			System.out.println(e.getMessage());
			return null;
		}
	}

	public Employee getEmployeeByNameAndWorkerid(String name, String workerid){
		try{
			Employee e = (Employee) em.createQuery("SELECT q FROM Employee q WHERE fullname=? AND workerid=?").setParameter(1, name)
					.setParameter(2, workerid).getSingleResult();
			return e;
		}catch(Exception e){
			System.out.println(e.getMessage());
			return null;
		}
	}
	
	public Map<String, Object> getEmployeesBySelector(int pagenum, int lotsize,
			String statement) {
		List<Employee> list = em.createQuery(statement + " AND status=?").setFirstResult(pagenum*lotsize-lotsize).setParameter(1, "A").setMaxResults(lotsize).getResultList();
		String str = statement.substring(statement.indexOf("FROM"));
		str = "SELECT COUNT(q) " + str + " AND status='A'";
		Long count = (Long)em.createQuery(str).getSingleResult();
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("count", count);
		map.put("list", list);
		return map;
	}
	
	public Long countEmployees(){
		Long count = (Long)em.createQuery("SELECT COUNT(q) FROM Employee q WHERE status=?").setParameter(1, "A").getSingleResult();
		return count;
	}
	
	@Transactional
	public Department saveDepartment(Department department){
		em.persist(department);
		return department;
	}
	
	@Transactional
	public boolean deleteDepartment(Department department){
		List<Employee> list = em.createQuery("SELECT d FROM Employee d WHERE departmentid=?").setParameter(1, department.getId()).setMaxResults(1).getResultList();
		if(list == null || list.size() == 0){
			Department entity = (Department)em.find(Department.class, department.getId());
			em.remove(entity);
			return true;
		}else{
			return false;
		}
	}
	public List<Department> getAllDepartment(){
		List<Department> departments = em.createQuery("SELECT d FROM Department d").getResultList();
		return departments;
	}
	
	public Department getDepartmentByName(String name){
		try{
		Department d = (Department) em.createQuery("SELECT d FROM Department d WHERE name=?").setParameter(1, name).getSingleResult();
		return d;
		}catch(Exception e){
			System.out.println(e.getMessage());
			return null;
		}
	}
	
	
	@Transactional
	public Position savePosition(Position position){
		em.persist(position);
		return position;
	}
	
	@Transactional
	public boolean deletePosition(Position position){
		List<Employee> list = em.createQuery("SELECT d FROM Employee d WHERE positionid=?").setParameter(1, position.getId()).setMaxResults(1).getResultList();
		if(list == null || list.size() == 0){
			Position entity = (Position)em.find(Position.class, position.getId());
			em.remove(entity);
			return true;
		}else{
			return false;
		}
	}
	
	public List<Position> getAllPosition() {
		List<Position> positions = em.createQuery("SELECT p FROM Position p").getResultList();
		return positions;
	}
	
	public Position getPositionByName(String name){
		try{
			Position p = (Position) em.createQuery("SELECT d FROM Position d WHERE name=?").setParameter(1, name).getSingleResult();
			return p;
			}catch(Exception e){
				System.out.println(e.getMessage());
				return null;
			}
	}
	
	@Transactional
	public void saveContract(Contract contract){
		Employee e = em.find(Employee.class, contract.getEmployee().getId());
		contract.setCreatedate(new Date());
		contract.setStatus("A");
		contract.setEmployee(e);
		em.persist(contract);
	}
	
	@Transactional
	public void updateContract(Contract contract){
		Contract c  = em.find(Contract.class, contract.getId());
		c.copy(contract);
		em.merge(c);
	}
	
	@Transactional
	public void removeContract(int id){
		Contract c = em.find(Contract.class,  id);
		c.setStatus("D");
		em.merge(c);
	}
	
	public Long countContracts(){
		Long count = (Long)em.createQuery("SELECT COUNT(q) FROM Contract q WHERE status=?").setParameter(1, "A").getSingleResult();
		return count;
	}
	
	public List<Contract> getContracts(int pagenum, int lotsize){
		List<Contract> list = em.createQuery("SELECT q FROM Contract q WHERE status=? AND q.employee.status != 'D' ORDER BY createdate DESC").setFirstResult(pagenum*lotsize-lotsize).setParameter(1, "A").setMaxResults(lotsize).getResultList();
		return list;
	}
	
	public List<Contract> getContractsByEmployeeId(int id){
		List<Contract> list = em.createQuery("SELECT q FROM Contract q WHERE employeeid=? ORDER BY createdate DESC").setParameter(1, id).getResultList();
		return list;
	}
	
	public List<Contract> getContractsByName(int pagenum, int lotsize, String ename){
		List<Employee> employees = getEmployeeByName(ename);
		List<Contract> tempc = null;
		if(employees == null){
			return new ArrayList<Contract>();
		}else{
			List<Contract> contracts = new ArrayList<Contract>();
			for(int i=0;i<employees.size();i++){
				tempc = getContractsByEmployeeId(employees.get(i).getId());
				contracts.addAll(tempc);
			}
			return contracts;
		}
	}
	
	public List<Contract> getContractsBySelector(int pagenum, int lotsize, String statement){
		List<Contract> list = new ArrayList<Contract>();
		if(statement.contains("=") || statement.contains("<") || statement.contains(">") )
			list = em.createQuery("SELECT q FROM Contract q WHERE status=? AND "+statement).setFirstResult(pagenum*lotsize-lotsize).setParameter(1, "A").setMaxResults(lotsize).getResultList();
		else
			list = em.createQuery("SELECT q FROM Contract q WHERE status=? "+statement).setFirstResult(pagenum*lotsize-lotsize).setParameter(1, "A").setMaxResults(lotsize).getResultList();
		return list;
	}

	@Transactional
	public void saveIdcard(Idmanagement idcard){
		em.persist(idcard);
	}
	
	public List<Idmanagement> getIdcardsByEmployeeId(String id){
		try{
			List<Idmanagement> list = em.createQuery("SELECT q FROM Idmanagement q WHERE employeeid=?").setParameter(1, Integer.parseInt(id)).getResultList();
			return list;
		}catch(Exception e){
			return new ArrayList<Idmanagement>();
		}
	}
	
	public Idmanagement getIdCard(String type, String num){
		try{
			Idmanagement id = (Idmanagement) em.createQuery("SELECT q FROM Idmanagement q WHERE type=? AND number=?")
					.setParameter(1, type).setParameter(2, num).getSingleResult();
			return id;
		}catch(Exception e){
			System.out.println("Idmanagement:"+e.getMessage());
			return null;
		}
	}
	
	@Transactional
	public void deleteIdcard(String cardId){
		try{
			Idmanagement idcard = em.find(Idmanagement.class, Integer.parseInt(cardId));
			em.remove(idcard);
		}catch(Exception e){
			System.out.println(e.getMessage());
		}
	}
	
	@Transactional
	public String saveCoordination(Promoandtransfer coordinate){
		String str= "";
		try{
			Employee e = (Employee) em.createQuery("SELECT q FROM Employee q WHERE fullname=? AND workerid=?")
					.setParameter(1, coordinate.getEmployee().getFullname())
					.setParameter(2, coordinate.getEmployee().getWorkerid()).getSingleResult();
			Position prep = (Position) em.find(Position.class, coordinate.getPreposition().getId());
			Position curp = (Position) em.find(Position.class, coordinate.getCurposition().getId());
			Department pred = (Department) em.find(Department.class, coordinate.getPredepartment().getId());
			Department curd = (Department) em.find(Department.class, coordinate.getCurdepartment().getId());
			coordinate.setPredepartment(pred);
			coordinate.setCurdepartment(curd);
			coordinate.setPreposition(prep);
			coordinate.setCurposition(curp);
			coordinate.setEmployee(e);
			coordinate.setCreatedate(new Date());
			
			if(isCoordinatorExist(coordinate) == null)
				em.persist(coordinate);
			str = "创建成功";
		}catch(Exception e){
			e.printStackTrace();
			str = "创建失败，可能员工不存在."+e.getMessage();
		}
		return str;
	}
	
	private Promoandtransfer isCoordinatorExist(Promoandtransfer c) {
		try{
			if(c.getId() == null){
				Promoandtransfer coor = (Promoandtransfer) em.createQuery("SELECT q FROM Promoandtransfer q WHERE " +
						"employeeid=? AND predepartmentid=? AND curdepartmentid=?" +
						" AND prepositionid=? AND curpositionid=?").setParameter(1, c.getEmployee().getId())
						.setParameter(2, c.getPredepartment().getId()).setParameter(3, c.getCurdepartment().getId())
						.setParameter(4, c.getPreposition().getId()).setParameter(5, c.getCurposition().getId()).getSingleResult();
				System.out.println(coor);
				return coor;
			}else{
				Promoandtransfer coor = em.find(Promoandtransfer.class, c.getId());
				return coor;
			}
		}catch(Exception e){
			System.out.println(e.getMessage() + ". Coordination not exist.");
			return null;
		}
	}

	public List<Promoandtransfer> getAllCoordinators(int pagenum, int lotsize){
		try{
			List<Promoandtransfer> list = em.createQuery("SELECT q FROM Promoandtransfer q ORDER BY createdate DESC").setFirstResult(pagenum*lotsize-lotsize).setMaxResults(lotsize).getResultList();
			return list;
		}catch(Exception e){
			e.printStackTrace();
			return new ArrayList<Promoandtransfer>();
		}
	}

	/**
	 * From file driver license
	 * @param driverLicense
	 * @param serviceLicense
	 * @param service
	 */
	@Transactional
	public void saveIdsFromFile(Idmanagement driverLicense,
			Idmanagement serviceLicense, Idmanagement service) {
		if(driverLicense != null && getIdCard(driverLicense.getType(), driverLicense.getNumber()) == null){
			em.persist(driverLicense);
		}
		if(serviceLicense != null && getIdCard(serviceLicense.getType(), serviceLicense.getNumber()) == null){
			em.persist(serviceLicense);
		}
		if(service != null && getIdCard(service.getType(),service.getNumber()) == null){
			em.persist(service);
		}
	}
	
}
