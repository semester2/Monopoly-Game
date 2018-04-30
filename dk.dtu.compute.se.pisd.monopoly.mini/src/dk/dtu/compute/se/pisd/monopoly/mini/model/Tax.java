package dk.dtu.compute.se.pisd.monopoly.mini.model;

import dk.dtu.compute.se.pisd.monopoly.mini.GameController;
import dk.dtu.compute.se.pisd.monopoly.mini.model.exceptions.PlayerBrokeException;
import gui_main.GUI;

/**
 * Represents a space, where the player has to pay tax.
 * Player gets a choice to pay either 10% tax or $2000.
 * @author Ekkart Kindler, ekki@dtu.dk
 * @author Oliver Køppen TOTAL EDIT
 *
 */
public class Tax extends Space {
    private GUI gui;

    @Override
    public void doAction(GameController controller, Player player) throws PlayerBrokeException {
        String selection = gui.getUserSelection("Pay 10% of total value or $2000?", "Pay 10%", "Pay $2000");
        switch (selection) {
            case "Pay 10%":
                controller.paymentToBank(player, player.getTotalWorth(player) / 10);
                break;
            case "Pay $2000":
                controller.paymentToBank(player, 2000);
                break;
            default:
                controller.paymentToBank(player, 2000);
                break;

        }

    }
}
