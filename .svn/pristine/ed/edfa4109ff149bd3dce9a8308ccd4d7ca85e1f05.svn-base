package com.bus.dto.application;

import java.io.Serializable;
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


@Entity
@XmlRootElement
@Table(name="training")
public class Training implements Serializable{
	
	private Integer id;
	private HRApplication applicant;
	private Integer jointraining;
	private Date jointrainingdate;
	private Integer tctraining;
	private Date tctrainingdate;
	
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
	@JoinColumn(name="applicationid")
	public HRApplication getApplicant() {
		return applicant;
	}
	public void setApplicant(HRApplication applicant) {
		this.applicant = applicant;
	}
	
	@Column(name="jointraining")
	public Integer getJointraining() {
		return jointraining;
	}
	public void setJointraining(Integer jointraining) {
		this.jointraining = jointraining;
	}
	
	@Column(name="jointrainingdate")
	public Date getJointrainingdate() {
		return jointrainingdate;
	}
	public void setJointrainingdate(Date jointrainingdate) {
		this.jointrainingdate = jointrainingdate;
	}
	
	@Column(name="tctraining")
	public Integer getTctraining() {
		return tctraining;
	}
	public void setTctraining(Integer tctraining) {
		this.tctraining = tctraining;
	}
	
	@Column(name="tctrainingdate")
	public Date getTctrainingdate() {
		return tctrainingdate;
	}
	public void setTctrainingdate(Date tctrainingdate) {
		this.tctrainingdate = tctrainingdate;
	}
	
}
