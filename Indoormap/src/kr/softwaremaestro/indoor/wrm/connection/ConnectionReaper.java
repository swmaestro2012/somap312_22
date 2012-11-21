package kr.softwaremaestro.indoor.wrm.connection;


/**
 * To prevent connection lost by idle time or unexpected interrupt<p>
 * re-establish connection frequently by {@link kr.ac.kaist.isilab.db.config.DBInfo#DELAY DELAY}
 * @author torajim@kaist.ac.kr
 */
public class ConnectionReaper extends Thread{
	private DBManager dbm;
	
	public ConnectionReaper(DBManager dbm){
		this.dbm = dbm;
	}
	
	public void run(){
		while(true){
			try{
				sleep(DBInfo.DELAY);
			}catch(InterruptedException e){
				try {
					dbm.connect();
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} 
			}
		}
	}
}
