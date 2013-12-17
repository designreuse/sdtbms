package com.bus.stripes.actionbean.vehicle;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.channels.FileChannel;
import java.util.Date;

import security.action.Secure;

import com.bus.dto.Hrimage;
import com.bus.dto.vehicleprofile.VehicleFiles;
import com.bus.dto.vehicleprofile.VehicleProfile;
import com.bus.services.CommonBean;
import com.bus.services.CustomActionBean;
import com.bus.services.VehicleBean;
import com.bus.util.HRUtil;
import com.bus.util.Roles;
import com.bus.util.VehicleProfileExcelFile;

import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.FileBean;
import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.HandlesEvent;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.action.UrlBinding;
import net.sourceforge.stripes.integration.spring.SpringBean;

@UrlBinding(value="/actionbean/VehicleFunction.action")
public class VehicleFunctionActionBean extends CustomActionBean{
	private CommonBean commonBean;
	public CommonBean getCommonBean() {
		return commonBean;
	}
	@SpringBean
	public void setCommonBean(CommonBean commonBean) {
		this.commonBean = commonBean;
	}
	
	private VehicleBean vBean;
	public VehicleBean getvBean() {return vBean;}
	@SpringBean
	public void setvBean(VehicleBean vBean) {this.vBean = vBean;}
	
	private FileBean multipleVehicles;
	private String picFileName;
	
	@DefaultHandler
	@Secure(roles=Roles.ADMINISTRATOR)
	public Resolution defaultAction(){
		return new ForwardResolution("/vehicle/vfunction.jsp");
	}
	
	@HandlesEvent(value="sortProfilePic")
	@Secure(roles=Roles.ADMINISTRATOR)
	public Resolution sortProfilePic(){
		try{
			String rootStr  = context.getLocalFileLocation()+"vehicleprofilepic\\";
			if(picFileName != null)
				rootStr += picFileName;
			File root = new File(rootStr);
			if(!root.exists())
				throw new Exception("File not exist."+root.getAbsolutePath());
			if(!root.isDirectory())
				throw new Exception("File not directory."+root.getAbsolutePath());
			for(File subRoot : root.listFiles()){
				reNameFile(subRoot);
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return defaultAction();
	}
	
	private void reNameFile(File f){
		if(f.isDirectory()){
			for(File sub:f.listFiles())
				reNameFile(sub);
		}else{

			FileChannel source = null;
			FileChannel destination = null;
			
				Hrimage img = new Hrimage();
				String vid = f.getName().substring(0,f.getName().indexOf("."));
				if(vid.contains("(")){
					vid = vid.substring(0,vid.indexOf("("));
				}else if(vid.contains("（")){
					vid = vid.substring(0,vid.indexOf("（"));
				}
				VehicleProfile vp = vBean.getVehicleProfileLikeVid(vid, null);
				if(vp == null){
					if(HRUtil.removeNoneNumber(vid).length() < 3){
						String temvid = HRUtil.removeNoneNumber(vid);
						vid = vid.substring(0,vid.length()-temvid.length());
						while(temvid.length() < 3)
							temvid = "0"+temvid;
						vid = vid + temvid;
					}
					vp = vBean.getVehicleProfileBySelfId(vid);
					if(vp == null){
						System.out.println("File "+f.getAbsolutePath() + " vehicle not in database.");
						return;
					}
				}

				//Delete old file
				if(vp.getImage() != null){
					img = vp.getImage();
					File oldFile = new File(img.getIpath());
					if(oldFile.exists())
						oldFile.delete();
				}
				
				String filename = vp.getVid() + f.getName().substring(f.getName().indexOf("."));
				String ipath = context.getLocalFileLocation() + "vehicleprofilepic/"+filename;
				System.out.println("Saving pic:"+vid + " To pic:"+ipath);
				File profilePic = new File(ipath);
				VehicleFiles vf = new VehicleFiles();
				vf.setCreator(context.getUser());
				vf.setUdate(new Date());
				vf.setFilename(filename);
				vf.setIpath(ipath);
				
				try{
					source = new FileInputStream(f).getChannel();
					destination = new FileOutputStream(profilePic).getChannel();
					destination.transferFrom(source, 0, source.size());
					vf = vBean.saveVehicleFile(vf);
					
					img.setName(filename);
					img.setIpath(ipath);
					vp.setImage(img);
					
					commonBean.saveVehicleProfilePic(vp);
//					if(f.exists() && !f.getName().equals(filename))
//						f.deleteOnExit();
				}catch(Exception e){
					e.printStackTrace();
					if(profilePic.exists())
						profilePic.delete();
				}
				
		}
	}
	
	@HandlesEvent(value="importVehicleProfileExcelFiles")
	@Secure(roles=Roles.ADMINISTRATOR)
	public Resolution importVehicleProfileExcelFiles(){
		try{
			if(multipleVehicles == null)
				throw new Exception("No file provided.");
			if(!multipleVehicles.getFileName().contains(".xlsx"))
				throw new Exception("Not an excel 2007 file.");
			VehicleProfileExcelFile excelFile = new VehicleProfileExcelFile((FileInputStream)multipleVehicles.getInputStream());
			excelFile.importVehicles(vBean,context.getUser());
		}catch(Exception e){
			e.printStackTrace();
		}
		return defaultAction();
	}
	
	public FileBean getMultipleVehicles() {
		return multipleVehicles;
	}
	public void setMultipleVehicles(FileBean multipleVehicles) {
		this.multipleVehicles = multipleVehicles;
	}
	public String getPicFileName() {
		return picFileName;
	}
	public void setPicFileName(String picFileName) {
		this.picFileName = picFileName;
	}
	
	
}
