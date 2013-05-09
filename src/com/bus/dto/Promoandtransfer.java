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
@Table(name="promoandtransfer")
public class Promoandtransfer {
	
	private Integer id;
	private Employee employee;
	private String type;
	private String amount;
	private Department predepartment;
	private Department curdepartment;
	private Position preposition;
	private Position curposition;
	private Date movedate;
	private Date activedate;
	private String remark;
	private Date createdate;
	private Account creator;
	
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
	
	@Column(name="type",nullable=false,length=8)
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	
	@Column(name="amount",length=16)
	public String getAmount() {
		return amount;
	}
	public void setAmount(String amount) {
		this.amount = amount;
	}
	
	@ManyToOne
	@JoinColumn(name="predepartmentid")
	public Department getPredepartment() {
		return predepartment;
	}
	public void setPredepartment(Department predepartment) {
		this.predepartment = predepartment;
	}
	
	@ManyToOne
	@JoinColumn(name="curdepartmentid")
	public Department getCurdepartment() {
		return curdepartment;
	}
	public void setCurdepartment(Department curdepartment) {
		this.curdepartment = curdepartment;
	}
	
	@ManyToOne
	@JoinColumn(name="prepositionid")
	public Position getPreposition() {
		return preposition;
	}
	public void setPreposition(Position preposition) {
		this.preposition = preposition;
	}
	
	@ManyToOne
	@JoinColumn(name="curpositionid")
	public Position getCurposition() {
		return curposition;
	}
	public void setCurposition(Position curposition) {
		this.curposition = curposition;
	}
	
	@Column(name="movedate", nullable =false)
	public Date getMovedate() {
		return movedate;
	}
	public void setMovedate(Date movedate) {
		this.movedate = movedate;
	}
	
	@Column(name="activedate", nullable=false)
	public Date getActivedate() {
		return activedate;
	}
	public void setActivedate(Date activedate) {
		this.activedate = activedate;
	}
	
	@Column(name="remark",length=128)
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	
	@Column(name="createdate")
	public Date getCreatedate() {
		return createdate;
	}
	public void setCreatedate(Date createdate) {
		this.createdate = createdate;
	}
	
	@ManyToOne
	@JoinColumn(name="creator")
	public Account getCreator() {
		return creator;
	}
	public void setCreator(Account creator) {
		this.creator = creator;
	}
	
	@Transient
	public String getCreatedatestr(){
		if(this.createdate != null)
			return HRUtil.parseDateToString(this.createdate);
		else
			return "";
	}
	@Transient
	public String getActivedatestr(){
		if(this.activedate != null)
			return HRUtil.parseDateToString(this.activedate);
		else
			return "";
	}
	@Transient
	public String getMovedatestr(){
		if(this.movedate != null)
			return HRUtil.parseDateToString(this.movedate);
		else
			return "";
	}
}
