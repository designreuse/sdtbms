package com.bus.stripes.actionbean.vehicle;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import security.action.Secure;
import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.HandlesEvent;
import net.sourceforge.stripes.action.RedirectResolution;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.action.StreamingResolution;
import net.sourceforge.stripes.action.UrlBinding;
import net.sourceforge.stripes.integration.spring.SpringBean;

import com.bus.dto.vehicleprofile.VehicleProfile;
import com.bus.services.CustomActionBean;
import com.bus.services.VehicleBean;
import com.bus.stripes.selector.VehicleSelector;
import com.bus.util.Roles;

@UrlBinding(value="/actionbean/VehicleProfile.action")
public class VehicleProfileActionBean extends CustomActionBean{

	private VehicleBean vBean;
	private List<VehicleProfile> profiles;
	private VehicleProfile profile;
	private VehicleSelector selector;
	
	private int pagenum;
	private int lotsize;
	private Long totalcount;
	private Long recordsTotal;
	
	@DefaultHandler
	@Secure(roles=Roles.VEHICLE_SYSTEM)
	public Resolution defaultAction(){
		loadVehicles();
		return new ForwardResolution("/vehicle/profile.jsp").addParameter("pagenum", pagenum);
	}
	
	private void loadVehicles() {
		try{
			if(pagenum <= 0 || lotsize <= 0){
				pagenum = 1;
				lotsize = 20;
			}
			Map map = null;
			if(selector == null){
				map = vBean.getVehicles(pagenum, lotsize);
			}else{
				String statement = selector.getSelectorStatement();
				map = vBean.getVehicles(pagenum, lotsize,statement);
			}
			setRecordsTotal((Long) map.get("count"));
			setProfiles((List<VehicleProfile>) map.get("list"));
			setTotalcount(getRecordsTotal()/lotsize+1);
			if(pagenum > totalcount)
				pagenum = Integer.parseInt(totalcount.toString());
		}catch(Exception e){
			System.out.println(e.getMessage());
			profiles  = new ArrayList<VehicleProfile>();
			setRecordsTotal(0L);
			setTotalcount(1L);
			setPagenum(1);
		}
	}

	@HandlesEvent(value="addVehicle")
	@Secure(roles=Roles.VEHICLE_PROFILE_EDIT)
	public Resolution addVehicle(){
		try{
			if(profile == null)
				return new StreamingResolution("text/charset=UTF-8;","数据上传出错");
			vBean.saveVehicle(profile);
			return new StreamingResolution("text/charset=UTF-8;","新建成功");
		}catch(Exception e){
			return new StreamingResolution("text/charset=UTF-8;","创建失败."+e.getMessage());
		}
	}
	
	@HandlesEvent(value="deleteVehicle")
	@Secure(roles=Roles.VEHICLE_PROFILE_EDIT)
	public Resolution deleteVehicle(){
		try{
			String targetId = context.getRequest().getParameter("targetId");
			if(targetId == null)
				return new StreamingResolution("text/charset=UTF-8;","数据上传出错");
			vBean.deleteVehicle(Integer.parseInt(targetId));
			return new StreamingResolution("text/charset=UTF-8;","删除成功");
		}catch(Exception e){
			return new StreamingResolution("text/charset=UTF-8;","删除失败."+e.getMessage());
		}
	}
	
	@HandlesEvent(value="vehicleDetail")
	@Secure(roles=Roles.VEHICLE_PROFILE_VIEW)
	public Resolution vehicleDetail(){
		try{
			String targetId = context.getRequest().getParameter("targetId");
			if(targetId == null)
				return new StreamingResolution("text/charset=UTF-8;","数据上传出错");
			profile = vBean.getVehicleProfileById(Integer.parseInt(targetId));
			return new ForwardResolution("/vehicle/detail.jsp");
		}catch(Exception e){
			return new StreamingResolution("text/charset=UTF-8;","数据获取出错."+e.getMessage());
		}
	}
	
	@HandlesEvent(value="editVehicle")
	@Secure(roles=Roles.VEHICLE_PROFILE_EDIT)
	public Resolution editVehicle(){
		try{
			if(profile.getId() == null)
				return new StreamingResolution("text/charset=UTF-8;","数据上传失败，id没上传.");
			else
				vBean.saveVehicle(profile);
			return new StreamingResolution("text/charset=UTF-8;","修改成功");
		}catch(Exception e){
			return new StreamingResolution("text/charset=UTF-8;","修改失败."+e.getMessage());
		}
	}
	
	@HandlesEvent(value="filter")
	public Resolution filter(){
		return defaultAction();
	}
	
	@HandlesEvent(value="prevpage")
	public Resolution prevpage(){
		pagenum--;
		return defaultAction();
	}
	
	@HandlesEvent(value="nextpage")
	public Resolution nextpage(){
		pagenum++;
		return defaultAction();
	}
	
	public int getPagenum() {
		return pagenum;
	}


	public void setPagenum(int pagenum) {
		this.pagenum = pagenum;
	}


	public int getLotsize() {
		return lotsize;
	}


	public void setLotsize(int lotsize) {
		this.lotsize = lotsize;
	}


	public Long getTotalcount() {
		return totalcount;
	}


	public void setTotalcount(Long totalcount) {
		this.totalcount = totalcount;
	}


	public Long getRecordsTotal() {
		return recordsTotal;
	}


	public void setRecordsTotal(Long recordsTotal) {
		this.recordsTotal = recordsTotal;
	}

	
	public VehicleBean getvBean() {
		return vBean;
	}
	@SpringBean
	public void setvBean(VehicleBean vBean) {
		this.vBean = vBean;
	}

	public List<VehicleProfile> getProfiles() {
		return profiles;
	}

	public void setProfiles(List<VehicleProfile> profiles) {
		this.profiles = profiles;
	}

	public VehicleProfile getProfile() {
		return profile;
	}

	public void setProfile(VehicleProfile profile) {
		this.profile = profile;
	}


	
}
