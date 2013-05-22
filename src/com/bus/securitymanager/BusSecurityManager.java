package com.bus.securitymanager;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.stripes.action.ActionBeanContext;
import security.controller.StripesSecurityManager;

public class BusSecurityManager implements StripesSecurityManager{

	@Override
	public boolean isUserInRole(List<String> arg0, ActionBeanContext arg1) {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isUserInRole(List<String> arg0, HttpServletRequest arg1,
			HttpServletResponse arg2) {
		// TODO Auto-generated method stub
		return true;
	}

}
