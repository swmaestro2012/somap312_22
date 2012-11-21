package kr.softwaremaestro.indoor.wrm.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;

import kr.softwaremaestro.indoor.wrm.connection.DBManager;
import kr.softwaremaestro.indoor.wrm.vo.Accesspoint;


public class AccesspointDAO extends AbstractDAO<Accesspoint>{
	
	public AccesspointDAO(DBManager dbm) {
		super.dbm = dbm;
		super.tableName = "accesspoints";
	}

	@Override
	protected Accesspoint createVOFromResultSet(ResultSet rset)
			throws SQLException {
		Integer id = rset.getInt("id");
		String bssid = rset.getString("bssid");
		String ssid = rset.getString("ssid");
		String capabilities = rset.getString("capabilities");
		Integer frequency = rset.getInt("frequency");
		
		Accesspoint ap = new Accesspoint(id, bssid, ssid, capabilities, frequency);
		return ap;
	}

	@Override
	protected Accesspoint[] createVOsFromResultSet(ResultSet rset)
			throws SQLException {
		Accesspoint[] aps = null;
		Vector<Accesspoint> tempVector = new Vector<Accesspoint>();
		do {
			Integer id = rset.getInt("id");
			String bssid = rset.getString("bssid");
			String ssid = rset.getString("ssid");
			String capabilities = rset.getString("capabilities");
			Integer frequency = rset.getInt("frequency");
			Accesspoint ap = new Accesspoint(id, bssid, ssid, capabilities, frequency);
			tempVector.addElement(ap);
		} while(rset.next());
		aps = new Accesspoint[tempVector.size()];
		tempVector.toArray(aps);
		return aps;
	}

	@Override
	public int insert(Accesspoint obj) throws SQLException {
		int result = -1;
		PreparedStatement pstmt = null;
		try{
			Connection con = dbm.getConnection();
			String query = "insert into " + tableName + " values(NULL, ?, ?, ?, ?)";
			pstmt = con.prepareStatement(query);
//			pstmt.setInt(1, obj.getId());
			pstmt.setString(1, obj.getBssid());
			pstmt.setString(2, obj.getSsid());
			pstmt.setString(3, obj.getCapabilities());
			pstmt.setInt(4, obj.getFrequency());
			pstmt.executeUpdate();
			ResultSet rset= pstmt.executeQuery("select last_insert_id()");
			while (rset.next()) {
				result = rset.getInt(1);
			}			
			rset.close();
		}catch(SQLException e){
			throw e;
		}finally {			
			try {
				if (pstmt != null) {
					pstmt.close();
				}
			} catch (SQLException e) {
				throw e;
			}			
		}
		return result;
	}
}
