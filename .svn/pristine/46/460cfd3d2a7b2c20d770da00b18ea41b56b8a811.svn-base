package com.bus.dto.vehicleprofile;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.xml.bind.annotation.XmlRootElement;

import com.bus.dto.Account;

@Entity
@XmlRootElement
@Table(name="vehicle_technical_detail")
public class VehicleTechnicalDetail {
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id")
	private Integer id;
	
	@ManyToOne
	@JoinColumn(name="vid",referencedColumnName="id")
	private VehicleProfile vid;
	
	@Column(name="vtype")
	private String vtype;

	@Column(name="factorycode")
	private String factorycode;
	
	@Column(name="factorydate")
	private Date factorydate;
	
	@Column(name="madein")
	private String madein;
	
	@Column(name="vincode")
	private String vincode;
	
	@Column(name="basecode")
	private String basecode;
	
	@Column(name="vlevel")
	private String vlevel;
	
	@Column(name="vlength")
	private String vlength;
	
	@Column(name="vwidth")
	private String vwidth;
	
	@Column(name="vheight")
	private String vheight;
	
	@Column(name="weight")
	private String weight;
	
	@Column(name="sitarrange")
	private String sitarrange;
	
	@Column(name="sits")
	private String sits;
	
	@Column(name="dragweight")
	private String dragweight;
	
	@Column(name="vaxis")
	private String vaxis;
	
	@Column(name="enginemodel")
	private String enginemodel;
	
	@Column(name="enginecode")
	private String enginecode;
	
	@Column(name="fueltype")
	private String fueltype;
	
	@Column(name="enginepower")
	private String enginepower;
	
	@Column(name="enginehorse")
	private String enginehorse;
	
	@Column(name="releasestandard")
	private String releasestandard;
	
	@Column(name="drivermode")
	private String drivermode;
	
	@Column(name="tyre")
	private String tyre;
	
	@Column(name="frontlight")
	private String frontlight;
	
	@Column(name="variatormode")
	private String variatormode;
	
	@Column(name="retarder")
	private String retarder;
	
	@Column(name="turner")
	private String turner;
	
	@Column(name="breakmode")
	private String breakmode;
	
	@Column(name="frontbreak")
	private String frontbreak;
	
	@Column(name="backbreak")
	private String backbreak;
	
	@Column(name="hangmodefront")
	private String hangmodefront;
	
	@Column(name="hangmodeback")
	private String hangmodeback;
	
	@Column(name="other")
	private String other;
	
	@Transient
	private List<String> frontbreakList;
	
	@Transient
	private List<String> backbreakList;
	
	@Transient
	private List<String> hangmodefrontList;
	
	@Transient
	private List<String> hangmodebackList;
	
	@Transient
	private List<String> otherList;
	
	@ManyToOne
	@JoinColumn(name="creator",referencedColumnName="id")
	private Account creator;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public VehicleProfile getVid() {
		return vid;
	}

	public void setVid(VehicleProfile vid) {
		this.vid = vid;
	}

	public String getVtype() {
		return vtype;
	}

	public void setVtype(String vtype) {
		this.vtype = vtype;
	}

	public String getFactorycode() {
		return factorycode;
	}

	public void setFactorycode(String factorycode) {
		this.factorycode = factorycode;
	}

	public Date getFactorydate() {
		return factorydate;
	}

	public void setFactorydate(Date factorydate) {
		this.factorydate = factorydate;
	}

	public String getMadein() {
		return madein;
	}

	public void setMadein(String madein) {
		this.madein = madein;
	}

	public String getVincode() {
		return vincode;
	}

	public void setVincode(String vincode) {
		this.vincode = vincode;
	}

	public String getBasecode() {
		return basecode;
	}

	public void setBasecode(String basecode) {
		this.basecode = basecode;
	}

	public String getVlevel() {
		return vlevel;
	}

	public void setVlevel(String vlevel) {
		this.vlevel = vlevel;
	}

	public String getVlength() {
		return vlength;
	}

	public void setVlength(String vlength) {
		this.vlength = vlength;
	}

	public String getVwidth() {
		return vwidth;
	}

	public void setVwidth(String vwidth) {
		this.vwidth = vwidth;
	}

	public String getVheight() {
		return vheight;
	}

	public void setVheight(String vheight) {
		this.vheight = vheight;
	}

	public String getWeight() {
		return weight;
	}

	public void setWeight(String weight) {
		this.weight = weight;
	}

	public String getSitarrange() {
		return sitarrange;
	}

	public void setSitarrange(String sitarrange) {
		this.sitarrange = sitarrange;
	}

	public String getSits() {
		return sits;
	}

	public void setSits(String sits) {
		this.sits = sits;
	}

	public String getDragweight() {
		return dragweight;
	}

	public void setDragweight(String dragweight) {
		this.dragweight = dragweight;
	}

	public String getVaxis() {
		return vaxis;
	}

	public void setVaxis(String vaxis) {
		this.vaxis = vaxis;
	}

	public String getEnginemodel() {
		return enginemodel;
	}

	public void setEnginemodel(String enginemodel) {
		this.enginemodel = enginemodel;
	}

	public String getEnginecode() {
		return enginecode;
	}

	public void setEnginecode(String enginecode) {
		this.enginecode = enginecode;
	}

	public String getFueltype() {
		return fueltype;
	}

	public void setFueltype(String fueltype) {
		this.fueltype = fueltype;
	}

	public String getEnginepower() {
		return enginepower;
	}

	public void setEnginepower(String enginepower) {
		this.enginepower = enginepower;
	}

	public String getEnginehorse() {
		return enginehorse;
	}

	public void setEnginehorse(String enginehorse) {
		this.enginehorse = enginehorse;
	}

	public String getReleasestandard() {
		return releasestandard;
	}

	public void setReleasestandard(String releasestandard) {
		this.releasestandard = releasestandard;
	}

	public String getDrivermode() {
		return drivermode;
	}

	public void setDrivermode(String drivermode) {
		this.drivermode = drivermode;
	}

	public String getTyre() {
		return tyre;
	}

	public void setTyre(String tyre) {
		this.tyre = tyre;
	}

	public String getFrontlight() {
		return frontlight;
	}

	public void setFrontlight(String frontlight) {
		this.frontlight = frontlight;
	}

	public String getVariatormode() {
		return variatormode;
	}

	public void setVariatormode(String variatormode) {
		this.variatormode = variatormode;
	}

	public String getRetarder() {
		return retarder;
	}

	public void setRetarder(String retarder) {
		this.retarder = retarder;
	}

	public String getTurner() {
		return turner;
	}

	public void setTurner(String turner) {
		this.turner = turner;
	}

	public String getBreakmode() {
		return breakmode;
	}

	public void setBreakmode(String breakmode) {
		this.breakmode = breakmode;
	}

	public String getFrontbreak() {
		return frontbreak;
	}

	public void setFrontbreak(String frontbreak) {
		this.frontbreak = frontbreak;
		if(frontbreak != null){
			String[] strs = frontbreak.split(",",-1);
			frontbreakList = new ArrayList<String>();
			for(String s :strs){
				frontbreakList.add(s);
			}
		}
	}

	public String getBackbreak() {
		return backbreak;
	}

	public void setBackbreak(String backbreak) {
		this.backbreak = backbreak;
		if(backbreak != null){
			String[] strs = backbreak.split(",",-1);
			backbreakList = new ArrayList<String>();
			for(String s :strs){
				backbreakList.add(s);
			}
		}
	}

	public String getOther() {
		return other;
	}

	public void setOther(String other) {
		this.other = other;
		if(other != null){
			String[] strs = other.split(",",-1);
			otherList = new ArrayList<String>();
			for(String s :strs){
				otherList.add(s);
			}
		}
	}

	public Account getCreator() {
		return creator;
	}

	public void setCreator(Account creator) {
		this.creator = creator;
	}

	public List<String> getFrontbreakList() {
		return frontbreakList;
	}

	public void setFrontbreakList(List<String> frontbreakList) {
		this.frontbreakList = frontbreakList;
		if(frontbreakList != null){
			frontbreak = "";
			for(String s:frontbreakList){
				frontbreak += s + ",";
			}
			frontbreak = frontbreak.substring(0,frontbreak.length()-1);
		}
	}

	public List<String> getBackbreakList() {
		return backbreakList;
	}

	public void setBackbreakList(List<String> backbreakList) {
		this.backbreakList = backbreakList;
		if(backbreakList != null){
			backbreak = "";
			for(String s:backbreakList){
				backbreak += s + ",";
			}
			backbreak = backbreak.substring(0,backbreak.length()-1);
		}
	}

	public String getHangmodefront() {
		return hangmodefront;
	}

	public void setHangmodefront(String hangmodefront) {
		this.hangmodefront = hangmodefront;
		if(hangmodefront != null){
			String[] strs = hangmodefront.split(",",-1);
			hangmodefrontList = new ArrayList<String>();
			for(String s :strs){
				hangmodefrontList.add(s);
			}
		}
	}

	public String getHangmodeback() {
		return hangmodeback;
	}

	public void setHangmodeback(String hangmodeback) {
		this.hangmodeback = hangmodeback;
		if(hangmodeback != null){
			String[] strs = hangmodeback.split(",",-1);
			hangmodebackList = new ArrayList<String>();
			for(String s :strs){
				hangmodebackList.add(s);
			}
		}
	}

	public List<String> getHangmodefrontList() {
		return hangmodefrontList;
	}

	public void setHangmodefrontList(List<String> hangmodefrontList) {
		this.hangmodefrontList = hangmodefrontList;
		if(hangmodefrontList != null){
			hangmodefront = "";
			for(String s:hangmodefrontList){
				hangmodefront += s + ",";
			}
			hangmodefront = hangmodefront.substring(0,hangmodefront.length()-1);
		}
	}

	public List<String> getHangmodebackList() {
		return hangmodebackList;
	}

	public void setHangmodebackList(List<String> hangmodebackList) {
		this.hangmodebackList = hangmodebackList;
		if(hangmodebackList != null){
			hangmodeback = "";
			for(String s:hangmodebackList){
				hangmodeback += s + ",";
			}
			hangmodeback = hangmodeback.substring(0,hangmodeback.length()-1);
		}
	}

	public List<String> getOtherList() {
		return otherList;
	}

	public void setOtherList(List<String> otherList) {
		this.otherList = otherList;
		if(otherList != null){
			other = "";
			for(String s:otherList){
				other += s + ",";
			}
			other = other.substring(0,other.length()-1);
		}
	}
	
	@Transient
	public void setStringToList(){
		setBackbreak(backbreak);
		setFrontbreak(frontbreak);
		setOther(other);
		setHangmodeback(hangmodeback);
		setHangmodefront(hangmodefront);
	}
}
