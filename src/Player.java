import java.util.ArrayList;

public class Player {
	private int row;
	private int col;
	private int numPokeBalls;
	private ArrayList<Pokemon> pkmCaught;
	private ArrayList<Cell> pathVisited;
	
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
	
	

}
