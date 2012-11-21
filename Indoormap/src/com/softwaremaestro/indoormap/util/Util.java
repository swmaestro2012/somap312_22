package com.softwaremaestro.indoormap.util;

import java.io.File;
import java.util.ArrayList;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class Util {

	private Context con;
	private SQLiteDatabase db;
	private String dbpath;
	private String createTable1 = "create table indoormap (key INTEGER not null PRIMARY KEY AUTOINCREMENT,content TEXT);";

	Util(Context _con) {
		con = _con;
		dbpath=con.getApplicationContext().getDatabasePath("indoormap").getPath();
		File dbfile = new File(dbpath);
		if (dbfile.exists()) {
		} else {
			db = con.openOrCreateDatabase("indoormap.db", con.MODE_PRIVATE, null);
			db.execSQL(createTable1);
			db.close();
		}
	}
	
	public void insertData(String query){
		db = con.openOrCreateDatabase("indoormap.db", con.MODE_PRIVATE, null);
		db.execSQL(query);
		db.close();	
	}
	
	public void deleteData(String query){
		db = con.openOrCreateDatabase("indoormap.db", con.MODE_PRIVATE, null);
		db.execSQL(query);
		db.close();	
	}
	
	public ArrayList<String> selectData(String query){
		ArrayList<String> arr;
		arr = new ArrayList<String>();
		db = con.openOrCreateDatabase("indoormap.db", con.MODE_PRIVATE, null);
		Cursor cursor=db.rawQuery(query,null);
		while(cursor.moveToNext())
			arr.add(cursor.getString(0)+" : "+cursor.getString(1)+"\n");
		db.close();	
		return arr;
	}

}
