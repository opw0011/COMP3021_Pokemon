package pokemon;

import java.util.ArrayList;
import java.util.Random;

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
		
		while(this.visible) {
//			System.out.println("Pokemon thread is running " + this);
			Cell oldCell = new Cell(this.getRow(), this.getCol());
			
			ArrayList<Cell> possibleCells = new ArrayList<>();
	
			// generate a list of possible cells
			Cell up = new Cell(this.getRow() - 1, this.getCol());
			Cell down = new Cell(this.getRow() + 1, this.getCol());
			Cell left = new Cell(this.getRow(), this.getCol() - 1);
			Cell right = new Cell(this.getRow(), this.getCol() + 1);
			
			possibleCells.add(up);
			possibleCells.add(down);
			possibleCells.add(left);
			possibleCells.add(right);
			
			ArrayList<Cell> movableCells = (ArrayList<Cell>) possibleCells.clone();
			for(Cell cell : possibleCells) {
				if(! PokemonScreen.pkmCanMove(cell))
					movableCells.remove(cell);
			}
			
			// if no available cell to move, stay still
			if(!movableCells.isEmpty()) {
				// rand number
				Random rand = new Random();
				int randIndex = rand.nextInt(movableCells.size());
				
				Cell moveToCell = movableCells.get(randIndex);
				
//				System.out.println(moveToCell);
				
				if(PokemonScreen.pkmCanMove(moveToCell) && PokemonScreen.isPause() == false) {
					// Move and update the screen
//					System.out.println("Moving");
					PokemonScreen.movePokemon(this, moveToCell, oldCell);
				}
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
