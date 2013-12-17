package com.bus.stripes.actionbean.score;

import java.io.FileInputStream;

import security.action.Secure;

import com.bus.services.CustomActionBean;
import com.bus.services.HRBean;
import com.bus.services.ScoreBean;
import com.bus.util.ExcelScoreUpload;
import com.bus.util.HRUtil;
import com.bus.util.Roles;
import com.bus.util.ScoreExcelFileProcessor;
import com.google.gson.JsonObject;

import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.FileBean;
import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.HandlesEvent;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.action.StreamingResolution;
import net.sourceforge.stripes.action.UrlBinding;
import net.sourceforge.stripes.integration.spring.SpringBean;

@UrlBinding(value="/actionbean/Scorefile.action")
public class ScoreFileUploadActionBean extends CustomActionBean{
	
	private FileBean itemsfile;
	private FileBean scorefile;
	private Integer success = 0;
	private String msg = "";
	
	private ScoreBean scoreBean;
	public ScoreBean getScoreBean(){return this.scoreBean;}
	@SpringBean
	public void setScoreBean(ScoreBean bean){this.scoreBean = bean;}
	
	private HRBean hrBean;
	public HRBean getHrBean(){return hrBean;}
	@SpringBean
	public void setHrBean(HRBean bean){this.hrBean = bean;}
	
	
	@DefaultHandler
	public Resolution defaultAction(){
		if(D) System.out.println("Uploading score file 4");
		return new ForwardResolution("/score/batchitems.jsp");
	}

	@HandlesEvent(value="itemsupload")
	@Secure(roles=Roles.SCORE_ITEMS_FILE_UPLOAD)
	public Resolution itemsupload(){
		try{
			if(D) System.out.println("Uploading score file 3");
			if(itemsfile != null){
				ExcelScoreUpload saver = new ExcelScoreUpload();
				if(saver.isExcelFile2007(itemsfile.getFileName())){
					saver.init((FileInputStream)itemsfile.getInputStream(), true);
					saver.uploadScoreitems2007(scoreBean, context.getUser());
				}else if(saver.isExcelFile2003(itemsfile.getFileName())){
					saver.init((FileInputStream)itemsfile.getInputStream(), false);
					saver.uploadScoreitems2003(scoreBean, context.getUser());
				}else{
					__log("Not excel file");
					success = -1;
					msg = "不是Excel文件";
					return defaultAction();
				}
				success = 1;
				msg = "上传成功";
			}
		}catch(Exception e){
			e.printStackTrace();
			success = -1;
			msg = "上传出错"+e.getMessage();
			return context.errorResolution("上传出错", e.getMessage());
		}
		return defaultAction();
	}
	
	@HandlesEvent(value="scoreupload")
	@Secure(roles=Roles.SCORE_SCORES_FILE_UPLOAD)
	public Resolution scoreupload(){
		try{
			if(D) System.out.println("Uploading score file 2");
			if(scorefile != null){
				ExcelScoreUpload saver  = new ExcelScoreUpload();
				if(saver.isExcelFile2007(scorefile.getFileName())){
					__log("Is excel 2007 file");
					saver.init(((FileInputStream)scorefile.getInputStream()),true);
					saver.saveScore2007(hrBean,scoreBean,context.getUser());
				}else if(saver.isExcelFile2003(scorefile.getFileName())){
					__log("Is excel 2003 file");
					saver.init(((FileInputStream)scorefile.getInputStream()),false);
					saver.saveScore2003(hrBean,scoreBean,context.getUser());
				}else{
					__log("Not excel file");
					success = -1;
					msg = "不是Excel文件";
					return defaultAction();
				}
			}
			success = 1;
			msg = "上传成功";
		}catch(Exception e){
			e.printStackTrace();
			success = -1;
			msg = "上传出错"+e.getMessage();
			return context.errorResolution("上传出错", e.getMessage());
		}
		return defaultAction();
	}
	
	@HandlesEvent(value="driverScoreFileUpload")
	@Secure(roles = Roles.SCORE_SCORES_FILE_UPLOAD)
	public Resolution driverScoreFileUpload(){
		try{
			if(D) System.out.println("Uploading score file");
			if(scorefile != null){
				ExcelScoreUpload saver  = new ExcelScoreUpload();
				if(saver.isExcelFile2007(scorefile.getFileName())){
					saver.init(((FileInputStream)scorefile.getInputStream()),true);
					saver.saveDriverScore2007(hrBean,scoreBean,context.getUser());
					success = 1;
					msg = "上传成功";
				}else if(saver.isExcelFile2003(scorefile.getFileName())){
					saver.init(((FileInputStream)scorefile.getInputStream()),false);
					saver.saveDriverScore2003(hrBean,scoreBean,context.getUser());
					success = 1;
					msg = "上传成功";
				}else{
					System.out.println("Not excel file");
					success = -1;
					msg = "不是Excel文件";
				}
			}else{
				success = -1;
				msg = "没有文件上传";
			}
		}catch(Exception e){
			e.printStackTrace();
			success = -1;
			msg = "上传出错.<br/>"+e.getMessage();
			return context.errorResolution("上传出错", msg);
		}
		return defaultAction();
	}
	
	public FileBean getItemsfile() {
		return itemsfile;
	}

	public void setItemsfile(FileBean itemsfile) {
		this.itemsfile = itemsfile;
	}

	public FileBean getScorefile() {
		return scorefile;
	}

	public void setScorefile(FileBean scorefile) {
		this.scorefile = scorefile;
	}
	public Integer getSuccess() {
		return success;
	}
	public void setSuccess(Integer success) {
		this.success = success;
	}
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
	
	
}
