package template;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;

import javafx.animation.AnimationTimer;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Font;
import sprites.Bullet;
import sprites.Crosshair;
import sprites.Fubuchan;

/**
 * Client ata dapat to???
 */
public class GameTimer extends AnimationTimer{
	private GraphicsContext gc;
	private Scene scene;
	private Fubuchan ship;
	private Crosshair crosshair;
	private GameStage gs;
	private long gameStart;
	private int inGameTime;
	private boolean win;
	
	double prevX,prevY,prevXM,prevYM,x, y;
	String server, name;
	/**
	 * Player name of others
	 */
	String pname;
	/**
	 * Flag to indicate whether this player has connected or not
	 */
	boolean connected=false;
	/**
	 * get a datagram socket
	 */
    DatagramSocket socket = new DatagramSocket();
    /**
     * Placeholder for data received from server
     */
	String serverData;

	GameTimer(GraphicsContext gc, Scene scene, GameStage gs, String server,String name) throws Exception{
		this.gc = gc;
		this.gs = gs;
		this.scene = scene;
		this.gameStart = System.currentTimeMillis();
		this.inGameTime = 0;
		this.win = false;
		
		this.server = server;
		this.name = name;
		//set some timeout for the socket
		socket.setSoTimeout(100);
		
		// Thread t=new Thread(this); //idk yet
		
		
				
		//Initialization
		this.init();
		//Process Input
		this.processInput();
	}
	
	//net methods
	/**
	 * Helper method for sending data to server
	 * @param msg
	 */
	public void send(String msg){
		try{
			byte[] buf = msg.getBytes();
        	InetAddress address = InetAddress.getByName(server);
        	DatagramPacket packet = new DatagramPacket(buf, buf.length, address, 1234);
        	socket.send(packet);
        }catch(Exception e){}
		
	}
	
	@Override
	public void handle(long now) {
		// Updating game states
		this.update(now);
		
		// Rendering visual changes
		this.render();
	}
	
	// Initialize method
	public void init() {
		// // Adding the ship
		// this.ship = new Fubuchan(150, 200);
		
		
		// crosshair
		this.crosshair = new Crosshair(GameStage.WINDOW_WIDTH/2, GameStage.WINDOW_HEIGHT/2);
	}
	
	// Process input method
	private void processInput() {
		// Moving ship and bullets
		// Fubuchan ship = this.ship;
		Crosshair crosshair = this.crosshair;
		// this.scene.setOnKeyPressed(new EventHandler<KeyEvent>() {
		// 	public void handle(KeyEvent e) {
		// 		KeyCode code = e.getCode();
		// 		prevX=ship.getX();prevY=ship.getY();
				
		// 		// Possible movements
		// 		if (code == KeyCode.W) {
		// 			ship.setdY(-Fubuchan.SHIP_SPEED);
		// 			ship.setOrientation('W');
		// 		}
				
		// 		if (code == KeyCode.S) {
		// 			ship.setdY(Fubuchan.SHIP_SPEED);
		// 			ship.setOrientation('E');
		// 		}
				
		// 		if (code == KeyCode.A) { 
		// 			ship.setdX(-Fubuchan.SHIP_SPEED);
		// 			ship.setOrientation('W');
		// 		}
				
		// 		if (code == KeyCode.D) {
		// 			ship.setdX(Fubuchan.SHIP_SPEED);
		// 			ship.setOrientation('E');
		// 		}
		// 		// Logging movements
		// 		System.out.println(code + " key pressed.");
				
		// 		if (prevX != x || prevY != y){
		// 			send("PLAYER "+name+" "+x+" "+y);
		// 		}
				

		// 	}
		// });
		
		// this.scene.setOnKeyReleased(new EventHandler<KeyEvent>() {
		// 	public void handle(KeyEvent e) {
		// 		// Stopping movements
		// 		ship.setdX(0);
		// 		ship.setdY(0);
		// 		ship.setOrientation(' ');
		// 	}
		// });
		
		// this.scene.setOnMouseMoved(new EventHandler<MouseEvent>() {
		// 	public void handle(MouseEvent e) {
		// 		crosshair.setdXdY(e.getX(), e.getY());
			
		// 		System.out.println("MOUSE X : " + e.getX() + " MOUSE Y : " + e.getY());
		// 		if (prevXM != e.getX() || prevYM != e.getY()){
		// 			send("PLAYER MOUSE"+name+" "+e.getX()+" "+e.getX());
		// 		}
		// 	}
		// });
		
		// this.scene.setOnMouseClicked(new EventHandler<MouseEvent>() {
		// 	public void handle(MouseEvent e) {
		// 		crosshair.setdXdY(e.getX(), e.getY());
		// 		ship.shoot(e.getX(), e.getY());
		// 	}
		// });
		
		// flame thrower 
		//this.scene.setOnMouseDragged(new EventHandler<MouseEvent>() {
		//	public void handle(MouseEvent e) {
		//		crosshair.setdXdY(e.getX(), e.getY());
		//		ship.shoot(e.getX(), e.getY());
		//	}
		//});
	}
	
	// Update method
	private void update(long now) {
		// updating time
		this.inGameTime = (int) (System.currentTimeMillis() - this.gameStart) / 1000;
		
		// moving sprites
		// this.ship.move();
		this.crosshair.move();
		
		
		// for (Bullet b : this.ship.getBulletList()) {
		// 	b.move();
		// }

		
		// checking if the game is over
		if (this.inGameTime > 60 ) {
			this.stop();
			this.win = true;
			this.gs.setGameOver(1);
		}
		
		// checking collisions
		// this.detectCollisions();
				
		// // removing bullets and power ups outside the window
		// this.cleanBullets();
		
	}
	
	// Render method
	private void render() {		
		// rendering game area
		this.gc.clearRect(0, 0, GameStage.WINDOW_WIDTH,GameStage.WINDOW_HEIGHT);
		this.gc.drawImage(GameStage.BG, 0, 0);
		
		// rendering scoreboard
		this.drawScoreboard();
		
		// rendering the units
		// this.ship.render(this.gc);
		
		// render crosshair
		this.crosshair.render(this.gc);
		
		
		// rendering bullets
		// for (Bullet bullets : this.ship.getBulletList()) {
		// 	bullets.render(this.gc);
		// }
		
	}

	// private void detectCollisions() {
	// 	// Using iterator to avoid ConcurrencyModificationException
	// 	for (Iterator<Fish> i = this.fishes.iterator(); i.hasNext(); ) {
	// 		Fish f = i.next();
	// 		for (Iterator<Bullet> j = this.ship.getBulletList().iterator(); j.hasNext(); ) {
	// 			Bullet b = j.next();
				
	// 			if (b.collidesWith(f)) {
	// 				// bullet deals damage to fish and is removed
	// 				b.doDamage(f);
	// 				j.remove();
					
	// 				// if fish is dead, remove fish
	// 				if (!f.isAlive()) {
	// 					i.remove();
	// 				}
	// 			}
	// 		}
			
	// 		if (f.collidesWith(this.ship)) {
	// 			// fish deals damage to ship and is removed
	// 			f.doDamage(this.ship);
	// 			i.remove();
				
	// 			// if ship is destroyed, end the game
	// 			if (!this.ship.isAlive()) {
	// 				this.stop();
	// 				this.gs.setGameOver(0);
	// 				System.out.println("Game Over");
	// 			}
	// 		}
	// 	}
		
	// 	for (Iterator<PowerUp> k = this.powerUps.iterator(); k.hasNext(); ) {
	// 		PowerUp p = k.next();
			
	// 		if (p.collidesWith(this.ship)) {
	// 			// p modifies the ship's stats
	// 			p.modifyShip(this.ship);
	// 			k.remove();
	// 		}
	// 	}
	// }
	
	// private void cleanBullets() {
	// 	for (Iterator<Bullet> j = this.ship.getBulletList().iterator(); j.hasNext(); ) {
	// 		Bullet b = j.next();
			
	// 		if (b.getPositionX() + Bullet.BULLET.getWidth() > GameStage.WINDOW_WIDTH) {
	// 			j.remove();
	// 		}
	// 	}
	// }

	private void drawScoreboard() {
		this.gc.setFont(Font.font("Calibri", 30));
		this.gc.fillText("Strength: ", 10, 30);
		this.gc.fillText(Integer.toString(this.ship.getStrength()), 140, 30);
		this.gc.fillText("Time: ", GameStage.WINDOW_WIDTH / 2 - 60, 30);
		this.gc.fillText(Integer.toString(this.inGameTime), GameStage.WINDOW_WIDTH / 2 + 30, 30);
		this.gc.fillText("Score: ", GameStage.WINDOW_WIDTH - 140, 30);
		this.gc.fillText(Integer.toString(this.ship.getScore()), GameStage.WINDOW_WIDTH - 40, 30);
	}

	
	public boolean isWin() {
		return this.win;
	}


}
