package com.zh.spsclient.common;

public enum TaskStatus {
	// δ��ȡ = 0,
	Start(0) ,
	 //����ȡ = 1,
	Used(1),
	  //ִ���� = 2,
	Performing(2),
	  //������� = 3,
	Suspending(3),
	 // ����ȡ�� = 4,
	Cancel(4),
	//������� = 5
	End(5);
	
	int nColor = 0;
	TaskStatus(int nColor){
		this.nColor = nColor;
	}
	public int GetColor(){
		return this.nColor;
	}
}
