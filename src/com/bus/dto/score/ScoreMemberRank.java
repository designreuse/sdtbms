package com.bus.dto.score;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Transient;

/**
 * Not actually exist, use to store the ranking page data from 
 * EntityManager.createNativeQuery()
 * @author Administrator
 *
 */
@Entity
public class ScoreMemberRank implements Serializable{

	
//	private Integer id;
	private String workerid;
	private Long fixscore;
	private Long tempscore;
	private Long performancescore;
	private Long totalscore;
	private Integer rank;
	private Long count;
	private String name;
	private Date firstworktime;
	private String positionName;
	
//	@Id
//	@Column(name="id")
//	public Integer getId() {
//		return id;
//	}
//	public void setId(Integer id) {
//		this.id = id;
//	}
	
	@Id
	@Column(name="workercode")
	public String getWorkerid() {
		return workerid;
	}
	public void setWorkerid(String workerid) {
		this.workerid = workerid;
	}
	
	@Column(name="fixscore")
	public Long getFixscore() {
		return fixscore;
	}
	public void setFixscore(Long fixscore) {
		this.fixscore = fixscore;
	}
	
	@Column(name="tempscore")
	public Long getTempscore() {
		return tempscore;
	}
	public void setTempscore(Long tempscore) {
		this.tempscore = tempscore;
	}
	
	@Column(name="totalscore")
	public Long getTotalscore() {
		return totalscore;
	}
	public void setTotalscore(Long totalscore) {
		this.totalscore = totalscore;
	}
	
	@Column(name="rank")
	public Integer getRank() {
		return rank;
	}
	public void setRank(Integer rank) {
		this.rank = rank;
	}
	
	@Column(name="count")
	public Long getCount() {
		return count;
	}
	public void setCount(Long count) {
		this.count = count;
	}
	
	@Column(name="name")
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	@Column(name="firstworktime")
	public Date getFirstworktime() {
		return firstworktime;
	}
	public void setFirstworktime(Date firstworktime) {
		this.firstworktime = firstworktime;
	}
	
	@Transient
	public String getWorkage() {
		if(this.firstworktime != null){
			Calendar c = Calendar.getInstance();
			c.setTime(this.firstworktime);
			Calendar cnow = Calendar.getInstance();
			cnow.setTime(new Date());
			Integer yearint = cnow.get(Calendar.YEAR) - c.get(Calendar.YEAR);
			Integer monthint = (cnow.get(Calendar.MONTH) - c.get(Calendar.MONTH) + 12)%12;
			Integer dayint  = (cnow.get(Calendar.DAY_OF_MONTH) - c.get(Calendar.DAY_OF_MONTH)+30)%30;
			return yearint.toString()+"年"+monthint.toString()+"月"+dayint.toString()+"日";
		}else
			return "";
	}
	
	@Column(name="positionname")
	public String getPositionName() {
		return positionName;
	}
	public void setPositionName(String positionName) {
		this.positionName = positionName;
	}
	
	@Column(name="performancescore")
	public Long getPerformancescore() {
		return performancescore;
	}
	public void setPerformancescore(Long performancescore) {
		this.performancescore = performancescore;
	}
}
