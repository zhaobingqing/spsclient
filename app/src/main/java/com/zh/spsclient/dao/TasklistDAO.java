package com.zh.spsclient.dao;

import com.zh.spsclient.common.CommonRecord;
import com.zh.spsclient.entity.TaskEntity;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class TasklistDAO {
	private SQLiteDatabase _database = null;
	public TasklistDAO(SQLiteDatabase db){
		_database = db;
	}
	public TasklistDAO(){
		
	}
	public void createTable(){		
		_database.execSQL(CommonRecord.sqlCreateTable_tasklist);
	}
	public void closeDB(){
		if(_database.isOpen()){
			_database.close();
		}
	}
	public void deleteData(){
		 try {
			String sql = "delete from " + CommonRecord.TASK_LIST_TABLE_NAME;
			_database.execSQL(sql);
			Log.i("*******   xuming", sql);
		} catch (Exception e) {
			// TODO: handle exception
		}		
	 }
	
	public void insertData(TaskEntity entity){
		String sql = "insert into " + CommonRecord.TASK_LIST_TABLE_NAME
				+ CommonRecord.TASK_LIST_COLUMN_LIST + " values ("
				+ "'" + entity.getTaskID() + "'" + "," + "'"
				+ entity.getPipeID() + "'" + "," + "'"
				+ entity.getPlanID() + "'" + "," + "'"
				+ entity.getTerminalID() + "'" + "," + "'"
				+ entity.getUserID() + "'" + ","
				+ entity.getDeptID() + "," + "'"
				+ entity.getTaskName() + "'" + "," + "'"
				+ entity.getTaskDesc() + "'" + "," + "'"
				+ entity.getPlanStartDate() + "'" + "," + "'"
				+ entity.getPlanEndDate() + "'" + ","
				+ "'" + entity.getTaskStartPos() + "'"
				+ "," + "'" + entity.getTaskEndPos()
				+ "'" + "," + "'" + entity.getTaskStatus() + "'" + "," + "'"
				+ entity.getTaskTypeID() + "'" + ","
				+ "'" + entity.getActualStartDate() + "'"
				+ "," + "'" + entity.getActualEndDate() + "'"
				+ ")";
		_database.execSQL(sql);
	}
	
	 @SuppressWarnings("unused")
	private int queryCount(){
			
		 int count = 0 ;
		 Cursor cursor = _database.rawQuery("select count(*) from " + CommonRecord.TASK_LIST_TABLE_NAME, null);
         
         if (cursor.moveToNext()) {
             boolean f = cursor.getLong(0) <= 0;
             Log.e("f", "f = " + f);                 
             count = (int) cursor.getLong(0);
         }
         cursor.close();
         return count;
	 }
	 public void queryData(){
		Cursor cursor = _database.query(CommonRecord.TASK_LIST_TABLE_NAME,
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
			 _database.execSQL(sql);
	}
}
