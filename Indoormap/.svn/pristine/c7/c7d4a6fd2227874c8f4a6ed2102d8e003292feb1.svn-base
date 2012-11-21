package kr.softwaremaestro.indoor.wrm.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;

import kr.softwaremaestro.indoor.wrm.connection.DBManager;
import kr.softwaremaestro.indoor.wrm.vo.PointOfInterest;


public class PointOfInterestDAO extends AbstractDAO<PointOfInterest>{
	
	public PointOfInterestDAO(DBManager dbm) {
		super.dbm = dbm;
		super.tableName = "point_of_interests";
	}

	@Override
	protected PointOfInterest createVOFromResultSet(ResultSet rset)
			throws SQLException {
		
		Integer id = rset.getInt("id");
		Double x = rset.getDouble("x");
		Double y = rset.getDouble("y");
		Double z = rset.getDouble("z");
		String name = rset.getString("name");
		PointOfInterest poi = new PointOfInterest(id, x, y, z, name);
		return poi;
	}

	@Override
	protected PointOfInterest[] createVOsFromResultSet(ResultSet rset)
			throws SQLException {
		PointOfInterest[] pois = null;
		Vector<PointOfInterest> tempVector = new Vector<PointOfInterest>();
		do {
			Integer id = rset.getInt("id");
			Double x = rset.getDouble("x");
			Double y = rset.getDouble("y");
			Double z = rset.getDouble("z");
			String name = rset.getString("name");
			PointOfInterest poi = new PointOfInterest(id, x, y, z, name);
			tempVector.addElement(poi);
		} while(rset.next());
		pois = new PointOfInterest[tempVector.size()];
		tempVector.toArray(pois);
		return pois;
	}

	@Override
	public int insert(PointOfInterest obj) throws SQLException {
		int result = -1;
		PreparedStatement pstmt = null;
		try{
			Connection con = dbm.getConnection();
			String query = "insert into " + tableName + " values(?, ?, ?, ?, ?)";
			pstmt = con.prepareStatement(query);
			pstmt.setDouble(1, obj.getId());
			pstmt.setString(2, obj.getName());
			pstmt.setDouble(3, obj.getX());
			pstmt.setDouble(4, obj.getY());
			pstmt.setDouble(5, obj.getZ());
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
