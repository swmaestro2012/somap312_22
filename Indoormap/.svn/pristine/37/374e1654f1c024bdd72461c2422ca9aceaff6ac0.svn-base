package kr.softwaremaestro.indoor.wrm.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;

import kr.softwaremaestro.indoor.wrm.connection.DBManager;
import kr.softwaremaestro.indoor.wrm.vo.MeasurepointLink;


public class MeasurepointLinkDAO extends AbstractDAO<MeasurepointLink>{
	
	public MeasurepointLinkDAO(DBManager dbm) {
		super.dbm = dbm;
		super.tableName = "measurepoint_links";
	}
	
	@Override
	protected MeasurepointLink createVOFromResultSet(ResultSet rset)
			throws SQLException {
		
		Integer id = rset.getInt("id");
		Integer startPointId = rset.getInt("start_point_id");
		Integer endPointId = rset.getInt("end_point_id");
		MeasurepointLink measurepointLink = new MeasurepointLink(id, startPointId, endPointId);
		return measurepointLink;
	}

	@Override
	protected MeasurepointLink[] createVOsFromResultSet(ResultSet rset)
			throws SQLException {
		MeasurepointLink[] measurepointLinks = null;
		Vector<MeasurepointLink> tempVector = new Vector<MeasurepointLink>();
		do {
			Integer id = rset.getInt("id");
			Integer startPointId = rset.getInt("start_point_id");
			Integer endPointId = rset.getInt("end_point_id");
			MeasurepointLink measurepointLink = new MeasurepointLink(id, startPointId, endPointId);
			tempVector.addElement(measurepointLink);
		} while(rset.next());
		measurepointLinks = new MeasurepointLink[tempVector.size()];
		tempVector.toArray(measurepointLinks);
		return measurepointLinks;
	}

	@Override
	public int insert(MeasurepointLink obj) throws SQLException {
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
