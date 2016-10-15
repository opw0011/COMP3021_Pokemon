import java.util.Arrays;
import java.util.HashMap;

/**
 * Class for storing the map information
 * @author opw
 *
 */
public class Map implements Cloneable{
	private MapType[][] cells;
	private final int M;	// max row number
	private final int N;	// max column number
	public enum MapType {
		WALL, EMPTY, START, DEST, SUPPLY, POKEMON, VISITED
	}
//	public HashMap<Player, Integer> optimalStates = new HashMap<Player, Integer>();
	private HashMap<Integer, Integer> optimalStates = new HashMap<Integer, Integer>();	// Player State Hash -> Score
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
	/**
	 * 
	 * @param playerStateHash player state hash value
	 * @param score score calculated in that state
	 */
	public void setPlayerState(int playerStateHash, int score) {
		if(optimalStates.containsKey(playerStateHash))
			optimalStates.remove(playerStateHash);
		optimalStates.put(playerStateHash, score);		
	}
	
	/**
	 * Get the cell player state hash value
	 * @param playerStateHash player state
	 * @return score max score stored in that state
	 */
	public int getPlayerState(int playerStateHash) {
		if(optimalStates.containsKey(playerStateHash))
			return optimalStates.get(playerStateHash);
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
