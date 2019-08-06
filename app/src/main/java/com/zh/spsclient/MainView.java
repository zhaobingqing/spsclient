package com.zh.spsclient;

import com.zh.spsclient.activity.TaskListView;
import com.zh.spsclient.activity.ViewMap;
import com.zh.spsclient.util.Constants;

import android.app.ActivityGroup;
import android.content.Intent;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.TabHost;


@SuppressWarnings("deprecation")
public class MainView extends ActivityGroup  {
	TabHost tabHost;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);//隐藏状态栏
		setContentView(R.layout.bottom_tab);
		//Bundle bundle = this.getIntent().getExtras();		
        //String sUser = bundle.getString("user");
        tabHost = (TabHost) findViewById(R.id.tab_host_item);      
        tabHost.setup(this.getLocalActivityManager());
        
        tabHost.setPadding(tabHost.getPaddingLeft(),  
        		tabHost.getPaddingTop(), tabHost.getPaddingRight(),  
        		tabHost.getPaddingBottom()-10);  

        /*addTab("tabFirst",Constants.MAIN_VIEW_FRIST_PAGE,R.drawable.tab_info);
        addTab("tabCommunication",Constants.MAIN_VIEW_COMMUNICATION_RECORD_PAGE,R.drawable.tab_info);
        addTab("tabSetting",Constants.MAIN_VIEW_SETTING_PAGE,R.drawable.tab_info);
        addTab("tabMyInfo",Constants.MAIN_VIEW_MY_INFO_PAGE,R.drawable.tab_info);*/
        //主页面  任务单列表
        tabHost.addTab(
        		tabHost.newTabSpec("tabFirst")
	            .setIndicator(Constants.MAIN_VIEW_FRIST_PAGE, 
	            		getResources().getDrawable(R.drawable.home1))
	            .setContent(new Intent(this, TaskListView.class)));
        //管线位置
	    tabHost.addTab(
	    		tabHost.newTabSpec("tabCommunication")
	            .setIndicator(Constants.MAIN_VIEW_SETTING_PAGE, 
	            		getResources().getDrawable(R.drawable.communication))
	            .setContent(new Intent(this, ViewMap.class)));
	    /*// 设置界面
	    tabHost.addTab(
	    		tabHost.newTabSpec("tabSetting")
	            .setIndicator(Constants.MAIN_VIEW_SETTING_PAGE, 
	            		getResources().getDrawable(R.drawable.setting))
	            .setContent(new Intent(this, ViewMap.class)));*/
	    // 我的信息
	    tabHost.addTab(
	    		tabHost.newTabSpec("tabMyInfo")
	            .setIndicator(Constants.MAIN_VIEW_MY_INFO_PAGE, 
	            		getResources().getDrawable(R.drawable.info))
	            .setContent(new Intent(this, ViewMap.class)));
	    tabHost.setCurrentTab(0);
	}
	/*private void addTab(String label, String titleContent, int drawableId) { 
		Intent intent = new Intent(this, TaskListView.class); 
		TabHost.TabSpec spec = tabHost.newTabSpec(label); 
		  
		View tabIndicator = LayoutInflater.from(this).inflate(R.layout.tab_indicator, tabHost.getTabWidget(), false);
		TextView title = (TextView) tabIndicator.findViewById(R.id.title); 
		title.setText(titleContent); 
		ImageView icon = (ImageView) tabIndicator.findViewById(R.id.icon); 
		icon.setImageResource(drawableId); 
		  
		spec.setIndicator(tabIndicator);
		spec.setContent(intent); 
		tabHost.addTab(spec); 
	} */

}
