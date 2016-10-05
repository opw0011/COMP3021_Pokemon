import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


import java.io.BufferedReader;

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
		
		map.printMap();	
		
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
		
		System.out.println(pokemons);
		System.out.println(stations);
		System.out.println(player);
		
		br.close();
	}
	
	
	public boolean findPath(Cell current) {
		// return false if out of map boundary
		if(current.getRow() < 0 || current.getCol() < 0 || current.getRow() >= map.getM() || current.getCol() >= map.getN())
			return false;

		// check current cell type
		Map.MapType curCell = map.getCellType(current);
		switch (curCell) {
		case WALL:
			return false;
		case DEST:
			return true;
		default:
			break;
		}
		
		Cell up = new Cell(current.getRow() - 1, current.getCol());
		Cell right = new Cell(current.getRow(), current.getCol() + 1);
		Cell down = new Cell(current.getRow() + 1, current.getCol());
		Cell left = new Cell(current.getRow(), current.getCol() - 1);
		
		// north
		if(player.hasVisitedCell(up) == false) {
			player.addVistedCell(up);
			if(findPath(up))
				return true;
			else
				player.removeLastVisitedCell(up);
		}

		
		// east
		if(player.hasVisitedCell(right) == false) {
			player.addVistedCell(right);
			if(findPath(right))
				return true;
			else
				player.removeLastVisitedCell(right);
		}
		
		// south
		if(player.hasVisitedCell(down) == false) {
			player.addVistedCell(down);
			if(findPath(down))
				return true;
			else
				player.removeLastVisitedCell(down);
		}
		
		// west
		if(player.hasVisitedCell(left) == false) {
			player.addVistedCell(left);
			if(findPath(left))
				return true;
			else
				player.removeLastVisitedCell(left);
		}
		
		return false;
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
		
		// visit the cell at the initial point
		Cell initialPt = new Cell(game.player.getRow(), game.player.getCol());
		game.player.addVistedCell(initialPt);
		System.out.println(game.findPath(initialPt));
		game.player.printVistedPath();
		// TO DO 
		// Read the configures of the map and pokemons from the file inputFile
		// and output the results to the file outputFile
		
	}
	
}
