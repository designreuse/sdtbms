package com.bus.stripes.actionbean;

import com.bus.dto.Account;

public interface Permission {

	public boolean allowToAccess(Account user,String beanstr);
	public boolean getPermission(String action);
	
	public boolean getScoreSystemPermission();
}
