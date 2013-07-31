package com.bus.dto.vehicleprofile;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

@Entity
@XmlRootElement
@Table(name="vehiclemiles")
public class VehicleMiles implements Serializable{

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id")
	private Integer id;
	
	@ManyToOne
	@JoinColumn(name="vid")
	private VehicleMiles vehicle;
	
	@Column(name="vyear")
	private Integer vyear;
	
	@Column(name="jan")
	private Float jan;
	
	@Column(name="feb")
	private Float feb;
	
	@Column(name="mar")
	private Float mar;
	
	@Column(name="apr")
	private Float apr;
	
	@Column(name="may")
	private Float may;
	
	@Column(name="jun")
	private Float jun;
	
	@Column(name="jul")
	private Float jul;
	
	@Column(name="aug")
	private Float aug;
	
	@Column(name="sep")
	private Float sep;
	
	@Column(name="octo")
	private Float octo;
	
	@Column(name="nov")
	private Float nov;
	
	@Column(name="dece")
	private Float dece;
	
	@Column(name="yeartotal")
	private Float yeartotal;
	
	@Column(name="historytotal")
	private Float historytotal;
	
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public VehicleMiles getVehicle() {
		return vehicle;
	}
	public void setVehicle(VehicleMiles vehicle) {
		this.vehicle = vehicle;
	}
	public Integer getVyear() {
		return vyear;
	}
	public void setVyear(Integer vyear) {
		this.vyear = vyear;
	}
	public Float getJan() {
		return jan;
	}
	public void setJan(Float jan) {
		this.jan = jan;
	}
	public Float getFeb() {
		return feb;
	}
	public void setFeb(Float feb) {
		this.feb = feb;
	}
	public Float getMar() {
		return mar;
	}
	public void setMar(Float mar) {
		this.mar = mar;
	}
	public Float getApr() {
		return apr;
	}
	public void setApr(Float apr) {
		this.apr = apr;
	}
	public Float getMay() {
		return may;
	}
	public void setMay(Float may) {
		this.may = may;
	}
	public Float getJun() {
		return jun;
	}
	public void setJun(Float jun) {
		this.jun = jun;
	}
	public Float getJul() {
		return jul;
	}
	public void setJul(Float jul) {
		this.jul = jul;
	}
	public Float getAug() {
		return aug;
	}
	public void setAug(Float aug) {
		this.aug = aug;
	}
	public Float getSep() {
		return sep;
	}
	public void setSep(Float sep) {
		this.sep = sep;
	}
	public Float getOcto() {
		return octo;
	}
	public void setOcto(Float octo) {
		this.octo = octo;
	}
	public Float getNov() {
		return nov;
	}
	public void setNov(Float nov) {
		this.nov = nov;
	}
	public Float getDece() {
		return dece;
	}
	public void setDece(Float dece) {
		this.dece = dece;
	}
	public Float getYeartotal() {
		return yeartotal;
	}
	public void setYeartotal(Float yeartotal) {
		this.yeartotal = yeartotal;
	}
	public Float getHistorytotal() {
		return historytotal;
	}
	public void setHistorytotal(Float historytotal) {
		this.historytotal = historytotal;
	}
	
	
	
}