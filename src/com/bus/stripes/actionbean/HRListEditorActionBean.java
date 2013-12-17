package com.bus.stripes.actionbean;

import java.util.ArrayList;
import java.util.List;

import security.action.Secure;

import com.bus.dto.Account;
import com.bus.dto.Fixoptions;
import com.bus.dto.Qualification;
import com.bus.dto.Ethnic;
import com.bus.dto.Workertype;
import com.bus.services.CustomActionBean;
import com.bus.services.HRBean;
import com.bus.util.Roles;
import com.bus.util.SelectBoxOption;
import com.bus.util.SelectBoxOptions;

import net.sourceforge.stripes.action.ActionBean;
import net.sourceforge.stripes.action.ActionBeanContext;
import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.HandlesEvent;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.integration.spring.SpringBean;

public class HRListEditorActionBean extends CustomActionBean implements Permission{
	private HRBean bean;
	@SpringBean
	protected void setBean(HRBean bean){this.bean = bean;}
	protected HRBean getBean(){return this.bean;}
	
	private List<Fixoptions> fixoptions;
	private int optionlistid;
	private String optionlistnewdata;
	private String optionlistselectedvalue;
	private List<SelectBoxOption> workertypes;
	private List<Qualification> qualifications; 
	
	private List<Ethnic> ethnics; //民族
	
	private void loadOptionLists(){
		try{
			setFixoptions(bean.getAllFixOptions());
			setWorkertypes(SelectBoxOptions.getWorkerType(bean.getWorkertypeList()));
			setQualifications(bean.getQualificationList());
			setEthnics(bean.getEthnicList());
		}catch(Exception e){
			setFixoptions(new ArrayList<Fixoptions>());
		}
	}
	
	@DefaultHandler
	@Secure(roles=Roles.EMPLOYEE_PROPERTY_LIST_VIEW)
	public Resolution defaultAction(){
		loadOptionLists();
		return new ForwardResolution("/hr/hrlist.jsp").addParameter("optionlistnewdata", getOptionlistnewdata());
	}
	
	
	
	@HandlesEvent(value="createnewproperty")
	@Secure(roles=Roles.EMPLOYEE_PROPERTY_LIST_ADD)
	public Resolution createnewproperty(){
		try{
			setOptionlistid(Integer.parseInt(context.getRequest().getParameter("optionlistid")));
			if(optionlistid == 0)
				return defaultAction();
			if(optionlistnewdata == null || optionlistnewdata.equals("") || optionlistnewdata.indexOf(",")!= -1)
				return defaultAction();
			Fixoptions data = bean.getOptionListById(optionlistid);
			String newdata = data.getContent() + ","+ optionlistnewdata;
			data.setContent(newdata);
			bean.saveFixOptions(data);
			setOptionlistnewdata("");
			return defaultAction();
		}catch(Exception e){
			System.out.println(e.getMessage());
			return defaultAction();
		}
		
	}
	
	@HandlesEvent(value="createnewworkertype")
	@Secure(roles=Roles.EMPLOYEE_PROPERTY_LIST_ADD)
	public Resolution createnewworkertype(){
		try{
			if(optionlistnewdata == null || optionlistnewdata.equals("") || optionlistnewdata.indexOf(",")!= -1)
				return defaultAction();
			Workertype wt = new Workertype();
			wt.setName(optionlistnewdata);
			bean.saveWorkertype(wt);
			setOptionlistnewdata("");
			return defaultAction();
		}catch(Exception e){
			System.out.println(e.getMessage());
			return defaultAction();
		}
	}
	
	@HandlesEvent(value="deleteworkertype")
	@Secure(roles=Roles.EMPLOYEE_PROPERTY_LIST_RM)
	public Resolution deleteworkertype(){
		try{
			bean.deleteWorkerTypeByName(optionlistselectedvalue);
			return defaultAction();
		}catch(Exception e){
			System.out.println(e.getMessage());
			return defaultAction();
		}
	}
	
	@HandlesEvent(value="deletenewproperty")
	@Secure(roles=Roles.EMPLOYEE_PROPERTY_LIST_RM)
	public Resolution deletenewproperty(){
		try{
			setOptionlistid(Integer.parseInt(context.getRequest().getParameter("optionlistid")));
			bean.deleteFixoptionContent(optionlistid,optionlistselectedvalue);
			return defaultAction();
		}catch(Exception e){
			System.out.println(e.getMessage());
			return defaultAction();
		}
	}
	
	@HandlesEvent(value="createQualification")
	@Secure(roles=Roles.EMPLOYEE_PROPERTY_LIST_ADD)
	public Resolution createQualification(){
		try{
			if(optionlistnewdata == null || optionlistnewdata.equals("") || optionlistnewdata.indexOf(",")!= -1)
				return defaultAction();
			Qualification q = new Qualification();
			q.setName(optionlistnewdata);
			bean.saveQualification(q);
			setOptionlistnewdata("");
			return defaultAction();
		}catch(Exception e){
			System.out.println(e.getMessage());
			return defaultAction();
		}
	}
	
	@HandlesEvent(value="deleteQualification")
	@Secure(roles=Roles.EMPLOYEE_PROPERTY_LIST_RM)
	public Resolution deleteQualification(){
		try{
			bean.deleteQualification(optionlistselectedvalue);
			return defaultAction();
		}catch(Exception e){
			System.out.println(e.getMessage());
			return defaultAction();
		}
	}
	
	
	
	@HandlesEvent(value="createEthnic")
	@Secure(roles=Roles.EMPLOYEE_PROPERTY_LIST_ADD)
	public Resolution createEthnic(){
		try{
			if(optionlistnewdata == null || optionlistnewdata.equals("") || optionlistnewdata.indexOf(",")!= -1)
				return defaultAction();
			Ethnic et = new Ethnic();
			et.setName(optionlistnewdata);
			bean.saveEthnic(et);
			setOptionlistnewdata("");
			return defaultAction();
		}catch(Exception e){
			System.out.println(e.getMessage());
			return defaultAction();
		}
	}
	
	@HandlesEvent(value="deleteEthnic")
	@Secure(roles=Roles.EMPLOYEE_PROPERTY_LIST_RM)
	public Resolution deleteEthnic(){
		try{
			bean.deleteEthnic(optionlistselectedvalue);
			return defaultAction();
		}catch(Exception e){
			System.out.println(e.getMessage());
			return defaultAction();
		}
	}
	
	
	
	public int getOptionlistid() {
		return optionlistid;
	}
	public void setOptionlistid(int optionlistid) {
		this.optionlistid = optionlistid;
	}
	public String getOptionlistnewdata() {
		return optionlistnewdata;
	}
	public void setOptionlistnewdata(String optionlistnewdata) {
		this.optionlistnewdata = optionlistnewdata;
	}
	public List<Fixoptions> getFixoptions() {
		return fixoptions;
	}
	public void setFixoptions(List<Fixoptions> fixoptions) {
		this.fixoptions = fixoptions;
	}
	public String getOptionlistselectedvalue() {
		return optionlistselectedvalue;
	}
	public void setOptionlistselectedvalue(String optionlistselectedvalue) {
		this.optionlistselectedvalue = optionlistselectedvalue;
	}
	public List<SelectBoxOption> getWorkertypes() {
		return workertypes;
	}
	public void setWorkertypes(List<SelectBoxOption> workertypes) {
		this.workertypes = workertypes;
	}
	public List<Qualification> getQualifications() {
		return qualifications;
	}
	public void setQualifications(List<Qualification> qualifications) {
		this.qualifications = qualifications;
	}
	
	public List<Ethnic> getEthnics() {
		return ethnics;
	}
	public void setEthnics(List<Ethnic> ethnics) {
		this.ethnics = ethnics;
	}
}
