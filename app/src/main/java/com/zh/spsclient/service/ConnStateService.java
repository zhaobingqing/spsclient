package com.zh.spsclient.service;

import java.sql.Timestamp;

import com.zh.spsclient.common.WebServiceAdapter;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
 
public class ConnStateService extends Service
{
	static final String action1="Broadcast_action1";
	/*private int count = 0;*/
	Intent it;
	@Override
	public IBinder onBind(Intent intent)
	{
		return null;
	}
	@Override
	public void onCreate()   
	{
		super.onCreate();
		new Thread(){
			@Override
			public void run(){
				while(true){
					it = new Intent(action1);
					sendBroadcast(it); 
					try{
						//Thread.sleep(200);
						WebServiceAdapter adapter = new WebServiceAdapter();
						String timeStamp = new Timestamp(System.currentTimeMillis()).toString();
						adapter.insertConTimeStamp("", 
								"1", 
								"2013-12-22 10:00:00", 
								"2013-12-22 14:00:00", 
								timeStamp);
						Thread.sleep(10000);
					}catch(Exception e){
						e.printStackTrace();
					}
				}
			}
		}.start();
	}
	@Override
	public void onDestroy()
	{
		super.onDestroy();
		this.stopService(it);
	}
}