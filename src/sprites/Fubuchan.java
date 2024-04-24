package sprites;

import java.util.ArrayList;
import java.util.Random;

import javafx.scene.image.Image;
import template.GameStage;

public class Fubuchan extends Sprite { //Equivalent to ship
	public final static Image SHIP = new Image("assets/fubuki.png");
	public final static int SHIP_SPEED = 10;
	
	private int strength; //Instance attributes
	private boolean alive; 
	private ArrayList<Bullet> bullets;
	private int score;
	private boolean invincible;
	
	public Fubuchan(int positionX, int positionY) {	//Constructor
		super(positionX, positionY);
		Random r = new Random();
		this.strength = r.nextInt(51) + 100;
		this.alive = true;
		this.loadImage(Fubuchan.SHIP);
		this.bullets = new ArrayList<Bullet>();
		this.score = 0;
		this.invincible = false;
	}
	
	public void move() {
		// Restricting movement outside of the screen
		if (this.positionX > GameStage.WINDOW_WIDTH - Fubuchan.SHIP.getWidth()) {
			this.positionX = GameStage.WINDOW_WIDTH - Fubuchan.SHIP.getWidth();
		} else if (this.positionY > GameStage.WINDOW_HEIGHT - Fubuchan.SHIP.getHeight()) {
			this.positionY = GameStage.WINDOW_HEIGHT - Fubuchan.SHIP.getHeight();
		} else if (this.positionX < 0) { 
			this.positionX = 0;
		} else if (this.positionY < 0) {
			this.positionY = 0;
		} else {
			this.positionX += this.dX;
			this.positionY += this.dY;
		}
	}
	
	public void stop() {
		this.dX = 0;
		this.dY = 0;
	}
	
	public void shoot() {
		// getting position
		int x = (int) (this.positionX + Fubuchan.SHIP.getWidth() - 10);
		int y = (int) (this.positionY + Fubuchan.SHIP.getHeight()/3);
		
		this.bullets.add(new Bullet(x, y, this));
	}
	
	private void checkStrength() {
		if (this.strength <= 0) {
			this.alive = false;
		}
	}
	
	//Getters
	public ArrayList<Bullet> getBulletList() {
		return this.bullets;
	}
	
	public boolean isAlive() {
		this.checkStrength();
		return this.alive;
	}
	
	public int getStrength() {
		return this.strength;
	}
	
	public int getScore() {
		return this.score;
	}

	public boolean isInvincible() {
		return this.invincible;
	}
	
	//Setters
	void setStrength(int strength) {
		this.strength = strength;
	}
	
	void setScore(int score) {
		this.score = score;
	}
	
	void setInvincible(boolean invincible) {
		this.invincible = invincible;
	}
}
