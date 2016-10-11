import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

/**
 * Class for storing Player information
 * @author opw
 *
 */
public class Player implements Comparable<Player>{
	private int row;
	private int col;
	private int numPokeBalls = 0;
	private ArrayList<Pokemon> pkmCaught;
	private ArrayList<Cell> pathVisited;
	private HashMap<Cell, Integer> optimalCellState;
	private int score = 0;
	
	/**
	 * Constructor
	 * @param row initial row position
	 * @param col initial column position 
	 */
	public Player(int row, int col) {
		this.row = row;
		this.col = col;
		pkmCaught = new ArrayList<Pokemon>();
		pathVisited = new ArrayList<Cell>();	
		optimalCellState = new HashMap<Cell, Integer>();
	}
	
	public Player(Player player) {
		this.row = player.row;
		this.col = player.col;
		this.numPokeBalls = player.numPokeBalls;
		this.pkmCaught = new ArrayList<Pokemon>();
		for(Pokemon p : player.pkmCaught) {
			this.pkmCaught.add(p);
		}
		this.pathVisited = new ArrayList<Cell>();
		for(Cell c : player.pathVisited) {
			this.pathVisited.add(c);
		}		
		this.optimalCellState = new HashMap<Cell, Integer>();
		player.optimalCellState.forEach((key, value) -> {this.optimalCellState.put(key, value);});
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
	
	public ArrayList<Pokemon> getPkmCaught() {
		return pkmCaught;
	}

	public ArrayList<Cell> getPathVisited() {
		return pathVisited;
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
	
	public void addCaughtPokemon(Pokemon pkm) {
		if(pkm != null)
			pkmCaught.add(pkm);
	}
	
	public void removeLastVisitedCell(Cell cell) {
		pathVisited.remove(pathVisited.lastIndexOf(cell));
	}
	
	public boolean hasVisitedCell(Cell cell) {
		return pathVisited.contains(cell);
	}
	
//	public boolean hasVisitedCell(int row, int col) {
//		return pathVisited.contains(new Cell(row, col));
//	}
	
	public boolean hasCaughtPokemon(Pokemon pkm) {
		return pkmCaught.contains(pkm);
	}
	
	public void printVistedPath() {
		System.out.println(pathVisited);
	}
	
	public int getNumDistinctPokemonType() {
		HashSet<String> uniquePkm = new HashSet<String>();
		for(Pokemon p : this.pkmCaught) {
			uniquePkm.add(p.getTypes());
		}
		return uniquePkm.size();
	}
	
	public int getMaxPokemonCP() {
		int maxCP = 0;
		for(Pokemon p : this.pkmCaught) {
			int currentPkmCP = p.getCp();
			if(currentPkmCP > maxCP) {
				maxCP = currentPkmCP;
			}
		}
		return maxCP;
	}
	
	/**
	 * Return score of a player
	 * scoring function = < NB + 5 * NP + 10 * NS + MCP - Steps>
	 * @return score
	 */
	public int getScore() {
		int score = 0;
		score += this.numPokeBalls;
		score += 5 * this.pkmCaught.size();
		score += 10 * getNumDistinctPokemonType();
		score += getMaxPokemonCP();
		score -= this.pathVisited.size() - 1;	// initial point is not count as a step		
		return score;
	}

	@Override
	public int compareTo(Player o) {
//		if(this.getNumPokeBalls() > o.getNumPokeBalls() || this.pkmCaught.size() > o.pkmCaught.size()) {
//			return 1;
//		}
		if(this.getScore() == o.getScore()){
			return 0;
		}	
		else if(this.getScore() > o.getScore()) {
			return 1;
		}
		return -1;
	}
	
	public void recordCurrentState(Cell cell) {
		if(cell != null)
			optimalCellState.put(cell, this.getScore());
	}
	
	public int getStateScore(Cell c) {
		if(optimalCellState.containsKey(c))
			return optimalCellState.get(c);
		return Integer.MIN_VALUE;
	}


}
