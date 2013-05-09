package com.bus.stripes.actionbean;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import net.sourceforge.stripes.action.ActionBean;
import net.sourceforge.stripes.action.ActionBeanContext;
import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.HandlesEvent;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.action.StreamingResolution;
import net.sourceforge.stripes.action.UrlBinding;
import net.sourceforge.stripes.integration.spring.SpringBean;

import com.bus.dto.Employee;
import com.bus.dto.Promoandtransfer;
import com.bus.services.HRBean;
import com.bus.stripes.selector.CoordinationSelector;
import com.bus.util.SelectBoxOption;
import com.bus.util.SelectBoxOptions;

@UrlBinding("/actionbean/HRCoordinator.action")
public class HRCoordinatorActionBean implements ActionBean{

	private MyActionBeanContext context;
	public MyActionBeanContext getContext() { return context; }
	public void setContext(ActionBeanContext context) { this.context = (MyActionBeanContext)context; }
	
	private HRBean bean;
	private List<Promoandtransfer> coordinates = new ArrayList<Promoandtransfer>();
	private Promoandtransfer coordinate;
	private List<SelectBoxOption> types;
	private List<SelectBoxOption> departments;
	private List<SelectBoxOption> positions;
	
	private CoordinationSelector selector;
	
	private int pagenum;
	private long totalcount;
	private int lotsize;
	private Long recordsTotal;
	
	private void getSelectBoxOptions(){
		departments = SelectBoxOptions.getDepartment(bean.getAllDepartment());
		positions = SelectBoxOptions.getPosition(bean.getAllPosition());
		types = SelectBoxOptions.getSelectBoxFromFixOptions(bean.getOptionListById(6));
	}
	
	public void initData(){
		getSelectBoxOptions();
		if(pagenum <= 0 || lotsize <= 0){
			pagenum = 1;
			lotsize = 20;
		}
		getCoordinatesBySelector();
		if(pagenum > totalcount)
			pagenum = (int) totalcount;
	}
	
	private void getCoordinatesBySelector() {
		if(selector == null){
			Map<String,Object> map=bean.getAllCoordinators(pagenum, lotsize); 
			coordinates = (List<Promoandtransfer>) map.get("list");
			Long count  = (Long) map.get("count");
			setRecordsTotal(count);
			setCoordinates(coordinates);
		}else{
			String statement  = selector.getSelectorStatement();
			System.out.println(statement);
			Map<String, Object> map = bean.getCoordinationBySelector(pagenum,lotsize,statement);
			setRecordsTotal(((Long)map.get("count")));
			setCoordinates((List<Promoandtransfer>)map.get("list"));
		}
		setTotalcount(getRecordsTotal()/lotsize +1);
	}
	@DefaultHandler
	public Resolution defaultAction(){
		initData();
		return new ForwardResolution("/hr/coordinator.jsp").addParameter("pagenum", pagenum);
	}
	
	@HandlesEvent(value="create")
	public Resolution create(){
		coordinate.setCreator(context.getUser());
		String ret = bean.saveCoordination(coordinate);
		return new StreamingResolution("text;charset=utf-8",ret);
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
	
	public HRBean getBean() {
		return bean;
	}
	@SpringBean
	public void setBean(HRBean bean) {
		this.bean = bean;
	}

	public List<Promoandtransfer> getCoordinates() {
		return coordinates;
	}
	public void setCoordinates(List<Promoandtransfer> coordinates) {
		this.coordinates = coordinates;
	}

	public Promoandtransfer getCoordinate() {
		return coordinate;
	}
	public void setCoordinate(Promoandtransfer coordinate) {
		this.coordinate = coordinate;
	}

	public List<SelectBoxOption> getTypes() {
		return types;
	}
	public void setTypes(List<SelectBoxOption> types) {
		this.types = types;
	}

	public List<SelectBoxOption> getDepartments() {
		return departments;
	}
	public void setDepartments(List<SelectBoxOption> departments) {
		this.departments = departments;
	}

	public List<SelectBoxOption> getPositions() {
		return positions;
	}
	public void setPositions(List<SelectBoxOption> positions) {
		this.positions = positions;
	}
	public int getPagenum() {
		return pagenum;
	}
	public void setPagenum(int pagenum) {
		this.pagenum = pagenum;
	}
	public Long getTotalcount() {
		return totalcount;
	}
	public void setTotalcount(Long totalcount) {
		this.totalcount = totalcount;
	}
	public int getLotsize() {
		return lotsize;
	}
	public void setLotsize(int lotsize) {
		this.lotsize = lotsize;
	}
	public CoordinationSelector getSelector() {
		return selector;
	}
	public void setSelector(CoordinationSelector selector) {
		this.selector = selector;
	}
	public Long getRecordsTotal() {
		return recordsTotal;
	}
	public void setRecordsTotal(Long recordsTotal) {
		this.recordsTotal = recordsTotal;
	}

	
	
	

}
