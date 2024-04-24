package sprites;

import javafx.scene.image.Image;

public class Bullet extends Sprite {
	public final static Image BULLET = new Image("assets/pokeball.png");
	public final static int BULLET_SPEED = 20;
	
	private Fubuchan ship; 	//Instance Attributes
	private int damage;
	
	public Bullet(int positionX, int positionY, Fubuchan ship) { //Constructor
		super(positionX, positionY);
		this.loadImage(Bullet.BULLET);
		this.ship = ship;
		this.damage = this.ship.getStrength();
	}
	
	public void move() {
		this.setdX();
		this.positionX += this.dX;
	}
	
	public void doDamage(Fish f) {
		f.setHealth(f.getHealth() - this.damage);
		this.ship.setScore(this.ship.getScore() + 1);
	}
	
	//Getters
	public int getPositionX() {
		return (int) this.positionX;
	}
	
	//Setters
	private void setdX() {
		this.dX = Bullet.BULLET_SPEED;
	}
}
