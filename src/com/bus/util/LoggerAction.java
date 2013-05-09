package com.bus.util;

import java.util.Date;

import com.bus.dto.Account;
import com.bus.dto.logger.AccountLog;

public class LoggerAction {

	public final static int CREATE_ACCOUNT = 1;
	public final static int CREATE_GROUP = 2;
	public final static int ASSIGN_USER_TO_GROUP = 3;
	public final static int REMOVE_USER = 4;
	public final static int RESIGN_USER = 5;
	public final static int REMOVE_GROUP = 6;
	public final static int REMOVE_GROUP_FROM_ACC = 7;
	public final static int ASSIGN_ACTION_TO_GROUP = 8;
	public final static int REMOVE_ACTION_FROM_GROUP = 9;
	
	public static AccountLog createAccount(Account who, String updateId){
		AccountLog log = new AccountLog();
		log.setAction(CREATE_ACCOUNT);
		log.setCreatetime(new Date());
		log.setRecordid(updateId);
		log.setTablename("account");
		log.setWho(who);
		return log;
	}

	public static AccountLog createGroup(Account user, String updateId) {
		AccountLog log = new AccountLog();
		log.setAction(CREATE_GROUP);
		log.setCreatetime(new Date());
		log.setRecordid(updateId);
		log.setTablename("account");
		log.setWho(user);
		return log;
	}

	public static AccountLog assignUserToGroup(Account who,
			String id) {
		AccountLog log = new AccountLog();
		log.setAction(ASSIGN_USER_TO_GROUP);
		log.setCreatetime(new Date());
		log.setRecordid(id);
		log.setTablename("accountgroup");
		log.setWho(who);
		return log;
	}

	public static AccountLog removeUser(Account who, String id) {
		AccountLog log = new AccountLog();
		log.setAction(REMOVE_USER);
		log.setCreatetime(new Date());
		log.setRecordid(id);
		log.setTablename("account");
		log.setWho(who);
		return log;
	}
	
	public static AccountLog resignUser(Account who, String id){
		AccountLog log = new AccountLog();
		log.setAction(RESIGN_USER);
		log.setCreatetime(new Date());
		log.setRecordid(id);
		log.setTablename("account");
		log.setWho(who);
		return log;
	}
	
	public static AccountLog removeGroup(Account who, String id){
		AccountLog log = new AccountLog();
		log.setAction(REMOVE_GROUP);
		log.setCreatetime(new Date());
		log.setRecordid(id);
		log.setTablename("groups");
		log.setWho(who);
		return log;
	}

	public static AccountLog removeGroupFromAccount(Account who, String id) {
		AccountLog log = new AccountLog();
		log.setAction(REMOVE_GROUP_FROM_ACC);
		log.setCreatetime(new Date());
		log.setRecordid(id);
		log.setTablename("group-id");
		log.setWho(who);
		return log;
	}
	
	public static AccountLog assignActionToGroup(Account who, String id){
		AccountLog log = new AccountLog();
		log.setAction(ASSIGN_ACTION_TO_GROUP);
		log.setCreatetime(new Date());
		log.setRecordid(id);
		log.setTablename("actiongroup");
		log.setWho(who);
		return log;
	}

	public static Object removeActionFromGroup(Account who, String id) {
		AccountLog log = new AccountLog();
		log.setAction(REMOVE_ACTION_FROM_GROUP);
		log.setCreatetime(new Date());
		log.setRecordid(id);
		log.setTablename("group id");
		log.setWho(who);
		return log;
	}
}
