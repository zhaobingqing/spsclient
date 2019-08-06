package com.zh.spsclient.util;

public class Converts {
	
	public static String ConvertToTaskStatus(String status) {
		int index = Integer.parseInt(status);
		String taskStatus = "";
		switch(index){
		case 0:
			taskStatus = "未领取";
			break;
		case 1:
			taskStatus = "已领取";
			break;
		case 2:
			taskStatus = "执行中";
			break;
		case 3:
			taskStatus = "暂停";
			 break;
		case 4:
			taskStatus = "取消";
			break;
		case 5:
			taskStatus = "完成";
			break;
		default:
			break;
		}
		return taskStatus;
	}
	public static String JSONResult(Object o){
		String sResult = "";
		if(!o.equals(null)){
			sResult = o.toString();
		}
		return sResult;
	}
	
}
