package template;

import template.GameStage;
import template.GameTimer;
//import template.chat;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class GameOverStage {
	private StackPane pane;
	private Scene scene;
	private Stage stage;
	private GraphicsContext gc;
	private Canvas canvas;
	private GameTimer gametimer;
	private Button exitbtn = new Button("Exit Game");
	private Button menubtn = new Button("Main Menu");
	private String username;
	
	private final Button add = new Button("Add");
	private final VBox chatBox = new VBox(5);
	private List<Label> messages = new ArrayList<>();
	private ScrollPane container = new ScrollPane();
	private int index = 0;
	
	


	GameOverStage(String username){
		
		this.username = username;
		this.pane = new StackPane();
		this.scene = new Scene(pane, GameStage.WINDOW_WIDTH,GameStage.WINDOW_HEIGHT);
		this.canvas = new Canvas(GameStage.WINDOW_WIDTH, GameStage.WINDOW_HEIGHT);
		this.gc = canvas.getGraphicsContext2D();

	}

	protected void setStage(int n, Stage stage,GameTimer gametimer){
		this.gc.drawImage(GameStage.BG, 0, 0);
		this.gc.setFill(Color.BLACK);
		this.gametimer = gametimer;
		pane.getChildren().add(this.canvas);

		//display win or lose, 1=win
		if (n==1){
			this.gc.fillText("YOU WIN!", GameStage.WINDOW_WIDTH*0.32,GameStage.WINDOW_HEIGHT*0.42);
			
			//next stage button
			//display waiting for others text kung hindi pa lahat ay pumipindot

		}else{
			this.gc.fillText("YOU LOSE :(", GameStage.WINDOW_WIDTH*0.28,GameStage.WINDOW_HEIGHT*0.42);
			//retry button
			menubtn.setTranslateX(-30);
			menubtn.setTranslateY(80);
			this.addEventHandler(menubtn);
			pane.getChildren().add(menubtn);
		}

		//initialize buttons
		

		exitbtn.setTranslateX(-30);
		exitbtn.setTranslateY(135);
		this.addEventHandler(exitbtn);
		
		//add chatbox

		//set stage elements here
		
		pane.getChildren().addAll(exitbtn);
		
		this.stage = stage;


		this.stage.setTitle("GameOver");
		this.stage.setScene(this.scene);
		this.stage.show();
	}

	private void addEventHandler(Button btn) {
		if (btn.equals(this.exitbtn)){
			btn.setOnMouseClicked(new EventHandler<MouseEvent>() {

				public void handle(MouseEvent arg0) {
					System.exit(0);
				}
			});
		}else if (btn.equals(this.menubtn)){
			btn.setOnMouseClicked(new EventHandler<MouseEvent>() {
				public void handle(MouseEvent arg0) {
					GameStage menuStage = new GameStage();
					menuStage.setStage(stage);
			}
		});
		}
	}
	

	
	private void initChatBox(){

	    container.setPrefSize(216, 400);
	    container.setContent(chatBox);
		chatBox.minHeight(400);
		chatBox.minHeight(200);

	    chatBox.getStyleClass().add("chatbox");

	    add.setOnAction(evt->{

	        messages.add(new Label("I'm a message"));

	        messages.get(index).setAlignment(Pos.CENTER_LEFT);

	        chatBox.getChildren().add(messages.get(index));
	        index++;

	    });


	    pane.getChildren().addAll(container,add);
	}

}
