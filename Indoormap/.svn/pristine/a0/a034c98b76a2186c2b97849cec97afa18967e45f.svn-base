package kr.softwaremaestro.indoor.wrm.service;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.util.Log;

import kr.softwaremaestro.indoor.wrm.connection.DBManager;
import kr.softwaremaestro.indoor.wrm.dao.FloorDAO;
import kr.softwaremaestro.indoor.wrm.vo.Floor;

public class FloorService {
	private FloorDAO dao;
	
	public FloorService(DBManager dbm) {
		this.dao = new FloorDAO(dbm);
	}
	
	public Floor[] getAll() throws SQLException{
		Log.d("FloorService", "getAll()");
		return dao.select(null);
	}
	
	public Floor getById(Integer id) throws SQLException {
		Map<String, String> parameters = new HashMap<String, String>();
		parameters.put("id", String.valueOf(id));
		Floor floor = dao.selectOne(parameters);
		return floor;
	}
	
	public boolean removeAll() throws SQLException{
		return dao.delete(null);
	}
	
	public boolean removeById(Integer id) throws SQLException{
		Map<String, String> parameters = new HashMap<String, String>();
		parameters.put("id", String.valueOf(id));
		return dao.delete(parameters);
	}
	
	public Integer add(Floor floor) throws SQLException{
		return dao.insert(floor);
	}
	
	public void modifyById(Floor floor) throws SQLException {
		Map<String, Object> setMap = new HashMap<String, Object>();
		Map<String, String> whereMap = new HashMap<String, String>();
		setMap.put("name", floor.getName());
		setMap.put("image", floor.getImage());
		setMap.put("image_width", floor.getImageWidth());
		setMap.put("image_height", floor.getImageHeight());
		setMap.put("image_origin_x", floor.getImageOriginX());
		setMap.put("image_origin_y", floor.getImageOriginY());
		setMap.put("image_scale", floor.getImageScale());
		setMap.put("z", floor.getZ());
		whereMap.put("id", String.valueOf(floor.getId()));
		dao.update(setMap, whereMap);
	}
}
