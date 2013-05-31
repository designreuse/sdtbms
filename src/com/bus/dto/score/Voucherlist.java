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
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.xml.bind.annotation.XmlRootElement;

import com.bus.util.HRUtil;

@Entity
@XmlRootElement
@Table(name="voucherlist")
public class Voucherlist implements Serializable{

	private Integer id;
	private Date date;
	private Integer quantity;
	private Scoremember scoremember;
	private Long score;
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id")
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	
	@Column(name="date")
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	
	@ManyToOne
	@JoinColumn(name="workerid", referencedColumnName="workerid")
	public Scoremember getScoremember() {
		return scoremember;
	}
	public void setScoremember(Scoremember scoremember) {
		this.scoremember = scoremember;
	}
	
	@Column(name="quantity")
	public Integer getQuantity() {
		return quantity;
	}
	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}
	
	@Column(name="score")
	public Long getScore() {
		return score;
	}
	public void setScore(Long score) {
		this.score = score;
	}
	
	@Transient
	public String getDatestr(){
		if(date == null )
			return "";
		else{
			return HRUtil.parseDateToString(date);
		}
	}
}
