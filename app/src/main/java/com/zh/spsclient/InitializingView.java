package com.zh.spsclient;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.zh.spsclient.common.CommonRecord;
import com.zh.spsclient.common.WebServiceAdapter;
import com.zh.spsclient.dao.PipelineCoorDAO;
import com.zh.spsclient.dao.PipelineDAO;
import com.zh.spsclient.dao.RealCoorDAO;
import com.zh.spsclient.dao.TaskResultDAO;
import com.zh.spsclient.dao.TasklistDAO;
import com.zh.spsclient.dao.UserLoginDAO;
import com.zh.spsclient.entity.PipelineEntity;
import com.zh.spsclient.entity.TheoCoorEntity;
import com.zh.spsclient.util.Constants;

import android.app.Activity;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.WindowManager;

public class InitializingView extends Activity {
	String terminalid;
	//登录信息表		
	UserLoginDAO userLoginDAO;
	//管线坐标表
	PipelineCoorDAO pipelineCoorDAO;
	//管线表		
	PipelineDAO pipelineDAO;
	//坐标轨迹表
	RealCoorDAO realCoorDAO;
	TasklistDAO tasklistDAO;
	TheoCoorEntity theoCoorEntity;
	PipelineEntity pipelineEntity;
	TaskResultDAO taskResultDAO;
	private SQLiteDatabase _database = null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);//隐藏状态栏
		//设置布局资源
		this.setContentView(R.layout.initializing_view);
		try {
			new Thread(){
				@Override
				public void run()
				{        		
					try {
						WebServiceAdapter adapter = new WebServiceAdapter();
						terminalid = adapter.Init(Constants.WELCOME_VIEW_SIM_NUMBER);
						CommonRecord.authCode = terminalid;
						Log.i("terminalid",terminalid);
						//创建Message对象
						Message m = new Message();
						//将消息放到消息队列中
						initHandler.sendMessage(m);
					}      
					finally{			        			
					}
				}
			}.start();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	//执行接收到的消息，执行的顺序是按照队列进行，即先进先出
	Handler initHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			//显示logo界面
			initTable();
		}
	};
	
	/*
	 * 当初始化数据完成后，跳转到登录界面
	 */
	Handler transferHandler = new Handler(){
		@Override
		public void handleMessage(Message msg) {
			
			//实例化Intent
			Intent it=new Intent();
			it.setClass(InitializingView.this, LoginView.class);
			try {
				Thread.sleep(5000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			//启动Activity
			startActivity(it);
			InitializingView.this.finish();
		}		
	};	
	
	@Override
	protected void onStart(){		
		super.onStart();		
	}
	
	@Override
	protected void onStop(){
		
		super.onStop();		
	}
	/*
	 * ��������ҳ����������(non-Javadoc)
	 * @see android.support.v4.app.FragmentActivity#onResume()
	 */
	@Override
	protected void onResume() {
		super.onResume();	
		
	}
	
	@Override
	protected void onPause() {
		
		super.onPause();		
	}
	
	@Override
	protected void onDestroy() {
		
		super.onDestroy();
		if(_database != null){
			_database.close();
		}
	}
	/*
	 * 
	 */
	private void initTable(){
		_database = openOrCreateDatabase(CommonRecord.dbName,MODE_PRIVATE,null);
		if(_database != null){
			creatTable();
			insertUserLoginData();
			//insertPLCoordinateData();
			//insertPipeLineData();			
		}
		if(transferHandler != null){
			//创建Message对象
			Message m = new Message();
			//将消息放到消息队列中
			transferHandler.sendMessage(m);
		}
	}
	
	/*
	 * 创建嵌入式数据库，登录信息表，管线坐标表，管线表
	 */
	public void creatTable(){
		
		//登录信息表		
		userLoginDAO = new UserLoginDAO(_database);
		userLoginDAO.createTable();
		//管线坐标表
		pipelineCoorDAO = new PipelineCoorDAO(_database);
		pipelineCoorDAO.createTable();
		//管线表		
		pipelineDAO = new PipelineDAO(_database);
		pipelineDAO.createTable();
		//现场坐标轨迹表
		realCoorDAO = new RealCoorDAO(_database);
		realCoorDAO.createTable();		
		tasklistDAO = new TasklistDAO(_database);
		tasklistDAO.createTable();
		taskResultDAO = new TaskResultDAO(_database);
		taskResultDAO.createTable();
		/*new AlertDialog.Builder(InitializingView.this).setTitle("Message")
		.setMessage(tables).setNegativeButton("确定",
			new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog,int which) {
				}
		}).show();*/
	}
	
	public void insertUserLoginData(){
		//userLoginDAO.deleteUserLoginData();		
		userLoginDAO.insertUserLoginData("", "", terminalid);
		//userLoginDAO.queryData();
		
	}
	
	public void insertPLCoordinateData(){
		new Thread(){
        	@Override
	    	public void run()
	    	{ 
        		pipelineCoorDAO.deletePLCOORData();
				
        		WebServiceAdapter adapter = new WebServiceAdapter();
        		String result = "";
        		result = adapter.getPLCoordinate(1);
        		//result = "{\"Coorid\":\"1449\",\"Pipeid\":\"38\",\"Coorx\":\"108.985123\",\"Coory\":\"38.401\",\"Height\":3264,\"Depth\":2,\"Interval\":33,\"Terrain\":\"低洼\"}";
        		try {
        			JSONArray resultArray = new JSONArray(result);
        			JSONObject jsonObj = null;
        			for(int i = 0; i < resultArray.length();i++){
        				jsonObj = resultArray.getJSONObject(i); 
        				
        				theoCoorEntity = new TheoCoorEntity();
        				theoCoorEntity.setCoorid(jsonObj.getString("Coorid"));
        				theoCoorEntity.setPipeid(jsonObj.getString("Pipeid"));
        				theoCoorEntity.setCoorx(jsonObj.getString("Coorx"));
        				theoCoorEntity.setCoory(jsonObj.getString("Coory"));
        				theoCoorEntity.setHeight(jsonObj.getDouble("Height"));
        				theoCoorEntity.setDepth(jsonObj.getDouble("Depth"));
        				theoCoorEntity.setInterval(jsonObj.getDouble("Interval"));
        				theoCoorEntity.setTerrain(jsonObj.getString("Terrain"));
        				pipelineCoorDAO.insertPLCOORData(theoCoorEntity);
        				pipelineCoorDAO.queryData();        				
        				
        			} 
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}       
        		
        		pipelineDAO.deletePipelineData();
        		//WebServiceAdapter adapter = new WebServiceAdapter();
        		result = adapter.getPipeline();
        		try {
        			JSONArray resultArray = new JSONArray(result);
        			JSONObject jsonObj = null;
        			for(int i = 0; i < resultArray.length();i++){
        				jsonObj = resultArray.getJSONObject(i); 
        				
        				pipelineEntity = new PipelineEntity();
        				pipelineEntity.setPipeid(jsonObj.getString("PipeID"));
        				pipelineEntity.setPipeCode(jsonObj.getString("PipeCode"));
        				pipelineEntity.setPipeName(jsonObj.getString("PipeName"));
        				pipelineEntity.setPipeStandard(jsonObj.getString("PipeStandard"));
        				pipelineEntity.setPipeLength(jsonObj.getDouble("PipeLength"));
        				pipelineEntity.setPipeStart(jsonObj.getString("PipeStart"));
        				pipelineEntity.setPipeEnd(jsonObj.getString("PipeEnd"));
        				pipelineEntity.setPipePress(jsonObj.getDouble("PipePress"));
        				pipelineEntity.setPipeFlow(jsonObj.getDouble("PipeFlow"));
        				pipelineEntity.setPipeDate(jsonObj.getString("PipeDate"));
        				pipelineEntity.setPipeRoom(jsonObj.getString("PipeRoom"));
        				pipelineEntity.setPipeStatus(jsonObj.getInt("PipeStatus"));
        				pipelineEntity.setCoorType(jsonObj.getString("CoorType"));
        				pipelineEntity.setCoorEdition(jsonObj.getString("CoorEdition"));
        				pipelineDAO.insertPipelineData(pipelineEntity);  
        			}
        			pipelineDAO.queryData();
        			//pipelineDAO.closeDB();
        							
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}    
        		
	    	}
        }.start();
        
	}
	
	/*public void insertPipeLineData(){
		new Thread(){
        	@Override
	    	public void run()
	    	{ 
        		pipelineDAO.deletePipelineData();
        		WebServiceAdapter adapter = new WebServiceAdapter();
        		String result = adapter.getPipeline();
        		try {
        			JSONArray resultArray = new JSONArray(result);
        			JSONObject jsonObj = null;
        			for(int i = 0; i < resultArray.length();i++){
        				jsonObj = resultArray.getJSONObject(i); 
        				
        				pipelineEntity = new PipelineEntity();
        				pipelineEntity.setPipeid(jsonObj.getString("PipeID"));
        				pipelineEntity.setPipeCode(jsonObj.getString("PipeCode"));
        				pipelineEntity.setPipeName(jsonObj.getString("PipeName"));
        				pipelineEntity.setPipeStandard(jsonObj.getString("PipeStandard"));
        				pipelineEntity.setPipeLength(jsonObj.getDouble("PipeLength"));
        				pipelineEntity.setPipeStart(jsonObj.getString("PipeStart"));
        				pipelineEntity.setPipeEnd(jsonObj.getString("PipeEnd"));
        				pipelineEntity.setPipePress(jsonObj.getDouble("PipePress"));
        				pipelineEntity.setPipeFlow(jsonObj.getDouble("PipeFlow"));
        				pipelineEntity.setPipeDate(jsonObj.getString("PipeDate"));
        				pipelineEntity.setPipeRoom(jsonObj.getString("PipeRoom"));
        				pipelineEntity.setPipeStatus(jsonObj.getInt("PipeStatus"));
        				pipelineEntity.setCoorType(jsonObj.getString("CoorType"));
        				pipelineEntity.setCoorEdition(jsonObj.getString("CoorEdition"));
        				pipelineDAO.insertPipelineData(pipelineEntity);  
        			}
        			pipelineDAO.queryData();
        			//pipelineDAO.closeDB();
					
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}        		
	    	}
        }.start();
	}*/
	
}
