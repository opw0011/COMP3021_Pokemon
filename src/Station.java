
public class Station extends Cell{
	private int numPokeBalls;
	private static final boolean DEFAULT_CANPASS = true;

	public Station(int row, int col, int numPokeBalls) {
		super(row, col, DEFAULT_CANPASS);
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
