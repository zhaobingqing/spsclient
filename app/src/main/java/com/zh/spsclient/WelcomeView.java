package com.zh.spsclient;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.os.Environment;
import java.io.File;

import com.zh.spsclient.R;
import com.zh.spsclient.common.CommonRecord;

/**
 * 
 * */
@SuppressLint("HandlerLeak")
public class WelcomeView extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);//隐藏状态栏
		final Window win = getWindow();
		
		win.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
		
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		
		this.setContentView(R.layout.welcome_view);
		
		ImageView iv=(ImageView)this.findViewById(R.id.wpic);
		
		iv.setImageResource(R.drawable.welcome3);
		welcome();
		//initTable();
	}
	
	/**
	 * 
	 * @param 
	 * @return
	 */
	public void welcome() {
		
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				try {
					
					Thread.sleep(10000);
					Message m = new Message();
					
					logHandler.sendMessage(m);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}).start();
	}
	
	
	Handler logHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			
			showLogo();
		}
	};
	
	
	public boolean fileIsExists(){
        try{
        		File r=Environment.getDataDirectory();
        		String path = r.getPath() + File.separator + "data"  + File.separator + "com.zh.spsclient" + File.separator + "databases"+ File.separator + "spsDB";
                File f=new File(path);
                boolean a=f.exists();
                if(!a){
                        return false;
                }
        }catch (Exception e) {
                return false;
        }
        return true;
	}
	
	
	public boolean existDatabase() 
	{ 
		SQLiteDatabase _database = null;
		boolean flag = false; 

		try 
		{ 
		 	_database=SQLiteDatabase.openDatabase(CommonRecord.dbName, null, SQLiteDatabase.OPEN_READWRITE + SQLiteDatabase.CREATE_IF_NECESSARY);
		 	flag = true; 
		}
		catch (Exception  e) 
		{ 
			flag = false; 
		} 
		finally 
		{ 
			if (_database != null) 
				_database.close(); 
			_database = null; 
		} 
		return flag; 
	} 
	
	/**
	 * 
	 * @param 
	 * @return
	 */
	public void showLogo() {	
		
		Intent it=new Intent();	
		boolean isExists=fileIsExists();
		//初始化手持机数据库  lee 20140313
		//初次安装或重新安装需要通过此代码进行设备初始化取消屏蔽下行代码
		//CommonRecord.authCode = "";
		//if(CommonRecord.authCode == ""){
		if(!isExists){
			it.setClass(WelcomeView.this, InitializingView.class);			
		}else {
			it.setClass(WelcomeView.this, LoginView.class);
		}
		
    	startActivity(it);
    	
    	WelcomeView.this.finish();
	}
//	public void onAttachedToWindow() {
//	    this.getWindow().setType(WindowManager.LayoutParams.TYPE_KEYGUARD);
//	    super.onAttachedToWindow();
//	}


	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
//		
//		if(keyCode==4 ){			
//			
//			android.os.Process.killProcess(android.os.Process.myPid());
//		}
//		return super.onKeyDown(keyCode, event);
		switch (keyCode) {
	    case KeyEvent.KEYCODE_HOME:
	        return true;
	    case KeyEvent.KEYCODE_BACK:
	        return true;
	    case KeyEvent.KEYCODE_CALL:
	        return true;
	    case KeyEvent.KEYCODE_SYM:
	        return true;
	    case KeyEvent.KEYCODE_VOLUME_DOWN:
	        return true;
	    case KeyEvent.KEYCODE_VOLUME_UP:
	        return true;
	    case KeyEvent.KEYCODE_STAR:
	        return true;
	}
 return super.onKeyDown(keyCode, event);

	}	
}
