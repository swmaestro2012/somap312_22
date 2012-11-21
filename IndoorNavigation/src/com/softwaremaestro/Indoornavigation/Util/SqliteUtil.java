package com.softwaremaestro.Indoornavigation.Util;

import java.io.File;
import java.util.ArrayList;

import kr.softwaremaestro.indoor.wrm.vo.PointOfInterest;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class SqliteUtil {
	private Context con;
	private SQLiteDatabase db;
	private String dbpath;
	private String createTable = "create table recentSearchList (id INTEGER not null PRIMARY KEY,name TEXT,x DOUBLE,y DOUBLE,z DOUBLE);";

	public SqliteUtil(Context _con) {
		con = _con;
		dbpath=con.getApplicationContext().getDatabasePath("IndoorMap.db").getPath();
		File dbfile = new File(dbpath);
		if (dbfile.exists()) {
		} else {
			db = con.openOrCreateDatabase("IndoorMap.db", con.MODE_PRIVATE, null);
			db.execSQL(createTable);
			db.close();
		}
	}
	
	public void executeQuery(String query){
		db = con.openOrCreateDatabase("IndoorMap.db", con.MODE_PRIVATE, null);
		db.execSQL(query);
		db.close();	
	}
	public boolean isExist(int id){
		String query = "select * from recentSearchList where id="+id+";";
		db = con.openOrCreateDatabase("IndoorMap.db", con.MODE_PRIVATE, null);
		Cursor cursor=db.rawQuery(query,null);
		boolean result = false;
		if(cursor.moveToNext())
			result = true;
		db.close();	
		return result;
	}
	public ArrayList<PointOfInterest> selectRecentPOIData(){
		String query = "select * from recentSearchList;";
		ArrayList<PointOfInterest> result= new ArrayList<PointOfInterest>();
		db = con.openOrCreateDatabase("IndoorMap.db", con.MODE_PRIVATE, null);
		Cursor cursor=db.rawQuery(query,null);
		while(cursor.moveToNext()){
			result.add(new PointOfInterest(cursor.getInt(0),cursor.getDouble(2),cursor.getDouble(3),cursor.getDouble(4),cursor.getString(1)));
		}
		db.close();	
		return result;
	}
}
