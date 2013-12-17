package com.bus.email;
import java.util.Date;

import com.bus.util.HRUtil;


public class BMSEmailContent {

	private String header;
	private String footer;
	private String body;
	private String subject;
	private String recipientName;
	private Date date;
	
	public String getHeader() {
		return header;
	}
	public void setHeader(String header) {
		this.header = header;
	}
	public String getFooter() {
		return footer;
	}
	public void setFooter(String footer) {
		this.footer = footer;
	}
	public String getBody() {
		return body;
	}
	public void setBody(String body) {
		this.body = body;
	}
	public String getRecipientName() {
		return recipientName;
	}
	public void setRecipientName(String recipientName) {
		this.recipientName = recipientName;
	}
	public Date getDate() {
		return date;
	}
	public String getDateStr(){
		return HRUtil.parseDateToString(date);
	}
	public void setDate(Date date) {
		this.date = date;
	}
	
	public String getSubject() {
		return subject;
	}
	public void setSubject(String subject) {
		this.subject = subject;
	}
	public String buildEmail(){
		if(body != null)
			return body;
		else
			return "Empty Email";
	}
}
