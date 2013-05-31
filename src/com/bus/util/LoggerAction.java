package com.bus.util;

import java.util.Calendar;
import java.util.Date;

import com.bus.dto.Account;
import com.bus.dto.Employee;
import com.bus.dto.logger.AccountLog;
import com.bus.dto.logger.ScoreLog;
import com.bus.dto.score.Positiongroup;
import com.bus.dto.score.Scoregroup;
import com.bus.dto.score.Scoremember;
import com.bus.dto.score.Scorerecord;
import com.bus.dto.score.Scoresheets;
import com.bus.dto.score.Scoresummary;
import com.bus.dto.score.Scoretype;
import com.bus.dto.score.Voucherlist;

public class LoggerAction {

	//Accounts
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

	public static AccountLog removeActionFromGroup(Account who, String id) {
		AccountLog log = new AccountLog();
		log.setAction(REMOVE_ACTION_FROM_GROUP);
		log.setCreatetime(new Date());
		log.setRecordid(id);
		log.setTablename("group id");
		log.setWho(who);
		return log;
	}

	public static ScoreLog createScoreType(Account who, String id) {
		ScoreLog log = new ScoreLog();
		log.setAction(ScoreLog.CREATE_SCORE_TYPE);
		log.setCreatetime(new Date());
		log.setRecordid(id);
		log.setRemark("创建了新的条例");
		log.setWho(who);
		return log;
	}

	public static ScoreLog removeScoreType(Account user, Scoretype st) {
		ScoreLog log = new ScoreLog();
		log.setAction(ScoreLog.DELETE_SCORE_TYPE);
		log.setCreatetime(new Date());
		log.setRecordid(st.getId()+"");
		log.setRemark("删除了一个条例:"+st.getReference());
		log.setWho(user);
		return log;
	}

	public static ScoreLog editScoreType(Account user, Scoretype scoretype) {
		ScoreLog log = new ScoreLog();
		log.setAction(ScoreLog.EDIT_SCORE_TYPE);
		log.setCreatetime(new Date());
		log.setRecordid(scoretype.getId()+"");
		log.setRemark("更新了一个条例:"+scoretype.getReference());
		log.setWho(user);
		return log;
	}

	public static ScoreLog assignScoretype(Account user, Integer id, Scoretype st, Employee e) {
		ScoreLog log = new ScoreLog();
		log.setAction(ScoreLog.ASSIGN_SCORE_TYPE);
		log.setCreatetime(Calendar.getInstance().getTime());
		log.setRecordid(id+"");
		log.setRemark("赋值了条例"+st.getReference()+"到员工:"+e.getFullname());
		log.setWho(user);
		return log;
	}

	public static ScoreLog removeScoreRecord(Account user, Scorerecord record) {
		ScoreLog log = new ScoreLog();
		log.setAction(ScoreLog.REMOVE_SCORE_RECORD);
		log.setCreatetime(Calendar.getInstance().getTime());
		log.setRecordid(record.getId()+"");
		log.setRemark("移除了条例"+record.getScoretype().getReference()+"的分，从员工:"+record.getReceiver().getEmployee().getFullname());
		log.setWho(user);
		return log;
	}

	public static ScoreLog createScoreMember(Account user, Scoremember member) {
		ScoreLog log = new ScoreLog();
		log.setAction(ScoreLog.CREATE_SCORE_MEMBER);
		log.setCreatetime(Calendar.getInstance().getTime());
		log.setRecordid(member.getId()+"");
		log.setRemark("新建了积分成员"+member.getEmployee().getFullname());
		log.setWho(user);
		return log;
	}

	public static ScoreLog createNewScoreSummary(Account user,
			Scoresummary summary) {
		ScoreLog log = new ScoreLog();
		log.setAction(ScoreLog.CREATE_SCORE_SUMMARY);
		log.setCreatetime(Calendar.getInstance().getTime());
		log.setRecordid(summary.getId()+"");
		log.setRemark("新建了月总积分日期:"+HRUtil.parseDateToString(summary.getDate()));
		log.setWho(user);
		return log;
	}

	public static ScoreLog updateScoreSummary(Account user, Scoresummary summary) {
		ScoreLog log = new ScoreLog();
		log.setAction(ScoreLog.UPDATE_SCORE_SUMMARY);
		log.setCreatetime(Calendar.getInstance().getTime());
		log.setRecordid(summary.getId()+"");
		log.setRemark("更新了月总积分日期："+HRUtil.parseDateToString(summary.getDate())+"(新分值"+summary.getScore()+")");
		log.setWho(user);
		return log;
	}

	public static ScoreLog createScoreSheet(Account user, Scoresheets sheet) {
		ScoreLog log = new ScoreLog();
		log.setAction(ScoreLog.NEW_SCORE_SHEET);
		log.setCreatetime(Calendar.getInstance().getTime());
		log.setRecordid(sheet.getId()+"");
		log.setRemark("创建了新的积分表单:"+sheet.getName());
		log.setWho(user);
		return log;
	}

	public static ScoreLog removeScoreSheet(Account user, Scoresheets sheet) {
		ScoreLog log = new ScoreLog();
		log.setAction(ScoreLog.RM_SCORE_SHEET);
		log.setCreatetime(Calendar.getInstance().getTime());
		log.setRecordid(sheet.getId()+"");
		log.setRemark("删除了积分表单:"+sheet.getName());
		log.setWho(user);
		return log;
	}

	public static ScoreLog removeScoreTypeFromSheet(Account user,
			Scoresheets sheet, Scoretype st) {
		ScoreLog log = new ScoreLog();
		log.setAction(ScoreLog.RM_SCORE_SHEET_ST);
		log.setCreatetime(Calendar.getInstance().getTime());
		log.setRecordid(sheet.getId()+"");
		log.setRemark("从表单 "+sheet.getName()+" 移除了条例:"+st.getReference());
		log.setWho(user);
		return log;
	}

	public static ScoreLog assignScoreTypeToSheet(Account user, Scoretype st,
			Scoresheets sheet) {
		ScoreLog log = new ScoreLog();
		log.setAction(ScoreLog.ADD_SCORE_SHEET_ST);
		log.setCreatetime(Calendar.getInstance().getTime());
		log.setRecordid(sheet.getId()+"");
		log.setRemark("从表单 "+sheet.getName()+" 添加了条例:"+st.getReference());
		log.setWho(user);
		return log;
	}

	public static ScoreLog createScoreGroup(Account user, Scoregroup group) {
		ScoreLog log = new ScoreLog();
		log.setAction(ScoreLog.ADD_POS_GRP);
		log.setCreatetime(Calendar.getInstance().getTime());
		log.setRecordid(group.getId()+"");
		log.setRemark("新建了积分组"+group.getName());
		log.setWho(user);
		return log;
	}

	public static ScoreLog assginToScoreGroup(Account user, Positiongroup pg) {
		ScoreLog log = new ScoreLog();
		log.setAction(ScoreLog.ASSIGN_POS_GRP);
		log.setCreatetime(Calendar.getInstance().getTime());
		log.setRecordid(pg.getId()+"");
		log.setRemark("积分组 "+ pg.getScoreGroup().getName() +" 添加了职位 "+pg.getPosition().getName());
		log.setWho(user);
		return log;
	}

	public static ScoreLog quitScoreGroup(Account user, Positiongroup pg) {
		ScoreLog log = new ScoreLog();
		log.setAction(ScoreLog.QUIT_POS_GRP);
		log.setCreatetime(Calendar.getInstance().getTime());
		log.setRecordid(pg.getId()+"");
		log.setRemark("积分组 "+ pg.getScoreGroup().getName() +" 移除了职位 "+pg.getPosition().getName());
		log.setWho(user);
		return log;
	}

	public static ScoreLog editScoreGroupDetail(Account user, Scoregroup sg) {
		ScoreLog log = new ScoreLog();
		log.setAction(ScoreLog.EDIT_POS_GRP_DETAIL);
		log.setCreatetime(Calendar.getInstance().getTime());
		log.setRecordid(sg.getId()+"");
		log.setRemark("积分组 "+ sg.getName() +" 的资料更新了。 ");
		log.setWho(user);
		return log;
	}

	public static ScoreLog removeScoreGroup(Account user, Scoregroup sg) {
		ScoreLog log = new ScoreLog();
		log.setAction(ScoreLog.RM_POS_GRP);
		log.setCreatetime(Calendar.getInstance().getTime());
		log.setRecordid(sg.getId()+"");
		log.setRemark("积分组 "+ sg.getName() +" 被删除了。 ");
		log.setWho(user);
		return log;
	}

	public static ScoreLog giveVoucher(Account user, Voucherlist vl) {
		ScoreLog log = new ScoreLog();
		log.setAction(ScoreLog.GIVE_VOUCHER);
		log.setCreatetime(Calendar.getInstance().getTime());
		log.setRecordid(vl.getId()+"");
		log.setRemark("员工"+vl.getScoremember().getEmployee().getFullname() + "获取了"+vl.getQuantity()+"张奖券.");
		log.setWho(user);
		return log;
	}

	public static ScoreLog deleteVoucherRecord(Account user, Voucherlist vl) {
		ScoreLog log = new ScoreLog();
		log.setAction(ScoreLog.DELETE_VOUCHER);
		log.setCreatetime(Calendar.getInstance().getTime());
		log.setRecordid(vl.getId()+"");
		log.setRemark("删除了员工"+vl.getScoremember().getEmployee().getFullname() + "在"+vl.getQuantity()+"张奖券记录.");
		log.setWho(user);
		return log;
	}
}
