package com.bus.stripes.actionbean;

import java.util.List;
import java.util.Map;

import com.bus.dto.Account;
import com.bus.dto.Idmanagement;
import com.bus.services.CustomActionBean;
import com.bus.services.HRBean;
import com.bus.stripes.selector.IdmanagementSelector;
import com.bus.util.SelectBoxOption;
import com.bus.util.SelectBoxOptions;

import net.sourceforge.stripes.action.ActionBean;
import net.sourceforge.stripes.action.ActionBeanContext;
import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.HandlesEvent;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.integration.spring.SpringBean;

public class IdmanagementActionBean extends CustomActionBean implements Permission{

	private HRBean bean;
	private IdmanagementSelector selector;
	
	private List<Idmanagement> idmanagements;
	private List<SelectBoxOption> idcardtypes;
	
	private int pagenum;
	private int lotsize;
	private Long totalcount;
	
	@SpringBean
	protected void setBean(HRBean bean){
		this.bean = bean;
	}
	protected HRBean getBean(){
		return this.bean;
	}
	
	private void loadOptionList(){
		idcardtypes = SelectBoxOptions.getSelectBoxFromFixOptions(bean.getOptionListById(7));
	}
	
	private void initData(){
		loadOptionList();
		if(pagenum <= 0 || lotsize <= 0){
			pagenum = 1;
			lotsize = 20;
		}
		getIdsFromSelector();
		if(this.totalcount == null)
			setTotalcount(bean.countIdcards()/lotsize +1);
		if(pagenum > totalcount)
			pagenum = Integer.parseInt(totalcount.toString());
	}
	
	private void getIdsFromSelector() {
		Map<String, Object> map = null;
		if(selector == null)
			map = bean.getIdmanagements(pagenum, lotsize);
		else{
			String statement = selector.getSelectorStatement();
			try{
				map = bean.getIdcardsBySelector(pagenum,lotsize,statement);
				System.out.println("Using statement:"+statement);
			}catch(Exception e){
				e.printStackTrace();
				map = bean.getIdmanagements(pagenum, lotsize);
			}
		}
		Long count = (Long) map.get("count");
		if(count != null)
			setTotalcount(count/lotsize +1);
		idmanagements = (List<Idmanagement>) map.get("list");
	}
	
	@DefaultHandler
	public Resolution defaultAction(){
		initData();
		return new ForwardResolution("/hr/idmanagement.jsp").addParameter("pagenum", pagenum);
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
	
	@HandlesEvent(value="filter")
	public Resolution filter(){
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
	public List<Idmanagement> getIdmanagements() {
		return idmanagements;
	}
	public void setIdmanagements(List<Idmanagement> idmanagements) {
		this.idmanagements = idmanagements;
	}
	public List<SelectBoxOption> getIdcardtypes() {
		return idcardtypes;
	}
	public void setIdcardtypes(List<SelectBoxOption> idcardtypes) {
		this.idcardtypes = idcardtypes;
	}
	public IdmanagementSelector getSelector(){
		return this.selector;
	}
	public void setSelector(IdmanagementSelector selector){
		this.selector = selector;
	}

	
}
