package isep.ii.project.pitMining;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Queue;

public class EdmondsKarp<Vertex, Edge> extends FordFulkerson<Vertex, Edge> implements MaxFlowOperator<Vertex, Edge>{
	
	@Override
	protected List<Integer> getPath(Vertex src, Vertex t) {
		Map<Vertex, Integer> toSource =  new HashMap<Vertex, Integer>();
		Queue<Vertex> toProcess = new PriorityQueue<Vertex>(rGraph.getVertices().size(), new myComparator<Vertex>(toSource));
		Map<Vertex, Vertex> previousV = new HashMap<Vertex, Vertex>();
		
		Vertex visiting = src;
		toSource.put(src, 0);
		toProcess.add(visiting);
		previousV.put(visiting, null);

		while (!visiting.equals(t) && !toProcess.isEmpty()) {
			visiting = toProcess.remove();
			for (Vertex v : rGraph.getAdjacentVertices(visiting)) {
				if (!previousV.containsKey(v) && !toProcess.contains(v) && flow.get(rGraph.getEdge(visiting, v)) > 0) {
					toSource.put(v, toSource.get(visiting)+ 1);
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
	
}
