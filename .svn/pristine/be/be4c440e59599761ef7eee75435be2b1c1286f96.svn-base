package com.bus.dto.score;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;


@Entity
@XmlRootElement
@Table(name="scoresheetmapper")
public class Scoresheetmapper implements Serializable {

	private Integer id;
	private Scoresheets sheet;
	private Scoretype type;
	
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
	@JoinColumn(name="sheetid")
	public Scoresheets getSheet() {
		return sheet;
	}
	public void setSheet(Scoresheets sheet) {
		this.sheet = sheet;
	}
	
	@ManyToOne
	@JoinColumn(name="scoretypeid")
	public Scoretype getType() {
		return type;
	}
	public void setType(Scoretype type) {
		this.type = type;
	}
	
	
}
