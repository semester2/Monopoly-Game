package dk.dtu.compute.se.pisd.monopoly.mini.model;

import dk.dtu.compute.se.pisd.monopoly.mini.GameController;
import dk.dtu.compute.se.pisd.monopoly.mini.dal.SQLMethods;
import dk.dtu.compute.se.pisd.monopoly.mini.model.exceptions.PlayerBrokeException;

import java.sql.SQLException;

/**
 * A property which is a space that can be owned by a player.
 * 
 * @author Ekkart Kindler, ekki@dtu.dk
 *
 */
public class Property extends Space {
	
	private int cost;
	private int rent;
	private boolean mortgaged;
	private boolean developed;
	private Player owner;

	private SQLMethods sqlMethods = new SQLMethods();

	/**
	 * Returns the cost of this property.
	 * 
	 * @return the cost of this property
	 */
	public int getCost() {
		return cost;
	}

	/**
	 * Sets the cost of this property.
	 * 
	 * @param cost the new cost of this property
	 */
	public void setCost(int cost) {
		this.cost = cost;
		notifyChange();
	}

	/**
	 * Returns the rent to be payed for this property.
	 * 
	 * @return the rent for this property
	 */
	public int getRent() {
		return rent;
	}

	/**
	 * Sets the rent for this property.
	 * 
	 * @param rent the new rent for this property
	 */
	public void setRent(int rent) {
		this.rent = rent;
		notifyChange();
	}

	/**
	 * Returns the owner of this property. The value is <code>null</code>,
	 * if the property currently does not have an owner.
	 * 
	 * @return the owner of the property or <code>null</code>
	 */
	public Player getOwner() {
		return owner;
	}

	/**
	 * Sets the owner of this property  to the given owner (which can be 
	 * <code>null</code>).
	 * 
	 * @param player the new owner of the property
	 */
	public void setOwner(Player player) {
		if (this.getOwner() == null) {
			try {
				sqlMethods.setPropertyOwner(Game.gameID, this.getIndex(), player.getPlayerID());
			} catch (SQLException e) {
				System.out.println(e);
			}
		} else {
			try {
				sqlMethods.updatePropertyOwner(Game.gameID, this.getIndex(), player.getPlayerID());
			} catch (SQLException e) {
				System.out.println(e);
			}
		}
		this.owner = player;
		player.addOwnedProperty(this);
		notifyChange();
	}
	
	/**
	 * Returns whether the property is mortaged or not.
	 * 
	 * @return boolean <code>null</code>
	 * 
	 * @author Andreas and Jaafar
	 */
	public boolean getIsMortgaged() {
		return this.mortgaged;
	}

	/**
	 * Sets whether the property is mortgaged or not
	 * 
	 * @param b
	 * 
	 * @author Andreas and Jaafar
	 */
	public void setIsMortgaged(boolean b) {
		this.mortgaged = b;

		try {
			sqlMethods.mortgageProperty(Game.gameID, this.getIndex(), b);
		} catch (SQLException e) {
			System.out.println(e);
		}

		notifyChange();
	}
	
	/**
	 * Returns whether the property is developed or not.
	 * 
	 * @return boolean <code>null</code>
	 * 
	 * @author Andreas and Jaafar
	 */
	public boolean getIsDeveloped() {
		return this.developed;
	}

	/**
	 * Sets whether the property is developed or not
	 * 
	 * @param b
	 * 
	 * @author Andreas and Jaafar
	 */
	public void setIsDeveloped(boolean b) {
		this.developed = b;

		try {
			sqlMethods.mortgageProperty(Game.gameID, this.getIndex(), b);
		} catch (SQLException e) {
			System.out.println(e);
		}

		notifyChange();
	}

	@Override
	public void doAction(GameController controller, Player player) throws PlayerBrokeException {
		if (owner == null) {
			controller.offerToBuy(this, player);
		} else if (!owner.equals(player)) {
			
			if (this.mortgaged) {
				return;
			} else {
				controller.payment(player, computeRent(owner, controller), owner);
			}
		}
	}

	@Override
	public boolean getBuyable() {
		return true;
	}
	
	/**
	 * Computes the rent of the player, that lands on a space, 
	 * that is owned by another player.
	 * @param player
	 * @return the rent to pay as int
	 * @author Andreas and Jaafar
	 */
	protected int computeRent(Player player, GameController controller) {
		return this.rent;
	}

	protected void mortgageProperty(Player player) {
		
	}
}
