package com.zh.spsclient.dao;

import java.util.ArrayList;
import java.util.List;

import com.zh.spsclient.common.CommonRecord;
import com.zh.spsclient.entity.RealCoorDTO;
import com.zh.spsclient.entity.RealCoorEntity;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;



public class RealCoorDAO {
	public SQLiteDatabase database = null;
	public RealCoorDAO(){
		
	}
	public RealCoorDAO(SQLiteDatabase database){
		this.database = database;		
	}
	
	public void createTable(){
		database.execSQL(CommonRecord.sqlCreateTable_Real_Coordinate);				
	}
	
	
	public void closeDB(){
		if(database.isOpen()){
			database.close();
		}
	}
	public void deleteData(){
		String sql = "delete from "+ CommonRecord.REAL_COORDINATE_TABLE_NAME;  
		database.execSQL(sql);	
	}
	
	public void deleteData(String taskID){
		String sql = "delete from "+ CommonRecord.REAL_COORDINATE_TABLE_NAME + " where taskID="+taskID;  
		database.execSQL(sql);	
	}
	public void queryData(){
		Cursor cursor = database.query(CommonRecord.REAL_COORDINATE_TABLE_NAME, 
				CommonRecord.FIELD_REAL_COORDINATE, null,null, null, null, null,null);
		String str="";
		
		if(cursor.getCount()!=0){
			cursor.moveToFirst();
			for(int i=0;i< 1;i+=1){
				str=str+cursor.getInt(0)+" "
					+ cursor.getString(1)+ " " 
					+ cursor.getString(2) + " " 
					+ cursor.getString(3) + " " 
					+ "\n";
				//Log.i("*******   xuming", str);
				cursor.moveToNext();
			}
		}		
		cursor.close();
	}

	public List<RealCoorDTO> queryData(String taskID,String terminalID){
		String selection="taskID = ? and terminalID = ?";
		String[] selectionArgs={taskID,terminalID};
		Cursor cursor = database.query(CommonRecord.REAL_COORDINATE_TABLE_NAME, 
				CommonRecord.FIELD_REAL_COORDINATE, selection,selectionArgs, null, null, null,null);
		List<RealCoorDTO> aRealCoorDTO = new ArrayList<RealCoorDTO>();
		List<String> LonLats=new ArrayList<String>();
		RealCoorDTO realCoorDTO;
		if(cursor.getCount()!=0){
			cursor.moveToFirst();
			
			for(int i=0;i< cursor.getCount();i+=1){
				String lonlat=cursor.getString(3)+","+cursor.getString(4);
				if(!LonLats.contains(lonlat))
				{
					LonLats.add(lonlat);
					realCoorDTO = new RealCoorDTO();
					realCoorDTO.setCoorID(cursor.getInt(0));//ID
					realCoorDTO.setGeoLat(cursor.getString(3));//locLat
					realCoorDTO.setGeoLng(cursor.getString(4));//locLng
					aRealCoorDTO.add(realCoorDTO);
				}
				cursor.moveToNext();
			}
		}		
		cursor.close();
		return aRealCoorDTO;
	}
	
	public void insertData(RealCoorEntity realCoorEntity){
		try {
			String	sql = "insert into "+ CommonRecord.REAL_COORDINATE_TABLE_NAME
					+ CommonRecord.REAL_COORDINATE_COLUMN_LIST
					+ " values ("      
					+ realCoorEntity.getId() + ","
					+ "'" + realCoorEntity.getTaskID() + "'" + ","
					+ "'" + realCoorEntity.getTerminalID() + "'" + ","
					+ realCoorEntity.getGeoLat() + ","
					+ realCoorEntity.getGeoLng() + ","
					+ "'" +  realCoorEntity.getLocProvider() + "'" + ","
					+ "'" +  realCoorEntity.getCurrentTime() + "'" + ","
					+ "'" + realCoorEntity.getIfUpLoad() + "'"
					+ ")";  
			database.execSQL(sql);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
		
	}
	
	/*
	 * 获取末上传的坐标点信心
	 */
	public List<RealCoorDTO> queryNoUpData(String taskID,String terminalID){
		String selection="taskID = ? and terminalID = ? and coorUpLoad= ? ";
		String[] selectionArgs={taskID,terminalID,"0"};
		Cursor cursor = database.query(CommonRecord.REAL_COORDINATE_TABLE_NAME, 
				CommonRecord.FIELD_REAL_COORDINATE, selection,selectionArgs, null, null, "curDate",null);
		/*String str="";*/
		List<RealCoorDTO> aRealCoorDTO = new ArrayList<RealCoorDTO>();
		List<String> LonLats=new ArrayList<String>();
		RealCoorDTO realCoorDTO;
		if(cursor.getCount()!=0){
			cursor.moveToFirst();
			
			for(int i=0;i< cursor.getCount();i+=1){
				String lonlat=cursor.getString(3)+","+cursor.getString(4);
				if(!LonLats.contains(lonlat))
				{
					LonLats.add(lonlat);
					/*str=str+cursor.getInt(0)+" " // ID
							+ cursor.getString(1)+ " " 
							+ cursor.getString(2) + " " 
							+ cursor.getString(3) + " " //locLat
							+ cursor.getString(4) + " " //locLng
							+ "\n";*/
					realCoorDTO = new RealCoorDTO();
					realCoorDTO.setCoorID(cursor.getInt(0));//ID
					realCoorDTO.setGeoLat(cursor.getString(3));//locLat
					realCoorDTO.setGeoLng(cursor.getString(4));//locLng
					realCoorDTO.setLocProvider(cursor.getString(5));//locLng
					realCoorDTO.setCurrentTime(cursor.getString(6));//locLng
					aRealCoorDTO.add(realCoorDTO);
				}
				cursor.moveToNext();
			}
		}		
		cursor.close();
		return aRealCoorDTO;
	}
	/*
	 * 更改上传状态
	 */
	public void updateIfUpLoad(String locLat,String locLng)
	{
		String selection="locLat = ? and locLng = ? ";
		String[] selectionArgs={locLat,locLng};
		ContentValues cv = new ContentValues();
		cv.put("coorUpLoad", "1");
		database.update(CommonRecord.REAL_COORDINATE_TABLE_NAME, cv, selection,selectionArgs);
	}
}
