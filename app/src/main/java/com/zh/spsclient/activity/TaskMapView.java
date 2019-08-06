package com.zh.spsclient.activity;

import java.io.File;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.location.Location;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.location.LocationManagerProxy;
import com.amap.api.location.LocationProviderProxy;
import com.amap.api.maps.AMap;
import com.amap.api.maps.AMap.InfoWindowAdapter;
import com.amap.api.maps.AMap.OnInfoWindowClickListener;
import com.amap.api.maps.AMap.OnMapClickListener;
import com.amap.api.maps.AMap.OnMapLoadedListener;
import com.amap.api.maps.AMap.OnMapLongClickListener;
import com.amap.api.maps.AMap.OnMarkerClickListener;
import com.amap.api.maps.AMap.OnMarkerDragListener;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.LocationSource;
import com.amap.api.maps.SupportMapFragment;
import com.amap.api.maps.UiSettings;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.MyLocationStyle;
import com.amap.api.maps.model.PolylineOptions;

import com.zh.spsclient.MainView;
import com.zh.spsclient.R;
import com.zh.spsclient.common.CommonRecord;
import com.zh.spsclient.common.WebServiceAdapter;
import com.zh.spsclient.dao.PipelineCoorDAO;
import com.zh.spsclient.dao.RealCoorDAO;
import com.zh.spsclient.dao.TaskResultDAO;
import com.zh.spsclient.entity.ImageFileEntity;
import com.zh.spsclient.entity.RealCoorDTO;
import com.zh.spsclient.entity.RealCoorEntity;
import com.zh.spsclient.entity.TaskResultEntity;
import com.zh.spsclient.service.MapService;
import com.zh.spsclient.util.AMapUtil;
import com.zh.spsclient.util.Constants;
import com.zh.spsclient.util.Converts;
import com.zh.spsclient.util.ImageUtil;

@SuppressLint("HandlerLeak")
public class TaskMapView extends FragmentActivity implements
	LocationSource, 
	AMapLocationListener,
	OnMarkerClickListener, 
	OnInfoWindowClickListener, 
	InfoWindowAdapter, 
	OnMarkerDragListener, 
	OnMapLoadedListener,
	OnMapClickListener,
	OnMapLongClickListener{
	
	/*
	 * 高德地图API
	 */
	private AMap aMap;
	private OnLocationChangedListener mListener;
	private LocationManagerProxy mAMapLocationManager; //地图定位代理
	private UiSettings mUiSettings;
	private LatLng cPos;
	/*
	 * web service服务
	 */
	WebServiceAdapter adapter = new WebServiceAdapter();
	/*
	 * 控件变量
	 */
	Button btnEndChecking;
	Button btnRestartChecking;
	Button btnStartChecking;
	Button btnPauseChecking;
	Button btnRecordFeedback;
	private TextView mapTitle;	
	private LinearLayout layoutLinear;
	private ScrollView scrollMenu;
	//菜单是否显示
	private Boolean bIsVisible = true;
	/*
	 * 私有变量
	 */
	String sNew;
	String sCurrent;
	Map<String, Object> taskDetail;
	final int nFillColor = 15661055;
	static Boolean isRunning = false;
	private String taskState = "0";
	private boolean bIsStartingMap = false;
	private boolean bIsFirstLoadMap=true;
	/*private int locationUpdatesTime=30000;*/
	String taskID;
	String terminalID;
	List<RealCoorDTO> realCoorDTOList;
	String[] coors = {};
	RealCoorDAO realCoorDAO;
	TaskResultDAO taskResultDAO;
	PipelineCoorDAO pipelineCoorDAO;
	private SQLiteDatabase _database = null;
	/*private String sUser = "";*/
	
	@SuppressLint("HandlerLeak")	
	/*private static final int RETURN_MAIN_ITEM = Menu.FIRST;
	private static final int END_CHECKING_ITEM = Menu.FIRST + 1;
	private static final int PAUSE_CHECKING_ITEM = Menu.FIRST + 2;
	private static final int RECORD_DATA_ITEM = Menu.FIRST + 3;	*/

	/*
	 * activity生命周期
	 * (non-Javadoc)
	 * @see android.support.v4.app.FragmentActivity#onStart()
	 */
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);//隐藏状态栏
		//加载taskmap_view.xml资源文件
		this.setContentView(R.layout.taskmap_view);	
		
		initializing();
		initAMap();
		initTable();
		initTimer();
		startTimer();
		//retrieveData(_database);理论坐标点去掉
		retrieveHistoryData(_database);
		
	}	
	@Override
	protected void onStart(){		
		super.onStart();		
	}
	
	@Override
	protected void onStop(){
		realCoorDAO.closeDB();
		taskResultDAO.closeDB();
		super.onStop();		
	}
	/*
	 * (non-Javadoc)
	 * @see android.support.v4.app.FragmentActivity#onResume()
	 */
	@Override
	protected void onResume() {
		super.onResume();
		if(bIsFirstLoadMap)//定位到当前位置
		{
			//locationUpdatesTime=1000;
			activateLocation(mListener);
		}
		if(taskState.equals("2")){
			activate(mListener);
		}
		
			
		initTable();		
	}
	
	@Override
	protected void onPause() {
		realCoorDAO.closeDB();
		taskResultDAO.closeDB();
		pipelineCoorDAO.closeDB();
		super.onPause();		
	}
	
	@Override
	protected void onDestroy() {
		destroyLocationManager();
		realCoorDAO.closeDB();
		taskResultDAO.closeDB();
		pipelineCoorDAO.closeDB();
		stopTimer();
		super.onDestroy();
		if(_database!= null)   
		{  
			_database.close();//SQLiteDatabase sqldb;   
		} 
	}
	private void destroyLocationManager(){
		if (mAMapLocationManager != null) {
			mAMapLocationManager.removeUpdates(this);
			mAMapLocationManager.destory();
		}
		mAMapLocationManager = null;
	}
	

	/*
	 * 各个控件初始化
	 */
	@SuppressWarnings("unchecked")
	private void initializing(){
		
		layoutLinear = (LinearLayout) this.findViewById(R.id.layoutLinear);
		this.registerForContextMenu(layoutLinear);
		scrollMenu = (ScrollView)this.findViewById(R.id.scrollMenu);
		mapTitle = (TextView)this.findViewById(R.id.mark_listenter_text);		
		btnEndChecking = (Button)this.findViewById(R.id.btnEndChecking);
		btnRestartChecking = (Button)this.findViewById(R.id.btnRestartChecking);		
		btnStartChecking = (Button)this.findViewById(R.id.btnStartChecking);
		btnPauseChecking = (Button)this.findViewById(R.id.btnPauseChecking);
		btnRecordFeedback = (Button)this.findViewById(R.id.btnRecordFeedback);
		//获取Bundle
		Bundle bundle = this.getIntent().getExtras();
		//向Bundle中传递数据
		taskDetail = (Map<String, Object>) bundle.getSerializable("tasklist");
		taskID = (String)taskDetail.get("taskID");
		terminalID=(String)taskDetail.get("terminalid");
		taskState = (String)taskDetail.get("taskState");
		mapTitle.setText((String)taskDetail.get("taskName") + 
				" " + 
				"(" + 
				Converts.ConvertToTaskStatus (taskState) + 
				")");
		mAMapLocationManager = LocationManagerProxy.getInstance(this);
		//sUser = bundle.getString("user");
			
		/*RealCoorDAO realCoorDAO = new RealCoorDAO();
		realCoorDAO.database = openOrCreateDatabase(CommonRecord.dbName,MODE_PRIVATE,null);
		realCoorDAO.deleteData();
		realCoorDAO.closeDB();*/
	}
	
	List<RealCoorDTO> noUpRealCoorDTOList;
	List<TaskResultEntity> noUpTaskResultList;
	
	/*
	 * 延迟发送实时定位坐标点
	 */
	private void delaySendRealCoor()
	{
		noUpRealCoorDTOList = realCoorDAO.queryNoUpData(taskID, terminalID);	
		for(int i = 0; i < noUpRealCoorDTOList.size(); i++){
			double geoLat = Double.parseDouble(noUpRealCoorDTOList.get(i).getGeoLat());
			double geoLng = Double.parseDouble(noUpRealCoorDTOList.get(i).getGeoLng());
			String currentTime=noUpRealCoorDTOList.get(i).getCurrentTime();
			String locProvider=noUpRealCoorDTOList.get(i).getLocProvider();
			if(isNetworkConnected())
			{
				// 提供手持机系统时间，时间要求24小时制，解决漂移点问题  lee
				String isUpLoad=adapter.uploadCoor(terminalID, geoLng, geoLat, taskID,currentTime,locProvider);
				
				if(Boolean.parseBoolean(isUpLoad))
				{
					realCoorDAO.updateIfUpLoad(Double.toString(geoLat), Double.toString(geoLng));
				}
			}
		}
	}
	
	/*
	 * 延迟发送反馈信息
	 */
	private void delaySendTaskResult()
	{
		noUpTaskResultList=taskResultDAO.queryNoUpData(taskID, terminalID);
		for(int i = 0; i < noUpTaskResultList.size(); i++){
			List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("Taskid", noUpTaskResultList.get(i).getTaskID());
			map.put("Terminaid",noUpTaskResultList.get(i).getTerminalID());
			map.put("Feedbackcoordinate", noUpTaskResultList.get(i).getCoordinates()); //坐标
			map.put("Feedbacktxt", noUpTaskResultList.get(i).getContents());
			map.put("Feedbackdate", noUpTaskResultList.get(i).getFeedbackDate());
			map.put("Feedbackperson", noUpTaskResultList.get(i).getFeedBackPerson());
			map.put("Coorphoto", noUpTaskResultList.get(i).getCoorphoto());//插入图片名称
			list.add(map);
			
			if(isNetworkConnected())
			{
				String mCurrentPhotoPath="";// 图片路径
			    String imageFileName=noUpTaskResultList.get(i).getCoorphoto();//图片名称
			    String isPhotoUpLoad="false";
			    if(!imageFileName.equals(""))
			    {
					File image = new File(ImageUtil.getAlbumDir(), imageFileName);
					mCurrentPhotoPath = image.getAbsolutePath();
					// 提供手持机系统时间，时间要求24小时制，解决漂移点问题  lee
					if(mCurrentPhotoPath!=null)
					{
						//上传图片
					    isPhotoUpLoad=sendImage(mCurrentPhotoPath, "0");
					}
			    }
			    if(Boolean.parseBoolean(isPhotoUpLoad)||imageFileName.equals(""))
				{
					String isUpLoad=adapter.uploadResult(terminalID, list);
					if(Boolean.parseBoolean(isUpLoad))
					{
						taskResultDAO.updateIfUpLoad(noUpTaskResultList.get(i).getCoordinates());
					}
				}
			}
		}
	}
	
	/**
	 * 上传图片到服务器
	 */
	protected String sendImage(String... params) {
		String filePath = params[0];
		ImageFileEntity bean = new ImageFileEntity();
		//String type = params[1];// 上传图片还是视频，图片需要压缩
		String content;
			content = ImageUtil.bitmapToString(filePath);
		bean.setImageContent(content);
		File f = new File(filePath);
		String fileName = f.getName();
		bean.setImageName(fileName);
		String imgJson=bean.getImageContent();
	    return adapter.uploadImage(fileName,imgJson);//使用webservice
	}
	
	/*
	 * 定义定时器、定时器任务及Handler句柄
	 */
	private final Timer timer = new Timer();
	private TimerTask task;
	/*Handler timerhandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			//要做的事情
			//super.handleMessage(msg);
			delaySendRealCoor();
		}

	};*/
	
	/*
	 * 初始化计时器任务。
	 */
	private void initTimer()
	{
		task = new TimerTask() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				/*Message message = new Message();
				message.what = 1;
				timerhandler.sendMessage(message);*/
				try {
					delaySendRealCoor();
					delaySendTaskResult();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		};
	}
	
	/*
	 * 启动定时器。
	 */
	private void startTimer()
	{
		timer.schedule(task, 2000, 2*60*1000);
	}
	
	/*
	 * 停止定时器。
	 */
	private void stopTimer()
	{
		timer.cancel();
	}
	
	/*
	 * 判断是否有网络连接  2g 3g
	 */
	private boolean isNetworkConnected() 
	{ 
		Boolean isCon=false;
		ConnectivityManager mConnectivityManager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo mNetworkInfo = mConnectivityManager.getActiveNetworkInfo(); 
		if (mNetworkInfo != null&& mNetworkInfo.getType() == ConnectivityManager.TYPE_MOBILE) 
		{ 
			if(mNetworkInfo.getSubtype() == TelephonyManager.NETWORK_TYPE_GPRS||mNetworkInfo.getSubtype()==TelephonyManager.NETWORK_TYPE_EDGE||
					mNetworkInfo.getSubtype()==TelephonyManager.NETWORK_TYPE_UMTS||mNetworkInfo.getSubtype()==TelephonyManager.NETWORK_TYPE_HSDPA
					||mNetworkInfo.getSubtype()==TelephonyManager.NETWORK_TYPE_HSPA)
			{
				isCon=true;
			}
		} 
		return isCon; 
	}
	
	
	/**
	 * 初始化AMap，设置控件状态
	 */
	private void initAMap() {
		if (aMap == null) {
			aMap = ((SupportMapFragment) getSupportFragmentManager()
					.findFragmentById(R.id.map)).getMap();
			if (AMapUtil.checkReady(this, aMap)) {
				setUpMap();
			}			
		}	
		setButtonState(taskState);
	}
	
	private void setCurrrentLatLng(LatLng curr){
		// 设置当前位置经纬度
		LatLng currentPosition = curr;
		aMap.moveCamera(CameraUpdateFactory
						.newLatLngZoom(currentPosition, 18));
		

		//当前位置
		//sCurrent = currentPosition.latitude + "," + currentPosition.longitude;
		/*aMap.addMarker(new MarkerOptions()
			.position(currentPosition)
			//.title("")
			.snippet("" + currentPosition.latitude + "," + currentPosition.longitude)
			.icon(BitmapDescriptorFactory
			//.fromResource(R.drawable.start)))
			//.fromResource(R.drawable.location_marker)))				
			.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)))
			.showInfoWindow();*/
	}
	//地图设置
	private void setUpMap() {
		//if (mAMapLocationManager == null) {
			//mAMapLocationManager = LocationManagerProxy.getInstance(this);
		//}
		
		//setCurrrentLatLng(Constants.WUSHENQI);
		mUiSettings = aMap.getUiSettings();
		mUiSettings.setCompassEnabled(true);//设置指南针
		mUiSettings.setScaleControlsEnabled(true);//设置比例尺
		//aMap.setMyLocationEnabled(true);
		aMap.setOnMapClickListener(this);// 设置地图点击操作
		aMap.setOnMapLongClickListener(this);// 设置地图长按操作
		aMap.setOnMarkerClickListener(this);// 设置点击marker事件监听器
		aMap.setOnInfoWindowClickListener(this);// 设置点击infoWindow事件监听器
		aMap.setInfoWindowAdapter(this);// 设置自定义InfoWindow样式
		aMap.setOnMarkerDragListener(this);// 设置marker可拖拽事件监听器
		aMap.setOnMapLoadedListener(this);// 设置amap加载成功事件监听器
	}	
	
	private void setLocationPosition(){
		// 自定义系统定位小蓝点
		MyLocationStyle myLocationStyle = new MyLocationStyle();
		myLocationStyle.myLocationIcon(BitmapDescriptorFactory
						.fromResource(R.drawable.location_marker));		
				
		myLocationStyle.strokeWidth((float) 0.2)
			.radiusFillColor(nFillColor);
		aMap.setMyLocationStyle(myLocationStyle);
		mAMapLocationManager = LocationManagerProxy
			.getInstance(TaskMapView.this);
		aMap.setLocationSource(this);
		// 设置为true表示系统定位按钮显示并响应点击，false表示隐藏，默认是false
		aMap.setMyLocationEnabled(true);
	}
	/*
	 * 设置菜单按钮状态״̬
	 */
	private void setButtonState(String taskState) {
		// TODO Auto-generated method stub
		int nState = Integer.parseInt(taskState);
		mapTitle.setText((String)taskDetail.get("taskName") + 
				" " + 
				"(" + 
				Converts.ConvertToTaskStatus (taskState) + 
				")");
		switch(nState){
		case 0: //未领取			
			btnRestartChecking.setVisibility(View.GONE);
			btnEndChecking.setVisibility(View.GONE);
			btnPauseChecking.setVisibility(View.GONE);
			btnRecordFeedback.setVisibility(View.GONE);
			break;
		case 1: //已经领取
			btnRestartChecking.setVisibility(View.GONE);
			btnEndChecking.setVisibility(View.GONE);
			btnPauseChecking.setVisibility(View.GONE);
			btnRecordFeedback.setVisibility(View.GONE);
			break;
		case 2: //ִ执行欧诺个
			btnStartChecking.setVisibility(View.GONE);
			btnRestartChecking.setVisibility(View.GONE);
			btnEndChecking.setVisibility(View.VISIBLE);
			btnPauseChecking.setVisibility(View.VISIBLE);
			btnRecordFeedback.setVisibility(View.VISIBLE);
			LonLats=new ArrayList<String>();
			bIsStartingMap = true;
			/*//启动后台服务
			Intent intent=new Intent(TaskMapView.this,MapService.class);
			stopService(intent);*/
			
			
			
			
			//2014-03-13 测试重复数据问题 lee
			//activate(mListener);
			setLocationPosition();
			break;
		case 3: //
			btnRestartChecking.setVisibility(View.VISIBLE);
			btnRestartChecking.setEnabled(true);
			btnStartChecking.setVisibility(View.GONE);
			btnPauseChecking.setVisibility(View.GONE);
			break;
		case 5: //
			btnRestartChecking.setVisibility(View.GONE);
			btnStartChecking.setVisibility(View.GONE);
			btnPauseChecking.setVisibility(View.GONE);
			btnEndChecking.setVisibility(View.GONE);
			btnRecordFeedback.setVisibility(View.GONE);
			break;
		default:
			break;
		}
	}
	/*
	 * AMapLocationListener设置(non-Javadoc)
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
	
	
	
	/*
	 * LocationSource操作
	 */
	/**
	 * 定位激活
	 */
	@Override
	public void activate(OnLocationChangedListener listener) {
		if(bIsStartingMap){
			//locationUpdatesTime=30000;
			activateLocation(listener);
		}
	}

	private boolean iSLocationUpdates=false;
	private void activateLocation(OnLocationChangedListener listener){
		mListener = listener;
		if (mAMapLocationManager == null) {
			mAMapLocationManager = LocationManagerProxy.getInstance(this);
			
		}
		// Location API定位采用GPS和网络混合定位方式，时间最短是5000毫秒
		/*
		* mAMapLocManager.setGpsEnable(false);//
		* 1.0.2版本新增方法，设置true表示混合定位中包含gps定位，false表示纯网络定位，默认是true
		*/
		/**/
		if(bIsFirstLoadMap)//首次加载地图采用混合模式
		{
			mAMapLocationManager.requestLocationUpdates(
					LocationProviderProxy.AMapNetwork,1000, 10, this);
			/*mAMapLocationManager.requestLocationUpdates(
					LocationManagerProxy.GPS_PROVIDER, 1000, 10, this);*/
		}
		if(bIsStartingMap){
			if(!iSLocationUpdates)//定时定位巡线轨迹采用GPS模式
			{
				/*mAMapLocationManager.requestLocationUpdates(
						LocationManagerProxy.GPS_PROVIDER, 30000, 10, this);*/
				mAMapLocationManager.requestLocationUpdates(
				LocationProviderProxy.AMapNetwork,20000, 10, this);
				iSLocationUpdates=true;
			}
		}
	}
	
	private List<String> LonLats=null;
	private int count = 0;
	/**
	 * 向服务器传递实时坐标轨迹
	 */
	@Override
	public void onLocationChanged(final AMapLocation location) {
		if (mListener != null) {
			mListener.onLocationChanged(location);
		}
		if (location != null) {
			final Double geoLat = location.getLatitude();
			final Double geoLng = location.getLongitude();
			location.getExtras();
			//绘制起始点
			if(count == 0){
				cPos = new LatLng(geoLat,geoLng);
	    		aMap.addMarker(new MarkerOptions()
	    			.position(cPos)
	    			//.title("")
	    			//.snippet("" + cPos.latitude + "," + cPos.longitude)
	    			.icon(BitmapDescriptorFactory
	    				.fromResource(R.drawable.start)))				
	    				.showInfoWindow();
	    		//设置坐标
	    		setCurrrentLatLng(cPos);
	    		if(bIsFirstLoadMap)
	    		{
	    			bIsFirstLoadMap=false;
	    			deactivate();
	    		}
			}
			if(bIsStartingMap)
			{
				new Thread()
	    	    {
	    	    	@SuppressLint("SimpleDateFormat")
					@Override
	    	    	public void run()
	    	    	{ 
	    	    		
	    	    		String lonlat=geoLat.toString()+","+geoLng.toString();
	    				if(!LonLats.contains(lonlat))
	    				{
	    					LonLats.add(lonlat);
		    	    		Date date = new Date();
		    	   		 	SimpleDateFormat df=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		    	   		 	final String currentTime = df.format(date);
		    	    		terminalID = (String)taskDetail.get("terminalid");
							taskID = (String)taskDetail.get("taskID");
							
							System.out.println(df);
							   	
							RealCoorEntity realCoorEntity = new RealCoorEntity();
							realCoorEntity.setId(++count);
							realCoorEntity.setTaskID(taskID);
							realCoorEntity.setTerminalID(terminalID);
							realCoorEntity.setGeoLat(geoLat);
							realCoorEntity.setGeoLng(geoLng);
							realCoorEntity.setLocProvider(location.getProvider());
							realCoorEntity.setCurrentTime(currentTime);
							realCoorEntity.setIfUpLoad("0");
							realCoorDAO.insertData(realCoorEntity);
							if(isNetworkConnected())
							{
								// 提供手持机系统时间，时间要求24小时制，解决漂移点问题  lee
								String isUpLoad=adapter.uploadCoor(terminalID, geoLng, geoLat, taskID,currentTime,location.getProvider());
								
								if(Boolean.parseBoolean(isUpLoad))
								{
									realCoorDAO.updateIfUpLoad(Double.toString(geoLat), Double.toString(geoLng));
								}
							}
							Log.d("xuming..........",adapter.getsText());     
	    				}
	    	    	}
	    	    }.start();    	       	    
	    		
				String strLngLat = geoLat + "," + geoLng;
				Message msg = new Message();
				msg.obj = strLngLat;
				if (handler != null) {
					handler.sendMessage(msg);
				}
			}
		}
	}
	
	/**
	 * 停止定位
	 */
	@Override
	public void deactivate() {
		mListener = null;
		if (mAMapLocationManager != null) {
			mAMapLocationManager.removeUpdates(this);
			mAMapLocationManager.destory();
		}
		mAMapLocationManager = null;
		iSLocationUpdates=false;
	}
	/*
	 * 画线
	 */
	private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			drawTwoLine((String)msg.obj);
		}
	};
	/*
	 * 绘制坐标轨迹实时曲线
	 */
	private void drawTwoLine(String sNewPoint){
		//新位置坐标
		sNew = sNewPoint;			
		String[] aNew = sNew.split(",");
		double latNew = Double.parseDouble(aNew[0]);
		double lngNew = Double.parseDouble(aNew[1]);
		//原来位置坐标
		if(sCurrent!=null)
		{
			String[] aCurrent = sCurrent.split(",");
			double latCurrent = Double.parseDouble(aCurrent[0]);
			double lngCurrent = Double.parseDouble(aCurrent[1]);	
			
			
			aMap.addPolyline(
					(new PolylineOptions())					
					.add(new LatLng(latNew,lngNew))
					.add(new LatLng(latCurrent,lngCurrent))
					.color(Color.GREEN)
					.width(10));			
			
			aMap.addMarker(new MarkerOptions()
			.visible(true)
			.position(new LatLng(latNew,lngNew))
			.title("Maker" + count)
			.snippet(" " + latNew + "," + lngNew)
			.icon(BitmapDescriptorFactory
					.fromResource(R.drawable.hong1))
			.draggable(true));	
		}
		sCurrent = sNew;
		
	}

	/*
	 * 绘制坐标轨迹历史曲线
	 */
	private void drawHisData(List<RealCoorDTO> list){
		//绘制历实时坐标史轨迹起始点
		/*
		if(count == 0){
			LatLng cPos = new LatLng(Double.parseDouble(list.get(0).getGeoLat()),Double.parseDouble(list.get(0).getGeoLng()));
    		aMap.addMarker(new MarkerOptions()
    			.position(cPos)
    			.title("")
    			.snippet("" + cPos.latitude + "," + cPos.longitude)
    			.icon(BitmapDescriptorFactory
    				.fromResource(R.drawable.start)))				
    				.showInfoWindow();
		}
		*/
		for(int i = 0; i < list.size() - 1; i++){
			double latNew = Double.parseDouble(list.get(i).getGeoLat());
			double lngNew = Double.parseDouble(list.get(i).getGeoLng());
			double latCurrent = Double.parseDouble(list.get(i+1).getGeoLat());	
			double lngCurrent = Double.parseDouble(list.get(i+1).getGeoLng());	
			aMap.addPolyline(
					(new PolylineOptions())					
					.add(new LatLng(latNew,lngNew))
					.add(new LatLng(latCurrent,lngCurrent))
					.color(Color.GREEN)
					.width(10));	
			
			aMap.addMarker(new MarkerOptions()
				.visible(true)
				.position(new LatLng(latNew,lngNew))
				.title("")
				.snippet(" " + latNew + "," + lngNew)
				.icon(BitmapDescriptorFactory
						.fromResource(R.drawable.hong1))
				.draggable(true));		
		}
		if(list.size()>0)
		{
			int listNum=list.size();
			sCurrent=list.get(listNum-1).getGeoLat()+","+list.get(listNum-1).getGeoLng();
		}
	}
	/*
	 * 理论坐标画线
	 */
	/*private void drawLines(){		
		new Thread(){
        	@Override
	    	public void run()
	    	{ 
        		coors = pipelineCoorDAO.queryPLCOORData();
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
	}*/
	
	private void initTable(){
		_database = openOrCreateDatabase(CommonRecord.dbName,MODE_PRIVATE,null);
		if(_database != null){
			realCoorDAO = new RealCoorDAO(_database);		
			taskResultDAO=new TaskResultDAO(_database);
			pipelineCoorDAO = new PipelineCoorDAO(_database);
		}		
	}	
	/*
	 * 绘制理论坐标
	 */
	/*private void retrieveData(SQLiteDatabase db){
		if(db != null){			
			drawLines();
		}
	}*/
	/*
	 * 获取历史数据
	 */
	private void retrieveHistoryData(final SQLiteDatabase db){
		new Thread()
	    {
	    	@Override
	    	public void run()
	    	{ 
	    		if(db != null){
	    			if(taskState.equals("3") || taskState.equals("2")){
	    				realCoorDTOList = realCoorDAO.queryData(taskID,terminalID);	
	    				drawHisData(realCoorDTOList);
	    			}
	    			//drawLines();
	    		}
	    	}
	    }.start();
	}
	
	/*
	 * 启动反馈页面
	 */
	private void transferData(Boolean IsEnd){
		if(sNew!=null)
		{
			iSLocationUpdates=false;
			Intent intent = new Intent();
			intent.setClass(TaskMapView.this, TaskResultView.class);
			Bundle bundle = new Bundle();//获取Bundle
			if(IsEnd){
				taskDetail.put("taskState", "5");
			}
			bundle.putSerializable("tasklist", (Serializable)taskDetail);
			intent.putExtras(bundle);
			intent.putExtra("currentCoor", sNew);
			intent.putExtra("IsEnd", IsEnd);
			//intent.putExtra("user", sUser);
			startActivity(intent);
		}
		else
		{
			new AlertDialog.Builder(TaskMapView.this)//
			.setTitle("任务反馈")
			.setMessage("当前还没有定位坐标点信息，请稍后！")
			.setIcon(R.drawable.icon)
			.setPositiveButton("确定",null).show();
		}
	}
	
	/*
	 * 开始巡检
	 */
	public void setStartCheck(View v){
		taskState = "2";
		onMapClick(null);
		LonLats=new ArrayList<String>();
		bIsStartingMap = true;
		updateTaskState(taskState,true);		
		//activate(mListener);
	}
	
	@SuppressLint("SimpleDateFormat")
	private void updateTaskState(final String sState, Boolean isStart){
		final String authCode = (String)taskDetail.get("terminalid");
		final String taskID = (String)taskDetail.get("taskID");		
		Date date = new Date();
		SimpleDateFormat df=new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		
		final String startTime = (isStart)? df.format(date) : "";
		final String endTime = (isStart)? "": df.format(date);
		new Thread(){
        	@Override
	    	public void run()
	    	{         		
        		try {	        			
        			String result = adapter.updateTaskState(authCode, taskID, sState, startTime, endTime); 
					Message m = new Message();
					m.obj = result;
					startMapHandler.sendMessage(m);
				}      
        		finally{
        			
        		}
	    	}
        }.start(); 
	}
	Handler startMapHandler = new Handler(){
		@Override
		public void handleMessage(Message msg) {	
			
		}
	};
	/*
	 * 结束巡检
	 */
	public void setEndCheck(View v){
		//onKeyDown(KeyEvent.KEYCODE_BACK, null);
		new AlertDialog.Builder(TaskMapView.this)//
		.setTitle("巡检")
		.setMessage("是否结束巡检")
		.setIcon(R.drawable.icon)
		.setPositiveButton("否",new DialogInterface.OnClickListener(){
					public void onClick(DialogInterface dialog,int whichButton) {						
					}
				})
		.setNegativeButton("是",new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int whichButton) {
						setResult(RESULT_OK);
						taskState = "5";
						onMapClick(null);
						bIsStartingMap = false;
						LonLats=null;
						deactivate();
						updateTaskState("5",false);
						//transferData(true);		
					    //TaskMapView.this.finish();
					}
				}).show();		
	}
	
	/*
	 * 返回主页面
	 */
	public void setReturnMain(View v){
		onMapClick(null);
		if(taskState.equals("2")){
			startMapService();
		}
		if(whichHandler != null){
        	whichHandler.sendMessage(new Message());
        }
	}
	private void startMapService(){
		destroyLocationManager(); //销毁LocatioinManager
		bIsStartingMap = false;
		new Thread(){
        	@Override
        	public void run()
	    	{   
				Intent intent=new Intent(TaskMapView.this,MapService.class);
				Bundle bundle = new Bundle();	 		
		 		bundle.putSerializable("tasklist", (Serializable)taskDetail);
		 		intent.putExtras(bundle);  
		 		intent.putExtra("terminalID", (String)taskDetail.get("terminalid"));  
		 		intent.putExtra("taskID", (String)taskDetail.get("taskID"));  
				
		        startService(intent);		        
	    	}
        }.start();
	}
	Handler whichHandler = new Handler(){
		@Override
		public void handleMessage(Message msg) {	
			Intent intent=new Intent(TaskMapView.this,MainView.class);
			startActivity(intent);
			TaskMapView.this.finish();
			//onKeyDown(KeyEvent.KEYCODE_BACK, null);
		}
	};	
	
	/*
	 * 暂停巡检
	 */
	@SuppressLint("SimpleDateFormat")
	public void setPauseCheck(View v){		
		taskState = "3";
		updateTaskState(taskState,false);
        onMapClick(null);
        //setButtonState(taskState);
        bIsStartingMap = false;
        LonLats=null;
		deactivate();
	}
	
	/*
	 * 重新开始
	 */
	public void setRestartCheck(View v){
		taskState = "2";
		updateTaskState(taskState,true);
		onMapClick(null);
		LonLats=new ArrayList<String>();
		bIsStartingMap = true;
		//2014-03-13 测试重复数据问题 lee
		//activate(mListener);
		retrieveHistoryData(_database);
	}
	Handler pauseCheckingHandler = new Handler(){
		@Override
		public void handleMessage(Message msg) {			
			Intent intent = new Intent();
			intent.setClass(TaskMapView.this, MainView.class);		
			startActivity(intent);
			TaskMapView.this.finish();
		}
	};
	/*
	 * 反馈记录
	 */
	public void setRecordData(View v){
		onMapClick(null);
		if(taskState.equals("2")){
			startMapService();
		}
        if(RecordDataHandler != null){
        	RecordDataHandler.sendMessage(new Message());
        }		
	}
	Handler RecordDataHandler = new Handler(){
		@Override
		public void handleMessage(Message msg) {	
			transferData(false);
		}
	};
	/*
	 * 刷新
	 */
	public void setRefresh(View v){
		initializing();
		initAMap();
		initTable();
	}
	private long mExitTime;
	@Override
	public boolean onKeyDown(int keyCode,KeyEvent event){
		if(keyCode == KeyEvent.KEYCODE_BACK){
			if((System.currentTimeMillis() - mExitTime) > 2000){				
				Toast.makeText(this, getText(R.string.exit_message), Toast.LENGTH_LONG).show();
				mExitTime = System.currentTimeMillis();
			}
			else{
				finish();
			}
			return true;
		}
		return super.onKeyDown(keyCode, event);
	};
	/*
	 * 地图点击操作菜单(non-Javadoc)
	 * @see com.amap.api.maps.AMap.OnMapClickListener#onMapClick(com.amap.api.maps.model.LatLng)
	 */
	@Override
	public void onMapClick(LatLng arg0) {
		// TODO Auto-generated method stub
		bIsVisible = !bIsVisible;
		if(bIsVisible){
			scrollMenu.setVisibility(View.VISIBLE);
		}
		else{
			scrollMenu.setVisibility(View.INVISIBLE);
		}		
		setButtonState(taskState);
	}
	/*
	 * 地图上下文菜单(non-Javadoc)
	 * @see android.app.Activity#onCreateContextMenu(android.view.ContextMenu, android.view.View, android.view.ContextMenu.ContextMenuInfo)
	 */
	/*
	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {

		// TODO Auto-generated method stub
		super.onCreateContextMenu(menu, v, menuInfo);
		//menu.setHeaderIcon(R.drawable.tinfo);
		menu.setHeaderTitle(R.string.taskmap_view_context_menu_title);
		menu.add(Menu.NONE, 0, 0, R.string.taskmap_view_return_main);
		menu.add(Menu.NONE, 1, 1, R.string.taskmap_view_end_checking);
		menu.add(Menu.NONE, 2, 2, R.string.taskmap_view_pause_checking);
		menu.add(Menu.NONE, 3, 3, R.string.taskmap_view_record_data);
	}
	*/
	/*
	@Override
	public boolean onContextItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		switch (item.getItemId()) {
		case 0:
			
			Toast.makeText(this, "����ˡ�" + item.getTitle() + "���˵�",
					Toast.LENGTH_LONG).show();
			layoutLinear.setBackgroundColor(Color.GREEN);// ���ñ�����ɫ
			break;
		case 1:
			
			break;
		case 2:
			
			break;
		case 3:
			
			break;
		}
		return super.onContextItemSelected(item);
	}
	*/
	/*
	 * ��ͼҳ�����Ĳ˵�(non-Javadoc)
	 * @see android.app.Activity#onCreateOptionsMenu(android.view.Menu)
	 */
	// �˵�
	/*
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		menu.add(0, RETURN_MAIN_ITEM, 0, R.string.taskmap_view_return_main).setIcon(R.drawable.bill);
		menu.add(0, END_CHECKING_ITEM, 0, R.string.taskmap_view_end_checking).setIcon(R.drawable.bill);
		menu.add(0, PAUSE_CHECKING_ITEM, 0, R.string.taskmap_view_pause_checking).setIcon(R.drawable.bill);
		menu.add(0, RECORD_DATA_ITEM, 0, R.string.taskmap_view_record_data).setIcon(R.drawable.bill);
		return super.onCreateOptionsMenu(menu);
	}
	*/
	// �˵���Ӧ�¼�
	/*
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		super.onOptionsItemSelected(item);
		switch (item.getItemId()) {		
			case 0:			
				break;
			case 1:
				break;
			case 2:
				break;
		}
		return true;
	}
	*/
	/*
	 * ��ʱ��������(non-Javadoc)
	 * @see com.amap.api.maps.AMap.OnMapLongClickListener#onMapLongClick(com.amap.api.maps.model.LatLng)
	 */
	@Override
	public void onMapLongClick(LatLng arg0) {
		// TODO Auto-generated method stub
		bIsVisible = !bIsVisible;
		if(bIsVisible){
			scrollMenu.setVisibility(View.VISIBLE);
		}
		else{
			scrollMenu.setVisibility(View.INVISIBLE);
		}
	}
	/*
	 * 
	 */
	/**
	 * 
	 */
	@Override
	public boolean onMarkerClick(Marker arg0) {
		// TODO Auto-generated method stub
		if(arg0.getPosition() == Constants.ZHI_XIN_XI_LU)
		{
			if (AMapUtil.checkReady(this, aMap)) {
				new AlertDialog.Builder(this).setTitle("").setIcon(
					     android.R.drawable.ic_dialog_info).setView(
					     new EditText(this)).setPositiveButton("", 
					    		 new DialogInterface.OnClickListener() {
									@Override
									public void onClick(DialogInterface dialog,int which) {
									}
						})
					     .setNegativeButton("", null).show();
			}
		}
		return false;
	}
	
	@Override
	public void onMapLoaded() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onMarkerDrag(Marker arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onMarkerDragEnd(Marker arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onMarkerDragStart(Marker arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public View getInfoContents(Marker arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public View getInfoWindow(Marker arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onInfoWindowClick(Marker arg0) {
		// TODO Auto-generated method stub		
	}	
	
	/**
	 * 设置地图默认的比例尺是否显示
	 */
	public void setScaleEnabled(View v) {
		if (AMapUtil.checkReady(this, aMap)) {
			mUiSettings.setScaleControlsEnabled(((CheckBox) v).isChecked());
		}
	}

	/**
	 * 设置地图默认的缩放按钮是否显示
	 */
	public void setZoomButtonsEnabled(View v) {
		if (AMapUtil.checkReady(this, aMap)) {
			mUiSettings.setZoomControlsEnabled(((CheckBox) v).isChecked());
		}

	}

	/**
	 * 设置地图默认的指南针是否显示
	 */
	public void setCompassEnabled(View v) {
		if (AMapUtil.checkReady(this, aMap)) {
			mUiSettings.setCompassEnabled(((CheckBox) v).isChecked());
		}
	}

	/**
	 * 设置地图默认的定位按钮是否显示
	 */
	public void setMyLocationButtonEnabled(View v) {
		if (AMapUtil.checkReady(this, aMap)) {
			mUiSettings.setMyLocationButtonEnabled(((CheckBox) v).isChecked());
		}
	}

	/**
	 * 设置地图是否可以自动定位
	 */
	public void setMyLocationLayerEnabled(View v) {
		if (AMapUtil.checkReady(this, aMap)) {
			aMap.setMyLocationEnabled(((CheckBox) v).isChecked());
		}
	}

	/**
	 * 设置地图是否可以手势滑动
	 */
	public void setScrollGesturesEnabled(View v) {
		if (AMapUtil.checkReady(this, aMap)) {
			mUiSettings.setScrollGesturesEnabled(((CheckBox) v).isChecked());
		}

	}

	/**
	 * 设置地图是否可以手势缩放大小
	 */
	public void setZoomGesturesEnabled(View v) {
		if (AMapUtil.checkReady(this, aMap)) {
			mUiSettings.setZoomGesturesEnabled(((CheckBox) v).isChecked());
		}

	}

	/**
	 * 设置地图是否可以倾斜
	 */
	public void setTiltGesturesEnabled(View v) {
		if (AMapUtil.checkReady(this, aMap)) {
			mUiSettings.setTiltGesturesEnabled(((CheckBox) v).isChecked());
		}

	}

	/**
	 * 设置地图是否可以旋转
	 */
	public void setRotateGesturesEnabled(View v) {
		if (AMapUtil.checkReady(this, aMap)) {
			mUiSettings.setRotateGesturesEnabled(((CheckBox) v).isChecked());
		}
	}
}
		
