package com.bus.stripes.actionbean;

import com.bus.services.CustomActionBean;

import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.action.UrlBinding;

@UrlBinding(value="/actionbean/Index.action")
public class IndexActionBean extends CustomActionBean{

	@DefaultHandler
	public Resolution defaultAction(){
		return new ForwardResolution("/index.jsp");
	}
}
