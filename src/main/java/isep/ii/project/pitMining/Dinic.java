package isep.ii.project.pitMining;

import java.util.List;

public class Dinic<Vertex, Edge> extends FordFulkerson<Vertex, Edge> implements MaxFlowOperator<Vertex, Edge>{
	
	@Override
	protected List<Integer> getPath(Vertex src, Vertex t) {
		return null;
	} 
	
}
