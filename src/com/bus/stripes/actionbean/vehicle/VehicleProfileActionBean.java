package com.bus.stripes.actionbean.vehicle;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import security.action.Secure;
import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.FileBean;
import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.HandlesEvent;
import net.sourceforge.stripes.action.RedirectResolution;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.action.StreamingResolution;
import net.sourceforge.stripes.action.UrlBinding;
import net.sourceforge.stripes.integration.spring.SpringBean;

import com.bus.dto.vehicleprofile.VehicleCheck;
import com.bus.dto.vehicleprofile.VehicleFiles;
import com.bus.dto.vehicleprofile.VehicleProfile;
import com.bus.services.CustomActionBean;
import com.bus.services.VehicleBean;
import com.bus.stripes.selector.VehicleSelector;
import com.bus.util.ExcelFileSaver;
import com.bus.util.HRUtil;
import com.bus.util.Roles;

@UrlBinding(value="/actionbean/VehicleProfile.action")
public class VehicleProfileActionBean extends CustomActionBean{

	private VehicleBean vBean;
	private List<VehicleProfile> profiles;
	private VehicleProfile profile;
	private VehicleSelector selector;
	private VehicleCheck check;
	private VehicleCheck editCheck;
	private FileBean checkFile;
	
	
	//List files
	private List<VehicleCheck> maintenances;
	private List<VehicleCheck> repairs;
	private List<VehicleCheck> fullchecks;
	private List<VehicleCheck> annul;
	private List<VehicleCheck> extras;
	
	//file upload
	private FileBean detailFile;
	private FileBean repairFile;
	private FileBean teamFile;
	private FileBean newVehicleFile;
	
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
			maintenances = vBean.getVehicleCheckByTypeMaintennance(profile.getId());
			repairs = vBean.getVehicleCheckByTypeRepaire(profile.getId());
			fullchecks = vBean.getVehicleCheckByTypeFullCheck(profile.getId());
			annul = vBean.getVehicleCheckByTypeAnnul(profile.getId());
			extras = vBean.getVehicleCheckByTypeExtras(profile.getId());
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
	
	@HandlesEvent(value="addVehicleCheck")
	@Secure(roles=Roles.VEHICLE_FILE_CHECK)
	public Resolution addVehicleCheck(){
		try{
			String targetId = context.getRequest().getParameter("targetId");
			String link = context.getRequest().getParameter("returnLink");
			if(link == null) link = "/actionbean/VehicleProfile.action";
			if(targetId == null)
				return context.errorResolution("ID 没有上传", "上传数据出错.");
			VehicleProfile vptemp = vBean.getVehicleProfileById(Integer.parseInt(targetId));
			check.setVehicle(vptemp);
			check.setCreator(context.getUser());
			VehicleFiles vf = null;
			if(checkFile != null){
				String filename = "车辆_" + check.getCtype() + "_" +vptemp.getId() + HRUtil.getFileExtension(checkFile.getFileName());
				String ipath = context.getLocalFileLocation() + "车辆/"+check.getCtype() + "/"+filename;
				File file = new File(ipath);
				checkFile.save(file);
				
				vf = new VehicleFiles();
				vf.setCreator(context.getUser());
				vf.setUdate(new Date());
				vf.setFilename(filename);
				vf.setIpath(ipath);
				vBean.saveVehicleFile(vf);
			}
			if(vf != null)
				check.setImage(vf);
			vBean.saveVehicleCheck(check);
			return new RedirectResolution(link);
		}catch(Exception e){
			return context.errorResolution("服务器保存出错", "请联系管理员.错误:"+e.getMessage());
		}
	}
	
	/**
	 * Currently only file upload updatetable
	 * @return
	 */
	@HandlesEvent(value="updateVehicleCheck")
	@Secure(roles=Roles.VEHICLE_FILE_CHECK)
	public Resolution updateVehicleCheck(){
		String ipath = "";
		try{
			VehicleFiles image = new VehicleFiles();
			String checkId = context.getRequest().getParameter("checkId");
			String link = context.getRequest().getParameter("returnLink");
			if(checkFile != null){
				VehicleCheck vc = vBean.getVehicleCheckById(Integer.parseInt(checkId));
				if(vc.getImage() != null){
					image = vc.getImage();
					File oldF = new File(vc.getImage().getIpath());
					oldF.delete();
				}
				String filename = "车辆_" + vc.getCtype() + "_" +vc.getVehicle().getId() + HRUtil.getFileExtension(checkFile.getFileName());
				ipath = context.getLocalFileLocation() + "车辆/"+vc.getCtype() + "/"+filename;
				File newF = new File(ipath);
				checkFile.save(newF);
				image.setIpath(ipath);
				image.setFilename(filename);
				image.setCreator(context.getUser());
				image.setUdate(new Date());
				image = vBean.saveVehicleFile(image);
				if(vc.getImage() == null){
					vc.setImage(image);
					vBean.updateVehicleCheck(vc);
				}
			}
			return new RedirectResolution(link);
		}catch(Exception e){
			return defaultAction();
		}
	}
	
	@HandlesEvent(value="deleteVechileCheck")
	@Secure(roles=Roles.VEHICLE_FILE_CHECK)
	public Resolution deleteVechileCheck(){
		try{
			String checkId = context.getRequest().getParameter("checkId");
			if(checkId == null)
				return context.errorResolutionAjax("没用ID可用", "没有ID可用，请按提示删除。");
			vBean.removeVehicleCheck(checkId);
			return new StreamingResolution("text/charset=utf-8;","删除成功");
		}catch(Exception e){
			return context.errorResolutionAjax("服务器删除出错", "遇到问题."+e.getMessage());
		}
	}
	
	@HandlesEvent(value="throwVehicle")
	@Secure(roles = Roles.VEHICLE_PROFILE_EDIT)
	public Resolution throwVehicle(){
		try{
			String vid = context.getRequest().getParameter("vid");
			String dateval = context.getRequest().getParameter("dateval");
			if(vid == null || dateval == null)
				return context.errorResolutionAjax("没有提供ID或日期", "请确保vid&dateval已经发送");
			Date dateVal = HRUtil.parseDate(dateval, "yyyy-MM-dd");
			vBean.throwVehicle(vid, dateVal);
			return new StreamingResolution("text/charset=utf-8;","完成报废");
		}catch(Exception e){
			e.printStackTrace();
			return context.errorResolutionAjax("报废出错", "错误信息:"+e.getMessage());
		}
	}
	
	@HandlesEvent(value="vehicleDetailFileUpload")
	@Secure(roles = Roles.ADMINISTRATOR)
	public Resolution vehicleDetailFileUpload(){
		try{
			if(detailFile != null){
				ExcelFileSaver saver = new ExcelFileSaver((FileInputStream)detailFile.getInputStream());
				String result = saver.saveVehicleDetail(vBean,context.getUser());
				if(!result.equals("")){
					return  context.errorResolution("上传车辆档案出错", result);
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return defaultAction();
	}
	
	@HandlesEvent(value="vehicleRepaireDatesUpload")
	@Secure(roles = Roles.ADMINISTRATOR)
	public Resolution vehicleRepaireDatesUpload(){
		try{
			if(repairFile != null){
				ExcelFileSaver saver = new ExcelFileSaver((FileInputStream)repairFile.getInputStream());
				String result = saver.insertRepaireDatesToVehicles(vBean,context.getUser());
				if(!result.equals("")){
					return  context.errorResolution("上传车辆档案出错", result);
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return defaultAction();
	}
	
	@HandlesEvent(value="vehicleTeamFileUpload")
	@Secure(roles = Roles.ADMINISTRATOR)
	public Resolution vehicleTeamFileUpload(){
		try{
			if(teamFile != null){
				ExcelFileSaver saver = new ExcelFileSaver((FileInputStream)teamFile.getInputStream());
				String result = saver.saveTeamNameAndLeader(vBean,context.getUser());
				if(!result.equals("")){
					return  context.errorResolution("上传车辆档案出错", result);
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return defaultAction();
	}
	
	@HandlesEvent(value="vehicleNewFileUpload")
	@Secure(roles = Roles.ADMINISTRATOR)
	public Resolution vehicleNewFileUpload(){
		try{
			if(newVehicleFile != null){
				ExcelFileSaver saver = new ExcelFileSaver((FileInputStream)newVehicleFile.getInputStream());
				String result = saver.saveNewVehicle(vBean,context.getUser());
				if(!result.equals("")){
					return  context.errorResolution("上传车辆档案出错", result);
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return defaultAction();
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

	public VehicleCheck getCheck() {
		return check;
	}

	public void setCheck(VehicleCheck check) {
		this.check = check;
	}

	public FileBean getCheckFile() {
		return checkFile;
	}

	public void setCheckFile(FileBean checkFile) {
		this.checkFile = checkFile;
	}

	public List<VehicleCheck> getMaintenances() {
		return maintenances;
	}

	public void setMaintenances(List<VehicleCheck> maintenances) {
		this.maintenances = maintenances;
	}

	public VehicleCheck getEditCheck() {
		return editCheck;
	}

	public void setEditCheck(VehicleCheck editCheck) {
		this.editCheck = editCheck;
	}

	public List<VehicleCheck> getRepairs() {
		return repairs;
	}

	public void setRepairs(List<VehicleCheck> repairs) {
		this.repairs = repairs;
	}

	public VehicleSelector getSelector() {
		return selector;
	}

	public void setSelector(VehicleSelector selector) {
		this.selector = selector;
	}

	public List<VehicleCheck> getFullchecks() {
		return fullchecks;
	}

	public void setFullchecks(List<VehicleCheck> fullchecks) {
		this.fullchecks = fullchecks;
	}

	public List<VehicleCheck> getAnnul() {
		return annul;
	}

	public void setAnnul(List<VehicleCheck> annul) {
		this.annul = annul;
	}

	public List<VehicleCheck> getExtras() {
		return extras;
	}

	public void setExtras(List<VehicleCheck> extras) {
		this.extras = extras;
	}

	public FileBean getDetailFile() {
		return detailFile;
	}

	public void setDetailFile(FileBean detailFile) {
		this.detailFile = detailFile;
	}

	public FileBean getRepairFile() {
		return repairFile;
	}

	public void setRepairFile(FileBean repairFile) {
		this.repairFile = repairFile;
	}

	public FileBean getTeamFile() {
		return teamFile;
	}

	public void setTeamFile(FileBean teamFile) {
		this.teamFile = teamFile;
	}

	public FileBean getNewVehicleFile() {
		return newVehicleFile;
	}

	public void setNewVehicleFile(FileBean newVehicleFile) {
		this.newVehicleFile = newVehicleFile;
	}
	

	
}
