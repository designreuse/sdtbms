package com.bus.dto.score;

import java.io.Serializable;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

@Entity
@XmlRootElement
@Table(name="scoresheets")
public class Scoresheets  implements Serializable{

	private Integer id;
	private String name;
	private String remark;
	private Set<Scoresheetmapper> scoremapper;
	private Scoresheets parent;
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id")
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	
	@Column(name="name")
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	@Column(name="remark")
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	
	@OneToMany(fetch = FetchType.EAGER, mappedBy = "sheet",targetEntity=Scoresheetmapper.class)
	public Set<Scoresheetmapper> getScoremapper() {
		return scoremapper;
	}
	public void setScoremapper(Set<Scoresheetmapper> scoremapper) {
		this.scoremapper = scoremapper;
	}
	
	@ManyToOne
	@JoinColumn(name="pid",referencedColumnName="id")
	public Scoresheets getParent() {
		return parent;
	}
	public void setParent(Scoresheets parent) {
		this.parent = parent;
	}
	
	
}
