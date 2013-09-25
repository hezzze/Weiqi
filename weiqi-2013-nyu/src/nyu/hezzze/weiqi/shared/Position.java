package nyu.hezzze.weiqi.shared;

/**
 * This class represents
 * a location relative to the board
 * 
 * @author hezzze
 *
 */
public class Position {

	/**
	 * row index
	 */
	int row;
	
	/**
	 * column index
	 */
	int col;

	public Position(int row, int col) {
		this.row = row;
		this.col = col;
	}

	/**
	 * Helper method
	 * @return the position to the north
	 * of the current position
	 */
	public Position north() {
		return new Position(row - 1, col);
	}

	/**
	 * Helper method
	 * @return the position to the south
	 * of the current position
	 */
	public Position south() {
		return new Position(row + 1, col);
	}

	/**
	 * Helper method
	 * @return the position to the west
	 * of the current position
	 */
	public Position west() {
		return new Position(row, col - 1);
	}

	/**
	 * Helper method
	 * @return the position to the east
	 * of the current position
	 */
	public Position east() {
		return new Position(row, col + 1);
	}

	/**
	 * Helper method
	 * @return the position to the northwest
	 * of the current position
	 */
	public Position northwest() {
		return new Position(row - 1, col - 1);
	}

	/**
	 * Helper method
	 * @return the position to the northeast
	 * of the current position
	 */
	public Position northeast() {
		return new Position(row - 1, col + 1);
	}

	/**
	 * Helper method
	 * @return the position to the southwest
	 * of the current position
	 */
	public Position southwest() {
		return new Position(row + 1, col - 1);
	}

	/**
	 * Helper method
	 * @return the position to the southeast
	 * of the current position
	 */
	public Position southeast() {
		return new Position(row + 1, col + 1);
	}

	@Override
	public String toString() {
		return "(" + row + ", " + col + ")";
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (!(obj instanceof Position)) {
			return false;
		}
		Position other = (Position) obj;
		return row == other.row && col == other.col;
	}

}
