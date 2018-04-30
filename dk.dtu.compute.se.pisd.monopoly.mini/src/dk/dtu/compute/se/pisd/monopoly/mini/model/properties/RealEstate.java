package dk.dtu.compute.se.pisd.monopoly.mini.model.properties;

import java.util.ArrayList;
import java.util.List;
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
public class RealEstate extends Property implements Comparable<RealEstate> {

	private String color;
	private int housePrice;
	private List<Integer> rentRate = new ArrayList<>();

	public RealEstate(int id, String name, String color, int colorCode, int cost, int house0, int house1, int house2, int house3, int house4, int house5, int housePrice) {
		super.setCost(cost);
		super.setColorCode(colorCode);
		super.setIndex(id);
		super.setName(name);
		super.setRent(house0);
		this.color = color;
		this.housePrice = housePrice;
		this.rentRate.add(house0);
		this.rentRate.add(house1);
		this.rentRate.add(house2);
		this.rentRate.add(house3);
		this.rentRate.add(house4);
		this.rentRate.add(house5);
	}

	@Override
	protected int computeRent(Player player, GameController controller) {
		int rent = super.getRent();
		boolean ownsAll = controller.checkWetherPlayerOwnAllInColor(player, this.getColorCode());

		if (ownsAll && this.getNumberOfHouses() == 0) {
			rent = rent * 2;
		} else if (ownsAll && this.getNumberOfHouses() > 0) {
			rent = this.rentRate.get(this.getNumberOfHouses() - 1);
		}

		return rent;
	}

	public int getHousePrice() {
		return this.housePrice;
	}

	@Override
	public int compareTo(RealEstate o) {
		return Integer.compare(this.getNumberOfHouses(), o.getNumberOfHouses());
	}
}
