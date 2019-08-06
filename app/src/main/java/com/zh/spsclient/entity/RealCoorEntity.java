package com.zh.spsclient.entity;

public class RealCoorEntity {
	//编号
	private int id;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	//任务id
	private String taskID;
	public String getTaskID() {
		return taskID;
	}
	public void setTaskID(String taskID) {
		this.taskID = taskID;
	}
	//终端机id
	private String terminalID;
	public String getTerminalID() {
		return terminalID;
	}
	public void setTerminalID(String terminalID) {
		this.terminalID = terminalID;
	}
	//纬度
	private double geoLat;
	public double getGeoLat() {
		return geoLat;
	}
	public void setGeoLat(double geoLat) {
		this.geoLat = geoLat;
	}
	//经度
	private double geoLng;
	public double getGeoLng() {
		return geoLng;
	}
	public void setGeoLng(double geoLng) {
		this.geoLng = geoLng;
	}
	//坐标定位类型
	private String locProvider;
	public String getLocProvider() {
		return locProvider;
	}
	public void setLocProvider(String locProvider) {
		this.locProvider = locProvider;
	}
	//定位时间
	private String currentTime;
	public String getCurrentTime() {
		return currentTime;
	}
	public void setCurrentTime(String currentTime) {
		this.currentTime = currentTime;
	}
	//是否上传 0否 1是
	private String ifUpLoad;
	public String getIfUpLoad() {
		return ifUpLoad;
	}
	public void setIfUpLoad(String ifUpLoad) {
		this.ifUpLoad = ifUpLoad;
	}
}
