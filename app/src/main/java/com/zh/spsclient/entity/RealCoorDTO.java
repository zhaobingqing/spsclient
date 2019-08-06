package com.zh.spsclient.entity;

public class RealCoorDTO {
	private int coorID;
	public int getCoorID() {
		return coorID;
	}
	public void setCoorID(int coorID) {
		this.coorID = coorID;
	}
	public String getGeoLat() {
		return geoLat;
	}
	public void setGeoLat(String geoLat) {
		this.geoLat = geoLat;
	}
	public String getGeoLng() {
		return geoLng;
	}
	public void setGeoLng(String geoLng) {
		this.geoLng = geoLng;
	}
	String geoLat;
	String geoLng;
	
	//定位时间
	private String currentTime;
	public String getCurrentTime() {
		return currentTime;
	}
	public void setCurrentTime(String currentTime) {
		this.currentTime = currentTime;
	}
	
	//坐标定位类型
	private String locProvider;
	public String getLocProvider() {
		return locProvider;
	}
	public void setLocProvider(String locProvider) {
		this.locProvider = locProvider;
	}
}
