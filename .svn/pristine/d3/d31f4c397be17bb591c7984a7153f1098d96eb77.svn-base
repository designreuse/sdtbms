package com.bus.stripes.actionbean.vehicle;

import java.util.ArrayList;
import java.util.List;

import security.action.Secure;

import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.HandlesEvent;
import net.sourceforge.stripes.action.RedirectResolution;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.action.StreamingResolution;
import net.sourceforge.stripes.action.UrlBinding;
import net.sourceforge.stripes.integration.spring.SpringBean;

import com.bus.dto.vehicleprofile.VehicleMiles;
import com.bus.dto.vehicleprofile.VehicleProfile;
import com.bus.services.CustomActionBean;
import com.bus.services.VehicleBean;
import com.bus.util.Roles;

@UrlBinding(value="/actionbean/VehicleMiles.action")
public class VehicleMilesActionBean extends CustomActionBean{

	private VehicleBean vBean;
	private VehicleMiles mile;
	private VehicleMiles editmile;
	private String vid;
	private VehicleProfile vp;
	
	@DefaultHandler
	@Secure(roles=Roles.VEHICLE_MILE_VIEW)
	public Resolution defaultAction(){
		try{
			if(vid == null){
				vid = context.getRequest().getParameter("vid");
				if(vid == null)
					return context.errorResolution("没有选中的车辆", "请在车辆档案页面选择车辆再修改。");
			}
			vp = vBean.getVehicleProfileById(Integer.parseInt(vid));
			if(vp.getMiles() == null){
				vp.setMiles(new ArrayList<VehicleMiles>());
			}
			return new ForwardResolution("/vehicle/miles.jsp").addParameter("vid", vid);
		}catch(Exception e){
			return context.errorResolution("读取车辆信息失败", "读取车辆信息失败。错误:"+e.getMessage());
		}
	}
	
	@HandlesEvent(value="addMile")
	@Secure(roles = Roles.VEHICLE_MILE_EDIT)
	public Resolution addMile(){
		try{
			if(mile.getVehicle() == null || mile.getVehicle().getId() == null)
				return context.errorResolution("没有选中的车辆，车ID为空", "请按提示正确操作。");
			vid = mile.getVehicle().getId().toString();
			vBean.saveVehicleMile(mile);
			return new RedirectResolution("/actionbean/VehicleMiles.action?vid="+vid);
		}catch(Exception e){
			return context.errorResolution("没有选中的车辆", "请按提示正确操作。错误:"+e.getMessage());
		}
	}
	
	@HandlesEvent(value="updateMile")
	@Secure(roles = Roles.VEHICLE_MILE_EDIT)
	public Resolution updateMile(){
		try{
			if(editmile  == null || editmile.getId() == null)
				return context.errorResolutionAjax("没有选中的车辆，车ID为空", "请按提示正确操作。");
			vBean.updateVehicleMiles(editmile);
			return new StreamingResolution("text/charset=utf-8;","修改成功.");
		}catch(Exception e){
			return context.errorResolution("没有选中的车辆", "请按提示正确操作。错误:"+e.getMessage());
		}
	}
	
	@HandlesEvent(value="deleteVehicleMile")
	@Secure(roles = Roles.VEHICLE_MILE_EDIT)
	public Resolution deleteVehicleMile(){
		try{
			if(vid == null){
				vid = context.getRequest().getParameter("vid");
				if(vid == null)
					return context.errorResolutionAjax("没有选中的车辆", "vid 没发送成功。");
			}
			vBean.removeVehicleMiles(vid);
			return new StreamingResolution("text/charset=utf-8;","删除成功.");
		}catch(Exception e){
			return context.errorResolutionAjax("没有选中的车辆", "请按提示正确操作。错误:"+e.getMessage());
		}
	}
	
	public VehicleBean getvBean() {
		return vBean;
	}
	@SpringBean
	public void setvBean(VehicleBean vBean) {
		this.vBean = vBean;
	}
	public VehicleMiles getMile() {
		return mile;
	}
	public void setMile(VehicleMiles mile) {
		this.mile = mile;
	}


	public VehicleProfile getVp() {
		return vp;
	}


	public void setVp(VehicleProfile vp) {
		this.vp = vp;
	}

	public VehicleMiles getEditmile() {
		return editmile;
	}

	public void setEditmile(VehicleMiles editmile) {
		this.editmile = editmile;
	}
}
