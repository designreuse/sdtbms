package com.bus.dto.vehicleprofile;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.xml.bind.annotation.XmlRootElement;

import com.bus.dto.Account;
import com.bus.dto.Hrimage;
import com.bus.util.HRUtil;

@Entity
@XmlRootElement
@Table(name="vehicle_profile")
public class VehicleProfile implements Serializable{
	
	public static final String statusA = "A";
	public static final String statusE = "E";
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id")
	private Integer id;
	
	@Column(name="vid")
	private String vid;
	
	@Column(name="selfid")
	private String selfid;

	@Column(name="vcolor")
	private String vcolor;
	
	@Column(name="subcompany")
	private String subcompany;
	
	@Column(name="joindate")
	private Date joinDate;
	
	@Column(name="registerDate")
	private Date registerDate;
	
	@Column(name="throwdate")
	private Date throwDate;
	
	@Column(name="forcethrowdate")
	private Date forcethrowdate;
	
	@Column(name="purchasedate")
	private Date purchaseDate;
	
	@Column(name="purchasecode")
	private String purchaseCode;
	
	@Column(name="status")
	private String status;
	
	@OneToOne
	@JoinColumn(name="imageid")
	private Hrimage image;
	
	@ManyToOne
	@JoinColumn(name="creator",referencedColumnName="id")
	private Account creator;
	
	@OneToOne(fetch=FetchType.LAZY,mappedBy="vid",cascade = CascadeType.ALL)
	private VehicleBasicInfo info;
	
	@OneToOne(fetch=FetchType.LAZY,mappedBy="vehicle",cascade = CascadeType.ALL)
	private VehicleLaneMapper vehiclelane;
	
	@OneToOne(fetch=FetchType.LAZY,mappedBy="vehicle",cascade = CascadeType.ALL)
	private VehicleTeamMember vehicleteam;
	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getVid() {
		return vid;
	}

	public void setVid(String vid) {
		this.vid = vid;
	}

	public String getSelfid() {
		return selfid;
	}

	public void setSelfid(String selfid) {
		this.selfid = selfid;
	}

	public String getVcolor() {
		return vcolor;
	}

	public void setVcolor(String vcolor) {
		this.vcolor = vcolor;
	}

	public Date getRegisterDate() {
		return registerDate;
	}

	public void setRegisterDate(Date registerDate) {
		this.registerDate = registerDate;
	}

	public Date getThrowDate() {
		return throwDate;
	}

	public void setThrowDate(Date throwDate) {
		this.throwDate = throwDate;
	}

	public Date getPurchaseDate() {
		return purchaseDate;
	}

	public void setPurchaseDate(Date purchaseDate) {
		this.purchaseDate = purchaseDate;
	}

	public String getPurchaseCode() {
		return purchaseCode;
	}

	public void setPurchaseCode(String purchaseCode) {
		this.purchaseCode = purchaseCode;
	}

	public Account getCreator() {
		return creator;
	}

	public void setCreator(Account creator) {
		this.creator = creator;
	}

	public VehicleBasicInfo getInfo() {
		return info;
	}

	public void setInfo(VehicleBasicInfo info) {
		this.info = info;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public VehicleLaneMapper getVehiclelane() {
		return vehiclelane;
	}

	public void setVehiclelane(VehicleLaneMapper vehiclelane) {
		this.vehiclelane = vehiclelane;
	}

	public VehicleTeamMember getVehicleteam() {
		return vehicleteam;
	}

	public void setVehicleteam(VehicleTeamMember vehicleteam) {
		this.vehicleteam = vehicleteam;
	}

	public static String getStatusa() {
		return statusA;
	}

	public static String getStatuse() {
		return statusE;
	}

	public Date getForcethrowdate() {
		return forcethrowdate;
	}

	public void setForcethrowdate(Date forcethrowdate) {
		this.forcethrowdate = forcethrowdate;
	}
	
	public String getSubcompany() {
		return subcompany;
	}

	public void setSubcompany(String subcompany) {
		this.subcompany = subcompany;
	}

	public Date getJoinDate() {
		return joinDate;
	}

	public void setJoinDate(Date joinDate) {
		this.joinDate = joinDate;
	}

	@Transient
	public String getPurchaseDateStr(){
		if(purchaseDate != null)
			return HRUtil.parseDateToString(purchaseDate);
		else
			return "";
	}
	
	@Transient
	public String getRegisterDateStr(){
		if(registerDate != null)
			return HRUtil.parseDateToString(registerDate);
		else
			return "";
	}
	
	@Transient
	public String getThrowDateStr(){
		if(throwDate != null)
			return HRUtil.parseDateToString(throwDate);
		else
			return "";
	}
	
	@Transient
	public String getForcethrowdateStr(){
		if(forcethrowdate != null)
			return HRUtil.parseDateToString(forcethrowdate);
		else
			return "";
	}
	
	@Transient
	public String getJoinDateStr(){
		if(joinDate != null)
			return HRUtil.parseDateToString(joinDate);
		else
			return "";
	}

	public Hrimage getImage() {
		return image;
	}

	public void setImage(Hrimage image) {
		this.image = image;
	}
	
	
}
