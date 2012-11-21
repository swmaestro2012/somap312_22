package kr.softwaremaestro.indoor.wrm.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;

import kr.softwaremaestro.indoor.wrm.connection.DBManager;
import kr.softwaremaestro.indoor.wrm.vo.Waypoint;


public class WaypointDAO extends AbstractDAO<Waypoint>{
	
	public WaypointDAO(DBManager dbm) {
		super.dbm = dbm;
		super.tableName = "waypoints";
	}

	@Override
	protected Waypoint createVOFromResultSet(ResultSet rset)
			throws SQLException {
		
		Integer id = rset.getInt("id");
		Double x = rset.getDouble("x");
		Double y = rset.getDouble("y");
		Double z = rset.getDouble("z");
		Waypoint waypoint = new Waypoint(id, x, y, z);
		return waypoint;
	}

	@Override
	protected Waypoint[] createVOsFromResultSet(ResultSet rset)
			throws SQLException {
		Waypoint[] waypoints = null;
		Vector<Waypoint> tempVector = new Vector<Waypoint>();
		do {
			Integer id = rset.getInt("id");
			Double x = rset.getDouble("x");
			Double y = rset.getDouble("y");
			Double z = rset.getDouble("z");
			Waypoint waypoint = new Waypoint(id, x, y, z);
			tempVector.addElement(waypoint);
		} while(rset.next());
		waypoints = new Waypoint[tempVector.size()];
		tempVector.toArray(waypoints);
		return waypoints;
	}

	@Override
	public int insert(Waypoint obj) throws SQLException {
		int result = -1;
		PreparedStatement pstmt = null;
		try{
			Connection con = dbm.getConnection();
			String query = "insert into " + tableName + " values(?, ?, ?, ?)";
			pstmt = con.prepareStatement(query);
			pstmt.setInt(1, obj.getId());
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
