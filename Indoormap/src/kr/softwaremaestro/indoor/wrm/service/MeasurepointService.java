package kr.softwaremaestro.indoor.wrm.service;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import kr.softwaremaestro.indoor.wrm.connection.DBManager;
import kr.softwaremaestro.indoor.wrm.dao.MeasurepointDAO;
import kr.softwaremaestro.indoor.wrm.vo.Measurepoint;

public class MeasurepointService {
	private MeasurepointDAO dao;
	
	public MeasurepointService(DBManager dbm) {
		this.dao = new MeasurepointDAO(dbm);
	}
	
	public Measurepoint[] getAll() throws SQLException{
		return dao.select(null);
	}
	
	public Measurepoint getById(Integer id) throws SQLException {
		Map<String, String> parameters = new HashMap<String, String>();
		parameters.put("id", String.valueOf(id));
		Measurepoint measurepoint = dao.selectOne(parameters);
		return measurepoint;
	}
	
	public boolean removeAll() throws SQLException{
		return dao.delete(null);
	}
	
	public boolean removeById(Integer id) throws SQLException{
		Map<String, String> parameters = new HashMap<String, String>();
		parameters.put("id", String.valueOf(id));
		return dao.delete(parameters);
	}
	
	public Integer add(Measurepoint measurepoint) throws SQLException{
		return dao.insert(measurepoint);
	}
		
	public void modifyById(Measurepoint measurepoint) throws SQLException {
		Map<String, Object> setMap = new HashMap<String, Object>();
		Map<String, String> whereMap = new HashMap<String, String>();
		setMap.put("x", measurepoint.getX());
		setMap.put("y", measurepoint.getY());
		setMap.put("z", measurepoint.getZ());
		setMap.put("mp_link_id", measurepoint.getMpLinkId());
		whereMap.put("id", String.valueOf(measurepoint.getId()));
		dao.update(setMap, whereMap);
	}
}
