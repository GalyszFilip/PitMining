package isep.ii.project.pitMining;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class AdjacencyNetwork<Vertex, Edge> implements DiGraph<Vertex, Edge> {

	protected Set<Vertex> vertices = new HashSet<Vertex>();
	protected Set<Edge> edges = new HashSet<Edge>();
	protected Map<Vertex, List<Edge>> vertexToEdges = new HashMap<Vertex, List<Edge>>();
	protected Map<Edge, Vertex> edgeToSrc = new HashMap<Edge, Vertex>();
	protected Map<Edge, Vertex> edgeToDest = new HashMap<Edge, Vertex>();
	protected Map<String, Vertex> nameToVertex = new HashMap<String, Vertex>();
	protected Map<Vertex, Integer> profits = new HashMap<Vertex, Integer>();

	public void addVertex(Vertex v) {
		if (!vertices.contains(v)) {
			vertices.add(v);
			vertexToEdges.put(v, new ArrayList<Edge>());
			nameToVertex.put(((Cell) v).getName(), v);

		}

	}

	public List<Vertex> getVertices() {
		return new ArrayList<Vertex>(vertices);
	}

	public List<Edge> getEdges() {
		return new ArrayList<Edge>(edges);
	}

	public List<Vertex> getAdjacentVertices(Vertex v) {
		List<Vertex> res = new ArrayList<Vertex>();
		for (Edge e : vertexToEdges.get(v)) {
			res.add(edgeToDest.get(e));
		}
		return res;
	}

	public void nameVertex(String name, Vertex v) {
		nameToVertex.put(name, v);
	}

	public Vertex getVertexByName(String name) {
		if (this.nameToVertex.containsKey(name)) {
			return nameToVertex.get(name);
		}
		return null;
	}

	public String getNameOrNullByVertex(Vertex v) {
		for (Map.Entry<String, Vertex> e : nameToVertex.entrySet()) {
			if (e.getValue().equals(v)) {
				return e.getKey();
			}
		}
		return null;
	}

	public List<String> getNames() {
		return new ArrayList<String>(nameToVertex.keySet());
	}

	public boolean areConnected(Vertex src, Vertex dest) {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean areConnected(String src, String dest) {
		// TODO Auto-generated method stub
		return false;
	}

	public List<Vertex> shortestPath(String src, String dest) {
		// TODO Auto-generated method stub
		return null;
	}

	public List<Vertex> shortestPath(Vertex src, Vertex dest) {
		// TODO Auto-generated method stub
		return null;
	}

	public Map<Vertex, List<Edge>> getVertexToEdges() {
		return vertexToEdges;
	}

	public void addEdge(Edge e, Vertex src, Vertex dest) {
		addVertex(src);
		if (dest != null) {
			addVertex(dest);
			edges.add(e);
			edgeToSrc.put(e, src);
			edgeToDest.put(e, dest);
			vertexToEdges.get(src).add(e);
		}
	}
	
	public void addEdgeMf(Edge e, Vertex src, Vertex dest) {
		addVertex(src);
		addVertex(dest);
		if(getEdge(src,dest) == null) {
			edges.add(e);
			edgeToSrc.put(e, src);
			edgeToDest.put(e, dest);
			vertexToEdges.get(src).add(e);
		}
		
	}
	
	
	public void putProfit(Vertex v, int p) {
		if(vertices.contains(v)) {
			profits.put(v, p);
		}
	}

	public List<Integer> getProfits() {
		return new ArrayList<Integer>(profits.values());
	}

	@Override
	public Integer getProfit(Vertex v) {
		return profits.get(v);
	}
	
	public Edge getEdge(Vertex v1, Vertex v2) {
		for(Edge e: vertexToEdges.get(v1)) {
			if(edgeToDest.get(e).equals(v2))
				return e;
		}return null;
	}

	@Override
	public Vertex getSrc(Edge e) {
		return edgeToSrc.get(e);
	}

	@Override
	public Vertex getDest(Edge e) {
		// TODO Auto-generated method stub
		return edgeToDest.get(e);
	}


	
	

}
