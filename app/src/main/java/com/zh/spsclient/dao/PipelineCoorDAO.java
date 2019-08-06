package com.zh.spsclient.dao;

import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.zh.spsclient.common.CommonRecord;
import com.zh.spsclient.entity.TheoCoorEntity;

public class PipelineCoorDAO {
	private SQLiteDatabase database = null;
	public PipelineCoorDAO(SQLiteDatabase database2){
		database = database2;
	}
	
	public void createTable(){		
		try {
			database.execSQL(CommonRecord.sqlCreateTable_Coordinate);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
		/*cursor = database.query("sqlite_master", new String[]{"name"}, "type = ?",new String[]{"table"}, null, null, null,null);
		cursor.close();*/
	}
	public void deletePLCOORData(){
		try {
			String sql = "";
			sql = "delete from "+ CommonRecord.PIPELINE_COORDINATE_TABLE_NAME;  
			database.execSQL(sql);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}			
	}
	
	public void insertPLCOORData(
			String coorid,
			String pipeid,
			String coorx,
			String coory,
			double height,
			double depth,
			double interval,
			String terrain){
		String sql = "insert into "+ CommonRecord.PIPELINE_COORDINATE_TABLE_NAME 
				+ "(coorNumber,pipeID,locLng,locLat,height,depth,interval,terrain)"
				+ " values ("      
				+ "'" + coorid + "'" + ","
				+ "'" + pipeid + "'" + ","
				+ "'" + coorx  + "'" + ","
				+ "'" + coory  + "'" + ","
				+ height + ","
				+ depth + ","
				+ interval + ","        						
				+ "'" + terrain  + "'"
				+ ")";          				
		database.execSQL(sql);	
	}
	public void insertPLCOORData(TheoCoorEntity entity){
		String sql = "insert into "+ CommonRecord.PIPELINE_COORDINATE_TABLE_NAME 
				+ "(coorNumber,pipeID,locLng,locLat,height,depth,interval,terrain)"
				+ " values ("      
				+ "'" + entity.getCoorid() + "'" + ","
				+ "'" + entity.getPipeid() + "'" + ","
				+ "'" + entity.getCoorx()  + "'" + ","
				+ "'" + entity.getCoory()  + "'" + ","
				+ entity.getHeight() + ","
				+ entity.getDepth() + ","
				+ entity.getInterval() + ","        						
				+ "'" + entity.getTerrain()  + "'"
				+ ")";          				
		database.execSQL(sql);	
	}
	public void queryData(){
		Cursor cursor = database.query(CommonRecord.PIPELINE_COORDINATE_TABLE_NAME, 
				CommonRecord.FIELD_COORDINATE, null,null, null, null, null,null);
		String str="";
		
		if(cursor.getCount()!=0){
			cursor.moveToLast();
			for(int i=0;i< 1;i+=1){
				str=str+cursor.getString(0)+" "
					+ cursor.getString(1)+ " " 
					+ cursor.getString(2) + " " 
					+ cursor.getString(3) + " " 
					+ "\n";				
				cursor.moveToNext();
			}
			Log.i("*******   xuming", str);
		}	
		cursor.close();
	}
	public String[] queryPLCOORData(){
		String[] coors = null;
		if(database == null){
			
		}	
		else{
			Cursor cursor = database.query(CommonRecord.PIPELINE_COORDINATE_TABLE_NAME, 
					CommonRecord.FIELD_COORDINATE, null,null, null, null, null,null);
			int n = cursor.getCount();			
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
		return coors;
	}
	public void closeDB(){
		if(database.isOpen()){
			database.close();
		}
	}
}
