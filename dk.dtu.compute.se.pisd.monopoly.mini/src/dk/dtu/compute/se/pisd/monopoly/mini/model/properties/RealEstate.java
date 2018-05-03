package dk.dtu.compute.se.pisd.monopoly.mini.model.properties;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import dk.dtu.compute.se.pisd.monopoly.mini.GameController;
import dk.dtu.compute.se.pisd.monopoly.mini.dal.SQLMethods;
import dk.dtu.compute.se.pisd.monopoly.mini.model.Game;
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
	private int numberOfHouses = 0;
	private List<Integer> rentRate = new ArrayList<>();
	private SQLMethods sqlMethods = new SQLMethods();

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

	/**
	 * Increment the number of houses on a property
	 *
	 * @author Andreas and Jaafar
	 */
	public void incrementHouses() {
		this.numberOfHouses++;

		if (this.numberOfHouses > 0) {
			this.setIsDeveloped(true);

			try {
				sqlMethods.updateNumberOfHouses(Game.gameID, this.getIndex(), this.numberOfHouses);
			} catch (SQLException e) {
				System.out.println(e);
			}

		}
		notifyChange();
	}

	/**
	 * Decrement the number of houses on a property
	 *
	 * @author Jaafar Mahdi
	 */
	public void decrementHouses() {
		this.numberOfHouses--;

		if (this.numberOfHouses == 0) {
			this.setIsDeveloped(false);

			try {
				sqlMethods.updateNumberOfHouses(Game.gameID, this.getIndex(), this.numberOfHouses);
			} catch (SQLException e) {
				System.out.println(e);
			}
		}
		notifyChange();
	}

	public int getNumberOfHouses() {
		return this.numberOfHouses;
	}

	public void setNumberOfHouses(int number) {
		this.numberOfHouses = number;
		if (number > 0) {
			this.setIsDeveloped(true);
		}
		notifyChange();
	}
}
