package dk.dtu.compute.se.pisd.monopoly.mini.model.properties;

import dk.dtu.compute.se.pisd.monopoly.mini.GameController;
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
    public int computeRent(Player player, GameController controller){
        int computedRent;
        int placement = getUtilityPlacement(player);
        switch (placement){
            case 1 : computedRent = computeRentBrewery(player, controller); break;
            case 2 : computedRent = computerRentShipyard(player, controller); break;
            default : computedRent = super.getRent();
        }
        return computedRent;
    }

	public int computeRentBrewery(Player player, GameController controller){
	    int owned = controller.checkNumberOfOwnedUtilities(getOwner(),this.getColorCode());
        return owned*100*3;
	}

    public int computerRentShipyard(Player player, GameController controller){
        int owned = controller.checkNumberOfOwnedUtilities(getOwner(),this.getColorCode());
	    int rent;
        switch (owned){
            case 0 : rent =0; break;
            case 1 : rent = 500; break;
            case 2 : rent = 1000; break;
            case 3 : rent = 2000; break;
            case 4 : rent = 4000; break;
            default: rent = 0;
        }
        return rent;
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
