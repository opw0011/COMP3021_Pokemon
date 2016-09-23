import java.util.ArrayList;

public class Player {
	private int x;
	private int y;
	private int numPokeBalls;
	private ArrayList<Pokemon> pkmCaught;
	private ArrayList<Cell> pathVisited;
	
	public Player(int x, int y) {
		this.x = x;
		this.y = y;
		numPokeBalls = 0;
		pkmCaught = new ArrayList<Pokemon>();
		pathVisited = new ArrayList<Cell>();				
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	public int getNumPokeBalls() {
		return numPokeBalls;
	}

	public void setNumPokeBalls(int numPokeBalls) {
		this.numPokeBalls = numPokeBalls;
	}
	
	

}
