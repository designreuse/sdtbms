package com.bus.dto.score;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.xml.bind.annotation.XmlRootElement;


import com.bus.dto.Account;
import com.bus.util.HRUtil;

@Entity
@XmlRootElement
@Table(name="scorerecord")
public class Scorerecord  implements Serializable{

	private Integer id;
	private Scoremember receiver;
	private Scoremember sender;
	private Scoretype scoretype;
	private Date scoredate;
	private Date createdate;
	private Float score;
	private Account creator;
	private int status;
	
	@Transient
	private int excepStatus;
	
	private ScorerecordRemark remark;
	
	public static final int APPROVED = 0;
	public static final int WAITING = 1;
	public static final int REJECTED = 2;
	public static final int CREATED = 3;
	

	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id")
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	
	@Column(name="scoredate")
	public Date getScoredate() {
		return scoredate;
	}
	public void setScoredate(Date scoredate) {
		this.scoredate = scoredate;
	}
	
	@Column(name="createdate")
	public Date getCreatedate() {
		return createdate;
	}
	public void setCreatedate(Date createdate) {
		this.createdate = createdate;
	}
	
	@ManyToOne
	@JoinColumn(name="receiverid",referencedColumnName="workerid")
	public Scoremember getReceiver() {
		return receiver;
	}
	public void setReceiver(Scoremember receiver) {
		this.receiver = receiver;
	}
	
	@ManyToOne
	@JoinColumn(name="senderid",referencedColumnName="workerid")
	public Scoremember getSender() {
		return sender;
	}
	public void setSender(Scoremember sender) {
		this.sender = sender;
	}
	
	@ManyToOne
	@JoinColumn(name="scoretypeid", referencedColumnName="id")
	public Scoretype getScoretype() {
		return scoretype;
	}
	public void setScoretype(Scoretype scoretype) {
		this.scoretype = scoretype;
	}
	
	@ManyToOne
	@JoinColumn(name="creator", referencedColumnName="id")
	public Account getCreator() {
		return creator;
	}
	public void setCreator(Account creator) {
		this.creator = creator;
	}
	
	@Transient
	public String getScoredatestr(){
		if(scoredate == null)
			return "";
		return HRUtil.parseDateToString(scoredate);
	}
	
	@Transient
	public String getCreatedatestr(){
		if(createdate == null)
			return "";
		return HRUtil.parseDateToString(createdate);
	}
	
	@Column(name="score")
	public Float getScore() {
		return score;
	}
	public void setScore(Float score) {
		this.score = score;
	}
	
	@Column(name="status")
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	
	@Transient
	public int getExcepStatus() {
		return excepStatus;
	}
	public void setExcepStatus(int excepStatus) {
		this.excepStatus = excepStatus;
	}
	@ManyToOne
	@JoinColumn(name="remarkid",referencedColumnName="id")
	public ScorerecordRemark getRemark() {
		return remark;
	}
	public void setRemark(ScorerecordRemark remark) {
		this.remark = remark;
	}
}
