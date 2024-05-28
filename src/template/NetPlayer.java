package template;

import java.net.InetAddress;

public class NetPlayer {
	
	private InetAddress address;
	private int port;
	private String username;
	private int x,y;
	private String message;
	
	public NetPlayer(String username,InetAddress address, int port){
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
	 * Sets the X coordinate of the player
	 * @param x
	 */
	public void setX(int x){
		this.x=x;
	}
	
	
	/**
	 * Returns the X coordinate of the player
	 * @return
	 */
	public int getX(){
		return x;
	}
	
	
	/**
	 * Returns the y coordinate of the player
	 * @return
	 */
	public int getY(){
		return y;
	}
	
	/**
	 * Sets the y coordinate of the player
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
		retval+="PLAYER ";
		retval+=username+" ";
		retval+=x+" ";
		retval+=y;
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
