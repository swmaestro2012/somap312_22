package kr.softwaremaestro.indoor.wrm.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;

import android.util.Log;

import kr.softwaremaestro.indoor.wrm.connection.DBManager;
import kr.softwaremaestro.indoor.wrm.vo.ApSet;


public class ApSetDAO extends AbstractDAO<ApSet>{
	
	public ApSetDAO(DBManager dbm) {
		super.dbm = dbm;
		super.tableName = "ap_set";
	}

	@Override
	protected ApSet createVOFromResultSet(ResultSet rset)
			throws SQLException {
		
		Integer fingerprintId = rset.getInt("fingerprint_id");
		Integer apId = rset.getInt("ap_id");
		Integer signalStrength = rset.getInt("signal_strength");
		
		ApSet apSet = new ApSet(fingerprintId, apId, signalStrength);
		return apSet;
	}

	@Override
	protected ApSet[] createVOsFromResultSet(ResultSet rset)
			throws SQLException {
		ApSet[] apSets = null;
		Vector<ApSet> tempVector = new Vector<ApSet>();
		do {
			Integer fingerprintId = rset.getInt("fingerprint_id");
			Integer apId = rset.getInt("ap_id");
			Integer signalStrength = rset.getInt("signal_strength");
			
			ApSet apSet = new ApSet(fingerprintId, apId, signalStrength);
			tempVector.addElement(apSet);
		} while(rset.next());
		apSets = new ApSet[tempVector.size()];
		tempVector.toArray(apSets);
		return apSets;
	}

	@Override
	public int insert(ApSet obj) throws SQLException {
		int result = -1;
		PreparedStatement pstmt = null;
		try{
			Connection con = dbm.getConnection();
			String query = "insert into " + tableName + " values(?, ?, ?)";
			pstmt = con.prepareStatement(query);
			Log.d("obj fingerprintid", obj.getFingerprintId()+"");
			Log.d("obj apid", obj.getApId()+"");
			
			pstmt.setInt(1, obj.getFingerprintId());
			pstmt.setInt(2, obj.getApId());
			pstmt.setInt(3, obj.getSignalStrength());
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
