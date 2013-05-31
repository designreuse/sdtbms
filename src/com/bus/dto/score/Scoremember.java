package com.bus.dto.score;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.xml.bind.annotation.XmlRootElement;

import com.bus.dto.Employee;
import com.bus.util.HRUtil;

@Entity
@XmlRootElement
@Table(name="scoremember")
public class Scoremember implements Serializable{

	private Integer id;
	private Employee employee;
	private Integer monthlytotal = 0;
	private Integer monthlyremain = 0;
	private Long historytotal = 0L;
	private Integer monthlyscore = 0;
	private Long voucherscore = 0L;
	
	@Column(name="monthlytotal")
	public Integer getMonthlytotal() {
		return monthlytotal;
	}
	public void setMonthlytotal(Integer monthlytotal) {
		this.monthlytotal = monthlytotal;
	}
	
	@Column(name="monthlyremain")
	public Integer getMonthlyremain() {
		return monthlyremain;
	}
	public void setMonthlyremain(Integer monthlyremain) {
		this.monthlyremain = monthlyremain;
	}
	
	@Column(name="historytotal")
	public Long getHistorytotal() {
		return historytotal;
	}
	public void setHistorytotal(Long historytotal) {
		this.historytotal = historytotal;
	}
	
	@Transient
	public String getHistorytotalstr(){
		if(historytotal != null)
			return HRUtil.formatNumberComma(historytotal.toString());
		else
			return "0";
	}
	
	@Column(name="monthlyscore")
	public Integer getMonthlyscore() {
		return monthlyscore;
	}
	public void setMonthlyscore(Integer monthlyscore) {
		this.monthlyscore = monthlyscore;
	}
	
	@OneToOne
	@JoinColumn(name="workerid",referencedColumnName="workerid")
	public Employee getEmployee() {
		return employee;
	}
	public void setEmployee(Employee employee) {
		this.employee = employee;
	}
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id")
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	
	@Column(name="voucherscore")
	public Long getVoucherscore() {
		return voucherscore;
	}
	public void setVoucherscore(Long voucherscore) {
		this.voucherscore = voucherscore;
	}
	
	
}
