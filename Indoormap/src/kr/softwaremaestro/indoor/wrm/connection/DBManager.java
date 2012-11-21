package kr.softwaremaestro.indoor.wrm.connection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;



public class DBManager implements DBInfo{
	private String host;
	private String port;
	private String dbname;
	private String user;
	private String pass;
	private String url;
	private Connection con;
	//private Handler mHandler;
	public DBManager() throws SQLException{
		/*
		mHandler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				Log.d("DBManager:Handler", "connection is received");
				if (msg.obj != null) {
					Log.d("DBManager:Handler", "msg.obj is not null");
					DBManager.con = (Connection)msg.obj;
				}
			}
		};
		*/
		host = HOST;
		port = PORT;
		dbname = DBNAME;
		user = USER;
		pass = PASS;
		connect();
	}
	
	public DBManager(String host, String port, String dbname, String user, String pass) throws SQLException{
		/*
		mHandler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				if (msg.obj != null) {
					DBManager.con = (Connection)msg.obj;
					Log.d("DBManager", "connection is received");
					
				}
			}
		};
		*/
		this.host = host;
		this.port = port;
		this.dbname = dbname;
		this.user = user;
		this.pass = pass;
		connect();
	}
	public void connect() {
		try {
			if(con == null || con.isClosed()){
				url = "jdbc:mysql://" + host + ":" + port + "/" + dbname + "?blobSendChunkSize=20971520" + 
				"&useUnicode=true&characterEncoding=utf8&autoReconnect=true"; 
				try {
					try {
						Class.forName("com.mysql.jdbc.Driver").newInstance();
					} catch (InstantiationException e) {
						e.printStackTrace();
					} catch (IllegalAccessException e) {
						e.printStackTrace();
					}
				} catch (ClassNotFoundException e) {
					e.printStackTrace();
				}
				ConnectionThread conThread = new ConnectionThread(this, url, user, pass);
				Log.d("DBManager", "new connection is making");
				conThread.start();
				conThread.join();
				Log.d("DBManager", "new connection is created");
				/*
				Log.d("DBManager", "finish joining");
				Log.d("DBManager", "before handlerThead");
				HandlerThread handlerThread = new HandlerThread(mHandler);
				handlerThread.start();
				Log.d("DBManager", "after handlerThead");
				Log.d("DBManager", "join handlerThead");
				handlerThread.join();
				if(mHandler.hasMessages(0, con) != true) {
					Log.d("DBManager", "start sleeping");
					Thread.sleep(1000);
					Log.d("DBManager", "end sleeping");
				}
				else {
					Message msg = mHandler.obtainMessage();
					if (msg == null) {
						Log.d("msg", "msg is null");
					}
					Log.d("msg", msg.obj.toString());
					if (msg.obj instanceof java.sql.Connection) {
						Log.d("DBManager", "wow");
					}
					Log.d("DBManager", "hasssssssss");
				}
				*/
			}
			else {
				Log.d("DBManager", "connect() : connection already exists");
			}
			if (con == null)
				Log.d("DBManager", "connect() : fail to make connection");
			else
				Log.d("DBManager", "connect() : connection is made successfully");
		} catch(SQLException e) {
			e.printStackTrace();
		} catch(InterruptedException e) {
			e.printStackTrace();
		}
		
		/*
		catch (Exception e) {
			Log.d("DBManager", "connect() : fail to make connection");
			e.printStackTrace();
		}
		*/
	}
	/*
	public void connect() throws SQLException{
		if(con == null || con.isClosed()){
			url = "jdbc:mysql://" + host + ":" + port + "/" + dbname + "?blobSendChunkSize=20971520" + 
			"&useUnicode=true&characterEncoding=utf8&autoReconnect=true"; 
			Log.d("url", url);
			try {
				try {
					Class.forName("com.mysql.jdbc.Driver").newInstance();
				} catch (InstantiationException e) {
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				}
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
			con = DriverManager.getConnection(url, user, pass);
			if (con == null) {
				Log.d("connection", "test");
			}
			else {
				Log.d("test", "test");
			}
		}
	}
	*/
	/*
	public Connection getConnection(){
		try{
			if(con == null || con.isClosed()){
				con = DriverManager.getConnection(url, user, pass);
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return con;
	}
	*/
	public Connection getConnection(){
			try {
				if(con == null || con.isClosed()){
					ConnectionThread conThread = new ConnectionThread(this, url, user, pass);
					Log.d("DBManager", "new connection is making");
					conThread.start();
					conThread.join();
					Log.d("DBManager", "new connection is created");
				}
				else {
					Log.d("DBManager", "getConnection() : connection already exists");
				}
			} catch (SQLException e) {
				e.printStackTrace();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			return con;
			
	}
	
	public void closeConnection() {
		try {
			if(con != null && !con.isClosed()) {
				con.close();
			}
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	public void setConnection(Connection con) {
		this.con = con;
	}
	
	public String getDBHost(){
		try {
			if(con == null || con.isClosed()){
				return null;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return this.host;
	}

	public String getDBPort(){
		try {
			if(con == null || con.isClosed()){
				return null;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return this.port;
	}

	public String getDBUser(){
		try {
			if(con == null || con.isClosed()){
				return null;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return this.user;
	}

	public String getDBName(){
		try {
			if(con == null || con.isClosed()){
				return null;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return this.dbname;
	}

	public String getDBPass(){
		try {
			if(con == null || con.isClosed()){
				return null;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return this.pass;
	}
	
	class ConnectionThread extends Thread {
		private Connection con;
		private String url;
		private String user;
		private String pass;
		private DBManager dbm;
		//private Handler mHandler;
		//private volatile Looper mMyLooper;
		public ConnectionThread(DBManager dbm, String url, String user, String pass) {
			this.url = url;
			this.user = user;
			this.pass = pass;
			this.dbm = dbm;
		}
		@Override
		public void run() {
				try {
					Log.d("DBManager:ConnectionThead", "before getting connection");
					con = DriverManager.getConnection(url, user, pass);
					Log.d("DBManager:ConnectionThead", "after getting connection");
					dbm.setConnection(con);
					Log.d("DBManager:ConnectionThead", "set connection");
				} catch (SQLException e) {
					e.printStackTrace();
				}
				/*
				Log.d("mhandler", mHandler.toString());
				Message msg = mHandler.obtainMessage();
				msg.what = 100;
				msg.obj = con;
				Log.d("...", msg.what+"");
				Log.d("...", msg.obj+"");
				Log.d("...", msg.obj.getClass().getName()+"");
				mHandler.sendMessageAtFrontOfQueue(msg);
				Log.d("DBManager:ConnectionThread", "ConnectionThread sends connection to the Handler");
				*/
				try {
					Log.d("DBManager:ConnectionThread", "start sleeping");
					Thread.sleep(2000);
					Log.d("DBManager:ConnectionThread", "finish sleeping");
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			
		}
	}
	
	class HandlerThread extends Thread {
		private Handler handler;
		public HandlerThread(Handler handler) {
			this.handler = handler;
		}
		@Override
		public void run() {
			Message hmsg = null;
			/*
			while((hmsg = handler.obtainMessage(100)).obj == null || hmsg.obj.getClass().getName().compareTo("com.mysql.jdbc.Connection") != 0) {
				try {
					Log.d("DBManager:HandlerThread", "start sleeping");
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
			if (hmsg == null) {
				Log.d("asf", "asdf");
			}
			else {
				Log.d("asdf0", hmsg.toString() +"");
				Log.d("asdf0", hmsg.what +"");
				Log.d("asdf1", hmsg.arg1 +", "test");
			}
		}
	}
	*/
	/*
	public Connection getConnection(){
		try{
			if(con == null || con.isClosed()){
				con = DriverManager.getConnection(url, user, pass);
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return con;
	}
	*/
	public Connection getConnection(){
			try {
	