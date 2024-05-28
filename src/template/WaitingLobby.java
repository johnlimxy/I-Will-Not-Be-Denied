package template;

import java.util.ArrayList;
  import java.util.List;

  import javafx.application.Application;
  import javafx.geometry.Pos;
  import javafx.scene.Scene;
  import javafx.scene.control.Button;
  import javafx.scene.control.Label;
  import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
  import javafx.scene.layout.VBox;
  import javafx.stage.Stage;
  
 import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;


public class WaitingLobby implements Runnable{

	private Pane root = new Pane();
	private Scene scene;
	
	private final Button add = new Button("Send");
	private final VBox chatBox = new VBox(5);
	private final TextField msgBox = new TextField();
	private List<Label> messages = new ArrayList<>();
	private ScrollPane container = new ScrollPane();
	private int index = 0;
	Thread t=new Thread(this);
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
	

	public WaitingLobby(Stage stage, String server, String name) throws Exception{
	
	    initChatBox();
	    this.server=server;
		this.name=name;
		socket.setSoTimeout(100);
	    root.getStylesheets().add(getClass().getResource("Style.css").toExternalForm());
	    root.getChildren().addAll(container,add,msgBox);
	    scene = new Scene(root,GameStage.WINDOW_WIDTH,GameStage.WINDOW_HEIGHT);
	    stage.setScene(scene);
	    stage.show();
	    t.start();
	    
	
	}
	
	private void initChatBox(){
	
	    container.setPrefSize(216, 400);
	    container.setContent(chatBox); 
	
	    chatBox.getStyleClass().add("chatbox");
	    msgBox.setLayoutX(100);
		msgBox.setLayoutY(100);
		msgBox.prefHeight(50);
		msgBox.prefWidth(100);
		String message = msgBox.getText();
	
	    add.setOnAction(evt->{
	
	        messages.add(new Label(message));
	        messages.get(index).setAlignment(Pos.CENTER_LEFT);
	        chatBox.getChildren().add(messages.get(index));
	        index++;
	        
	        send("MESSAGE,,"+name+",,"+ message);
	    });
	
	
	
	}
	
	public void send(String msg){
		try{
			byte[] buf = msg.getBytes();
        	InetAddress address = InetAddress.getByName(server);
        	DatagramPacket packet = new DatagramPacket(buf, buf.length, address, 1234);
        	socket.send(packet);
        }catch(Exception e){}
		
	}
	public void run(){
		while(true){
			try{
				Thread.sleep(1);
			}catch(Exception ioe){}
						
			//Get the data from players
			byte[] buf = new byte[256];
			DatagramPacket packet = new DatagramPacket(buf, buf.length);
			try{
     			socket.receive(packet);
			}catch(Exception ioe){/*lazy exception handling :)*/}
			
			serverData=new String(buf);
			serverData=serverData.trim();
			
			//if (!serverData.equals("")){
			//	System.out.println("Server Data:" +serverData);
			//}

			//Study the following kids. 
			if (!connected && serverData.startsWith("CONNECTED")){
				connected=true;
				System.out.println("Connected.");
			}else if (!connected){
				System.out.println("Connecting..");				
				send("CONNECT "+name);
			}else if (connected){
				if (serverData.startsWith("MESSAGE")){
					String[] playersInfo = serverData.split(":");
					for (int i=0;i<playersInfo.length;i++){
						String[] playerInfo = playersInfo[i].split(",,");
						messages.add(new Label(playerInfo[1] +": "+playerInfo[2]));	
					}
				}			
			}			
		}
	}



}