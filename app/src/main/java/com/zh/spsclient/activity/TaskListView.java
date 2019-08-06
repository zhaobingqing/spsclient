package com.zh.spsclient.activity;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.zh.spsclient.LoginView;
import com.zh.spsclient.R;
import com.zh.spsclient.common.CommonRecord;
import com.zh.spsclient.common.WebServiceAdapter;
import com.zh.spsclient.service.MapService;
import com.zh.spsclient.util.Constants;
import com.zh.spsclient.util.Converts;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

import android.telephony.TelephonyManager;   

public class TaskListView extends Activity {
	private List<Map<String, Object>> mData; 
	WebServiceAdapter adapter;
	WebServiceAdapter adapter2;
	JSONArray resultArray;
	ProgressDialog m_Dialog;
	private SQLiteDatabase _listdatabase = null;
	Map<String,Object> taskDetail;
	String status = "0";
	/*private String sUser = "";*/
    /** Called when the activity is first created. */
	
	/*
	 * activity控件生命周期(non-Javadoc)
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);//隐藏状态栏
        setContentView(R.layout.tasklist_view);       
        
		try {
			downloadTaskList();
		} catch (Exception e) {
			// TODO: handle exception
		}
    }
    
    @Override
	protected void onStop(){    	
		super.onStop();
	}
    
    @Override
	protected void onPause() {    	
		super.onPause();		
	}
    
    @Override
	protected void onResume() {    	
		super.onResume();	
		//downloadTaskList();
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();	
		if(_listdatabase != null){
			_listdatabase.close();
		}
		//onKeyDown(KeyEvent.KEYCODE_BACK,null);		
	}
	
	 /*
     * 下载任务列表
     */
    private void downloadTaskList(){
    	try {
			m_Dialog = ProgressDialog.show(TaskListView.this, 
					Constants.TASK_LIST_VIEW_PLEASE_WAITING, 
					Constants.TASK_LIST_VIEW_DOWNLOADING_TASK,true);
			TelephonyManager    telephonyManager;
			telephonyManager =         ( TelephonyManager )getSystemService( Context.TELEPHONY_SERVICE );
			final String imeistring = telephonyManager.getDeviceId();
			//开始下载任务列表
			new Thread(){
				@Override
				public void run()
				{ 
					adapter = new WebServiceAdapter();
					String result = adapter.getTaskByCode(imeistring, "");
					try {
						resultArray = new JSONArray(result);
						//JSONObject jsonObj = resultArray.getJSONObject(0);
						Message m = new Message();
						loadHandler.sendMessage(m);
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}      
					finally{
						m_Dialog.dismiss();
					}
				}
			}.start();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}             
    }
    
    /*
    private void downloadTaskList2(){
    	m_Dialog = ProgressDialog.show(TaskListView.this, 
				Constants.TASK_LIST_VIEW_PLEASE_WAITING, 
				Constants.TASK_LIST_VIEW_DOWNLOADING_TASK,true);
    	TelephonyManager    telephonyManager;
		telephonyManager =         ( TelephonyManager )getSystemService( Context.TELEPHONY_SERVICE );
		final String imeistring = telephonyManager.getDeviceId();
        //开始下载任务列表
        new Thread(){
        	@Override
	    	public void run()
	    	{ 
        		adapter = new WebServiceAdapter();
        		String result = adapter.getTaskByCode(imeistring, "");
        		try {
        			resultArray = new JSONArray(result);
					//JSONObject jsonObj = resultArray.getJSONObject(0);
					Message m = new Message();
					loadHandler.sendMessage(m);
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}      
        		finally{
        			m_Dialog.dismiss();
        		}
	    	}
        }.start();             
    }*/
    @Override
	protected void onNewIntent(Intent intent){
    	super.onNewIntent(intent);
    	setIntent(intent);
    	
    	Bundle bundle = intent.getExtras();
    	bundle.getSerializable("tasklist");
    	
    	loadData();
    }
    /*
     * 加载任务单数据
     */
    @SuppressLint("HandlerLeak")
    Handler loadHandler = new Handler(){
		@Override
		public void handleMessage(Message msg) {			
			loadData();
			initTable();			
		}
	};
	
	@SuppressLint("HandlerLeak")
	Handler startMapHandler = new Handler(){
		@Override
		public void handleMessage(Message msg) {	
			
			//_listdatabase.close();
			if(status.equals("2")){//如果状态是执行中，则先停止服务再进入页面
				Intent intent=new Intent(TaskListView.this,MapService.class);
				stopService(intent);
			}
			Intent it = new Intent();	
			it.setClass(TaskListView.this, TaskMapView.class);
			Bundle bundle = new Bundle();		
	 		bundle.putSerializable("tasklist", (Serializable)taskDetail);
	 		//it.putExtra("user", sUser);
	 		it.putExtras(bundle);  
			startActivity(it);//onKeyDown(KeyEvent.KEYCODE_BACK,null);
		}
	};
	
	/*
	 * 
	 */
    private void loadData(){
    	/*int nGetCount = 0;
    	_listdatabase =openOrCreateDatabase(CommonRecord.dbName,MODE_PRIVATE,null);
		if(_listdatabase != null){
			nGetCount = queryCount();	
			Log.e("f", "f = " + nGetCount);
		}*/
    	//if(nGetCount == 0){
    	
    	mData = getData(resultArray);  
    	//}
    	//else{
    	//	queryData();
    	//}
        MyAdapter adapter = new MyAdapter(this); 
        ListView lv = (ListView)findViewById(android.R.id.list);
		lv.setAdapter(adapter);		
        lv.setOnItemClickListener(new OnItemClickListener(){
        	
			@Override
			public void onItemClick(AdapterView<?> a, View v, int position, long id) {
				// TODO Auto-generated method stub
				Log.i("Click","ListItemSelected:"+ a.toString()+" "
				            + v.toString()+":"+"ID:"+ id + "position: " + String.valueOf(position));
				//Uri uri =Uri.parse("smsto://123456789");
		 		//Intent intent = new Intent(Intent.ACTION_SENDTO,uri);				
		 		startChecking(position);
			}        	
        });
    }
    
    /*
     * 
     */
    private List<Map<String, Object>> getData(JSONArray resultArray) { 
    	List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();      	
    	
    	for(int i=0;i<resultArray.length();i++)
    	{
    		JSONObject jsonObj;
    		Map<String, Object> map = new HashMap<String, Object>();    
			try {
				jsonObj = resultArray.getJSONObject(i);
				map.put("taskID", Converts.JSONResult(jsonObj.get("Taskid")));
				map.put("terminalid", Converts.JSONResult(jsonObj.get("Terminalid")));
				map.put("taskName", Converts.JSONResult(jsonObj.get("Taskname")));       
				status = (String)jsonObj.get("Taskstatus");
				
				map.put("taskState", status);
				if(status.equals("1") || status.equals("0")){
					map.put("img", R.drawable.used);
				}
				else if(status.equals("2")){
					map.put("img", R.drawable.working);
					//isEnable = false;
				}
				else if(status.equals("3")){
					map.put("img", R.drawable.pausing);					
				}
				else{
					map.put("img", R.drawable.bill); 
				}
	        	map.put("taskDes",Converts.JSONResult(jsonObj.get("Taskdescribe")));
	        	map.put("Pipeid",Converts.JSONResult(jsonObj.get("Pipeid")));
	        	map.put("PipeName", Converts.JSONResult(jsonObj.get("PipeName")));
	        	map.put("Planstartdate",Converts.JSONResult(jsonObj.get("Planstartdate")));
	        	map.put("Planenddate",Converts.JSONResult(jsonObj.get("Planenddate")));
	        	map.put("Taskstartposition",Converts.JSONResult(jsonObj.get("Taskstartposition")));
	        	map.put("Taskendposition",Converts.JSONResult(jsonObj.get("Taskendposition")));
	        	map.put("Person", Converts.JSONResult(jsonObj.get("Person")));
	        	//map.put("img2", R.drawable.bill); 
	        	list.add(map); 
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    	}          
    	return list;     
    } 

	/*
	 * 进入地图巡检页面
	 */
	
	 @SuppressLint("SimpleDateFormat")
	public void startChecking(int index){        
		 //if(nMutex == 1){return;}
		 m_Dialog = ProgressDialog.show(TaskListView.this, 
					Constants.TASK_LIST_VIEW_PLEASE_WAITING, 
					Constants.TASK_LIST_VIEW_STARTING_MAP,true);
		 taskDetail = mData.get(index);
		 /*final String authCode = (String)taskDetail.get("terminalid");
		 final String taskID = (String)taskDetail.get("taskID");
		 final String status = (String)taskDetail.get("taskState");
		 Date date = new Date();
		 SimpleDateFormat df=new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		 final String startTime = df.format(date);
		 final String endTime = "";*/
		 
	        //进入地图巡检页面
	        new Thread(){
	        	@Override
		    	public void run()
		    	{ 	        		    		
	        		try {	      			
	        			/*String result = "";*/
	        			Message m = new Message();
						startMapHandler.sendMessage(m);
					}      
	        		finally{
	        			m_Dialog.dismiss();
	        			//_listdatabase.close();
	        			TaskListView.this.finish();
	        		}
		    	}
	        }.start();
			//TaskListView.this.finish();
			//onKeyDown(KeyEvent.KEYCODE_BACK,null);
	 } 	 	 
   
	 public void refreshTasklist(View v){
		 downloadTaskList();
	 }
	 
	 public void exitTasklist(View v){
		 for(int i=0;i<mData.size();i++)
		 {
			 taskDetail = mData.get(i);
			 String status = (String)taskDetail.get("taskState");
			 if(status.equals("2")){//如果状态是执行中，则先停止服务再进入页面
				 status = "3";
				 String terminalid = (String)taskDetail.get("terminalid");
				 String taskID = (String)taskDetail.get("taskID");	
				 updateTaskState(status,false,terminalid,taskID);
			
				 Intent intent=new Intent(TaskListView.this,MapService.class);
				 stopService(intent);
			 }
		 }
		 
		 if(exitHandler != null){
			 exitHandler.sendMessage(new Message());
		 }
	 }
	 
	 @SuppressLint("SimpleDateFormat")
	private void updateTaskState(final String sState, Boolean isStart,final String terminalid,final String taskID){
		Date date = new Date();
		SimpleDateFormat df=new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		
		final String startTime = (isStart)? df.format(date) : "";
		final String endTime = (isStart)? "": df.format(date);
		new Thread(){
        	@Override
	    	public void run()
	    	{         					
        		try {
					adapter.updateTaskState(terminalid, taskID, sState, startTime, endTime);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} 
	    	}
        }.start(); 
	}
	 
	 @SuppressLint("HandlerLeak")
	 Handler exitHandler = new Handler(){
			@Override
			public void handleMessage(Message msg) {	
				Intent intent=new Intent(TaskListView.this,LoginView.class);
				startActivity(intent);
				TaskListView.this.finish();
				//onKeyDown(KeyEvent.KEYCODE_BACK, null);
			}
	 };	
		
	 private void initTable(){			
		_listdatabase =openOrCreateDatabase(CommonRecord.dbName,MODE_PRIVATE,null);
		if(_listdatabase != null){				
			deleteData();
			insertData();					
		}	
		_listdatabase.close();
	}
	
	 public void deleteData(){
		 try {
			String sql = "delete from " + CommonRecord.TASK_LIST_TABLE_NAME;
			_listdatabase.execSQL(sql);
			Log.i("*******   xuming", sql);
		} catch (Exception e) {
			// TODO: handle exception
		}		
	 }
	 public void insertData(){
		String sql;
		try {
			JSONObject jsonObj = null;
			for (int i = 0; i < resultArray.length(); i++) {
				jsonObj = resultArray.getJSONObject(i);
				String status = (jsonObj.getString("Taskstatus").equals("0")) ? "1"
						: "0";
				sql = "insert into " + CommonRecord.TASK_LIST_TABLE_NAME
						+ CommonRecord.TASK_LIST_COLUMN_LIST + " values ("
						+ "'" + jsonObj.getString("Taskid") + "'" + "," + "'"
						+ jsonObj.getString("Pipeid") + "'" + "," + "'"
						+ jsonObj.getString("Planid") + "'" + "," + "'"
						+ jsonObj.getString("Terminalid") + "'" + "," + "'"
						+ jsonObj.getString("Userid") + "'" + ","
						+ jsonObj.getInt("Deptid") + "," + "'"
						+ jsonObj.getString("Taskname") + "'" + "," + "'"
						+ jsonObj.getString("Taskdescribe") + "'" + "," + "'"
						+ jsonObj.getString("Planstartdate") + "'" + "," + "'"
						+ jsonObj.getString("Planenddate") + "'" + ","

						+ "'" + jsonObj.getString("Taskstartposition") + "'"
						+ "," + "'" + jsonObj.getString("Taskendposition")
						+ "'" + "," + "'" + status + "'" + "," + "'"
						+ jsonObj.getString("Tasktypeid") + "'" + ","

						+ "'" + jsonObj.getString("Actualstartdate") + "'"
						+ "," + "'" + jsonObj.getString("Actualenddate") + "'"
						+ ")";
				_listdatabase.execSQL(sql);
				Log.i("*******   xuming", sql);
			}
			queryData();
		} catch (Exception e) {
			// TODO: handle exception
		}
		if(_listdatabase.isOpen())
	     _listdatabase.close();
	}
		
	 @SuppressWarnings("unused")
	private int queryCount(){
		
		 int count = 0 ;
		 Cursor cursor = _listdatabase.rawQuery("select count(*) from " + CommonRecord.TASK_LIST_TABLE_NAME, null);
         if (cursor.moveToNext()) {
                 boolean f = cursor.getLong(0) <= 0;
                 Log.e("f", "f = " + f);                 
                 count = (int) cursor.getLong(0);
         }
         cursor.close();
         return count;
	 }
	public void queryData(){
		Cursor cursor = _listdatabase.query(CommonRecord.TASK_LIST_TABLE_NAME,
				CommonRecord.FIELD_TASKLIST, null,null, null, null, null,null);
		String str="";
		
		if(cursor.getCount()!=0){
			cursor.moveToFirst();
			for(int i=0;i< cursor.getCount();i+=1){
				str = str+cursor.getString(0)+" "
					+ cursor.getString(1)+ " " 
					+ cursor.getString(2) + " " 
					+ cursor.getString(3) + " " 
					+ cursor.getString(4) + " "
					+ cursor.getInt(5) + " "
					+ cursor.getString(6) + " " //taskname
					+ cursor.getString(7) + " "
					+ cursor.getString(8) + " " //taskDescription
					+ cursor.getString(9) + " "
					+ cursor.getString(10) + " "
					+ cursor.getString(11) + " "
					+ cursor.getString(12) + " " //taskStatus
					+ cursor.getString(13) + " "
					+ cursor.getString(14) + " "
					+ cursor.getString(15) + " "
					+ "\n";
				Log.i("------   xuming", str);
				/*mData = new ArrayList<Map<String, Object>>();
				Map<String, Object> map = new HashMap<String, Object>();   
				map.put("taskName", cursor.getString(6));       
				
				String status = Converts.ConvertToTaskStatus(cursor.getString(12));
				map.put("taskState", status);
	        	map.put("img", R.drawable.bill); 
	        	map.put("taskDes",cursor.getString(7));
				mData.add(map);*/
				cursor.moveToNext();
			}
		}
		cursor.close();				
	}
	public void updateData(
				 String taskID, 
				 String taskStartDate, 
				 String taskEndDate, 
				 String taskStatus){
			 
			 String sql = "update "+ CommonRecord.TASK_LIST_TABLE_NAME +
					 " set " + 
					 " taskStartDate = " + taskStartDate + 
					 " taskEndDate = " + taskEndDate + 
					 " taskStatus = " + taskStatus + 
					 " where taskID=" + taskID;  
			 _listdatabase.execSQL(sql);
	}
	
	private long mExitTime;
	@Override
	public boolean onKeyDown(int keyCode,KeyEvent event){
		
		if(keyCode == KeyEvent.KEYCODE_BACK){
			if((System.currentTimeMillis() - mExitTime) > 2000){
				
				//Object mHelperUtils;
				Toast.makeText(this, getText(R.string.exit_message), Toast.LENGTH_LONG).show();
				mExitTime = System.currentTimeMillis();
			}
			else{
				//增加修改任务状态为挂起的代码
				finish();
			}
			return true;
		}
		return super.onKeyDown(keyCode, event);
	};
		 
		/*
		 * �����������ʾ�����б��adapter              
		 */
		public class MyAdapter extends BaseAdapter{    
			private LayoutInflater mInflater;      
			public MyAdapter(Context context){    
				this.mInflater = LayoutInflater.from(context);    
				}       
			@Override
			public int getCount() {   
				// TODO Auto-generated method stub        
				return mData.size();      
				}          
			@Override
			public Object getItem(int arg0) {       
				// TODO Auto-generated method stub        
				return null;        
				}        
			@Override
			public long getItemId(int arg0) { 
				// TODO Auto-generated method stub     
				return 0;       
				}         
			@Override
			public View getView(int position, View convertView, ViewGroup parent) {  
				ViewHolder holder = null;     
				if (convertView == null) {        
					holder=new ViewHolder();   
					convertView = mInflater.inflate(R.layout.listview_row, null);      
					holder.img = (ImageView)convertView.findViewById(R.id.img);   
					holder.taskName = (TextView)convertView.findViewById(R.id.taskName);  
					holder.taskState = (TextView)convertView.findViewById(R.id.taskState);
					holder.taskState.setVisibility(View.INVISIBLE);
					holder.taskDes = (TextView)convertView.findViewById(R.id.taskDes);
					holder.view_btn = (Button) convertView.findViewById(R.id.view_btn);
					
					//holder.img2 = (ImageView)convertView.findViewById(R.id.img2);  
					convertView.setTag(holder);    
					}else {                  
						holder = (ViewHolder)convertView.getTag();  
						}  
				//holder.view_btn.setImageResource(R.drawable.arrow);
				//holder.img.setBackgroundResource((Integer)mData.get(position).get("img"));      
				holder.taskName.setText((String)mData.get(position).get("taskName")); 
				//holder.taskState.setText((String)mData.get(position).get("taskState"));				
				String _status = (String)mData.get(position).get("taskState");
				holder.taskDes.setText((String)mData.get(position).get("taskDes"));
				holder.taskState.setText(Converts.ConvertToTaskStatus(_status));
				if(_status.equals("2")){//����Ѳ��
					holder.img.setImageResource(R.anim.animation);
					holder.draw = (AnimationDrawable)holder.img.getDrawable();
					
					if(holder.draw.isRunning()){
						holder.draw.stop();
					}
					holder.draw.start();
				}
				else if(_status.equals("3")){//��ͣ���¿�ʼ 
					holder.img.setImageResource(R.drawable.pausing);
				}
				else{
					holder.img.setImageResource(R.drawable.used);
				}
				final int _index = position;
				/*holder.view_btn.setEnabled(isEnable);
				
				if(status.equals("0") || status.equals("1")){
					//holder.view_btn.setText(Constants.TASK_LIST_START_CHECKING);
					
				}
				else if(status.equals("2")){//����Ѳ��
					//holder.view_btn.setText(Constants.TASK_LIST_RUNNING_CHECKING);					
					holder.view_btn.setEnabled(true);
				}
				else if(status.equals("3")){//��ͣ���¿�ʼ 
					//holder.view_btn.setText(Constants.TASK_LIST_RESTART_CHECKING);
					holder.view_btn.setEnabled(true);
				}*/
				holder.view_btn.setOnClickListener(new OnClickListener(){
					/*
					 * 
					 * �鿴������ϸ�����ݣ���ת��������ϸҳ��
					 * @see android.view.View.OnClickListener#onClick(android.view.View)
					 */
					@Override
					public void onClick(View arg0) {									
						Intent intent = new Intent();
						intent.setClass(TaskListView.this, TaskDetailView.class);
						Bundle bundle = new Bundle(); 		
				 		bundle.putSerializable("tasklist", (Serializable) mData.get(_index));
				 		intent.putExtras(bundle); 
				 		startActivity(intent);
						
					}});
				/*holder.view_btn.setOnTouchListener(new OnTouchListener(){

					@Override
					public boolean onTouch(View arg0, MotionEvent arg1) {
						// TODO Auto-generated method stub
						 if(arg1.getAction() == MotionEvent.ACTION_DOWN){   
			                 arg0.setBackgroundResource(R.color.button_clicked);   
			                 Log.i("TestAndroid Button", "MotionEvent.ACTION_DOWN");
			             }   
			             else if(arg1.getAction() == MotionEvent.ACTION_UP){   
			                 arg0.setBackgroundResource(R.color.button_press); 
			                 Log.i("TestAndroid Button", "MotionEvent.ACTION_UP");
			             } 

						return false;
					}
					
				});*/
				return convertView;      
				}             
		}
}
