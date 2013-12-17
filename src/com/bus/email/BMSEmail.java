package com.bus.email;

import java.util.List;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class BMSEmail {

	private Properties props = new Properties();
	private Session mailSession;
	private List<String> recAddrs;
	private BMSEmailContent emailContent;
	private String host = "smtp.163.com";
	private String username = "sdtransport";
	private String password = "kobebryant";
	private String senderName = "sdtransport@163.com";

	public BMSEmail(String receiverName, List<String> receivers,
			String subject, String content) {
		emailContent = new BMSEmailContent();
		emailContent.setBody(content);
		emailContent.setRecipientName(receiverName);
		emailContent.setSubject(subject);
		recAddrs = receivers;

		props.setProperty("mail.smtp.auth", "true");
		props.setProperty("mail.transport.protocol", "smtp");
		props.setProperty("mail.smtp.host", host);
		props.setProperty("mail.user", "sdtransport");
		props.setProperty("mail.password", "kobebryant");

		mailSession = Session.getInstance(props,
				new javax.mail.Authenticator() {
					protected PasswordAuthentication getPasswordAuthentication() {
						return new PasswordAuthentication(username, password);
					}
				});
	}

	public boolean sendEmail() {
		try {
			 Transport transport = mailSession.getTransport();
			 MimeMessage message = new MimeMessage(mailSession);
			 message.setFrom(new InternetAddress(senderName));
			 message.setSubject(emailContent.getSubject());
			 message.setContent(emailContent.buildEmail(), "text/html;charset=UTF-8");
			
			 InternetAddress[] address = getAddresses();
			 if(address == null){
				 throw new Exception("No receiver defined.");
			 }
			 message.addRecipients(Message.RecipientType.TO, address);
			
			 transport.connect();
			 transport.sendMessage(message,
			 message.getRecipients(Message.RecipientType.TO));
			 transport.close();

			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	private InternetAddress[] getAddresses() throws Exception {
		if (recAddrs == null || recAddrs.size() == 0) {
			return null;
		} else {
			InternetAddress[] addrs = new InternetAddress[recAddrs.size()];
			for (int i = 0; i < recAddrs.size(); i++) {
				addrs[i] = new InternetAddress(recAddrs.get(i));
			}
			return addrs;
		}
	}

	public List<String> getRecAddrs() {
		return recAddrs;
	}

	public void setRecAddrs(List<String> recAddrs) {
		this.recAddrs = recAddrs;
	}

	public BMSEmailContent getEmailContent() {
		return emailContent;
	}

	public void setEmailContent(BMSEmailContent emailContent) {
		this.emailContent = emailContent;
	}

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getSenderName() {
		return senderName;
	}

	public void setSenderName(String senderName) {
		this.senderName = senderName;
	}

	public Properties getProps() {
		return props;
	}

	public void setProps(Properties props) {
		this.props = props;
	}

}
