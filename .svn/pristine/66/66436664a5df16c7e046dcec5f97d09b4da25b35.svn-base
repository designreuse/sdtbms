package com.bus.dto;

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
@Table(name="idmanagement")
public class Idmanagement {

	private Integer id;
	private Employee employee;
	private String number;
	private Date validfrom;
	private Date expiredate;
	private Hrimage image;
	private String remark;
	private String type;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="id")
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	
	@ManyToOne
	@JoinColumn(name="employeeid")
	public Employee getEmployee() {
		return employee;
	}
	public void setEmployee(Employee employee) {
		this.employee = employee;
	}
	
	@Column(name="number",length=32,nullable=false)
	public String getNumber() {
		return number;
	}
	public void setNumber(String number) {
		this.number = number;
	}
	
	@Column(name="validfrom",nullable=false)
	public Date getValidfrom() {
		return validfrom;
	}
	public void setValidfrom(Date validfrom) {
		this.validfrom = validfrom;
	}
	
	@Column(name="expiredate",nullable=false)
	public Date getExpiredate() {
		return expiredate;
	}
	public void setExpiredate(Date expiredate) {
		this.expiredate = expiredate;
	}
	
	@ManyToOne
	@JoinColumn(name="imageid")
	public Hrimage getImage() {
		return image;
	}
	public void setImage(Hrimage image) {
		this.image = image;
	}
	
	@Column(name="remark")
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	
	@Column(name="type")
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	
	@Transient
	public String getValidfromstr(){
		if(this.validfrom != null)
			return HRUtil.parseDateToString(this.validfrom);
		else
			return "";
	}
	@Transient
	public String getExpiredatestr(){
		if(this.expiredate != null)
			return HRUtil.parseDateToString(this.expiredate);
		else
			return "";
	}

}
