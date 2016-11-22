package pokemon;

import pokemon.ui.PokemonScreen;

/**
 * Class for storing pokemon cell
 * @author opw
 *
 */
public class Pokemon extends Cell implements Runnable{
	private String name;
	private String types;
	private int cp;
	private int numRequiredBalls;
	private boolean visible;

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
		this.visible = true;
	}

	//--------------------
	// Getters and Setters
	//--------------------
	public String getTypes() {
		return types;
	}

	public String getName() {
		return name;
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

	public boolean isVisible() {
		return visible;
	}

	public void setVisible(boolean visible) {
		this.visible = visible;
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


	@Override
	public void run() {
		// TODO Auto-generated method stub
		while(true) {
//			System.out.println("Pokemon thread is running " + this);
			Cell oldCell = new Cell(this.getRow(), this.getCol());
			Cell moveToCell = new Cell(this.getRow(), this.getCol() - 1);
			
			if(PokemonScreen.pkmCanMove(moveToCell) && PokemonScreen.isPause() == false && this.visible) {
				// Move and update the screen
//				System.out.println("Moving");
//				this.setCol(moveToCell.getCol());
//				this.setRow(moveToCell.getRow());
				PokemonScreen.movePokemon(this, moveToCell, oldCell);
				
			}
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

}
