package kr.softwaremaestro.indoor.wrm.service;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import kr.softwaremaestro.indoor.wrm.connection.DBManager;
import kr.softwaremaestro.indoor.wrm.dao.WaypointLinkDAO;
import kr.softwaremaestro.indoor.wrm.vo.WaypointLink;

public class WaypointLinkService {
	private WaypointLinkDAO dao;
	
	public WaypointLinkService(DBManager dbm) {
		this.dao = new WaypointLinkDAO(dbm);
	}
	
	public WaypointLink[] getAll() throws SQLException{
		return dao.select(null);
	}
	
	public WaypointLink getById(Integer id) throws SQLException {
		Map<String, String> parameters = new HashMap<String, String>();
		parameters.put("id", String.valueOf(id));
		WaypointLink waypointLink = dao.selectOne(parameters);
		return waypointLink;
	}
	
	public boolean removeAll() throws SQLException{
		return dao.delete(null);
	}
	
	public boolean removeById(Integer id) throws SQLException{
		Map<String, String> parameters = new HashMap<String, String>();
		parameters.put("id", String.valueOf(id));
		return dao.delete(parameters);
	}
	
	public Integer add(WaypointLink waypointLink) throws SQLException{
		return dao.insert(waypointLink);
	}
	
	public void modifyById(WaypointLink waypointLink) throws SQLException {
		Map<String, Object> setMap = new HashMap<String, Object>();
		Map<String, String> whereMap = new HashMap<String, String>();
		setMap.put("start_point_id", waypointLink.getStartPointId());
		setMap.put("end_point_id", waypointLink.getEndPointId());
		whereMap.put("id", String.valueOf(waypointLink.getId()));
		dao.update(setMap, whereMap);
	}
}
