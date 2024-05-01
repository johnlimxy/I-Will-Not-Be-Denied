package sprites;

import javafx.scene.image.Image;
import java.lang.Math;

public class Bullet extends Sprite {
	public final static Image BULLET = new Image("assets/pokeball.png");
	public final static int BULLET_SPEED = 5;
	
	private Fubuchan ship; 	//Instance Attributes
	private int damage;
	private double s;
	private double m;
	private char orientation;
	
	public Bullet(int positionX, int positionY, double m, char orientation, Fubuchan ship) { //Constructor
		super(positionX, positionY);
		this.loadImage(Bullet.BULLET);
		this.ship = ship;
		this.m = m;
		this.s = Bullet.BULLET_SPEED/Math.sqrt(1 + (m*m));
		this.damage = this.ship.getStrength();
		this.orientation = orientation;
		
		this.setdX();
		this.setdY();
	}
	
	public void move() {
		if (orientation == 'W') {
			this.positionX -= this.dX;
			this.positionY -= this.dY;
		} else {
			this.positionX += this.dX;
			this.positionY += this.dY;
		}
		
	}
	
	public void doDamage(Fish f) {
		f.setHealth(f.getHealth() - this.damage);
		this.ship.setScore(this.ship.getScore() + 1);
	}
	
	//Getters
	public int getPositionX() {
		return (int) this.positionX;
	}
	
	public int getPositionY() {
		return (int) this.positionY;
	}
	
	//Setters
	private void setdX() {
		this.dX = this.s;
	}
	
	private void setdY() {
		this.dY = this.m * this.s;
	}
}
