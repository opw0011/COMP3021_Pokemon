import java.util.Arrays;

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
	
//	private Player[][] cellState;
	
	/**
	 * Constructor
	 * @param M total rows in the map
	 * @param N total columns in the map
	 */
	public Map(int M, int N) {
		this.M = M;
		this.N = N;
		cells = new MapType[M][N];
//		cellState = new Player[M][N];
	}
	
//	public void setCellState(Player player) {
//		int r = player.getRow();
//		int c = player.getCol();
//		
//		if(cellState[r][c] == null) {
//			cellState[r][c] = player;
//		}
//	}
//	
//	public boolean stateHasImproved(Player player) {
//		int r = player.getRow();
//		int c = player.getCol();
//		if(cellState[r][c] == null) {
//			cellState[r][c] = player;
//			return true;
//		}
//		
//		// state not improved
//		if(player.compareTo(cellState[r][c]) > 0){
//			cellState[r][c] = player;	// set player as optimal state
//			return true;
//		}
//		
//		return false;		
////		System.out.println("COMPARING:::: " + cellState[r][c] + " &&& " + player);
////		return (player.compareTo(cellState[r][c]) > 0);
//	}
	
//	public void printMapStates() {
//		for(int i = 0; i < M; i++) {
//			for(int j = 0; j < N; j++) {
//				if(cellState[i][j] != null)
//					System.out.println(i + "," + j + " == " + cellState[i][j]);
//			}
//		}
//	}
//	
	/**
	 * Insert the cell type to the map
	 * @param row
	 * @param col
	 * @param type
	 */
	public void insertCell(int row, int col, MapType type) {
		if(row < M && col < N){
			cells[row][col] = type;
		}
	}

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

	public void printMap() {
		for(int i = 0; i < M; i++) {
			for(int j = 0; j < N; j++) {
				
				System.out.print(cells[i][j] + " ");
			}
			System.out.println();
		}
	}
	
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
	
//	public Player getCellState(int row, int col) {
//		return cellState[row][col];
//	}

	@Override
	public Map clone() throws CloneNotSupportedException {
		Map newMap = new Map(M, N);		
		for(int i = 0; i < M; i++) {
			for(int j = 0; j < N; j++) {
				newMap.cells[i][j] = this.cells[i][j];
//				newMap.cellState[i][j] = this.cellState[i][j];
			}
		}
		return newMap;
	}
	
	
	
	
	

}
