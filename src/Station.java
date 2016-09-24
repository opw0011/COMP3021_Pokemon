
public class Station extends Cell{
	private int numPokeBalls;

	public Station(int row, int col, int numPokeBalls) {
		super(row, col);
		this.numPokeBalls = numPokeBalls;
	}

	public int getNumPokeBalls() {
		return numPokeBalls;
	}

	@Override
	public String toString() {
		return "Station [numPokeBalls=" + numPokeBalls + "]";
	}

	
	
}
