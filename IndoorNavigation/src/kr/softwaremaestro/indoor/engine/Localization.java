package kr.softwaremaestro.indoor.engine;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import kr.softwaremaestro.indoor.wrm.vo.ApSet;
import kr.softwaremaestro.indoor.wrm.vo.Fingerprint;
import kr.softwaremaestro.indoor.wrm.vo.Link;
import kr.softwaremaestro.indoor.wrm.vo.Point;
import kr.softwaremaestro.indoor.wrm.vo.WaypointLink;
import android.content.Context;

public class Localization {
	private Point currentPos = null;
	private Link currentLink = null;
	private Set<ApSet> userFingerprint = null;
	//private WiFiRadioMap WRM = null;
	private WiFiRadioMap WRM = null;
	private DijkstraAlgorithm dijkstra = null;
	private boolean firstGetPositionCall = true;
	private ParticleFilter particleFilter = null;
	/*
	public Localization(WiFiRadioMap WRM) {
		this.WRM = WRM;
		dijkstra = new DijkstraAlgorithm(WRM.getWaypoints(),WRM.getWaypointLinks());
	}
	*/
	public Localization(WiFiRadioMap WRM) {
		this.WRM = WRM;
		dijkstra = new DijkstraAlgorithm(WRM.getWaypoints(),WRM.getWaypointLinks());
	}
	
	public Point getPosition(Set<ApSet> fingerprint) {
		userFingerprint = fingerprint;
		Point p = runKNN(3);
		if (p == null) {
			return null;
		}
		if (firstGetPositionCall == false) {
			double dist = getDistanceOfTwoPoints(p, currentPos);
			particleFilter.correct(currentPos, 1/dist);
		}
		currentPos = mapMatching(p);
		if (firstGetPositionCall == true) {
			firstGetPositionCall = false;
			particleFilter = new ParticleFilter(currentPos, 1);
		}
		return currentPos;
	}
	public List<Point> getShortestPath(Point startPos, Point destination) {
		Point tmpCurrentPos = currentPos;
		Link tmpCurrentLink = currentLink;
		
		currentPos = startPos;
		currentLink = getNearestWaypointLinkFromDestination(startPos);
		List<Point> points = getShortestPath(destination);
		currentPos = tmpCurrentPos;
		currentLink = tmpCurrentLink;
		return points;
	}
	
	public List<Point> getShortestPath(Point destination) {
		List<Point> points = new ArrayList<Point>();
		if (currentPos == null) {
			return null;
		}
		else {
			List<Point> addedPoints = new ArrayList<Point>();
			List<Link> addedLinks = new ArrayList<Link>();
			List<Link> removedLinks = new ArrayList<Link>();
			Integer lastVertexId = dijkstra.getLastVertexId() + 1;
			Integer lastEdgeId = dijkstra.getLastEdgeId() + 1;
			
			currentPos.setId(lastVertexId++);
			dijkstra.addVertex(currentPos);
			addedPoints.add(currentPos);
			
			WaypointLink waypointLink = new WaypointLink(lastEdgeId++, currentPos.getId(), currentLink.getStartPointId());
			dijkstra.addEdge(waypointLink);
			addedLinks.add(waypointLink);
			
			waypointLink = new WaypointLink(lastEdgeId++, currentLink.getStartPointId(), currentPos.getId());
			dijkstra.addEdge(waypointLink);
			addedLinks.add(waypointLink);
			
			waypointLink = new WaypointLink(lastEdgeId++, currentPos.getId(), currentLink.getEndPointId());
			dijkstra.addEdge(waypointLink);
			addedLinks.add(waypointLink);
			
			waypointLink = new WaypointLink(lastEdgeId++, currentLink.getEndPointId(), currentPos.getId());
			dijkstra.addEdge(waypointLink);
			addedLinks.add(waypointLink);
			
			dijkstra.removeEdge(currentLink);
			removedLinks.add(currentLink);
			
			
			WaypointLink nearestWaypointLink = getNearestWaypointLinkFromDestination(destination);
			Point projectedPoint = getProjectedPoint(destination, nearestWaypointLink);
			
			destination.setId(lastVertexId++);
			dijkstra.addVertex(destination);
			addedPoints.add(destination);
			
			projectedPoint.setId(lastVertexId++);
			dijkstra.addVertex(projectedPoint);
			addedPoints.add(projectedPoint);
			
			waypointLink = new WaypointLink(lastEdgeId++, projectedPoint.getId(), destination.getId());
			dijkstra.addEdge(waypointLink);
			addedLinks.add(waypointLink);
			
			waypointLink = new WaypointLink(lastEdgeId++, destination.getId(), projectedPoint.getId());
			dijkstra.addEdge(waypointLink);
			addedLinks.add(waypointLink);
			
			waypointLink = new WaypointLink(lastEdgeId++, projectedPoint.getId(), nearestWaypointLink.getStartPointId());
			dijkstra.addEdge(waypointLink);
			addedLinks.add(waypointLink);
			
			waypointLink = new WaypointLink(lastEdgeId++, nearestWaypointLink.getStartPointId(), projectedPoint.getId());
			dijkstra.addEdge(waypointLink);
			addedLinks.add(waypointLink);
			
			waypointLink = new WaypointLink(lastEdgeId++, projectedPoint.getId(), nearestWaypointLink.getEndPointId());
			dijkstra.addEdge(waypointLink);
			addedLinks.add(waypointLink);
			
			waypointLink = new WaypointLink(lastEdgeId++, nearestWaypointLink.getEndPointId(), projectedPoint.getId());
			dijkstra.addEdge(waypointLink);
			addedLinks.add(waypointLink);
			
			dijkstra.removeEdge(nearestWaypointLink);
			removedLinks.add(nearestWaypointLink);
			
			dijkstra.execute(currentPos);
			points = dijkstra.getPath(destination);
			
			dijkstra.removeVertexs(addedPoints);
			dijkstra.removeEdges(addedLinks);
			dijkstra.addEdges(removedLinks);
			return points;
		}
	}
	
	private WaypointLink getNearestWaypointLinkFromDestination(Point destination) {
		Point tmpPoint = null;
		Double minDistance = Double.POSITIVE_INFINITY;
		Double distance = Double.POSITIVE_INFINITY;
		WaypointLink resultLink = null;
		for(WaypointLink waypointLink : WRM.getWaypointLinks()) {
			tmpPoint = getProjectedPoint(destination, waypointLink);
			distance = getDistanceOfTwoPoints(destination, tmpPoint);
			if (minDistance > distance) {
				minDistance = distance;
				resultLink = waypointLink;
			}
		}
		return resultLink;
	}

	private Point mapMatching(Point p) {
		Point startPoint = null;
		Point endPoint = null;
		Double distance = null;
		Double shortedDistance = Double.POSITIVE_INFINITY;
		WaypointLink nearestLink = null;
		for (WaypointLink wayLink : WRM.getWaypointLinks(p.getZ())) {
			startPoint = WRM.getWaypointByWaypointId(wayLink.getStartPointId());
			endPoint = WRM.getWaypointByWaypointId(wayLink.getEndPointId());
			// 점과 벡터의 최단 거리 
			// 참고 : http://blog.naver.com/PostView.nhn?blogId=helloktk&logNo=80025453535&parentCategoryNo=4&viewDate=&currentPage=1&listtype=0
			distance = getDistanceBetweenPointAndVector(p, startPoint, endPoint);
			if (distance < shortedDistance) {
				shortedDistance = distance;
				nearestLink = wayLink;
			}
		}
		currentLink = nearestLink;
		return getProjectedPoint(p, nearestLink);
	}
	
	// 점에서 직선에 내린 수선의 교점 생성 및 리턴
	private Point getProjectedPoint(Point p, WaypointLink nearestLink) {
		Point projectedPoint = new Point();
		
		if(!isPointOnVerticalAreaOfLink(p, nearestLink)) {
			Double distance = getDistanceOfTwoPoints(p, WRM.getWaypointByWaypointId(nearestLink.getStartPointId()));
			if(distance > getDistanceOfTwoPoints(p, WRM.getWaypointByWaypointId(nearestLink.getEndPointId()))) 
				projectedPoint = WRM.getWaypointByWaypointId(nearestLink.getEndPointId());
			else
				projectedPoint = WRM.getWaypointByWaypointId(nearestLink.getStartPointId());
			
			return projectedPoint;
		}
		
		Point startPoint = WRM.getWaypointByWaypointId(nearestLink.getStartPointId());
		Point endPoint = WRM.getWaypointByWaypointId(nearestLink.getEndPointId());
		
		double s = (endPoint.getY() - startPoint.getY()) / (endPoint.getX() - startPoint.getX());
		if(s == Double.POSITIVE_INFINITY)
			s = 1/0.0000001d;
		else if(s == Double.NEGATIVE_INFINITY)
			s = -1/0.0000001d;
		else if(s == 0.0d)
			s = 0.0000001d;
		
		double B = 1;
		double C = s*startPoint.getX() - startPoint.getY();
		
		double a = p.getX();
		double b = p.getY();
		
		double c = (a + b*s + C*s/B) / (Math.pow(s,2) + 1);
		double d = (a*s + b*Math.pow(s,2) + C/B*Math.pow(s,2)) / (Math.pow(s,2)+1) - C/B;
		
		projectedPoint.setX(c);
		projectedPoint.setY(d);
		projectedPoint.setZ(p.getZ());
		
		return projectedPoint;
	}

	private Double getDistanceOfTwoPoints(Point p1, Point p2) {
		Double FLOOR_DISTANCE = 100.0;
		Double p1Z = p1.getZ();
		Double p2Z = p2.getZ();
		if(p1Z < 0)
			p1Z++;
		
		if(p2Z < 0)
			p2Z++;
		
		Double distance = Math.pow(p2.getX() - p1.getX(),2) + Math.pow(p2.getY() - p1.getY(),2) + Math.pow((p2Z - p1Z) * FLOOR_DISTANCE, 2);
		distance = Math.sqrt(distance);
		
		return distance;
	}
	
	private boolean isPointOnVerticalAreaOfLink(Point p, WaypointLink wayLink) {
		Point startPoint = WRM.getWaypointByWaypointId(wayLink.getStartPointId());
		Point endPoint = WRM.getWaypointByWaypointId(wayLink.getEndPointId());
		
		// Not on the same floor
		if(p.getZ().compareTo(startPoint.getZ()) != 0 || p.getZ().compareTo(endPoint.getZ()) != 0)
			return false;
		
		Double s = (endPoint.getY() - startPoint.getY()) / (endPoint.getX() - startPoint.getX());
		if(s == Double.POSITIVE_INFINITY)
			s = 1/0.00000000000000000001d;
		else if(s == Double.NEGATIVE_INFINITY)
			s = -1/0.00000000000000000001d;
		else if(s == 0.0d)
			s = 0.00000000000000000001d;
		
		// 점 (a,b)를 지나고 기울기가 s에 수직인 직선 f(x,y)=Ax+By+C에서 A = 1/s, B = 1, C = -a/s - b
		// startPoint에 대한 직선 파라미터 구하기
		Double A = 1/s;
		Double B = 1.0;
		Double startPointC = -1 * startPoint.getX() / s - startPoint.getY();

		// endPoint에 대한 직선 파라미터 구하기
		Double endPointC = -1 * endPoint.getX() / s - endPoint.getY();
		// startPoint과 endPoint에 p의 좌표를 넣은 값의 곱으로 판별변수를 생성함
		Double D = 1.0;
		D *= A * p.getX() + B * p.getY() + startPointC;
		D *= A * p.getX() + B * p.getY() + endPointC;
		
		// 판별변수의 부호로 판단
		return (D <= 0);
	}

	private Double getDistanceBetweenPointAndVector(Point p, Point startPoint,
			Point endPoint) {
		Double segmentMag = Math.pow(endPoint.getX() - startPoint.getX(), 2.0) + Math.pow(endPoint.getY() - startPoint.getY(), 2.0);
		Double u = ((p.getX() - startPoint.getX()) * (endPoint.getX() - startPoint.getX()) + (p.getY() - startPoint.getY()) * (endPoint.getY() - startPoint.getY())) / segmentMag;
		Double xp = startPoint.getX() + u * (endPoint.getX() - startPoint.getX());
		Double yp = startPoint.getY() + u * (endPoint.getY() - startPoint.getY());
		return Math.sqrt(Math.pow((xp-p.getX()), 2.0) + Math.pow((yp-p.getY()), 2.0));
	}

	private Map<Double, Integer> getFingerprintIdBySignalDistance() {
		Map<Double, Integer> fingerprintIdBySignalDistanceMap = new TreeMap<Double, Integer>();
		List<Point> pointList = null;
		Double distance = null;
		Integer numOfComparedAps = null;
		for (String coordinate : WRM.getRepresentativeFingerprintByCoordinateMap().keySet()) {
			distance = 0.0;
			numOfComparedAps = 0;
			Fingerprint fingerprint = WRM.getRepresentativeFingerprintByCoordinateMap().get(coordinate);
			for(ApSet wrmApSet : WRM.getApSetsByFingerprintId(fingerprint.getId())) {
				for(ApSet userApSet : userFingerprint) {
					if (userApSet.getApId() == wrmApSet.getApId()) {
						numOfComparedAps++;
						distance = distance + Math.abs((double)userApSet.getSignalStrength() - wrmApSet.getSignalStrength());
					}
				}
			}
			if (distance == 0.0 || numOfComparedAps == 0) {
				continue;
			}
			distance = distance / numOfComparedAps;
			fingerprintIdBySignalDistanceMap.put(distance, fingerprint.getId());
		}
		return fingerprintIdBySignalDistanceMap;
	}
	
	private Point runKNN(Integer k) {
		Point estimatedPoint = null;
		Map<Double, Integer> fingerprintIdBySignalDistanceMap = getFingerprintIdBySignalDistance();
		if (fingerprintIdBySignalDistanceMap.size() == 0) {
			return null;
		}
		List<Point> kPoints = new ArrayList<Point>();
		Integer idx = 0;
		for(Double signalDistance : fingerprintIdBySignalDistanceMap.keySet()) {
			Integer fingerprintId = fingerprintIdBySignalDistanceMap.get(signalDistance);
			kPoints.add(WRM.getPointByFingerprintId(fingerprintId));
		}
		estimatedPoint = getCenterPoint(kPoints, k);
		return estimatedPoint;
	}

	private Point getCenterPoint(List<Point> kPoints, Integer k) {
		Point centerPoint = new Point();
		centerPoint.setId(-1);
		centerPoint.setX(0.0);
		centerPoint.setY(0.0);
		centerPoint.setZ(0.0);
		Double initialZ = kPoints.get(0).getZ();
		Integer idx = 0;
		for (Point p : kPoints) {
			if (++idx > k) {
				break;
			}
			System.out.println("X : " + p.getX());
			System.out.println("Y : " + p.getY());
			System.out.println("Z : " + p.getZ());
			centerPoint.setX(centerPoint.getX() + p.getX());
			centerPoint.setY(centerPoint.getY() + p.getY());
			centerPoint.setZ(centerPoint.getZ() + p.getZ());
		}
		centerPoint.setX(centerPoint.getX()/k);
		centerPoint.setY(centerPoint.getY()/k);
		centerPoint.setZ(centerPoint.getZ()/k);
		return initialZ.compareTo(centerPoint.getZ()) == 0 ? centerPoint : null;
	}
}
