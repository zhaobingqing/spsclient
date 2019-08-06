package com.zh.spsclient.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import com.zh.spsclient.*;

public class CommandReceiver extends BroadcastReceiver {
    int status;//״ֵ̬
    public static final String UPDATE_STATUS="UPDATE";
	@Override
	public void onReceive(final Context context, Intent intent) {
		//updateUI(context);
		if (intent.getAction().equals("android.intent.action.PACKAGE_ADDED")) {		// install
			String packageName = intent.getDataString();

			Log.i("homer", "安装了 :" + packageName);
		}

		if (intent.getAction().equals("android.intent.action.PACKAGE_REMOVED")) {	// uninstall
			String packageName = intent.getDataString();

			Log.i("homer", "卸载了 :" + packageName);
		}
		
		if(intent.getAction().equals("android.intent.action.BOOT_COMPLETED")) {		// boot
			Intent intent2 = new Intent(context, WelcomeView.class);
			intent2.setAction("android.intent.action.MAIN");
			intent2.addCategory("android.intent.category.LAUNCHER");
			intent2.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			context.startActivity(intent2);
		}
	}
	
	public void updateUI(Context context)
	{
		try
		{
			//TaskMapView.tv.setTextSize(20);
			//TaskMapView.tv.setText(ZMDUtil.next());
		}catch(Exception e)
		{
			
		}		
	}
}
