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
import com.bus.dto.Employee;
import com.bus.dto.vehicleprofile.VehicleTeam;

@Entity
@XmlRootElement
@Table(name="scoreapprover")
public class Scoreapprover {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id")
	private Integer id;
	
	@ManyToOne
	@JoinColumn(name="approver",referencedColumnName="id")
	private Employee approver; 
	
	@ManyToOne
	@JoinColumn(name="departmentid",referencedColumnName="id")
	private Department department;
	
	@ManyToOne
	@JoinColumn(name="vehicleteamid",referencedColumnName="id")
	private VehicleTeam vehicleteam;
	
	@Column(name="isapprover")
	private String isapprover;
	
	public final static String APPROVER = "Y";
	public final static String NONE_APPROVER = "N";

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Employee getApprover() {
		return approver;
	}

	public void setApprover(Employee approver) {
		this.approver = approver;
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

	public String getIsapprover() {
		return isapprover;
	}

	public void setIsapprover(String isapprover) {
		this.isapprover = isapprover;
	}
	
	
}
