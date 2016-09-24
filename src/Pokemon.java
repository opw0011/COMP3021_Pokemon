
public class Pokemon extends Cell{
	private String name;
	private String species;
	private int cp;
	private int numRequiredBalls;

	public Pokemon(int row, int col, String name, String species, int cp, int numRequiredBalls) {
		super(row, col);
		this.name = name;
		this.species = species;
		this.cp = cp;
		this.numRequiredBalls = numRequiredBalls;
	}

	@Override
	public String toString() {
		return "Pokemon [name=" + name + ", species=" + species + ", cp=" + cp + ", numRequiredBalls="
				+ numRequiredBalls + "]";
	}
	
	
	

}
