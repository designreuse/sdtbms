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
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.xml.bind.annotation.XmlRootElement;

import com.bus.dto.Account;
import com.bus.util.HRUtil;

@Entity
@XmlRootElement
@Table(name="vehiclecheck")
public class VehicleCheck implements Serializable{

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id")
	private Integer id;
	
	@ManyToOne
	@JoinColumn(name="vid")
	private VehicleProfile vehicle;
	
	@Column(name="ctype")
	private String ctype;
	
	@Column(name="miles")
	private Float miles;
	
	@Column(name="cdate")
	private Date cdate;
	
	@Column(name="remark")
	private String remark;
	
	@OneToOne
	@JoinColumn(name="fid")
	private VehicleFiles image;
	
	@ManyToOne
	@JoinColumn(name="creator")
	private Account creator;
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public VehicleProfile getVehicle() {
		return vehicle;
	}
	public void setVehicle(VehicleProfile vehicle) {
		this.vehicle = vehicle;
	}
	public String getCtype() {
		return ctype;
	}
	public void setCtype(String ctype) {
		this.ctype = ctype;
	}
	public Float getMiles() {
		return miles;
	}
	public void setMiles(Float miles) {
		this.miles = miles;
	}
	public Date getCdate() {
		return cdate;
	}
	public void setCdate(Date cdate) {
		this.cdate = cdate;
	}
	public VehicleFiles getImage() {
		return image;
	}
	public void setImage(VehicleFiles image) {
		this.image = image;
	}
	public Account getCreator() {
		return creator;
	}
	public void setCreator(Account creator) {
		this.creator = creator;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	
	@Transient
	public String getCdateStr(){
		if(cdate == null)
			return "";
		return HRUtil.parseDateToString(cdate);
	}
}
