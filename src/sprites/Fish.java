package sprites;

import java.util.Random;

import javafx.scene.image.Image;
import template.GameStage;

public class Fish extends Sprite {
	private final static Image FISH = new Image("assets/magikarp.png");
	private final static Image FISH_REV = new Image("assets/magikarp_rev.png");
	private final static Image BIG_FISH = new Image("assets/magikarp_s.png");
	private final static Image BIG_FISH_REV = new Image("assets/magikarp_s_rev.png");
	private final static int MAX_FISH_SPEED = 5;
	
	private boolean alive; //Instance Attributes
	private boolean toRight;
	private int health;
	private int speed;
	private int damage;
	private int type;
	
	public Fish(int positionX, int positionY, int type) { //Constructor
		super(positionX, positionY);
		this.alive = true;
		this.toRight = false;
		this.type = type;
		if (this.type == 1) {
			this.health = 1;
			this.damage = 30;
			this.loadImage(Fish.FISH);
		} else if (this.type == 2) {
			this.health = 3000;
			this.damage = 50;
			this.loadImage(Fish.BIG_FISH);
		}
		
		// Setting speed
		Random r = new Random();
		this.speed = r.nextInt(Fish.MAX_FISH_SPEED) + 1;
	}
	
	// Methods
	public void checkDirection() {
		if (this.positionX <= 0) {
			this.toRight = true;
			if (this.type == 1) {
				this.loadImage(Fish.FISH_REV);
			} else if (this.type == 2) {
				this.loadImage(Fish.BIG_FISH_REV);
			}
		} else if (this.positionX + this.getImage().getWidth() >= GameStage.WINDOW_WIDTH) {
			this.toRight = false;
			if (this.type == 1) {
				this.loadImage(Fish.FISH);
			} else if (this.type == 2) {
				this.loadImage(Fish.BIG_FISH);
			}
		}
	}
	
	public void move() {
		this.setdX();
		this.checkDirection();
		
		if (!this.toRight) {
			this.positionX += this.dX;
		} else {
			this.positionX += -this.dX;
		}
	}
	
	private void checkHealth() {
		if (this.health < 0) {
			this.alive = false;
		}
	}
	
	public void doDamage(Fubuchan ship) {
		if (!ship.isInvincible()) {
			ship.setStrength(ship.getStrength() - this.damage);
		}
	}
	
	// Getters
	int getHealth() {
		return this.health;
	}
	
	public boolean isAlive() {
		this.checkHealth();
		return this.alive;
	}
	
	public int getType() {
		return this.type;
	}
	
	// Setters
	void setHealth(int health) {
		this.health = health;
	}
	
	// Overriding setdX from Sprite to not take inputs
	private void setdX() {
		this.dX = -this.speed;
	}
}
