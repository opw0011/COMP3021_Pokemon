import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
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
	private Cell startPoint;
	private Cell destPoint;	
	
	private Player optPlayer;
	private int playerCount = 0;
	
	private int maxScore;
	private ArrayList<Cell> optmPath;
	private HashMap<Cell, HashMap<Cell, Integer>> distanceTable = new HashMap<Cell, HashMap<Cell, Integer>>();
	
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
//			playerList.add(new Player(player));
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
		if(currentScore < map.getStateScoreNew(currentPlayerState)) {
//			System.out.println("Low score >> " + current + player.getScore());
			return;
		}
		
		// current score improved, update the state
		map.recordCurrentStateNew(currentPlayerState, currentScore);

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
	
	private void setOptimalPlayer(Player p) {
		if(this.optPlayer == null || p.getScore() > this.optPlayer.getScore()) {
			this.optPlayer = new Player(p);
		}
		playerCount ++;
//		System.out.println(p);
	}
	
	// must pass in Cell type, subclass not works
	private int findShortestPath(Cell start, Cell dest) {		
		// BFS
		Queue<Cell> queue = new LinkedList<Cell>();
		HashMap<Cell, Integer> distanceMap= new HashMap<Cell, Integer>();
		HashSet<Cell> visitedList = new HashSet<Cell>();
		queue.add(start);
		distanceMap.put(start, 0);
		while(! queue.isEmpty()) {
			Cell current = queue.poll();
			visitedList.add(current);

			// out of boundaries
			if(current.getRow() < 0 || current.getCol() < 0 || current.getRow() >= map.getM() || current.getCol() >= map.getN()) continue;
			// wall
			Map.MapType curCell = map.getCellType(current);
			if(current.equals(dest)) break;
			if(curCell == Map.MapType.WALL) continue;
			if(curCell == Map.MapType.DEST)	continue;	// DEST act like a wall
			
			
			ArrayList<Cell> cellList = new ArrayList<Cell>();
			cellList.add(new Cell(current.getRow() - 1, current.getCol()));	// up
			cellList.add(new Cell(current.getRow(), current.getCol() + 1));	// right
			cellList.add(new Cell(current.getRow() + 1, current.getCol()));	// down
			cellList.add(new Cell(current.getRow(), current.getCol() - 1));	// left			
			
			int newDistance = distanceMap.get(current) + 1;
			// visit 4 directions
			for(Cell cell : cellList) {
				if(!visitedList.contains(cell)) {
					queue.add(cell);
					if(!distanceMap.containsKey(cell) || newDistance < distanceMap.get(current))
						distanceMap.put(cell, newDistance);
				}
			}
		}
		
		if(distanceMap.containsKey(dest)) {
			return distanceMap.get(dest);
		}
		return 0;	// no path found
	}
	
	
	
	private void findOptimalPath() {
		// generate possible and valid permutation of B,P1...Pn,S1...Sn,D
		// return the path with max score
		ArrayList<Cell> list = new ArrayList<Cell>();
		list.addAll(this.stations);
		list.addAll(this.pokemons);
		list.add(this.destPoint);
		permute(list, 0);		
	}
	
    private void permute(ArrayList<Cell> arr, int k){
        for(int i = k; i < arr.size(); i++){
            java.util.Collections.swap(arr, i, k);
            permute(arr, k+1);
            java.util.Collections.swap(arr, k, i);
        }
        
        // a new permutation is generated here
        if (k == arr.size() -1){
//        	System.out.println(arr.size());
        	int score = 0;
        	int numPokeBall = 0;
        	int maxCp = 0;
        	HashSet<String> distinctType = new HashSet<String>();
        	ArrayList<Cell> list = (ArrayList<Cell>) arr.clone();
        	list.add(0, this.startPoint);
        	// compute two adjacent cell
        	for(int i = 0; i < list.size() - 1; i++) {
        		Cell first = list.get(i);
        		Cell second = list.get(i + 1);
        		
        		if(map.getCellType(first) == Map.MapType.DEST) break;	// already reach destination
        		
        		switch(map.getCellType(second)) {
        		case SUPPLY:
        			int numBall = ((Station)second).getNumPokeBalls();
        			numPokeBall += numBall;        			
        			break;
        		case POKEMON:
        			Pokemon pkm = (Pokemon)second;
        			int reqBalls = pkm.getNumRequiredBalls();
        			if(numPokeBall >= reqBalls) {
        				// catch pkm
        				numPokeBall -= reqBalls;
        				distinctType.add(pkm.getTypes());
        				score += 5;
        				if(pkm.getCp() > maxCp)	maxCp = pkm.getCp();
        			}        			
        			break;
				default:
					break;
        		
        		}        		
        		Cell firstCell = new Cell(first.getRow(), first.getCol());
        		Cell secondCell = new Cell(second.getRow(), second.getCol());
        		if(distanceTable.containsKey(firstCell) && distanceTable.get(firstCell).containsKey(secondCell)) {
        			score -= distanceTable.get(firstCell).get(secondCell);
        		}
        		else {
        			int s = findShortestPath(firstCell, secondCell);
        			HashMap<Cell, Integer> map;
        			if(distanceTable.containsKey(firstCell)) {
//        				distanceTable.put(firstCell, v)
        				map = distanceTable.get(firstCell);
        			}
        			else {
        				map = new HashMap<Cell, Integer>();
        				distanceTable.put(firstCell, map);
        			}
        			map.put(secondCell, s);    
        			score -= s;
        		}
//        		score -= findShortestPath(firstCell, secondCell);       		
        	}
        	
        	for(Cell cell : list) {
        		switch(map.getCellType(cell)) {
        		case SUPPLY:
        			System.out.print(" S ");
        			break;
        		case POKEMON:
        			System.out.print(" P ");
        			break;
        		case DEST:
        			System.out.print(" D ");
        			break;
        		case START:
        			System.out.print(" B ");
        			break;
        		default:
        			System.out.print(" ? ");
        			break;
        		}
        	}
    		score += numPokeBall;
    		score += 10 * distinctType.size();
    		score += maxCp;
    		if(score > this.maxScore) {
    			this.maxScore = score;
    			this.optmPath = list;
    		}
//        	System.out.println(score);
//            System.out.println(Arrays.toString(arr.toArray()));
        	
        }
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
//		System.out.println(game.BFS(new Cell(8,0), new Cell(1,11)));		
		long startTime, stopTime;		
//		System.out.println(game.findShortestPath(new Cell(4, 4), new Cell(4,19)));
		
//		
//		startTime = System.nanoTime();
//		game.findOptimalPath();
//		stopTime = System.nanoTime();
//		System.out.println("Time: " + (stopTime - startTime) / 1000000000.0);
//		System.out.println();
//		System.out.println("OPTM SCORE: " + game.maxScore + "\n" + game.optmPath);
		
//		ArrayList<ArrayList<Cell>> paths = new ArrayList<ArrayList<Cell>>();
//		System.out.println(game.generatePath(paths));
		
//		startTime = System.nanoTime();
////		for(int i = 0; i <= 1000000; i++)
////			game.findShortestPath(new Cell(8,0), new Cell(8,4));
////		System.out.println(game.findShortestPath(new Cell(8,0), new Cell(1,11)));
//			
//		stopTime = System.nanoTime();
//		System.out.println("BFS Time: " + (stopTime - startTime) / 1000000000.0);
		
		// visit the cell at the initial point
		Cell initialPt = new Cell(game.player.getRow(), game.player.getCol());

		startTime = System.nanoTime();
		game.findPath(initialPt, game.player);	// recursion find path
		stopTime = System.nanoTime();
		System.out.println("FindPath Time: " + (stopTime - startTime) / 1000000000.0);
			
		
		System.out.println("================= SOLUTION ===================");
		System.out.println("Total possible path = " + game.playerCount);
		System.out.printf("Optimal[score:%s NB:%s NP:%s NS:%s MCP:%s %s]\n", 
				game.optPlayer.getScore(), game.optPlayer.getNumPokeBalls(), game.optPlayer.getPkmCaught().size(), 
				game.optPlayer.getNumDistinctPokemonType(), game.optPlayer.getMaxPokemonCP(), game.optPlayer);		
		
		// TO DO 
		// Read the configures of the map and pokemons from the file inputFile
		// and output the results to the file outputFile
		BufferedWriter bw = new BufferedWriter(new FileWriter(outputFile));
		bw.write(String.format("%d", game.optPlayer.getScore()));
		bw.newLine();
		bw.write(String.format("%d:%d:%d:%d", 
				game.optPlayer.getNumPokeBalls(), game.optPlayer.getPkmCaught().size(), game.optPlayer.getNumDistinctPokemonType(), game.optPlayer.getMaxPokemonCP()));
		bw.newLine();
		int j = 0;
		ArrayList<Cell> pathList = game.optPlayer.getPathVisited();
		for(Cell c : pathList) {
			bw.write(String.format("<%s,%s>", c.getRow(), c.getCol()));
			if(j != pathList.size() - 1)	
				bw.write("->");
			j++;
		}

		bw.close();
	}
	
}
