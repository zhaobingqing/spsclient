package com.zh.spsclient.dao;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.zh.spsclient.common.CommonRecord;
import com.zh.spsclient.entity.PipelineEntity;

public class PipelineDAO {
	public SQLiteDatabase database = null;
	
	public PipelineDAO(SQLiteDatabase database2) {
		// TODO Auto-generated constructor stub
		database = database2;
	}


	public void createTable(){
		//���߱�
		
		database.execSQL(CommonRecord.sqlCreateTable_pipeline);
		/*cursor = database.query("sqlite_master", new String[]{"name"}, "type = ?",new String[]{"table"}, null, null, null,null);
		cursor.close();*/
	}
	
	public void deletePipelineData(){
		String sql = "delete from "+ CommonRecord.PIPELINE_TABLE_NAME; 
		database.execSQL(sql);
	}
	
	public void insertPipelineData(PipelineEntity entity){
		String sql = "insert into "+ CommonRecord.PIPELINE_TABLE_NAME + 
				" values ("      
				+ "'" + entity.getPipeid()  + "'" + ","
				+ "'" + entity.getPipeCode() + "'"  + ","
				+ "'" + entity.getPipeName() + "'" + ","
				+ "'" + entity.getPipeStandard() + "'" + ","
				+ entity.getPipeLength() + ","
				+ "'" + entity.getPipeStart() + "'" + ","
				+ "'" + entity.getPipeEnd() + "'" + ","
				+ entity.getPipePress() + ","
				+ entity.getPipeFlow() + ","
				+ "'" + entity.getPipeDate() + "'" + ","
				+ "'" + entity.getPipeRoom() + "'" + ","
				+ entity.getPipeStatus() + ","
				+ "'" + entity.getCoorType() + "'" + ","
				+ "'" + entity.getCoorEdition()+ "'" 
				+ ")";  
		database.execSQL(sql);				
	}
	
	public void queryData(){
		Cursor cursor = database.query(CommonRecord.PIPELINE_TABLE_NAME,
				CommonRecord.FIELD_PIPELINE, null,null, null, null, null,null);
		String str="";
		
		if(cursor.getCount()!=0){
			cursor.moveToFirst();
			for(int i=0;i< 2;i+=1){
				str=str+cursor.getString(0)+" "
					+ cursor.getString(1)+ " " 
					+ cursor.getString(2) + " " 
					+ cursor.getString(3) + " " 
					+ "\n";
				Log.i("*******   xuming", str);
				cursor.moveToNext();
			}
		}	
	}
	
	public void closeDB(){
		if(database.isOpen()){
			database.close();
		}
	}
}
