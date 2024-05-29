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

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.List;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class GameOverStage implements Runnable{
	private StackPane pane;
	private Scene scene;
	private Stage stage;
	private GraphicsContext gc;
	private Canvas canvas;
	private GameTimer gametimer;
	private Button exitbtn = new Button("Exit Game");
	private Button menubtn = new Button("Main Menu");
	
	private final Button add;
	private final VBox chatBox;
	private final TextField msgBox;
	private TextArea messages;
	private ScrollPane container;
	private int index;
	
	Thread t = new Thread(this);
	/**
	 * Flag to indicate whether this player has connected or not
	 */
	boolean connected=false;

	/**
	 * get a datagram socket
	 */
    DatagramSocket socket = new DatagramSocket();
	
	/**
	 * Player name of others
	 */
	String pname;
	/**
	 * Placeholder for data received from server
	 */
	String serverData;
	String server;
	String name;


	GameOverStage(String name) throws SocketException{
		this.name = name;
		this.pane = new StackPane();
		this.scene = new Scene(pane, GameStage.WINDOW_WIDTH,GameStage.WINDOW_HEIGHT);
		this.canvas = new Canvas(GameStage.WINDOW_WIDTH, GameStage.WINDOW_HEIGHT);
		this.gc = canvas.getGraphicsContext2D();
		
		this.add = new Button("Add");
		this.chatBox = new VBox(5);
		this.msgBox = new TextField();
		this.messages = new TextArea();
		this.container  = new ScrollPane();
		this.index = 0;
		socket.setSoTimeout(100);
		t.start();

	}

	protected void setStage(int n, Stage stage,GameTimer gametimer){
		this.gc.drawImage(GameStage.BG, 0, 0);
		this.gc.setFill(Color.BLACK);
		this.gametimer = gametimer;
		pane.getChildren().addAll(this.canvas);
		//add chatbox
				container.setPrefSize(100, GameStage.WINDOW_HEIGHT);
			    container.setContent(chatBox); 
			    container.setLayoutX(GameStage.WINDOW_WIDTH-100);
				container.setLayoutY(0);
				chatBox.setPrefSize(100,GameStage.WINDOW_HEIGHT);
			    chatBox.setLayoutX(GameStage.WINDOW_HEIGHT-100);
				chatBox.setLayoutY(0);
			    msgBox.prefHeight(50);
				msgBox.prefWidth(200);
				msgBox.setLayoutX(GameStage.WINDOW_WIDTH-250);
				msgBox.setLayoutY(GameStage.WINDOW_HEIGHT-50);
				add.setLayoutX(GameStage.WINDOW_WIDTH-50);
				add.setLayoutY(GameStage.WINDOW_HEIGHT-50);
			    add.setOnAction(evt->{
			    	String temp = messages.getText();
			        
			        sendChatMessage(msgBox.getText());
			        
			        msgBox.clear();
			    });


			    pane.getChildren().addAll(container,chatBox,messages,msgBox,add);

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
	
	
	public void send(String msg){
		try{
			byte[] buf = msg.getBytes();
        	InetAddress address = InetAddress.getByName(server);
        	DatagramPacket packet = new DatagramPacket(buf, buf.length, address, 1234);
        	socket.send(packet);
        }catch(Exception e){}
		
	}
	
	public void sendChatMessage(String message) {
        send("MESSAGE " + name + ": " + message);
        System.out.print("To server MESSAGE " + name + ": " + message+"\n");
    }
	
	public void run() {
        while (true) {
            try {
                Thread.sleep(1);
            } catch (Exception ioe) {
            }

            //Get the data from players
            byte[] buf = new byte[256];
            DatagramPacket packet = new DatagramPacket(buf, buf.length);
            try {
                socket.receive(packet);
            } catch (Exception ioe) {/*lazy exception handling :)*/
            }

            serverData = new String(buf);
            serverData = serverData.trim();

            //if (!serverData.equals("")){
            //	System.out.println("Server Data:" +serverData);
            //}
            //Study the following kids. 
            if (!connected && serverData.startsWith("CONNECTED")) {
                connected = true;
                System.out.println("Connected.");
            } else if (!connected) {
                System.out.println("Connecting..");
                send("CONNECT " + name);
            } else if (connected) {
                if (serverData.startsWith("MESSAGE")) {
                    // Tokenize chat message
                    String[] tokens = serverData.split(" ", 2);
                    String message = tokens[1];
                    // Display chat message in chat panel
                    System.out.print("from server MESSAGE " + message + "\n");
                    
                    String temp = messages.getText();
			        messages.setText(temp+message+"\n");
                }
            }
        }
    }
	
	

}
