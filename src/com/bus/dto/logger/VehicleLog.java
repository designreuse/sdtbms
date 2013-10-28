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
@Table(name="vehicle_log")
public class VehicleLog {

	//Scores
	public final static int CREATE = 1;
	public final static int DELETE = 2;
	public final static int UPDATE = 3;
	public final static int THROW = 4;
	
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
		case CREATE:
			return "新建";
		case DELETE:
			return "删除";
		case UPDATE:
			return "更新";
			default:
				return "";
		}
	}
	
}
