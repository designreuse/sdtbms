package com.bus.dto.vehicleprofile;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

@Entity
@XmlRootElement
@Table(name="vehicleprofile")
public class VehicleProfile implements Serializable{
	

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id")
	private Integer id;
	@Column(name="vid")
	private String vid;
	@Column(name="company")
	private String company ;
	@Column(name="companyaddr")
	private String companyaddr;
	@Column(name="datejoin")
	private Date datejoin;
	@Column(name="datepurchase")
	private Date datepurchase;
	@Column(name="dateuse")
	private Date dateuse;
	@Column(name="ptaxnumber")
	private String ptaxnumber;
	@Column(name="source")
	private String source;
	@Column(name="servicetype")
	private String servicetype;
	@Column(name="vehicleprice")
	private String vehicleprice;
	@Column(name="subprice")
	private String subprice;
	@Column(name="dateproduction")
	private Date dateproduction;
	@Column(name="productionaddr")
	private String productionaddr;
	@Column(name="madein")
	private String madein;
	@Column(name="vtype")
	private String vtype;
	@Column(name="vlevel")
	private String vlevel;
	@Column(name="enginenum")
	private String enginenum;
	@Column(name="framenum")
	private String framenum;
	@Column(name="model")
	private String model;
	@Column(name="basednum")
	private String basednum ;
	@Column(name="enginemodel")
	private String enginemodel;
	@Column(name="bodysize")
	private String bodysize;
	@Column(name="color")
	private String color;
	@Column(name="sits")
	private String sits;
	@Column(name="tyrenum")
	private String tyrenum;
	
	@Column(name="tyremodel")
	private String tyremodel;
	
	@Column(name="fueltype")
	private String fueltype;
	
	@Column(name="vpower")
	private String vpower;
	
	@Column(name="vspeed")
	private String vspeed;
	
	@Column(name="fueltankcapacity")
	private String fueltankcapacity ;
	
	@Column(name="wheelbase")
	private Integer wheelbase;
	
	@Column(name="frontwheel")
	private Integer frontwheel ;
	
	@Column(name="backwheel")
	private Integer backwheel;
	
	@Column(name="turnbase")
	private String turnbase ;
	
	@Column(name="totalweight")
	private Integer totalweight;
	
	@Column(name="subweight")
	private Integer subweight;
	
	@Column(name="drivemode")
	private String drivemode;
	
	@Column(name="subsides")
	private String subsides;
	
	@Column(name="rimmodel")
	private String rimmodel;
	
	@Column(name="turntype")
	private String turntype ;
	
	@Column(name="turnmethod")
	private String turnmethod;
	
	@Column(name="movebreak")
	private String movebreak;
	
	@Column(name="stopbreak")
	private String stopbreak;
	
	@Column(name="hangmodel")
	private String hangmodel;
	
	@Column(name="aircond")
	private String aircond;
	
	@Column(name="ecotype")
	private String ecotype;
	
	
	public String getEcotype() {
		return ecotype;
	}
	public void setEcotype(String ecotype) {
		this.ecotype = ecotype;
	}
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
	public String getCompany() {
		return company;
	}
	public void setCompany(String company) {
		this.company = company;
	}
	public String getCompanyaddr() {
		return companyaddr;
	}
	public void setCompanyaddr(String companyaddr) {
		this.companyaddr = companyaddr;
	}
	public Date getDatejoin() {
		return datejoin;
	}
	public void setDatejoin(Date datejoin) {
		this.datejoin = datejoin;
	}
	public Date getDatepurchase() {
		return datepurchase;
	}
	public void setDatepurchase(Date datepurchase) {
		this.datepurchase = datepurchase;
	}
	public Date getDateuse() {
		return dateuse;
	}
	public void setDateuse(Date dateuse) {
		this.dateuse = dateuse;
	}
	public String getPtaxnumber() {
		return ptaxnumber;
	}
	public void setPtaxnumber(String ptaxnumber) {
		this.ptaxnumber = ptaxnumber;
	}
	public String getSource() {
		return source;
	}
	public void setSource(String source) {
		this.source = source;
	}
	public String getServicetype() {
		return servicetype;
	}
	public void setServicetype(String servicetype) {
		this.servicetype = servicetype;
	}
	public String getVehicleprice() {
		return vehicleprice;
	}
	public void setVehicleprice(String vehicleprice) {
		this.vehicleprice = vehicleprice;
	}
	public String getSubprice() {
		return subprice;
	}
	public void setSubprice(String subprice) {
		this.subprice = subprice;
	}
	public Date getDateproduction() {
		return dateproduction;
	}
	public void setDateproduction(Date dateproduction) {
		this.dateproduction = dateproduction;
	}
	public String getProductionaddr() {
		return productionaddr;
	}
	public void setProductionaddr(String productionaddr) {
		this.productionaddr = productionaddr;
	}
	public String getMadein() {
		return madein;
	}
	public void setMadein(String madein) {
		this.madein = madein;
	}
	public String getVtype() {
		return vtype;
	}
	public void setVtype(String vtype) {
		this.vtype = vtype;
	}
	public String getVlevel() {
		return vlevel;
	}
	public void setVlevel(String vlevel) {
		this.vlevel = vlevel;
	}
	public String getEnginenum() {
		return enginenum;
	}
	public void setEnginenum(String enginenum) {
		this.enginenum = enginenum;
	}
	public String getFramenum() {
		return framenum;
	}
	public void setFramenum(String framenum) {
		this.framenum = framenum;
	}
	public String getModel() {
		return model;
	}
	public void setModel(String model) {
		this.model = model;
	}
	public String getBasednum() {
		return basednum;
	}
	public void setBasednum(String basednum) {
		this.basednum = basednum;
	}
	public String getEnginemodel() {
		return enginemodel;
	}
	public void setEnginemodel(String enginemodel) {
		this.enginemodel = enginemodel;
	}
	public String getBodysize() {
		return bodysize;
	}
	public void setBodysize(String bodysize) {
		this.bodysize = bodysize;
	}
	public String getColor() {
		return color;
	}
	public void setColor(String color) {
		this.color = color;
	}
	public String getSits() {
		return sits;
	}
	public void setSits(String sits) {
		this.sits = sits;
	}
	public String getTyrenum() {
		return tyrenum;
	}
	public void setTyrenum(String tyrenum) {
		this.tyrenum = tyrenum;
	}
	public String getTyremodel() {
		return tyremodel;
	}
	public void setTyremodel(String tyremodel) {
		this.tyremodel = tyremodel;
	}
	public String getFueltype() {
		return fueltype;
	}
	public void setFueltype(String fueltype) {
		this.fueltype = fueltype;
	}
	public String getVpower() {
		return vpower;
	}
	public void setVpower(String vpower) {
		this.vpower = vpower;
	}
	public String getVspeed() {
		return vspeed;
	}
	public void setVspeed(String vspeed) {
		this.vspeed = vspeed;
	}
	public String getFueltankcapacity() {
		return fueltankcapacity;
	}
	public void setFueltankcapacity(String fueltankcapacity) {
		this.fueltankcapacity = fueltankcapacity;
	}
	public Integer getWheelbase() {
		return wheelbase;
	}
	public void setWheelbase(Integer wheelbase) {
		this.wheelbase = wheelbase;
	}
	public Integer getFrontwheel() {
		return frontwheel;
	}
	public void setFrontwheel(Integer frontwheel) {
		this.frontwheel = frontwheel;
	}
	public Integer getBackwheel() {
		return backwheel;
	}
	public void setBackwheel(Integer backwheel) {
		this.backwheel = backwheel;
	}
	public String getTurnbase() {
		return turnbase;
	}
	public void setTurnbase(String turnbase) {
		this.turnbase = turnbase;
	}
	public Integer getTotalweight() {
		return totalweight;
	}
	public void setTotalweight(Integer totalweight) {
		this.totalweight = totalweight;
	}
	public Integer getSubweight() {
		return subweight;
	}
	public void setSubweight(Integer subweight) {
		this.subweight = subweight;
	}
	public String getDrivemode() {
		return drivemode;
	}
	public void setDrivemode(String drivemode) {
		this.drivemode = drivemode;
	}
	public String getSubsides() {
		return subsides;
	}
	public void setSubsides(String subsides) {
		this.subsides = subsides;
	}
	public String getRimmodel() {
		return rimmodel;
	}
	public void setRimmodel(String rimmodel) {
		this.rimmodel = rimmodel;
	}
	public String getTurntype() {
		return turntype;
	}
	public void setTurntype(String turntype) {
		this.turntype = turntype;
	}
	public String getTurnmethod() {
		return turnmethod;
	}
	public void setTurnmethod(String turnmethod) {
		this.turnmethod = turnmethod;
	}
	public String getMovebreak() {
		return movebreak;
	}
	public void setMovebreak(String movebreak) {
		this.movebreak = movebreak;
	}
	public String getStopbreak() {
		return stopbreak;
	}
	public void setStopbreak(String stopbreak) {
		this.stopbreak = stopbreak;
	}
	public String getHangmodel() {
		return hangmodel;
	}
	public void setHangmodel(String hangmodel) {
		this.hangmodel = hangmodel;
	}
	public String getAircond() {
		return aircond;
	}
	public void setAircond(String aircond) {
		this.aircond = aircond;
	}
	
	
}
