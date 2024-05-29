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
import sprites.Fish;
import sprites.Iron;
import sprites.Fubuchan;
import sprites.Sash;
import sprites.PowerUp;

/**
 * Client ata dapat to???
 */
public class GameTimer extends AnimationTimer{
	private GraphicsContext gc;
	private Scene scene;
	private ArrayList<Fish> fishes;
	private ArrayList<PowerUp> powerUps;
	private Fubuchan ship;
	private Crosshair crosshair;
	private GameStage gs;
	private long gameStart;
	private int inGameTime;
	private long fishDelay;
	private long powerUpDelay;
	private long bigFishDelay;
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
//    Thread t = new Thread(this);
    /**
     * Placeholder for data received from server
     */
	String serverData;

	GameTimer(GraphicsContext gc, Scene scene, GameStage gs, String server,String name) throws Exception{
		this.gc = gc;
		this.gs = gs;
		this.scene = scene;
		this.fishes = new ArrayList<Fish>();
		this.powerUps = new ArrayList<PowerUp>();
		this.gameStart = System.currentTimeMillis();
		this.fishDelay = this.powerUpDelay = this.bigFishDelay = System.nanoTime();
		this.inGameTime = 0;
		this.win = false;
		
		this.server = server;
		this.name = name;
		//set some timeout for the socket
		socket.setSoTimeout(100);
	
		//Initialization
		this.init();
		//Process Input
		this.processInput();
		
//		t.start();
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
		// Adding the ship
		this.ship = new Fubuchan(150, 200);
		
		// Adding the fishes
		this.addFishes(7);
		
		// crosshair
		this.crosshair = new Crosshair(GameStage.WINDOW_WIDTH/2, GameStage.WINDOW_HEIGHT/2);
	}
	
	// Process input method
	private void processInput() {
		// Moving ship and bullets
		Fubuchan ship = this.ship;
		Crosshair crosshair = this.crosshair;
		this.scene.setOnKeyPressed(new EventHandler<KeyEvent>() {
			public void handle(KeyEvent e) {
				KeyCode code = e.getCode();
				prevX=ship.getX();prevY=ship.getY();
				
				// Possible movements
				if (code == KeyCode.W) {
					ship.setdY(-Fubuchan.SHIP_SPEED);
					ship.setOrientation('W');
				}
				
				if (code == KeyCode.S) {
					ship.setdY(Fubuchan.SHIP_SPEED);
					ship.setOrientation('E');
				}
				
				if (code == KeyCode.A) { 
					ship.setdX(-Fubuchan.SHIP_SPEED);
					ship.setOrientation('W');
				}
				
				if (code == KeyCode.D) {
					ship.setdX(Fubuchan.SHIP_SPEED);
					ship.setOrientation('E');
				}
				// Logging movements
				System.out.println(code + " key pressed.");
				
				if (prevX != x || prevY != y){
					send("PLAYER "+name+" "+x+" "+y);
				}
				

			}
		});
		
		this.scene.setOnKeyReleased(new EventHandler<KeyEvent>() {
			public void handle(KeyEvent e) {
				// Stopping movements
				ship.setdX(0);
				ship.setdY(0);
				ship.setOrientation(' ');
			}
		});
		
		this.scene.setOnMouseMoved(new EventHandler<MouseEvent>() {
			public void handle(MouseEvent e) {
				crosshair.setdXdY(e.getX(), e.getY());
			
				System.out.println("MOUSE X : " + e.getX() + " MOUSE Y : " + e.getY());
				if (prevXM != e.getX() || prevYM != e.getY()){
					send("PLAYER MOUSE"+name+" "+e.getX()+" "+e.getX());
				}
			}
		});
		
		this.scene.setOnMouseClicked(new EventHandler<MouseEvent>() {
			public void handle(MouseEvent e) {
				crosshair.setdXdY(e.getX(), e.getY());
				ship.shoot(e.getX(), e.getY());
			}
		});
		
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
		this.ship.move();
		this.crosshair.move();
		
		double fishM;
		double s;
		char fishOrientation;
		
		for (Bullet b : this.ship.getBulletList()) {
			b.move();
		}
		for (Fish f : this.fishes) {
			fishM = (this.ship.getY() - f.getY())/(this.ship.getX() - f.getX());
			s = f.getMaxSpeed()/Math.sqrt(1 + (fishM*fishM));
			
			f.setdX(s);
			f.setdY(s*fishM);
			
			if (this.ship.getX() >= f.getX()) {
				fishOrientation = 'E';
			} else {
				fishOrientation = 'W';
			}
			
			f.move(fishOrientation);
		}
		for (PowerUp p : this.powerUps) {
			p.move();
		}
		
		// adding wave of fishes every 5s
		double fishDelay = (now - this.fishDelay) / 1000000000.0;
		if (fishDelay > 5) {
			this.addFishes(3);
			this.fishDelay = System.nanoTime();
		}
		
		// spawning big fish
		double bigFishDelay = (now - this.bigFishDelay) / 1000000000.0;
		if (bigFishDelay > 30) {
			this.addBigFish();
			this.bigFishDelay = System.nanoTime();
		}
		
		// spawning power ups randomly
		double powerUpDelay = (now - this.powerUpDelay) / 1000000000.0;
		if (powerUpDelay > 10) {
			this.addPowerUp();
			this.powerUpDelay = System.nanoTime();
		}
		
		// checking if the game is over
		if (this.inGameTime > 60 ) {
			this.stop();
			this.win = true;
			this.gs.setGameOver(1);
		}
		
		// checking collisions
		this.detectCollisions();
				
		// removing bullets and power ups outside the window
		this.cleanBullets();
		this.cleanPowerUps();
		
	}
	
	// Render method
	private void render() {		
		// rendering game area
		this.gc.clearRect(0, 0, GameStage.WINDOW_WIDTH,GameStage.WINDOW_HEIGHT);
		this.gc.drawImage(GameStage.BG, 0, 0);
		
		// rendering scoreboard
		this.drawScoreboard();
		
		// rendering the units
		this.ship.render(this.gc);
		
		// render crosshair
		this.crosshair.render(this.gc);
		
		// rendering fishes
		for (Fish fish : this.fishes) {
			fish.render(this.gc);	
		}
		
		// rendering bullets
		for (Bullet bullets : this.ship.getBulletList()) {
			bullets.render(this.gc);
		}
		
		// rendering power ups
		for (PowerUp powerUp : this.powerUps) {
			powerUp.render(this.gc);
		}
	}
	
	// Miscellaneous Methods
	private void addFishes(int n) {
		Random r = new Random();
		for (int i = 0; i < n; i++) {
			double x;
			double y;
			if (this.ship.getX() < (GameStage.WINDOW_WIDTH / 2)) {
				x = r.nextInt((int)GameStage.WINDOW_WIDTH / 2) + this.ship.getX();
			}else {
				x = this.ship.getX() - r.nextInt((int)GameStage.WINDOW_WIDTH / 2);
			}
			
			if (this.ship.getY() < (GameStage.WINDOW_HEIGHT / 2)) {
				y = r.nextInt((int)GameStage.WINDOW_HEIGHT / 2) + this.ship.getY();
			}else {
				y = this.ship.getY() - r.nextInt((int)GameStage.WINDOW_HEIGHT / 2);
			}
			Fish fish = new Fish(x, y, 1);
			this.fishes.add(fish);
		}
	}
	
	private void addBigFish() {
		Random r = new Random();
		int x = r.nextInt(151) + 540;
		int y = r.nextInt(517) + 30;
		Fish fish = new Fish(x, y, 2);
		this.fishes.add(fish);
	}
	
	private void addPowerUp() {
		PowerUp powerUp;
		Random r = new Random();
		int x = r.nextInt(191) + 40;
		int y = r.nextInt(551) + 30;
		
		if (r.nextInt(2) == 1) {
			powerUp = new Iron(x, y);
		} else {
			powerUp = new Sash(x, y);
		}
		
		powerUp.getTimer().start();
		this.powerUps.add(powerUp);
	}

	private void detectCollisions() {
		// Using iterator to avoid ConcurrencyModificationException
		for (Iterator<Fish> i = this.fishes.iterator(); i.hasNext(); ) {
			Fish f = i.next();
			for (Iterator<Bullet> j = this.ship.getBulletList().iterator(); j.hasNext(); ) {
				Bullet b = j.next();
				
				if (b.collidesWith(f)) {
					// bullet deals damage to fish and is removed
					b.doDamage(f);
					j.remove();
					
					// if fish is dead, remove fish
					if (!f.isAlive()) {
						i.remove();
					}
				}
			}
			
			if (f.collidesWith(this.ship)) {
				// fish deals damage to ship and is removed
				f.doDamage(this.ship);
				i.remove();
				
				// if ship is destroyed, end the game
				if (!this.ship.isAlive()) {
					this.stop();
					this.gs.setGameOver(0);
					System.out.println("Game Over");
				}
			}
		}
		
		for (Iterator<PowerUp> k = this.powerUps.iterator(); k.hasNext(); ) {
			PowerUp p = k.next();
			
			if (p.collidesWith(this.ship)) {
				// p modifies the ship's stats
				p.modifyShip(this.ship);
				k.remove();
			}
		}
	}
	
	private void cleanBullets() {
		for (Iterator<Bullet> j = this.ship.getBulletList().iterator(); j.hasNext(); ) {
			Bullet b = j.next();
			
			if (b.getPositionX() + Bullet.BULLET.getWidth() > GameStage.WINDOW_WIDTH) {
				j.remove();
			}
		}
	}
	
	private void cleanPowerUps() {
		for (Iterator<PowerUp> j = this.powerUps.iterator(); j.hasNext(); ) {
			PowerUp p = j.next();
			
			if ((p.getPositionY() + p.getImage().getWidth() > GameStage.WINDOW_WIDTH) || p.isExpired()) {
				j.remove();
			}
		}
	}

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

                if (serverData.startsWith("PLAYER")) {
                    String[] playersInfo = serverData.split(":");
                    for (int i = 0; i < playersInfo.length; i++) {
                        String[] playerInfo = playersInfo[i].split(" ");
                        String pname = playerInfo[1];
                        int x = Integer.parseInt(playerInfo[2]);
                        int y = Integer.parseInt(playerInfo[3]);
                        // player.setX(x);
                        // player.setY(y);
//                        updatePlayerPosition(pname, x, y);

                    }
                    //show the changes
//                    repaint();
                }

            }
        }
    }
	

	
	



}
