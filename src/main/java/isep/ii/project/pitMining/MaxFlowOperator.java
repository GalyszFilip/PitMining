package isep.ii.project.pitMining;
import java.util.List;

public interface MaxFlowOperator<Vertex, Edge> {
	
	public int getMaxFlow();	
	public List<Vertex> getCellsToExcavate(DiGraph<Vertex, Edge> graph, Vertex src, Vertex t);
	public List<Vertex> getCellsToExcavate();
}
