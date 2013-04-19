package com.bus.stripes.actionbean;

import java.util.ArrayList;
import java.util.List;

import com.bus.dto.Employee;
import com.bus.dto.Idmanagement;
import com.bus.services.HRBean;
import com.bus.util.SelectBoxOption;
import com.bus.util.SelectBoxOptions;

import net.sourceforge.stripes.action.ActionBean;
import net.sourceforge.stripes.action.ActionBeanContext;
import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.HandlesEvent;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.integration.spring.SpringBean;

public class IDCardsActionBean implements ActionBean{
	
	private MyActionBeanContext context;
	public MyActionBeanContext getContext() { return context; }
	public void setContext(ActionBeanContext context) { this.context = (MyActionBeanContext)context; }
	
	private HRBean bean;
	
	private List<Idmanagement> idcards = new ArrayList<Idmanagement>();
	private List<SelectBoxOption> typeoptions = SelectBoxOptions.getIdcardType();
	private Idmanagement idcard;
	
	private String targetId;
	
	@DefaultHandler
	public Resolution defaultAction(){
		setTargetId(context.getRequest().getParameter("targetId"));
		if(targetId != null)
			setIdcards(bean.getIdcardsByEmployeeId(targetId));
		return new ForwardResolution("/hr/idcards.jsp");
	}
	
	@HandlesEvent(value="create")
	public Resolution create(){
		if(idcard == null)
			return defaultAction();
		Employee e = new Employee();
		e.setId(Integer.parseInt(targetId));
		idcard.setEmployee(e);
		bean.saveIdcard(idcard);
		idcard = new Idmanagement();
		return defaultAction();
	}
	
	@HandlesEvent(value="delete")
	public Resolution delete(){
		bean.deleteIdcard(idcard.getId()+"");
		return defaultAction();
	}
	
	public Idmanagement getIdcard() {
		return idcard;
	}
	public void setIdcard(Idmanagement idcard) {
		this.idcard = idcard;
	}

	public List<Idmanagement> getIdcards() {
		return idcards;
	}
	public void setIdcards(List<Idmanagement> idcards) {
		this.idcards = idcards;
	}
	public List<SelectBoxOption> getTypeoptions() {
		return typeoptions;
	}
	public void setTypeoptions(List<SelectBoxOption> typeoptions) {
		this.typeoptions = typeoptions;
	}
	public String getTargetId() {
		return targetId;
	}
	public void setTargetId(String targetId) {
		this.targetId = targetId;
	}
	
	public HRBean getBean() {
		return bean;
	}
	
	@SpringBean
	public void setBean(HRBean bean) {
		this.bean = bean;
	}
}
