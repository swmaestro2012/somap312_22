package kr.softwaremaestro.indoor.wrm.service;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import kr.softwaremaestro.indoor.wrm.connection.DBManager;
import kr.softwaremaestro.indoor.wrm.dao.FingerprintDAO;
import kr.softwaremaestro.indoor.wrm.vo.Fingerprint;

public class FingerprintService {
	private FingerprintDAO dao;
	
	public FingerprintService(DBManager dbm) {
		this.dao = new FingerprintDAO(dbm);
	}
	
	public Fingerprint[] getAll() throws SQLException{
		return dao.select(null);
	}
	
	public Fingerprint getById(Integer id) throws SQLException {
		Map<String, String> parameters = new HashMap<String, String>();
		parameters.put("id", String.valueOf(id));
		Fingerprint fingerprint = dao.selectOne(parameters);
		return fingerprint;
	}
	
	public Fingerprint[] getByCoordinate(Double x, Double y, Double z) throws SQLException {
		Map<String, String> parameters = new HashMap<String, String>();
		parameters.put("x", String.valueOf(x));
		parameters.put("y", String.valueOf(y));
		parameters.put("z", String.valueOf(z));
		Fingerprint[] fingerprints = dao.select(parameters);
		return fingerprints;
	}
	
	public boolean removeAll() throws SQLException{
		return dao.delete(null);
	}
	
	public boolean removeById(Integer id) throws SQLException{
		Map<String, String> parameters = new HashMap<String, String>();
		parameters.put("id", String.valueOf(id));
		return dao.delete(parameters);
	}
	
	public Integer add(Fingerprint fingerprint) throws SQLException{
		return dao.insert(fingerprint);
	}
	
}
