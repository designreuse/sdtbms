package com.bus.interceptor;

import com.bus.stripes.actionbean.LoginActionBean;
import com.bus.stripes.actionbean.MyActionBeanContext;

import net.sourceforge.stripes.action.ActionBean;
import net.sourceforge.stripes.action.ActionBeanContext;
import net.sourceforge.stripes.action.RedirectResolution;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.controller.ExecutionContext;
import net.sourceforge.stripes.controller.Interceptor;
import net.sourceforge.stripes.controller.Intercepts;
import net.sourceforge.stripes.controller.LifecycleStage;

@Intercepts(LifecycleStage.HandlerResolution)
public class SecurityInterceptor implements Interceptor{

	@Override
	public Resolution intercept(ExecutionContext ctx) throws Exception {
		System.out.println("Checking account validity。。。。。");
		Resolution resolution = ctx.proceed();
		
//		if(isPermitted(ctx.getActionBean(),ctx.getActionBeanContext())){
//			return resolution;
//		}
		if(ctx.getActionBean() instanceof LoginActionBean){
			return resolution;
		}
		if(isLoggedIn(ctx.getActionBeanContext())){
			return resolution;
		}
		
		return new RedirectResolution("/actionbean/Login.action");
	}
	
	 /** Returns true if the user is logged in. */
    protected boolean isLoggedIn(ActionBeanContext ctx) { 
        return ((MyActionBeanContext) ctx).getUser() != null;
//    	System.out.println("Tntercepting with security");
//    	return true;
    }

    /** Returns true if the user is permitted to invoke the event requested. */
    protected boolean isPermitted(ActionBean bean, ActionBeanContext ctx) {
    	return true;
    }
}