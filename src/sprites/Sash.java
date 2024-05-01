package sprites;

import javafx.scene.image.Image;

public class Sash extends PowerUp {
	private final static Image STAR = new Image("assets/sash.png"); 
	final static int DURATION = 5;
	
	private int duration; //Instance Attributes
	
	public Sash(int positionX, int positionY) { //Constructor
		super(positionX, positionY);
		this.loadImage(Sash.STAR);
		this.duration = Sash.DURATION;
	}

	@Override
	public void modifyShip(Fubuchan ship) {
		ship.setInvincible(true);
		System.out.println("Fubuki equipped Focus Sash");
		new Thread() {
			@Override
			public void run() {
				while (duration != 0) {
					try {
						Thread.sleep(1000);
						duration--;
					} catch (InterruptedException e) {
						System.out.println(e.getMessage());
					}
				}
				
				ship.setInvincible(false);
				System.out.println("Fubuki hung on using her Focus Sash!");
			}
		}.start();
	}
}
