package com.bus.stripes.actionbean;

import java.util.HashMap;

import com.bus.dto.Account;

import net.sourceforge.stripes.action.ActionBeanContext;
import net.sourceforge.stripes.util.CryptoUtil;

public class MyActionBeanContext extends ActionBeanContext {

    public void setAccount(Account acc) {
        getRequest().getSession().setAttribute("account", acc);
    }

    public Account getUser() {
        return (Account) getRequest().getSession().getAttribute("account");
    }
    
    public void removeUser(){
    	getRequest().getSession().removeAttribute("account");
    }
    
    public void setPermission(HashMap<String,Integer> permissions){
    	getRequest().getSession().setAttribute("permissions", permissions);
    }
    
    public HashMap<String,Integer> getPermission(){
    	return (HashMap<String,Integer>)(getRequest().getSession().getAttribute("permissions"));
    }
}