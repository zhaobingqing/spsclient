package com.zh.spsclient.common;

public enum TaskStatus {
	// 未领取 = 0,
	Start(0) ,
	 //已领取 = 1,
	Used(1),
	  //执行中 = 2,
	Performing(2),
	  //任务挂起 = 3,
	Suspending(3),
	 // 任务取消 = 4,
	Cancel(4),
	//任务完成 = 5
	End(5);
	
	int nColor = 0;
	TaskStatus(int nColor){
		this.nColor = nColor;
	}
	public int GetColor(){
		return this.nColor;
	}
}
