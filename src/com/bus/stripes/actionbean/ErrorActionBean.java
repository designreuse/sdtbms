package com.bus.stripes.actionbean;

import net.sourceforge.stripes.action.ActionBean;
import net.sourceforge.stripes.action.ActionBeanContext;
import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.Resolution;

public class ErrorActionBean implements ActionBean{

	private MyActionBeanContext context;
	public MyActionBeanContext getContext() { return context; }
	public void setContext(ActionBeanContext context) { this.context = (MyActionBeanContext)context; }
	

	private String error;
	private String description;
	
	public String getError() {
		return error;
	}
	public void setError(String error) {
		this.error = error;
	}

	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}

	@DefaultHandler
	public Resolution defaultAction(){
		if(error == null)
			error = "Unknow Error";
		if(description == null)
			description = "description not set";
		return new ForwardResolution("/error.jsp");
	}
}
