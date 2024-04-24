package sprites;

public class Timer extends Thread {
	// Instance Attributes
	private PowerUp powerUp;
	private int duration;
	
	// Constructor
	Timer(PowerUp powerUp) {
		this.powerUp = powerUp;
		this.duration = PowerUp.POWERUP_DURATION;
	}
	
	// Methods	
	private void countdown() {
		while (this.duration != 0) {
				try {
					Thread.sleep(1000);
					this.duration--;
				} catch (InterruptedException e) {
					System.out.println(e.getMessage());
				}
		}
		this.powerUp.expire();
	}
	
	@Override
	public void run() {
		this.countdown();
	}
}
