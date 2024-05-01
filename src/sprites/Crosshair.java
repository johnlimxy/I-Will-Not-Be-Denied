package sprites;

import javafx.scene.image.Image;
import template.GameStage;

public class Crosshair extends Sprite{
	public final static Image CROSSHAIR = new Image("assets/crosshair.png");
	
	public Crosshair(double positionX, double positionY) { //Constructor
		super(positionX, positionY);
		this.loadImage(Crosshair.CROSSHAIR);
	}
	
	public void move() {
		if (this.dX >= 0 && this.dX <= GameStage.WINDOW_WIDTH) {
			this.positionX = this.dX;
		}
		
		if (this.dY >= 0 && this.dY <= GameStage.WINDOW_HEIGHT) {
			this.positionY = this.dY;
		}
	}
	
	// getter
	
	public int getPositionX() {
		return (int) this.positionX;
	}
	
	public int getPositionY() {
		return (int) this.positionY;
	}
	
	// setter
	
	public void setdXdY(double dX, double dY) {
		this.dX = dX;
		this.dY = dY;
	}
	

}
