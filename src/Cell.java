
public class Cell {
	private int row;
	private int col;
	private boolean canPass;
	
	public Cell(int row, int col, boolean canPass) {
		this.row = row;
		this.col = col;
		this.canPass = canPass;
	}

	public int getRow() {
		return row;
	}

	public int getCol() {
		return col;
	}

	public boolean isCanPass() {
		return canPass;
	}

	@Override
	public String toString() {
		return "Cell [row=" + row + ", col=" + col + ", canPass=" + canPass + "]";
	}
	
	
}
