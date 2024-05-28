package template;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import template.GameTimer;

public class Server implements Runnable{
	String playerData;
	int playerCount = 0;
	DatagramSocket serverSocket = null;
	GameState game;
	int gameStage=WAITING_FOR_PLAYERS;
	int numPlayers;
	
	Thread t = new Thread(this);
	
	public void GameServer(int numPlayers) {
		this.numPlayers = numPlayers;
		
		try {
			serverSocket = new DatagramSocket(5050);
			serverSocket.setSoTimeout(100);
		} catch (IOException e) {
			System.err.println("Could not listen to port 5050");
			System.exit(-1);
		} catch (Exception e) {}
		
		game = new GameState();
		
		System.out.println("Game Created.");
		
		t.start();
	}
	
	public void broadcast(String msg) {
		for(Iterator ite=game.getPlayers().keySet().iterator();ite.hasNext();) {
			String name=(String)ite.next();
			GameTimer player=(GameTimer)game.getPlayers().get(playerId);
			send(player,msg);
		}
	}
	
	public void send(GamerTimer player, String msg) {
		DatagramPacket packet;
		byte buf[] = msg.getBytes();
		packet = new DatagramPacket(buf, buf.length, player.getA);
	}
}
