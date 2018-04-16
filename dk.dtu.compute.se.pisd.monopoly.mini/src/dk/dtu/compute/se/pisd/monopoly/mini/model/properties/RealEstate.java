package dk.dtu.compute.se.pisd.monopoly.mini.model.properties;

import java.util.Set;

import dk.dtu.compute.se.pisd.monopoly.mini.model.Player;
import dk.dtu.compute.se.pisd.monopoly.mini.model.Property;

/**
 * A specific property, which represents real estate on which houses
 * and hotels can be built. Note that has not details yet and needs
 * to be implemented.
 * 
 * @author Ekkart Kindler, ekki@dtu.dk
 *
 */
public class RealEstate extends Property{
	
	
	@Override
	protected int computeRent(Player player) {
		int initialRent = super.getRent();
		int color = super.getColorCode();
		int houses = super.getNumberOfHouses();
		boolean ownAll = false;
		boolean twoSpaceColor = false;

		int ownedSameColor = getOwnedSameColor(player, color);
		ownAll = getOwnAll(twoSpaceColor, ownedSameColor, color);
		
		if (ownAll) {
			initialRent = initialRent * 2;
		}
		
		return calculateRent(initialRent, houses);
	}
	
	/**
	 * 
	 * @param player
	 * @param color
	 * @return returns int with the number of properties that
	 * is owned by the player, for that specific color code.
	 * 
	 * @author Andreas and Jaafar
	 */
	public int getOwnedSameColor(Player player, int color) {
		int ownedSameColor = 0;
		Set<Property> propertyList = player.getOwnedProperties();
		for (Property property: propertyList) {
			int propertyColor = property.getColorCode();
			if (propertyColor == color) {
				ownedSameColor++;
			}
		}
		return ownedSameColor;
	}
	
	/**
	 * 
	 * @param twoSpaceColor
	 * @param ownedSameColor
	 * @param color
	 * @return returns boolean of whether the player own all of the properties
	 * for that specific color code.
	 * 
	 * @author Andreas and Jaafar
	 */
	public boolean getOwnAll(boolean twoSpaceColor, int ownedSameColor, int color) {
		boolean ownAll = false;
		if (color == super.TWO_SPACE_COLOR_1 || color == super.TWO_SPACE_COLOR_2) {
			twoSpaceColor = true;
		}
		
		if (twoSpaceColor && ownedSameColor == 1) {
			ownAll = true;
		} else if (!twoSpaceColor && ownedSameColor == 2) {
			ownAll = true;
		}
		
		return ownAll;
	}
	
	/**
	 * 
	 * @param initialRent
	 * @param houses
	 * @return returns the rent for players landing
	 * on the real estate
	 * 
	 * @author Andreas and Jaafar
	 */
	public int calculateRent(int initialRent, int houses) {
		switch (houses) {
			case 1: initialRent = initialRent * 2;
				break;
			case 2: initialRent = initialRent * 3;
				break;
			case 3: initialRent = initialRent * 4;
				break;
			case 4: initialRent = initialRent * 5;
				break;
			case 5: initialRent = initialRent * 6;
				break;
			default: break;
		}
		
		return initialRent;
	}
	
	
	
}
