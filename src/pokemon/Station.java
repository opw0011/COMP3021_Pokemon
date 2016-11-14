package pokemon;

import java.util.ArrayList;
import java.util.Random;

import pokemon.ui.PokemonScreenLAB9;

/**
 * Class for storing Pokeshop (Station)
 * @author opw
 *
 */
public class Station extends Cell implements Runnable{
	private int numPokeBalls;
	private static final int MIN_RAND_SPAWN_TIME = 5 * 1000;
	private static final int MAX_RAND_SPAWN_TIME = 10 * 1000;

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
		return "Station [" + super.toString() +" numPokeBalls=" + numPokeBalls + "]";
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

	@Override
	public void run() {
		// TODO Auto-generated method stub
		try {
			System.out.println("Trigger Station Thread");
			
			Cell oldCell = new Cell(this.getRow(), this.getCol());

			// Random sleep between 5 to 10 s
			Random rand = new Random();
			int delay = rand.nextInt(MAX_RAND_SPAWN_TIME - MIN_RAND_SPAWN_TIME) +  MIN_RAND_SPAWN_TIME;
			Thread.sleep(delay);
			
			// find available cell
			Cell newCell = PokemonScreenLAB9.getRandomEmptyCell();
			setRow(newCell.getRow());
			setCol(newCell.getCol());
			
			// invoke UI thread to update station img
			PokemonScreenLAB9.spawnStation(this, oldCell);			
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
}
