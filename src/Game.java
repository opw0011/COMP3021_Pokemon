import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
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
	
	private ArrayList<Player> playerList = new ArrayList<Player>();
	
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
					map.insertCell(i, j, Map.MapType.START);
					break;
				case 'D':
					// player destination point
					map.insertCell(i, j, Map.MapType.DEST);
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
		
		map.printPrettyMap();	
		
		// to do
		// Find the number of stations and pokemons in the map 
		// Continue read the information of all the stations and pokemons by using br.readLine();
		while((line = br.readLine()) != null) {
//			System.out.println(line);
			
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
	
	public void findPath(Cell current, Player player) {
		// return false if out of map boundary
		if(current.getRow() < 0 || current.getCol() < 0 || current.getRow() >= map.getM() || current.getCol() >= map.getN())
			return;
		
		// check current cell type
		Map.MapType curCell = map.getCellType(current);
		
		// invalid cell
		if(curCell == Map.MapType.WALL)	return;
		
		// valid cells
		switch (curCell) {
		case DEST:
//			System.out.println("REACH DEST");
			player.addVistedCell(current);
			playerList.add(new Player(player));
			return;
		case SUPPLY:
			// search for station and check if it is used
			// get num of pokeball and add to
			// add ball to player if he has not visited the shop before, otherwise treat as normal cell
			if(! player.hasVisitedCell(current)) {
				for(Station station : stations) {
					if(station.getCol() == current.getCol() && station.getRow() == current.getRow()) {
						int numPB = station.getNumPokeBalls();
						player.setNumPokeBalls(player.getNumPokeBalls() + numPB);
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
		if(player.getScore() < player.getStateScore(current)) {
//			System.out.println("Low score >> " + current + player.getScore());
			return;
		}
		
		// current score improved, update the state
		player.recordCurrentState(current);

		Cell up = new Cell(current.getRow() - 1, current.getCol());
		Cell right = new Cell(current.getRow(), current.getCol() + 1);
		Cell down = new Cell(current.getRow() + 1, current.getCol());
		Cell left = new Cell(current.getRow(), current.getCol() - 1);

		Player player1 = new Player(player);
		Player player2 = new Player(player);
		Player player3 = new Player(player);
		Player player4 = new Player(player);
		findPath(up, player1);
		findPath(right, player2);
		findPath(down, player3);
		findPath(left, player4);		
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
		
		// Testing
		long startTime, stopTime;		
		
		// visit the cell at the initial point
		Cell initialPt = new Cell(game.player.getRow(), game.player.getCol());

		startTime = System.nanoTime();
		game.findPath(initialPt, game.player);
		stopTime = System.nanoTime();
		System.out.println("FindPath Time: " + (stopTime - startTime) / 1000000000.0);
		System.out.println("Player List size: " + game.playerList.size());
		
		startTime = System.nanoTime();
		// Sort the player with the highest score
		Collections.sort(game.playerList);
		
		stopTime = System.nanoTime();
		System.out.println("Sorting TIme: " + (stopTime - startTime) / 1000000000.0);
		
		System.out.println("================= SOLUTION ===================");
		Player op = game.playerList.get(game.playerList.size()-1);	// last element is the highest score
		System.out.printf("Optimal[score:%s NB:%s NP:%s NS:%s MCP:%s %s]\n", 
				op.getScore(), op.getNumPokeBalls(), op.getPkmCaught().size(), op.getNumDistinctPokemonType(), op.getMaxPokemonCP(), op);
		
		ArrayList<Cell> pathList = op.getPathVisited();
		int i = 0;
		for(Cell c : pathList) {
			System.out.printf("<%s,%s>", c.getRow(), c.getCol());
			if(i != pathList.size() - 1)	System.out.print("->");
			else System.out.println();
			i++;
		}
		Map visitedMap = game.map.clone();
		for(Cell c : op.getPathVisited()) {
			visitedMap.insertCell(c.getRow(), c.getCol(), Map.MapType.VISITED);
		}
		visitedMap.printPrettyMap();
		
		
		// TO DO 
		// Read the configures of the map and pokemons from the file inputFile
		// and output the results to the file outputFile
		BufferedWriter bw = new BufferedWriter(new FileWriter(outputFile));
		bw.write(String.format("%d", op.getScore()));
		bw.newLine();
		bw.write(String.format("%d:%d:%d:%d", 
				op.getNumPokeBalls(), op.getPkmCaught().size(), op.getNumDistinctPokemonType(), op.getMaxPokemonCP()));
		bw.newLine();
		int j = 0;
		for(Cell c : pathList) {
			bw.write(String.format("<%s,%s>", c.getRow(), c.getCol()));
			if(j != pathList.size() - 1)	
				bw.write("->");
			j++;
		}
		bw.close();
	}
	
}
