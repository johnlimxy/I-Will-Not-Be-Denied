package sprites;

import javafx.scene.image.Image;

public class Iron extends PowerUp {
	private final static Image PEARL = new Image("assets/iron.png"); //Class Attributes
	private final static int PEARL_BOOST = 5;
	
	public Iron(int positionX, int positionY) { //Constructor
		super(positionX, positionY);
		this.loadImage(Iron.PEARL);
	}
	
	@Override
	public void modifyShip(Fubuchan ship) {
		ship.setStrength(ship.getStrength() + Iron.PEARL_BOOST);
	}

}
