package sprites;

import java.util.ArrayList;
import java.util.Random;

import javafx.scene.image.Image;
import template.GameStage;

public class Fubuchan extends Sprite { //Equivalent to ship
	public final static Image SHIP = new Image("assets/fubuki.gif");
	public final static Image REV_SHIP = new Image("assets/fubuki_rev.gif");
	public final static Image IDLE_SHIP = new Image("assets/idle1.png");
	public final static Image IDLE_REV_SHIP = new Image("assets/idle1_rev.png");
	public final static int SHIP_SPEED = 2;
	
	private int strength; //Instance attributes
	private boolean alive; 
	private ArrayList<Bullet> bullets;
	private int ammo;
	private int score;
	private boolean invincible;
	
	private char orientation;
	
	public Fubuchan(double positionX, double positionY) {	//Constructor
		super(positionX, positionY);
		Random r = new Random();
		this.strength = r.nextInt(51) + 100;
		this.alive = true;
		this.loadImage(Fubuchan.IDLE_SHIP);
		this.bullets = new ArrayList<Bullet>();
		this.score = 0;
		this.invincible = false;
		this.orientation = 'E';
		this.ammo = 20;
	}
	
	public void move() {
		// Restricting movement outside of the screen
		if(this.positionX+ this.dX < GameStage.WINDOW_WIDTH && this.positionX + this.dX > 0 && this.positionY +this.dY <GameStage.WINDOW_HEIGHT && this.positionY + this.dY > 0 ){
			this.positionX += this.dX;
			this.positionY += this.dY;
		}
	}
	
	public void stop() {
		this.dX = 0;
		this.dY = 0;
	}
	
	public void reload() {
		this.ammo = 10;
	}
	
	public void shoot(double mouseX, double mouseY) {
		// getting position
		if (this.ammo > 0) {
			int x = (int) (this.positionX + Fubuchan.SHIP.getWidth() - 10);
			int y = (int) (this.positionY + Fubuchan.SHIP.getHeight()/3);
			
			double m = (mouseY - y)/(mouseX - x);
			
			char orientation;
			
			if (mouseX > x) {
				orientation = 'E';
			}else {
				orientation = 'W';
			}
			
			this.bullets.add(new Bullet(x, y, m, orientation, this));
			
			this.ammo -=1;
		}
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
	
	public double getX() {
		return this.positionX;
	}

	public double getY() {
		return this.positionY;
	}
	
	public boolean isInvincible() {
		return this.invincible;
	}
	
	public char getOrientation() {
		return this.orientation;
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
	
	public void setOrientation(char orientation) {
		if (orientation == 'W') {
			this.loadImage(Fubuchan.REV_SHIP);
			this.orientation = 'W';
		} else if (orientation == 'E'){
			this.loadImage(Fubuchan.SHIP);
			this.orientation = 'E';
		} else {
			if (this.orientation == 'W') {
				this.loadImage(Fubuchan.IDLE_REV_SHIP);
			}else {
				this.loadImage(Fubuchan.IDLE_SHIP);
			}
		}
	}
}