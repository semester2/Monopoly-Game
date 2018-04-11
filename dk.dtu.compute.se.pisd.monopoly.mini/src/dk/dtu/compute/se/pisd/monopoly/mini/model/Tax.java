package dk.dtu.compute.se.pisd.monopoly.mini.model;

import dk.dtu.compute.se.pisd.monopoly.mini.GameController;
import dk.dtu.compute.se.pisd.monopoly.mini.model.exceptions.PlayerBrokeException;

/**
 * Represents a space, where the player has to pay tax.
 * 
 * @author Ekkart Kindler, ekki@dtu.dk
 *
 */
public class Tax extends Space {
	
	@Override
	public void doAction(GameController controller, Player player) throws PlayerBrokeException {
		// TODO note that tax concerns all assets an not just cash
		if (player.getPayTaxInCash()) {
			controller.paymentToBank(player, 4000);
		}
		else {
			controller.paymentToBank(player, player.getTotalWorth(player) / 10);
		}
	}

}
