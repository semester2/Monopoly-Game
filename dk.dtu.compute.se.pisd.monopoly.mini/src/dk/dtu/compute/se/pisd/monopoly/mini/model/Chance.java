package dk.dtu.compute.se.pisd.monopoly.mini.model;

import dk.dtu.compute.se.pisd.monopoly.mini.GameController;
import dk.dtu.compute.se.pisd.monopoly.mini.model.exceptions.PlayerBrokeException;

/**
 * Represents a space, where the user has to draw a chance card.
 * 
 * @author Ekkart Kindler, ekki@dtu.dk
 *
 */
public class Chance extends Space {

	private String color;

	/**
	 * Constructor for Chance objects
	 * @param id
	 * @param name
	 * @param color
	 * @param colorCode
	 *
	 * @author Jaafar Mahdi
	 */
	public Chance(int id, String name, String color, int colorCode) {
		super.setIndex(id);
		super.setName(name);
		super.setColorCode(colorCode);
		this.color = color;
	}

	@Override
	public void doAction(GameController controller, Player player) throws PlayerBrokeException {
		controller.takeChanceCard(player);
	}

}
