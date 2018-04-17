package dk.dtu.compute.se.pisd.monopoly.mini.model.properties;

import java.util.Set;

import dk.dtu.compute.se.pisd.monopoly.mini.GameController;
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
	protected int computeRent(Player player, GameController controller) {
		int rent = super.getRent();
		boolean ownsAll = controller.checkWetherPlayerOwnAllInColor(player, super.getColorCode());
		if (ownsAll) {
			rent = rent * 2;
		}

		return rent;
	}
}
