import java.util.ArrayList;

/**
 * Class for storing Player information
 * @author opw
 *
 */
public class Player {
	private int row;
	private int col;
	private int numPokeBalls;
	private ArrayList<Pokemon> pkmCaught;
	private ArrayList<Cell> pathVisited;
	
	/**
	 * Constructor
	 * @param row initial row position
	 * @param col initial column position 
	 */
	public Player(int row, int col) {
		this.row = row;
		this.col = col;
		numPokeBalls = 0;
		pkmCaught = new ArrayList<Pokemon>();
		pathVisited = new ArrayList<Cell>();				
	}

	public int getRow() {
		return row;
	}

	public void setRow(int row) {
		this.row = row;
	}

	public int getCol() {
		return col;
	}

	public void setCol(int col) {
		this.col = col;
	}

	public int getNumPokeBalls() {
		return numPokeBalls;
	}

	public void setNumPokeBalls(int numPokeBalls) {
		this.numPokeBalls = numPokeBalls;
	}

	@Override
	public String toString() {
		return "Player [row=" + row + ", col=" + col + ", numPokeBalls=" + numPokeBalls + ", pkmCaught=" + pkmCaught
				+ ", pathVisited=" + pathVisited + "]";
	}
	
	
	public void addVistedCell(Cell cell) {
		if(cell != null)
			pathVisited.add(cell);
	}
	
//	public void addVistedCell(int col, int row) {
//		pathVisited.add(new Cell(col, row));
//	}
//	
//	public void removeLastVisitedCell(int col, int row) {
//		pathVisited.remove(pathVisited.lastIndexOf(new Cell(col, row)));
//	}
	
	public void removeLastVisitedCell(Cell cell) {
		pathVisited.remove(pathVisited.lastIndexOf(cell));
	}
	
	public boolean hasVisitedCell(Cell cell) {
		return pathVisited.contains(cell);
	}
	
	public boolean hasVisitedCell(int row, int col) {
		return pathVisited.contains(new Cell(row, col));
	}
	
	public void printVistedPath() {
		System.out.println(pathVisited);
	}
	
	

}
