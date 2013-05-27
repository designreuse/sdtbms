package com.bus.securitymanager;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.bus.dto.Account;
import com.bus.dto.Accountgroup;
import com.bus.dto.Actiongroup;
import com.bus.services.AccountBean;

import net.sourceforge.stripes.action.ActionBeanContext;
import net.sourceforge.stripes.integration.spring.SpringBean;
import security.controller.StripesSecurityManager;

public class BusSecurityManager implements StripesSecurityManager{

	
	protected AccountBean accBean;
	protected HttpServletRequest request;
	
	public BusSecurityManager(){
		accBean = new AccountBean();
	}
	
	/**
	 * Called from class annotation
	 */
	@Override
	public boolean isUserInRole(List<String> roles, ActionBeanContext context) {
		if (roles == null || roles.size() < 1)
			return false;
		request = context.getRequest();
		return checkRoles(roles);
	}

	/**
	 * Call from page roles tag
	 */
	@Override
	public boolean isUserInRole(List<String> roles, HttpServletRequest request,
			HttpServletResponse response) {
		if (roles == null || roles.size() < 1)
			return false;
		this.request = request;
		return checkRoles(roles);
	}
	
	/**
	 * Checks roels exist in the session roles for users, where the roles are saved during login
	 * @param roles
	 * @return
	 */
	private boolean checkRoles(List<String> roles) {
		@SuppressWarnings("unchecked")
		List<String> userroles = (List<String>) request.getSession().getAttribute("roles");
		if(userroles == null){
			System.out.println("User doesn't have any roles.");
			return false;
		}
		boolean roleok = false;

//		for(String r:userroles){
//			System.out.println("user has role:"+r);
//		}
//		
		for (String role : roles){
			roleok = false;
			for(String userrole : userroles){
				if(userrole.equals("administrator") || userrole.equals(role)){
					roleok = true;
					break;
				}
			}
			if(!roleok)
				return false;
		}
		return roleok;
	}
}
