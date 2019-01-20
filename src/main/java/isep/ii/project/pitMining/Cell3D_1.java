package isep.ii.project.pitMining;

public class Cell3D extends Cell2D implements Cell{

	protected int z;
	
	public Cell3D(int x, int y, int z) {
		super(x,y);
		this.z = z;
	}

	@Override
	public String getName() {
		return x+","+y+","+z;
	}
	
	
	@Override
	public int compareTo(Cell c) {
		if(c instanceof Cell3D) {
			Cell3D c2 = (Cell3D)c;
			if (this.x < c2.x) {
				return -1;
			} else if (this.x == c2.x) {
				if (this.y < c2.y) {
					return -1;
				} else if (this.y == c2.y) {
					if (this.z < c2.z) {
						return -1;
					} else if (this.z == c2.z) {
						return 0;
					}
				}return 1;
			}return 1;
		}return 0;
		
	}

}
