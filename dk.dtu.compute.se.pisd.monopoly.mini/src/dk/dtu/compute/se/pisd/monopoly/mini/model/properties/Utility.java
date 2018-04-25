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
     * Calculates
     *
     * @param player
     * @param controller
     * @return computedRent
     *
     * @author Oliver Køppen
     */
    @Override
    public int computeRent(Player player, GameController controller){
        int computedRent;
        int colour = this.getColorCode();
        switch (colour){
            case 1 : computedRent = computeRentBrewery(controller); break;
            case 2 : computedRent = computeRentShipyard(controller); break;
            default : computedRent = super.getRent();
        }
        return computedRent;
    }

    /**
     * Computes brewery rent as a multiply of number of owned utilies
     *
     * @param controller
     * @return computedRent
     *
     * @author Oliver Køppen
     */
	public int computeRentBrewery(GameController controller){
	    int owned = controller.checkNumberOfOwnedUtilities(getOwner(),this.getColorCode());
        return owned*100*controller.getDiceValue();
	}

    /**
     * Computes shipyard rent in relation to number of owned utilies
     *
     * @param controller
     * @return rent
     *
     * @author Oliver Køppen
     */
    public int computeRentShipyard(GameController controller){
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

}
