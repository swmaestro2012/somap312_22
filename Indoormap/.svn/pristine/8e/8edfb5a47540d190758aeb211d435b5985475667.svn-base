package kr.softwaremaestro.indoor.wrm.service;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import kr.softwaremaestro.indoor.wrm.connection.DBManager;
import kr.softwaremaestro.indoor.wrm.dao.ApSetDAO;
import kr.softwaremaestro.indoor.wrm.vo.ApSet;

public class ApSetService {
	private ApSetDAO dao;
	
	public ApSetService(DBManager dbm) {
		this.dao = new ApSetDAO(dbm);
	}
	
	public ApSet[] getAll() throws SQLException{
		return dao.select(null);
	}
	
	public ApSet[] getByFingerprintId(Integer fingerprintId) throws SQLException {
		Map<String, String> parameters = new HashMap<String, String>();
		parameters.put("fingerprint_id", String.valueOf(fingerprintId));
		ApSet[] apSets = dao.select(parameters);
		return apSets;
	}
	
	public ApSet[] getByApId(Integer apId) throws SQLException {
		Map<String, String> parameters = new HashMap<String, String>();
		parameters.put("ap_id", String.valueOf(apId));
		ApSet[] apSets = dao.select(parameters);
		return apSets;
	}
	
	public boolean removeAll() throws SQLException{
		return dao.delete(null);
	}
	
	public boolean removeByFingerprintId(Integer fingerprintId) throws SQLException{
		Map<String, String> parameters = new HashMap<String, String>();
		parameters.put("fingerprint_id", String.valueOf(fingerprintId));
		return dao.delete(parameters);
	}
	
	public boolean removeByApId(Integer apId) throws SQLException{
		Map<String, String> parameters = new HashMap<String, String>();
		parameters.put("ap_id", String.valueOf(apId));
		return dao.delete(parameters);
	}
	
	public Integer add(ApSet apSet) throws SQLException{
		return dao.insert(apSet);
	}
}
