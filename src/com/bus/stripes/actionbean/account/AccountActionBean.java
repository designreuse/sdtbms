package com.bus.stripes.actionbean.account;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.stripes.action.ActionBean;
import net.sourceforge.stripes.action.ActionBeanContext;
import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.HandlesEvent;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.action.UrlBinding;
import net.sourceforge.stripes.integration.spring.SpringBean;
import net.sourceforge.stripes.validation.Validate;

import com.bus.dto.Account;
import com.bus.dto.Action;
import com.bus.dto.Groups;
import com.bus.services.AccountBean;
import com.bus.services.HRBean;
import com.bus.stripes.actionbean.MyActionBeanContext;
import com.bus.stripes.typeconverter.PasswordTypeConverter;
import com.bus.util.SelectBoxOption;
import com.bus.util.SelectBoxOptions;

@UrlBinding(value="/actionbean/account/Account.action")
public class AccountActionBean implements ActionBean {

	private MyActionBeanContext context;
	public MyActionBeanContext getContext() { return context; }
	public void setContext(ActionBeanContext context) { this.context = (MyActionBeanContext)context; }
	
	private AccountBean bean;
	public AccountBean getBean() {return bean;}
	@SpringBean
	public void setBean(AccountBean bean) {this.bean = bean;}

	private HRBean hrBean;
	public HRBean getHrBean(){return hrBean;}
	@SpringBean
	public void setHrBean(HRBean bean){this.hrBean = bean;}
	
	private String empworkerid;
	private String username;
	private String groupname;
	private Account account;
	private Groups group;
	private List<String> userids;
	private List<String> groupids;
	private List<Action> allactions;
	private List<SelectBoxOption> usergroups;
	private List<Action> groupactions;
	private List<SelectBoxOption> users;
	private List<SelectBoxOption> groups;
	
	@Validate(converter=PasswordTypeConverter.class)
	private String password;
	
	private void loadOptionsList(){
		try{
			users = SelectBoxOptions.getUsers(bean.getAccounts());
			groups = SelectBoxOptions.getGroups(bean.getGroups());
		}catch(Exception e){
			System.out.println(e);
		}
	}
	
	@DefaultHandler
	public Resolution defaultAction(){
		loadOptionsList();
		return new ForwardResolution("/acc/accmanagement.jsp");
	}
	
	@HandlesEvent(value="createaccount")
	public Resolution createaccount(){
		try{
			if(empworkerid != null && !hrBean.isEmployeeWorkerIdExist(empworkerid)){
				return defaultAction();
			}
			if(username == null || username.trim().equals(""))
				return defaultAction();
			Account a = new Account();
			a.setUsername(username);
			a.setPassword(password);
			a.setEmployee(empworkerid);
			bean.saveAccount(a, context.getUser());
			return defaultAction();
		}catch(Exception e){
			e.printStackTrace();
			return defaultAction();
		}
	}
	
	@HandlesEvent(value="creategroup")
	public Resolution creategroup(){
		try{
			if(groupname == null || groupname.trim().equals(""))
				return defaultAction();
			Groups g = new Groups();
			g.setName(groupname);
			bean.saveGroup(g, context.getUser());
			return defaultAction();
		}catch(Exception e){
			e.printStackTrace();
			return defaultAction();
		}
	}
	
	@HandlesEvent(value="assigngroups")
	public Resolution assigngroups(){
		try{
			if(userids == null || groupids == null)
				return defaultAction();
			for(int i=0;i<userids.size();i++){
				for(int j=0;j<groupids.size();j++){
					if(!bean.isGroupAssigned(groupids.get(j),userids.get(i)))
						bean.assignToGroup(userids.get(i),groupids.get(j), context.getUser());
				}
			}
			return defaultAction();
		}catch(Exception e){
			System.out.println(e.getMessage());
			return defaultAction();
		}
	}
	
	@HandlesEvent(value="removeusers")
	public Resolution removeusers(){
		try{
			if(userids == null)
				return defaultAction();
			for(int i=0;i<userids.size();i++){
				bean.removeUser(userids.get(i), context.getUser());
			}
			return defaultAction();
		}catch(Exception e){
			System.out.println(e.getMessage());
			return defaultAction();
		}
	}
	
	@HandlesEvent(value="resignusers")
	public Resolution resignusers(){
		try{
			if(userids == null)
				return defaultAction();
			for(int i=0;i<userids.size();i++){
				bean.resignUser(userids.get(i), context.getUser());
			}
			return defaultAction();
		}catch(Exception e){
			System.out.println(e.getMessage());
			return defaultAction();
		}
	}
	
	@HandlesEvent(value="removegroups")
	public Resolution removegroups(){
		try{
			if(groupids == null)
				return defaultAction();
			for(int i=0;i<groupids.size();i++){
				bean.removeGroup(groupids.get(i), context.getUser());
			}
			return defaultAction();
		}catch(Exception e){
			System.out.println(e.getMessage());
			return defaultAction();
		}

	}
	
	@HandlesEvent(value="accountgroups")
	public Resolution accountgroups(){
		String targetId = context.getRequest().getParameter("targetId");
		try{
			account = bean.getAccountById(targetId);
			usergroups = SelectBoxOptions.getGroups(bean.getUserGroups(targetId));
			return new ForwardResolution("/acc/usergroups.jsp");
		}catch(Exception e){
			return new ForwardResolution("/actionbean/Error.action").addParameter("error", "获取账号信息失败")
					.addParameter("description", "无法获取账号ID:"+targetId+"的信息，请联系管理员。");
		}
	}
	
	@HandlesEvent(value="groupactions")
	public Resolution groupactions(){
		String targetId = context.getRequest().getParameter("targetId");
		try{
			group = bean.getGroupById(targetId);
			groupactions = bean.getGroupActions(targetId);
			allactions = bean.getAllActions();
			for(int i=0;i<groupactions.size();i++){
				for(int j=0;j<allactions.size();j++){
					if(groupactions.get(i).getId() == allactions.get(j).getId())
						allactions.get(j).setChecked(true);
				}
			}
			groupactions = null;
			return new ForwardResolution("/acc/groupactions.jsp");
		}catch(Exception e){
			return new ForwardResolution("/actionbean/Error.action").addParameter("error", "获取用户组信息失败")
					.addParameter("description", "无法用户组ID:"+targetId+"的信息，请联系管理员。");
		}
	}
	
	
	@HandlesEvent("removeusergroup")
	public Resolution removeusergroup(){
		String usergroupid = context.getRequest().getParameter("usergroupid");
		String targetId = context.getRequest().getParameter("targetId");
		try{
			bean.removeUsergroupMapper(targetId,usergroupid,context.getUser());
			return accountgroups();
		}catch(Exception e){
			return new ForwardResolution("/actionbean/Error.action").addParameter("error", "移除组失败")
					.addParameter("description", "无法移除账号ID:"+targetId+"的组信息，请联系管理员。"+e.getMessage());
		}
	}
	
	@HandlesEvent("assignactionstogroup")
	public Resolution assignactionstogroup(){
		if(groupactions == null)
			groupactions = new ArrayList<Action>();
		String groupid = context.getRequest().getParameter("targetId");
		try{
			allactions = bean.getGroupActions(groupid);
			for(int j=0;j<allactions.size();j++){
				boolean exist = false;
				for(int i=0;i<groupactions.size();i++){
					if(groupactions.get(i) != null && allactions.get(j).getId() == groupactions.get(i).getId()){
						exist = true;
						break;
					}
				}
				if(!exist && bean.isGroupActionExist(allactions.get(j),groupid)){
					bean.removeActionFromGroup(allactions.get(j),groupid, context.getUser());
				}
			}
			for(int i=0;i<groupactions.size();i++){
				if(groupactions.get(i) != null && !bean.isGroupActionExist(groupactions.get(i),groupid)){
					bean.assignActionToGroup(groupactions.get(i),groupid,context.getUser());
				}
			}
			return groupactions();
		}catch(Exception e){
			e.printStackTrace();
			return new ForwardResolution("/actionbean/Error.action").addParameter("error", "更新组失败")
					.addParameter("description", "无法更新组ID:"+groupid+"的组信息，请联系管理员。"+e.getMessage());
		}
	}
	
	public String getEmpworkerid() {
		return empworkerid;
	}
	public void setEmpworkerid(String empworkerid) {
		this.empworkerid = empworkerid;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getGroupname() {
		return groupname;
	}
	public void setGroupname(String groupname) {
		this.groupname = groupname;
	}
	public List<SelectBoxOption> getUsers() {
		return users;
	}
	public void setUsers(List<SelectBoxOption> users) {
		this.users = users;
	}
	public List<SelectBoxOption> getGroups() {
		return groups;
	}
	public void setGroups(List<SelectBoxOption> groups) {
		this.groups = groups;
	}
	public List<String> getUserids() {
		return userids;
	}
	public void setUserids(List<String> userids) {
		this.userids = userids;
	}
	public List<String> getGroupids() {
		return groupids;
	}
	public void setGroupids(List<String> groupids) {
		this.groupids = groupids;
	}
	public List<SelectBoxOption> getUsergroups() {
		return usergroups;
	}
	public void setUsergroups(List<SelectBoxOption> usergroups) {
		this.usergroups = usergroups;
	}
	public List<Action> getGroupactions() {
		return groupactions;
	}
	public void setGroupactions(List<Action> groupactions) {
		this.groupactions = groupactions;
	}
	public Account getAccount() {
		return account;
	}
	public void setAccount(Account account) {
		this.account = account;
	}
	public Groups getGroup() {
		return group;
	}
	public void setGroup(Groups group) {
		this.group = group;
	}
	public List<Action> getAllactions() {
		return allactions;
	}
	public void setAllactions(List<Action> allactions) {
		this.allactions = allactions;
	}
	
	
}