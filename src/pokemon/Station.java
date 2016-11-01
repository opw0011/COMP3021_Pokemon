package pokemon;
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

	//--------------------
	// Getters and Setters
	//--------------------
	public int getNumPokeBalls() {
		return numPokeBalls;
	}
	
	//--------------------------
	// Auto Generated Functions
	//--------------------------
	@Override
	public String toString() {
		return "Station [numPokeBalls=" + numPokeBalls + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + numPokeBalls;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		Station other = (Station) obj;
		if (numPokeBalls != other.numPokeBalls)
			return false;
		return true;
	}
	
}
