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

	@Override
	public String toString() {
		return "Pokemon [name=" + name + ", types=" + types + ", cp=" + cp + ", numRequiredBalls="
				+ numRequiredBalls + "]";
	}
	
	
	

}
