package com.bus.stripes.actionbean;

import net.sourceforge.stripes.action.ActionBean;
import net.sourceforge.stripes.action.ActionBeanContext;
import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.HandlesEvent;
import net.sourceforge.stripes.action.RedirectResolution;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.action.UrlBinding;
import net.sourceforge.stripes.integration.spring.SpringBean;
import net.sourceforge.stripes.validation.SimpleError;
import net.sourceforge.stripes.validation.Validate;
import net.sourceforge.stripes.validation.ValidationErrors;
import net.sourceforge.stripes.validation.ValidationMethod;

import com.bus.dto.Account;
import com.bus.services.HRBean;
import com.bus.stripes.typeconverter.PasswordTypeConverter;

@UrlBinding("/actionbean/Login.action")
public class LoginActionBean implements ActionBean{

	private MyActionBeanContext context;
	public MyActionBeanContext getContext() { return context; }
	public void setContext(ActionBeanContext context) { this.context = (MyActionBeanContext)context; }
	
	private Account account;
	private HRBean bean;
	
	@Validate(converter=PasswordTypeConverter.class)
	private String password;
	
	@SpringBean
	protected void setBean(HRBean bean){
		this.bean = bean;
	}
	protected HRBean getBean(){
		return this.bean;
	}
	
	public Account getAccount() {
		return account;
	}
	public void setAccount(Account account) {
		this.account = account;
	}
	
	public String getPassword(){
		return this.password;
	}
	
	public void setPassword(String pass){
		this.password = pass;
	}
	
	@DefaultHandler
	public Resolution defaultAction(){
		account = new Account();
		account.setUsername("jianxing");
		account.setPassword("jianxing");
		return new ForwardResolution("/login.jsp");
	}
	
	@HandlesEvent(value="login")
	public Resolution login(){
		context.setAccount(account);
		return new RedirectResolution("/actionbean/Employee.action");
	}
	
	@HandlesEvent(value="logout")
	public Resolution logout(){
		context.removeUser();
		return defaultAction();
	}
	
	@ValidationMethod(on="login")
	public void avoidInvalidLogin(ValidationErrors errors){
		if(this.account == null){
			errors.add("account", new SimpleError("用户名和密码不能为空"));
			return;
		}else if(this.account.getPassword() == null){
			this.account.setPassword(password);
		}
		else{
			if (this.account.getUsername() == null
					|| this.account.getUsername().trim().equals("")) {
				errors.add("account.username", new SimpleError("用户名不能为空"));
				return;
			}
			if (this.account.getPassword() == null
					|| this.account.getPassword().trim().equals("")) {
				errors.add("account.password", new SimpleError("密码不能为空"));
				return;
			}
		}
		account = bean.login(account);
		if(account.getId() == null){
			errors.add("account.password", new SimpleError("用户名或密码错误"));
		}
	}
}