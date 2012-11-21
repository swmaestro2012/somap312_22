package kr.softwaremaestro.indoor.wrm.service;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import kr.softwaremaestro.indoor.wrm.connection.DBManager;
import kr.softwaremaestro.indoor.wrm.dao.MeasurepointLinkDAO;
import kr.softwaremaestro.indoor.wrm.vo.MeasurepointLink;

public class MeasurepointLinkService {
	private MeasurepointLinkDAO dao;
	
	public MeasurepointLinkService(DBManager dbm) {
		this.dao = new MeasurepointLinkDAO(dbm);
	}
	
	public MeasurepointLink[] getAll() throws SQLException{
		return dao.select(null);
	}
	
	public MeasurepointLink getById(Integer id) throws SQLException {
		Map<String, String> parameters = new HashMap<String, String>();
		parameters.put("id", String.valueOf(id));
		MeasurepointLink measurepointLink = dao.selectOne(parameters);
		return measurepointLink;
	}
	
	public boolean removeAll() throws SQLException{
		return dao.delete(null);
	}
	
	public boolean removeById(Integer id) throws SQLException{
		Map<String, String> parameters = new HashMap<String, String>();
		parameters.put("id", String.valueOf(id));
		return dao.delete(parameters);
	}
	
	public Integer add(MeasurepointLink measurepointLink) throws SQLException{
		return dao.insert(measurepointLink);
	}
	
	public void modifyById(MeasurepointLink measurepointLink) throws SQLException {
		Map<String, Object> setMap = new HashMap<String, Object>();
		Map<String, String> whereMap = new HashMap<String, String>();
		setMap.put("start_point_id", measurepointLink.getStartPointId());
		setMap.put("end_point_id", measurepointLink.getEndPointId());
		whereMap.put("id", String.valueOf(measurepointLink.getId()));
		dao.update(setMap, whereMap);
	}
}
