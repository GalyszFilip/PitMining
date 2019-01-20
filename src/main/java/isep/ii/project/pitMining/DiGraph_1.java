package isep.ii.project.pitMining;
import java.util.List;
import java.util.Map;

public interface DiGraph<Vertex, Edge> {
	
	public void addVertex(Vertex v);

	public List<Vertex> getVertices();

	public List<Edge> getEdges();

	public List<Vertex> getAdjacentVertices(Vertex v);

	public void nameVertex(String name, Vertex v);

	public Vertex getVertexByName(String name);

	public String getNameOrNullByVertex(Vertex v);

	public List<String> getNames();

	public boolean areConnected(Vertex src, Vertex dest);

	public boolean areConnected(String src, String dest);

	public List<Vertex> shortestPath(Vertex src, Vertex dest);

	public List<Vertex> shortestPath(String src, String dest);
	
	public Map<Vertex, List<Edge>> getVertexToEdges();

	public void addEdge(Edge e, Vertex src, Vertex dest);

	public List<Integer> getProfits();
	
	public Integer getProfit(Vertex v);

	public void putProfit(Vertex v, int p);
	
	public Edge getEdge(Vertex v1, Vertex v2);
	
	public Vertex getSrc(Edge e);
	
	public Vertex getDest(Edge e);
	
	public void addEdgeMf(Edge e, Vertex src, Vertex dest);

}
