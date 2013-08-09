package com.bus.stripes.actionbean.vehicle;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import security.action.Secure;

import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.HandlesEvent;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.action.UrlBinding;
import net.sourceforge.stripes.integration.spring.SpringBean;

import com.bus.dto.vehicleprofile.VehicleLane;
import com.bus.dto.vehicleprofile.VehicleLaneMapper;
import com.bus.dto.vehicleprofile.VehicleProfile;
import com.bus.services.CustomActionBean;
import com.bus.services.VehicleBean;
import com.bus.stripes.selector.VehicleSelector;
import com.bus.util.Roles;

@UrlBinding(value="/actionbean/VehicleLane.action")
public class VehicleLaneActionBean extends CustomActionBean{
	
	private List<VehicleLane> routes;
	private VehicleLane route;
	private Set<VehicleLaneMapper> mappers;
	private Long totalcount;
	private VehicleSelector selector;
	private List<String> selectedVehicles;
	private VehicleLane newRoute;
	private VehicleLane delRoute;
	
	private List<VehicleProfile> vehicles; //for emptyroute.jsp page
	
	private VehicleBean vBean;
	
	@DefaultHandler
	@Secure(roles=Roles.VEHICLE_ROUTE_VIEW)
	public Resolution defaultAction(){
		try{
			Map map = vBean.getAllVehicleLanes();
			routes = (List<VehicleLane>) map.get("list");
			totalcount = (Long) map.get("count");
		}catch(Exception e){
			routes = new ArrayList<VehicleLane>();
			totalcount = 0L;
		}
		return new ForwardResolution("/vehicle/route.jsp");
	}

	@HandlesEvent(value="routeDetail")
	@Secure(roles=Roles.VEHICLE_ROUTE_VIEW)
	public Resolution routeDetail(){
		try{
			String targetId = context.getRequest().getParameter("targetId");
			if(targetId == null)
				return defaultAction();
			route = vBean.getVehicleLaneById(targetId);
			if(route == null)
				return defaultAction();
			setMappers(route.getMapper());
			totalcount = (Long.parseLong(route.getVehicleCount()));
		}catch(Exception e){
			routes = new ArrayList<VehicleLane>();
			totalcount = 0L;
		}
		return new ForwardResolution("/vehicle/routedetail.jsp");
	}
	
	@HandlesEvent(value="emptyRoute")
	@Secure(roles=Roles.VEHICLE_ROUTE_VIEW)
	public Resolution emptyRoute(){
		try{
			vehicles = vBean.getVehiclesNoRoute();
			totalcount = ((long)vehicles.size());
		}catch(Exception e){
			routes = new ArrayList<VehicleLane>();
			totalcount = 0L;
		}
		return new ForwardResolution("/vehicle/emptyroute.jsp");
	}
	
	@HandlesEvent(value="newRouteAction")
	@Secure(roles=Roles.VEHICLE_ROUTE_EDIT)
	public Resolution newRouteAction(){
		try{
			if(newRoute != null){
				if(newRoute.getNum().trim().equals("")){
					return context.errorResolution("线路不能为空", "线路编号不能为空");
				}
				if(vBean.isRouteExist(newRoute)){
					return context.errorResolution("线路已经存在", "线路:"+newRoute.getNum()+","+newRoute.getDetail()+" 已经存在");
				}
				vBean.saveVehicleLane(newRoute);
			}
			return defaultAction();
		}catch(Exception e){
			return context.errorResolution("添加失败", "错误信息:"+e.getMessage());
		}
	}
	
	@HandlesEvent(value="deleteRouteAction")
	@Secure(roles=Roles.VEHICLE_ROUTE_EDIT)
	public Resolution deleteRouteAction(){
		try{
			if(delRoute != null){
				if(delRoute.getNum().trim().equals("")){
					return context.errorResolution("线路不能为空", "线路编号不能为空");
				}
				if(vBean.isRouteExist(delRoute)){
					vBean.removeVehicleLane(delRoute);
				}else{
					return context.errorResolution("线路不存在", "线路:"+delRoute.getNum()+","+delRoute.getDetail()+" 不存在");
				}
			}
			return defaultAction();
		}catch(Exception e){
			return context.errorResolution("删除失败", "错误信息:"+e.getMessage());
		}
	}
	
	@HandlesEvent(value="joinRoute")
	@Secure(roles=Roles.VEHICLE_ROUTE_EDIT)
	public Resolution joinRoute(){
		try{
			if(newRoute != null && selectedVehicles != null){
				if(newRoute.getNum().trim().equals("")){
					return context.errorResolution("线路不能为空", "线路编号不能为空");
				}
				if(vBean.isRouteExist(newRoute)){
					vBean.joinVehiclesToRoute(newRoute, selectedVehicles);
				}else{
					return context.errorResolution("线路不存在", "线路:"+newRoute.getNum()+","+newRoute.getDetail()+" 不存在");
				}
			}
			return defaultAction();
		}catch(Exception e){
			return context.errorResolution("加入线路失败", "错误信息:"+e.getMessage());
		}
	}
	
	@HandlesEvent(value="filter")
	public Resolution filter(){
		return defaultAction();
	}
	
	public List<VehicleLane> getRoutes() {
		return routes;
	}

	public void setRoutes(List<VehicleLane> routes) {
		this.routes = routes;
	}

	public Long getTotalcount() {
		return totalcount;
	}

	public void setTotalcount(Long totalcount) {
		this.totalcount = totalcount;
	}

	public VehicleBean getvBean() {
		return vBean;
	}
	@SpringBean
	public void setvBean(VehicleBean vBean) {
		this.vBean = vBean;
	}

	public VehicleSelector getSelector() {
		return selector;
	}

	public void setSelector(VehicleSelector selector) {
		this.selector = selector;
	}

	public VehicleLane getRoute() {
		return route;
	}

	public void setRoute(VehicleLane route) {
		this.route = route;
	}

	public List<String> getSelectedVehicles() {
		return selectedVehicles;
	}

	public void setSelectedVehicles(List<String> selectedVehicles) {
		this.selectedVehicles = selectedVehicles;
	}

	public Set<VehicleLaneMapper>  getMappers() {
		return mappers;
	}

	public void setMappers(Set<VehicleLaneMapper>  mappers) {
		this.mappers = mappers;
	}

	public VehicleLane getNewRoute() {
		return newRoute;
	}

	public void setNewRoute(VehicleLane newRoute) {
		this.newRoute = newRoute;
	}

	public VehicleLane getDelRoute() {
		return delRoute;
	}

	public void setDelRoute(VehicleLane delRoute) {
		this.delRoute = delRoute;
	}

	public List<VehicleProfile> getVehicles() {
		return vehicles;
	}

	public void setVehicles(List<VehicleProfile> vehicles) {
		this.vehicles = vehicles;
	}

}
