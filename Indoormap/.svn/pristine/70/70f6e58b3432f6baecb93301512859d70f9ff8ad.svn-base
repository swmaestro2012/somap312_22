package kr.softwaremaestro.indoor.wrm.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Map;
import java.util.Set;

import kr.softwaremaestro.indoor.wrm.connection.DBManager;
import android.util.Log;

public abstract class AbstractDAO<T> {
	protected DBManager dbm;
	protected String tableName = "";

	abstract protected T createVOFromResultSet(ResultSet rset) throws SQLException;
	abstract protected T[] createVOsFromResultSet(ResultSet rset) throws SQLException;
	abstract public int insert(T obj) throws SQLException;	
	
	public T selectOne(Map whereCondition) throws SQLException {		
		T result = null;
		Statement stmt = null;
		ResultSet rset = null;
		try{
			Connection con = dbm.getConnection();
			stmt = con.createStatement();
			StringBuffer query = new StringBuffer();
			query.append("select * from " + tableName);
			query.append(makeWhereClause(whereCondition));
			rset = stmt.executeQuery(query.toString());
			boolean isExists = rset.next();
			if (isExists) {
				result = createVOFromResultSet(rset);				
			}
			rset.close();
			stmt.close();
		}catch(SQLException e){
			throw e;
		} finally {
			if (rset != null) {
				try {
					rset.close();
				} catch (SQLException e) {}
			}
			if (stmt != null) {
				try {
					stmt.close();
				} catch (SQLException e) {}
			}
		}
		return result;
	}
	
	public T[] select(Map whereCondition) throws SQLException {		
		T[] result = null;
		Statement stmt = null;
		ResultSet rset = null;		
		try{
			Log.d("AbstractDao", "dbm : " + dbm.toString());
			Connection con = dbm.getConnection();
			if (con == null) 
				Log.d("AbstractDao", "connection is null");
			else
				Log.d("AbstractDao", "connection is connected");
			stmt = con.createStatement();
			StringBuffer query = new StringBuffer();
			query.append("select * from " + tableName);
			query.append(makeWhereClause(whereCondition));
			rset = stmt.executeQuery(query.toString());
			boolean isExists = rset.next();
			if (isExists) {
				result = createVOsFromResultSet(rset);				
			}
			rset.close();
			stmt.close();
		} catch(SQLException e){
			throw e;
		} finally {
			if (rset != null) {
				try {
					rset.close();
				} catch(SQLException e) {};
			}
			if (stmt != null) {
				try {
					stmt.close();
				} catch(SQLException e) {};
			}
		}
		return result;
	}
	
	public void update(Map setCondition, Map whereCondition) throws SQLException {
		PreparedStatement pstmt = null;
		try {			
			Connection con = dbm.getConnection();
			StringBuffer query = new StringBuffer();
			query.append("update " + tableName);
			query.append(makeSetClause(setCondition));
			query.append(makeWhereClause(whereCondition));
			pstmt = con.prepareStatement(query.toString());
			pstmt = fillSetCondition(pstmt, setCondition);
			pstmt.executeUpdate();
			pstmt.close();
		} catch (SQLException e) {
			throw e;
		} finally {
			if (pstmt != null) {
				try {
					pstmt.close();
				} catch (SQLException e) {}
			}
		}
	}
	
	private PreparedStatement fillSetCondition(PreparedStatement pstmt, Map setCondition) throws SQLException {
		int idx = 1;
		for(String field : (Set<String>)setCondition.keySet()) {
			if (setCondition.get(field) instanceof Integer) 
				pstmt.setInt(idx++, (Integer)setCondition.get(field));
			else if (setCondition.get(field) instanceof Double)
				pstmt.setDouble(idx++, (Double)setCondition.get(field));
			else if (setCondition.get(field) instanceof String)
				pstmt.setString(idx++, (String)setCondition.get(field));
			else if (setCondition.get(field) instanceof Byte)
				pstmt.setByte(idx++, (Byte)setCondition.get(field));
			else if (setCondition.get(field) instanceof byte[])
				pstmt.setBytes(idx++, (byte[])setCondition.get(field));
		}
		return pstmt;
	}
	public boolean delete(Map whereCondition) throws SQLException {
		boolean is_ok = true;
		Statement stmt = null;
		try {
			Connection con = dbm.getConnection();
			stmt = con.createStatement();
			StringBuffer query = new StringBuffer();
			query.append("delete from " + tableName);
			query.append(makeWhereClause(whereCondition));
			int num_affected_row = stmt.executeUpdate(query.toString());
			stmt.close();
			if (num_affected_row == 0) {
				is_ok = false;
			}
		} catch (SQLException e) {
			is_ok = false;
			throw e;
		} finally {
			if (stmt != null) {
				try {
					stmt.close();
				} catch (SQLException e) {}
			}
		}
		return is_ok;
	}
	
	private String makeWhereClause(Map whereCondition) {
		if (whereCondition == null || whereCondition.size() == 0) {
			return "";
		}
		else {
			StringBuffer whereClause = new StringBuffer();
			whereClause.append(" where ");
			int idx = 0;
			for(String fieldName : (Set<String>)whereCondition.keySet()) {
				String delemeter = (++idx == whereCondition.size()) ? "' " : "', ";
				whereClause.append(fieldName+"='" + whereCondition.get(fieldName) + delemeter)	;
			}
			return whereClause.toString();
		}
	}
	
	
	
	private String makeSetClause(Map setCondition) {
		if (setCondition == null || setCondition.size() == 0) {
			return "";
		}
		else {
			StringBuffer whereClause = new StringBuffer();
			whereClause.append(" set ");
			int idx = 0;
			for(String fieldName : (Set<String>)setCondition.keySet()) {
				String delemeter = (++idx == setCondition.size()) ? " " : ", ";
				whereClause.append(fieldName+"=" + "?" + delemeter)	;
			}
			return whereClause.toString();
		}
	}
	
}
