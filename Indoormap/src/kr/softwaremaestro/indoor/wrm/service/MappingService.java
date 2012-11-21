package kr.softwaremaestro.indoor.wrm.service;

import java.lang.reflect.Method;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import kr.softwaremaestro.indoor.wrm.connection.DBManager;
import kr.softwaremaestro.indoor.wrm.vo.Accesspoint;
import kr.softwaremaestro.indoor.wrm.vo.ApSet;
import kr.softwaremaestro.indoor.wrm.vo.BaseMeasurepoint;
import kr.softwaremaestro.indoor.wrm.vo.Fingerprint;
import kr.softwaremaestro.indoor.wrm.vo.Floor;
import kr.softwaremaestro.indoor.wrm.vo.Measurepoint;
import kr.softwaremaestro.indoor.wrm.vo.MeasurepointLink;
import kr.softwaremaestro.indoor.wrm.vo.PointOfInterest;
import kr.softwaremaestro.indoor.wrm.vo.Waypoint;
import kr.softwaremaestro.indoor.wrm.vo.WaypointLink;
import android.os.Handler;
import android.util.Log;

public class MappingService {
	private DBManager dbm = null;
	
	private AccesspointService ACC_SVC = null;
	private ApSetService AP_SET_SVC = null;
	private BaseMeasurepointService BASE_MP_SVC = null;
	private FingerprintService FINGERPRINT_SVC = null;
	private FloorService FLOOR_SVC = null;
	private MeasurepointLinkService MP_LINK_SVC = null;
	private MeasurepointService MP_SVC = null;
	private PointOfInterestService POI_SVC = null;
	private WaypointLinkService WP_LINK_SVC = null;
	private WaypointService WP_SVC = null;
	private Handler mHandler = null;
	private Handler mHandler2 = null;
	private static List<String> methodName = new ArrayList<String>();
	private static List<Class[]> parameterTypes = new ArrayList<Class[]>();
	private static List<Object[]> arguments = new ArrayList<Object[]>();
	public static boolean update = false;

	private String host = "192.168.0.13";
	private String port = "3306";
	private String dbname = "kaist_n5_building_wrm";
	private String user = "limjunsung";
	private String pass = "1234";
	
	public MappingService() {
		try {
			dbm = new DBManager(host, port, dbname, user, pass);
			Log.d("MappingService", "dbm : " + dbm.toString());
		} catch (SQLException e) {
			e.printStackTrace();
		}
		ACC_SVC = new AccesspointService(dbm);
		AP_SET_SVC = new ApSetService(dbm);
		BASE_MP_SVC = new BaseMeasurepointService(dbm);
		FINGERPRINT_SVC = new FingerprintService(dbm);
		FLOOR_SVC = new FloorService(dbm);
		MP_LINK_SVC = new MeasurepointLinkService(dbm);
		MP_SVC = new MeasurepointService(dbm);
		POI_SVC = new PointOfInterestService(dbm);
		WP_LINK_SVC = new WaypointLinkService(dbm);
		WP_SVC = new WaypointService(dbm);
	}
	public MappingService(String host) {
		this.host = host;
		try {
			dbm = new DBManager(host, port, dbname, user, pass);
			Log.d("MappingService", "dbm : " + dbm.toString());
		} catch (SQLException e) {
			e.printStackTrace();
		}
		ACC_SVC = new AccesspointService(dbm);
		AP_SET_SVC = new ApSetService(dbm);
		BASE_MP_SVC = new BaseMeasurepointService(dbm);
		FINGERPRINT_SVC = new FingerprintService(dbm);
		FLOOR_SVC = new FloorService(dbm);
		MP_LINK_SVC = new MeasurepointLinkService(dbm);
		MP_SVC = new MeasurepointService(dbm);
		POI_SVC = new PointOfInterestService(dbm);
		WP_LINK_SVC = new WaypointLinkService(dbm);
		WP_SVC = new WaypointService(dbm);
	}
	
	
	//AccesspointService
	public Accesspoint[] getAllAccesspoints() throws SQLException{
		/*
		if (update == false) {
			methodName.add("getAllAccesspoints");
			parameterTypes.add(null);
			arguments.add(null);
			return null;
		}
		*/
		//return ACC_SVC.getAll();
		Accesspoint[] aps = ACC_SVC.getAll();
		dbm.closeConnection();
		return aps;
	}
	
	public Accesspoint getAccesspointById(Integer id) throws SQLException {
		/*
		if (update == false) {
			methodName.add("getAllAccesspointById");
			parameterTypes.add(new Class[] {java.lang.Integer.class});
			arguments.add(new Object[] {new Integer(id)});
			return null;
		}
		*/
		//return ACC_SVC.getById(id);
		Accesspoint ap = ACC_SVC.getById(id);
		dbm.closeConnection();
		return ap;
	}
	
	public boolean removeAllAccesspoints() throws SQLException{
		if (update == false) {
			methodName.add("removeAllAccesspoints");
			parameterTypes.add(null);
			arguments.add(null);
			return true;
		}
		return ACC_SVC.removeAll();
	}
	
	public boolean removeAccesspointById(Integer id) throws SQLException{
		if (update == false) {
			methodName.add("removeAccesspointById");
			parameterTypes.add(new Class[] {java.lang.Integer.class});
			arguments.add(new Object[] {new Integer(id)});
			return true;
		}
		return ACC_SVC.removeById(id);
	}
	
	public Integer addAccesspoint(Accesspoint accesspoint) throws SQLException{
		if (update == false) {
			methodName.add("addAccesspoint");
			parameterTypes.add(new Class[] {kr.softwaremaestro.indoor.wrm.vo.Accesspoint.class});
			Log.d("addAccesspoint:accesspoint id", accesspoint.getId()+"");
			Log.d("addAccesspoint:accesspoint bssid", accesspoint.getBssid());
			arguments.add(new Object[] {new Accesspoint(accesspoint)});
			Log.d("addAccesspoint:result accesspoint id", ((Accesspoint)(arguments.get(arguments.size()-1))[0]).getId()+"");
			Log.d("addAccesspoint:result accesspoint bssid", ((Accesspoint)(arguments.get(arguments.size()-1))[0]).getBssid());
			return null;
		}
		if (accesspoint != null) {
			Log.d("accesspoint id", accesspoint.getId()+"");
			Log.d("accesspoint bssid", accesspoint.getBssid());
		}
		else {
			Log.d("accesspoint", "null");
		}
		return ACC_SVC.add(accesspoint);
	}
	
	public void modifyAccesspointById(Accesspoint accesspoint) throws SQLException {
		if (update == false) {
			methodName.add("modifyAccesspointById");
			parameterTypes.add(new Class[] {kr.softwaremaestro.indoor.wrm.vo.Accesspoint.class});
			arguments.add(new Object[] {new Accesspoint(accesspoint)});
		}
		ACC_SVC.modifyById(accesspoint);
	}
	
	//AccesspointService
	public ApSet[] getAllApSets() throws SQLException{
		/*
		if (update == false) {
			methodName.add("getAllApSets");
			parameterTypes.add(null);
			arguments.add(null);
			return null;
		}
		*/
		//return AP_SET_SVC.getAll();
		ApSet[] apSets = AP_SET_SVC.getAll();
		dbm.closeConnection();
		return apSets;
	}
	
	public ApSet[] getApSetByFingerprintId(Integer fingerprintId) throws SQLException {
		/*
		if (update == false) {
			methodName.add("getApSetByFingerprintId");
			parameterTypes.add(new Class[] {java.lang.Integer.class});
			arguments.add(new Object[] {new Integer(fingerprintId)});
			return null;
		}*/
		//return AP_SET_SVC.getByFingerprintId(fingerprintId);
		ApSet[] apSets = AP_SET_SVC.getByFingerprintId(fingerprintId);
		dbm.closeConnection();
		return apSets;
	}
	
	public ApSet[] getApSetByApId(Integer apId) throws SQLException {
		/*
		if (update == false) {
			methodName.add("getApSetByApId");
			parameterTypes.add(new Class[] {java.lang.Integer.class});
			arguments.add(new Object[] {new Integer(apId)});
			return null;
		}
		*/
		//return AP_SET_SVC.getByApId(apId);
		ApSet[] apSets = AP_SET_SVC.getByApId(apId);
		dbm.closeConnection();
		return apSets;
	}
	public boolean removeAllApSets() throws SQLException{
		if (update == false) {
			methodName.add("removeAllApSets");
			parameterTypes.add(null);
			arguments.add(null);
			return true;
		}
		return AP_SET_SVC.removeAll();
	}
	
	public boolean removeApSetByFingerprintId(Integer fingerprintId) throws SQLException{
		if (update == false) {
			methodName.add("removeApSetByFingerprintId");
			parameterTypes.add(new Class[] {java.lang.Integer.class});
			arguments.add(new Object[] {new Integer(fingerprintId)});
			return true;
		}
		return AP_SET_SVC.removeByFingerprintId(fingerprintId);
	}
	
	public boolean removeApSetByApId(Integer apId) throws SQLException{
		if (update == false) {
			methodName.add("removeApSetByApId");
			parameterTypes.add(new Class[] {java.lang.Integer.class});
			arguments.add(new Object[] {new Integer(apId)});
			return true;
		}
		return AP_SET_SVC.removeByApId(apId);
	}
	
	public Integer addApSet(ApSet apSet) throws SQLException{
		if (update == false) {
			methodName.add("addApSet");
			parameterTypes.add(new Class[] {kr.softwaremaestro.indoor.wrm.vo.ApSet.class});
			arguments.add(new Object[] {new ApSet(apSet)});
			return null;
		}
		return AP_SET_SVC.add(apSet);
	}
	
	// BaseMeasurepoinrtService
	public BaseMeasurepoint[] getAllBaseMeasurepoints() throws SQLException{
		/*
		if (update == false) {
			methodName.add("getAllBaseMeasurepoints");
			parameterTypes.add(null);
			arguments.add(null);
			return null;
		}
		*/
		//return BASE_MP_SVC.getAll();
		BaseMeasurepoint[] bmps = BASE_MP_SVC.getAll();
		dbm.closeConnection();
		return bmps;
	}
	
	public BaseMeasurepoint getBaseMeasurepointById(Integer id) throws SQLException {
		/*
		if (update == false) {
			methodName.add("getBaseMeasurepointById");
			parameterTypes.add(new Class[] {java.lang.Integer.class});
			arguments.add(new Object[] {new Integer(id)});
			return null;
		}
		*/
		//return BASE_MP_SVC.getById(id);
		BaseMeasurepoint bmp = BASE_MP_SVC.getById(id);
		//dbm.closeConnection();
		return bmp;
	}
	
	public boolean removeAllBaseMeasurepoints() throws SQLException{
		if (update == false) {
			methodName.add("removeAllBaseMeasurepoints");
			parameterTypes.add(null);
			arguments.add(null);
			return true;
		}
		return BASE_MP_SVC.removeAll();
	}
	
	public boolean removeBaseMeasurepointById(Integer id) throws SQLException{
		if (update == false) {
			methodName.add("removeBaseMeasurepointById");
			parameterTypes.add(new Class[] {java.lang.Integer.class});
			arguments.add(new Object[] {new Integer(id)});
			return true;
		}
		return BASE_MP_SVC.removeById(id);
	}
	
	public Integer addBaseMeasurepoint(BaseMeasurepoint baseMeasurepoint) throws SQLException{
		if (update == false) {
			methodName.add("addBaseMeasurepoint");
			parameterTypes.add(new Class[] {kr.softwaremaestro.indoor.wrm.vo.BaseMeasurepoint.class});
			arguments.add(new Object[] {new BaseMeasurepoint(baseMeasurepoint)});
			return null;
		}
		return BASE_MP_SVC.add(baseMeasurepoint);
	}
	
	public void modifyBaseMeasurepointById(BaseMeasurepoint baseMeasurepoint) throws SQLException {
		if (update == false) {
			methodName.add("modifyBaseMeasurepointById");
			parameterTypes.add(new Class[] {kr.softwaremaestro.indoor.wrm.vo.BaseMeasurepoint.class});
			arguments.add(new Object[] {new BaseMeasurepoint(baseMeasurepoint)});
		}
		BASE_MP_SVC.modifyById(baseMeasurepoint);
	}
	
	// FingerprintService
	public Fingerprint[] getAllFingerprints() throws SQLException{
		/*
		if (update == false) {
			methodName.add("getAllFingerprints");
			parameterTypes.add(null);
			arguments.add(null);
			return null;
		}
		*/
		//return FINGERPRINT_SVC.getAll();
		Fingerprint[] fingerprints = FINGERPRINT_SVC.getAll();
		dbm.closeConnection();
		return fingerprints;
	}
	
	public Fingerprint getFingerprintById(Integer id) throws SQLException {
		/*
		if (update == false) {
			methodName.add("getFingerprintById");
			parameterTypes.add(new Class[] {java.lang.Integer.class});
			arguments.add(new Object[] {new Integer(id)});
			return null;
		}
		*/
		//return FINGERPRINT_SVC.getById(id);
		Fingerprint fingerprint = FINGERPRINT_SVC.getById(id);
		dbm.closeConnection();
		return fingerprint;
	}
	
	public boolean removeAllFingerprints() throws SQLException{
		if (update == false) {
			methodName.add("removeAllFingerprints");
			parameterTypes.add(null);
			arguments.add(null);
			return true;
		}
		return FINGERPRINT_SVC.removeAll();
	}
	
	public boolean removeFingerprintById(Integer id) throws SQLException{
		if (update == false) {
			methodName.add("removeFingerprintById");
			parameterTypes.add(new Class[] {java.lang.Integer.class});
			arguments.add(new Object[] {new Integer(id)});
			return true;
		}
		return FINGERPRINT_SVC.removeById(id);
	}
	
	public Integer addFingerprint(Fingerprint fingerprint) throws SQLException{
		if (update == false) {
			methodName.add("addFingerprint");
			parameterTypes.add(new Class[] {kr.softwaremaestro.indoor.wrm.vo.Fingerprint.class});
			arguments.add(new Object[] {new Fingerprint(fingerprint)});
			return null;
		}
		return FINGERPRINT_SVC.add(fingerprint);
	}
	
	// FloorService
	public Floor[] getAllFloors() throws SQLException{
		Floor[] floors =  FLOOR_SVC.getAll();
		dbm.closeConnection();
		return floors;
		/*
		if (update == false) {
			methodName.add("getAllFloors");
			parameterTypes.add(new Class[] {});
			arguments.add(new Object[] {});
			return null;
		}
		*/
		//return FLOOR_SVC.getAll();
	}
	
	public Floor getFloorById(Integer id) throws SQLException {
		/*
		if (update == false) {
			methodName.add("getFloorById");
			//parameterTypes.add(new Class[] {java.lang.Integer.class});
			//arguments.add(new Object[] {new Integer(id)});
			parameterTypes.add(null);
			arguments.add(null);
			return null;
		}
		*/
		//return FLOOR_SVC.getById(id);
		Floor floor = FLOOR_SVC.getById(id);
		dbm.closeConnection();
		return floor;
	}
	
	public boolean removeAllFloors() throws SQLException{
		if (update == false) {
			methodName.add("removeAllFloors");
			parameterTypes.add(null);
			arguments.add(null);
			return true;
		}
		return FLOOR_SVC.removeAll();
	}
	
	public boolean removeFloorById(Integer id) throws SQLException{
		if (update == false) {
			methodName.add("removeFloorById");
			parameterTypes.add(new Class[] {java.lang.Integer.class});
			arguments.add(new Object[] {new Integer(id)});
			return true;
		}
		return FLOOR_SVC.removeById(id);
	}
	
	public Integer addFloor(Floor floor) throws SQLException{
		if (update == false) {
			methodName.add("addFloor");
			parameterTypes.add(new Class[] {kr.softwaremaestro.indoor.wrm.vo.Floor.class});
			arguments.add(new Object[] {new Floor(floor)});
			return null;
		}
		return FLOOR_SVC.add(floor);
	}
	
	public void modifyFloorById(Floor floor) throws SQLException {
		if (update == false) {
			methodName.add("modifyFloorById");
			parameterTypes.add(new Class[] {kr.softwaremaestro.indoor.wrm.vo.Floor.class});
			arguments.add(new Object[] {new Floor(floor)});
		}
		FLOOR_SVC.modifyById(floor);
	}	
	
	// MeasurepointLinkService
	public MeasurepointLink[] getAllMeasurepointLinks() throws SQLException{
		/*
		if (update == false) {
			methodName.add("getAllMeasurepointLinks");
			parameterTypes.add(null);
			arguments.add(null);
			return null;
		}
		*/
		//return MP_LINK_SVC.getAll();
		MeasurepointLink[] mpLinks = MP_LINK_SVC.getAll();
		dbm.closeConnection();
		return mpLinks;
	}
	
	public MeasurepointLink getMeasurepointLinkById(Integer id) throws SQLException {
		/*
		if (update == false) {
			methodName.add("getMeasurepointLinkById");
			parameterTypes.add(new Class[] {java.lang.Integer.class});
			arguments.add(new Object[] {new Integer(id)});
			return null;
		}
		*/
		//return MP_LINK_SVC.getById(id);
		MeasurepointLink mpLink = MP_LINK_SVC.getById(id);
		dbm.closeConnection();
		return mpLink;
	}
	
	public boolean removeAllMeasurepointLinks() throws SQLException{
		if (update == false) {
			methodName.add("removeAllMeasurepointLinks");
			parameterTypes.add(null);
			arguments.add(null);
			return false;
		}
		return MP_LINK_SVC.removeAll();
	}
	
	public boolean removeMeasurepointLinkById(Integer id) throws SQLException{
		if (update == false) {
			methodName.add("removeMeasurepointLinkById");
			parameterTypes.add(new Class[] {java.lang.Integer.class});
			arguments.add(new Object[] {new Integer(id)});
			return false;
		}
		return MP_LINK_SVC.removeById(id);
	}
	
	public Integer addMeasurepointLink(MeasurepointLink measurepointLink) throws SQLException{
		if (update == false) {
			methodName.add("addMeasurepointLink");
			parameterTypes.add(new Class[] {kr.softwaremaestro.indoor.wrm.vo.MeasurepointLink.class});
			arguments.add(new Object[] {new MeasurepointLink(measurepointLink)});
			return null;
		}
		return MP_LINK_SVC.add(measurepointLink);
	}
	
	public void modifyMeasurepointLinkById(MeasurepointLink measurepointLink) throws SQLException {
		if (update == false) {
			methodName.add("modifyMeasurepointLinkById");
			parameterTypes.add(new Class[] {kr.softwaremaestro.indoor.wrm.vo.MeasurepointLink.class});
			arguments.add(new Object[] {new MeasurepointLink(measurepointLink)});
		}
		MP_LINK_SVC.modifyById(measurepointLink);
	}	
	
	// MeasurepointService
	public Measurepoint[] getAllMeasurepoints() throws SQLException{
		/*
		if (update == false) {
			methodName.add("getAllMeasurepoints");
			parameterTypes.add(null);
			arguments.add(null);
			returew Class[] {});
			arguments.add(new Object[] {});
			return null;
		}
		*/
		//return FLOOR_SVC.getAll();
	}
	
	public Floor getFloorById(Integer id) throws SQLException {
		/*
		if (update == false) {
			methodName.add("getFloorById");
			//parameterTypes.add(new Class[] {java.lang.Integer.class});
			//arguments.add(new Object[] {new Integer(id)});
			parameterTypes.add(null);
			arguments.add(null);
			return null;
		}
		*/
		//return FLOOR_SVC.getById(id);
		Floor floor = FLOOR_SVC.getById(id);
		dbm.closeConnection();
		return floor;
	}
	
	public boolean removeAllFloors() throws SQLException{
		if (update == false) {
			methodName.add("removeAllFloors");
			parameterTypes.add(null);
			arguments.add(null);
			return true;
		}
		return FLOOR_SVC.removeAll();
	}
	
	public boolean removeFloorById(Integer id) throws SQLException{
		if (update == false) {
			methodName.add("removeFloorById");
			parameterTypes.add(new Class[] {java.lang.Integer.class});
			arguments.add(new Object[] {new Integer(id)});
			return true;
		}
		return FLOOR_SVC.removeById(id);
	}
	
	public Integer addFloor(Floor floor) throws SQLException{
		if (update == false) {
			methodName.add("addFloor");
			parameterTypes.add(new Class[] {kr.softwaremaestro.indoor.wrm.vo.Floor.class});
			arguments.add(new Object[] {new Floor(floor)});
			return null;
		}
		return FLOOR_SVC.add(floor);
	}
	
	public void modifyFloorById(Floor floor) throws SQLException {
		if (update == false) {
			methodName.add("modifyFloorById");
			parameterTypes.add(new Class[] {kr.softwaremaestro.indoor.wrm.vo.Floor.class});
			arguments.add(new Object[] {new Floor(floor)});
		}
		FLOOR_SVC.modifyById(floor);
	}	
	
	// MeasurepointLinkService
	public MeasurepointLink[] getAllMeasurepointLinks() throws SQLException{
		/*
		if (update == false) {
			methodName.add("getAllMeasurepointLinks");
			parameterTypes.add(null);
			arguments.add(null);
			return null;
		}
		*/
		//return MP_LINK_SVC.getAll();
		MeasurepointLink[] mpLinks = MP_LINK_SVC.getAll();
		dbm.closeConnection();
		return mpLinks;
	}
	
	public MeasurepointLink getMeasurepointLinkById(Integer id) throws SQLException {
		/*
		if (update == false) {
			methodName.add("getMeasurepointLinkById");
			parameterTypes.add(new Class[] {java.lang.Integer.class});
			arguments.add(new Object[] {new Integer(id)});
			return null;
		}
		*/
		//return MP_LINK_SVC.getById(id);
		MeasurepointLink mpLink = MP_LINK_SVC.getById(id);
		dbm.closeConnection();
		return mpLink;
	}
	
	public boolean removeAllMeasurepointLinks() throws SQLException{
		if (update == false) {
			methodName.add("removeAllMeasurepointLinks");
			parameterTypes.add(null);
			arguments.add(null);
			return false;
		}
		return MP_LINK_SVC.removeAll();
	}
	
	public boolean removeMeasurepointLinkById(Integer id) throws SQLException{
		if (update == false) {
			methodName.add("removeMeasurepointLinkById");
			parameterTypes.add(new Class[] {java.lang.Integer.class});
			arguments.add(new Object[] {new Integer(id)});
			return false;
		}
		return MP_LINK_SVC.removeById(id);
	}
	
	public Integer addMeasurepointLink(MeasurepointLink measurepointLink) throws SQLException{
		if (update == false) {
			methodName.add("addMeasurepointLink");
			parameterTypes.add(new Class[] {kr.softwaremaestro.indoor.wrm.vo.MeasurepointLink.class});
			arguments.add(new Object[] {new MeasurepointLink(measurepointLink)});
			return null;
		}
		return MP_LINK_SVC.add(measurepointLink);
	}
	
	public void modifyMeasurepointLinkById(MeasurepointLink measurepointLink) throws SQLException {
		if (update == false) {
			methodName.add("modifyMeasurepointLinkById");
			parameterTypes.add(new Class[] {kr.softwaremaestro.indoor.wrm.vo.MeasurepointLink.class});
			arguments.add(new Object[] {new MeasurepointLink(measurepointLink)});
		}
		MP_LINK_SVC.modifyById(measurepointLink);
	}	
	
	// MeasurepointService
	public Measurepoint[] getAllMeasurepoints() throws SQLException{
		/*
		if (update == false) {
			methodName.add("getAllMeasurepoints");
			parameterTypes.add(null);
			arguments.add(null);
			retur			arguments.add(null);
			return null;
		}
		*/
		//return WP_LINK_SVC.getAll();
		WaypointLink[] wayLinks = WP_LINK_SVC.getAll();
		dbm.closeConnection();
		return wayLinks;
	}
	
	public WaypointLink getWaypointLinkById(Integer id) throws SQLException {
		/*
		if (update == false) {
			methodName.add("getWaypointLinkById");
			parameterTypes.add(new Class[] {java.lang.Integer.class});
			arguments.add(new Object[] {new Integer(id)});
			return null;
		}
		*/
		//return WP_LINK_SVC.getById(id);
		WaypointLink wayLink = WP_LINK_SVC.getById(id);
		dbm.closeConnection();
		return wayLink;
	}
	
	public boolean removeAllWaypointLinks() throws SQLException{
		if (update == false) {
			methodName.add("removeAllWaypointLinks");
			parameterTypes.add(null);
			arguments.add(null);
			return false;
		}
		return WP_LINK_SVC.removeAll();
	}
	
	public boolean removeWaypointLinkById(Integer id) throws SQLException{
		if (update == false) {
			methodName.add("removeWaypointLinkById");
			parameterTypes.add(new Class[] {java.lang.Integer.class});
			arguments.add(new Object[] {new Integer(id)});
			return false;
		}
		return WP_LINK_SVC.removeById(id);
	}
	
	public Integer addWaypointLink(WaypointLink waypointLink) throws SQLException{
		if (update == false) {
			methodName.add("addWaypointLink");
			parameterTypes.add(new Class[] {kr.softwaremaestro.indoor.wrm.vo.WaypointLink.class});
			arguments.add(new Object[] {new WaypointLink(waypointLink)});
			return null;
		}
		return WP_LINK_SVC.add(waypointLink);
	}
	
	public void modifyWaypointLinkById(WaypointLink waypointLink) throws SQLException {
		if (update == false) {
			methodName.add("modifyWaypointById");
			parameterTypes.add(new Class[] {kr.softwaremaestro.indoor.wrm.vo.WaypointLink.class});
			arguments.add(new Object[] {new WaypointLink(waypointLink)});
		}
		WP_LINK_SVC.modifyById(waypointLink);
	}	
	
	// WaypointService
	public Waypoint[] getAllWaypoints() throws SQLException{
		/*
		if (update == false) {
			methodName.add("getAllWaypoints");
			parameterTypes.add(null);
			arguments.add(null);
			return null;
		}
		*/
		//return WP_SVC.getAll();
		Waypoint[] wps = WP_SVC.getAll();
		dbm.closeConnection();
		return wps;
	}
	
	public Waypoint getWaypointById(Integer id) throws SQLException {
		/*
		if (update == false) {
			methodName.add("getWaypointById");
			parameterTypes.add(new Class[] {java.lang.Integer.class});
			arguments.add(new Object[] {new Integer(id)});
			return null;
		}
		*/
		//return WP_SVC.getById(id);
		Waypoint wp = WP_SVC.getById(id);
		dbm.closeConnection();
		return wp;
	}
	
	public boolean removeAllWaypoints() throws SQLException{
		if (update == false) {
			methodName.add("removeAllWaypoints");
			parameterTypes.add(null);
			arguments.add(null);
			return false;
		}
		return WP_SVC.removeAll();
	}
	
	public boolean removeWaypointById(Integer id) throws SQLException{
		if (update == false) {
			methodName.add("removeWaypointById");
			parameterTypes.add(new Class[] {java.lang.Integer.class});
			arguments.add(new Object[] {new Integer(id)});
			return false;
		}
		return WP_SVC.removeById(id);
	}
	
	public Integer addWaypoint(Waypoint waypoint) throws SQLException{
		if (update == false) {
			methodName.add("addWaypoint");
			parameterTypes.add(new Class[] {kr.softwaremaestro.indoor.wrm.vo.Waypoint.class});
			arguments.add(new Object[] {new Waypoint(waypoint)});
			return null;
		}
		return WP_SVC.add(waypoint);
	}
	
	public void modifyWaypointById(Waypoint waypoint) throws SQLException {
		if (update == false) {
			methodName.add("modifyWaypointById");
			parameterTypes.add(new Class[] {kr.softwaremaestro.indoor.wrm.vo.Waypoint.class});
			arguments.add(new Object[] {new Waypoint(waypoint)});
		}
		WP_SVC.modifyById(waypoint);
	}	
	
	public boolean updateIntoDB() {
		try {
			update = true;
			dbm.closeConnection();
			dbm.getConnection().setAutoCommit(false);
			Class className = Class.forName("kr.softwaremaestro.indoor.wrm.service.MappingService");
			Object o = className.newInstance();
			for (int i = 0; i < methodName.size(); i++) {
				Log.d("MappingService:updateIntoDB()", "method : " + methodName.get(i));
				Method method = className.getMethod(methodName.get(i), parameterTypes.get(i));
				method.invoke(o, arguments.get(i));
			}
			dbm.getConnection().setAutoCommit(true);
			dbm.closeConnection();
			methodName.clear();
			parameterTypes.clear();
			arguments.clear();
			update = false;
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	public static boolean isUpdate() {
		return update;
	}
	public static void setUpdate(boolean update) {
		MappingService.update = update;
	}
}
