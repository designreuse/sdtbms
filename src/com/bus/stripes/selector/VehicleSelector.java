package com.bus.stripes.selector;

import java.util.Date;

import com.bus.util.HRUtil;

public class VehicleSelector implements BMSSelector{

	
	public Date getExpire1() {
		return expire1;
	}

	public void setExpire1(Date expire1) {
		this.expire1 = expire1;
	}

	public Date getExpire2() {
		return expire2;
	}

	public void setExpire2(Date expire2) {
		this.expire2 = expire2;
	}

	private String vid;
	private Integer throwed;
	private Date expire1;
	private Date expire2;
	
	@Override
	public String getSelectorStatement() {
		int set = 0;
		String query = "SELECT q FROM VehicleProfile q WHERE ";
		if(vid != null){
			query += "vid LIKE '%"+vid+"%'";
			set++;
		}
		if(throwed != null && throwed == 1){
			if(set >0)
				query += " AND";
			query += " status = 'E'";
			set++;
		}else if(throwed != null){
			if(set >0)
				query += " AND";
			query += " status = 'A'";
			set++;
		}
		if(expire1 != null || expire2 != null){
			if(set>0)
				query += " AND";
			if(expire1 != null && expire2 != null){
				query += " dateinvalidate <='"+HRUtil.parseDateToString(expire2)+"' AND dateinvalidate >='"+HRUtil.parseDateToString(expire1)+"'";
			}else if(expire2 != null){
				query += " dateinvalidate <='"+HRUtil.parseDateToString(expire2)+"'";
			}else if(expire1 != null){
				query += " dateinvalidate >='"+HRUtil.parseDateToString(expire1)+"'";
			}
		}
		query += " ORDER BY datepurchase DESC";
		return query;
	}

	public String getVid() {
		return vid;
	}

	public void setVid(String vid) {
		this.vid = vid;
	}

	public Integer getThrowed() {
		return throwed;
	}

	public void setThrowed(Integer throwed) {
		this.throwed = throwed;
	}

}
