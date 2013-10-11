package com.bus.services;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.springframework.transaction.annotation.Transactional;

import com.bus.dto.Account;
import com.bus.dto.Accountgroup;
import com.bus.dto.Action;
import com.bus.dto.Actiongroup;
import com.bus.dto.Employee;
import com.bus.dto.Groups;
import com.bus.dto.logger.AccountLog;
import com.bus.util.HRUtil;
import com.bus.util.LoggerAction;

public class AccountBean  extends EMBean{

	@Transactional
	public void saveAccount(Account a, Account user) throws Exception{
		a.setStatus("A");
		a.setRegisterdate(new Date());
		a.setPassword(a.getPassword());
		em.persist(a);
		em.flush();
		em.persist(LoggerAction.createAccount(user, a.getId()+""));
	}

	@Transactional
	public void saveGroup(Groups g, Account user) throws Exception{
		em.persist(g);
		em.flush();
		em.persist(LoggerAction.createGroup(user,g.getId()+""));
	}

	public List<Account> getAccounts() throws Exception{
		return em.createQuery("SELECT q FROM Account q WHERE status='A' ORDER BY q.employee").getResultList();
	}
	
	public List<Groups> getGroups() throws Exception{
		return em.createQuery("SELECT q FROM Groups q ORDER BY q.name").getResultList();
	}

	public boolean isGroupAssigned(String groupid, String userid) throws Exception{
		try{
			List<Accountgroup> mapper = em.createQuery("SELECT q FROM Accountgroup q WHERE accountid=? AND" +
					" groupid=?").setParameter(1, Integer.parseInt(userid)).setParameter(2, Integer.parseInt(groupid)).getResultList();
			if(mapper.size()>0)
				return true;
			else
				return false;
		}catch(Exception e){
			return false;
		}
	}

	@Transactional
	public void assignToGroup(String userid, String groupid, Account who) throws Exception{
		Accountgroup ag = new Accountgroup();
		Account a = em.find(Account.class, Integer.parseInt(userid));
		Groups g = em.find(Groups.class, Integer.parseInt(groupid));
		ag.setAccount(a);
		ag.setGroups(g);
		em.persist(ag);
		em.flush();
		em.persist(LoggerAction.assignUserToGroup(who, ag.getId()+""));
	}

	@Transactional
	public void removeUser(String userid,Account who) throws Exception{
		Account a = em.find(Account.class, Integer.parseInt(userid));
		try{
			List<Accountgroup> mappers = em.createQuery("SELECT q FROM Accountgroup q WHERE accountid=?")
					.setParameter(1, Integer.parseInt(userid)).getResultList();
			for(int i=0;i<mappers.size();i++){
				em.remove(mappers.get(i));
			}
		}catch(Exception e){
			//ignore this try
		}
		em.remove(a);
		em.flush();
		em.persist(LoggerAction.removeUser(who,a.getId()+""));
	}

	@Transactional
	public void resignUser(String userid ,Account who)  throws Exception{
		Account a = em.find(Account.class, Integer.parseInt(userid));
		a.setStatus("E");
		em.merge(a);
		em.flush();
		em.persist(LoggerAction.resignUser(who,a.getId()+""));
	}

	@Transactional
	public void removeGroup(String groupid, Account who)  throws Exception{
		Groups g = em.find(Groups.class, Integer.parseInt(groupid));
		em.remove(g);
		em.persist(LoggerAction.removeGroup(who,g.getId()+""));
	}

	public List<Groups> getUserGroups(String targetId) throws Exception{
		List<Groups> groups = new ArrayList<Groups>();
		Account a = em.find(Account.class, Integer.parseInt(targetId));
		for(Accountgroup ag:a.getGroups()){
			groups.add(ag.getGroups());
		}
		return groups;
	}

	public Account getAccountById(String targetId) throws Exception{
		return em.find(Account.class, Integer.parseInt(targetId));
	}

	@Transactional
	public void removeUsergroupMapper(String targetId, String usergroupid, Account who) throws Exception{
		Accountgroup ag = (Accountgroup) em.createQuery("SELECT q FROM Accountgroup q WHERE accountid=? AND" +
				" groupid=?").setParameter(1, Integer.parseInt(targetId)).setParameter(2, Integer.parseInt(usergroupid))
				.getSingleResult();
		em.persist(LoggerAction.removeGroupFromAccount(who,ag.getGroups().getId()+""));
		em.remove(ag);
	}

	public Groups getGroupById(String targetId) throws Exception{
		return em.find(Groups.class, Integer.parseInt(targetId));
	}

	public List<Action> getGroupActions(String targetId) throws Exception{
		Groups group = em.find(Groups.class, Integer.parseInt(targetId));
		List<Action> actions = new ArrayList<Action>();
		for(Actiongroup ag:group.getActions()){
			actions.add(ag.getAction());
		}
		return actions;
	}

	public List<Action> getAllActions() throws Exception{
		return em.createQuery("SELECT q FROM Action q").getResultList();
	}

	@Transactional
	public void assignActionToGroup(Action action, String groupid,Account who) throws Exception{
		Action a = this.getActionById(action.getId());
		Groups group = this.getGroupById(groupid);
		Actiongroup ag = new Actiongroup();
		ag.setAction(a);
		ag.setGroups(group);
		em.persist(ag);
		em.flush();
		em.persist(LoggerAction.assignActionToGroup(who, ag.getId()+""));
	}

	public Action getActionById(Integer id) throws Exception{
		return em.find(Action.class, id);
	}

	@Transactional
	public void removeActionFromGroup(Action action, String groupid, Account who)  throws Exception{
		Actiongroup ag = (Actiongroup) em.createQuery("SELECT q FROM Actiongroup q WHERE action=? AND groupid=?")
				.setParameter(1, action).setParameter(2, Integer.parseInt(groupid)).getSingleResult();
		em.persist(LoggerAction.removeActionFromGroup(who,ag.getGroups().getId()+""));
		em.remove(ag);
	}

	public boolean isGroupActionExist(Action action, String groupid) throws Exception{
		try{
			Action a = this.getActionById(action.getId());
			Actiongroup ag = (Actiongroup) em.createQuery("SELECT q FROM Actiongroup q WHERE action=? AND groupid=?")
				.setParameter(1, a).setParameter(2, Integer.parseInt(groupid)).getSingleResult();
			if(ag == null)
				return false;
			return true;
		}catch(Exception e){
			return false;
		}
	}

	/**
	 * Change password of user
	 * @param acc2
	 */
	@Transactional
	public void changePasswd(Account acc2) throws Exception{
		Account a = em.find(Account.class,acc2.getId());
		if(a !=null){
			a.setPassword(acc2.getPassword());
			em.merge(a);
		}
	}

	/**
	 * 创建没有账号的工号账号
	 */
	@Transactional
	public void createAllAccountWithWorkerid() throws Exception{
		String query = "SELECT e.* FROM employee e WHERE " +
				" status='A' AND workerid not in (" +
				" SELECT account.employee FROM account WHERE account.employee IS NOT NULL)";
		List<Employee> list = em.createNativeQuery(query,Employee.class).getResultList();
		Groups g = (Groups) em.createQuery("SELECT q FROM Groups q WHERE q.name=?").setParameter(1, "积分制普通人员").getSingleResult();
		int count = 0;
		for(Employee e: list){
			Account a = new Account();
			a.setEmployee(e.getWorkerid());
			a.setUsername(e.getWorkerid());
			a.setPassword(HRUtil.getStringMD5(e.getWorkerid()));
			a.setRegisterdate(new Date());
			a.setStatus("A");
			em.persist(a);
			em.flush();
			Accountgroup ag = new Accountgroup();
			ag.setAccount(a);
			ag.setGroups(g);
			em.persist(ag);
			em.flush();
			System.out.println(count++ + "-- One employee done:"+e.getWorkerid());
		}
	}
}
