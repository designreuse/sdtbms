package com.bus.dto.score;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

import com.bus.dto.Department;
import com.bus.dto.vehicleprofile.VehicleTeam;

@Entity
@XmlRootElement
@Table(name="departmentscores")
public class DepartmentScore {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id")
	private Integer id;
	
	@ManyToOne
	@JoinColumn(name="departmentid",referencedColumnName="id")
	private Department department;
	
	@ManyToOne
	@JoinColumn(name="vehicleteamid",referencedColumnName="id")
	private VehicleTeam vehicleteam;
	
	@Column(name="basescore")
	private Float basescore;
	
	@Column(name="available")
	private Float available;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Department getDepartment() {
		return department;
	}

	public void setDepartment(Department department) {
		this.department = department;
	}

	public VehicleTeam getVehicleteam() {
		return vehicleteam;
	}

	public void setVehicleteam(VehicleTeam vehicleteam) {
		this.vehicleteam = vehicleteam;
	}

	public Float getBasescore() {
		return basescore;
	}

	public void setBasescore(Float basescore) {
		this.basescore = basescore;
	}

	public Float getAvailable() {
		return available;
	}

	public void setAvailable(Float available) {
		this.available = available;
	}
	
	
}
