package kr.softwaremaestro.indoor.wrm.service;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import kr.softwaremaestro.indoor.wrm.connection.DBManager;
import kr.softwaremaestro.indoor.wrm.dao.AccesspointDAO;
import kr.softwaremaestro.indoor.wrm.vo.Accesspoint;

public class AccesspointService {
	private AccesspointDAO dao;
	
	public AccesspointService(DBManager dbm) {
		this.dao = new AccesspointDAO(dbm);
	}
	
	public Accesspoint[] getAll() throws SQLException{
		return dao.select(null);
	}
	
	public Accesspoint getById(Integer id) throws SQLException {
		Map<String, String> parameters = new HashMap<String, String>();
		parameters.put("id", String.valueOf(id));
		Accesspoint ap = dao.selectOne(parameters);
		return ap;
	}
	
	public boolean removeAll() throws SQLException{
		return dao.delete(null);
	}
	
	public boolean removeById(Integer id) throws SQLException{
		Map<String, String> parameters = new HashMap<String, String>();
		parameters.put("id", String.valueOf(id));
		return dao.delete(parameters);
	}
	
	public Integer add(Accesspoint accesspoint) throws SQLException{
		return dao.insert(accesspoint);
	}
	
	public void modifyById(Accesspoint accesspoint) throws SQLException {
		Map<String, String> setMap = new HashMap<String, String>();
		Map<String, String> whereMap = new HashMap<String, String>();
		setMap.put("bssid", accesspoint.getBssid());
		setMap.put("ssid", accesspoint.getSsid());
		setMap.put("capabilities", accesspoint.getCapabilities());
		setMap.put("frequency", String.valueOf(accesspoint.getFrequency()));
		whereMap.put("id", String.valueOf(accesspoint.getId()));
		dao.update(setMap, whereMap);
	}
}
