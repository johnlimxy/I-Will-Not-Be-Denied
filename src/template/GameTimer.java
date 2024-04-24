package template;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;

import javafx.animation.AnimationTimer;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.text.Font;
import sprites.Bullet;
import sprites.Fish;
import sprites.Iron;
import sprites.Fubuchan;
import sprites.Sash;
import sprites.PowerUp;

public class GameTimer extends AnimationTimer {
	private GraphicsContext gc;
	private Scene scene;
	private ArrayList<Fish> fishes;
	private ArrayList<PowerUp> powerUps;
	private Fubuchan ship;
	private GameStage gs;
	private long gameStart;
	private int inGameTime;
	private long fishDelay;
	private long powerUpDelay;
	private long bigFishDelay;
	private boolean win;

	GameTimer(GraphicsContext gc, Scene scene, GameStage gs) {
		this.gc = gc;
		this.gs = gs;
		this.scene = scene;
		this.fishes = new ArrayList<Fish>();
		this.powerUps = new ArrayList<PowerUp>();
		this.gameStart = System.currentTimeMillis();
		this.fishDelay = this.powerUpDelay = this.bigFishDelay = System.nanoTime();
		this.inGameTime = 0;
		this.win = false;
		
		//Initialization
		this.init();
		//Process Input
		this.processInput();
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
	}
	
	// Process input method
	private void processInput() {
		// Moving ship and bullets
		Fubuchan ship = this.ship;
		this.scene.setOnKeyPressed(new EventHandler<KeyEvent>() {
			public void handle(KeyEvent e) {
				KeyCode code = e.getCode();
				
				// Possible movements
				if (code == KeyCode.UP) ship.setdY(-Fubuchan.SHIP_SPEED);
				if (code == KeyCode.DOWN) ship.setdY(Fubuchan.SHIP_SPEED);
				if (code == KeyCode.LEFT) ship.setdX(-Fubuchan.SHIP_SPEED);
				if (code == KeyCode.RIGHT) ship.setdX(Fubuchan.SHIP_SPEED);
				// Shooting
				if (code == KeyCode.SPACE) {
					ship.shoot();
				}
				// Logging movements
				System.out.println(code + " key pressed.");
				

			}
		});
		
		this.scene.setOnKeyReleased(new EventHandler<KeyEvent>() {
			public void handle(KeyEvent e) {
				// Stopping movements
				ship.setdX(0);
				ship.setdY(0);
			}
		});
	}
	
	// Update method
	private void update(long now) {
		// updating time
		this.inGameTime = (int) (System.currentTimeMillis() - this.gameStart) / 1000;
		
		// moving sprites
		this.ship.move();
		for (Bullet b : this.ship.getBulletList()) {
			b.move();
		}
		for (Fish f : this.fishes) {
			f.move();
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
			this.gs.setGameOver();
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
			int x = r.nextInt(191) + 540;
			int y = r.nextInt(551) + 30;
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
					this.gs.setGameOver();
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
}
