package kr.softwaremaestro.indoor.engine;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import kr.softwaremaestro.indoor.wrm.vo.Accesspoint;
import kr.softwaremaestro.indoor.wrm.vo.ApSet;
import kr.softwaremaestro.indoor.wrm.vo.BaseMeasurepoint;
import kr.softwaremaestro.indoor.wrm.vo.Fingerprint;
import kr.softwaremaestro.indoor.wrm.vo.Floor;
import kr.softwaremaestro.indoor.wrm.vo.Measurepoint;
import kr.softwaremaestro.indoor.wrm.vo.MeasurepointLink;
import kr.softwaremaestro.indoor.wrm.vo.Point;
import kr.softwaremaestro.indoor.wrm.vo.PointOfInterest;
import kr.softwaremaestro.indoor.wrm.vo.Waypoint;
import kr.softwaremaestro.indoor.wrm.vo.WaypointLink;
import android.content.Context;

public class WiFiRadioMap {
	// private String path = "/Users/limjunsung/Documents/";
	public static WiFiRadioMap instance=null; 
	private String delimiter = ",";
	private static List<Accesspoint> accesspoints = null;
	private static List<ApSet> apSets = null;
	private static List<Fingerprint> fingerprints = new ArrayList<Fingerprint>();
	private static List<BaseMeasurepoint> baseMeasurepoints = null;
	private static Map<String, BaseMeasurepoint> baseMeasurepointByCoordinateMap = new HashMap<String, BaseMeasurepoint>();
	private static Map<String, List<Fingerprint>> fingerprintsByCoordinateMap = null;
	private static Map<Integer, Fingerprint> fingerprintByFingerprintIdMap = new TreeMap<Integer, Fingerprint>();
	private static Map<String, Fingerprint> representativeFingerprintByCoordinateMap = null;
	private static List<Floor> floors = null;
	private static List<Measurepoint> measurepoints = null;
	private static Map<String, Measurepoint> measurepointByCoordinateMap = new HashMap<String, Measurepoint>();
	private static List<MeasurepointLink> measurepointLinks = null;
	private static List<PointOfInterest> pointOfInterests = null;
	private static List<Waypoint> waypoints = null;
	private static List<WaypointLink> waypointLinks = null;
	private static Map<Integer, List<ApSet>> apSetsByFingerprintIdMap = new TreeMap<Integer, List<ApSet>>();
	private static Map<Integer, Waypoint> waypointByWaypointIdMap = new TreeMap<Integer, Waypoint>();
	private static Map<Double, List<WaypointLink>> waypointLinksByZMap = new TreeMap<Double, List<WaypointLink>>();
	private static Map<Integer, Point> pointByFingerprintIdMap = new TreeMap<Integer, Point>();
	private Context context;

	private WiFiRadioMap(Context context) throws IOException {
		this.context = context;
		accesspoints = createAllAccesspointsFromExternalFile();
		apSets = createAllApSetsFromExternalFile();
		apSetsByFingerprintIdMap = createApSetsByFingerprintId();
		baseMeasurepoints = createAllBaseMeasurepointsFromExternalFile();
		fingerprintsByCoordinateMap = createAllFingerprintsFromExternalFile();
		floors = createAllFloorsFromExternalFile();
		measurepoints = createAllMeasurepointsFromExternalFile();
		measurepointLinks = createAllMeasurepointLinksFromExternalFile();
		waypoints = createAllWaypointsFromExternalFile();
		waypointLinks = createAllWaypointLinksFromExternalFile();
		pointOfInterests = createAllPointOfInterestsFromExternalFile();
		waypointByWaypointIdMap = createWaypointByWaypointId();
		waypointLinksByZMap = createWaypointLinksByZ();
		createRepresentativeFingerprintAndApSet();
	}
	
	public static WiFiRadioMap getinstance(Context c) throws IOException{
		if(instance==null){
				instance =  new WiFiRadioMap(c);
				return instance;
		}
		else
			return instance;
	}

	private Map<Double, List<WaypointLink>> createWaypointLinksByZ() {
		Map<Double, List<WaypointLink>> waypointLinksByZMap = new TreeMap<Double, List<WaypointLink>>();
		for (WaypointLink wayLink : waypointLinks) {
			Waypoint waypoint = waypointByWaypointIdMap.get(wayLink
					.getStartPointId());
			if (waypointLinksByZMap.get(waypoint.getZ()) == null) {
				waypointLinksByZMap.put(waypoint.getZ(),
						new ArrayList<WaypointLink>());
			}
			waypointLinksByZMap.get(waypoint.getZ()).add(wayLink);
		}
		return waypointLinksByZMap;
	}

	private Map<Integer, Waypoint> createWaypointByWaypointId() {
		Map<Integer, Waypoint> waypointByWaypointIdMap = new TreeMap<Integer, Waypoint>();
		for (Waypoint wPoint : waypoints) {
			waypointByWaypointIdMap.put(wPoint.getId(), wPoint);
		}
		return waypointByWaypointIdMap;
	}

	private Map<Integer, List<ApSet>> createApSetsByFingerprintId() {
		Map<Integer, List<ApSet>> fingerprintsMap = new TreeMap<Integer, List<ApSet>>();
		for (ApSet apSet : apSets) {
			if (fingerprintsMap.get(apSet.getFingerprintId()) == null) {
				fingerprintsMap.put(apSet.getFingerprintId(),
						new ArrayList<ApSet>());
			}
			fingerprintsMap.get(apSet.getFingerprintId()).add(apSet);
		}
		return fingerprintsMap;
	}

	private List<Accesspoint> createAllAccesspointsFromExternalFile()
			throws IOException {
		List<Accesspoint> result = new ArrayList<Accesspoint>();
		String fileName = "accesspoints.csv";
		InputStream is = context.getAssets().open(fileName);
		InputStreamReader isr = new InputStreamReader(is);
		BufferedReader br = new BufferedReader(isr);

		String readStr;

		br.readLine(); // 첫 줄 버림.
		while ((readStr = br.readLine()) != null) {
			String[] tokens = readStr.split(delimiter);
			Accesspoint ap = new Accesspoint();
			ap.setId(Integer.parseInt(tokens[0]));
			ap.setBssid(tokens[1]);
			ap.setSsid(isNull(tokens[2]) == true ? null : tokens[2]);
			ap.setCapabilities(isNull(tokens[3]) == true ? null : tokens[3]);
			ap.setFrequency(isNull(tokens[4]) == true ? null : Integer
					.parseInt(tokens[4]));
			result.add(ap);
		}
		return result;
	}

	private List<ApSet> createAllApSetsFromExternalFile() throws IOException {
		List<ApSet> result = new ArrayList<ApSet>();
		String fileName = "ap_set.csv";
		InputStream is = context.getAssets().open(fileName);
		InputStreamReader isr = new InputStreamReader(is);
		BufferedReader br = new BufferedReader(isr);
		String readStr;

		br.readLine(); // 첫 줄 버림.
		while ((readStr = br.readLine()) != null) {
			String[] tokens = readStr.split(delimiter);
			ApSet apSet = new ApSet();
			apSet.setFingerprintId(Integer.parseInt(tokens[0]));
			apSet.setApId(Integer.parseInt(tokens[1]));
			apSet.setSignalStrength(Integer.parseInt(tokens[2]));
			result.add(apSet);
		}
		return result;
	}

	private List<BaseMeasurepoint> createAllBaseMeasurepointsFromExternalFile()
			throws IOException {
		List<BaseMeasurepoint> result = new ArrayList<BaseMeasurepoint>();
		String fileName = "base_measurepoints.csv";
		InputStream is = context.getAssets().open(fileName);
		InputStreamReader isr = new InputStreamReader(is);
		BufferedReader br = new BufferedReader(isr);
		String readStr;

		br.readLine(); // 첫 줄 버림.
		while ((readStr = br.readLine()) != null) {
			String[] tokens = readStr.split(delimiter);
			BaseMeasurepoint bmPoint = new BaseMeasurepoint();
			bmPoint.setId(Integer.parseInt(tokens[0]));
			bmPoint.setX(Double.parseDouble(tokens[1]));
			bmPoint.setY(Double.parseDouble(tokens[2]));
			bmPoint.setZ(Double.parseDouble(tokens[3]));
			result.add(bmPoint);
			this.baseMeasurepointByCoordinateMap.put(
					String.valueOf(bmPoint.getX())
							+ String.valueOf(bmPoint.getY())
							+ String.valueOf(bmPoint.getZ()), bmPoint);
		}
		return result;
	}

	private List<PointOfInterest> createAllPointOfInterestsFromExternalFile()
			throws IOException {
		List<PointOfInterest> result = new ArrayList<PointOfInterest>();
		String fileName = "point_of_interests.csv";
		InputStream is = context.getAssets().open(fileName);
		InputStreamReader isr = new InputStreamReader(is);
		BufferedReader br = new BufferedReader(isr);
		String readStr;

		br.readLine(); // 첫 줄 버림.
		while ((readStr = br.readLine()) != null) {
			String[] tokens = readStr.split(delimiter);
			PointOfInterest poi = new PointOfInterest();
			poi.setId(Integer.parseInt(tokens[0]));
			poi.setName(tokens[1]);
			poi.setX(Double.parseDouble(tokens[2]));
			poi.setY(Double.parseDouble(tokens[3]));
			poi.setZ(Double.parseDouble(tokens[4]));
			result.add(poi);
		}
		return result;
	}

	private List<Measurepoint> createAllMeasurepointsFromExternalFile()
			throws IOException {
		List<Measurepoint> result = new ArrayList<Measurepoint>();
		String fileName = "measurepoints.csv";
		InputStream is = context.getAssets().open(fileName);
		InputStreamReader isr = new InputStreamReader(is);
		BufferedReader br = new BufferedReader(isr);
		String readStr;

		br.readLine(); // 첫 줄 버림.
		while ((readStr = br.readLine()) != null) {
			String[] tokens = readStr.split(delimiter);
			Measurepoint mPoint = new Measurepoint();
			mPoint.setId(Integer.parseInt(tokens[0]));
			mPoint.setX(Double.parseDouble(tokens[1]));
			mPoint.setY(Double.parseDouble(tokens[2]));
			mPoint.setZ(Double.parseDouble(tokens[3]));
			mPoint.setMpLinkId(Integer.parseInt(tokens[4]));
			result.add(mPoint);
			this.measurepointByCoordinateMap.put(
					String.valueOf(mPoint.getX())
							+ String.valueOf(mPoint.getY())
							+ String.valueOf(mPoint.getZ()), mPoint);
		}
		return result;
	}

	private Map<String, List<Fingerprint>> createAllFingerprintsFromExternalFile()
			throws IOException {
		Map<String, List<Fingerprint>> fingerprintsByCoordinate = new HashMap<String, List<Fingerprint>>();

		String fileName = "fingerprints.csv";
		InputStream is = context.getAssets().open(fileName);
		InputStreamReader isr = new InputStreamReader(is);
		BufferedReader br = new BufferedReader(isr);
		String readStr;

		br.readLine(); // 첫 줄 버림.
		while ((readStr = br.readLine()) != null) {
			String[] tokens = readStr.split(delimiter);
			Fingerprint fingerprint = new Fingerprint();
			fingerprint.setId(Integer.parseInt(tokens[0]));
			try {

				SimpleDateFormat sdf = new SimpleDateFormat(
						"yyyy-MM-dd HH:mm:ss");
				Date date = sdf.parse(tokens[1].substring(1,
						tokens[1].length() - 2));
				fingerprint.setTime(Timestamp.valueOf(sdf.format(date)));
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			fingerprint.setX(Double.parseDouble(tokens[2]));
			fingerprint.setY(Double.parseDouble(tokens[3]));
			fingerprint.setZ(Double.parseDouble(tokens[4]));

			String x = String.valueOf(fingerprint.getX());
			String y = String.valueOf(fingerprint.getY());
			String z = String.valueOf(fingerprint.getZ());
			if (fingerprintsByCoordinate.get(x + y + z) == null) {
				fingerprintsByCoordinate.put(x + y + z,
						new ArrayList<Fingerprint>());
			}
			fingerprintsByCoordinate.get(x + y + z).add(fingerprint);
		}

		return fingerprintsByCoordinate;
	}

	private void createRepresentativeFingerprintAndApSet() throws IOException {
		Map<String, Fingerprint> newFingerprintByCoordinateMap = new HashMap<String, Fingerprint>();
		List<ApSet> newApSets = new ArrayList<ApSet>();
		Map<Integer, List<ApSet>> newApSetsByFingerprintIdMap = new TreeMap<Integer, List<ApSet>>();

		int fingerprintId = 1;
		Fingerprint f;
		Timestamp time;
		Double x;
		Double y;
		Double z;

		for (String coordinate : fingerprintsByCoordinateMap.keySet()) {
			f = fingerprintsByCoordinateMap.get(coordinate).get(0);
			time = f.getTime();
			x = f.getX();
			y = f.getY();
			z = f.getZ();

			Map<Integer, List<Integer>> signalsByApId = new TreeMap<Integer, List<Integer>>();
			for (Fingerprint fingerprint : fingerprintsByCoordinateMap
					.get(coordinate)) {

				List<ApSet> apSets = apSetsByFingerprintIdMap.get(fingerprint
						.getId());
				if (apSets == null) {
					continue;
				}
				for (ApSet apSet : apSets) {
					if (signalsByApId.get(apSet.getApId()) == null) {
						signalsByApId.put(apSet.getApId(),
								new ArrayList<Integer>());
					}
					signalsByApId.get(apSet.getApId()).add(
							apSet.getSignalStrength());
				}
			}

			List<ApSet> representiveApSet = new ArrayList<ApSet>();
			for (Integer apId : signalsByApId.keySet()) {
				Integer avgSignalStrength = 0;
				for (Integer signalStrength : signalsByApId.get(apId)) {
					avgSignalStrength = avgSignalStrength + signalStrength;
				}
				avgSignalStrength = (int) ((double) avgSignalStrength / (double) signalsByApId
						.get(apId).size());
				representiveApSet.add(new ApSet(fingerprintId, apId,
						avgSignalStrength));
			}
			newApSets.addAll(representiveApSet);
			Fingerprint representiveFingerprint = new Fingerprint(
					fingerprintId, time, x, y, z);
			this.fingerprints.add(representiveFingerprint);
			this.fingerprintByFingerprintIdMap.put(fingerprintId,
					representiveFingerprint);
			newFingerprintByCoordinateMap.put(coordinate,
					representiveFingerprint);
			newApSetsByFingerprintIdMap.put(fingerprintId, representiveApSet);
			fingerprintId++;
		}

		this.apSets = newApSets;
		this.representativeFingerprintByCoordinateMap = newFingerprintByCoordinateMap;
		this.apSetsByFingerprintIdMap = newApSetsByFingerprintIdMap;
	}

	private List<Floor> createAllFloorsFromExternalFile() throws IOException {
		List<Floor> result = new ArrayList<Floor>();
		String fileName = "floors.csv";
		InputStream is = context.getAssets().open(fileName);
		InputStreamReader isr = new InputStreamReader(is);
		BufferedReader br = new BufferedReader(isr);
		String readStr;

		br.readLine(); // 첫 줄 버림.
		while ((readStr = br.readLine()) != null) {
			String[] tokens = readStr.split(delimiter);
			Floor floor = new Floor();
			floor.setId(Integer.parseInt(tokens[0]));
			floor.setName(tokens[1]);
			// tokens[2] : image file
			floor.setImageWidth(Integer.parseInt(tokens[3]));
			floor.setImageHeight(Integer.parseInt(tokens[4]));
			floor.setImageOriginX(Integer.parseInt(tokens[5]));
			floor.setImageOriginY(Integer.parseInt(tokens[6]));
			floor.setImageScale(Double.parseDouble(tokens[7]));
			floor.setZ(Double.parseDouble(tokens[8]));
			result.add(floor);
		}
		return result;
	}

	private List<MeasurepointLink> createAllMeasurepointLinksFromExternalFile()
			throws IOException {
		List<MeasurepointLink> result = new ArrayList<MeasurepointLink>();
		String fileName = "measurepoint_links.csv";
		InputStream is = context.getAssets().open(fileName);
		InputStreamReader isr = new InputStreamReader(is);
		BufferedReader br = new BufferedReader(isr);
		String readStr;

		br.readLine(); // 첫 줄 버림.
		while ((readStr = br.readLine()) != null) {
			String[] tokens = readStr.split(delimiter);
			MeasurepointLink mpLink = new MeasurepointLink();
			mpLink.setId(Integer.parseInt(tokens[0]));
			mpLink.setStartPointId(Integer.parseInt(tokens[1]));
			mpLink.setEndPointId(Integer.parseInt(tokens[2]));
			result.add(mpLink);
		}
		return result;
	}

	private List<WaypointLink> createAllWaypointLinksFromExternalFile()
			throws IOException {
		List<WaypointLink> result = new ArrayList<WaypointLink>();
		String fileName = "waypoint_links.csv";
		InputStream is = context.getAssets().open(fileName);
		InputStreamReader isr = new InputStreamReader(is);
		BufferedReader br = new BufferedReader(isr);
		String readStr;

		br.readLine(); // 첫 줄 버림.
		while ((readStr = br.readLine()) != null) {
			String[] tokens = readStr.split(delimiter);
			WaypointLink wayLink = new WaypointLink();
			wayLink.setId(Integer.parseInt(tokens[0]));
			wayLink.setStartPointId(Integer.parseInt(tokens[1]));
			wayLink.setEndPointId(Integer.parseInt(tokens[2]));
			result.add(wayLink);
		}
		return result;
	}

	private List<Waypoint> createAllWaypointsFromExternalFile()
			throws IOException {
		List<Waypoint> result = new ArrayList<Waypoint>();
		String fileName = "waypoints.csv";
		InputStream is = context.getAssets().open(fileName);
		InputStreamReader isr = new InputStreamReader(is);
		BufferedReader br = new BufferedReader(isr);
		String readStr;
		br.readLine(); // 첫 줄 버림.
		while ((readStr = br.readLine()) != null) {
			String[] tokens = readStr.split(delimiter);
			Waypoint wayPoint = new Waypoint();
			wayPoint.setId(Integer.parseInt(tokens[0]));
			wayPoint.setX(Double.parseDouble(tokens[1]));
			wayPoint.se		Integer avgSignalStrength = 0;
				for (Integer signalStrength : signalsByApId.get(apId)) {
					avgSignalStrength = avgSignalStrength + signalStrength;
				}
				avgSignalStrength = (int) ((double) avgSignalStrength / (double) signalsByApId
						.get(apId).size());
				representiveApSet.add(new ApSet(fingerprintId, apId,
						avgSignalStrength));
			}
			newApSets.addAll(representiveApSet);
			Fingerprint representiveFingerprint = new Fingerprint(
					fingerprintId, time, x, y, z);
			this.fingerprints.add(representiveFingerprint);
			this.fingerprintByFingerprintIdMap.put(fingerprintId,
					representiveFingerprint);
			newFingerprintByCoordinateMap.put(coordinate,
					representiveFingerprint);
			newApSetsByFingerprintIdMap.put(fingerprintId, representiveApSet);
			fingerprintId++;
		}

		this.apSets = newApSets;
		this.representativeFingerprintByCoordinateMap = newFingerprintByCoordinateMap;
		this.apSetsByFingerprintIdMap = newApSetsByFingerprintIdMap;
	}

	private List<Floor> createAllFloorsFromExternalFile() throws IOException {
		List<Floor> result = new ArrayList<Floor>();
		String fileName = "floors.csv";
		InputStream is = context.getAssets().open(fileName);
		InputStreamReader isr = new InputStreamReader(is);
		BufferedReader br = new BufferedReader(isr);
		String readStr;

		br.readLine(); // 첫 줄 버림.
		while ((readStr = br.readLine()) != null) {
			String[] tokens = readStr.split(delimiter);
			Floor floor = new Floor();
			floor.setId(Integer.parseInt(tokens[0]));
			floor.setName(tokens[1]);
			// tokens[2] : image file
			floor.setImageWidth(Integer.parseInt(tokens[3]));
			floor.setImageHeight(Integer.parseInt(tokens[4]));
			floor.setImageOriginX(Integer.parseInt(tokens[5]));
			floor.setImageOriginY(Integer.parseInt(tokens[6]));
			floor.setImageScale(Double.parseDouble(tokens[7]));
			floor.setZ(Double.parseDouble(tokens[8]));
			result.add(floor);
		}
		return result;
	}

	private List<MeasurepointLink> createAllMeasurepointLinksFromExternalFile()
			throws IOException {
		List<MeasurepointLink> result = new ArrayList<MeasurepointLink>();
		String fileName = "measurepoint_links.csv";
		InputStream is = context.getAssets().open(fileName);
		InputStreamReader isr = new InputStreamReader(is);
		BufferedReader br = new BufferedReader(isr);
		String readStr;

		br.readLine(); // 첫 줄 버림.
		while ((readStr = br.readLine()) != null) {
			String[] tokens = readStr.split(delimiter);