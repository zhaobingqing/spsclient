package com.zh.spsclient.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class MySqliteDBHelper extends SQLiteOpenHelper {

    /*private static final int VERSION = 1;*/
    
    private final String tag="SqliteHelper";

	public MySqliteDBHelper(Context context, String name,
			CursorFactory factory, int version) {
		super(context, name, factory, version);
		// TODO Auto-generated constructor stub
		
	}

	 

		@Override
	    public void onCreate(SQLiteDatabase db) {
	        Log.d(tag, "������һ��Sqlite��ݿ�");
	        db.execSQL("create table user" +
	                    "(" +
	                    "    id int," +
	                    "    name varchar(20)" +
	                    ")"
	        );
	    }

	    @Override
	    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
	        Log.d(tag, "Upgrade execute.");
	    }



}
