package kr.softwaremaestro.indoor.wrm.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;

import kr.softwaremaestro.indoor.wrm.connection.DBManager;
import kr.softwaremaestro.indoor.wrm.vo.WaypointLink;


public class WaypointLinkDAO extends AbstractDAO<WaypointLink>{
	
	public WaypointLinkDAO(DBManager dbm) {
		super.dbm = dbm;
		super.tableName = "waypoint_links";
	}
	
	@Override
	protected WaypointLink createVOFromResultSet(ResultSet rset)
			throws SQLException {
		
		Integer id = rset.getInt("id");
		Integer startPointId = rset.getInt("start_point_id");
		Integer endPointId = rset.getInt("end_point_id");
		WaypointLink waypointLink = new WaypointLink(id, startPointId, endPointId);
		return waypointLink;
	}

	@Override
	protected WaypointLink[] createVOsFromResultSet(ResultSet rset)
			throws SQLException {
		WaypointLink[] waypointLinks = null;
		Vector<WaypointLink> tempVector = new Vector<WaypointLink>();
		do {
			Integer id = rset.getInt("id");
			Integer startPointId = rset.getInt("start_point_id");
			Integer endPointId = rset.getInt("end_point_id");
			WaypointLink waypointLink = new WaypointLink(id, startPointId, endPointId);
			tempVector.addElement(waypointLink);
		} while(rset.next());
		waypointLinks = new WaypointLink[tempVector.size()];
		tempVector.toArray(waypointLinks);
		return waypointLinks;
	}

	@Override
	public int insert(WaypointLink obj) throws SQLException {
		int result = -1;
		PreparedStatement pstmt = null;
		try{
			Connection con = dbm.getConnection();
			String query = "insert into " + tableName + " values(?, ?, ?)";
			pstmt = con.prepareStatement(query);
			pstmt.setInt(1, obj.getId());
			pstmt.setInt(2, obj.getStartPointId());
			pstmt.setInt(3, obj.getEndPointId());
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
