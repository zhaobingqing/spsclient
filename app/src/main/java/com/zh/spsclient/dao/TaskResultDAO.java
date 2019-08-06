package com.zh.spsclient.dao;

import java.util.ArrayList;
import java.util.List;

import com.zh.spsclient.common.CommonRecord;
import com.zh.spsclient.entity.TaskResultEntity;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class TaskResultDAO {
	public SQLiteDatabase database = null;
	public TaskResultDAO(){
		
	}
	public TaskResultDAO(SQLiteDatabase database){
		this.database = database;
	}
	public void createTable(){		
		database.execSQL(CommonRecord.sqlCreateTable_task_result);
	}
	public void insertTable(TaskResultEntity entity){
		String sql_User = "insert into "+ CommonRecord.TASK_RESULT_TABLE_NAME
				+ CommonRecord.TASK_RESULT_COLUMN_LIST
				+ " values ("      
				+ "'" + entity.getTaskResultID()+ "'" + ","
				+ "'" + entity.getTaskID()+ "'" + ","
				+ "'" + entity.getTerminalID()+ "'" + ","
				+ "'" + entity.getCoordinates()+ "'" + ","	
				+ "'" + entity.getContents()+ "'" + ","
				+ "''" + ","
				+ "''" + ","
				+ "'" + entity.getCoorphoto()+ "'" + ","
				+ "'" + entity.getFeedBackPerson()+ "'" + ","
				+ "'" + entity.getFeedbackDate()+ "'" + ","
				+ "'" + entity.getIfUpLoad()+ "'" 
				+ ")";          				
		database.execSQL(sql_User);	
	}
	public void deleteData(){
		String sql = "delete from "+ CommonRecord.TASK_RESULT_TABLE_NAME; 
		database.execSQL(sql);
	}
	public void queryData(){
		Cursor cursor = database.query(CommonRecord.TASK_RESULT_TABLE_NAME,
				CommonRecord.FIELD_TASK_RESULT, null,null, null, null, null,null);
		String str="";
		
		if(cursor.getCount()!=0){
			cursor.moveToFirst();
			for(int i=0;i< cursor.getCount();i+=1){
				str=str+cursor.getString(0)+" "
					+ cursor.getString(1)+ " " 
					+ cursor.getString(2) + " " 
					+ cursor.getString(3) + " " 
					+ cursor.getString(4) + " " 
					+ cursor.getString(9) + " " 
					+ "\n";
				
				cursor.moveToNext();
			}
		}	
	}
	
	public List<TaskResultEntity> retrieveData(){
		List<TaskResultEntity> list = new ArrayList<TaskResultEntity>();
		TaskResultEntity entity = null;
		Cursor cursor = database.query(CommonRecord.TASK_RESULT_TABLE_NAME,
				CommonRecord.FIELD_TASK_RESULT, null,null, null, null, null,null);
		/*String str="";*/
		int n = cursor.getCount();
		if(n != 0){
			cursor.moveToFirst();
			for(int i=0;i < n ;i+=1){
				
				entity = new TaskResultEntity();
				entity.setTaskResultID(cursor.getString(0));
				entity.setTaskID(cursor.getString(1));
				entity.setTerminalID(cursor.getString(2));
				entity.setCoordinates(cursor.getString(3));
				entity.setContents(cursor.getString(4));
				entity.setFeedbackDate(cursor.getString(5));
				list.add(entity);
				cursor.moveToNext();
			}
		}
		return list;
	}
	
	/*
	 * 获取末上传的坐标点信心
	 */
	public List<TaskResultEntity> queryNoUpData(String taskID,String terminalID){
		String selection="TaskID = ? and TerminalID = ? and FeedBackUpLoad= ? ";
		String[] selectionArgs={taskID,terminalID,"0"};
		Cursor cursor = database.query(CommonRecord.TASK_RESULT_TABLE_NAME,
				CommonRecord.FIELD_TASK_RESULT, selection,selectionArgs, null, null, null,null);
		List<TaskResultEntity> list = new ArrayList<TaskResultEntity>();
		TaskResultEntity entity = null;
		
		int n = cursor.getCount();
		if(n != 0){
			cursor.moveToFirst();
			for(int i=0;i < n ;i+=1){
				
				entity = new TaskResultEntity();
				entity.setTaskResultID(cursor.getString(0));
				entity.setTaskID(cursor.getString(1));
				entity.setTerminalID(cursor.getString(2));
				entity.setCoordinates(cursor.getString(3));
				entity.setContents(cursor.getString(4));
				entity.setCoorphoto(cursor.getString(7));
				entity.setFeedBackPerson(cursor.getString(8));
				entity.setFeedbackDate(cursor.getString(9));
				list.add(entity);
				cursor.moveToNext();
			}
		}
		return list;
	}
	
	/*
	 * 更改上传状态
	 */
	public void updateIfUpLoad(String feedBackCOOR)
	{
		String selection="FeedBackCOOR = ? ";
		String[] selectionArgs={feedBackCOOR};
		ContentValues cv = new ContentValues();
		cv.put("FeedBackUpLoad", "1");
		database.update(CommonRecord.TASK_RESULT_TABLE_NAME, cv, selection,selectionArgs);
	}
	
	
	public void closeDB(){
		if(database.isOpen()){
			database.close();
		}
	}
}
