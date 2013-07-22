package com.bus.dto.application;

import java.io.Serializable;
import java.util.Calendar;
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
import com.bus.dto.Department;
import com.bus.dto.Position;
import com.bus.util.HRUtil;

@Entity
@XmlRootElement
@Table(name="hr_application")
public class HRApplication implements Serializable{
	
	private Integer id;
	private Date applyDate;
	private Department  department;
	private Position position;
	private String name;
	private String mobile;
	private String email;
	private String domicile;
	private String residence;
	private String pol;
	private Integer dorm;
	private Integer driver;
	private Date dob;
	private Integer bodyCheckNoti;
	private Integer bodyCheckPass;
	private Integer submitDetail;
	private Date interviewNoti;
	private Integer interviewResult;
	private String interviewRemark;
	private Integer driverexamresult;
	private Date approveDate;
	private Integer approveResult;
	private Date joinDate;
	private String remark;
	private Account creator;
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id")
	public Integer getId() {
		return id;
	}
	
	public void setId(Integer id) {
		this.id = id;
	}
	
	@Column(name="applydate")
	public Date getApplyDate() {
		return applyDate;
	}
	public void setApplyDate(Date applyDate) {
		this.applyDate = applyDate;
	}
	
	@ManyToOne
	@JoinColumn(name="department")
	public Department getDepartment() {
		return department;
	}
	public void setDepartment(Department department) {
		this.department = department;
	}
	
	@ManyToOne
	@JoinColumn(name="position")
	public Position getPosition() {
		return position;
	}
	public void setPosition(Position position) {
		this.position = position;
	}
	
	@Column(name="name")
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	@Column(name="mobile")
	public String getMobile() {
		return mobile;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	
	@Column(name="email")
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	
	@Column(name="domicile")
	public String getDomicile() {
		return domicile;
	}
	public void setDomicile(String domicile) {
		this.domicile = domicile;
	}
	
	@Column(name="residence")
	public String getResidence() {
		return residence;
	}
	public void setResidence(String residence) {
		this.residence = residence;
	}
	
	@Column(name="pol")
	public String getPol() {
		return pol;
	}
	public void setPol(String pol) {
		this.pol = pol;
	}
	
	@Column(name="dorm")
	public Integer getDorm() {
		return dorm;
	}
	public void setDorm(Integer dorm) {
		this.dorm = dorm;
	}
	
	@Column(name="driver")
	public Integer getDriver() {
		return driver;
	}
	public void setDriver(Integer driver) {
		this.driver = driver;
	}
	
	@Column(name="dob")
	public Date getDob() {
		return dob;
	}
	public void setDob(Date dob) {
		this.dob = dob;
	}
	
	@Column(name="bodychecknoti")
	public Integer getBodyCheckNoti() {
		return bodyCheckNoti;
	}
	public void setBodyCheckNoti(Integer bodyCheckNoti) {
		this.bodyCheckNoti = bodyCheckNoti;
	}
	
	@Column(name="bodycheckpass")
	public Integer getBodyCheckPass() {
		return bodyCheckPass;
	}
	public void setBodyCheckPass(Integer bodyCheckPass) {
		this.bodyCheckPass = bodyCheckPass;
	}
	
	@Column(name="submitdetail")
	public Integer getSubmitDetail() {
		return submitDetail;
	}
	public void setSubmitDetail(Integer submitDetail) {
		this.submitDetail = submitDetail;
	}
	
	@Column(name="interviewnoti")
	public Date getInterviewNoti() {
		return interviewNoti;
	}
	public void setInterviewNoti(Date interviewNoti) {
		this.interviewNoti = interviewNoti;
	}
	
	@Column(name="interviewresult")
	public Integer getInterviewResult() {
		return interviewResult;
	}
	public void setInterviewResult(Integer interviewResult) {
		this.interviewResult = interviewResult;
	}
	
	@Column(name="interviewremark")
	public String getInterviewRemark() {
		return interviewRemark;
	}
	public void setInterviewRemark(String interviewRemark) {
		this.interviewRemark = interviewRemark;
	}
	
	@Column(name="approvedate")
	public Date getApproveDate() {
		return approveDate;
	}
	public void setApproveDate(Date approveDate) {
		this.approveDate = approveDate;
	}
	
	@Column(name="approveresult")
	public Integer getApproveResult() {
		return approveResult;
	}
	public void setApproveResult(Integer approveResult) {
		this.approveResult = approveResult;
	}
	
	@Column(name="joindate")
	public Date getJoinDate() {
		return joinDate;
	}
	public void setJoinDate(Date joinDate) {
		this.joinDate = joinDate;
	}
	
	@Column(name="remark")
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	
	@ManyToOne
	@JoinColumn(name="creator")
	public Account getCreator() {
		return creator;
	}
	public void setCreator(Account creator) {
		this.creator = creator;
	}
	
	@Column(name="driverexamresult")
	public Integer getDriverexamresult() {
		return driverexamresult;
	}

	public void setDriverexamresult(Integer driverexamresult) {
		this.driverexamresult = driverexamresult;
	}
	
	@Transient
	public String getDriverexamresultStr(){
		if(driverexamresult == null || driverexamresult == 0)
			return "未报";
		else if(driverexamresult == 1)
			return "合格";
		else if(driverexamresult == 2)
			return "不合格";
		else
			return "已报";
	}
	
	@Transient
	public String getApplyDateStr(){
		if(applyDate == null)
			return "";
		else{
			return HRUtil.parseDateToString(applyDate);
		}
	}
	
	@Transient
	public String getJoinDateStr(){
		if(joinDate == null)
			return "";
		else
			return HRUtil.parseDateToString(joinDate);
	}
	
	@Transient
	public String getDormStr(){
		if(dorm != null && dorm == 1){
			return "是";
		}else
			return "否";
	}
	
	@Transient
	public String getAge(){
		if(this.dob != null){
			Calendar c = Calendar.getInstance();
			c.setTime(this.dob);
			Calendar cnow = Calendar.getInstance();
			cnow.setTime(new Date());
			Integer yearint = cnow.get(Calendar.YEAR) - c.get(Calendar.YEAR);
			Integer monthint = (cnow.get(Calendar.MONTH) - c.get(Calendar.MONTH) + 12)%12;
			Integer dayint  = (cnow.get(Calendar.DAY_OF_MONTH) - c.get(Calendar.DAY_OF_MONTH)+30)%30;
			return yearint.toString()+"岁"+monthint.toString()+"月"+dayint.toString()+"日";
		}else
			return "";
	}
	
	@Transient
	public String getDriverStr(){
		if(driver != null && driver == 1)
			return "是";
		else
			return "否";
	}
	
	@Transient
	public String getBodyCheckNotiStr(){
		if(bodyCheckNoti != null && bodyCheckNoti == 1)
			return "已发";
		else
			return "未发";
	}
	
	@Transient
	public String getBodyCheckPassStr(){
		if(bodyCheckPass == null)
			return "未知";
		else if(bodyCheckPass == 1)
			return "合格";
		else
			return "不合格";
		 
	}
	
	@Transient
	public String getInterviewResultStr(){
		if(interviewResult == null || interviewResult == 0)
			return "未面";
		else if(interviewResult == 1)
			return "通过";
		else if(interviewResult == 2)
			return "待定";
		else
			return "不录";
	}
	
	@Transient
	public String getApproveDateStr(){
		if(approveDate == null)
			return "";
		else
			return HRUtil.parseDateToString(approveDate);
	}
	
	@Transient
	public String getApproveResultStr(){
		if(approveResult == null || approveResult == 0)
			return "未审";
		else if(approveResult == 1)
			return "录取";
		else if(approveResult == 2)
			return "后备";
		else
			return "不录";
	}


}
