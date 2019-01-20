package isep.ii.project.pitMining;

public class Cell2D implements Cell,Comparable<Cell> {

	protected int x;
	protected int y;

	public Cell2D(int a, int b) {
		x = a;
		y = b;
	}

	@Override
	public int hashCode() {
		return getName().hashCode();
	}

	@Override
	public boolean equals(Object other) {
		if (other == null || !(other instanceof Cell2D))
			return false;
		Cell2D otherCell = (Cell2D) other;
		return x == otherCell.x && y == otherCell.y;
	}

	public String getName() {
		return new String(x+ "," +y);
	}

	

	@Override
	public int compareTo(Cell c) {
		if(c instanceof Cell2D) {
			Cell2D c2 = (Cell2D)c;
			if (this.x < c2.x) {
				return -1;
			} else if (this.x == c2.x) {
				if (this.y < c2.y) {
					return -1;
				} else if (this.y == c2.y) {
					return 0;
				}
				return 1;
			}
			return 1;
		}return 0;
	}
}
