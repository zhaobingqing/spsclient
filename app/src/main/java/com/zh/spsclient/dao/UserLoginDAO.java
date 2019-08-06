package com.zh.spsclient.dao;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import com.zh.spsclient.common.CommonRecord;

public class UserLoginDAO {
	public SQLiteDatabase database = null;
	
	public UserLoginDAO(){
		
	}
	
	public UserLoginDAO(SQLiteDatabase database2) {
		// TODO Auto-generated constructor stub
		database = database2;
	}

	public void createTable(){		
		database.execSQL(CommonRecord.sqlCreateTable_user_login);
	}
	public void insertUserLoginData(String userid, String password, String authCode){
		String sql = "delete from "+ CommonRecord.USER_LOGIN_TABLE_NAME;  
		database.execSQL(sql);		
		if(queryCount(userid,password,authCode) == 0)
		{
			String sql_User = "insert into "+ CommonRecord.USER_LOGIN_TABLE_NAME
					+ CommonRecord.USER_LOGIN_COLUMN_LIST
					+ " values ("      
					+ "''" + ","
					+ "''" + ","
					+ "'" +  authCode + "'" + ","
					+ "'1'" + ","				
					+ "'1'" // 1:login; 0: logout
					+ ")";          				
			database.execSQL(sql_User);	
			queryData();
		}
		else{
		
		}
	}
	public void deleteUserLoginData(){
		String sql = "delete from "+ CommonRecord.USER_LOGIN_TABLE_NAME;  
		database.execSQL(sql);		
	}
	public void queryData(){
		Cursor cursor = database.query(CommonRecord.USER_LOGIN_TABLE_NAME,
				CommonRecord.FIELD_USER_LOGIN, null,null, null, null, null,null);
		String str="";
		
		if(cursor.getCount()!=0){
			cursor.moveToFirst();
			for(int i=0;i< cursor.getCount();i+=1){
				str=str+cursor.getString(0)+" "
					+ cursor.getString(1)+ " " 
					+ cursor.getString(2) + " " 
					+ cursor.getInt(3) + " " 
					+ cursor.getInt(4) + " " 
					+ "\n";				
				cursor.moveToNext();
			}
			Log.i("*******   xuming", str);
		}		
		cursor.close();
	}
	
	private int queryCount(String userid, String password, String authCode){
		
		 int count = 0 ;
		 Cursor cursor = database.rawQuery(CommonRecord.USER_LOGIN_QUERY_COUNT ,
				 new String[]{userid, password,authCode});
        
        if (cursor.moveToNext()) {
                //boolean f = cursor.getLong(0) <= 0;
                //Log.e("f", "f = " + f);                 
                count = (int) cursor.getLong(0);
        }
        cursor.close();
        return count;
	 }
	
	public void closeDB(){
		if(database.isOpen()){
			database.close();
		}
	}
}
