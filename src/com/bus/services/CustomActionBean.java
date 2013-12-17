package com.bus.services;

import security.controller.StripesSecurityFilter;

import com.bus.dto.Account;
import com.bus.dto.Accountgroup;
import com.bus.dto.Actiongroup;
import com.bus.stripes.actionbean.MyActionBeanContext;
import com.bus.stripes.actionbean.Permission;

import net.sourceforge.stripes.action.ActionBean;
import net.sourceforge.stripes.action.ActionBeanContext;
import net.sourceforge.stripes.integration.spring.SpringBean;

public class CustomActionBean implements ActionBean,Permission{

	protected final static boolean D = true;
	protected MyActionBeanContext context;
	public MyActionBeanContext getContext() { return context; }
	public void setContext(ActionBeanContext context) { this.context = (MyActionBeanContext)context; }
	
	protected AccountBean accBean;
	public AccountBean getAccBean() {return accBean;}
	@SpringBean
	public void setAccBean(AccountBean bean) {this.accBean = bean;}

	@Override
	public boolean allowToAccess(Account user, String beanstr) {
		try{
			Account a = context.getUser();
			if(a == null)
				return false;
			a = accBean.getAccountById(a.getId()+"");
			for(Accountgroup ag:a.getGroups()){
				if(ag.getGroups().getName().equals("administrator"))
					return true;
				for(Actiongroup actiong:ag.getGroups().getActions()){
					if(actiong.getAction().getName().indexOf(beanstr) == 0)
						return true;
				}
			}
			return false;
		}catch(Exception e){
			return false;
		}
	}
	
	public void __log(String str){
		if(D) System.out.println(str);
	}
	
	public static boolean isD() {
		return D;
	}
	
	
}
