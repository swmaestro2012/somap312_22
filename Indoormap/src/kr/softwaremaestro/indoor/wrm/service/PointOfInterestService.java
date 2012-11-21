package kr.softwaremaestro.indoor.wrm.service;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import kr.softwaremaestro.indoor.wrm.connection.DBManager;
import kr.softwaremaestro.indoor.wrm.dao.PointOfInterestDAO;
import kr.softwaremaestro.indoor.wrm.vo.PointOfInterest;

public class PointOfInterestService {
	private PointOfInterestDAO dao;
	
	public PointOfInterestService(DBManager dbm) {
		this.dao = new PointOfInterestDAO(dbm);
	}
	
	public PointOfInterest[] getAll() throws SQLException{
		return dao.select(null);
	}
	
	public PointOfInterest getById(Integer id) throws SQLException {
		Map<String, String> parameters = new HashMap<String, String>();
		parameters.put("id", String.valueOf(id));
		PointOfInterest poi = dao.selectOne(parameters);
		return poi;
	}
	
	public boolean removeAll() throws SQLException{
		return dao.delete(null);
	}
	
	public boolean removeById(Integer id) throws SQLException{
		Map<String, String> parameters = new HashMap<String, String>();
		parameters.put("id", String.valueOf(id));
		return dao.delete(parameters);
	}
	
	public Integer add(PointOfInterest poi) throws SQLException{
		return dao.insert(poi);
	}
	
	public void modifyById(PointOfInterest poi) throws SQLException {
		Map<String, Object> setMap = new HashMap<String, Object>();
		Map<String, String> whereMap = new HashMap<String, String>();
		setMap.put("x", poi.getX());
		setMap.put("y", poi.getY());
		setMap.put("z", poi.getZ());
		setMap.put("name", poi.getName());
		whereMap.put("id", String.valueOf(poi.getId()));
		dao.update(setMap, whereMap);
	}
}
