package dk.dtu.compute.se.pisd.monopoly.mini.model.properties;

import dk.dtu.compute.se.pisd.monopoly.mini.model.Player;
import dk.dtu.compute.se.pisd.monopoly.mini.model.Property;

/**
 * A specific property, which represents utilities which can
 * not be developed with houses or hotels.
 * 
 * @author Ekkart Kindler, ekki@dtu.dk
 *
 */
public class Utility extends Property {
	
	// TODO to be implemented
	
	@Override
	protected void mortgageProperty(Player player) {
		this.setIsMortgaged(true);
		player.setBalance(player.getBalance()+this.getCost()/2);
	}

}
