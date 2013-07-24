package com.bus.stripes.actionbean.account;

import com.bus.dto.Account;
import com.bus.services.CustomActionBean;
import com.bus.services.HRBean;
import com.bus.stripes.typeconverter.PasswordTypeConverter;

import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.HandlesEvent;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.action.UrlBinding;
import net.sourceforge.stripes.integration.spring.SpringBean;
import net.sourceforge.stripes.validation.Validate;

@UrlBinding(value="/actionbean/ChangePasswd.action")
public class ChangePasswdActionBean extends CustomActionBean{

	private HRBean hrBean;
	public HRBean getHrBean() {return hrBean;}
	@SpringBean
	public void setHrBean(HRBean hrBean) {this.hrBean = hrBean;}
	
	private String username;
	private String errMsg;
	
	@Validate(converter=PasswordTypeConverter.class)
	private String oldpasswd;
	
	@Validate(converter=PasswordTypeConverter.class)
	private String newpasswd;
	
	
	
	@DefaultHandler
	public Resolution defaultAction(){
		if(context.getUser() != null)
			username = context.getUser().getUsername();
		return new ForwardResolution("/acc/changepasswd.jsp");
	}

	@HandlesEvent(value="changepasswd")
	public Resolution changepasswd(){
		try{
			Account acc = new Account();
			acc.setUsername(username);
			acc.setPassword(oldpasswd);
			Account acc2 = hrBean.login(acc);
			if(acc2.getId() == null)
				setErrMsg("旧密码不正确");
			else{
				acc2.setPassword(newpasswd);
				accBean.changePasswd(acc2);
				context.removeUser();
				return new ForwardResolution("/login.jsp");
			}
		}catch(Exception e){
			setErrMsg("出错."+e.getMessage());
		}
		return defaultAction();
	}
	
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getOldpasswd() {
		return oldpasswd;
	}
	public void setOldpasswd(String oldpasswd) {
		this.oldpasswd = oldpasswd;
	}
	public String getNewpasswd() {
		return newpasswd;
	}

	public void setNewpasswd(String newpasswd) {
		this.newpasswd = newpasswd;
	}
	public String getErrMsg() {
		return errMsg;
	}
	public void setErrMsg(String errMsg) {
		this.errMsg = errMsg;
	}

	
}
