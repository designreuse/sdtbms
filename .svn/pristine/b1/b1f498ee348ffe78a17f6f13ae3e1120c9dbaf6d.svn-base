package com.bus.dto.vehicleprofile;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

import com.bus.dto.Employee;

@Entity
@XmlRootElement
@Table(name="vehicledriver")
public class VehicleDriver {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id")
	private Integer id;
	
	@ManyToOne
	@JoinColumn(name="vid")
	private VehicleProfile vehicle;
	
	@ManyToOne
	@JoinColumn(name="laneid")
	private VehicleLane lane;
	
	@ManyToOne
	@JoinColumn(name="driver1")
	private Employee driverOne;
	
	@ManyToOne
	@JoinColumn(name="driver2")
	private Employee driverTwo;
	
	@Column(name="ddate")
	private Date date;
	
	@Column(name="remark")
	private String remark;

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

	public VehicleLane getLane() {
		return lane;
	}

	public void setLane(VehicleLane lane) {
		this.lane = lane;
	}

	public Employee getDriverOne() {
		return driverOne;
	}

	public void setDriverOne(Employee driverOne) {
		this.driverOne = driverOne;
	}

	public Employee getDriverTwo() {
		return driverTwo;
	}

	public void setDriverTwo(Employee driverTwo) {
		this.driverTwo = driverTwo;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}
	
	
}
