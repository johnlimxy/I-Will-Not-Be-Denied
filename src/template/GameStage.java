package template;

import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.scene.Scene;
import javafx.animation.FadeTransition;
import javafx.animation.TranslateTransition;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

public class GameStage {
	// Class Attributes
	public final static Image BG = new Image("assets/bg.jpg");
	public final static Image LOGO = new Image("assets/title.png");
	public final static Image NEW_GAME = new Image("assets/new_game.png");
	public final static Image NEW_GAME_HOVER = new Image("assets/new_game_hover.png");
	public final static Image ABOUT = new Image("assets/about.png");
	public final static Image ABOUT_HOVER = new Image("assets/about_hover.png");
	public final static Image HELP = new Image("assets/help.png");
	public final static Image HELP_HOVER = new Image("assets/help_hover.png");
	public final static Image INSTRUCTIONS = new Image("assets/instructions.png");
	public final static Image BACK = new Image("assets/back.png");
	public final static Image BACK_HOVER = new Image("assets/back_hover.png");
	public final static Image CREDITS = new Image("assets/credits.png");
	public final static Image WIN = new Image("assets/win.png");
	public final static Image LOSE = new Image("assets/gameover.png");
	public final static Image ICON = new Image("assets/icon.png");
	public final static double WINDOW_WIDTH = BG.getWidth();
	public final static double WINDOW_HEIGHT = BG.getHeight();
	
	// Instance attributes
	private Stage stage;
	private Group root;
	private Canvas canvas;
	private GameTimer gametimer;
	private GraphicsContext gc;
	private Scene scene;
	
	// Constructor
	public GameStage() {
		// Instantiation
		this.root = new Group();
		this.scene = new Scene(root, GameStage.WINDOW_WIDTH, GameStage.WINDOW_HEIGHT);
		this.canvas = new Canvas(GameStage.WINDOW_WIDTH, GameStage.WINDOW_HEIGHT);
		this.gc = this.canvas.getGraphicsContext2D();
		this.root.getChildren().add(this.canvas);
	}
	
	// Methods
	public void selectSplash(Stage stage) {
		// New pane for the splash select
		Pane root = new Pane();
		
		// Background
		Canvas canvas = new Canvas(GameStage.WINDOW_WIDTH, GameStage.WINDOW_HEIGHT);
		GraphicsContext gc = canvas.getGraphicsContext2D();
		gc.drawImage(GameStage.BG, 0, 0);
		
		// Title Logo
		ImageView logo = new ImageView();
		logo.setImage(GameStage.LOGO);
		logo.setLayoutX(GameStage.WINDOW_WIDTH / 2 - GameStage.LOGO.getWidth() / 2);
		logo.setLayoutY(GameStage.WINDOW_HEIGHT * 2 / 6 - GameStage.LOGO.getHeight() / 2 - 20);
		
		// Buttons
		ImageView newGame = new ImageView();
		newGame.setImage(GameStage.NEW_GAME);
		newGame.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
		     public void handle(MouseEvent event) {
				 stage.setScene(initGame(stage));
		         event.consume();
		     }
		});
		// hover listener
		newGame.hoverProperty().addListener((observable) -> {
			if (newGame.isHover()) {
				newGame.setImage(GameStage.NEW_GAME_HOVER);
			} else {
				newGame.setImage(GameStage.NEW_GAME);
			}
		});
		ImageView help = new ImageView();
		help.setImage(GameStage.HELP);
		help.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
		     public void handle(MouseEvent event) {
				 stage.setScene(initHelp(stage));
		         event.consume();
		     }
		});
		// hover listener
		help.hoverProperty().addListener((observable) -> {
			if (help.isHover()) {
				help.setImage(GameStage.HELP_HOVER);
			} else {
				help.setImage(GameStage.HELP);
			}
		});
		ImageView about = new ImageView();
		about.setImage(GameStage.ABOUT);
		about.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
		     public void handle(MouseEvent event) {
				 stage.setScene(initAbout(stage));
		         event.consume();
		     }
		});
		// hover listener
		about.hoverProperty().addListener((observable) -> {
			if (about.isHover()) {
				about.setImage(GameStage.ABOUT_HOVER);
			} else {
				about.setImage(GameStage.ABOUT);
			}
		});
		VBox vbox = new VBox(25);
		vbox.setAlignment(Pos.CENTER);
		vbox.getChildren().addAll(newGame, help, about);
		vbox.setLayoutX(GameStage.WINDOW_WIDTH / 2 - GameStage.NEW_GAME.getWidth() / 2);
		vbox.setLayoutY(GameStage.WINDOW_HEIGHT  * 3 / 4 - (GameStage.NEW_GAME.getHeight() + GameStage.HELP.getHeight() + GameStage.ABOUT.getHeight() + 40) / 2 - 30);
		
		// Animations
		FadeTransition fadein = new FadeTransition(Duration.millis(1500), logo);
		fadein.setFromValue(0.0);
	    fadein.setToValue(1.0);
	    fadein.play();
	    FadeTransition fadeinvbox = new FadeTransition(Duration.millis(2000), vbox);
		fadeinvbox.setFromValue(0.0);
	    fadeinvbox.setToValue(1.0);
	    fadeinvbox.play();
	    TranslateTransition movevbox = new TranslateTransition(Duration.millis(1500), vbox);
	    movevbox.setFromY(GameStage.WINDOW_HEIGHT - 100);
	    movevbox.setToY(0);
	    movevbox.play();
		
		// Adding nodes to the pane
		root.getChildren().addAll(canvas, vbox, logo);
		this.gc.drawImage(GameStage.BG, 0, 0);
		this.scene = new Scene(root);
	}
	
	public Scene initHelp(Stage stage) {
		// Setting up the instruction scene
		Pane root = new Pane();
		
		// Background
		Canvas canvas = new Canvas(GameStage.WINDOW_WIDTH, GameStage.WINDOW_HEIGHT);
		GraphicsContext gc = canvas.getGraphicsContext2D();
		gc.drawImage(GameStage.BG, 0, 0);
		
		// Instructions
		ImageView help = new ImageView();
		help.setImage(GameStage.INSTRUCTIONS);
		help.setLayoutX(GameStage.WINDOW_WIDTH / 2 - GameStage.INSTRUCTIONS.getWidth() / 2);
		help.setLayoutY(GameStage.WINDOW_HEIGHT / 3 - GameStage.INSTRUCTIONS.getHeight() / 2 + 55);
		
		// Back button
		ImageView exit = new ImageView();
		exit.setImage(GameStage.BACK);
		exit.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
		     public void handle(MouseEvent event) {
				 stage.setScene(scene);
		         event.consume();
		     }
		});
		exit.hoverProperty().addListener((observable) -> {
			if (exit.isHover()) {
				exit.setImage(GameStage.BACK_HOVER);
			} else {
				exit.setImage(GameStage.BACK);
			}
		});
		exit.setLayoutX(GameStage.WINDOW_WIDTH / 2 - GameStage.BACK.getWidth() / 2);
		exit.setLayoutY(GameStage.WINDOW_HEIGHT * 2 / 3 - GameStage.BACK.getHeight() / 2 + 55);
		
		// Adding nodes to the pane
		root.getChildren().addAll(canvas, exit, help);
		return new Scene(root);
	}
	
	public Scene initAbout(Stage stage) {
		// Setting up the about screen
		Pane root = new Pane();
		
		// Background
		Canvas canvas = new Canvas(GameStage.WINDOW_WIDTH, GameStage.WINDOW_HEIGHT);
		GraphicsContext gc = canvas.getGraphicsContext2D();
		gc.drawImage(GameStage.BG, 0, 0);
		
		// About
		ImageView about = new ImageView();
		about.setImage(GameStage.CREDITS);
		about.setLayoutX(318 - GameStage.CREDITS.getWidth() / 2);
		about.setLayoutY(266 - GameStage.CREDITS.getHeight() / 2);
		
		// Back Button
		ImageView exit = new ImageView();
		exit.setImage(GameStage.BACK);
		exit.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
		     public void handle(MouseEvent event) {
				 stage.setScene(scene);
		         event.consume();
		     }
		});
		exit.hoverProperty().addListener((observable) -> {
			if (exit.isHover()) {
				exit.setImage(GameStage.BACK_HOVER);
			} else {
				exit.setImage(GameStage.BACK);
			}
		});
		exit.setLayoutX(641 - GameStage.BACK.getWidth() / 2);
		exit.setLayoutY(532 - GameStage.BACK.getHeight() / 2);
		
		// Adding nodes to the pane
		root.getChildren().addAll(canvas, about, exit);
		return new Scene(root);
	}
	
	public Scene initGame(Stage stage) {
		// Setting up the game scene
		StackPane root = new StackPane();
		Canvas canvas = new Canvas(GameStage.WINDOW_WIDTH, GameStage.WINDOW_HEIGHT);
		GraphicsContext gc = canvas.getGraphicsContext2D();
		root.getChildren().add(canvas);
		Scene gameScene = new Scene(root);
		
		// Starting the game
		this.gametimer = new GameTimer(gc, gameScene, this);
		this.gametimer.start();
		
		return gameScene;
	}
	
	public void setGameOver() {
		// Game over screen
		StackPane root = new StackPane();
		
		// Setting the background
		Canvas canvas = new Canvas(GameStage.WINDOW_WIDTH, GameStage.WINDOW_HEIGHT);
		GraphicsContext gc = canvas.getGraphicsContext2D();
		gc.setFill(Color.BLACK);
		gc.fillRect(0, 0, GameStage.WINDOW_WIDTH, GameStage.WINDOW_HEIGHT);
		
		// Change scene content depending on the game results
		ImageView result = new ImageView();
		if (this.gametimer.isWin()) {
			result.setImage(GameStage.WIN);
		} else {
			result.setImage(GameStage.LOSE);
		}
		
		// Adding the node to the scene
		root.getChildren().addAll(canvas, result);
		this.stage.setScene(new Scene(root));
	}
	
	public void setStage(Stage stage) {
		// Setting up the welcome screen
		this.stage = stage;
		this.stage.setTitle("Fubuki's Shiny Magikarp");
		
		// Setting up the main menu
		this.selectSplash(this.stage);
		
		// Setting up the window
		this.stage.getIcons().add(GameStage.ICON);
		this.stage.setScene(this.scene);
		this.stage.setResizable(false);
		this.stage.show();
	}
}
