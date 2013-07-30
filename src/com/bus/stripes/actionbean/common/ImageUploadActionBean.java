package com.bus.stripes.actionbean.common;

import java.io.File;
import java.util.Date;

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

import com.bus.dto.Contract;
import com.bus.dto.Employee;
import com.bus.dto.Hrimage;
import com.bus.dto.Idmanagement;
import com.bus.dto.common.ContractImg;
import com.bus.services.CommonBean;
import com.bus.services.CustomActionBean;
import com.bus.services.HRBean;
import com.bus.util.HRUtil;
import com.bus.util.Roles;

@UrlBinding(value="/actionbean/ImageUpload.action")
public class ImageUploadActionBean extends CustomActionBean{

	private FileBean fileContractImg;
	private FileBean fileIdCardImg;
	private FileBean fileProfilePic;
	
	private CommonBean commonBean;
	private HRBean hrBean;
	
	@DefaultHandler
	public Resolution defaultAction(){
		return new StreamingResolution("text/charset=utf8;","没用可用的默认页面。不允许直接访问。");
	}
	
	@HandlesEvent(value="profilePicUpload")
	@Secure(roles=Roles.EMPLOYEE_PROFILEPIC_UPLOAD)
	public Resolution profilePicUpload(){
		String ipath = "";
		try{
			File oldFile = null;
			Hrimage img = new Hrimage();
			String targetId = context.getRequest().getParameter("targetId");
			String link = context.getRequest().getParameter("returnLink");
			if(fileProfilePic != null){
				Employee e = hrBean.getEmployeeById(targetId);
				if(e.getImage() != null){
					img = e.getImage();
					oldFile = new File(img.getIpath());
					if(oldFile.exists())
						oldFile.delete();
				}
				String filename = e.getWorkerid()+".jpg";
				ipath = context.getLocalFileLocation()+"profilepic/"+filename;
				File f = new File(ipath);
				fileProfilePic.save(f);
				img.setName(filename);
				img.setIpath(ipath);
				e.setImage(img);
				commonBean.saveProfilePic(e);
			}
			return new RedirectResolution(link);
		}catch(Exception e){
			e.printStackTrace();
			File file = new File(ipath);
			if(file.exists())
				file.delete();
			return defaultAction();
		}
	}
	
	@HandlesEvent(value="contractImage")
	@Secure(roles=Roles.EMPLOYEE_CONTRACT_FILE_UPLOAD)
	public Resolution contractImage(){
		String ipath = "";
		try{
			File oldFile = null;
			ContractImg img = new ContractImg();
			String contractId = context.getRequest().getParameter("contractId");
			String link = context.getRequest().getParameter("returnLink");
			if(fileContractImg != null){
				Contract c  = hrBean.getContractById(Integer.parseInt(contractId));
				if(c.getImage()!= null){
					img = c.getImage();
					oldFile = new File(img.getIpath());
					if(oldFile.exists())
						oldFile.delete();
				}
				String filename = c.getEmployee().getFullname() + "_合同_"+c.getId()+HRUtil.getFileExtension(fileContractImg.getFileName());
				ipath = context.getLocalFileLocation()+"合同/"+filename;
				File f  = new File(ipath);
				fileContractImg.save(f);
				img.setContract(c);
				img.setIpath(ipath);
				img.setName(filename);
				img.setUploaddate(new Date());
				commonBean.saveContractImg(img);
			}
			return new RedirectResolution(link);
		}catch(Exception e){
			e.printStackTrace();
			File file = new File(ipath);
			if(file.exists())
				file.delete();
			return defaultAction();
		}
	}

	@HandlesEvent(value="idCardImage")
	@Secure(roles=Roles.EMPLOYEE_IDCARDS_FILE_UPLOAD)
	public Resolution idCardImage(){
		String ipath = "";
		try{
			Hrimage img = new Hrimage();
			String oldFile = null;
			String cardId = context.getRequest().getParameter("cardId");
			String link = context.getRequest().getParameter("returnLink");
			Idmanagement card = hrBean.getIdCardById(Integer.parseInt(cardId));
			if(card.getImage() != null){
				img = card.getImage();
				oldFile = img.getIpath();
				if(oldFile != null){
					File of = new File(oldFile);
					if(of.exists())
						of.delete();
				}
			}
			if(fileIdCardImg != null){
				String filename = card.getEmployee().getFullname()+"_"+card.getType()+"_"+card.getId()+HRUtil.getFileExtension(fileIdCardImg.getFileName());
				ipath = context.getLocalFileLocation()+card.getType()+"/"+filename;
				File nf = new File(ipath);
				fileIdCardImg.save(nf);
				
				img.setIpath(ipath);
				img.setName(filename);
				card.setImage(img);
				hrBean.saveIdCardImage(card);
			}
			return new RedirectResolution(link);
		}catch(Exception e){
			e.printStackTrace();
			File file = new File(ipath);
			if(file.exists())
				file.delete();
			return defaultAction();
		}
	}
	
	public FileBean getFileContractImg() {
		return fileContractImg;
	}

	public void setFileContractImg(FileBean fileContractImg) {
		this.fileContractImg = fileContractImg;
	}

	public CommonBean getCommonBean() {
		return commonBean;
	}
	@SpringBean
	public void setCommonBean(CommonBean commonBean) {
		this.commonBean = commonBean;
	}

	public HRBean getHrBean() {
		return hrBean;
	}
	@SpringBean
	public void setHrBean(HRBean hrBean) {
		this.hrBean = hrBean;
	}

	public FileBean getFileIdCardImg() {
		return fileIdCardImg;
	}

	public void setFileIdCardImg(FileBean fileIdCardImg) {
		this.fileIdCardImg = fileIdCardImg;
	}

	public FileBean getFileProfilePic() {
		return fileProfilePic;
	}

	public void setFileProfilePic(FileBean fileProfilePic) {
		this.fileProfilePic = fileProfilePic;
	}
}
