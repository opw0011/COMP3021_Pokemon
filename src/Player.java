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
	}
	
	public Player(Player player) {
		this.row = player.row;
		this.col = player.col;
		this.numPokeBalls = player.numPokeBalls;
		this.pkmCaught = new ArrayList<Pokemon>(player.pkmCaught);
		this.pathVisited = new ArrayList<Cell>(player.pathVisited);
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
	
	/**
	 * Get a pure player state without path visited
	 * @return
	 */
	public Player getPlayerState() {
		Player p = new Player(this.row, this.col);
		p.numPokeBalls = this.numPokeBalls;
		p.pkmCaught = new ArrayList<Pokemon>(this.pkmCaught);
		return p;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + col;
		result = prime * result + numPokeBalls;
		result = prime * result + ((pkmCaught == null) ? 0 : pkmCaught.hashCode());
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
		Player other = (Player) obj;
		if (col != other.col)
			return false;
		if (numPokeBalls != other.numPokeBalls)
			return false;
		if (pkmCaught == null) {
			if (other.pkmCaught != null)
				return false;
		} else if (!pkmCaught.equals(other.pkmCaught))
			return false;
		if (row != other.row)
			return false;
		return true;
	}

	
}
