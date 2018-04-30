package dk.dtu.compute.se.pisd.monopoly.mini.model;

import dk.dtu.compute.se.pisd.monopoly.mini.GameController;
import dk.dtu.compute.se.pisd.monopoly.mini.model.exceptions.PlayerBrokeException;
import gui_main.GUI;

/**
 * Represents a space, where the player has to pay tax.
 * Player gets a choice to pay either 10% tax or $2000
 * @author Ekkart Kindler, ekki@dtu.dk
 * @author Oliver Køppen TOTAL EDIT
 *
 */
public class Tax extends Space {

    private GUI gui;
    private String color;

    /**
     * Constructor for Tax
     *
     * @param id
     * @param name
     * @param color
     * @param colorCode
     *
     * @author Jaafar Mahdi
     */
    public Tax(int id, String name, String color, int colorCode) {
        super.setIndex(id);
        super.setName(name);
        super.setColorCode(colorCode);
        this.color = color;
    }

    @Override
    public void doAction(GameController controller, Player player) throws PlayerBrokeException {
        int placement = super.getIndex();
        if (placement == 38){
            controller.paymentToBank(player,2000);
        }
        else {
        	if(!player.getPayTaxInCash()) {
        		controller.paymentToBank(player, player.getTotalWorth(player) / 10);
        	}
        	if(player.getPayTaxInCash()) {
        		controller.paymentToBank(player, 4000);
        	}
        	/*
        String selection = gui.getUserSelection("Pay 10% of total value or $4000?", "Pay 10%", "Pay $4000");
        switch (selection) {
            case "Pay 10%":
                controller.paymentToBank(player, player.getTotalWorth(player) / 10);
                break;
            case "Pay $4000":
                controller.paymentToBank(player, 4000);
                break;
            default:
                controller.paymentToBank(player, 4000);
                break;
            }*/
        }

    }
}
