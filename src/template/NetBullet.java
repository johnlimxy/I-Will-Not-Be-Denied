package template;

import java.net.InetAddress;

public class NetBullet {
	
	private InetAddress address;
	private int port;
	private String username;
	private int x,y;
	private String message;
	
	public NetBullet(String username,InetAddress address, int port){
		this.address = address;
		this.port = port;
		this.username = username;
	}

	/**
	 * Returns the address
	 * @return
	 */
	public InetAddress getAddress(){
		return address;
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
	
	/**
	 * Sets the X coordinate of the bullet
	 * @param x
	 */
	public void setX(int x){
		this.x=x;
	}
	
	
	/**
	 * Returns the X coordinate of the bullet
	 * @return
	 */
	public int getX(){
		return x;
	}
	
	
	/**
	 * Returns the y coordinate of the bullet
	 * @return
	 */
	public int getY(){
		return y;
	}
	
	/**
	 * Sets the y coordinate of the bullet
	 * @param y
	 */
	public void setY(int y){
		this.y=y;		
	}

	/**
	 * String representation. used for transfer over the network
	 */
	public String toString(){
		String retval="";
		retval+="BULLET ";
		retval+=username+" ";
		retval+=x+" ";
		retval+=y;
		return retval;
	}
	
}
