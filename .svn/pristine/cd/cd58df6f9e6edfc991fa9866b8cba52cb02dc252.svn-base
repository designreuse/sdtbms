package com.bus.dto.score;

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

import com.bus.dto.Position;

@Entity
@XmlRootElement
@Table(name="scoreexceptionlist")
public class ScoreExceptionList {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id")
	private Integer id;
	
	@ManyToOne
	@JoinColumn(name="positionid", referencedColumnName="id")
	private Position position;
	
	@Column(name="status")
	private Integer status;

	public static final int NOT_JOIN_SCORE = 0;
	public static final int NO_UPPER_SCORE_LIMIT = 1;
	public static final int HAS_UPPER_SCORE_LIMIT = 2;
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Position getPosition() {
		return position;
	}
	public void setPosition(Position position) {
		this.position = position;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	
	@Transient
	public String getStatusStr(){
		if(status == null){
			return "";
		}else if(status == NOT_JOIN_SCORE){
			return "不参加";
		}else if(status == NO_UPPER_SCORE_LIMIT){
			return "无上限";
		}else if(status == HAS_UPPER_SCORE_LIMIT){
			return "有上限";
		}else{
			return "";
		}
	}
}
