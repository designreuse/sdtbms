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

import com.bus.dto.Employee;

@Entity
@XmlRootElement
@Table(name="score_group_mapper")
public class ScoreGroupMapper {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id")
	private Integer id;
	
	@ManyToOne
	@JoinColumn(name="empid")
	private Employee employee;
	
	@ManyToOne
	@JoinColumn(name="scoregroupid")
	private ScoreDivGroup group;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Employee getEmployee() {
		return employee;
	}

	public void setEmployee(Employee employee) {
		this.employee = employee;
	}

	public ScoreDivGroup getGroup() {
		return group;
	}

	public void setGroup(ScoreDivGroup group) {
		this.group = group;
	}
	
	
}
