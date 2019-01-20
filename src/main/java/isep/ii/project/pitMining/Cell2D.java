package isep.ii.project.pitMining;

public class Cell2D implements Cell,Comparable<Cell2D> {

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
	public int compareTo(Cell2D c) {
		c = (Cell2D) c;
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
