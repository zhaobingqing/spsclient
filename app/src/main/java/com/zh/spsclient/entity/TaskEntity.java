package com.zh.spsclient.entity;

public class TaskEntity {
	private static final long serialVersionUID = 1L;
	public String getTaskID() {
		return taskID;
	}
	public void setTaskID(String taskID) {
		this.taskID = taskID;
	}
	public String getPipeID() {
		return pipeID;
	}
	public void setPipeID(String pipeID) {
		this.pipeID = pipeID;
	}
	public String getPlanID() {
		return planID;
	}
	public void setPlanID(String planID) {
		this.planID = planID;
	}
	public String getTaskTypeID() {
		return taskTypeID;
	}
	public void setTaskTypeID(String taskTypeID) {
		this.taskTypeID = taskTypeID;
	}
	public String getTerminalID() {
		return terminalID;
	}
	public void setTerminalID(String terminalID) {
		this.terminalID = terminalID;
	}
	public String getUserID() {
		return userID;
	}
	public void setUserID(String userID) {
		this.userID = userID;
	}
	public String getDeptID() {
		return deptID;
	}
	public void setDeptID(String deptID) {
		this.deptID = deptID;
	}
	public String getTaskName() {
		return taskName;
	}
	public void setTaskName(String taskName) {
		this.taskName = taskName;
	}
	public String getTaskDesc() {
		return taskDesc;
	}
	public void setTaskDesc(String taskDesc) {
		this.taskDesc = taskDesc;
	}
	public String getPlanStartDate() {
		return planStartDate;
	}
	public void setPlanStartDate(String planStartDate) {
		this.planStartDate = planStartDate;
	}
	public String getPlanEndDate() {
		return planEndDate;
	}
	public void setPlanEndDate(String planEndDate) {
		this.planEndDate = planEndDate;
	}
	public String getTaskStartPos() {
		return taskStartPos;
	}
	public void setTaskStartPos(String taskStartPos) {
		this.taskStartPos = taskStartPos;
	}
	public String getTaskEndPos() {
		return taskEndPos;
	}
	public void setTaskEndPos(String taskEndPos) {
		this.taskEndPos = taskEndPos;
	}
	public String getActualStartDate() {
		return actualStartDate;
	}
	public void setActualStartDate(String actualStartDate) {
		this.actualStartDate = actualStartDate;
	}
	public String getActualEndDate() {
		return actualEndDate;
	}
	public void setActualEndDate(String actualEndDate) {
		this.actualEndDate = actualEndDate;
	}
	public String getTaskStatus() {
		return taskStatus;
	}
	public void setTaskStatus(String taskStatus) {
		this.taskStatus = taskStatus;
	}
	public String getIsAuto() {
		return isAuto;
	}
	public void setIsAuto(String isAuto) {
		this.isAuto = isAuto;
	}
	public String getCreateDate() {
		return createDate;
	}
	public void setCreateDate(String createDate) {
		this.createDate = createDate;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	private String taskID;
	private String pipeID;
	private String planID;
	private String taskTypeID;
	private String terminalID;
	private String userID;
	private String deptID;
	private String taskName;//ÈÎÎñÃû³Æ
	private String taskDesc;
	private String planStartDate;
	private String planEndDate;
	private String taskStartPos;
	private String taskEndPos;
	private String actualStartDate;
	private String actualEndDate;
	private String taskStatus;
	private String isAuto;
	private String createDate;	
}
