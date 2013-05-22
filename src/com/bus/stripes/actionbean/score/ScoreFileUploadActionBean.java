package com.bus.stripes.actionbean.score;

import java.io.FileInputStream;

import com.bus.services.CustomActionBean;
import com.bus.services.HRBean;
import com.bus.services.ScoreBean;
import com.bus.util.ScoreExcelFileProcessor;

import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.FileBean;
import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.HandlesEvent;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.action.UrlBinding;
import net.sourceforge.stripes.integration.spring.SpringBean;

@UrlBinding(value="/actionbean/Scorefile.action")
public class ScoreFileUploadActionBean extends CustomActionBean{
	
	private FileBean itemsfile;
	private FileBean scorefile;
	
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
		return new ForwardResolution("/score/batchitems.jsp");
	}

	@HandlesEvent(value="itemsupload")
	public Resolution itemsupload(){
		if(!getPermission(context.getUser(), "scorefileupload_uploaditems")){
			return context.errorResolution("权限错误","你没有权限进行该操作,请联系管理员");
		}
		try{
			if(itemsfile != null){
				ScoreExcelFileProcessor saver  = new ScoreExcelFileProcessor(((FileInputStream)itemsfile.getInputStream()));
				String str = saver.saveItems(scoreBean,context.getUser());
				if(!str.equals("")){
					return new ForwardResolution("/actionbean/Error.action").addParameter("error", "<span style='color:red;'>出错:某些条例导入失败</span>")
							.addParameter("description", "这些条例没有被上传:<br/>\n" + str);
				}
			}
			
		}catch(Exception e){
			e.printStackTrace();
		}
		return defaultAction();
	}
	
	@HandlesEvent(value="scoreupload")
	public Resolution scoreupload(){
		if(!getPermission(context.getUser(), "scorefileupload_uploadscores")){
			return context.errorResolution("权限错误","你没有权限进行该操作,请联系管理员");
		}
		try{
			if(scorefile != null){
				ScoreExcelFileProcessor saver  = new ScoreExcelFileProcessor(((FileInputStream)scorefile.getInputStream()));
				String str = saver.saveScores(hrBean,scoreBean,context.getUser());
				if(!str.equals("")){
					return new ForwardResolution("/actionbean/Error.action").addParameter("error", "<span style='color:red;'>出错:某些积分导入失败</span>")
							.addParameter("description", "这些积分没有被上传:<br/>\n" + str);
				}
			}
			
		}catch(Exception e){
			e.printStackTrace();
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
	
	
}
