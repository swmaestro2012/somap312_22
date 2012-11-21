package kr.softwaremaestro.indoor.wrm.service;

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
import android.os.Message;

public class CopyOfMappingService {
	private static CopyOfMappingService mappingService = new CopyOfMappingService();
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
	private List<String> methodName = new ArrayList<String>();
	private List<Class> parameterTypes = new ArrayList<Class>();
	private List<Object> arguments = new ArrayList<Object>();
	private boolean update = false;
	private CopyOfMappingService() {
		try {
			// dbm = new DBManager();
			mHandler = new Handler() {

				@Override
				public void handleMessage(Message msg) {
					dbm = (DBManager) msg.obj;
					initObject();
				}

				private void initObject() {
					try {
						dbm.getConnection().setAutoCommit(false);
					} catch (SQLException e) {
						// TODO Auto-generated catch block
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
			};
			DBThread dbThread = new DBThread(mHandler, dbm);
			dbThread.setDaemon(true);
			dbThread.start();

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static CopyOfMappingService getInstance() {
		return mappingService;
	}
	
	public void setDBInfo(String host, String port, String dbname, String user, String pass) throws SQLException {
		if (dbm != null) {
			dbm.closeConnection();
		}
		dbm = new DBManager(host, port, dbname, user, pass);
	}
	
	class DBThread extends Thread {
		Handler mHandler = null;
		DBManager dbManager = null;
		public DBThread(Handler mHandler, DBManager dbManager) {
			this.mHandler = mHandler;
			this.dbManager = dbManager;
		}
		public void run() {
			try {
				dbManager = new DBManager();
				Message msg = Message.obtain();
				msg.obj = dbManager;
				mHandler.sendMessage(msg);
				} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	
	//AccesspointService
	public Accesspoint[] getAllAccesspoints() throws SQLException{
		if (update == false) {
			methodName.add("getAllAccesspoints");
			parameterTypes.add(null);
			arguments.add(null);
		}
		return ACC_SVC.getAll();
	}
	
	public Accesspoint getAccesspointById(Integer id) throws SQLException {
		if (update == false) {
			methodName.add("getAllAccesspointById");
			parameterTypes.add(java.lang.Integer.class);
			arguments.add(id);
		}
		return ACC_SVC.getById(id);
	}
	
	public boolean removeAllAccesspoints() throws SQLException{
		if (update == false) {
			methodName.add("removeAllAccesspoints");
			parameterTypes.add(null);
			arguments.add(null);
		}
		return ACC_SVC.removeAll();
	}
	
	public boolean removeAccesspointById(Integer id) throws SQLException{
		if (update == false) {
			methodName.add("removeAccesspointById");
			parameterTypes.add(java.lang.Integer.class);
			arguments.add(id);
		}
		return ACC_SVC.removeById(id);
	}
	
	public Integer addAccesspoint(Accesspoint accesspoint) throws SQLException{
		return ACC_SVC.add(accesspoint);
	}
	
	public void modifyAccesspointById(Accesspoint accesspoint) throws SQLException {
		ACC_SVC.modifyById(accesspoint);
	}
	
	//AccesspointService
	public ApSet[] getAllApSets() throws SQLException{
		return AP_SET_SVC.getAll();
	}
	
	public ApSet[] getApSetByFingerprintId(Integer fingerprintId) throws SQLException {
		return AP_SET_SVC.getByFingerprintId(fingerprintId);
	}
	
	public ApSet[] getApSetByApId(Integer apId) throws SQLException {
		return AP_SET_SVC.getByApId(apId);
	}
	public boolean removeAllApSets() throws SQLException{
		return AP_SET_SVC.removeAll();
	}
	
	public boolean removeApSetByFingerprintId(Integer fingerprintId) throws SQLException{
		return AP_SET_SVC.removeByFingerprintId(fingerprintId);
	}
	
	public boolean removeApSetByApId(Integer apId) throws SQLException{
		return AP_SET_SVC.removeByApId(apId);
	}
	
	public Integer addApSet(ApSet apSet) throws SQLException{
		return AP_SET_SVC.add(apSet);
	}
	
	// BaseMeasurepoinrtService
	public BaseMeasurepoint[] getAllBaseMeasurepoints() throws SQLException{
		return BASE_MP_SVC.getAll();
	}
	
	public BaseMeasurepoint getBaseMeasurepointById(Integer id) throws SQLException {
		return BASE_MP_SVC.getById(id);
	}
	
	public boolean removeAllBaseMeasurepoints() throws SQLException{
		return BASE_MP_SVC.removeAll();
	}
	
	public boolean removeBaseMeasurepointById(Integer id) throws SQLException{
		return BASE_MP_SVC.removeById(id);
	}
	
	public Integer addBaseMeasurepoint(BaseMeasurepoint baseMeasurepoint) throws SQLException{
		return BASE_MP_SVC.add(baseMeasurepoint);
	}
	
	public void modifyBaseMeasurepointById(BaseMeasurepoint baseMeasurepoint) throws SQLException {
		BASE_MP_SVC.modifyById(baseMeasurepoint);
	}
	
	// FingerprintService
	public Fingerprint[] getAllFingerprints() throws SQLException{
		if (update == false) {
			methodName.add("getAllFingerprints");
			parameterTypes.add(null);
			arguments.add(null);
		}
		return FINGERPRINT_SVC.getAll();
	}
	
	public Fingerprint getFingerprintById(Integer id) throws SQLException {
		if (update == false) {
			methodName.add("getFingerprintById");
			parameterTypes.add(java.lang.Integer.class);
			arguments.add(id);
		}
		return FINGERPRINT_SVC.getById(id);
	}
	
	public boolean removeAllFingerprints() throws SQLException{
		if (update == false) {
			methodName.add("removeAllFingerprints");
			parameterTypes.add(null);
			arguments.add(null);
		}
		return FINGERPRINT_SVC.removeAll();
	}
	
	public boolean removeFingerprintById(Integer id) throws SQLException{
		if (update == false) {
			methodName.add("removeFingerprintById");
			parameterTypes.add(java.lang.Integer.class);
			arguments.add(id);
		}
		return FINGERPRINT_SVC.removeById(id);
	}
	
	public Integer addFingerprint(Fingerprint fingerprint) throws SQLException{
		if (update == false) {
			methodName.add("addFingerprint");
			parameterTypes.add(kr.softwaremaestro.indoor.wrm.vo.Fingerprint.class);
			arguments.add(fingerprint);
		}
		return FINGERPRINT_SVC.add(fingerprint);
	}
	
	// FloorService
	public Floor[] getAllFloors() throws SQLException{
		if (update == false) {
			methodName.add("getAllFloors");
			parameterTypes.add(null);
			arguments.add(null);
		}
		return FLOOR_SVC.getAll();
	}
	
	public Floor getFloorById(Integer id) throws SQLException {
		if (update == false) {
			methodName.add("getFloorById");
			parameterTypes.add(java.lang.Integer.class);
			arguments.add(id);
		}
		return FLOOR_SVC.getById(id);
	}
	
	public boolean removeAllFloors() throws SQLException{
		if (update == false) {
			methodName.add("removeAllFloors");
			parameterTypes.add(null);
			arguments.add(null);
		}
		return FLOOR_SVC.removeAll();
	}
	
	public boolean removeFloorById(Integer id) throws SQLException{
		if (update == false) {
			methodName.add("removeFloorById");
			parameterTypes.add(java.lang.Integer.class);
			arguments.add(id);
		}
		return FLOOR_SVC.removeById(id);
	}
	
	public Integer addFloor(Floor floor) throws SQLException{
		if (update == false) {
			methodName.add("addFloor");
			parameterTypes.add(kr.softwaremaestro.indoor.wrm.vo.Floor.class);
			arguments.add(floor);
		}
		return FLOOR_SVC.add(floor);
	}
	
	public void modifyFloorById(Floor floor) throws SQLException {
		if (update == false) {
			methodName.add("modifyFloorById");
			parameterTypes.add(kr.softwaremaestro.indoor.wrm.vo.Floor.class);
			arguments.add(floor);
		}
		FLOOR_SVC.modifyById(floor);
	}	
	
	// MeasurepointLinkService
	public MeasurepointLink[] getAllMeasurepointLinks() throws SQLException{
		return MP_LINK_SVC.getAll();
	}
	
	public MeasurepointLink getMeasurepointLinkById(Integer id) throws SQLException {
		return MP_LINK_SVC.getById(id);
	}
	
	public boolean removeAllMeasurepointLinks() throws SQLException{
		return MP_LINK_SVC.removeAll();
	}
	
	public boolean removeMeasurepointLinkById(Integer id) throws SQLException{
		return MP_LINK_SVC.removeById(id);
	}
	
	public Integer addMeasurepointLink(MeasurepointLink measurepointLink) throws SQLException{
		return MP_LINK_SVC.add(measurepointLink);
	}
	
	public void modifyMeasurepointLinkById(MeasurepointLink measurepointLink) throws SQLException {
		MP_LINK_SVC.modifyById(measurepointLink);
	}	
	
	// MeasurepointService
	public Measurepoint[] getAllMeasurepoints() throws SQLException{
		return MP_SVC.getAll();
	}
	
	public Measurepoint getMeasurepointById(Integer id) throws SQLException {
		return MP_SVC.getById(id);
	}
	
	public boolean removeAllMeasurepoints() throws SQLException{
		return MP_SVC.removeAll();
	}
	
	public boolean removeMeasurepointById(Integer id) throws SQLException{
		return MP_SVC.removeById(id);
	}
	
	public Integer addMeasurepoint(Measurepoint measurepoint) throws SQLException{
		return MP_SVC.add(measurepoint);
	}
	
	public void modifyMeasurepointById(Measurepoint measurepoint) throws SQLException {
		MP_SVC.modifyById(measurepoint);
	}	
	
	// PointOfInterestService
	public PointOfInterest[] getAllPointOfInterests() throws SQLException{
		return POI_SVC.getAll();
	}
	
	public PointOfInterest getPointOfInterestById(Integer id) throws SQLException {
		return POI_SVC.getById(id);
	}
	
	public boolean removeAllPointOfInterests() throws SQLException{
		return POI_SVC.removeAll();
	}
	
	public boolean removePointOfInterestById(Integer id) throws SQLException{
		return POI_SVC.removeById(id);
	}
	
	public Integer addPointOfInterest(PointOfInterest pointOfInterest) throws SQLException{
		return POI_SVC.add(pointOfInterest);
	}
	
	public void modifyPointOfInterestById(PointOfInterest pointOfInterest) throws SQLException {
		POI_SVC.modifyById(pointOfInterest);
	}
	
	// WaypointLinkService
	public WaypointLink[] getAllWaypointLinks() throws SQLException{
		return WP_LINK_SVC.getAll();
	}
	
	public WaypointLink getWaypointLinkById(Integer id) throws SQLException {
		return WP_LINK_SVC.getById(id);
	}
	
	public boolean removeAllWaypointLinks() throws SQLException{
		return WP_LINK_SVC.removeAll();
	}
	
	public boolean removeWaypointLinkById(Integer id) throws SQLException{
		return WP_LINK_SVC.removeById(id);
	}
	
	public Integer addWaypointLink(WaypointLink waypointLink) throws SQLException{
		return WP_LINK_SVC.add(waypointLink);
	}
	
	public void modifyWaypointLinkById(WaypointLink waypointLink) throws SQLException {
		WP_LINK_SVC.modifyById(waypointLink);
	}	
	
	// WaypointService
	public Waypoint[] getAllWaypoints() throws SQLException{
		return WP_SVC.getAll();
	}
	
	public Waypoint getWaypointById(Integer id) throws SQLException {
		return WP_SVC.getById(id);
	}
	
	public boolean removeAllWaypoints() throws SQLException{
		return WP_SVC.removeAll();
	}
	
	public boolean removeWaypointById(Integer id) throws SQLException{
		return WP_SVC.removeById(id);
	}
	
	public Integer addWaypoint(Waypoint waypoint) throws SQLException{
		return WP_SVC.add(waypoint);
	}
	
	public void modifyWaypointById(Waypoint waypoint) throws SQLException {
		WP_SVC.modifyById(waypoint);
	}	
	
	public boolean updateIntoDB() {
		try {
			dbm.getConnection().commit();
			return true;
		} catch (SQLException e) {
			try {
				dbm.getConnection().rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			e.printStackTrace();
			return false;
		}
	}
}

