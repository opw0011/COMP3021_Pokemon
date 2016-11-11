package pokemon;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


import java.io.BufferedReader;
import java.io.BufferedWriter;

/**
 * Class for the main game
 * @author opw
 *
 */
public class Game {
	private Map map;
	private Player player;
	private ArrayList<Station> stations = new ArrayList<Station>();
	private ArrayList<Pokemon> pokemons = new ArrayList<Pokemon>();
	private static long timer;
	private static File outputFile;
	private Cell startPoint;
	private Cell destPoint;		
	private Player optPlayer;
	private int playerCount = 0;
	
	/**
	 * Initialize the game
	 * @param inputFile input file that contains the game data
	 * @throws Exception cannot parse map
	 */
	public void initialize(File inputFile) throws Exception{
		BufferedReader br = new BufferedReader(new FileReader(inputFile));
		
		// Read the first of the input file
		String line = br.readLine();
		int M = Integer.parseInt(line.split(" ")[0]);
		int N = Integer.parseInt(line.split(" ")[1]);
		
		// To do: define a map
		map = new Map(M, N);
		
		// Read the following M lines of the Map
		for (int i = 0; i < M; i++) {
			// to do
			// Read the map line by line
			line = br.readLine();
//			System.out.println(line);
			for(int j = 0; j < N; j++) {
				char cellType = line.charAt(j);
				switch(cellType) {
				case '#':
					// wall
					map.insertCell(i, j, Map.MapType.WALL);
					break;
				case ' ':
					// empty cell
					map.insertCell(i, j, Map.MapType.EMPTY);
					break;
				case 'B':
					// player starting point
					player = new Player(i, j);
					startPoint = new Cell(i, j);
					map.insertCell(i, j, Map.MapType.START);
					break;
				case 'D':
					// player destination point
					map.insertCell(i, j, Map.MapType.DEST);
					destPoint = new Cell(i, j);
					break;
				case 'S':
					// supply station
					map.insertCell(i, j, Map.MapType.SUPPLY);
					break;
				case 'P':
					// pokemon
					map.insertCell(i, j, Map.MapType.POKEMON);
					break;
				default:
					System.out.printf("ERROR: '%s' is a unknown cell type\n");
					break;	
				}				
			}
		}
		
//		map.printPrettyMap();	
		
		// to do
		// Find the number of stations and pokemons in the map 
		// Continue read the information of all the stations and pokemons by using br.readLine();
		while((line = br.readLine()) != null) {
			
			// setting up REGEX for data parsing
			String pkmREGEX = "<(\\d+),(\\d+)>,\\s?(\\w+)\\s?,\\s?(\\w+)\\s?,\\s?(\\d+)\\s?,\\s?(\\d+)\\s?";
			String pksREGEX = "<(\\d+),(\\d+)>,\\s?(\\d+)\\s?";
			Pattern pkmPattern = Pattern.compile(pkmREGEX);
			Pattern pksPattern = Pattern.compile(pksREGEX);
			Matcher pkmMatch = pkmPattern.matcher(line);
			Matcher pksMatch = pksPattern.matcher(line);
			
			// parse pokemon
			if(pkmMatch.find()) {
				int row = Integer.parseInt(pkmMatch.group(1));
				int col = Integer.parseInt(pkmMatch.group(2));
				String name = pkmMatch.group(3);
				String types = pkmMatch.group(4);
				int cp = Integer.parseInt(pkmMatch.group(5));
				int reqBalls = Integer.parseInt(pkmMatch.group(6));				
				// insert new pokemon
				pokemons.add(new Pokemon(row, col, name, types, cp, reqBalls));		
			}
			// parse pokeshop
			else if(pksMatch.find()) {
				int row = Integer.parseInt(pksMatch.group(1));
				int col = Integer.parseInt(pksMatch.group(2));
				int pvdBalls = Integer.parseInt(pksMatch.group(3));
				// insert new pokeshop
				stations.add(new Station(row, col, pvdBalls));
			}
			else {
				throw new Exception("Cannot parse map data");
			}
		}
		
//		System.out.println(pokemons);
//		System.out.println(stations);
//		System.out.println(player);
		
		br.close();
	}
	
	/**
	 * Recursively find a optimal path with highest score
	 * @param current initial cell
	 * @param player initial player
	 * @throws IOException 
	 */
	public void findPath(Cell current, Player player) throws IOException {
		// write current optimal solution to text file every 10s
		long curTime = System.nanoTime();
		if(curTime - timer > 10 *1000000000.0) {
			System.out.printf("Writing current optimal solution to text file [SCORE = %d] (Updates every 10s)\n" , this.optPlayer.getScore());
			this.writeCurrentSolutionToFile();
			timer = System.nanoTime();
//			System.out.println(map.optimalStates.size());
		}
		
		// return false if out of map boundary
		if(current.getRow() < 0 || current.getCol() < 0 || current.getRow() >= map.getM() || current.getCol() >= map.getN()) {
			return;
		}
		
		// check current cell type
		Map.MapType curCell = map.getCellType(current);
		
		// invalid cell
		if(curCell == Map.MapType.WALL)	return;
		
		// valid cells
		switch (curCell) {
		case DEST:
//			System.out.println("REACH DEST");
			player.addVistedCell(current);
			this.setOptimalPlayer(player);
//			System.out.println(player);
			return;
		case SUPPLY:
			// search for station and check if it is used
			// get num of pokeball and add to
			// add ball to player if he has not visited the shop before, otherwise treat as normal cell
			if(! player.hasVisitedCell(current)) {
				for(Station station : stations) {
					if(station.getCol() == current.getCol() && station.getRow() == current.getRow()) {
						player.setNumPokeBalls(player.getNumPokeBalls() + station.getNumPokeBalls());
						break;
					}
				}	
			}				
			break;
		case POKEMON:
			// search pokemon arraylist, see if current player has enough pokeball to catch it
			// check if the pokemon has already been caught, if yes treat it as normal cell
			for(Pokemon pkm : pokemons) {
				if(pkm.getCol() == current.getCol() && pkm.getRow() == current.getRow()) {
					// check if player has caught this pkm
					if(! player.hasCaughtPokemon(pkm) && (player.getNumPokeBalls() >= pkm.getNumRequiredBalls())) {
						// catch it
						player.addCaughtPokemon(pkm);
						player.setNumPokeBalls(player.getNumPokeBalls() - pkm.getNumRequiredBalls());
						break;
					}				
				}
			}
			break;
		default:
			break;
		}
		
		// visit the cell
		player.setRow(current.getRow());
		player.setCol(current.getCol());
		player.addVistedCell(current);
		
		// break the recursion if the state has no improvement
		int currentScore = player.getScore();
		Player currentPlayerState = player.getPlayerState();
		if(currentScore <= map.getPlayerState(currentPlayerState.hashCode())) {
			return;
		}
		
		// current score improved, update the state
		map.setPlayerState(currentPlayerState.hashCode(), currentScore);

		findPath(new Cell(current.getRow() - 1, current.getCol()), new Player(player));	// up
		findPath(new Cell(current.getRow(), current.getCol() + 1), new Player(player));	// right
		findPath(new Cell(current.getRow() + 1, current.getCol()), new Player(player));	// down
		findPath(new Cell(current.getRow(), current.getCol() - 1), new Player(player));	// left
	}
	
	/**
	 * Record the player when a better solution is found
	 * @param p player
	 */
	private void setOptimalPlayer(Player p) {
		if(this.optPlayer == null || p.getScore() > this.optPlayer.getScore()) {
			this.optPlayer = new Player(p);
		}
		playerCount ++;
	}
	
	/**
	 * Write the current optimal solution to the output text file
	 * @throws IOException output file not found
	 */
	public void writeCurrentSolutionToFile() throws IOException {
		BufferedWriter bw = new BufferedWriter(new FileWriter(outputFile));
		bw.write(String.format("%d", this.optPlayer.getScore()));
		bw.newLine();
		bw.write(String.format("%d:%d:%d:%d", 
				this.optPlayer.getNumPokeBalls(), this.optPlayer.getPkmCaught().size(), this.optPlayer.getNumDistinctPokemonType(), this.optPlayer.getMaxPokemonCP()));
		bw.newLine();
		int j = 0;
		ArrayList<Cell> pathList = this.optPlayer.getPathVisited();
		for(Cell c : pathList) {
			bw.write(String.format("<%s,%s>", c.getRow(), c.getCol()));
			if(j != pathList.size() - 1)	
				bw.write("->");
			j++;
		}
		bw.close();
	}
	
	public Map getMap() {
		return map;
	}

	public Player getPlayer() {
		return player;
	}
	
	public ArrayList<Station> getStations() {
		return stations;
	}

	public ArrayList<Pokemon> getPokemons() {
		return pokemons;
	}

	public Pokemon getPokemon(Cell c) {
		// TODO: loop through the list and return matched pokemon
		for(Pokemon pkm : pokemons) {
			if(pkm.getCol() == c.getCol() && pkm.getRow() == c.getRow()) {
				return pkm;
			}
		}
		return null;
	}
	
	public Station getStation(Cell c) {
		for(Station s : stations) {
			if(s.getCol() == c.getCol() && s.getRow() == c.getRow()) {
				return s;
			}
		}
		return null;
	}
	
	/**
	 * Main function to be called first
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception{
		File inputFile = new File("./sampleIn.txt");
		File outputFile = new File("./sampleOut.txt");
		
		if (args.length > 0) {
			inputFile = new File(args[0]);
		} 

		if (args.length > 1) {
			outputFile = new File(args[1]);
		}
		
		Game game = new Game();
		game.initialize(inputFile);
		Game.outputFile = outputFile;
		
		// Testing	
		long startTime, stopTime;
		
		System.out.println("Finding optimal path ... Please be patient");
		// visit the cell at the initial point
		Cell initialPt = new Cell(game.player.getRow(), game.player.getCol());

		Game.timer = System.nanoTime();
		startTime = System.nanoTime();
		game.findPath(initialPt, game.player);	// recursion find path
		stopTime = System.nanoTime();
		System.out.println("FindPath Time: " + (stopTime - startTime) / 1000000000.0);
			
		System.out.println("================= SOLUTION ===================");
		System.out.println("Total possible path = " + game.playerCount);
		System.out.printf("Optimal Solution = [score:%s NB:%s NP:%s NS:%s MCP:%s %s]\n", 
				game.optPlayer.getScore(), game.optPlayer.getNumPokeBalls(), game.optPlayer.getPkmCaught().size(), 
				game.optPlayer.getNumDistinctPokemonType(), game.optPlayer.getMaxPokemonCP(), game.optPlayer);		
		
		// TO DO 
		// Read the configures of the map and pokemons from the file inputFile
		// and output the results to the file outputFile
		game.writeCurrentSolutionToFile();
	}
	
}
