package kr.softwaremaestro.indoor.wrm.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Vector;

import kr.softwaremaestro.indoor.wrm.connection.DBManager;
import kr.softwaremaestro.indoor.wrm.vo.Floor;


public class FloorDAO extends AbstractDAO<Floor>{
	
	public FloorDAO(DBManager dbm) {
		super.dbm = dbm;
		super.tableName = "floors";
	}

	@Override
	protected Floor createVOFromResultSet(ResultSet rset)
			throws SQLException {
		
		Integer id = rset.getInt("id");
		String name = rset.getString("name");
		byte[] image = rset.getBytes("image");
		Integer imageWidth = rset.getInt("image_width");
		Integer imageHeight = rset.getInt("image_height");
		Integer imageOriginX = rset.getInt("image_origin_x");
		Integer imageOriginY = rset.getInt("image_origin_y");
		Double imageScale = rset.getDouble("image_scale");
		Double z = rset.getDouble("z");
		Floor floor = new Floor(id, name, image, imageWidth, imageHeight, imageOriginX, imageOriginY, imageScale, z);
		return floor;
	}

	@Override
	protected Floor[] createVOsFromResultSet(ResultSet rset)
			throws SQLException {
		Floor[] floors = null;
		Vector<Floor> tempVector = new Vector<Floor>();
		do {
			Integer id = rset.getInt("id");
			String name = rset.getString("name");
			byte[] image = rset.getBytes("image");
			Integer imageWidth = rset.getInt("image_width");
			Integer imageHeight = rset.getInt("image_height");
			Integer imageOriginX = rset.getInt("image_origin_x");
			Integer imageOriginY = rset.getInt("image_origin_y");
			Double imageScale = rset.getDouble("image_scale");
			Double z = rset.getDouble("z");
			Floor floor = new Floor(id, name, image, imageWidth, imageHeight, imageOriginX, imageOriginY, imageScale, z);
			tempVector.addElement(floor);
		} while(rset.next());
		floors = new Floor[tempVector.size()];
		tempVector.toArray(floors);
		return floors;
	}

	@Override
	public int insert(Floor obj) throws SQLException {
		int result = -1;
		PreparedStatement pstmt = null;
		try{
			Connection con = dbm.getConnection();
			String query = "insert into " + tableName + " values(?, ?, ?, ?, ?, ?, ?, ?, ?)";
			pstmt = con.prepareStatement(query);
			pstmt.setInt(1, obj.getId());
			pstmt.setString(2, obj.getName());
			pstmt.setBytes(3, obj.getImage());
			pstmt.setInt(4, obj.getImageWidth());
			pstmt.setInt(5, obj.getImageHeight());
			pstmt.setInt(6, obj.getImageOriginX());
			pstmt.setInt(7, obj.getImageOriginY());
			pstmt.setDouble(8, obj.getImageScale());
			pstmt.setDouble(9, obj.getZ());
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
