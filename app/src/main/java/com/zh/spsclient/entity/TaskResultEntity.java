package com.zh.spsclient.entity;

public class TaskResultEntity {
	//反馈编码
	private String taskResultID;
	public String getTaskResultID() {
		return taskResultID;
	}
	public void setTaskResultID(String taskResultID) {
		this.taskResultID = taskResultID;
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
	//反馈坐标
	private String coordinates;
	public String getCoordinates() {
		return coordinates;
	}
	public void setCoordinates(String coordinates) {
		this.coordinates = coordinates;
	}
	//反馈内容
	private String contents;
	public String getContents() {
		return contents;
	}
	public void setContents(String contents) {
		this.contents = contents;
	}
	//反馈时间
	private String feedbackDate;
	public String getFeedbackDate() {
		return feedbackDate;
	}
	public void setFeedbackDate(String feedbackDate) {
		this.feedbackDate = feedbackDate;
	}
	//反馈照片
	private String coorphoto;
	public  String  getCoorphoto() {
		return coorphoto;
	}
	public  void  setCoorphoto(String coorphoto) {
		this.coorphoto=coorphoto;
	}
	
	//反馈人
	private String feedBackPerson;
	public String getFeedBackPerson() {
		return feedBackPerson;
	}
	public void setFeedBackPerson(String feedBackPerson) {
		this.feedBackPerson = feedBackPerson;
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
