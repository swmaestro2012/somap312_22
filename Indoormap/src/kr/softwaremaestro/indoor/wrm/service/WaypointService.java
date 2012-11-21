package kr.softwaremaestro.indoor.wrm.service;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import kr.softwaremaestro.indoor.wrm.connection.DBManager;
import kr.softwaremaestro.indoor.wrm.dao.WaypointDAO;
import kr.softwaremaestro.indoor.wrm.vo.Waypoint;

public class WaypointService {
	private WaypointDAO dao;
	
	public WaypointService(DBManager dbm) {
		this.dao = new WaypointDAO(dbm);
	}
	
	public Waypoint[] getAll() throws SQLException{
		return dao.select(null);
	}
	
	public Waypoint getById(Integer id) throws SQLException {
		Map<String, String> parameters = new HashMap<String, String>();
		parameters.put("id", String.valueOf(id));
		Waypoint waypoint = dao.selectOne(parameters);
		return waypoint;
	}
	
	public boolean removeAll() throws SQLException{
		return dao.delete(null);
	}
	
	public boolean removeById(Integer id) throws SQLException{
		Map<String, String> parameters = new HashMap<String, String>();
		parameters.put("id", String.valueOf(id));
		return dao.delete(parameters);
	}
	
	public Integer add(Waypoint waypoint) throws SQLException{
		return dao.insert(waypoint);
	}
		
	public void modifyById(Waypoint waypoint) throws SQLException {
		Map<String, Object> setMap = new HashMap<String, Object>();
		Map<String, String> whereMap = new HashMap<String, String>();
		setMap.put("x", waypoint.getX());
		setMap.put("y", waypoint.getY());
		setMap.put("z", waypoint.getZ());
		whereMap.put("id", String.valueOf(waypoint.getId()));
		dao.update(setMap, whereMap);
	}
}
