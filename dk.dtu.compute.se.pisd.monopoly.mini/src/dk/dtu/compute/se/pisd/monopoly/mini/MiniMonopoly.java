package dk.dtu.compute.se.pisd.monopoly.mini;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.stream.JsonReader;
import dk.dtu.compute.se.pisd.monopoly.mini.model.Card;
import dk.dtu.compute.se.pisd.monopoly.mini.model.Chance;
import dk.dtu.compute.se.pisd.monopoly.mini.model.Game;
import dk.dtu.compute.se.pisd.monopoly.mini.model.Property;
import dk.dtu.compute.se.pisd.monopoly.mini.model.Space;
import dk.dtu.compute.se.pisd.monopoly.mini.model.Tax;
import dk.dtu.compute.se.pisd.monopoly.mini.model.cards.CardMove;
import dk.dtu.compute.se.pisd.monopoly.mini.model.cards.CardReceiveMoneyFromBank;
import dk.dtu.compute.se.pisd.monopoly.mini.model.cards.PayTax;
import dk.dtu.compute.se.pisd.monopoly.mini.model.properties.RealEstate;
import dk.dtu.compute.se.pisd.monopoly.mini.model.properties.Utility;

/**
 * Main class for setting up and running a (Mini-)Monoploy game.
 * 
 * @author Ekkart Kindler, ekki@dtu.dk
 *
 */
public class MiniMonopoly {

	/**
	 * Creates the initial static situation of a Monopoly game. Note
	 * that the players are not created here, and the chance cards
	 * are not shuffled here.
	 * 
	 * @return the initial game board and (not shuffled) deck of chance cards 
	 */
	public static Game createGame() {

		// Create the initial Game set up (note that, in this simple
		// setup, we use only 11 spaces). Note also that this setup
		// could actually be loaded from a file or database instead
		// of creating it programmatically.
		Game game = new Game();

		populateGame(game);

		System.out.println(game.getSpaces().get(1).getIndex());

		List<Card> cards = new ArrayList<Card>();
		
		CardMove move = new CardMove();
		move.setTarget(game.getSpaces().get(9));
		move.setText("Move to All�gade!");
		cards.add(move);
		
		PayTax tax = new PayTax();
		tax.setText("Pay 10% income tax!");
		cards.add(tax);
		
		CardReceiveMoneyFromBank b = new CardReceiveMoneyFromBank();
		b.setText("You receive 100$ from the bank.");
		b.setAmount(100);
		cards.add(b);
		game.setCardDeck(cards);

		game.populatePropertyList();

		return game;
	}

	/**
	 * The main method which creates a game, shuffles the chance
	 * cards, creates players, and then starts the game.
	 * 
	 * @param args not used
	 */
	public static void main(String[] args) throws SQLException {

		Game game = createGame();
		game.shuffleCardDeck();
		
		GameController controller = new GameController(game);
		controller.createPlayers();
		controller.initializeGUI();
		
		controller.play();
	}

	/**
	 * Reads from three different JSONs and populates the game with Spaces
	 *
	 * @param game
	 *
	 * @author Jaafar Mahdi
	 */
	public static void populateGame(Game game) {

		Space go = new Space();
		go.setName("Go");
		go.setColorCode(0);
		game.addSpace(go);

		Gson gson = new Gson();
		FileReader fileReader = null;

		try {
			fileReader = new FileReader("src/resources/prop.json");
			JsonReader reader = gson.newJsonReader(fileReader);
			JsonObject json = gson.fromJson(reader, JsonObject.class);
			JsonArray jsonArray = json.get("data").getAsJsonArray();

			for (int i = 0; i < jsonArray.size(); i++) {
				JsonObject jsonObject = jsonArray.get(i).getAsJsonObject();
				RealEstate realEstate = new RealEstate(
						jsonObject.get("ID").getAsInt(),
						jsonObject.get("Name").getAsString(),
						jsonObject.get("Color").getAsString(),
						jsonObject.get("ColorCode").getAsInt(),
						jsonObject.get("Price").getAsInt(),
						jsonObject.get("House0").getAsInt(),
						jsonObject.get("House1").getAsInt(),
						jsonObject.get("House2").getAsInt(),
						jsonObject.get("House3").getAsInt(),
						jsonObject.get("House4").getAsInt(),
						jsonObject.get("House5").getAsInt(),
						jsonObject.get("BuildHouseCost").getAsInt()
				);

				game.addSpace(realEstate);
			}

			reader.close();
			fileReader = null;
		} catch (IOException e) {
			System.out.println(e);
		}

		try {
			fileReader = new FileReader("src/resources/util.json");
			JsonReader reader = gson.newJsonReader(fileReader);
			JsonObject json = gson.fromJson(reader, JsonObject.class);
			JsonArray jsonArray = json.get("data").getAsJsonArray();

			for (int i = 0; i < jsonArray.size(); i++) {
				JsonObject jsonObject = jsonArray.get(i).getAsJsonObject();
				Utility utility = new Utility(
						jsonObject.get("ID").getAsInt(),
						jsonObject.get("Name").getAsString(),
						jsonObject.get("Color").getAsString(),
						jsonObject.get("ColorCode").getAsInt(),
						jsonObject.get("Price").getAsInt()
				);

				game.addSpace(utility);
			}

			reader.close();
			fileReader = null;
		} catch (IOException e) {
			System.out.println(e);
		}

		try {
			fileReader = new FileReader("src/resources/chance.json");
			JsonReader reader = gson.newJsonReader(fileReader);
			JsonObject json = gson.fromJson(reader, JsonObject.class);
			JsonArray jsonArray = json.get("data").getAsJsonArray();

			for (int i = 0; i < jsonArray.size(); i++) {
				JsonObject jsonObject = jsonArray.get(i).getAsJsonObject();
				Chance chance = new Chance(
						jsonObject.get("ID").getAsInt(),
						jsonObject.get("Name").getAsString(),
						jsonObject.get("Color").getAsString(),
						jsonObject.get("ColorCode").getAsInt()
				);

				game.addSpace(chance);
			}

			reader.close();
			fileReader = null;
		} catch (IOException e) {
			System.out.println(e);
		}

		Collections.sort(game.getModifiableSpaceList(), new Comparator<Space>() {
			@Override
			public int compare(Space o1, Space o2) {
				return Integer.compare(o1.getIndex(), o2.getIndex());
			}
		});
	}

}
