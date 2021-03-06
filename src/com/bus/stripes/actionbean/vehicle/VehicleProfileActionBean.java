package com.bus.stripes.actionbean.vehicle;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

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

import com.bus.dto.vehicleprofile.VehicleBasicInfo;
import com.bus.dto.vehicleprofile.VehicleCheck;
import com.bus.dto.vehicleprofile.VehicleChange;
import com.bus.dto.vehicleprofile.VehicleAccident;
//import com.bus.dto.vehicleprofile.VehicleCheck;
import com.bus.dto.vehicleprofile.VehicleFiles;
import com.bus.dto.vehicleprofile.VehicleLane;
import com.bus.dto.vehicleprofile.VehicleLevel;
import com.bus.dto.vehicleprofile.VehiclePartsRepair;
import com.bus.dto.vehicleprofile.VehicleProfile;
import com.bus.dto.vehicleprofile.VehicleRepairRecord;
import com.bus.dto.vehicleprofile.VehicleTeam;
import com.bus.dto.vehicleprofile.VehicleTechnicalDetail;
import com.bus.dto.vehicleprofile.VehicleUseRecord;
import com.bus.services.CustomActionBean;
import com.bus.services.VehicleBean;
import com.bus.stripes.selector.VehicleSelector;
import com.bus.util.ExcelFileSaver;
import com.bus.util.HRUtil;
import com.bus.util.Roles;
import com.bus.util.VehicleProfileExcelFile;
import com.google.gson.JsonObject;

@UrlBinding(value="/actionbean/VehicleProfile.action")
public class VehicleProfileActionBean extends CustomActionBean{

	private FileBean detailFile;
	private FileBean newVehicleFile;
	private FileBean recordIdFile;
	private FileBean teamLaneFile;
	private FileBean profilePicFile;
	private FileBean documentXSSFUpload;
	
	
	public FileBean getDocumentXSSFUpload() {
		return documentXSSFUpload;
	}

	public void setDocumentXSSFUpload(FileBean documentXSSFUpload) {
		this.documentXSSFUpload = documentXSSFUpload;
	}

	public FileBean getProfilePicFile() {
		return profilePicFile;
	}

	public void setProfilePicFile(FileBean profilePicFile) {
		this.profilePicFile = profilePicFile;
	}

	public FileBean getTeamLaneFile() {
		return teamLaneFile;
	}

	public void setTeamLaneFile(FileBean teamLaneFile) {
		this.teamLaneFile = teamLaneFile;
	}

	public FileBean getRecordIdFile() {
		return recordIdFile;
	}

	public void setRecordIdFile(FileBean recordIdFile) {
		this.recordIdFile = recordIdFile;
	}

	public FileBean getDetailFile() {
		return detailFile;
	}

	public void setDetailFile(FileBean detailFile) {
		this.detailFile = detailFile;
	}

	public FileBean getNewVehicleFile() {
		return newVehicleFile;
	}

	public void setNewVehicleFile(FileBean newVehicleFile) {
		this.newVehicleFile = newVehicleFile;
	}

	@HandlesEvent(value="vehicleXSSFUpload")
	@Secure(roles= Roles.ADMINISTRATOR)
	public Resolution vehicleXSSFUpload(){
		try{
			if(documentXSSFUpload == null)
				throw new Exception("文档没有上传成功");
			boolean isExcel2007 = false;
			if(documentXSSFUpload.getFileName().contains(".xlsx"))
				isExcel2007 = true;
			else
				throw new Exception("文件不是Excel2007文件");
			VehicleProfileExcelFile saver = new VehicleProfileExcelFile((FileInputStream)documentXSSFUpload.getInputStream());
			saver.saveVehicleProfileFromExcel(vBean, context.getUser());
			return defaultAction();
		}catch(Exception e){
			e.printStackTrace();
			return context.errorResolution("服务器出错", HRUtil.getStringFromStackTrace(e));
		}
	}
	
//	@HandlesEvent(value="addProfilePicture")
//	@Secure(roles=Roles.VEHICLE_PROFILE_EDIT)
//	public Resolution addProfilePicture(){
//		try{
//			System.out.println("正在添加");
//			String targetId = context.getRequest().getParameter("targetId");
//			String link = context.getRequest().getParameter("returnLink");
//			if(link == null) link = "/actionbean/VehicleProfile.action";
//			if(targetId == null)
//				return context.errorResolution("ID 没有上传", "上传数据出错.");
//			VehicleProfile vptemp = vBean.getVehicleProfileById(Integer.parseInt(targetId));
//			check.setVehicle(vptemp);
//			check.setCreator(context.getUser());
//			VehicleFiles vf = null;
//			if(checkFile != null){
//				System.out.println("Check file is not null . saving");
//				String filename = "车辆_" + check.getCtype() + "_" +vptemp.getId() + HRUtil.getFileExtension(checkFile.getFileName());
//				String ipath = context.getLocalFileLocation() + "车辆/"+check.getCtype() + "/"+filename;
//				File file = new File(ipath);
//				checkFile.save(file);
//				System.out.println("Save file to "+ file.getAbsolutePath());
//				
//				vf = new VehicleFiles();
//				vf.setCreator(context.getUser());
//				vf.setUdate(new Date());
//				vf.setFilename(filename);
//				vf.setIpath(ipath);
//				vBean.saveVehicleFile(vf);
//			}
//			if(vf != null)
//				check.setImage(vf);
//			vBean.saveVehicleCheck(check);
//			return new RedirectResolution(link);
//		}catch(Exception e){
//			e.printStackTrace();
//			return context.errorResolution("服务器保存出错", "请联系管理员.错误:"+e.getMessage());
//		}
//	}
	
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
	
//	@HandlesEvent(value="vehicleRepaireDatesUpload")
//	@Secure(roles = Roles.ADMINISTRATOR)
//	public Resolution vehicleRepaireDatesUpload(){
//		try{
//			if(repairFile != null){
//				ExcelFileSaver saver = new ExcelFileSaver((FileInputStream)repairFile.getInputStream());
//				String result = saver.insertRepaireDatesToVehicles(vBean,context.getUser());
//				if(!result.equals("")){
//					return  context.errorResolution("上传车辆档案出错", result);
//				}
//			}
//		}catch(Exception e){
//			e.printStackTrace();
//		}
//		return defaultAction();
//	}
//	
//	@HandlesEvent(value="vehicleTeamFileUpload")
//	@Secure(roles = Roles.ADMINISTRATOR)
//	public Resolution vehicleTeamFileUpload(){
//		try{
//			if(teamFile != null){
//				ExcelFileSaver saver = new ExcelFileSaver((FileInputStream)teamFile.getInputStream());
//				String result = saver.saveTeamNameAndLeader(vBean,context.getUser());
//				if(!result.equals("")){
//					return  context.errorResolution("上传车辆档案出错", result);
//				}
//			}
//		}catch(Exception e){
//			e.printStackTrace();
//		}
//		return defaultAction();
//	}
	
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
	
	@HandlesEvent(value="vehicleTeamAndLaneUpload")
	@Secure(roles = Roles.ADMINISTRATOR)
	public Resolution vehicleTeamAndLaneUpload(){
		try{
			if(teamLaneFile != null){
				ExcelFileSaver saver = new ExcelFileSaver((FileInputStream)teamLaneFile.getInputStream());
				String result = saver.saveTeamAndLane(vBean,context.getUser());
				if(!result.equals("")){
					return  context.errorResolution("上传车辆档案出错", result);
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return defaultAction();
	}
	
	@HandlesEvent(value="vehicleRecordIdUpload")
	@Secure(roles = Roles.ADMINISTRATOR)
	public Resolution vehicleRecordIdUpload(){
		try{
			if(recordIdFile != null){
				ExcelFileSaver saver = new ExcelFileSaver((FileInputStream)recordIdFile.getInputStream());
				String result = saver.saveVehicleRecordIds(vBean,context.getUser());
				if(!result.equals("")){
					return  context.errorResolution("上传车辆档案出错", result);
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return defaultAction();
	}

	
	private VehicleBean vBean;
	public VehicleBean getvBean() {return vBean;}
	@SpringBean
	public void setvBean(VehicleBean vBean) {this.vBean = vBean;}
	
	private VehicleSelector selector;
	private List<VehicleProfile> profiles;
	private VehicleProfile profile;
	private int pagenum;
	private int lotsize;
	private Long totalcount;
	private Long recordsTotal;
	
	private VehicleCheck check;
	private VehicleCheck editCheck;
	private FileBean checkFile;
	
	/**
	 * 车辆档案的表格
	 */
	private VehicleTechnicalDetail vtech;
	private VehicleBasicInfo vBasic;
	private VehicleRepairRecord vRepair;
	private VehiclePartsRepair vParts;
	private VehicleLevel vLevel;
	private VehicleChange vChange;
	private VehicleUseRecord vUseRecord;
	private VehicleAccident vAccident;
	private List<String> selectList;
	
	/**
	 * 车辆档案显示用数据
	 */
	private List<VehicleRepairRecord> vRepairList;
	private List<VehiclePartsRepair> vPartsList;
	private List<VehicleLevel> vLevelList;
	private List<VehicleChange> vChangeList;
	private List<VehicleUseRecord> vUseRecordList;
	private List<VehicleAccident> vAccidentList;
	
	/**
	 * 时间变量，公用
	 */
	private Date date1;
	private Date date2;
	
	private List<VehicleTeam> teams;
	private List<VehicleLane> lanes;
	
	//List files
	private List<VehicleCheck> maintenances;
	private List<VehicleCheck> repairs;
	private List<VehicleCheck> fullchecks;
	private List<VehicleCheck> annul;
	private List<VehicleCheck> extras;
	
	private void setDefaultVariables(){
		if(pagenum <= 0 || lotsize <= 0){
			pagenum = 1;
			lotsize = 20;
		}
	}
	
	@DefaultHandler
	public Resolution defaultAction(){
		setDefaultVariables();
		try{
			Map<String,Object> map = null;
			if(selector == null){
				map = vBean.getVehicleProfiles(pagenum,lotsize);
			}else{
				String statement = selector.getSelectorStatement();
				System.out.println(statement);
				map = vBean.getVehicleProfilesBySelector(pagenum,lotsize,statement);
			}
			System.out.println("In VProfileAB defaultAction pagenum:"+pagenum);
			teams = vBean.getAllVehicleTeams();
			lanes = (List<VehicleLane>) vBean.getAllVehicleLaneNames();
			profiles = (List<VehicleProfile>) map.get("list");
			recordsTotal = (Long) map.get("count");
			totalcount = recordsTotal/lotsize + 1;
			if(pagenum > totalcount)
				pagenum = Integer.parseInt(totalcount.toString());
		}catch(Exception e){
			e.printStackTrace();
			if(profiles == null)
				profiles = new ArrayList<VehicleProfile>();
			if(teams == null)
				teams = new ArrayList<VehicleTeam>();
			if(lanes == null)
				lanes = new ArrayList<VehicleLane>();
//			setRecordsTotal(0L);
//			setTotalcount(1L);
//			setPagenum(1);
		}
		ForwardResolution fr = new ForwardResolution("/vehicle/profile.jsp");
		fr.addParameter("pagenum", pagenum);
		fr.addParameter("lotsize", lotsize);
		return fr;
	}

	@HandlesEvent(value="addVehicle")
	@Secure(roles=Roles.VEHICLE_PROFILE_EDIT)
	public Resolution addVehicle(){
		JsonObject json = new JsonObject();
		try{
			if(profile == null){
				json.addProperty("result", "0");
				json.addProperty("msg", "没有收到档案数据，新建失败");
				return new StreamingResolution("text/charset=UTF-8;",json.toString());
			}
			vBean.saveVehicle(profile,context.getUser());
			json.addProperty("result", "1");
			json.addProperty("msg", "创建成功");
			return new StreamingResolution("text/charset=UTF-8;",json.toString());
		}catch(Exception e){
			e.printStackTrace();
			json.addProperty("result", "0");
			json.addProperty("msg", "新建的时候出错，新建失败."+e.getMessage());
			return new StreamingResolution("text/charset=UTF-8;",json.toString());
		}
	}
	
	@HandlesEvent(value="deleteVehicle")
	@Secure(roles=Roles.VEHICLE_PROFILE_EDIT)
	public Resolution deleteVehicle(){
		JsonObject json = new JsonObject();
		try{
			String targetId = context.getRequest().getParameter("targetId");
			if(targetId == null){
				json.addProperty("result", "0");
				json.addProperty("msg", "没有提供要删除记录的记录ID");
				return new StreamingResolution("text/charset=UTF-8;",json.toString());
			}
			vBean.deleteVehicle(Integer.parseInt(targetId), context.getUser());
			json.addProperty("result", "1");
			json.addProperty("msg", "删除成功");
			return new StreamingResolution("text/charset=UTF-8;",json.toString());
		}catch(Exception e){
			e.printStackTrace();
			json.addProperty("result", "0");
			json.addProperty("msg", "删除出错。可能车辆已被其它数据引用。");
			return new StreamingResolution("text/charset=UTF-8;",json.toString());
		}
	}
	
	@HandlesEvent(value="throwVehicle")
	@Secure(roles = Roles.VEHICLE_PROFILE_EDIT)
	public Resolution throwVehicle(){
		JsonObject json = new JsonObject();
		try{
			String vid = context.getRequest().getParameter("vid");
			String dateval = context.getRequest().getParameter("dateval");
			if(vid == null || dateval == null){
				json.addProperty("result", "0");
				json.addProperty("msg", "没有提供ID或日期");
				return new StreamingResolution("text/charset=UTF-8;",json.toString());
			}
			Date dateVal = HRUtil.parseDate(dateval, "yyyy-MM-dd");
			vBean.throwVehicle(vid, dateVal,context.getUser());
			json.addProperty("result", "1");
			json.addProperty("msg", "完成报废");
			return new StreamingResolution("text/charset=UTF-8;",json.toString());
		}catch(Exception e){
			e.printStackTrace();
			json.addProperty("result", "0");
			json.addProperty("msg", "报废出错.请联系管理员."+e.getMessage());
			return new StreamingResolution("text/charset=UTF-8;",json.toString());
		}
	}
	
	@HandlesEvent(value="vehicleDetail")
	@Secure(roles=Roles.VEHICLE_PROFILE_VIEW)
	public Resolution vehicleDetail(){
		try{
			String targetId = context.getRequest().getParameter("targetId");
			if(targetId == null)
				return context.errorResolution("查找车辆档案出错", "没有提供合适的车辆档案ID");
			vBasic = vBean.getVehicleBasicInfoByVehicleId(Integer.parseInt(targetId));
			if(vBasic.getVid() == null){
				profile = vBean.getVehicleProfileById(Integer.parseInt(targetId));
				vBasic.setVid(profile);
			}else{
				profile = vBasic.getVid();
			}
			maintenances = vBean.getVehicleCheckByTypeMaintennance(profile.getId());
			repairs = vBean.getVehicleCheckByTypeRepaire(profile.getId());
			fullchecks = vBean.getVehicleCheckByTypeFullCheck(profile.getId());
			annul = vBean.getVehicleCheckByTypeAnnul(profile.getId());
			extras = vBean.getVehicleCheckByTypeExtras(profile.getId());
			return new ForwardResolution("/vehicle/vehicleBasicInfo.jsp");
		}catch(Exception e){
			e.printStackTrace();
			return new StreamingResolution("text/charset=UTF-8;","数据获取出错."+e.getMessage());
		}
	}
	
	@HandlesEvent(value="saveVehicleBasicInfo")
	@Secure(roles=Roles.VEHICLE_PROFILE_EDIT)
	public Resolution saveVehicleBasicInfo(){
		JsonObject json = new JsonObject();
		try{
			if(vBasic == null || vBasic.getVid().getId() == null){
				json.addProperty("result", "0");
				json.addProperty("msg", "没有提供合适的档案ID");
			}
			vBean.saveVehicleBasicInfo(vBasic, context.getUser());
			json.addProperty("result", "1");
			json.addProperty("msg", "修改成功！");
		}catch(Exception e){
			e.printStackTrace();
			json.addProperty("result", "0");
			json.addProperty("msg", "修改出错。错误信息:"+e.getMessage());
		}
		return new StreamingResolution("text/charset=utf-8",json.toString());
	}
	
	@HandlesEvent(value="vehicleTechDetail")
	@Secure(roles=Roles.VEHICLE_PROFILE_VIEW)
	public Resolution vehicleTechDetail(){
		try{
			String targetId = context.getRequest().getParameter("targetId");
			if(targetId == null)
				return context.errorResolution("查找车辆档案出错", "没有提供合适的车辆档案ID");
			vtech = vBean.getVehicleTechnicalDetailByVehicleId(Integer.parseInt(targetId));
			if(vtech.getVid() == null){
				profile = vBean.getVehicleProfileById(Integer.parseInt(targetId));
//				System.out.println("No records found for tech document FOR vid:"+profile.getVid());
				vtech.setVid(profile);
			}else{
				profile = vtech.getVid();
			}
			vtech.setStringToList(); //设置逗号分隔的字符串到list列表
			return new ForwardResolution("/vehicle/vehicleTech.jsp");
		}catch(Exception e){
			return new StreamingResolution("text/charset=UTF-8;","数据获取出错."+e.getMessage());
		}
	}
	
	@HandlesEvent(value="saveVehicleTechDetail")
	@Secure(roles=Roles.VEHICLE_PROFILE_EDIT)
	public Resolution saveVehicleTechDetail(){
		JsonObject json = new JsonObject();
		try{
			if(vtech == null || vtech.getVid().getId() == null){
				json.addProperty("result", "0");
				json.addProperty("msg", "没有提供合适的档案ID");
			}
			vBean.saveVehicleTechDetail(vtech, context.getUser());
			json.addProperty("result", "1");
			json.addProperty("msg", "修改成功！");
		}catch(Exception e){
			e.printStackTrace();
			json.addProperty("result", "0");
			json.addProperty("msg", "修改出错。错误信息:"+e.getMessage());
		}
		return new StreamingResolution("text/charset=utf-8",json.toString());
	}
	

	@HandlesEvent(value="addVehicleCheck")
	@Secure(roles=Roles.VEHICLE_FILE_CHECK)
	public Resolution addVehicleCheck(){
		try{
			System.out.println("正在添加");
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
				System.out.println("Check file is not null . saving");
				String filename = "车辆_" + check.getCtype() + "_" +vptemp.getId() + HRUtil.getFileExtension(checkFile.getFileName());
				String ipath = context.getLocalFileLocation() + "车辆/"+check.getCtype() + "/"+filename;
				File file = new File(ipath);
				checkFile.save(file);
				System.out.println("Save file to "+ file.getAbsolutePath());
				
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
			e.printStackTrace();
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
			e.printStackTrace();
			return defaultAction();
		}
	}
	
	@HandlesEvent(value="deleteVechileCheck")
	@Secure(roles=Roles.VEHICLE_FILE_CHECK)
	public Resolution deleteVechileCheck(){
		JsonObject json = new JsonObject();
		try{
			String checkId = context.getRequest().getParameter("checkId");
			if(checkId == null){
				json.addProperty("result", "0");
				json.addProperty("msg", "没有ID可用，请按提示删除。");
				return new StreamingResolution("text/charset=utf-8;",json.toString());
			}
			vBean.removeVehicleCheck(checkId);
			json.addProperty("result", "1");
			json.addProperty("msg", "删除成功.");
			return new StreamingResolution("text/charset=utf-8;",json.toString());
		}catch(Exception e){
			e.printStackTrace();
			json.addProperty("result", "0");
			json.addProperty("msg", "没有ID可用，请联系管理员。"+e.getMessage());
			return new StreamingResolution("text/charset=utf-8;",json.toString());
		}
	}
	
	@HandlesEvent(value="vehicleRepairRecord")
	@Secure(roles=Roles.VEHICLE_PROFILE_VIEW)
	public Resolution vehicleRepairRecord(){
		try{
			String targetId = context.getRequest().getParameter("targetId");
			if(targetId == null)
				return context.errorResolution("查找车辆档案出错", "没有提供合适的车辆档案ID");
			profile = vBean.getVehicleProfileById(Integer.parseInt(targetId));
			vRepairList = vBean.getVehicleRepairRecordsByTimeAndProfileId(Integer.parseInt(targetId), date1,date2);
			if(vRepairList == null){
				vRepairList = new ArrayList<VehicleRepairRecord>();
			}
			return new ForwardResolution("/vehicle/vehicleRepairRecords.jsp");
		}catch(Exception e){
			e.printStackTrace();
			return new StreamingResolution("text/charset=UTF-8;","数据获取出错."+e.getMessage());
		}
	}
	
	
	@HandlesEvent(value="addNewRepairRecord")
	@Secure(roles=Roles.VEHICLE_PROFILE_EDIT)
	public Resolution addNewRepairRecord(){
		JsonObject json = new JsonObject();
		try{
			if(vRepair == null){
				json.addProperty("result", "0");
				json.addProperty("msg", "没有提交有效的维修资料");
			}else{
				vBean.addNewRepairRecord(vRepair, context.getUser());
				json.addProperty("result", "1");
				json.addProperty("msg", "添加成功");
			}
		}catch(Exception e){
			e.printStackTrace();
			json.addProperty("result", "0");
			json.addProperty("msg", "创建失败，请联系管理员.");
		}
		return new StreamingResolution("text/charset=UTF-8;",json.toString());
	}
	
	@HandlesEvent(value="editVehicleRepairRecord")
	@Secure(roles=Roles.VEHICLE_PROFILE_EDIT)
	public Resolution editVehicleRepairRecord(){
		JsonObject json = new JsonObject();
		try{
			vBean.editVehicleRepairRecord(vRepair, context.getUser());
			vRepair = new VehicleRepairRecord();
			json.addProperty("result", "1");
			json.addProperty("msg", "修改成功");
		}catch(Exception e){
			e.printStackTrace();
			json.addProperty("result", "0");
			json.addProperty("msg", "创建失败，请联系管理员."+e.getMessage());
		}
		return new StreamingResolution("text/charset=UTF-8;",json.toString());
	}
	
	@HandlesEvent(value="deleteVehicleRepairRecord")
	@Secure(roles=Roles.VEHICLE_PROFILE_EDIT)
	public Resolution deleteVehicleRepairRecord(){
		JsonObject json = new JsonObject();
		try{
			if(selectList != null){
				for(String s:selectList){
					if(s != null)
						vBean.removeVehicleRepairRecord(s,context.getUser());
				}
			}
			json.addProperty("result", "1");
			json.addProperty("msg", "删除成功");
		}catch(Exception e){
			e.printStackTrace();
			json.addProperty("result", "0");
			json.addProperty("msg", "删除失败."+e.getMessage());
		}
		return new StreamingResolution("text/charset=UTF-8;",json.toString());
	}
	
	@HandlesEvent(value="vehiclePartsRepair")
	@Secure(roles=Roles.VEHICLE_PROFILE_VIEW)
	public Resolution vehiclePartsRepair(){
		try{
			String targetId = context.getRequest().getParameter("targetId");
			if(targetId == null)
				return context.errorResolution("查找车辆档案出错", "没有提供合适的车辆档案ID");
			profile = vBean.getVehicleProfileById(Integer.parseInt(targetId));
			vPartsList = vBean.getVehiclePartsRepairByTimeAndProfileId(Integer.parseInt(targetId), date1,date2);
			if(vPartsList == null){
				vPartsList = new ArrayList<VehiclePartsRepair>();
			}
			return new ForwardResolution("/vehicle/vehiclePartsRepair.jsp");
		}catch(Exception e){
			e.printStackTrace();
			return new StreamingResolution("text/charset=UTF-8;","数据获取出错."+e.getMessage());
		}
	}
	
	@HandlesEvent(value="saveVehiclePartsRepair")
	@Secure(roles=Roles.VEHICLE_PROFILE_EDIT)
	public Resolution saveVehiclePartsRepair(){
		JsonObject json = new JsonObject();
		try{
			if(vParts == null){
				json.addProperty("result", "0");
				json.addProperty("msg", "没有提交有效的更换资料");
			}else{
				vBean.addVehiclePartsRepair(vParts, context.getUser());
				json.addProperty("result", "1");
				json.addProperty("msg", "添加成功");
			}
		}catch(Exception e){
			e.printStackTrace();
			json.addProperty("result", "0");
			json.addProperty("msg", "创建失败，请联系管理员.");
		}
		return new StreamingResolution("text/charset=UTF-8;",json.toString());
	}
	
	@HandlesEvent(value="editVehiclePartsRepair")
	@Secure(roles=Roles.VEHICLE_PROFILE_EDIT)
	public Resolution editVehiclePartsRepair(){
		JsonObject json = new JsonObject();
		try{
			vBean.editVehiclePartsRepair(vParts, context.getUser());
			vParts = new VehiclePartsRepair();
			json.addProperty("result", "1");
			json.addProperty("msg", "修改成功");
		}catch(Exception e){
			e.printStackTrace();
			json.addProperty("result", "0");
			json.addProperty("msg", "创建失败，请联系管理员."+e.getMessage());
		}
		return new StreamingResolution("text/charset=UTF-8;",json.toString());
	}
	
	@HandlesEvent(value="deleteVehiclePartsRepair")
	@Secure(roles=Roles.VEHICLE_PROFILE_EDIT)
	public Resolution deleteVehiclePartsRepair(){
		JsonObject json = new JsonObject();
		try{
			if(selectList != null){
				for(String s:selectList){
					if(s != null)
						vBean.removeVehiclePartsRepair(s,context.getUser());
				}
			}
			json.addProperty("result", "1");
			json.addProperty("msg", "删除成功");
		}catch(Exception e){
			e.printStackTrace();
			json.addProperty("result", "0");
			json.addProperty("msg", "删除失败."+e.getMessage());
		}
		return new StreamingResolution("text/charset=UTF-8;",json.toString());
	}
	
	@HandlesEvent(value="vehicleChange")
	@Secure(roles=Roles.VEHICLE_PROFILE_VIEW)
	public Resolution vehicleChange(){
		try{
			String targetId = context.getRequest().getParameter("targetId");
			if(targetId == null)
				return context.errorResolution("查找车辆档案出错", "没有提供合适的车辆档案ID");
			profile = vBean.getVehicleProfileById(Integer.parseInt(targetId));
			vChangeList = vBean.getVehicleChangeByTimeAndProfileId(Integer.parseInt(targetId), date1,date2);
			if(vChangeList == null){
				vChangeList = new ArrayList<VehicleChange>();
			}
			return new ForwardResolution("/vehicle/vehicleChange.jsp");
		}catch(Exception e){
			e.printStackTrace();
			return new StreamingResolution("text/charset=UTF-8;","数据获取出错."+e.getMessage());
		}
	}
	
	@HandlesEvent(value="addVehicleChange")
	@Secure(roles=Roles.VEHICLE_PROFILE_EDIT)
	public Resolution addVehicleChange(){
		JsonObject json = new JsonObject();
		try{
			if(vChange == null){
				json.addProperty("result", "0");
				json.addProperty("msg", "没有提交有效的资料");
			}else{
				vBean.addVehicleChange(vChange, context.getUser());
				json.addProperty("result", "1");
				json.addProperty("msg", "添加成功");
			}
		}catch(Exception e){
			e.printStackTrace();
			json.addProperty("result", "0");
			json.addProperty("msg", "创建失败，请联系管理员.");
		}
		return new StreamingResolution("text/charset=UTF-8;",json.toString());
	}
	
	@HandlesEvent(value="editVehicleChange")
	@Secure(roles=Roles.VEHICLE_PROFILE_EDIT)
	public Resolution editVehicleChange(){
		JsonObject json = new JsonObject();
		try{
			vBean.editVehicleChange(vChange, context.getUser());
			vChange = new VehicleChange();
			json.addProperty("result", "1");
			json.addProperty("msg", "修改成功");
		}catch(Exception e){
			e.printStackTrace();
			json.addProperty("result", "0");
			json.addProperty("msg", "创建失败，请联系管理员."+e.getMessage());
		}
		return new StreamingResolution("text/charset=UTF-8;",json.toString());
	}
	
	@HandlesEvent(value="deleteVehicleChange")
	@Secure(roles=Roles.VEHICLE_PROFILE_EDIT)
	public Resolution deleteVehicleChange(){
		JsonObject json = new JsonObject();
		try{
			if(selectList != null){
				for(String id:selectList){
					if(id != null)
						vBean.removeVehicleChange(id,context.getUser());
				}
			}
			json.addProperty("result", "1");
			json.addProperty("msg", "删除成功");
		}catch(Exception e){
			e.printStackTrace();
			json.addProperty("result", "0");
			json.addProperty("msg", "删除失败."+e.getMessage());
		}
		return new StreamingResolution("text/charset=UTF-8;",json.toString());
	}
	
	@HandlesEvent(value="vehicleLevel")
	@Secure(roles=Roles.VEHICLE_PROFILE_VIEW)
	public Resolution vehicleLevel(){
		try{
			String targetId = context.getRequest().getParameter("targetId");
			if(targetId == null)
				return context.errorResolution("查找车辆档案出错", "没有提供合适的车辆档案ID");
			profile = vBean.getVehicleProfileById(Integer.parseInt(targetId));
			vLevelList = vBean.getVehicleLevelCheckByTimeAndProfileId(Integer.parseInt(targetId), date1,date2);
			if(vLevelList == null){
				vLevelList = new ArrayList<VehicleLevel>();
			}
			return new ForwardResolution("/vehicle/vehicleLevel.jsp");
		}catch(Exception e){
			e.printStackTrace();
			return new StreamingResolution("text/charset=UTF-8;","数据获取出错."+e.getMessage());
		}
	}
	
	@HandlesEvent(value="addVehicleLevelCheck")
	@Secure(roles=Roles.VEHICLE_PROFILE_EDIT)
	public Resolution addVehicleLevelCheck(){
		JsonObject json = new JsonObject();
		try{
			if(vLevel == null){
				json.addProperty("result", "0");
				json.addProperty("msg", "没有提交有效的资料");
			}else{
				vBean.addVehicleLevelCheck(vLevel, context.getUser());
				json.addProperty("result", "1");
				json.addProperty("msg", "添加成功");
			}
		}catch(Exception e){
			e.printStackTrace();
			json.addProperty("result", "0");
			json.addProperty("msg", "创建失败，请联系管理员.");
		}
		return new StreamingResolution("text/charset=UTF-8;",json.toString());
	}
	
	@HandlesEvent(value="editVehicleLevel")
	@Secure(roles=Roles.VEHICLE_PROFILE_EDIT)
	public Resolution editVehicleLevel(){
		JsonObject json = new JsonObject();
		try{
			vBean.editVehicleLevel(vLevel, context.getUser());
			vLevel = new VehicleLevel();
			json.addProperty("result", "1");
			json.addProperty("msg", "修改成功");
		}catch(Exception e){
			e.printStackTrace();
			json.addProperty("result", "0");
			json.addProperty("msg", "创建失败，请联系管理员."+e.getMessage());
		}
		return new StreamingResolution("text/charset=UTF-8;",json.toString());
	}
	
	@HandlesEvent(value="deleteVehicleLevel")
	@Secure(roles=Roles.VEHICLE_PROFILE_EDIT)
	public Resolution deleteVehicleLevel(){
		JsonObject json = new JsonObject();
		try{
			if(selectList != null){
				for(String s:selectList){
					if(s != null)
						vBean.removeVehicleLevel(s,context.getUser());
				}
			}
			json.addProperty("result", "1");
			json.addProperty("msg", "删除成功");
		}catch(Exception e){
			e.printStackTrace();
			json.addProperty("result", "0");
			json.addProperty("msg", "删除失败."+e.getMessage());
		}
		return new StreamingResolution("text/charset=UTF-8;",json.toString());
	}
	
	@HandlesEvent(value="vehicleUseRecord")
	@Secure(roles=Roles.VEHICLE_PROFILE_VIEW)
	public Resolution vehicleUseRecord(){
		try{
			String targetId = context.getRequest().getParameter("targetId");
			if(targetId == null)
				return context.errorResolution("查找车辆档案出错", "没有提供合适的车辆档案ID");
			profile = vBean.getVehicleProfileById(Integer.parseInt(targetId));
			vUseRecordList = vBean.getVehicleUseRecordByTimeAndProfileId(Integer.parseInt(targetId), date1,date2);
			if(vUseRecordList == null){
				vUseRecordList = new ArrayList<VehicleUseRecord>();
			}
			return new ForwardResolution("/vehicle/vehicleUseRecord.jsp");
		}catch(Exception e){
			e.printStackTrace();
			return new StreamingResolution("text/charset=UTF-8;","数据获取出错."+e.getMessage());
		}
	}
	
	@HandlesEvent(value="addVehicleUseRecord")
	@Secure(roles=Roles.VEHICLE_PROFILE_EDIT)
	public Resolution addVehicleUseRecord(){
		JsonObject json = new JsonObject();
		try{
			if(vUseRecord == null){
				json.addProperty("result", "0");
				json.addProperty("msg", "没有提交有效的资料");
			}else{
				vBean.addVehicleUseRecord(vUseRecord, context.getUser());
				json.addProperty("result", "1");
				json.addProperty("msg", "添加成功");
			}
		}catch(Exception e){
			e.printStackTrace();
			json.addProperty("result", "0");
			json.addProperty("msg", "创建失败，请联系管理员.");
		}
		return new StreamingResolution("text/charset=UTF-8;",json.toString());
	}
	
	@HandlesEvent(value="editVehicleUseRecord")
	@Secure(roles=Roles.VEHICLE_PROFILE_EDIT)
	public Resolution editVehicleUseRecord(){
		JsonObject json = new JsonObject();
		try{
			vBean.editVehicleUseRecord(vUseRecord, context.getUser());
			vUseRecord = new VehicleUseRecord();
			json.addProperty("result", "1");
			json.addProperty("msg", "修改成功");
		}catch(Exception e){
			e.printStackTrace();
			json.addProperty("result", "0");
			json.addProperty("msg", "创建失败，请联系管理员."+e.getMessage());
		}
		return new StreamingResolution("text/charset=UTF-8;",json.toString());
	}
	
	@HandlesEvent(value="deleteVehicleUseRecord")
	@Secure(roles=Roles.VEHICLE_PROFILE_EDIT)
	public Resolution deleteVehicleUseRecord(){
		JsonObject json = new JsonObject();
		try{
			if(selectList != null){
				for(String s:selectList){
					if(s != null)
						vBean.removeVehicleUseRecord(s,context.getUser());
				}
			}
			json.addProperty("result", "1");
			json.addProperty("msg", "删除成功");
		}catch(Exception e){
			e.printStackTrace();
			json.addProperty("result", "0");
			json.addProperty("msg", "删除失败."+e.getMessage());
		}
		return new StreamingResolution("text/charset=UTF-8;",json.toString());
	}
	
	@HandlesEvent(value="vehicleAccident")
	@Secure(roles=Roles.VEHICLE_PROFILE_VIEW)
	public Resolution vehicleAccident(){
		try{
			String targetId = context.getRequest().getParameter("targetId");
			if(targetId == null)
				return context.errorResolution("查找车辆档案出错", "没有提供合适的车辆档案ID");
			profile = vBean.getVehicleProfileById(Integer.parseInt(targetId));
			vAccidentList = vBean.getVehicleAccidentByTimeAndProfileId(Integer.parseInt(targetId), date1,date2);
			if(vAccidentList == null){
				vAccidentList = new ArrayList<VehicleAccident>();
			}
			return new ForwardResolution("/vehicle/vehicleAccident.jsp");
		}catch(Exception e){
			e.printStackTrace();
			return new StreamingResolution("text/charset=UTF-8;","数据获取出错."+e.getMessage());
		}
	}
	
	@HandlesEvent(value="addVehicleAccident")
	@Secure(roles=Roles.VEHICLE_PROFILE_EDIT)
	public Resolution addVehicleAccident(){
		JsonObject json = new JsonObject();
		try{
			if(vAccident == null){
				json.addProperty("result", "0");
				json.addProperty("msg", "没有提交有效的资料");
			}else{
				vBean.addVehicleAccident(vAccident, context.getUser());
				json.addProperty("result", "1");
				json.addProperty("msg", "添加成功");
			}
		}catch(Exception e){
			e.printStackTrace();
			json.addProperty("result", "0");
			json.addProperty("msg", "创建失败，请联系管理员.");
		}
		return new StreamingResolution("text/charset=UTF-8;",json.toString());
	}
	
	@HandlesEvent(value="editVehicleAccident")
	@Secure(roles=Roles.VEHICLE_PROFILE_EDIT)
	public Resolution editVehicleAccident(){
		JsonObject json = new JsonObject();
		try{
			vBean.editVehicleAccident(vAccident, context.getUser());
			vAccident = new VehicleAccident();
			json.addProperty("result", "1");
			json.addProperty("msg", "修改成功");
		}catch(Exception e){
			e.printStackTrace();
			json.addProperty("result", "0");
			json.addProperty("msg", "创建失败，请联系管理员."+e.getMessage());
		}
		return new StreamingResolution("text/charset=UTF-8;",json.toString());
	}
	
	@HandlesEvent(value="deleteVehicleAccident")
	@Secure(roles=Roles.VEHICLE_PROFILE_EDIT)
	public Resolution deleteVehicleAccident(){
		JsonObject json = new JsonObject();
		try{
			if(selectList != null){
				for(String id:selectList){
					if(id != null)
						vBean.removeVehicleAccident(id,context.getUser());
				}
			}
			json.addProperty("result", "1");
			json.addProperty("msg", "删除成功");
		}catch(Exception e){
			e.printStackTrace();
			json.addProperty("result", "0");
			json.addProperty("msg", "删除失败."+e.getMessage());
		}
		return new StreamingResolution("text/charset=UTF-8;",json.toString());
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

	public VehicleSelector getSelector() {
		return selector;
	}

	public void setSelector(VehicleSelector selector) {
		this.selector = selector;
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

	public List<VehicleTeam> getTeams() {
		return teams;
	}

	public void setTeams(List<VehicleTeam> teams) {
		this.teams = teams;
	}

	public List<VehicleLane> getLanes() {
		return lanes;
	}

	public void setLanes(List<VehicleLane> lanes) {
		this.lanes = lanes;
	}
	public VehicleTechnicalDetail getVtech() {
		return vtech;
	}
	public void setVtech(VehicleTechnicalDetail vtech) {
		this.vtech = vtech;
	}
	public VehicleBasicInfo getvBasic() {
		return vBasic;
	}
	public void setvBasic(VehicleBasicInfo vBasic) {
		this.vBasic = vBasic;
	}
	public List<VehicleCheck> getMaintenances() {
		return maintenances;
	}
	public void setMaintenances(List<VehicleCheck> maintenances) {
		this.maintenances = maintenances;
	}
	public List<VehicleCheck> getRepairs() {
		return repairs;
	}
	public void setRepairs(List<VehicleCheck> repairs) {
		this.repairs = repairs;
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
	public VehicleCheck getCheck() {
		return check;
	}
	public void setCheck(VehicleCheck check) {
		this.check = check;
	}
	public VehicleCheck getEditCheck() {
		return editCheck;
	}
	public void setEditCheck(VehicleCheck editCheck) {
		this.editCheck = editCheck;
	}
	public FileBean getCheckFile() {
		return checkFile;
	}
	public void setCheckFile(FileBean checkFile) {
		this.checkFile = checkFile;
	}
	public VehicleRepairRecord getvRepair() {
		return vRepair;
	}
	public void setvRepair(VehicleRepairRecord vRepair) {
		this.vRepair = vRepair;
	}
	public List<VehicleRepairRecord> getvRepairList() {
		return vRepairList;
	}
	public void setvRepairList(List<VehicleRepairRecord> vRepairList) {
		this.vRepairList = vRepairList;
	}
	public Date getDate1() {
		return date1;
	}
	public void setDate1(Date date1) {
		this.date1 = date1;
	}
	public Date getDate2() {
		return date2;
	}
	public void setDate2(Date date2) {
		this.date2 = date2;
	}
	public VehiclePartsRepair getvParts() {
		return vParts;
	}
	public void setvParts(VehiclePartsRepair vParts) {
		this.vParts = vParts;
	}
	public List<VehiclePartsRepair> getvPartsList() {
		return vPartsList;
	}
	public void setvPartsList(List<VehiclePartsRepair> vPartsList) {
		this.vPartsList = vPartsList;
	}
	public VehicleLevel getvLevel() {
		return vLevel;
	}
	public void setvLevel(VehicleLevel vLevel) {
		this.vLevel = vLevel;
	}
	public List<VehicleLevel> getvLevelList() {
		return vLevelList;
	}
	public void setvLevelList(List<VehicleLevel> vLevelList) {
		this.vLevelList = vLevelList;
	}
	public VehicleChange getvChange() {
		return vChange;
	}
	public void setvChange(VehicleChange vChange) {
		this.vChange = vChange;
	}
	public List<VehicleChange> getvChangeList() {
		return vChangeList;
	}
	public void setvChangeList(List<VehicleChange> vChangeList) {
		this.vChangeList = vChangeList;
	}
	public VehicleUseRecord getvUseRecord() {
		return vUseRecord;
	}
	public void setvUseRecord(VehicleUseRecord vUseRecord) {
		this.vUseRecord = vUseRecord;
	}
	public VehicleAccident getvAccident() {
		return vAccident;
	}
	public void setvAccident(VehicleAccident vAccident) {
		this.vAccident = vAccident;
	}
	public List<VehicleUseRecord> getvUseRecordList() {
		return vUseRecordList;
	}
	public void setvUseRecordList(List<VehicleUseRecord> vUseRecordList) {
		this.vUseRecordList = vUseRecordList;
	}
	public List<VehicleAccident> getvAccidentList() {
		return vAccidentList;
	}
	public void setvAccidentList(List<VehicleAccident> vAccidentList) {
		this.vAccidentList = vAccidentList;
	}
	public List<String> getSelectList() {
		return selectList;
	}
	public void setSelectList(List<String> selectList) {
		this.selectList = selectList;
	}
	
	
}
