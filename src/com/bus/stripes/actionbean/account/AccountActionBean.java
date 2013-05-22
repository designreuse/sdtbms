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
import com.bus.services.CustomActionBean;
import com.bus.services.HRBean;
import com.bus.stripes.actionbean.MyActionBeanContext;
import com.bus.stripes.actionbean.Permission;
import com.bus.stripes.typeconverter.PasswordTypeConverter;
import com.bus.util.SelectBoxOption;
import com.bus.util.SelectBoxOptions;

@UrlBinding(value="/actionbean/account/Account.action")
public class AccountActionBean extends CustomActionBean implements Permission{

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
			users = SelectBoxOptions.getUsers(accBean.getAccounts());
			groups = SelectBoxOptions.getGroups(accBean.getGroups());
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
		if(!getPermission(context.getUser(), "account_createaccount")){
			return context.errorResolution("权限错误","你没有权限进行该操作,请联系管理员");
		}
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
			accBean.saveAccount(a, context.getUser());
			return defaultAction();
		}catch(Exception e){
			e.printStackTrace();
			return defaultAction();
		}
	}
	
	@HandlesEvent(value="creategroup")
	public Resolution creategroup(){
		if(!getPermission(context.getUser(), "account_creategroup")){
			return context.errorResolution("权限错误","你没有权限进行该操作,请联系管理员");
		}
		try{
			if(groupname == null || groupname.trim().equals(""))
				return defaultAction();
			Groups g = new Groups();
			g.setName(groupname);
			accBean.saveGroup(g, context.getUser());
			return defaultAction();
		}catch(Exception e){
			e.printStackTrace();
			return defaultAction();
		}
	}
	
	@HandlesEvent(value="assigngroups")
	public Resolution assigngroups(){
		if(!getPermission(context.getUser(), "account_assigngroup")){
			return context.errorResolution("权限错误","你没有权限进行该操作,请联系管理员");
		}
		try{
			if(userids == null || groupids == null)
				return defaultAction();
			for(int i=0;i<userids.size();i++){
				for(int j=0;j<groupids.size();j++){
					if(!accBean.isGroupAssigned(groupids.get(j),userids.get(i)))
						accBean.assignToGroup(userids.get(i),groupids.get(j), context.getUser());
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
		if(!getPermission(context.getUser(), "account_removeaccount")){
			return context.errorResolution("权限错误","你没有权限进行该操作,请联系管理员");
		}
		try{
			if(userids == null)
				return defaultAction();
			for(int i=0;i<userids.size();i++){
				accBean.removeUser(userids.get(i), context.getUser());
			}
			return defaultAction();
		}catch(Exception e){
			System.out.println(e.getMessage());
			return defaultAction();
		}
	}
	
	@HandlesEvent(value="resignusers")
	public Resolution resignusers(){
		if(!getPermission(context.getUser(), "account_resignaccount")){
			return context.errorResolution("权限错误","你没有权限进行该操作,请联系管理员");
		}
		try{
			if(userids == null)
				return defaultAction();
			for(int i=0;i<userids.size();i++){
				accBean.resignUser(userids.get(i), context.getUser());
			}
			return defaultAction();
		}catch(Exception e){
			System.out.println(e.getMessage());
			return defaultAction();
		}
	}
	
	@HandlesEvent(value="removegroups")
	public Resolution removegroups(){
		if(!getPermission(context.getUser(), "account_removegroup")){
			return context.errorResolution("权限错误","你没有权限进行该操作,请联系管理员");
		}
		try{
			if(groupids == null)
				return defaultAction();
			for(int i=0;i<groupids.size();i++){
				accBean.removeGroup(groupids.get(i), context.getUser());
			}
			return defaultAction();
		}catch(Exception e){
			System.out.println(e.getMessage());
			return defaultAction();
		}

	}
	
	@HandlesEvent(value="accountgroups")
	public Resolution accountgroups(){
		if(!getPermission(context.getUser(), "account_viewaccountgroups")){
			return context.errorResolution("权限错误","你没有权限进行该操作,请联系管理员");
		}
		String targetId = context.getRequest().getParameter("targetId");
		try{
			account = accBean.getAccountById(targetId);
			usergroups = SelectBoxOptions.getGroups(accBean.getUserGroups(targetId));
			return new ForwardResolution("/acc/usergroups.jsp");
		}catch(Exception e){
			return new ForwardResolution("/actionbean/Error.action").addParameter("error", "获取账号信息失败")
					.addParameter("description", "无法获取账号ID:"+targetId+"的信息，请联系管理员。");
		}
	}
	
	@HandlesEvent(value="groupactions")
	public Resolution groupactions(){
		if(!getPermission(context.getUser(), "account_viewgroupactions")){
			return context.errorResolution("权限错误","你没有权限进行该操作,请联系管理员");
		}
		String targetId = context.getRequest().getParameter("targetId");
		try{
			group = accBean.getGroupById(targetId);
			groupactions = accBean.getGroupActions(targetId);
			allactions = accBean.getAllActions();
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
		if(!getPermission(context.getUser(), "account_removegroupfromuser")){
			return context.errorResolution("权限错误","你没有权限进行该操作,请联系管理员");
		}
		String usergroupid = context.getRequest().getParameter("usergroupid");
		String targetId = context.getRequest().getParameter("targetId");
		try{
			accBean.removeUsergroupMapper(targetId,usergroupid,context.getUser());
			return accountgroups();
		}catch(Exception e){
			return new ForwardResolution("/actionbean/Error.action").addParameter("error", "移除组失败")
					.addParameter("description", "无法移除账号ID:"+targetId+"的组信息，请联系管理员。"+e.getMessage());
		}
	}
	
	@HandlesEvent("assignactionstogroup")
	public Resolution assignactionstogroup(){
		if(!getPermission(context.getUser(), "account_assignactiontogroup")){
			return context.errorResolution("权限错误","你没有权限进行该操作,请联系管理员");
		}
		if(groupactions == null)
			groupactions = new ArrayList<Action>();
		String groupid = context.getRequest().getParameter("targetId");
		try{
			allactions = accBean.getGroupActions(groupid);
			for(int j=0;j<allactions.size();j++){
				boolean exist = false;
				for(int i=0;i<groupactions.size();i++){
					if(groupactions.get(i) != null && allactions.get(j).getId() == groupactions.get(i).getId()){
						exist = true;
						break;
					}
				}
				if(!exist && accBean.isGroupActionExist(allactions.get(j),groupid)){
					accBean.removeActionFromGroup(allactions.get(j),groupid, context.getUser());
				}
			}
			for(int i=0;i<groupactions.size();i++){
				if(groupactions.get(i) != null && !accBean.isGroupActionExist(groupactions.get(i),groupid)){
					accBean.assignActionToGroup(groupactions.get(i),groupid,context.getUser());
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
