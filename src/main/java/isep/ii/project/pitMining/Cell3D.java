package isep.ii.project.pitMining;

public class Cell3D implements Cell, Comparable<Cell3D>{
	
	protected int x;
	protected int y;
	protected int z;
	
	public Cell3D(int x, int y, int z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}

	@Override
	public String getName() {
		return x+","+y+","+z;
	}
	
	
	@Override
	public int compareTo(Cell3D c) {
		if (this.x < c.x) {
			return -1;
		} else if (this.x == c.x) {
			if (this.y < c.y) {
				return -1;
			} else if (this.y == c.y) {
				return 0;
			}
			return 1;
		}
		return 1;
	}

}
