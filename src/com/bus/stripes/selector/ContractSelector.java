package com.bus.stripes.selector;

import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.Date;

import com.bus.util.HRUtil;

public class ContractSelector implements BMSSelector{

	private String type = null;
	private int dateselector = -1; //0 nothing, 1 enddate, 2 activedateorder, 3 probation date
	private Date expireDate;
	
	private Date expireDate2;
	
	@Override
	public String getSelectorStatement() {
		String query = "SELECT q FROM Contract q WHERE status='A' ";
		try{
		
			if(type != null){
				query += " AND type = '" + type +"'";
			}
		if(dateselector != 0){
			if(dateselector == 1){
				if(!query.equals(""))
					query += " AND";
				if(expireDate == null && expireDate2 == null){
					query += " enddate > now() ORDER BY enddate ASC";
				}else if(expireDate == null){
					query += " enddate >= now() AND enddate <= '"+HRUtil.parseDateToString(expireDate2)+"' ORDER BY enddate ASC";
				}else if(expireDate2 == null){
					query += " enddate >= '"+HRUtil.parseDateToString(expireDate)+"' ORDER BY enddate ASC";
				}else if(expireDate.getTime() == expireDate2.getTime()){
					query += " enddate = '"+HRUtil.parseDateToString(expireDate2)+"'";
				}else{
					query += " enddate <= '"+HRUtil.parseDateToString(expireDate2)+"'" +
							" AND enddate >= '"+HRUtil.parseDateToString(expireDate)+"' ORDER BY enddate ASC";
				}
			}else if(dateselector == 2){
				query += " ORDER BY activedate DESC";
			}else if(dateselector == 3){
				query += " AND (startdate+probation) > now() ORDER BY (startdate+probation) ASC";
			}
		}
		return query;
		}catch(Exception e){
			e.printStackTrace();
			return query;
		}
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public int getDateselector() {
		return dateselector;
	}
	public void setDateselector(int dateselector) {
		this.dateselector = dateselector;
	}
	public Date getExpireDate() {
		return expireDate;
	}
	public void setExpireDate(Date expireDate) {
		this.expireDate = expireDate;
	}
	public Date getExpireDate2() {
		return expireDate2;
	}
	public void setExpireDate2(Date expireDate2) {
		this.expireDate2 = expireDate2;
	}
}
