package com.bus.dto.score;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Transient;

import org.joda.time.Period;
import org.joda.time.PeriodType;
import org.springframework.context.annotation.Scope;

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
	private Integer countsum;
	
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
			Period p = new Period(c.getTimeInMillis(),cnow.getTimeInMillis(),PeriodType.yearMonthDay());
			Integer yearint = p.getYears();
			Integer monthint = p.getMonths();
			Integer dayint = p.getDays();
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
	
	@Transient
	public Integer getCountsum(){
		return this.countsum;
	}
	public void setCountsum(Integer countsum){
		this.countsum = countsum;
	}
	
	@Transient
	public Integer getPercent(){
		if(countsum != null && countsum > 0){
			float result = (float)rank/(float)countsum * 100F;
			return Math.round(result);
		}
		else
			return 0;
	}
}
