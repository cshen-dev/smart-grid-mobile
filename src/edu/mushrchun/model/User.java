package edu.mushrchun.model;

import java.io.Serializable;

public class User implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -6825537288062082103L;
	private String uid;
	private String name;
	private String mail;
	private String loginpass;
	private String loginname;
	public User(){}

	@Override
	public String toString() {
		return "User [uid=" + uid + ", name=" + name + ", mail=" + mail
				+ ", loginpass=" + loginpass + ", loginname=" + loginname
				+ ", toString()=" + super.toString() + "]";
	}
	public String getLoginpass() {
		return loginpass;
	}
	public void setLoginpass(String loginpass) {
		this.loginpass = loginpass;
	}
	public String getLoginname() {
		return loginname;
	}
	public void setLoginname(String loginname) {
		this.loginname = loginname;
	}
	public String getUid() {
		return uid;
	}
	public void setUid(String uid) {
		this.uid = uid;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getMail() {
		return mail;
	}
	public void setMail(String mail) {
		this.mail = mail;
	}
	
	
}
