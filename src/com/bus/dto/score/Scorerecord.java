package com.bus.dto.score;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

import com.bus.dto.Account;

@Entity
@XmlRootElement
@Table(name="scorerecord")
public class Scorerecord {

	private Integer id;
	private Account receiver;
	private Account sender;
	private Scoretype scoretype;
	private Date scoredate;
	private Date createdate;
	private Account creator;
	
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
	@JoinColumn(name="receiverid")
	public Account getReceiver() {
		return receiver;
	}
	public void setReceiver(Account receiver) {
		this.receiver = receiver;
	}
	
	@ManyToOne
	@JoinColumn(name="senderid")
	public Account getSender() {
		return sender;
	}
	public void setSender(Account sender) {
		this.sender = sender;
	}
	
	@ManyToOne
	@JoinColumn(name="scoretypeid")
	public Scoretype getScoretype() {
		return scoretype;
	}
	public void setScoretype(Scoretype scoretype) {
		this.scoretype = scoretype;
	}
	
	@ManyToOne
	@JoinColumn(name="creator")
	public Account getCreator() {
		return creator;
	}
	public void setCreator(Account creator) {
		this.creator = creator;
	}
	
	
}