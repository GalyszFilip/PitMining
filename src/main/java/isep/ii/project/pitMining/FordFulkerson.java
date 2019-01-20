package isep.ii.project.pitMining;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.TreeSet;

public class FordFulkerson<Vertex, Edge> implements MaxFlowOperator<Vertex, Edge>{
	
	protected int maxFlow = 0;
	protected DiGraph<Vertex, Integer> rGraph;
	protected Map<Integer, Integer> flow = new HashMap<Integer, Integer>();
	protected Map<Integer, Integer> residual = new HashMap<Integer, Integer>();
	protected Set<Vertex> cellsToMine = new TreeSet<Vertex>();
	
	
	@Override
	public int getMaxFlow() {
		return maxFlow;
	}

	@Override
	public List<Vertex> getCellsToExcavate(DiGraph<Vertex, Edge> graph, Vertex src, Vertex t) {
		this.maxFlow(graph, src, t);
		return new ArrayList<Vertex>(cellsToMine);
	}
	
	public List<Vertex> getCellsToExcavate() {
		return new ArrayList<Vertex>(cellsToMine);
	}
	
	protected void reachableCells(Vertex src, Set<Vertex> visited) {
		cellsToMine.add(src);
		visited.add(src);
		for (Vertex v : rGraph.getAdjacentVertices(src)) {
			int e = rGraph.getEdge(src, v);
			if (flow.get(e) > 0 && !visited.contains(v)) {
				//System.out.println(((Cell) src).getName());
				reachableCells(v, visited);
			}
		}
		return;
	}
	
	protected List<Integer> getPath(Vertex src, Vertex t) {
		Queue<Vertex> toProcess = new LinkedList<Vertex>();
		Map<Vertex, Vertex> previousV = new HashMap<Vertex, Vertex>();
		Vertex visiting = src;
		toProcess.add(src);
		previousV.put(src, null);

		while (!visiting.equals(t) && !toProcess.isEmpty()) {
			visiting = toProcess.remove();
			for (Vertex v : rGraph.getAdjacentVertices(visiting)) {
				if (!previousV.containsKey(v) && !toProcess.contains(v) && flow.get(rGraph.getEdge(visiting, v)) > 0) {
					toProcess.add(v);
					previousV.put(v, visiting);
				}
			}
		}

		List<Integer> path = new ArrayList<Integer>();
		if (visiting.equals(t)) {
			Vertex v = t;
			while (previousV.get(v) != null) {
				path.add(rGraph.getEdge(previousV.get(v), v));
				v = previousV.get(v);
			} return path;
		} return null;
	}
	
	protected void createResidualGraph(DiGraph<Vertex, Edge> graph, Vertex src, Vertex t) {
		rGraph = new AdjacencyNetwork<Vertex, Integer>();
		rGraph.addVertex(src);
		rGraph.addVertex(t);
		int mfg = (int)Double.POSITIVE_INFINITY;
		@SuppressWarnings("unchecked")
		int i = Collections.max((ArrayList<Integer>)graph.getEdges())+1, p;
		int[]tab1, tab2;
		
		for (Vertex v : graph.getVertices()) {
			rGraph.addVertex(v);
			for (Vertex v2 : graph.getAdjacentVertices(v)) {
				tab1 = new int[] {(int)(graph.getEdge(v, v2)), mfg};
				tab2 = new int[] {++i, 0};
				addConnection(v, v2, tab1, tab2);
			}
			
			p = graph.getProfit(v);
			tab1 = new int[] {++i, Math.abs(p)};
			tab2 = new int[] {++i, 0};
			if (p > 0) {
				addConnection(src, v, tab1, tab2);
			} else if (p < 0) {
				addConnection(v, t, tab1, tab2);
			}
		}
	}
	
	protected void maxFlow(DiGraph<Vertex, Edge> graph, Vertex src, Vertex t) {
		
		this.createResidualGraph(graph, src, t);
		List<Integer> path;
		int path_Flow;
		
		while ((path = getPath(src, t)) != null) {
			
			path_Flow = flow.get(path.get(0));
			
			for (int e : path) {
				path_Flow = Math.min(path_Flow, flow.get(e));
			}
 
			for (int e : path) {
				flow.put(e, flow.get(e) - path_Flow);
				flow.put(residual.get(e), flow.get(residual.get(e)) + path_Flow);
			}

			this.maxFlow += path_Flow;
			print("max flow: " + maxFlow);

		}

		reachableCells(src, new HashSet<Vertex>());
		cellsToMine.remove(src);
	}
	
	protected void addConnection(Vertex src, Vertex dest, int[]i1, int[]i2) {
		this.rGraph.addEdgeMf(i1[0], src, dest);
		rGraph.addEdgeMf(i2[0], dest, src);
		this.flow.put(i1[0], i1[1]);
		this.flow.put(i2[0], i2[1]);
		residual.put(i1[0], i2[0]);
		residual.put(i2[0], i1[0]);
	}
	
	protected void print(String s) {
		System.out.println(s);
	}

}
