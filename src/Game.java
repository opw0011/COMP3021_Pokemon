import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.io.BufferedReader;

public class Game {
	Map map;
	Player player;
	ArrayList<Station> stations = new ArrayList<Station>();
	ArrayList<Pokemon> pokemons = new ArrayList<Pokemon>();
	
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
				String species = pkmMatch.group(4);
				int cp = Integer.parseInt(pkmMatch.group(5));
				int reqBalls = Integer.parseInt(pkmMatch.group(6));				
				// insert new pokemon
				pokemons.add(new Pokemon(row, col, name, species, cp, reqBalls));		
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
		
		br.close();
	}
	
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
		// TO DO 
		// Read the configures of the map and pokemons from the file inputFile
		// and output the results to the file outputFile
	}
	
}
