package com.bus.stripes.actionbean.application;


import java.util.ArrayList;
import java.util.List;

import security.action.Secure;

import com.bus.dto.application.ApplicationIdCards;
import com.bus.services.CustomActionBean;
import com.bus.services.EmpApplicationBean;
import com.bus.util.Roles;

import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.HandlesEvent;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.action.StreamingResolution;
import net.sourceforge.stripes.action.UrlBinding;
import net.sourceforge.stripes.integration.spring.SpringBean;

@UrlBinding(value="/actionbean/AppIDCards.action")
public class EmploymentIdCardActionBean extends CustomActionBean{
	
	private EmpApplicationBean empBean;
	public EmpApplicationBean getEmpApplicationBean(){return this.empBean;}
	@SpringBean
	public void setEmpApplicationBean(EmpApplicationBean bean){this.empBean = bean;}
	
	private List<ApplicationIdCards> idcards;
	private ApplicationIdCards card;
	
	@DefaultHandler
	@Secure(roles=Roles.EMPLOYMENT_IDCARD)
	public Resolution defaultAction(){
		try {
			idcards = empBean.getApplicationIDCards();
		} catch (Exception e) {
			idcards = new ArrayList<ApplicationIdCards>();
		}
		return new ForwardResolution("/employment/appidcards.jsp");
	}
	
	@HandlesEvent(value="createIdCard")
	@Secure(roles=Roles.EMPLOYMENT_IDCARD)
	public Resolution createIdCard(){
		try{
			empBean.createIdCard(card);
			return defaultAction();
		}catch(Exception e){
			return defaultAction();
		}
	}
	
	@HandlesEvent(value="deleteIdCard")
	@Secure(roles=Roles.EMPLOYMENT_IDCARD)
	public Resolution deleteIdCard(){
		try{
			String targetId = context.getRequest().getParameter("targetId");
			empBean.deleteIdCard(targetId);
			return new StreamingResolution("text/charset=utf8;","删除成功 ");
		}catch(Exception e){
			return new StreamingResolution("text/charset=utf8;","删除失败."+e.getMessage());
		}
	}
	
	
	
	public List<ApplicationIdCards> getIdcards() {
		return idcards;
	}
	public void setIdcards(List<ApplicationIdCards> idcards) {
		this.idcards = idcards;
	}
	public ApplicationIdCards getCard() {
		return card;
	}
	public void setCard(ApplicationIdCards card) {
		this.card = card;
	}
}
