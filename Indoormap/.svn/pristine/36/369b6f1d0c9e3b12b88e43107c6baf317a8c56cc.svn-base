package kr.softwaremaestro.indoor.wrm.service;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import kr.softwaremaestro.indoor.wrm.connection.DBManager;
import kr.softwaremaestro.indoor.wrm.dao.BaseMeasurepointDAO;
import kr.softwaremaestro.indoor.wrm.vo.BaseMeasurepoint;

public class BaseMeasurepointService {
	private BaseMeasurepointDAO dao;
	
	public BaseMeasurepointService(DBManager dbm) {
		this.dao = new BaseMeasurepointDAO(dbm);
	}
	
	public BaseMeasurepoint[] getAll() throws SQLException{
		return dao.select(null);
	}
	
	public BaseMeasurepoint getById(Integer id) throws SQLException {
		Map<String, String> parameters = new HashMap<String, String>();
		parameters.put("id", String.valueOf(id));
		BaseMeasurepoint baseMeasurepoint = dao.selectOne(parameters);
		return baseMeasurepoint;
	}
	
	public boolean removeAll() throws SQLException{
		return dao.delete(null);
	}
	
	public boolean removeById(Integer id) throws SQLException{
		Map<String, String> parameters = new HashMap<String, String>();
		parameters.put("id", String.valueOf(id));
		return dao.delete(parameters);
	}
	
	public Integer add(BaseMeasurepoint baseMeasurepoint) throws SQLException{
		return dao.insert(baseMeasurepoint);
	}
	
	public void modifyById(BaseMeasurepoint baseMeasurepoint) throws SQLException {
		Map<String, String> setMap = new HashMap<String, String>();
		Map<String, String> whereMap = new HashMap<String, String>();
		setMap.put("x", String.valueOf(baseMeasurepoint.getX()));
		setMap.put("y", String.valueOf(baseMeasurepoint.getY()));
		setMap.put("z", String.valueOf(baseMeasurepoint.getZ()));
		whereMap.put("id", String.valueOf(baseMeasurepoint.getId()));
		dao.update(setMap, whereMap);
	}
}
