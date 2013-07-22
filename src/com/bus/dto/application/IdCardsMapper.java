package com.bus.dto.application;

import java.io.Serializable;

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
@Table(name="applicant_idcard_mapper")
public class IdCardsMapper implements Serializable{

	private Integer id;
	private ApplicationIdCards idCard;
	private HRApplication applicant;
	
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
	@JoinColumn(name = "idcardid",referencedColumnName="id")
	public ApplicationIdCards getIdCard() {
		return idCard;
	}
	public void setIdCard(ApplicationIdCards idCard) {
		this.idCard = idCard;
	}
	
	@ManyToOne
	@JoinColumn(name = "applicationid",referencedColumnName="id")
	public HRApplication getApplicant() {
		return applicant;
	}
	public void setApplicant(HRApplication applicant) {
		this.applicant = applicant;
	}
}
