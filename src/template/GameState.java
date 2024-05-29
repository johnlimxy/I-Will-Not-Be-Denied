package template;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class GameState {
	/**
	 * This is a map(key-value pair) of <player name,NetPlayer>
	 */
	private Map players=new HashMap();
	/**
	 * Simple constructor
	 *
	 */
	public GameState(){}
	
	/**
	 * Update the game state. Called when player moves
	 * @param name
	 * @param player
	 */
	public void update(String name, NetPlayer player){
		players.put(name,player);
	}
	
	/**
	 * String representation of this object. Used for data transfer
	 * over the network
	 */
	public String toString(){
		String retval="";
		for(Iterator ite=players.keySet().iterator();ite.hasNext();){
			String name=(String)ite.next();
			NetPlayer player=(NetPlayer)players.get(name);
			retval+=player.toString()+":";
		}
		return retval;
	}

	
	/**
	 * Returns the map
	 * @return
	 */
	public Map getPlayers(){
		return players;
	}

}
