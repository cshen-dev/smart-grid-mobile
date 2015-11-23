package edu.mushrchun.model;

public class Device {
	private String device_id;
	private String did;
	public String getDid() {
		return did;
	}
	public void setDid(String did) {
		this.did = did;
	}
	private String uid;
	private String name;
	private String type;
	public String getDevice_id() {
		return device_id;
	}
	public void setDevice_id(String device_id) {
		this.device_id = device_id;
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
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
}
