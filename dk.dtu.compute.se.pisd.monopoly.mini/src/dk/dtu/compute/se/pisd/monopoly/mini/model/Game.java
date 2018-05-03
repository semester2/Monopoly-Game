package dk.dtu.compute.se.pisd.monopoly.mini.model;

import java.awt.*;
import java.util.*;
import java.util.List;

import dk.dtu.compute.se.pisd.designpatterns.Subject;
import dk.dtu.compute.se.pisd.monopoly.mini.model.cards.CardMove;
import dk.dtu.compute.se.pisd.monopoly.mini.model.cards.CardReceiveMoneyFromBank;
import dk.dtu.compute.se.pisd.monopoly.mini.model.cards.PayTax;

/**
 * Represents the top-level element of a Monopoly game's state. In order
 * to use this model with the MVC-pattern, it extends the
 * {@link dk.dtu.compute.se.pisd.designpatterns.Subject} of the observer
 * design pattern.
 *
 * @author Ekkart Kindler, ekki@dtu.dk
 *
 */
public class Game extends Subject {

	public static int gameID;

	private String gameName;

	private List<Space> spaces = new ArrayList<Space>();

	private List<Card> cardDeck = new ArrayList<Card>();

	private List<Player> players = new ArrayList<Player>();

	private List<Property> propertyList = new ArrayList<>();

	private Map<Integer, List<Property>> colorToPropertyMap;

	private Player current;

	private final int MONEY_FROM_PASSING_START = 4000;

	/**
	 * Returns a list of all the games spaces.
	 *
	 * @return an unmodifiable list of the games spaces
	 */
	public List<Space> getSpaces() {
		return Collections.unmodifiableList(spaces);
	}

	/**
	 * Returns a list of all spaces, but it is modifiable.
	 * The purpose is to sort this list.
	 *
	 * @return
	 */
	public List<Space> getModifiableSpaceList() {
		return this.spaces;
	}

	/**
	 * Sets all the spaces of the game. Note that the provided
	 * list of spaces is copied, so that they cannot be changed
	 * without the game being aware of the change.
	 *
	 * @param spaces the list of spaces
	 */
	public void setSpaces(List<Space> spaces) {
		this.spaces = new ArrayList<Space>(spaces);
		notifyChange();
	}

	/**
	 * Adds a space to the game at the end.
	 *
	 * @param space the added space
	 *
	 * @author Modified by Jaafar Mahdi
	 */
	public void addSpace(Space space) {
		spaces.add(space);
		notifyChange();
	}

	/**
	 * Returns a list of the cards in the current deck.
	 *
	 * @return an unmodifiable list of all the cards currently in the deck
	 */
	public List<Card> getCardDeck() {
		return Collections.unmodifiableList(cardDeck);
	}

	/**
	 * Removes the topmost card from the deck and returns it.
	 * Added defensive programming, will never return null
	 * @return the topmost card of the deck
	 * @author Oliver Køppen
	 */
	public Card drawCardFromDeck() throws IndexOutOfBoundsException{
		if (cardDeck.size() == 0){
			setCardDeck(generateNewCardDeck());
		}

		Card card = cardDeck.remove(0);
		notifyChange();
		return card;

	}

	/**
	 * Add the given card to the bottom of the deck.
	 *
	 * @param card the card added to the bottom of the deck.
	 */
	public void returnCardToDeck(Card card) {
		cardDeck.add(card);
		notifyChange();
	}

	/**
	 * Uses the provided list of cards as the new deck. The
	 * list will be copied in order to avoid, changes on it
	 * without the game being aware of it.
	 *
	 * @param cardDeck the new deck of cards
	 */
	public void setCardDeck(List<Card> cardDeck) {
		this.cardDeck = new ArrayList<Card>(cardDeck);
		notifyChange();
	}


	/**
	 * Shuffles the cards in the deck.
	 */
	public void shuffleCardDeck() {
		Collections.shuffle(cardDeck);
		// This notification is probably not needed, but for
		// completeness sake, we have it here
		notifyChange();
	}

	/**
	 * Returns all the players of the game as an unmodifiable list.
	 *
	 * @return a list of the current players
	 */
	public List<Player> getPlayers() {
		return Collections.unmodifiableList(players);
	}

	/**
	 * Returns Modifiable List of players
	 *
	 * @return
	 *
	 * @author Jaafar Mahdi
	 */
	public List<Player> getModifiablePlayerList() {
		return this.players;
	}

	/**
	 * Sets the list of players. The list of players is actually copied
	 * in order to avoid the list being modified without the game being
	 * aware of it.
	 *
	 * @param players the list of players
	 */
	public void setPlayers(List<Player> players) {
		this.players = new ArrayList<Player>(players);
		notifyChange();
	}

	/**
	 * Adds a player to the game.
	 *
	 * @param player the player to be added.
	 */
	public void addPlayer(Player player) {
		players.add(player);
		notifyChange();
	}

	/**
	 * Returns the current player of the game. This is the player
	 * who's turn it is to do the next move (or currently is doing a move).
	 *
	 * @return the current player
	 */
	public Player getCurrentPlayer() {
		if (current == null) {
			current = players.get(0);
		}
		return current;
	}

	/**
	 * Sets the current player. It is required that the player is a
	 * player of the game already; otherwise an IllegalArumentException
	 * will be thrown.
	 *
	 * @param player the new current player
	 */
	public void setCurrentPlayer(Player player) {
		if (player != null && players.contains(player)) {
			current = player;
		} else {
			throw new IllegalArgumentException("Player is not in the game!");
		}
		notifyChange();
	}

	/**
	 *
	 * @return The amount of money you receive from passing start.
	 *
	 * @author Andreas Bennecke
	 */
	public int getMoneyForPassingStart() {
		return this.MONEY_FROM_PASSING_START;
	}

	/**
	 * Setter
	 * @param gameID
	 * @author Jaafar Mahdi
	 */
	public void setGameID(int gameID) {
		this.gameID = gameID;
	}

	/**
	 * Setter
	 * @param gameName
	 * @author Jaafar Mahdi
	 */
	public void setGameName(String gameName) {
		this.gameName = gameName;
	}

	/**
	 * Getter
	 * @return
	 * @author Jaafar Mahdi
	 */
	public int getGameID() {
		return gameID;
	}

	/**
	 * Getter
	 * @return
	 * @author Jaafar Mahdi
	 */
	public String getGameName() {
		return gameName;
	}

	/**
	 * Populates a list of properties, based on all the spaces
	 * in the game.
	 *
	 * @author Jaafar Mahdi
	 */
	public void populatePropertyList() {
		List<Property> propertyList = new ArrayList<>();
		for (Space space: this.getSpaces()) {
			if (space.getBuyable()) {
				propertyList.add((Property) space);
			}
		}

		this.propertyList = propertyList;
		populateMap();
	}

	/**
	 *
	 * @return a list with all properties
	 *
	 * @author Jaafar Mahdi
	 */
	public List<Property> getPropertyList() {
		return this.propertyList;
	}

	/**
	 * Creates a HashMap that maps a connection between a property' colorcode
	 * and an ArrayList, that contains all the specific properties
	 *
	 * @author Jaafar Mahdi
	 */
	private void populateMap() {
		colorToPropertyMap = new HashMap<Integer, List<Property>>();
		for (Property property : propertyList) {
			int color = property.getColorCode();
			List<Property> list = colorToPropertyMap.get(color);
			if (list == null) {
				list = new ArrayList<Property>();
				colorToPropertyMap.put(color,list);
			}
			list.add(property);
		}
	}

	public Map<Integer, List<Property>> getColorToPropertyMap() {
		return this.colorToPropertyMap;
	}

	public List<Card> generateNewCardDeck() {
		List<Card> cardDeck = new ArrayList<>();

		for (int i = 0; i < 5; i++) {
			CardMove move = new CardMove();
			move.setTarget(this.getSpaces().get(9));
			move.setText("Move to All�gade!");
			cardDeck.add(move);

			PayTax tax = new PayTax();
			tax.setText("Pay 10% income tax!");
			cardDeck.add(tax);

			CardReceiveMoneyFromBank b = new CardReceiveMoneyFromBank();
			b.setText("You receive 100$ from the bank.");
			b.setAmount(100);
			cardDeck.add(b);
		}

		Collections.shuffle(cardDeck);
		notifyChange();
		return cardDeck;

	}

	/**
	 * Generates colors that we can give to new players
	 * @return List of colors
	 *
	 * @author Jaafar Mahdi
	 */
	public List<Color> getColorList() {
		List<Color> colors = new ArrayList<>();
		colors.add(Color.RED);
		colors.add(Color.YELLOW);
		colors.add(Color.GREEN);
		colors.add(Color.BLUE);
		colors.add(Color.ORANGE);
		colors.add(Color.BLACK);

		return colors;
	}

}
