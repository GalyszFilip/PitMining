package isep.ii.project.pitMining;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;


public class PitMiningOperator<Vertex, Edge> {

	private DiGraph<Vertex, Edge> graph;
	private List<Vertex> excavated = new ArrayList<Vertex>();
	private int sommeProfit = 0;
	private int sommeProfitMf = 0;
	private MaxFlowOperator<Vertex, Edge> maxFlowOp;

	public void setSommeProfit(int sommeProfit) {
		this.sommeProfit = sommeProfit;
	}

	public PitMiningOperator(DiGraph<Vertex, Edge> g, MaxFlowOperator<Vertex, Edge> mo) {
		graph = g;
		this.maxFlowOp = mo;
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
	
	public void addProfitMf(Vertex v) {
		this.sommeProfitMf += graph.getProfit(v);
	}

	
	public int getSommeProfit() {
		return sommeProfit;
	}

	public int getSommeProfitMf() {
		return this.sommeProfitMf;
	}
	
	public List<Vertex> maxFlow(Vertex src, Vertex t) {
		return this.maxFlowOp.getCellsToExcavate(graph, src, t);

	}
	
	public boolean win() {
		return sommeProfit == sommeProfitMf;
	}	
	
	public void print(String s) {
		System.out.println(s);
	}


}
