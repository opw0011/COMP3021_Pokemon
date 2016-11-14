package pokemon;
/**
 * Class for storing the cell 
 * @author opw
 *
 */
public class Cell {
	private int row;
	private int col;
	
	/**
	 * Constructor
	 * @param row row number
	 * @param col column number
	 */
	public Cell(int row, int col) {
		this.row = row;
		this.col = col;
	}
	
	//--------------------
	// Getters and Setters
	//--------------------
	public int getRow() {
		return row;
	}

	public int getCol() {
		return col;
	}

	public void setRow(int row) {
		this.row = row;
	}

	public void setCol(int col) {
		this.col = col;
	}

	//--------------------------
	// Auto Generated Functions
	//--------------------------
	@Override
	public String toString() {
		return "Cell [row=" + row + ", col=" + col + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + col;
		result = prime * result + row;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Cell other = (Cell) obj;
		if (col != other.col)
			return false;
		if (row != other.row)
			return false;
		return true;
	}	
}
