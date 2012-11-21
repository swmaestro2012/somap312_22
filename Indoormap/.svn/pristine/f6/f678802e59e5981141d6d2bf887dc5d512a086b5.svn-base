package kr.softwaremaestro.indoor.wrm.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Vector;

import kr.softwaremaestro.indoor.wrm.connection.DBManager;
import kr.softwaremaestro.indoor.wrm.vo.Fingerprint;


public class FingerprintDAO extends AbstractDAO<Fingerprint>{
	
	public FingerprintDAO(DBManager dbm) {
		super.dbm = dbm;
		super.tableName = "fingerprints";
	}
	
	@Override
	protected Fingerprint createVOFromResultSet(ResultSet rset)
			throws SQLException {
		
		Integer id = rset.getInt("id");
		Timestamp time = rset.getTimestamp("time");
		Double x = rset.getDouble("x");
		Double y = rset.getDouble("y");
		Double z = rset.getDouble("z");
		Fingerprint fingerPrint = new Fingerprint(id, time, x, y, z);
		return fingerPrint;
	}

	@Override
	protected Fingerprint[] createVOsFromResultSet(ResultSet rset)
			throws SQLException {
		Fingerprint[] fingerPrints = null;
		Vector<Fingerprint> tempVector = new Vector<Fingerprint>();
		do {
			Integer id = rset.getInt("id");
			Timestamp time = rset.getTimestamp("time");
			Double x = rset.getDouble("x");
			Double y = rset.getDouble("y");
			Double z = rset.getDouble("z");
			Fingerprint fingerPrint = new Fingerprint(id, time, x, y, z);
			tempVector.addElement(fingerPrint);
		} while(rset.next());
		fingerPrints = new Fingerprint[tempVector.size()];
		tempVector.toArray(fingerPrints);
		return fingerPrints;
	}

	@Override
	public int insert(Fingerprint obj) throws SQLException {
		int result = -1;
		PreparedStatement pstmt = null;
		try{
			Connection con = dbm.getConnection();
			String query = "insert into " + tableName + " values(NULL, ?, ?, ?, ?)";
			pstmt = con.prepareStatement(query);
			/*
			if (obj.getId() != null)
				pstmt.setInt(1, obj.getId());
			else
				pstmt.setNull(1, java.sql.Types.NULL);
			*/
			pstmt.setTimestamp(1, obj.getTime());
			pstmt.setDouble(2, obj.getX());
			pstmt.setDouble(3, obj.getY());
			pstmt.setDouble(4, obj.getZ());
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
