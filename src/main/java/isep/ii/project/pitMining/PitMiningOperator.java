package isep.ii.project.pitMining;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Deque;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.TreeSet;

public class PitMiningOperator<Vertex, Edge> {

	private DiGraph<Vertex, Edge> graph;
	private List<Vertex> excavated = new ArrayList<Vertex>();
	private int sommeProfit = 0;
	private int sommeProfitMf = 0;
	private DiGraph<Vertex, Integer> mfGraph;
	private Set<Vertex> cellsToMine = new TreeSet<Vertex>();


	public void setSommeProfit(int sommeProfit) {
		this.sommeProfit = sommeProfit;
	}

	public PitMiningOperator(DiGraph<Vertex, Edge> g) {
		graph = g;
	}

	public Deque<Vertex> cellsToExcavate(String s) {
		Deque<Vertex> toExcavate = new ArrayDeque<Vertex>();
		Queue<Vertex> toProcess = new LinkedList<Vertex>();
		Vertex currentV = graph.getVertexByName(s);
		toProcess.add(currentV);
		while (!toProcess.isEmpty()) {
			currentV = toProcess.remove();
			if (!excavated.contains(currentV)) {
				toExcavate.addFirst(currentV);
				addProfit(currentV);
				excavated.add(currentV);
			}
			for (Vertex v : graph.getAdjacentVertices(currentV)) {
				if (!excavated.contains(v)) {
					toProcess.add(v);
					toExcavate.addFirst(v);
					addProfit(v);
					excavated.add(v);
				}
			}
			
		}
		return toExcavate;
	}

	

	public DiGraph<Vertex, Edge> getGraph() {
		return graph;
	}

	private void addProfit(Vertex v) {
		this.sommeProfit += graph.getProfit(v);
	}
	
	public int getSommeProfit() {
		return sommeProfit;
	}
	
	public void addProfitMf(int s) {
		this.sommeProfitMf += s;
	}

	public int getSommeProfitMf() {
		return this.sommeProfitMf;
	}
	
	/**
	public void resetProfit() {
		this.sommeProfit = 0;
	}

	public void resetProfitMf() {
		this.sommeProfitMf = 0;
	}**/

	public void reachableCells(Map<Integer, Integer> f, Vertex src, Set<Vertex> visited) {
		cellsToMine.add(src);
		visited.add(src);
		for (Vertex v : mfGraph.getAdjacentVertices(src)) {
			int e = mfGraph.getEdge(src, v);
			if (f.get(e) > 0 && !visited.contains(v)) {
				//System.out.println(((Cell) src).getName());
				reachableCells(f, v, visited);
			}
		}
		return;
	}

	public List<Integer> bfs(Map<Integer, Integer> f, Vertex src, Vertex t) {
		Queue<Vertex> toProcess = new LinkedList<Vertex>();
		Map<Vertex, Vertex> previousV = new HashMap<Vertex, Vertex>();
		Vertex visiting = src;
		toProcess.add(src);
		previousV.put(src, null);

		while (!visiting.equals(t) && !toProcess.isEmpty()) {
			visiting = toProcess.remove();
			for (Vertex v : mfGraph.getAdjacentVertices(visiting)) {
				if (!previousV.containsKey(v) && !toProcess.contains(v) && f.get(mfGraph.getEdge(visiting, v)) > 0) {
					toProcess.add(v);
					previousV.put(v, visiting);
				}

			}
		}

		List<Integer> path = new ArrayList<Integer>();
		if (visiting.equals(t)) {
			Vertex v = t;
			while (previousV.get(v) != null) {
				path.add(mfGraph.getEdge(previousV.get(v), v));
				v = previousV.get(v);
			}

			return path;

		}
		return null;
	}

	public int maxFlow(Vertex src, Vertex t) {
		int mf = 0;
		mfGraph = new pitMiningGraph<Vertex, Integer>();
		mfGraph.addVertex(src);
		mfGraph.addVertex(t);
		Map<Integer, Integer> flow = new HashMap<Integer, Integer>();
		Map<Integer, Integer> residual = new HashMap<Integer, Integer>();
		int maxFlow = Collections.max(graph.getProfits()) * graph.getVertices().size();
		@SuppressWarnings("unchecked")
		int i = Collections.max((ArrayList<Integer>)graph.getEdges())+1, p, i1, i2, edge;

		for (Vertex v : graph.getVertices()) {
			mfGraph.addVertex(v);
			for (Vertex v2 : graph.getAdjacentVertices(v)) {
				i1 = ++i;
				edge = (int)(graph.getEdge(v, v2));
				mfGraph.addEdgeMf(edge, v, v2);
				mfGraph.addEdgeMf(i1, v2, v);
				flow.put(edge, maxFlow);
				flow.put(i1, 0);
				residual.put(edge, i1);
				residual.put(i1, edge);
				
			}
		}


		for (Vertex v : graph.getVertices()) {
			p = graph.getProfit(v);
			if (p > 0) {
				i1 = ++i;
				i2 = ++i;
				mfGraph.addEdgeMf(i1, src, v);
				mfGraph.addEdgeMf(i2, v, src);
				flow.put(i1, p);
				flow.put(i2, 0);
				residual.put(i1, i2);
				residual.put(i2, i1);

			} else if (p < 0) {
				i1 = ++i;
				i2 = ++i;
				mfGraph.addEdgeMf(i1, v, t);
				mfGraph.addEdgeMf(i2, t, v);
				flow.put(i1, -p);
				flow.put(i2, 0);
				residual.put(i1, i2);
				residual.put(i2, i1);

			}
		}

		List<Integer> path;
		int path_Flow = maxFlow;
		
		while ((path = bfs(flow, src, t)) != null) {

			path_Flow = maxFlow;
			for (int e : path) {
				print(mfGraph.getNameOrNullByVertex(mfGraph.getDest(e)));
				print("");
				path_Flow = Math.min(path_Flow, flow.get(e));
			}

			for (int e : path) {
				flow.put(e, flow.get(e) - path_Flow);
				flow.put(residual.get(e), flow.get(residual.get(e)) + path_Flow);
			}

			mf += path_Flow;
			print("max flow: " + mf);
			print("");
		}

		reachableCells(flow, src, new HashSet<Vertex>());
		cellsToMine.remove(src);
		return mf;
	}
	
	public boolean win() {
		return sommeProfit == sommeProfitMf;
	}	
	
	public void print(String s) {
		System.out.println(s);
	}
	public List<Vertex> getCellsMaxFlow() {
		return new ArrayList<Vertex>(cellsToMine);
	}

}
