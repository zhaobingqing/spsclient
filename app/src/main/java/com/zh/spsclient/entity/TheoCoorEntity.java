package com.zh.spsclient.entity;

public class TheoCoorEntity {
	private String coorid;
	public String getCoorid() {
		return coorid;
	}
	public void setCoorid(String coorid) {
		this.coorid = coorid;
	}
	public String getPipeid() {
		return pipeid;
	}
	public void setPipeid(String pipeid) {
		this.pipeid = pipeid;
	}
	public String getCoorx() {
		return coorx;
	}
	public void setCoorx(String coorx) {
		this.coorx = coorx;
	}
	public String getCoory() {
		return coory;
	}
	public void setCoory(String coory) {
		this.coory = coory;
	}
	public double getHeight() {
		return height;
	}
	public void setHeight(double height) {
		this.height = height;
	}
	public double getDepth() {
		return depth;
	}
	public void setDepth(double depth) {
		this.depth = depth;
	}
	public double getInterval() {
		return interval;
	}
	public void setInterval(double interval) {
		this.interval = interval;
	}
	public String getTerrain() {
		return terrain;
	}
	public void setTerrain(String terrain) {
		this.terrain = terrain;
	}
	private String pipeid;
	private String coorx;
	private String coory;
	private double height;
	private double depth;
	private double interval;
	private String terrain;
}
