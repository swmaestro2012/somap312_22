package kr.softwaremaestro.indoor.wrm.service;

import java.sql.SQLException;

import kr.softwaremaestro.indoor.wrm.connection.ConnectionReaper;
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


public class BackupOfMappingService {
	private static BackupOfMappingService mappingService = new BackupOfMappingService();
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
	private ConnectionReaper cp;
	private BackupOfMappingService() {
		try {
			dbm = new DBManager();
			cp = new ConnectionReaper(dbm);
			cp.start();
			dbm.getConnection().setAutoCommit(false);
			
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
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static BackupOfMappingService getInstance() {
		return mappingService;
	}
	
	public void setDBInfo(String host, String port, String dbname, String user, String pass) throws SQLException {
		if (dbm != null) {
			dbm.closeConnection();
		}
		dbm = new DBManager(host, port, dbname, user, pass);
		cp = new ConnectionReaper(dbm);
		cp.start();
	}
	
	//AccesspointService
	public Accesspoint[] getAllAccesspoints() throws SQLException{
		return ACC_SVC.getAll();
	}
	
	public Accesspoint getAccesspointById(Integer id) throws SQLException {
		return ACC_SVC.getById(id);
	}
	
	public boolean removeAllAccesspoints() throws SQLException{
		return ACC_SVC.removeAll();
	}
	
	public boolean removeAccesspointById(Integer id) throws SQLException{
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
		return FINGERPRINT_SVC.getAll();
	}
	
	public Fingerprint getFingerprintById(Integer id) throws SQLException {
		return FINGERPRINT_SVC.getById(id);
	}
	
	public boolean removeAllFingerprints() throws SQLException{
		return FINGERPRINT_SVC.removeAll();
	}
	
	public boolean removeFingerprintById(Integer id) throws SQLException{
		return FINGERPRINT_SVC.removeById(id);
	}
	
	public Integer addFingerprint(Fingerprint fingerprint) throws SQLException{
		return FINGERPRINT_SVC.add(fingerprint);
	}
	
	// FloorService
	public Floor[] getAllFloors() throws SQLException{
		return FLOOR_SVC.getAll();
	}
	
	public Floor getFloorById(Integer id) throws SQLException {
		return FLOOR_SVC.getById(id);
	}
	
	public boolean removeAllFloors() throws SQLException{
		return FLOOR_SVC.removeAll();
	}
	
	public boolean removeFloorById(Integer id) throws SQLException{
		return FLOOR_SVC.removeById(id);
	}
	
	public Integer addFloor(Floor floor) throws SQLException{
		return FLOOR_SVC.add(floor);
	}
	
	public void modifyFloorById(Floor floor) throws SQLException {
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
		return WP_LINK_SVC.getAllpoint getBaseMeasurepointById(Integer id) throws SQLException {
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
		return FINGERPRINT_SVC.getAll();
	}
	
	public Fingerprint getFingerprintById(Integer id) throws SQLException {
		return FINGERPRINT_SVC.getById(id);
	}
	
	public boolean removeAllFingerprints() throws SQLException{
		return FINGERPRINT_SVC.removeAll();
	}
	
	public boolean removeFingerprintById(Integer id) throws SQLException{
		return FINGERPRINT_SVC.removeById(id);
	}
	
	public Integer addFingerprint(Fingerprint fingerprint) throws SQLException{
		return FINGERPRINT_SVC.add(fingerprint);
	}
	
	// FloorService
	public Floor[] getAllFloors() throws SQLException{
		return FLOOR_SVC.getAll();
	}
	
	public Floor getFloorById(Integer id) throws SQLException {
		return FLOOR_SVC.getById(id);
	}
	
	public boolean removeAllFloors() throws SQLExceptio