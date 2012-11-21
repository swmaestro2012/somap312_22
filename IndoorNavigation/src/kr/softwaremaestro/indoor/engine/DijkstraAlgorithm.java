package kr.softwaremaestro.indoor.engine;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import kr.softwaremaestro.indoor.wrm.vo.Link;
import kr.softwaremaestro.indoor.wrm.vo.Point;
import kr.softwaremaestro.indoor.wrm.vo.Waypoint;
import kr.softwaremaestro.indoor.wrm.vo.WaypointLink;
// reference : http://www.vogella.com/articles/JavaAlgorithmsDijkstra/article.html
public class DijkstraAlgorithm {

	private final List<Vertex> nodes;
	private final List<Edge> edges;
	private Set<Vertex> settledNodes;
	private Set<Vertex> unSettledNodes;
	private Map<Vertex, Vertex> predecessors;
	private Map<Vertex, Integer> distance;
	private Point startPoint;
	public DijkstraAlgorithm(List<Waypoint> waypoints,
			List<WaypointLink> wayLinks) {
		// Create a copy of the array so that we can operate on this array
		this.nodes = createVertex(waypoints);
		this.edges = createEdge(wayLinks);
	}

	public void execute(Vertex source) {
		settledNodes = new HashSet<Vertex>();
		unSettledNodes = new HashSet<Vertex>();
		distance = new HashMap<Vertex, Integer>();
		predecessors = new HashMap<Vertex, Vertex>();
		distance.put(source, 0);
		unSettledNodes.add(source);
		while (unSettledNodes.size() > 0) {
			Vertex node = getMinimum(unSettledNodes);
			settledNodes.add(node);
			unSettledNodes.remove(node);
			findMinimalDistances(node);
		}
	}
	
	public void execute(Point currentPos) {
		startPoint = currentPos;
		Vertex source = new Vertex(String.valueOf(currentPos.getId()), String.valueOf(currentPos.getId()), 
				currentPos.getX(), currentPos.getY(), currentPos.getZ());
		execute(source);
	}
	
	public void addEdge(Link link) {
		Vertex source = new Vertex(String.valueOf(link.getStartPointId()), String.valueOf(link.getStartPointId()), 
				getVertex(link.getStartPointId()).getX(), getVertex(link.getStartPointId()).getY(), getVertex(link.getStartPointId()).getZ());
		Vertex destination = new Vertex(String.valueOf(link.getEndPointId()), String.valueOf(link.getEndPointId()), 
				getVertex(link.getEndPointId()).getX(), getVertex(link.getEndPointId()).getY(), getVertex(link.getEndPointId()).getZ());
		Edge edge = new Edge(String.valueOf(link.getId()), source, destination);
		edge.setWeight(getEdgeWeight(edge));
		edges.add(edge);
	}
	public void addEdges(List<Link> links) {
		for(Link link : links) {
			Vertex source = new Vertex(String.valueOf(link.getStartPointId()), String.valueOf(link.getStartPointId()), 
					getVertex(link.getStartPointId()).getX(), getVertex(link.getStartPointId()).getY(), getVertex(link.getStartPointId()).getZ());
			Vertex destination = new Vertex(String.valueOf(link.getEndPointId()), String.valueOf(link.getEndPointId()), 
					getVertex(link.getEndPointId()).getX(), getVertex(link.getEndPointId()).getY(), getVertex(link.getEndPointId()).getZ());
			Edge edge = new Edge(String.valueOf(link.getId()), source, destination);
			edge.setWeight(getEdgeWeight(edge));
			edges.add(edge);
		}
	}
	public void removeEdge(Link link) {
		for(Edge edge : edges) {
			if (edge.getId().compareTo(String.valueOf(link.getId())) == 0) {
				edges.remove(edge);
				break;
			}
		}
	}
	public void removeEdges(List<Link> links) {
		for (Link link : links) {
			for(Edge edge : edges) {
				if (edge.getId().compareTo(String.valueOf(link.getId())) == 0) {
					edges.remove(edge);
					break;
				}
			}
		}
	}
	public void addVertex(Point point) {
		nodes.add(new Vertex(String.valueOf(point.getId()), String.valueOf(point.getId()), point.getX(), point.getY(), point.getZ()));
	}
	
	public void removeVertexs(List<Point> points) {
		for (Point point : points) {
			for (Vertex vertex : nodes) {
				if (vertex.getId().compareTo(String.valueOf(point.getId())) == 0) {
					nodes.remove(vertex);
					break;
				}
			}
		}
	}
	private Edge selectEdgeByLinkId(Integer id) {
		Edge resultEdge = null;
		for(Edge edge : edges) {
			if (edge.getId().compareTo(String.valueOf(id))==0) {
				resultEdge = edge;
				break;
			}
		}
		return resultEdge;
	}

	private List<Vertex> createVertex(List<Waypoint> waypoints) {
		List<Vertex> vertexList = new ArrayList<Vertex>();
		for(Waypoint waypoint : waypoints) {
			Vertex vertex = new Vertex(String.valueOf(waypoint.getId()), String.valueOf(waypoint.getId()), waypoint.getX(), waypoint.getY(), waypoint.getZ());
			vertexList.add(vertex);
		}
		return vertexList;
	}
	public Integer getLastVertexId() {
		Integer lastId = Integer.MIN_VALUE;
		for (Vertex vertex : nodes) {
			if (lastId < Integer.parseInt(vertex.getId())) {
				lastId = Integer.parseInt(vertex.getId());
			}
		}
		return lastId;
	}
	public Integer getLastEdgeId() {
		Integer lastId = Integer.MIN_VALUE;
		for (Edge edge : edges) {
			if (lastId < Integer.parseInt(edge.getId())) {
				lastId = Integer.parseInt(edge.getId());
			}
		}
		return lastId;
	}
	
	private List<Edge> createEdge(List<WaypointLink> wayLinks) {
		List<Edge> edgeList = new ArrayList<Edge>();
		Edge edge = null;
		Integer idx = 1;
		for (WaypointLink wayLink : wayLinks) {
			edge = new Edge(String.valueOf(idx), getVertex(wayLink.getStartPointId()), getVertex(wayLink.getEndPointId()));
			edge.setWeight(getEdgeWeight(edge));
			edgeList.add(edge);
			idx++;
			edge = new Edge(String.valueOf(idx), getVertex(wayLink.getEndPointId()), getVertex(wayLink.getStartPointId()));
			edge.setWeight(getEdgeWeight(edge));
			edgeList.add(edge);
			idx++;
		}
		return edgeList;
	}
	private Vertex getVertex(Integer vertexId) {
		for (Vertex vertex : nodes) {
			if (vertex.getId().compareTo(String.valueOf(vertexId)) == 0) {
				return vertex;
			}
		}
		return null;
	}
	
	private Vertex getVertex(String vertexId) {
		for (Vertex vertex : nodes) {
			if (vertex.getId().compareTo(vertexId) == 0) {
				return vertex;
			}
		}
		return null;
	}
	private int getEdgeWeight(Edge edge) {
		double absX = Math.abs(edge.getSource().getX() - edge.getDestination().getX());
		double absY = Math.abs(edge.getSource().getY() - edge.getDestination().getY());
		double absZ = Math.abs(edge.getSource().getZ() - edge.getDestination().getZ());
		double weight = Math.sqrt(Math.pow(absX, 2.0) + Math.pow(absY, 2.0) + Math.pow(absZ, 2.0));
		return (int)weight;
	}

	private void findMinimalDistances(Vertex node) {
		List<Vertex> adjacentNodes = getNeighbors(node);
		for (Vertex target : adjacentNodes) {
			if (getShortestDistance(target) > getShortestDistance(node)
					+ getDistance(node, target)) {
				distance.put(target,
						getShortestDistance(node) + getDistance(node, target));
				predecessors.put(target, node);
				unSettledNodes.add(target);
			}
		}

	}

	private int getDistance(Vertex node, Vertex target) {
		for (Edge edge : edges) {
			if (edge.getSource().equals(node)
					&& edge.getDestination().equals(target)) {
				return edge.getWeight();
			}
		}
		throw new RuntimeException("Should not happen");
	}

	private List<Vertex> getNeighbors(Vertex node) {
		List<Vertex> neighbors = new ArrayList<Vertex>();
		for (Edge edge : edges) {
			if (edge.getSource().equals(node)
					&& !isSettled(edge.getDestination())) {
				neighbors.add(edge.getDestination());
			}
		}
		return neighbors;
	}

	private Vertex getMinimum(Set<Vertex> vertexes) {
		Vertex minimum = null;
		for (Vertex vertex : vertexes) {
			if (minimum == null) {
				minimum = vertex;
			} else {
				if (getShortestDistance(vertex) < getShortestDistance(minimum)) {
					minimum = vertex;
				}
			}
		}
		return minimum;
	}

	private boolean isSettled(Vertex vertex) {
		return settledNodes.contains(vertex);
	}

	private int getShortestDistance(Vertex destination) {
		Integer d = distance.get(destination);
		if (d == null) {
			return Integer.MAX_VALUE;
		} else {
			return d;
		}
	}

	/*
	 * This method returns the path from the source to the selected target and
	 * NULL if no path exists
	 */
	public LinkedList<Vertex> getPath(Vertex target) {
		LinkedList<Vertex> path = new LinkedList<Vertex>();
		Vertex step = target;
		// Check if a path exists
		if (predecessors.get(step) == null) {
			return null;
		}
		path.add(step);			break;
			}
		}
		return resultEdge;
	}

	private List<Vertex> createVertex(List<Waypoint> waypoints) {
		List<Vertex> vertexList = new ArrayList<Vertex>();
		for(Waypoint waypoint : waypoints) {
			Vertex vertex = new Vertex(String.valueOf(waypoint.getId()), String.valueOf(waypoint.getId()), waypoint.getX(), waypoint.getY(), waypoint.getZ());
			vertexList.add(vertex);
		}
		return vertexList;
	}
	public Integer getLastVertexId() {
		Integer lastId = Integer.MIN_VALUE;
		for (Vertex vertex : nodes) {
			if (lastId < Integer.parseInt(vertex.getId())) {
				lastId = Integer.parseInt(vertex.getId());
			}
		}
		return lastId;
	}
	public Integer getLastEdgeId() {
		Integer lastId = Integer.MIN_VALUE;
		for (Edge edge : edges) {
			if (lastId < Integer.parseInt(edge.getId())) {
				lastId = Integer.parseInt(edge.getId());
			}
		}
		return lastId;
	}
	
	private List<Edge> createEdge(List<WaypointLink> wayLinks) {
		List<Edge> edgeList = new ArrayList<Edge>();
		Edge edge = null;
		Integer idx = 1;
		for (WaypointLink wayLink : wayLinks) {
			edge = new Edge(String.valueOf(idx), getVertex(wayLink.getStartPointId()), getVertex(wayLink.getEndPointId()));
			edge.setWeight(getEdgeWeight(edge));
			edgeList.add(edge);
			idx++;
			edge = new Edge(String.valueOf(idx), getVertex(wayLink.getEndPointId()), getVertex(wayLink.getStartPointId()));
			edge.setWeight(getEdgeWeight(edge));
			edgeList.add(edge);
			idx++;
		}
		return edgeList;
	}
	private Vertex getVertex(Integer vertexId) {
		for (Vertex vertex : nodes) {
			if (vertex.getId().compareTo(String.valueOf(vertexId)) == 0) {
				return vertex;
			}
		}
		return null;
	}
	
	private Vertex getVertex(String vertexId) {
		for (Vertex vertex : nodes) {
			if (vertex.getId().compareTo(vertexId) == 0) {
				return vertex;
			}
		}
		return null;
	}
	private int getEdgeWeight(Edge edge) {
		double absX = Math.abs(edge.getSource().getX() - edge.getDestination().getX());
		double absY = Math.abs(edge.getSource().getY() - edge.getDestination().getY());
		double absZ = Math.abs(edge.getSource().getZ() - edge.getDestination().getZ());
		double weight = Math.sqrt(Math.pow(absX, 2.0) + Math.pow(absY, 2.0) + Math.pow(absZ, 2.0));
		return (int)weight;
	}

	private void findMinimalDistances(Vertex node) {
		List<Vertex> adjacentNodes = getNeighbors(node);
		for (Vertex target : adjacentNodes) {
			if (getShortestDistance(target) > getShortestDistance(node)
					+ getDistance(node, target)) {
				distance.put(target,
						getShortestDistance(node) + getDistance(node, target));
				predecessors.put(target, node);
				unSettledNodes.add(target);
			}
		}

	}

	private int getDistance(Vertex node, Vertex target) {
		for (Edge edge : edges) {
			if (edge.getSource().equals(node)
					&& edge.getDestination().equals(target)) {
				return edge.getWeight();
			}
		}
		throw new RuntimeException("Should not happen");
	}

	private List<Vertex> getNeighbors(Vertex node) {
		List<Vertex> neighbors = n