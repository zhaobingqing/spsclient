package com.zh.spsclient.activity;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.location.LocationManagerProxy;
import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.SupportMapFragment;
import com.amap.api.maps.model.LatLng;
import com.zh.spsclient.R;
import com.zh.spsclient.common.CommonRecord;
import com.zh.spsclient.util.AMapUtil;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.location.Location;
import android.os.Bundle;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.view.WindowManager;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;

public class ViewMap extends FragmentActivity implements
OnSeekBarChangeListener,AMapLocationListener {
	private AMap aMap;
	/*private SeekBar mColorBar;
	private SeekBar mAlphaBar;
	private SeekBar mWidthBar;*/
	
	private LocationManagerProxy mAMapLocManager = null;
	String[] coors;
	/*private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {			
		}
	};*/
	
	/*private void drawLines(){
		if(coors.length>0)
		{
			new Thread(){
        	@Override
	    	public void run()
	    	{ 
        		String[] arr = coors[0].split(",");
				double a11 = Double.parseDouble(arr[0]);
				double a12 = Double.parseDouble(arr[1]);
        		LatLng startPosition = new LatLng(a11,a12);
        		arr = coors[coors.length - 1].split(",");
				a11 = Double.parseDouble(arr[0]);
				a12 = Double.parseDouble(arr[1]);
        		LatLng endPosition = new LatLng(a11,a12);
        		
        		aMap.addMarker(new MarkerOptions()
	        		.position(startPosition)
	        		.title("")
	        		.snippet("" + startPosition.latitude + "," + startPosition.longitude)
	        		.icon(BitmapDescriptorFactory
	        				.fromResource(R.drawable.start)))        				
	        				.showInfoWindow();
        		
        		aMap.addMarker(new MarkerOptions()
        		.position(endPosition)
        		.title("")
        		.snippet("" + endPosition.latitude + "," + endPosition.longitude)
        		.icon(BitmapDescriptorFactory
        				.fromResource(R.drawable.end)))        				
        				.showInfoWindow();
        		
				//for(int i = 0; i < coors.length /2; i = i+2){
        		for(int i = 0; i < coors.length -1; i ++){
					String sCurrent = coors[i];
					String sNew = coors[i + 1];
					String[] a = sNew.split(",");
					double a1 = Double.parseDouble(a[0]);
					double a2 = Double.parseDouble(a[1]);
					
					String[] b = sCurrent.split(",");
					double b1 = Double.parseDouble(b[0]);
					double b2 = Double.parseDouble(b[1]);		
					
					aMap.addPolyline((new PolylineOptions())
							.add(new LatLng(a1,a2))
							.add(new LatLng(b1,b2))					
							.color(Color.RED)
							.width(5));			
				}
	    	}
			}.start();
		}
	}*/
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);//隐藏状态栏
		setContentView(R.layout.viewmap);
		init();	
		
		//mAMapLocManager = LocationManagerProxy.getInstance(this);
		//queryPLCOORData();//理论坐标点不要
		//drawLines();
	}

	/**
	 * ��ʼ��AMap����
	 */
	private void init() {
		
		if (aMap == null) {
			aMap = ((SupportMapFragment) getSupportFragmentManager()
					.findFragmentById(R.id.map)).getMap();
			if (AMapUtil.checkReady(this, aMap)) {
				setUpMap();
			}
		}
	}

	private void setUpMap() {
		// ���ŵ�ǰ�ɼ��ͼ����
		aMap.moveCamera(CameraUpdateFactory
				.newLatLngZoom(new LatLng(38.591312617,108.709578014), 12));
		// ����һ������
		/*aMap.addPolyline((new PolylineOptions())
				.add(new LatLng(38.40143611460,108.98525181300), 
						new LatLng(38.40142420630, 108.98560699000)).color(Color.RED)
				.width(5));		*/
	}

	@Override
	public void onStopTrackingTouch(SeekBar seekBar) {
	}

	@Override
	public void onStartTrackingTouch(SeekBar seekBar) {
	}

	/**
	 * Polyline�ж������ɫ��͸���ȣ����ʿ��������Ӧ�¼�
	 */
	@Override
	public void onProgressChanged(SeekBar seekBar, int progress,
			boolean fromUser) {
		
	}

	public void enableMyLocation() {
		// Location API��λ����GPS�������϶�λ��ʽ��ʱ�������5000����
		/*
		 * mAMapLocManager.setGpsEnable(false);//
		 * 1.0.2�汾��������������true��ʾ��϶�λ�а�gps��λ��false��ʾ�����綨λ��Ĭ����true
		 */
		/*mAMapLocManager.requestLocationUpdates(
				LocationProviderProxy.AMapNetwork, 5000, 10, this);*/
	}

	public void disableMyLocation() {
		if (mAMapLocManager != null) {
			mAMapLocManager.removeUpdates(this);
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
		enableMyLocation();
	}

	@Override
	protected void onPause() {
		disableMyLocation();
		super.onPause();
	}

	@Override
	protected void onDestroy() {
		if (mAMapLocManager != null) {
			mAMapLocManager.removeUpdates(this);
			mAMapLocManager.destory();
		}
		mAMapLocManager = null;
		super.onDestroy(); 
		if(database!= null)   
		{  
			database.close();//SQLiteDatabase sqldb;   
		}  

	}

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

	@Override
	public void onLocationChanged(AMapLocation location) {
		if (location != null) {
			Double geoLat = location.getLatitude();
			Double geoLng = location.getLongitude();
			Bundle locBundle = location.getExtras();
			if (locBundle != null) {
				locBundle.getString("citycode");
				locBundle.getString("desc");
			}
			String strLngLat = geoLat + "," + geoLng;
			Message msg = new Message();
			msg.obj = strLngLat;
			/*if (handler != null) {
				handler.sendMessage(msg);
			}*/
		}
	}
	SQLiteDatabase database=null;
	public void queryPLCOORData(){
		database =openOrCreateDatabase(CommonRecord.dbName,MODE_PRIVATE,null);
		if(database == null){
			
		}	
		else{
			Cursor cursor = database.query(CommonRecord.PIPELINE_COORDINATE_TABLE_NAME, 
					CommonRecord.FIELD_COORDINATE, null,null, null, null, null,null);
			int n = cursor.getCount();
			//n = 2000;
			coors = new String[n];
			if(cursor.getCount()!=0){
				cursor.moveToFirst();
				for(int i=0;i< n;i+=1){					
					coors[i] = cursor.getString(3) + "," + cursor.getString(2);// 2: Lng; 3: lat
					cursor.moveToNext();
				}				
			}	
			cursor.close();
		}
		database.close();
	}
}
		
