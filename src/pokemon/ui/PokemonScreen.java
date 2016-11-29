package pokemon.ui;

import java.io.File;
import java.util.ArrayList;
import java.util.Random;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.*;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import pokemon.Cell;
import pokemon.Game;
//import pokemon.Empty;
import pokemon.Map;
import pokemon.Map.MapType;
import pokemon.Player;
import pokemon.Pokemon;
import pokemon.Station;
//import pokemon.Wall;

public class PokemonScreen extends Application {

	/**
	 * width of the window
	 */
	private static int W;

	/**
	 * height of the window
	 */
	private static int H;


	// this define the size of one CELL
	private static final int STEP_SIZE = 40;
	
	private static Game myGame;
	
	// this are the urls of the images
	private static final String front = new File("icons/front.png").toURI().toString();
	private static final String back = new File("icons/back.png").toURI().toString();
	private static final String left = new File("icons/left.png").toURI().toString();
	private static final String right = new File("icons/right.png").toURI().toString();
	
	// other game assets
	private static final String tree = new File("icons/tree.png").toURI().toString();
	private static final String exit = new File("icons/exit.png").toURI().toString();
	private static final String ball = new File("icons/ball_ani.gif").toURI().toString();
	
	// gif animations
	private static final String escape = new File("icons/escape.gif").toURI().toString();
	private static final String caught = new File("icons/caught.gif").toURI().toString();
	
	// avatar images
	private ImageView avatar;
	private Image avatarImage;

	// these booleans correspond to the key pressed by the user
	boolean goUp, goDown, goRight, goLeft;

	// current position of the avatar
	double currentPosx = 0;
	double currentPosy = 0;

	protected boolean stop = false;
	private static boolean pause = false;
	
	// bind variables
	private static SimpleIntegerProperty score = new SimpleIntegerProperty(0); 
	private static SimpleIntegerProperty numCaught = new SimpleIntegerProperty(0); 
	private static SimpleIntegerProperty numBalls = new SimpleIntegerProperty(0); 
	
	private static AnimationTimer timer;
	
	// Layout panels
	private static Group mapGroup;
	private static VBox rPanel;
	
	private static Label labelStatus;
	

	@Override
	public void start(Stage stage) throws Exception {
		BorderPane mainPane = new BorderPane();
		mainPane.setPadding(new Insets(10));
		
		// Setup Map and add to myGroup
		mapGroup = new Group();
		buildMap(mapGroup);
		
		mainPane.setLeft(mapGroup);
		
		// right-sided menu
		rPanel = new VBox(10);
		rPanel.setPrefWidth(170);
		
		Label labelScore = new Label();
		labelScore.textProperty().bind(score.asString());		
		rPanel.getChildren().add(new HBox(new Label("Current score: "), labelScore));
		
		Label labelCaught = new Label();
		labelCaught.textProperty().bind(numCaught.asString());	
		rPanel.getChildren().add(new HBox(new Label("# of Pokemons caught: "), labelCaught));
		
		Label labelBalls = new Label();
		labelBalls.textProperty().bind(numBalls.asString());	
		rPanel.getChildren().add(new HBox(new Label("# of Pokeballs owned: "),labelBalls));
		
		labelStatus = new Label();
		rPanel.getChildren().add(labelStatus);
				
		// buttons
		Button btnResume = new Button("Resume");
		btnResume.setId("btnResume");
		btnResume.setFocusTraversable(false);
		btnResume.setOnAction(e -> {
			System.out.println("Resume");
			labelStatus.setText("");
			pause = false;
			timer.start();
		});
		
		Button btnPause = new Button("Pause");
		btnPause.setId("btnPause");
		btnPause.setFocusTraversable(false);
		btnPause.setOnAction(e -> {
			System.out.println("Pause");
			labelStatus.setText("Game Pause!");
			pause = true;
			timer.stop();
		});
		
		HBox btnPanel = new HBox(10);
		btnPanel.setPadding(new Insets(50, 0,0,0));
		btnPanel.getChildren().addAll(btnResume, btnPause);
		rPanel.getChildren().add(btnPanel);
		
		mainPane.setRight(rPanel);

		// create scene
		Scene scene = new Scene(mainPane);

		// add listener on key pressing
		scene.setOnKeyPressed(new EventHandler<KeyEvent>() {
			@Override
			public void handle(KeyEvent event) {
				if(pause)	return;	// ignore key press when the pause btn is clicked
				switch (event.getCode()) {
				case UP:
					goUp = true;
					avatar.setImage(new Image(back));
					break;
				case DOWN:
					goDown = true;
					avatar.setImage(new Image(front));
					break;
				case LEFT:
					goLeft = true;
					avatar.setImage(new Image(left));
					break;
				case RIGHT:
					goRight = true;
					avatar.setImage(new Image(right));
					break;
				default:
					break;
				}
			}
		});
		// add listener key released
		scene.setOnKeyReleased(new EventHandler<KeyEvent>() {
			@Override
			public void handle(KeyEvent event) {
				if(pause)	return;	// ignore key press when the pause btn is clicked
//				switch (event.getCode()) {
//				case UP:
//					goUp = false;
//					break;
//				case DOWN:
//					goDown = false;
//					break;
//				case LEFT:
//					goLeft = false;
//					break;
//				case RIGHT:
//					goRight = false;
//					break;
//				default:
//					break;
//				}
				goUp = goDown = goLeft = goRight = false;
				stop = false;
			}
		});

		stage.setScene(scene);
		stage.show();

		// it will execute this periodically
		timer = new AnimationTimer() {
			@Override
			public void handle(long now) {
				if (stop)
					return;

				int dx = 0, dy = 0;
				
				Player player = myGame.getPlayer();
				int row = player.getRow();
				int col = player.getCol();

				if (goUp) {
					dy -= (STEP_SIZE);
					row--;
				} else if (goDown) {
					dy += (STEP_SIZE);
					row++;
				} else if (goRight) {
					dx += (STEP_SIZE);
					col++;
				} else if (goLeft) {
					dx -= (STEP_SIZE);
					col--;
				} else {
					// no key was pressed return
					return;
				}
				
				// check if the row and col is movealbe
				if(myGame.getMap().canWalk(row, col)) {
					Cell current = new Cell(row, col);
					
					// update player position
					player.setRow(row);
					player.setCol(col);
					player.addVistedCell(current);
					
					// move image of avatar
					moveAvatarBy(dx, dy);
					
					switch(myGame.getMap().getCellType(current)) {
					case SUPPLY:
						Station station = myGame.getStation(current);
						
						// update pokeballs
						int pkBalls =  myGame.getPlayer().getNumPokeBalls() + station.getNumPokeBalls();
						myGame.getPlayer().setNumPokeBalls(pkBalls);
						// update # balls label
						numBalls.set(pkBalls);
						
						// update game map 
						myGame.getMap().insertCell(row, col, MapType.EMPTY);
						myGame.getStations().remove(station);
						
						// get the station image and set it non-visible
						ImageView simg = (ImageView) mapGroup.lookup("#S" + row + col);	
						simg.setVisible(false);			
						
						// trigger function that spawn station after 5 to 10s
						Thread stationThread = new Thread(new Station(row, col, station.getNumPokeBalls()));
						stationThread.setDaemon(true);
						stationThread.start();		
						break;
						
					case POKEMON:
						Pokemon pkm = myGame.getPokemon(current);
						playerEncounterPokemon(pkm);
						break;
						
					case DEST:
						// pause the game and set btn disable
						pause = true;
						timer.stop();
						((Button) rPanel.lookup("#btnResume")).setDisable(true);
						((Button) rPanel.lookup("#btnPause")).setDisable(true);	
						labelStatus.setText("End Game");
						labelStatus.setTextFill(Color.GREEN);
						break;
					default:
						// reset text label
						labelStatus.setText("");
						break;
					}

					// update score label
					score.setValue(myGame.getPlayer().getScore());
					System.out.println("Active Threads Count: " + java.lang.Thread.activeCount());
				}

			}
		};
		// start the timer
		timer.start();
		
		// start moving pokemons
		for(Pokemon pkm : myGame.getPokemons()) {
			Thread pkmThread = new Thread(pkm);
			pkmThread.setDaemon(true);
			pkmThread.start();
		}
	}
	
	private synchronized static void showCatchAnimation(String imgPath) {
		if(imgPath == "") return;
		
		pause = true;
		timer.stop();
		
		// show windows
        Stage newStage = new Stage();
        newStage.setTitle("My New Stage Title");
        Image img = new Image(imgPath);
        ImageView imgView = new ImageView();
        imgView.setImage(img);
        imgView.setFitWidth(250);
        imgView.setPreserveRatio(true);
        StackPane pane = new StackPane();
        pane.getChildren().add(imgView); 
        newStage.setScene(new Scene(pane));
        newStage.setAlwaysOnTop(true);
        newStage.initStyle(StageStyle.UNDECORATED);
        newStage.show();
		((Button) rPanel.lookup("#btnResume")).setDisable(true);
		((Button) rPanel.lookup("#btnPause")).setDisable(true);	
        
        int animationTime = (imgPath == caught) ? 7200 : 5360;
      
        // close the window after certain seconds
        Thread callback = new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					Thread.sleep(animationTime);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				Platform.runLater(new Runnable() {
					@Override
					public void run() {
						newStage.close();
						timer.start();
						pause = false;
						((Button) rPanel.lookup("#btnResume")).setDisable(false);
						((Button) rPanel.lookup("#btnPause")).setDisable(false);	
					}
				});
			}
        });
        callback.start();
	}

	private void moveAvatarBy(int dx, int dy) {
		final double cx = avatar.getBoundsInLocal().getWidth() / 2;
		final double cy = avatar.getBoundsInLocal().getHeight() / 2;
		double x = cx + avatar.getLayoutX() + dx;
		double y = cy + avatar.getLayoutY() + dy;
		moveAvatar(x, y);
	}

	private void moveAvatar(double x, double y) {
		final double cx = avatar.getBoundsInLocal().getWidth() / 2;
		final double cy = avatar.getBoundsInLocal().getHeight() / 2;

		if (x - cx >= 0 && x + cx <= W && y - cy >= 0 && y + cy <= H) {		
            // relocate ImageView avatar
			avatar.relocate(x - cx, y - cy);
			
			//update position
			currentPosx = x - cx;
			currentPosy = y - cy;

			// I moved the avatar lets set stop at true and wait user release the key :)
			stop = true;
		}
	}
	
	/**
	 * Game Map UI Setup
	 * @param args
	 */
	private void buildMap(Group group) {
		System.out.println("Rendering map");
		myGame = new Game();
		// input file
		File inputFile = new File("./sampleIn.txt");
		try {
			myGame.initialize(inputFile);
		} catch (Exception e) {
			System.out.println("ERROR: Cannot build map");
			e.printStackTrace();
		}
		Map myMap = myGame.getMap();
//		myMap.printMap();
		
		// Initial the map width and height
		W = STEP_SIZE * myMap.getN();
		H = STEP_SIZE * myMap.getM();
		
		
		// Build map element according to map type
		for(int i = 0; i < myMap.getM(); i++) {
			for(int j = 0; j < myMap.getN(); j++) {	
				ImageView img;
				
				switch(myMap.getCellType(i, j)) {
				case WALL: 
					img = new ImageView(new Image(tree));
					break;
					
				case DEST:
					img = new ImageView(new Image(exit));
					break;

				case START:
					avatarImage = new Image(front);
					avatar = new ImageView(avatarImage);
					currentPosx = i;
					currentPosy = j;
					img = avatar;
					// add player first visited cell
					myGame.getPlayer().addVistedCell(new Cell(i, j));
					break;
				
				case POKEMON:
					// get pokemon id
					Pokemon pkm = myGame.getPokemon(new Cell(i, j));
					int id = PokemonList.getIdOfFromName(pkm.getName());;
					String path = new File("icons/" + id + ".png").toURI().toString();
					img = new ImageView(path);
					img.setId("P" + pkm.gameHashCode());
//					System.out.println(img.getId());
					break;
					
				case SUPPLY:
					img = new ImageView(ball);
					img.setId("S" + i + j);	// id: "S[row][col]"
					break;
					
				default:
					img = new ImageView();
					break;
				}
				
				img.setFitHeight(STEP_SIZE);
				img.setFitWidth(STEP_SIZE);
				img.setPreserveRatio(true);
				img.setVisible(true);
				img.setCache(true);
				img.relocate(j * STEP_SIZE, i * STEP_SIZE);
				group.getChildren().add(img);
			}
		}
	}
	
	private synchronized static void playerEncounterPokemon(Pokemon pkm) {
		int pkBallsLeft = myGame.getPlayer().getNumPokeBalls() - pkm.getNumRequiredBalls();
		
		int row = myGame.getPlayer().getRow();
		int col = myGame.getPlayer().getCol();
		
		// get the pkm image and set it non-visible
		ImageView pkmimg = (ImageView) mapGroup.lookup("#P" + pkm.gameHashCode());	
		pkmimg.setVisible(false);
				
		// update game map (remove that pkm)
		myGame.getMap().insertCell(row, col, MapType.EMPTY);

		
		// if have enough balls to catch pkm
		if(pkBallsLeft >= 0) {
			// catch pkm
			myGame.getPlayer().addCaughtPokemon(pkm);	
			myGame.getPokemons().remove(pkm);
			pkm.setVisible(false);	// kill a thread
			
	        Platform.runLater(new Runnable() {
	            @Override public void run() {
	    			// update # balls
	    			myGame.getPlayer().setNumPokeBalls(pkBallsLeft);
	    			numBalls.set(pkBallsLeft);
	    			
	    			// update # pkm caught
	    			numCaught.set(myGame.getPlayer().getPkmCaught().size());
	    			
	    			// update message
	    			labelStatus.setText("Pokemon Caught!");
	    			labelStatus.setTextFill(Color.GREEN);
	    			
					// update score label
					score.setValue(myGame.getPlayer().getScore());
					
					showCatchAnimation(caught);
	            }
	        });	 
		}
		else {        
	        // trigger function to respawn that pkm 
	        pkm.setRespawn(true);	      
	        
	        Platform.runLater(new Runnable() {
	            @Override public void run() {
	    			// not enough balls to catch that pkm
	    			labelStatus.setText("NOT enough pokemon ball!");
	    			labelStatus.setTextFill(Color.RED);
	    			showCatchAnimation(escape);
	            }
	        });		
		}
		
	}
	
	// make sure the walk is valid and cannot walk on station/other pkm
	public synchronized static boolean pkmCanMove(Cell cell) {
		if(! myGame.getMap().canWalk(cell.getRow(), cell.getCol()))	return false;
		
		MapType type = myGame.getMap().getCellType(cell.getRow(), cell.getCol());
		return ((type != MapType.SUPPLY  && type != MapType.POKEMON && type != MapType.DEST));
	}
	
	public synchronized static void movePokemon(Pokemon pkm, Cell newCell, Cell oldCell) {
		// Move pokemon
		pkm.setCol(newCell.getCol());
		pkm.setRow(newCell.getRow());
		
		// update game map
		myGame.getMap().insertCell(pkm.getRow(), pkm.getCol(), MapType.POKEMON);
		myGame.getMap().insertCell(oldCell.getRow(), oldCell.getCol(), MapType.EMPTY);
		
		// update pkm image
//		System.out.println(pkm.hashCode());
		ImageView pimg = (ImageView) mapGroup.lookup("#P" + pkm.gameHashCode());
		pimg.relocate(pkm.getCol() * STEP_SIZE, pkm.getRow() * STEP_SIZE);
		pimg.setVisible(true);
		
		// check if encounter with the player
		Player myPlayer = myGame.getPlayer();
		if(myPlayer.getRow() == newCell.getRow() && myPlayer.getCol() == newCell.getCol()) {
			playerEncounterPokemon(pkm);
		}
	}
	
	public static void spawnStation(Station station, Cell oldCell) {
		System.out.println("Spawn Station");
		System.out.println(station);
		// update game map and station list
		myGame.getStations().add(station);
		myGame.getMap().insertCell(station.getRow(), station.getCol(), MapType.SUPPLY);
		
		// update station image
		ImageView simg = (ImageView) mapGroup.lookup("#S" + oldCell.getRow() + oldCell.getCol());
		simg.relocate(station.getCol() * STEP_SIZE, station.getRow() * STEP_SIZE);
		simg.setId("S" + station.getRow() + station.getCol());
		simg.setVisible(true);	
	}
	
	// Loop through the map and get all available(EMPTY) cells
	public synchronized static Cell getRandomEmptyCell() {
		ArrayList<Cell> emptyCells = myGame.getMap().getEmptyCells();
		
		// make sure station will not spawn on player
		Player p = myGame.getPlayer();
		emptyCells.remove(new Cell(p.getRow(), p.getCol()));
//		System.out.println(emptyCells);
		
		Random rand = new Random();
		int randIndex = rand.nextInt(emptyCells.size());	// rand num [0, emptyCells.size())
		return emptyCells.get(randIndex);
	}
	
	public synchronized static boolean isPause() {
		return pause;
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}
