package kr.softwaremaestro.indoor.wrm.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;

import kr.softwaremaestro.indoor.wrm.connection.DBManager;
import kr.softwaremaestro.indoor.wrm.vo.BaseMeasurepoint;


public class BaseMeasurepointDAO extends AbstractDAO<BaseMeasurepoint>{
	
	public BaseMeasurepointDAO(DBManager dbm) {
		super.dbm = dbm;
		super.tableName = "base_measurepoints";
	}
	
	@Override
	protected BaseMeasurepoint createVOFromResultSet(ResultSet rset)
			throws SQLException {
		
		Integer id = rset.getInt("id");
		Double x = rset.getDouble("x");
		Double y = rset.getDouble("y");
		Double z = rset.getDouble("z");
		BaseMeasurepoint baseMeasurepoint = new BaseMeasurepoint(id, x, y, z);
		return baseMeasurepoint;
	}

	@Override
	protected BaseMeasurepoint[] createVOsFromResultSet(ResultSet rset)
			throws SQLException {
		BaseMeasurepoint[] baseMeasurepoints = null;
		Vector<BaseMeasurepoint> tempVector = new Vector<BaseMeasurepoint>();
		do {
			Integer id = rset.getInt("id");
			Double x = rset.getDouble("x");
			Double y = rset.getDouble("y");
			Double z = rset.getDouble("z");
			BaseMeasurepoint baseMeasurepoint = new BaseMeasurepoint(id, x, y, z);
			tempVector.addElement(baseMeasurepoint);
		} while(rset.next());
		baseMeasurepoints = new BaseMeasurepoint[tempVector.size()];
		tempVector.toArray(baseMeasurepoints);
		return baseMeasurepoints;
	}

	@Override
	public int insert(BaseMeasurepoint obj) throws SQLException {
		int result = -1;
		PreparedStatement pstmt = null;
		try{
			Connection con = dbm.getConnection();
			String query = "insert into " + tableName + " values(?, ?, ?, ?)";
			pstmt = con.prepareStatement(query);
			pstmt.setDouble(1, obj.getId());
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
