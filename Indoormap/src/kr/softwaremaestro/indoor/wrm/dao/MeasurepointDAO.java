package kr.softwaremaestro.indoor.wrm.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;

import kr.softwaremaestro.indoor.wrm.connection.DBManager;
import kr.softwaremaestro.indoor.wrm.vo.Measurepoint;


public class MeasurepointDAO extends AbstractDAO<Measurepoint>{
	
	public MeasurepointDAO(DBManager dbm) {
		super.dbm = dbm;
		super.tableName = "measurepoints";
	}
		
	@Override
	protected Measurepoint createVOFromResultSet(ResultSet rset)
			throws SQLException {
		
		Integer id = rset.getInt("id");
		Double x = rset.getDouble("x");
		Double y = rset.getDouble("y");
		Double z = rset.getDouble("z");
		Integer mpLinkId = rset.getInt("mp_link_id");
		Measurepoint bm = new Measurepoint(id, x, y, z, mpLinkId);
		return bm;
	}

	@Override
	protected Measurepoint[] createVOsFromResultSet(ResultSet rset)
			throws SQLException {
		Measurepoint[] measurePoints = null;
		Vector<Measurepoint> tempVector = new Vector<Measurepoint>();
		do {
			Integer id = rset.getInt("id");
			Double x = rset.getDouble("x");
			Double y = rset.getDouble("y");
			Double z = rset.getDouble("z");
			Integer mpLinkId = rset.getInt("mp_link_id");
			Measurepoint measurePoint = new Measurepoint(id, x, y, z, mpLinkId);
			tempVector.addElement(measurePoint);
		} while(rset.next());
		measurePoints = new Measurepoint[tempVector.size()];
		tempVector.toArray(measurePoints);
		return measurePoints;
	}

	@Override
	public int insert(Measurepoint obj) throws SQLException {
		int result = -1;
		PreparedStatement pstmt = null;
		try{
			Connection con = dbm.getConnection();
			String query = "insert into " + tableName + " values(?, ?, ?, ?, ?)";
			pstmt = con.prepareStatement(query);
			pstmt.setInt(1, obj.getId());
			pstmt.setDouble(2, obj.getX());
			pstmt.setDouble(3, obj.getY());
			pstmt.setDouble(4, obj.getZ());
			pstmt.setInt(5, obj.getMpLinkId());
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
