import java.util.Arrays;
import java.util.HashMap;

/**
 * Class for storing the map information
 * @author opw
 *
 */
public class Map implements Cloneable{
	private MapType[][] cells;
	private final int M;
	private final int N;
	public enum MapType {
		WALL, EMPTY, START, DEST, SUPPLY, POKEMON, VISITED
	}
//	private HashMap<Cell, Integer> optimalCellStates = new HashMap<Cell, Integer>();
	private HashMap<Player, Integer> optimalStates = new HashMap<Player, Integer>();
	/**
	 * Constructor
	 * @param M total rows in the map
	 * @param N total columns in the map
	 */
	public Map(int M, int N) {
		this.M = M;
		this.N = N;
		cells = new MapType[M][N];
	}
	
	/**
	 * Insert the cell type to the map
	 * @param row row number
	 * @param col column number
	 * @param type map type
	 */
	public void insertCell(int row, int col, MapType type) {
		if(row < M && col < N){
			cells[row][col] = type;
		}
	}

	/**
	 * Print map with readable symbols
	 */
	public void printPrettyMap() {
		for(int i = 0; i < M; i++) {
			for(int j = 0; j < N; j++) {
				MapType type = cells[i][j];
				char out = '?';
				switch(type) {
				case WALL:
					out = '#';
					break;
				case EMPTY:
					out = ' ';
					break;
				case START:
					out = 'B';
					break;
				case DEST:
					out = 'D';
					break;
				case SUPPLY:
					out = 'S';
					break;
				case POKEMON:
					out = 'P';
					break;
				case VISITED:
					out = '*';
					break;
				default:
					break;				
				}
				System.out.print(out);
			}
			System.out.println();
		}
	}

	/**
	 * Print map with detailed cell information
	 */
	public void printMap() {
		for(int i = 0; i < M; i++) {
			for(int j = 0; j < N; j++) {
				
				System.out.print(cells[i][j] + " ");
			}
			System.out.println();
		}
	}
	
	/**
	 * Set the cell player state
	 * @param playerState 
	 * @param score
	 */
	public void setPlayerState(Player playerState, int score) {
		if(playerState != null)
			optimalStates.put(playerState, score);
	}
	
	/**
	 * Get the cell player state
	 * @param playerState player state
	 * @return score
	 */
	public int getPlayerState(Player playerState) {
		if(optimalStates.containsKey(playerState))
			return optimalStates.get(playerState);
		return Integer.MIN_VALUE;
	}
	
	//--------------------
	// Getters and Setters
	//--------------------
	public MapType getCellType(int row, int col) {
		return cells[row][col];
	}
	
	public MapType getCellType(Cell cell) {
		return cells[cell.getRow()][cell.getCol()];
	}

	public int getM() {
		return M;
	}

	public int getN() {
		return N;
	}
		
	/**
	 * Clone map
	 */
	@Override
	public Map clone() throws CloneNotSupportedException {
		Map newMap = new Map(M, N);		
		for(int i = 0; i < M; i++) {
			for(int j = 0; j < N; j++) {
				newMap.cells[i][j] = this.cells[i][j];
			}
		}
		return newMap;
	}
}
