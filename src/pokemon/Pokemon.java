package pokemon;
/**
 * Class for storing pokemon cell
 * @author opw
 *
 */
public class Pokemon extends Cell{
	private String name;
	private String types;
	private int cp;
	private int numRequiredBalls;

	/**
	 * Constructor
	 * @param row
	 * @param col
	 * @param name Pokemon name
	 * @param types 
	 * @param cp combat power
	 * @param numRequiredBalls required balls to catch that Pokemon
	 */
	public Pokemon(int row, int col, String name, String types, int cp, int numRequiredBalls) {
		super(row, col);
		this.name = name;
		this.types = types;
		this.cp = cp;
		this.numRequiredBalls = numRequiredBalls;
	}

	//--------------------
	// Getters and Setters
	//--------------------
	public String getTypes() {
		return types;
	}

	public int getCp() {
		return cp;
	}

	public void setCp(int cp) {
		this.cp = cp;
	}

	public int getNumRequiredBalls() {
		return numRequiredBalls;
	}

	public void setNumRequiredBalls(int numRequiredBalls) {
		this.numRequiredBalls = numRequiredBalls;
	}

	//--------------------------
	// Auto Generated Functions
	//--------------------------
	@Override
	public String toString() {
		return "Pokemon [name=" + name + ", types=" + types + ", cp=" + cp + ", numRequiredBalls="
				+ numRequiredBalls + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + cp;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + numRequiredBalls;
		result = prime * result + ((types == null) ? 0 : types.hashCode());
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
		Pokemon other = (Pokemon) obj;
		if (cp != other.cp)
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (numRequiredBalls != other.numRequiredBalls)
			return false;
		if (types == null) {
			if (other.types != null)
				return false;
		} else if (!types.equals(other.types))
			return false;
		return true;
	}
}
