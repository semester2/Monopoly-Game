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

    /**
     * Computes the rent of the current placement.
     *
     * @param player
     * @return computedRent
     *
     * @author Oliver Køppen
     */
    @Override
    public int computeRent(Player player){
        int computedRent;
        int placement = getUtilityPlacement(player);
        switch (placement){
            case 1 : computedRent = computeRentBrewery(player); break;
            case 2 : computedRent = computerRentShipyard(player); break;
            default : computedRent = super.getRent();
        }
        return computedRent;
    }

	public int computeRentBrewery(Player player){
	    int initialRent = super.getRent();
	    int color = super.getColorCode();
        return -1;
	}

    public int computerRentShipyard(Player player){
        return -1;
    }

    private int utilitiesOwnedCount(Player player){
        int utilitiesOwned;
        int placement = getUtilityPlacement(player);
        Player owner = player.getCurrentPosition().getIndex()
        switch (placement) {
            case 1 :
        }

        return utilitiesOwned;
    }

    /**
     * Determines if the player is on a brewery or shipyard
     *
     * @param player
     * @return placement
     *
     * @author Oliver Køppen
     */
    private int getUtilityPlacement(Player player){
        int x = player.getCurrentPosition().getIndex();
        int temp;
        switch (x) {
            case 12 : temp = 1; break;
            case 28 : temp = 1; break;
            case 5 : temp = 2; break;
            case 15 : temp = 2; break;
            case 25 : temp = 2; break;
            case 35 : temp = 2; break;
            default : temp = -1;
        }
        return temp;
    }

}
