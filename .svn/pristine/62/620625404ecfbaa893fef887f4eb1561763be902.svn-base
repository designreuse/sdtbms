package com.bus.stripes.actionbean.application;

import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import security.action.Secure;

import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.HandlesEvent;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.action.StreamingResolution;
import net.sourceforge.stripes.action.UrlBinding;
import net.sourceforge.stripes.integration.spring.SpringBean;

import com.bus.dto.application.DrivingExam;
import com.bus.dto.application.HRApplication;
import com.bus.services.CustomActionBean;
import com.bus.services.EmpApplicationBean;
import com.bus.stripes.selector.DriverExamSelector;
import com.bus.util.HRUtil;
import com.bus.util.Roles;

@UrlBinding(value="/actionbean/EmpDriverExam.action")
public class EmpDriverExamActionBean extends CustomActionBean{

	private EmpApplicationBean empBean;
	public EmpApplicationBean getEmpApplicationBean(){return this.empBean;}
	@SpringBean
	public void setEmpApplicationBean(EmpApplicationBean bean){this.empBean = bean;}
	
	private List<DrivingExam> drivingexams;
	private DriverExamSelector selector;
	private String eRR;
	
	private int pagenum;
	private int lotsize;
	private Long totalcount;
	private int pagecount;
	
	public static final String ZHUANG_EXAM = "zhuang";
	public static final String ROAD_EXAM = "road";
	public static final String REMARK = "remark";
	
	private void initData(){
		if(pagenum <= 0 || lotsize <= 0){
			pagenum = 1;
			lotsize = 20;
		}
		setTotalcount(0L);
		getRequestsFromSelector();
		setPagecount((int) (totalcount/lotsize + 1));
		if(pagenum > totalcount)
			pagenum = Integer.parseInt(totalcount.toString());
	}
	
	private void getRequestsFromSelector() {
		try{
			if(selector == null){
				HashMap<String,Object> map = (HashMap<String, Object>) empBean.getDriverExams(pagenum, lotsize);
				setDrivingexams((List<DrivingExam>) map.get("list"));
				setTotalcount((Long) map.get("count"));
			}else if(selector != null){
				String statement = selector.getSelectorStatement();
				System.out.println(statement);
				HashMap<String,Object> map = (HashMap<String, Object>) empBean.getDriverExams(pagenum, lotsize,statement);
				setDrivingexams((List<DrivingExam>) map.get("list"));
				setTotalcount((Long) map.get("count"));
			}
		}catch(Exception e){
			eRR = e.getMessage();
			setDrivingexams(new ArrayList<DrivingExam>());
			setTotalcount(0L);
			setPagecount(0);
		}
	}
	
	@DefaultHandler
	@Secure(roles=Roles.EMPLOYMENT_DRIVER_EXAM)
	public Resolution defaultAction(){
		initData();
		return new ForwardResolution("/employment/driverexam.jsp");
	}
	
	@HandlesEvent(value="updateRequest")
	@Secure(roles=Roles.EMPLOYMENT_DRIVER_EXAM_EDIT)
	public Resolution updateRequest(){
		try{
			Map map = HRUtil.parseRequestToMap(context.getRequest().getQueryString());
			String value = (String) map.get("value");
			String update  = context.getRequest().getParameter("update");
			String targetId = context.getRequest().getParameter("targetId");
			DrivingExam de = empBean.getDriverExamById(targetId);
			if(update.equals(ZHUANG_EXAM)){
				de.setZhuangPass(Integer.parseInt(value));
			}else if(update.equals(ROAD_EXAM)){
				de.setRoadPass(Integer.parseInt(value));
			}else if(update.equals(REMARK)){
				value = URLDecoder.decode(value,"UTF-8");
				de.setRemark(value);
			}
			empBean.mergeDriverExam(de);
			empBean.updateAppExamStatus(de);
			return new StreamingResolution("text/charset=utf8;","修改成功.");
		}catch(Exception e){
			e.printStackTrace();
			return new StreamingResolution("text/charset=utf8;","修改失败."+e.getMessage());
		}
	}
	
	@HandlesEvent(value="deleteInstance")
	@Secure(roles=Roles.EMPLOYMENT_DRIVER_EXAM_EDIT)
	public Resolution deleteInstance(){
		try{
			String targetId = context.getRequest().getParameter("targetId");
			empBean.removeDriverExam(targetId);
			return new StreamingResolution("text/charset=utf8;","删除成功.");
		}catch(Exception e){
			e.printStackTrace();
			return new StreamingResolution("text/charset=utf8;","修改失败."+e.getMessage());
		}
	}
	
	@HandlesEvent(value="prevpage")
	public Resolution prevpage(){
		setPagenum(getPagenum() - 1);
		return defaultAction();
	}
	
	@HandlesEvent(value="nextpage")
	public Resolution nextpage(){
		setPagenum(getPagenum() + 1);
		return defaultAction();
	}
	
	@HandlesEvent(value="filter")
	public Resolution filter(){
		return defaultAction();
	}
	
	public EmpApplicationBean getEmpBean() {
		return empBean;
	}
	public void setEmpBean(EmpApplicationBean empBean) {
		this.empBean = empBean;
	}
	public List<DrivingExam> getDrivingexams() {
		return drivingexams;
	}
	public void setDrivingexams(List<DrivingExam> drivingexams) {
		this.drivingexams = drivingexams;
	}
	public String geteRR() {
		return eRR;
	}
	public void seteRR(String eRR) {
		this.eRR = eRR;
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
	public int getPagecount() {
		return pagecount;
	}
	public void setPagecount(int pagecount) {
		this.pagecount = pagecount;
	}
	public DriverExamSelector getSelector() {
		return selector;
	}
	public void setSelector(DriverExamSelector selector) {
		this.selector = selector;
	}

}
