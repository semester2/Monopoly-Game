package dk.dtu.compute.se.pisd.monopoly.mini;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import dk.dtu.compute.se.pisd.monopoly.mini.model.Card;
import dk.dtu.compute.se.pisd.monopoly.mini.model.Chance;
import dk.dtu.compute.se.pisd.monopoly.mini.model.Game;
import dk.dtu.compute.se.pisd.monopoly.mini.model.Property;
import dk.dtu.compute.se.pisd.monopoly.mini.model.Space;
import dk.dtu.compute.se.pisd.monopoly.mini.model.Tax;
import dk.dtu.compute.se.pisd.monopoly.mini.model.cards.CardMove;
import dk.dtu.compute.se.pisd.monopoly.mini.model.cards.CardReceiveMoneyFromBank;
import dk.dtu.compute.se.pisd.monopoly.mini.model.cards.PayTax;
import dk.dtu.compute.se.pisd.monopoly.mini.model.database.Connector;
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
		
		Space go = new Space();
		go.setName("Go");
		go.setColorCode(0);
		game.addSpace(go);

		
		Property p = new Property();
		p.setName("R�dovrevej");
		p.setCost(1200);
		p.setRent(50);
		p.setColorCode(1);
		game.addSpace(p);
		
		Chance chance = new Chance();
		chance.setName("Chance");
		game.addSpace(chance);
		
		p = new Property();
		p.setName("Hvidovrevej");
		p.setCost(1200);
		p.setRent(50);
		p.setColorCode(1);
		game.addSpace(p);
		
		Tax t = new Tax();
		t.setName("Pay tax (10% on Cash)");
		t.setTaxable(true);
		game.addSpace(t);

		Utility s = new Utility();
		s.setName("�resund");
		s.setCost(4000);
		s.setRent(500);
		game.addSpace(s);

		p = new Property();
		p.setName("Roskildevej");
		p.setCost(2000);
		p.setRent(100);
		p.setColorCode(2);
		game.addSpace(p);
		
		chance = new Chance();
		chance.setName("Chance");
		game.addSpace(chance);
		
		p = new Property();
		p.setName("Valby Langgade");
		p.setCost(2000);
		p.setRent(100);
		p.setColorCode(2);
		game.addSpace(p);
		
		p = new Property();
		p.setName("All�gade");
		p.setCost(2400);
		p.setRent(150);
		p.setColorCode(2);
		game.addSpace(p);
		
		Space prison = new Space();
		prison.setName("Prison");
		game.addSpace(prison);
		
		p = new Property();
		p.setName("Frederiksberg All�");
		p.setCost(2800);
		p.setRent(200);
		p.setColorCode(3);
		game.addSpace(p);
		
		p = new Property();
		p.setName("Coca-Cola Tapperi");
		p.setCost(3000);
		p.setRent(300);
		p.setColorCode(3);
		game.addSpace(p);
		
		p = new Property();
		p.setName("B�lowsvej");
		p.setCost(2800);
		p.setRent(200);
		p.setColorCode(3);
		game.addSpace(p);
		
		p = new Property();
		p.setName("Gl. Kongevej");
		p.setCost(3200);
		p.setRent(250);
		p.setColorCode(4);
		game.addSpace(p);
		
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

		Connector conn = new Connector();

		ResultSet rs = conn.doQuery("show columns in player");
		while(rs.next()){
			System.out.println(rs.getString("name"));
		}

		Game game = createGame();
		game.shuffleCardDeck();
		
		GameController controller = new GameController(game);
		controller.createPlayers();
		controller.initializeGUI();
		
		controller.play();
	}

}
