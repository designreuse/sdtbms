package com.bus.dto.score;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.xml.bind.annotation.XmlRootElement;

import com.bus.util.HRUtil;

@Entity
@XmlRootElement
@Table(name="score_group")
public class ScoreDivGroup {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id")
	private Integer id;
	
	@Column(name="name")
	private String name;
	
	@ManyToOne
	@JoinColumn(name="pid",referencedColumnName="id")
	private ScoreDivGroup parent;
	
	private Float basescore;
	private Float available;
	private Date lastUpdateDate;
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public ScoreDivGroup getParent() {
		return parent;
	}
	public void setParent(ScoreDivGroup parent) {
		this.parent = parent;
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
	public Date getLastUpdateDate() {
		return lastUpdateDate;
	}
	public void setLastUpdateDate(Date lastUpdateDate) {
		this.lastUpdateDate = lastUpdateDate;
	}
	
	@Transient
	public String getLastUpdateDateStr(){
		if(lastUpdateDate == null)
			return "";
		else
			return HRUtil.parseDateToString(lastUpdateDate);
	}
	
}
