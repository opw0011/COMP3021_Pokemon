/**
 * Class for storing Pokeshop (Station)
 * @author opw
 *
 */
public class Station extends Cell{
	private int numPokeBalls;

	/**
	 * Constructor
	 * @param row rows
	 * @param col columns
	 * @param numPokeBalls number of provided poke balls
	 */
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
