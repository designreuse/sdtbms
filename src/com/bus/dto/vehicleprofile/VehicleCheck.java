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
import javax.xml.bind.annotation.XmlRootElement;

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
	private Integer ctype;
	
	@Column(name="miles")
	private Float miles;
	
	@Column(name="cdate")
	private Date cdate;
	
	@Column(name="filename")
	private String filename;
	
	
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
	public Integer getCtype() {
		return ctype;
	}
	public void setCtype(Integer ctype) {
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
	public String getFilename() {
		return filename;
	}
	public void setFilename(String filename) {
		this.filename = filename;
	}
	
	
}
