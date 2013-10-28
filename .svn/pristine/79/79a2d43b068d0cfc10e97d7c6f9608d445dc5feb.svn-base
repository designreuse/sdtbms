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
@Table(name="vehicle_parts_repair")
public class VehiclePartsRepair  implements Serializable{

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id")
	private Integer id;
	
	@ManyToOne
	@JoinColumn(name="vid",referencedColumnName="id")
	private VehicleProfile vid;

	
	@Column(name="changedate")
	private Date changedate;
	
	@Column(name="description")
	private String description;
	
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


	public Date getChangedate() {
		return changedate;
	}

	public void setChangedate(Date changedate) {
		this.changedate = changedate;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
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
	public String getChangedateStr(){
		if(changedate == null)
			return "";
		else
			return HRUtil.parseDateToString(changedate);
	}
}
