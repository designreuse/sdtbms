package com.bus.dto.score;

import java.util.Date;

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

import com.bus.dto.Employee;

@Entity
@XmlRootElement
@Table(name="scoresummary")
public class Scoresummary {

	private Integer id;
	private Employee employee;
	private Date date;
	private Float score = 0F;
	private Float fixscore = 0F;
	private Float performancescore = 0F;
	private Float projectscore = 0F;
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id")
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	
	@OneToOne
	@JoinColumn(name="workerid",referencedColumnName="workerid")
	public Employee getEmployee() {
		return employee;
	}
	public void setEmployee(Employee employee) {
		this.employee = employee;
	}
	
	@Column(name="date")
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	
	@Column(name="score")
	public Float getScore() {
		return score;
	}
	public void setScore(Float score) {
		this.score = score;
	}
	
	@Column(name="fixscore")
	public Float getFixscore() {
		return fixscore;
	}
	public void setFixscore(Float fixscore) {
		this.fixscore = fixscore;
	}
	
	@Column(name="performancescore")
	public Float getPerformancescore() {
		return performancescore;
	}
	public void setPerformancescore(Float performancescore) {
		this.performancescore = performancescore;
	}
	
	@Column(name="projectscore")
	public Float getProjectscore() {
		return projectscore;
	}
	public void setProjectscore(Float projectscore) {
		this.projectscore = projectscore;
	}
}
