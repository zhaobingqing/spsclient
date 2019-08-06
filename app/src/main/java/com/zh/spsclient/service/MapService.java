package com.zh.spsclient.service;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Map;

import android.app.Service;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.location.Location;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Message;
import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.location.LocationManagerProxy;
import com.zh.spsclient.common.CommonRecord;
import com.zh.spsclient.common.WebServiceAdapter;
import com.zh.spsclient.dao.RealCoorDAO;
import com.zh.spsclient.entity.RealCoorEntity;
 
public class MapService extends Service implements
	AMapLocationListener{
	
	static final String action_map = "Broadcast_action_map";
	Intent it;
	
	private LocationManagerProxy mAMapLocManager = null;
	RealCoorDAO realCoorDAO;
	
	WebServiceAdapter adapter = new WebServiceAdapter();
	
	String terminalID = "";
	String taskID = "";
	Map<String, Object> taskDetail;
	
	private SQLiteDatabase db = null;
	
	/*private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			
		}
	};*/
	/*
	 * Service ��������(non-Javadoc)
	 * @see android.app.Service#onBind(android.content.Intent)
	 */
	@Override
	public IBinder onBind(Intent intent)
	{		
		return null;
	}
	@Override
	public void onCreate()   
	{
		super.onCreate();
		mAMapLocManager = LocationManagerProxy.getInstance(this);
		enableMyLocation();
		initTable();
		/*new Thread(){
			@Override
			public void run(){
				while(true){
					it = new Intent(action_map);
					sendBroadcast(it); 
					try{
						Thread.sleep(200);
					}catch(Exception e){
						e.printStackTrace();
					}
				}
			}
		}.start();*/
	}
	@Override
	@SuppressWarnings("unchecked")
	public void onStart(Intent intent,int startId){
		terminalID = intent.getStringExtra("terminalID");
		taskID = intent.getStringExtra("taskID");
		
		//��ȡBundle
		Bundle bundle = intent.getExtras();
		//��ȡBundle�е�������ϸ
		taskDetail = (Map<String, Object>)bundle.getSerializable("tasklist");
	}
	@Override
	public void onDestroy()
	{
		if (mAMapLocManager != null) {
			mAMapLocManager.removeUpdates(this);
			mAMapLocManager.destory();
		}
		mAMapLocManager = null;
		if(db.isOpen())
			db.close();
		super.onDestroy();		
	}
	@Override
	public boolean onUnbind(Intent intent){
		return false;		
	}
	
	/*
	 * AMapLocationListener�ӿ�ʵ��
	 * (non-Javadoc)
	 * @see android.location.LocationListener#onLocationChanged(android.location.Location)
	 */
	@Override
	public void onLocationChanged(Location arg0) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void onProviderDisabled(String arg0) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void onProviderEnabled(String arg0) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void onStatusChanged(String arg0, int arg1, Bundle arg2) {
		// TODO Auto-generated method stub
		
	}
	private int count = 0;
	@Override
	
	public void onLocationChanged(final AMapLocation location) {
		// TODO Auto-generated method stub
		if (location != null) {
			final Double geoLng = location.getLongitude();
			final Double geoLat = location.getLatitude();
			location.getExtras();
			
			new Thread()
    	    {
    	    	@Override
    	    	public void run()
    	    	{ 
    	    		GregorianCalendar ca = new GregorianCalendar();
    	    	

    	    		Date date = new Date();
    	    		
    	    		SimpleDateFormat df=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    	   		 	final String currentTime = df.format(date)+" "+ca.get(Calendar.AM_PM);
    	    		System.out.println(currentTime);		
					RealCoorEntity realCoorEntity = new RealCoorEntity();
					realCoorEntity.setId(++count);
					realCoorEntity.setTaskID(taskID);
					realCoorEntity.setTerminalID(terminalID);
					realCoorEntity.setGeoLat(geoLat);
					realCoorEntity.setGeoLng(geoLng);
					realCoorEntity.setLocProvider(location.getProvider());
					realCoorEntity.setCurrentTime(currentTime);
					realCoorEntity.setIfUpLoad("0");
					//System.out.println(currentTime);
					realCoorDAO.insertData(realCoorEntity);					
					//从手持机获取日期参数上传坐标信息 lee 20130313 
					//adapter.uploadCoor(terminalID, geoLng, geoLat, taskID,currentTime,location.getProvider());   
    	    	}
    	    }.start();
    	    
			String strLngLat = geoLat + "," + geoLng;
			Message msg = new Message();
			msg.obj = strLngLat;
			/*if (handler != null) {
				handler.sendMessage(msg);
			}*/
		}
	}
	public void enableMyLocation() {
		// Location API��λ����GPS�������϶�λ��ʽ��ʱ�������5000����
		/*
		 * mAMapLocManager.setGpsEnable(false);//
		 * 1.0.2�汾��������������true��ʾ��϶�λ�а�gps��λ��false��ʾ�����綨λ��Ĭ����true
		 */
		mAMapLocManager.requestLocationUpdates(
				LocationManagerProxy.GPS_PROVIDER, 60000, 10, this);
	}

	public void disableMyLocation() {
		if (mAMapLocManager != null) {
			mAMapLocManager.removeUpdates(this);
		}
	}
	
	private void initTable(){
		db =openOrCreateDatabase(CommonRecord.dbName,MODE_PRIVATE,null);
		if(db != null){
			realCoorDAO = new RealCoorDAO(db);				
		}
	}
	
}