import java.util.Arrays;

public class Map {
	private MapType[][] cells;
	private final int M;
	private final int N;
	public enum MapType {
		WALL, EMPTY, START, DEST, SUPPLY, POKEMON
	}
	
	public Map(int M, int N) {
		this.M = M;
		this.N = N;
		cells = new MapType[M][N];
	}
	
	public void insertCell(int row, int col, MapType type) {
		if(row < M && col < N){
			cells[row][col] = type;
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

	public MapType getCell(int row, int col) {
		return cells[row][col];
	}

	public int getM() {
		return M;
	}

	public int getN() {
		return N;
	}
	
	
	

}
