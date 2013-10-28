package com.bus.dto.vehicleprofile;

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

import com.bus.dto.Account;
import com.bus.util.HRUtil;

@Entity
@XmlRootElement
@Table(name="vehicle_use_record")
public class VehicleUseRecord  implements Serializable{

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id")
	private Integer id;
	
	@ManyToOne
	@JoinColumn(name="vid",referencedColumnName="id")
	private VehicleProfile vid;
	
	@Column(name="date")
	private Date date;
	
	@Column(name="mileage")
	private Double mileage;
	
	@Column(name="intervalmileage")
	private Double intervalmileage;
	
	@Column(name="fuel")
	private Double fuel;
	
	@Column(name="setprice")
	private Double setprice;
	
	@Column(name="remine")
	private Double remine;
	
	@Column(name="exceed")
	private Double exceed;
	
	@Column(name="company")
	private String company;
	
	@Column(name="registrant")
	private String registrant;
	
	@ManyToOne
	@JoinColumn(name="creator",referencedColumnName="id")
	private Account creator;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public VehicleProfile getVid() {
		return vid;
	}

	public void setVid(VehicleProfile vid) {
		this.vid = vid;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public Double getMileage() {
		return mileage;
	}

	public void setMileage(Double mileage) {
		this.mileage = mileage;
	}

	public Double getIntervalmileage() {
		return intervalmileage;
	}

	public void setIntervalmileage(Double intervalmileage) {
		this.intervalmileage = intervalmileage;
	}

	public Double getFuel() {
		return fuel;
	}

	public void setFuel(Double fuel) {
		this.fuel = fuel;
	}

	public Double getSetprice() {
		return setprice;
	}

	public void setSetprice(Double setprice) {
		this.setprice = setprice;
	}

	public Double getRemine() {
		return remine;
	}

	public void setRemine(Double remine) {
		this.remine = remine;
	}

	public Double getExceed() {
		return exceed;
	}

	public void setExceed(Double exceed) {
		this.exceed = exceed;
	}

	public String getCompany() {
		return company;
	}

	public void setCompany(String company) {
		this.company = company;
	}

	public String getRegistrant() {
		return registrant;
	}

	public void setRegistrant(String registrant) {
		this.registrant = registrant;
	}

	public Account getCreator() {
		return creator;
	}

	public void setCreator(Account creator) {
		this.creator = creator;
	}

	@Transient
	public String getDateStr(){
		if(date ==null)
			return "";
		else
			return HRUtil.parseDateToString(date);
	}
}
