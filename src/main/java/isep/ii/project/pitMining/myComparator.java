package isep.ii.project.pitMining;

import java.util.Comparator;
import java.util.Map;
public class myComparator<V> implements Comparator<V>{
	
	

	private Map<V, Integer> toCompare;
	
	public myComparator(Map<V, Integer> toCompare){
		this.toCompare = toCompare;
	}

	@Override
	public int compare(V o1, V o2) {
		return (toCompare.get(o1)).compareTo(toCompare.get(o2));
	}
}