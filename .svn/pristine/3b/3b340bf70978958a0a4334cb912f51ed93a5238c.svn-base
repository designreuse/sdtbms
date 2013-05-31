package com.bus.stripes.actionbean.score;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import security.action.Secure;

import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.HandlesEvent;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.action.StreamingResolution;
import net.sourceforge.stripes.action.UrlBinding;
import net.sourceforge.stripes.integration.spring.SpringBean;

import com.bus.dto.score.Scoremember;
import com.bus.dto.score.Voucherlist;
import com.bus.services.CustomActionBean;
import com.bus.services.ScoreBean;
import com.bus.util.Roles;

@UrlBinding(value="/actionbean/Voucher.action")
public class VoucherActionBean extends CustomActionBean{

	private ScoreBean scoreBean;
	public ScoreBean getScoreBean(){return this.scoreBean;}
	@SpringBean
	public void setScoreBean(ScoreBean bean){this.scoreBean = bean;}

	private String workerid;
	private Long totalscore;
	private Integer quantity;
	private String filtername;
	private String filterworkerid;
	private List<Voucherlist> voucherlist;
	
	@DefaultHandler
	@Secure(roles = Roles.SCORE_VOUCHER_VIEW)
	public Resolution defaultAction(){
		return new ForwardResolution("/score/voucher.jsp");
	}
	
	@HandlesEvent(value="getVouchers")
	public Resolution getVouchers(){
		try{
			if(filterworkerid != null){
				voucherlist = scoreBean.getVouchersByWorkerId(filterworkerid);
			}else if(filtername != null){
				voucherlist = scoreBean.getVouchersByName(filtername);
			}else{
				voucherlist = new ArrayList<Voucherlist>();
			}
		}catch(Exception e){
			System.out.println(e);
			voucherlist = new ArrayList<Voucherlist>();
		}
		return defaultAction();
	}
	
	@HandlesEvent(value="giveVouchers")
	@Secure(roles = Roles.SCORE_VOUCHER_MANAGEMENT)
	public Resolution giveVouchers(){
		try{
			if(workerid == null || workerid.trim() == "" || totalscore == null || quantity == null){
				throw new Exception("Not enough detail provided");
			}
			Voucherlist vl = new Voucherlist();
			Scoremember member = scoreBean.getScoreMemberByWorkerid(workerid);
			vl.setScoremember(member);
			vl.setQuantity(quantity);
			vl.setScore(totalscore);
			vl.setDate(Calendar.getInstance().getTime());
			scoreBean.giveVouchers(context.getUser(),vl);
		}catch(Exception e){
			return context.errorResolution("输入信息出错", "请确保输入的信息正确无误");
		}
		return getVouchers();
	}
	
	@HandlesEvent(value="deleteVoucher")
	@Secure(roles = Roles.SCORE_VOUCHER_MANAGEMENT)
	public Resolution deleteVoucher(){
		try{
			String targetId = context.getRequest().getParameter("targetId");
			if(targetId != null){
				scoreBean.deleteVoucherRecord(context.getUser(),Integer.parseInt(targetId));
				return new StreamingResolution("text;charset=utf-8;","删除成功");
			}
			return new StreamingResolution("text;charset=utf-8;","删除失败");
		}catch(Exception e){
			System.out.println(e);
			return new StreamingResolution("text;charset=utf-8;","删除失败");
		}
	}
	
	public String getWorkerid() {
		return workerid;
	}
	public void setWorkerid(String workerid) {
		this.workerid = workerid;
	}
	public Long getTotalscore() {
		return totalscore;
	}
	public void setTotalscore(Long totalscore) {
		this.totalscore = totalscore;
	}
	public Integer getQuantity() {
		return quantity;
	}
	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}
	public String getFiltername() {
		return filtername;
	}
	public void setFiltername(String filtername) {
		this.filtername = filtername;
	}
	public String getFilterworkerid() {
		return filterworkerid;
	}
	public void setFilterworkerid(String filterworkerid) {
		this.filterworkerid = filterworkerid;
	}
	public List<Voucherlist> getVoucherlist() {
		return voucherlist;
	}
	public void setVoucherlist(List<Voucherlist> voucherlist) {
		this.voucherlist = voucherlist;
	}
}
