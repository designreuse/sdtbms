package com.bus.dto.logger;

import java.util.Calendar;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.xml.bind.annotation.XmlRootElement;

import com.bus.dto.Account;

@Entity
@XmlRootElement
@Table(name="scorelog")
public class ScoreLog {

	//Scores
	public final static int CREATE_SCORE_TYPE = 10;
	public final static int DELETE_SCORE_TYPE = 11;
	public final static int EDIT_SCORE_TYPE= 12;
	public final static int ASSIGN_SCORE_TYPE =13;
	public final static int REMOVE_SCORE_RECORD = 14;
	public final static int CREATE_SCORE_MEMBER = 15;
	public final static int CREATE_SCORE_SUMMARY = 16;
	public final static int UPDATE_SCORE_SUMMARY = 17;
	
	private Integer id;
	private Account who;
	private Integer action;
	private Date createtime;
	private String remark;
	private String recordid;
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id")
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	
	@ManyToOne
	@JoinColumn(name="who")
	public Account getWho() {
		return who;
	}
	public void setWho(Account who) {
		this.who = who;
	}
	
	@Column(name="action")
	public Integer getAction() {
		return action;
	}
	public void setAction(Integer action) {
		this.action = action;
	}
	
	@Column(name="createtime")
	@Temporal(TemporalType.TIMESTAMP)
	public Date getCreatetime() {
		return createtime;
	}
	public void setCreatetime(Date createtime) {
		this.createtime = createtime;
	}
	
	@Column(name="remark")
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	
	@Column(name="recordid")
	public String getRecordid() {
		return recordid;
	}
	public void setRecordid(String recordid) {
		this.recordid = recordid;
	}
	
	@Transient
	public String getCreatetimestr(){
		if(this.createtime != null){
			Calendar c = Calendar.getInstance();
			c.setTime(this.createtime);
			return c.getTime().toString();
		}else
			return "";
	}
	
	@Transient 
	public String getActionstr(){
		switch(this.action){
		case CREATE_SCORE_TYPE:
			return "创建新条例";
		case EDIT_SCORE_TYPE:
			return "修改条例";
		case DELETE_SCORE_TYPE:
			return "删除条例";
		case ASSIGN_SCORE_TYPE:
			return "赋值条例到员工";
		case REMOVE_SCORE_RECORD:
			return "从员工移除条例";
		case CREATE_SCORE_MEMBER:
			return "新增积分成员";
		case CREATE_SCORE_SUMMARY:
			return "新建了月积分";
		case UPDATE_SCORE_SUMMARY:
			return "更新了月积分";
			default:
				return "未知";
		}
	}
	
}
