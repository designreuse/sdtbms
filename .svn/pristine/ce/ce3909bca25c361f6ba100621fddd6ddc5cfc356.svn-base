package com.bus.dto.application;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

import com.bus.dto.Department;
import com.bus.dto.Employee;
import com.bus.dto.Position;


@Entity
@XmlRootElement
@Table(name="work_arrangement")
public class WorkArrangement implements Serializable{

	private Integer id;
	private HRApplication applicant;
	private Employee employee;
	private Department department;
	private Position position;
	private String place;
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id")
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	
	@ManyToOne
	@JoinColumn(name="applicationid")
	public HRApplication getApplicant() {
		return applicant;
	}
	public void setApplicant(HRApplication applicant) {
		this.applicant = applicant;
	}
	
	@ManyToOne
	@JoinColumn(name="workerid",referencedColumnName="workerid")
	public Employee getEmployee() {
		return employee;
	}
	public void setEmployee(Employee employee) {
		this.employee = employee;
	}
	
	@ManyToOne
	@JoinColumn(name="department")
	public Department getDepartment() {
		return department;
	}
	public void setDepartment(Department department) {
		this.department = department;
	}
	
	@ManyToOne
	@JoinColumn(name="position")
	public Position getPosition() {
		return position;
	}
	public void setPosition(Position position) {
		this.position = position;
	}
	
	@Column(name="place")
	public String getPlace() {
		return place;
	}
	public void setPlace(String place) {
		this.place = place;
	}
	
	
}
