package pokemon.ui;

import java.io.File;
import java.util.Random;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.*;
import javafx.scene.control.Label;
import javafx.scene.image.*;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import pokemon.Cell;
import pokemon.Game;
//import pokemon.Empty;
import pokemon.Map;
import pokemon.Map.MapType;
import pokemon.Player;
import pokemon.Pokemon;
import pokemon.Station;
//import pokemon.Wall;

public class PokemonScreenLAB9 extends Application {

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
	
	// avatar images
	private ImageView avatar;
	private Image avatarImage;

	// these booleans correspond to the key pressed by the user
	boolean goUp, goDown, goRight, goLeft;

	// current position of the avatar
	double currentPosx = 0;
	double currentPosy = 0;

	protected boolean stop = false;
	
	private SimpleIntegerProperty score = new SimpleIntegerProperty(0); 
	private SimpleIntegerProperty numCaught = new SimpleIntegerProperty(0); 
	private SimpleIntegerProperty numBalls = new SimpleIntegerProperty(0); 
	

	@Override
	public void start(Stage stage) throws Exception {
		
		HBox hbox = new HBox(20);
		hbox.setPadding(new Insets(10));
		
		// Setup Map and add to myGroup
		Group mapGroup = new Group();
		buildMap(mapGroup);
		
		hbox.getChildren().add(mapGroup);

		
		// right-sided menu
		VBox rPanel = new VBox(10);
		
		Label lableScore = new Label();
		lableScore.textProperty().bind(score.asString());		
		rPanel.getChildren().add(new HBox(new Label("Current score:"), lableScore));
		
		Label labelCaught = new Label();
		labelCaught.textProperty().bind(numCaught.asString());	
		rPanel.getChildren().add(new HBox(new Label("# of Pokemons caught:"), labelCaught));
		
		Label labelBalls = new Label();
		labelBalls.textProperty().bind(numBalls.asString());	
		rPanel.getChildren().add(new HBox(new Label("# of Pokeballs owned:"),labelBalls));

		hbox.getChildren().add(rPanel);

		// create scene
		Scene scene = new Scene(hbox);

		// add listener on key pressing
		scene.setOnKeyPressed(new EventHandler<KeyEvent>() {
			@Override
			public void handle(KeyEvent event) {
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
				switch (event.getCode()) {
				case UP:
					goUp = false;
					break;
				case DOWN:
					goDown = false;
					break;
				case LEFT:
					goLeft = false;
					break;
				case RIGHT:
					goRight = false;
					break;
				default:
					break;
				}
				stop = false;
			}
		});

		stage.setScene(scene);
		stage.show();

		// it will execute this periodically
		AnimationTimer timer = new AnimationTimer() {
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
				
				// TODO: check if the row and col is movealbe
				if(myGame.getMap().canWalk(row, col)) {
					// move image of avatar
					moveAvatarBy(dx, dy);
					
					// update player position
					player.setRow(row);
					player.setCol(col);
					player.addVistedCell(new Cell(row, col));
					// update score label
					score.setValue(myGame.getPlayer().getScore());
				}

			}
		};
		// start the timer
		timer.start();
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
					Pokemon pkm = myGame.getPokemon(i, j);
					int id = PokemonList.getIdOfFromName(pkm.getName());;
					String path = new File("icons/" + id + ".png").toURI().toString();
					img = new ImageView(path);
					break;
					
				case SUPPLY:
					img = new ImageView(ball);
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

	public static void main(String[] args) {
		launch(args);
	}

}
