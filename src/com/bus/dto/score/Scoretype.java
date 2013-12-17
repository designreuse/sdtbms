package com.bus.dto.score;

import java.io.Serializable;
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

import com.bus.dto.Account;

@Entity
@XmlRootElement
@Table(name="scoretype")
public class Scoretype  implements Serializable{

	@Transient
	public final static int SCORE_TYPE_FIX = 1;
	public final static String SCORE_TYPE_FIX_STR = "固定";
	@Transient
	public final static int SCORE_TYPE_TEMP = 0;
	public final static String SCORE_TYPE_TEMP_STR = "临时";
	public final static int SCORE_TYPE_PERFORMENCE  = 2;
	public final static String SCORE_TYPE_PERFORMENCE_STR = "绩效";
	public final static int SCORE_TYPE_PROJECT = 3;
	public final static String SCORE_TYPE_PROJECT_STR = "项目";
	public final static int DELETED = 0;
	public final static int ACTIVE = 1;
	
	private Integer id;
	private Date createdate;
	private Float score;
	private String description;
	private Integer type;
	
	private Account account;
	private String reference;
	private String period;
	private String examine; 
	private String scoreobject;
	private String remark;
	private Integer status=1;
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id")
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	
	@Column(name="createdate")
	public Date getCreatedate() {
		return createdate;
	}
	public void setCreatedate(Date createdate) {
		this.createdate = createdate;
	}
	
	@Column(name="score")
	public Float getScore() {
		return score;
	}
	public void setScore(Float score) {
		this.score = score;
	}
	
	@ManyToOne
	@JoinColumn(name="creator",referencedColumnName="id")
	public Account getAccount() {
		return account;
	}
	public void setAccount(Account account) {
		this.account = account;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public Integer getType() {
		return type;
	}
	public void setType(Integer type) {
		this.type = type;
	}
	
	@Column(name="reference")
	public String getReference() {
		return reference;
	}
	public void setReference(String reference) {
		this.reference = reference;
	}

	@Transient
	public String getTypestr(){
		if(Scoretype.SCORE_TYPE_FIX == type){
			return Scoretype.SCORE_TYPE_FIX_STR;
		}else if(Scoretype.SCORE_TYPE_TEMP == type){
			return  Scoretype.SCORE_TYPE_TEMP_STR;
		}else if(Scoretype.SCORE_TYPE_PERFORMENCE == type){
			return Scoretype.SCORE_TYPE_PERFORMENCE_STR;
		}else if(Scoretype.SCORE_TYPE_PROJECT == type){
			return Scoretype.SCORE_TYPE_PROJECT_STR;
		}else
			return type.toString();
	}
	
	@Column(name="period")
	public String getPeriod() {
		return period;
	}
	public void setPeriod(String period) {
		this.period = period;
	}
	@Column(name="examine")
	public String getExamine() {
		return examine;
	}
	public void setExamine(String examine) {
		this.examine = examine;
	}
	
	@Column(name="scoreobject")
	public String getScoreobject() {
		return scoreobject;
	}
	public void setScoreobject(String scoreobject) {
		this.scoreobject = scoreobject;
	}
	
	@Column(name="remark")
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	@Column(name="status")
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
}
