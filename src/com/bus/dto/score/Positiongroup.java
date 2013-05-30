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

import com.bus.dto.Position;

@Entity
@XmlRootElement
@Table(name="positiongroup")
public class Positiongroup implements Serializable{

	private Integer id;
	private Scoregroup scoreGroup;
	private Position position;
	
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
	@JoinColumn(name="scoregroupid")
	public Scoregroup getScoreGroup() {
		return scoreGroup;
	}
	public void setScoreGroup(Scoregroup scoreGroup) {
		this.scoreGroup = scoreGroup;
	}
	
	@ManyToOne
	@JoinColumn(name="positionid")
	public Position getPosition() {
		return position;
	}
	public void setPosition(Position position) {
		this.position = position;
	}
	
	
}
