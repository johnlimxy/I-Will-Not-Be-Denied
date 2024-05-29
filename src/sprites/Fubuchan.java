package sprites;

import java.util.ArrayList;
import java.util.Random;
import java.net.InetAddress;

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
	// private ArrayList<Bullet> bullets;
	private int ammo;
	private int score;
	private boolean invincible;
	private boolean isReloading;
	private int port;
	private InetAddress address;
	private String username;
	private double mouseX, mouseY;
	
	private char orientation;
	
	public Fubuchan(double positionX, double positionY, String username,InetAddress address, int port) {	//Constructor
		super(positionX, positionY);
		// Random r = new Random();
		// this.strength = r.nextInt(51) + 100;
		// this.alive = true;
		// // this.loadImage(Fubuchan.IDLE_SHIP);
		// this.bullets = new ArrayList<Bullet>();
		// this.score = 0;
		// this.invincible = false;
		// this.orientation = 'E';
		// this.ammo = 10;
		this.address = address;
		this.port = port;
		this.username = username;
	}
	
	// public void move() {
	// 	// Restricting movement outside of the screen
	// 	if(this.positionX+ this.dX < GameStage.WINDOW_WIDTH && this.positionX + this.dX > 0 && this.positionY +this.dY <GameStage.WINDOW_HEIGHT && this.positionY + this.dY > 0 ){
	// 		this.positionX += this.dX;
	// 		this.positionY += this.dY;
	// 	}
	// }
	
	public void stop() {
		this.dX = 0;
		this.dY = 0;
	}
	
	public void setReloading(boolean reloading) {
		isReloading = reloading;
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
	
	// public boolean isAlive() {
	// 	this.checkStrength();
	// 	return this.alive;
	// }
	
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

	public int getScore(int score){
		return this.score;
	}

	public int getAmmo(){
		return this.ammo;
	}

	public boolean isReloading(){
		return this.isReloading;
	}

	/**
	 * Returns the port number
	 * @return
	 */
	public int getPort(){
		return port;
	}
	
	/**
	 * Returns the name of the player
	 * @return
	 */
	public String getName(){
		return username;
	}
	
	//Setters
	void setStrength(int strength) {
		this.strength = strength;
	}

	public void setAmmo() {
		this.ammo = 10;
	}

	public void setMouseX(double mouseX){
		this.mouseX = mouseX;
	}

	public void setMouseY(double mouseY){
		this.mouseY = mouseY;
	}

	public void setAlive(boolean alive) {
		this.alive = alive;
	}
	
	void setScore(int score) {
		this.score = score;
	}
	
	void setInvincible(boolean invincible) {
		this.invincible = invincible;
	}
	
	public void setOrientation(char orientation) {
		// if (orientation == 'W') {
		// 	this.loadImage(Fubuchan.REV_SHIP);
		// 	this.orientation = 'W';
		// } else if (orientation == 'E'){
		// 	this.loadImage(Fubuchan.SHIP);
		// 	this.orientation = 'E';
		// } else {
		// 	if (this.orientation == 'W') {
		// 		this.loadImage(Fubuchan.IDLE_REV_SHIP);
		// 	}else {
		// 		this.loadImage(Fubuchan.IDLE_SHIP);
		// 	}
		// }

		this.orientation = orientation;
	}

	/**
	 * Returns the address
	 * @return
	 */
	public InetAddress getAddress(){
		return address;
	}

	public boolean isAlive(){
		return this.alive;
	}

	public String toString(){
		String retval="";
		retval+="PLAYER ";
		retval+=username+" ";
		retval+=x+" ";
		retval+=y+" ";
		retval
		retval+=orientation+" ";
		retval+=ammo+" ";
		retval+=strength+" ";
		retval+=alive+" ";

		return retval;
	}
	/**
	 * String representation. used for transfer over the network
	 */
	public String messageToString(){
		String retval="";
		retval+="MESSAGE ";
		retval+=username+" ";
		retval+=message;
		return retval;
	}
}

	
	
	
