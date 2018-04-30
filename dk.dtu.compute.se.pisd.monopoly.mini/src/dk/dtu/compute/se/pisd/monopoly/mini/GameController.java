package dk.dtu.compute.se.pisd.monopoly.mini;

import java.awt.Color;
import java.util.*;

import dk.dtu.compute.se.pisd.monopoly.mini.model.Card;
import dk.dtu.compute.se.pisd.monopoly.mini.model.Game;
import dk.dtu.compute.se.pisd.monopoly.mini.model.Player;
import dk.dtu.compute.se.pisd.monopoly.mini.model.Property;
import dk.dtu.compute.se.pisd.monopoly.mini.model.Space;
import dk.dtu.compute.se.pisd.monopoly.mini.model.exceptions.PlayerBrokeException;
import dk.dtu.compute.se.pisd.monopoly.mini.model.properties.RealEstate;
import gui_main.GUI;

/**
 * The overall controller of a Monopoly game. It provides access
 * to all basic actions and activities for the game. All other
 * activities of the game, should be implemented by referring
 * to the basic actions and activities in this class. This is
 * necessary, since the GUI does not support MVC yet.
 * 
 * Note that this controller is far from being finished and many
 * things could be done in a much nicer and cleaner way! But, it
 * shows the general idea of how the model, view (GUI), and the
 * controller could work with each other, and how different parts
 * of the games activities can be separated from each other, so
 * that different parts can be added and extended independently
 * from each other.
 * 
 * Note that it is crucial that all changes to the
 * {@link dk.dtu.compute.se.pisd.monopoly.mini.model.Game} need to
 * be made through this controller, since the GUI does not
 * follow the MVC pattern. For fully implementing the game, it will
 * be necessary to add more of these basic actions in this class.
 * 
 * The action methods of the
 * {@link dk.dtu.compute.se.pisd.monopoly.mini.model.Space} and
 * the {@link dk.dtu.compute.se.pisd.monopoly.mini.model.Card}
 * can be implemented based on the basic actions and activities
 * of this game controller. Then, the game controller takes care
 * of updating the GUI. 
 * 
 * @author Ekkart Kindler, ekki@dtu.dk
 *
 */
public class GameController {
	
	private Game game;
	
	private GUI gui;
	
	private View view;
	
    private boolean disposed = false;

    private final int JAIL_FIELD = 10;
	private final String OPTION_1 = "Sell to other player";
	private final String OPTION_2 = "Sell house from Real Estate";
	private final String OPTION_3 = "Mortgage Real Estate";

    private int dieOne;
    private int dieTwo;
	
	/**
	 * Constructor for a controller of a game.
	 * 
	 * @param game the game
	 */
	public GameController(Game game) {
		super();
		this.game = game;
		
		gui = new GUI();
	}
	
	/**
	 * This method will be called when the game is started to create
	 * the participating players. Right now, the creation of players
	 * is hard-coded. But this should be done by interacting with 
	 * the user.
	 */
	public void createPlayers() {
		// TODO the players should be created interactively
		Player p = new Player();
		p.setName("Player 1");
		p.setCurrentPosition(game.getSpaces().get(0));
		p.setColor(Color.RED);
		game.addPlayer(p);
		
		p = new Player();
		p.setName("Player 2");
		p.setCurrentPosition(game.getSpaces().get(0));
		p.setColor(Color.YELLOW);
		game.addPlayer(p);
		
		p = new Player();
		p.setName("Player 3");
		p.setCurrentPosition(game.getSpaces().get(0));
		p.setColor(Color.GREEN);
		game.addPlayer(p);
	}
	
	/**
	 * This method will initialize the GUI. It should be called after
	 * the players of the game are created. As of now, the initialization
	 * assumes that the spaces of the game fit to the fields of the GUI;
	 * this could eventually be changed, by creating the GUI fields 
	 * based on the underlying game's spaces (fields).
	 */
	public void initializeGUI() {		
		this. view = new View(game, gui);
	}
	
	/**
	 * The main method to start the game with the given player.
	 * The game is started with the current player of the game;
	 * this makes it possible to resume a game at any point.
	 */
	public void play() {
		List<Player> players =  game.getPlayers();
		Player c = game.getCurrentPlayer();

		int current = 0;
		for (int i=0; i<players.size(); i++) {
			Player p = players.get(i);
			if (c.equals(p)) {
				current = i;
			}
		}

		boolean terminated = false;
		while (!terminated) {
			Player player = players.get(current);

			this.mortgageUserSelection(player);
			this.buyBackMortagedPropertiesUserSelect(player);

			if (!player.isBroke()) {
				try {
					this.makeMove(player);
				} catch (PlayerBrokeException e) {
					// We could react to the player having gone broke
				}
			}

			// Check whether we have a winner
			Player winner = null;
			int countActive = 0;
			for (Player p: players) {
				if (!p.isBroke()) {
					countActive++;
					winner = p;
				}
			}
			if (countActive == 1) {
				gui.showMessage(
						"Player " + winner.getName() +
						" has won with " + winner.getBalance() +"$.");
				break;
			} else if (countActive < 1) {
				// This can actually happen in very rare conditions and only
				// if the last player makes a stupid mistake (like buying something
				// in an auction in the same round when the last but one player went
				// bankrupt)
				gui.showMessage(
						"All players are broke.");
				break;

			}


			String tradeSelection = gui.getUserSelection("Does any players want to trade? ", "no", "yes");
			if(tradeSelection.equals("yes")) {
				this.tradePropertyUserSelection();

				current = (current + 1) % players.size();
				game.setCurrentPlayer(players.get(current));
				if (current == 0) {
					String selection = gui.getUserSelection(
							"A round is finished. Do you want to continue the game?",
							"yes",
							"no");
					if (selection.equals("no")) {
						terminated = true;
					}
				}
			}
			dispose();
		}
	}

	/**
	 * This method implements a activity of asingle move of the given player.
	 * It throws a {@link dk.dtu.compute.se.pisd.monopoly.mini.model.exceptions.PlayerBrokeException}
	 * if the player goes broke in this move. Note that this is still a very
	 * basic implementation of the move of a player; many aspects are still
	 * missing.
	 * 
	 * @param player the player making the move
	 * @throws PlayerBrokeException if the player goes broke during the move
	 *
	 * @author Ekkart Kindler - Modified by Andreas Bennecke & Oliver Køppen
	 */
	public void makeMove(Player player) throws PlayerBrokeException {

		int doublesCount = 0;
		boolean payedToGetOut = false;
		final int PRISON_FEE = 1000;

		if (player.isInPrison()) {
			String playerSelection = gui.getUserSelection("Do you want to pay the fine to escape prison?", "yes", "no");
			if (playerSelection.contains("yes")) {
				player.payMoney(PRISON_FEE);
				player.setInPrison(false);
				payedToGetOut = true;
				gui.showMessage("Player" + player.getName() + " leaves prison as he paid the fee of $1000");
			}
		}

		if (!payedToGetOut) {
			do {
				rollDice();
				gui.setDice(dieOne, dieTwo);

				if (player.isInPrison() && castDouble()) {
					player.setInPrison(false);
					gui.showMessage("Player " + player.getName() + " leaves prison now since he cast a double!");
				} else if (player.isInPrison()) {
					gui.showMessage("Player " + player.getName() + " stays in prison since he did not cast a double!");
				}
				
				if (castDouble()) {
					doublesCount++;
					if (doublesCount > 2) {
						gui.showMessage("Player " + player.getName() + " has cast the third double and goes to jail!");
						gotoJail(player);
						return;
					}
				}

				if (!player.isInPrison()) {
					// make the actual move by computing the new position and then
					// executing the action moving the player to that space
					int pos = player.getCurrentPosition().getIndex();
					List<Space> spaces = game.getSpaces();
					int newPos = (pos + dieOne + dieTwo) % spaces.size();
					Space space = spaces.get(newPos);
					moveToSpace(player, space);
					if (castDouble()) {
						gui.showMessage("Player " + player.getName() + " cast a double and makes another move.");
					}
				}
			} while (castDouble());
		}

	}
	
	/**
	 * This method implements the activity of moving the player to the new position,
	 * including all actions associated with moving the player to the new position.
	 * 
	 * @param player the moved player
	 * @param space the space to which the player moves
	 * @throws PlayerBrokeException when the player goes broke doing the action on that space
	 *
	 * @author Ekkart Kindler - Modified by Andreas Bennecke and Sebastian Bilde
	 */
	public void moveToSpace(Player player, Space space) throws PlayerBrokeException {
		int posOld = player.getCurrentPosition().getIndex();
		player.setCurrentPosition(space);

		if (posOld > player.getCurrentPosition().getIndex()) {
			gui.showMessage("Player " + player.getName() + " receives 4000$ for passing Go!");
			this.paymentFromBank(player, game.getMoneyForPassingStart());
		}		
		gui.showMessage("Player " + player.getName() + " arrives at " + space.getIndex() + ": " +  space.getName() + ".");
		
		// Execute the action associated with the respective space. Note
		// that this is delegated to the field, which implements this action
		if (space.getTaxable()) {
			String selection = gui.getUserSelection("How would you like to pay? ", "4000 kr", "10% of your total value");
			
			if (selection.equals("4000 kr")) {
				player.setPayTaxInCash(true);
			}
			else {
				player.setPayTaxInCash(false);
			}
		}
		space.doAction(this, player);
	}	

	/**
	 * The method implements the action of a player going directly to jail.
	 * 
	 * @param player the player going to jail
	 *
	 * @author Ekkart Kindler - Modified by Andreas Bennecke
	 */
	public void gotoJail(Player player) {
		player.setCurrentPosition(game.getSpaces().get(JAIL_FIELD));
		player.setInPrison(true);
	}
	
	/**
	 * The method implementing the activity of taking a chance card.
	 * 
	 * @param player the player taking a chance card
	 * @throws PlayerBrokeException if the player goes broke by this activity
	 */
	public  void takeChanceCard(Player player) throws PlayerBrokeException{
		Card card = game.drawCardFromDeck();
		gui.displayChanceCard(card.getText());
		gui.showMessage("Player " + player.getName() + " draws a chance card.");

		try {
			card.doAction(this, player);
		} finally {
			gui.displayChanceCard("done");
		}
	}
	
	/**
	 * This method implements the action returning a drawn card to the
	 * bottom of the deck.
	 * 
	 * @param card returned card
	 */
	public void returnChanceCardToDeck(Card card) {
		game.returnCardToDeck(card);
	}
	
	/**
	 * This method implements the activity where a player can obtain
	 * cash by selling houses back to the bank, by mortgaging own properties,
	 * or by selling properties to other players. This is called, whenever
	 * the player does not have enough cash available for an action. If
	 * he does not at least free the given amount of money, he will be broke;
	 * this is to help the player make the right choices to free enough money.
	 *  
	 * @param player the player
	 * @param amount the amount the player should have available after the act
	 */
	public void obtainCash(Player player, int amount) {
		List<String> options = generateObtainCashList(player);

		String selection = gui.getUserSelection("How do you want to obtain cash?", stringListToStringArray(options));

		switch (selection) {
		case OPTION_1:
			this.tradePropertyUserSelection();
			break;
		case OPTION_2:
			this.sellHousesUserSelection(player);
			break;
		case OPTION_3:
			this.mortgageUserSelection(player);
			break;
		default:
			break;
		}
	}
	
	/**
	 * This method implements the activity of offering a player to buy
	 * a property. This is typically triggered by a player arriving on
	 * a property that is not sold yet. If the player chooses not to
	 * buy, the property will be set for auction.
	 * 
	 * @param property the property to be sold
	 * @param player the player the property is offered to
	 * @throws PlayerBrokeException when the player chooses to buy but could not afford it
	 *
	 * @author Ekkart Kindler - Modified by Jaafar Mahdi and Sebastian Bilde
	 */
	public void offerToBuy(Property property, Player player) throws PlayerBrokeException {
		
		if(property.getCost()>player.getBalance()) {
			if(this.generateObtainCashList(player).size()>0) {
				String selection = gui.getUserSelection("You do not have enough money, do you want to obtain cash? ", "yes", "no");
				if(selection.equals("yes")) {
					this.obtainCash(player, property.getCost()-player.getBalance());
				}
			}
		}
		
		String choice = gui.getUserSelection(
				"Player " + player.getName() +
				": Do you want to buy " + property.getName() +
				" for " + property.getCost() + "$?",
				"yes",
				"no");

        if (choice.equals("yes")) {
    		try {
    			paymentToBank(player, property.getCost());
    		} catch (PlayerBrokeException e) {
    			// if the payment fails due to the player being broke,
    			// the an auction (among the other players is started
    			auction(property, player);
    			// then the current move is aborted by casting the
    			// PlayerBrokeException again
    			throw e;
    		}
    		player.addOwnedProperty(property);
    		property.setOwner(player);
    		return;
        }
        
		// In case the player does not buy the property an auction
        // is started
		auction(property, player);
	}
	
	
	/**
	 * This method implements a payment activity to another player,
	 * which involves the player to obtain some cash on the way, in case he does
	 * not have enough cash available to pay right away. If he cannot free
	 * enough money in the process, the player will go bankrupt.
	 * 
	 * @param payer the player making the payment
	 * @param amount the payed amount
	 * @param receiver the beneficiary of the payment
	 * @throws PlayerBrokeException when the payer goes broke by this payment
	 */
	public void payment(Player payer, int amount, Player receiver) throws PlayerBrokeException {
		if (payer.getBalance() < amount) {
			obtainCash(payer, amount);
			if (payer.getBalance() < amount) {
				playerBrokeTo(payer,receiver);
				throw new PlayerBrokeException(payer);
			}
		}
		gui.showMessage("Player " + payer.getName() + " pays " +  amount + "$ to player " + receiver.getName() + ".");
		payer.payMoney(amount);
		receiver.receiveMoney(amount);
	}
	
	/**
	 * This method implements the action of a player receiving money from
	 * the bank.
	 * 
	 * @param player the player receiving the money
	 * @param amount the amount
	 */
	public void paymentFromBank(Player player, int amount) {
		player.receiveMoney(amount);
	}

	/**
	 * This method implements the activity of a player making a payment to
	 * the bank. Note that this might involve the player to obtain some
	 * cash; in case he cannot free enough cash, he will go bankrupt
	 * to the bank. 
	 * 
	 * @param player the player making the payment
	 * @param amount the amount
	 * @throws PlayerBrokeException when the player goes broke by the payment
	 */
	public void paymentToBank(Player player, int amount) throws PlayerBrokeException{
		if (amount > player.getBalance()) {
			obtainCash(player, amount);
			if (amount > player.getBalance()) {
				playerBrokeToBank(player);
				throw new PlayerBrokeException(player);
			}
			
		}
		gui.showMessage("Player " + player.getName() + " pays " +  amount + "$ to the bank.");
		player.payMoney(amount);
	}
	
	/**
	 * This method implements the activity of auctioning a property.
	 * 
	 * @param property the property which is for auction
	 */
	public void auction(Property property, Player rejecter) {
		gui.showMessage("Now, there would be an auction of " + property.getName() + ".");

		Player highestBidder = null;
		int amount = 0;

		List<Player> allPlayers = game.getModifiablePlayerList();
		allPlayers.remove(rejecter);
		String[] allPlayersStringArray = playerToStringArray(allPlayers);

		String selection = gui.getUserSelection("Who bid the highest amount?", allPlayersStringArray);

		for (Player player : allPlayers) {
			if (player.getName().equals(selection)) {
				highestBidder = player;
				break;
			}
		}

		amount = gui.getUserInteger("How much did you bid?", property.getCost(), Integer.MAX_VALUE);
		buyPropertyOnAuction(property, rejecter, amount);

	}
	
	/**
	 * Action handling the situation when one player is broke to another
	 * player. All money and properties are the other player.
	 *  
	 * @param brokePlayer the broke player
	 * @param benificiary the player who receives the money and assets
	 * 
	 * @author Ekkart Kindler - Modified by Jaafar Mahdi
	 */
	public void playerBrokeTo(Player brokePlayer, Player benificiary) {
		int amount = brokePlayer.getBalance();
		benificiary.receiveMoney(amount);
		brokePlayer.setBalance(0);
		brokePlayer.setBroke(true);

		for (Property property: brokePlayer.getOwnedProperties()) {
			// Property is being 'cleaned' before transferred to a new owner
			cleanUpProperty(property);
			
			property.setOwner(benificiary);
			benificiary.addOwnedProperty(property);
		}	
		brokePlayer.removeAllProperties();
		
		while (!brokePlayer.getOwnedCards().isEmpty()) {
			game.returnCardToDeck(brokePlayer.getOwnedCards().get(0));
		}
		
		gui.showMessage("Player " + brokePlayer.getName() + "went broke and transfered all"
				+ "assets to " + benificiary.getName());
	}
	
	/**
	 * Action handling the situation when a player is broke to the bank.
	 * 
	 * @param player the broke player
	 * 
	 * @author Ekkart Kindler - Modified by Jaafar Mahdi
	 */
	public void playerBrokeToBank(Player player) {

		player.setBalance(0);
		player.setBroke(true);
		
		for (Property property: player.getOwnedProperties()) {
			property.setOwner(null);
			cleanUpProperty(property);
		}
		player.removeAllProperties();
		
		gui.showMessage("Player " + player.getName() + " went broke");
		
		while (!player.getOwnedCards().isEmpty()) {
			game.returnCardToDeck(player.getOwnedCards().get(0));
		}
	}
	
	/**
	 * Clears everything about a property, so it is, 
	 * as if nobody owned it.
	 * 
	 * @param property
	 * 
	 * @author Jaafar Mahdi
	 */
	private void cleanUpProperty(Property property) {
		property.setIsDeveloped(false);
		property.setIsMortgaged(false);
		property.setNumberOfHouses(0);
	}
	
	/**
	 * Thus method handles all the transfers when trading a property.
	 * It sets the buyer as the new owner, and transfers the money from the buyer to the seller.
	 * 
	 * @param seller
	 * @param property
	 * @param buyer
	 * @param money
	 * 
	 * @author Sebastian Bilde
	 */
	public void tradeProperty(Player seller, Property property, Player buyer, int money) {
		seller.removeOwnedProperty(property);
		buyer.addOwnedProperty(property);
		try {
			this.payment(buyer, money, seller);
		} catch (PlayerBrokeException e) {
			if(buyer.getBalance()<money) {
				playerBrokeTo(buyer, seller);
			}
		}
	}
	
	/**
	 * This method makes the user selection for trading property's. 
	 * It asks for a seller, buyer, the property being sold and the amount of money the buyer is paying.
	 * 
	 * @author Sebastian Bilde
	 */
	public void tradePropertyUserSelection() {
		ArrayList<Player> playerList = new ArrayList<Player>();
		ArrayList<Player> sellingPlayers = new ArrayList<Player>();
		String[] players = this.fromPlayerListToString(game.getPlayers());
		Player seller= null;
		Player buyer= null;
		Property chosenProperty=null;

		for(Player playerWithProperty: game.getPlayers())
			if(playerWithProperty.getOwnedProperties().size()>0) {
				//Computes all players who are eligible sellers
				for(Player player: game.getPlayers()) {
					if(player.getOwnedProperties().size()>0) {
						sellingPlayers.add(player);
					}
				}
				//Computes all player who are eligible buyers
				for(Player player: game.getPlayers()) {
					if(!player.isBroke()) {
						playerList.add(player);
					}
				}
				String sellSelect = gui.getUserSelection("Which player wants to sell a property? ", this.fromPlayerArrayListToStringArray(sellingPlayers));
				for(int i = 0; i < players.length; i++) {
					if(sellSelect.equals(players[i])) {
						seller = game.getPlayers().get(i);
						//removes the seller from the buying players list
						playerList.remove(i);
					}
				}
				List<Property> tradeableProperties = this.computeMortgageAblePropertyList(seller);
				String buySelect = gui.getUserSelection("Which player wants to buy a property? ", this.fromPlayerListToString(playerList));
				for(int i = 0; i < players.length; i++) {
					if(buySelect.equals(players[i])) {
						buyer = game.getPlayers().get(i);
					}
				}
				String propertySelect = gui.getUserSelection("Which property do you want to sell? ",  this.fromPropertyListToString(tradeableProperties));
				for(int i = 0; i < this.fromPropertyListToString(tradeableProperties).length; i++) {
					if(propertySelect.equals(this.fromPropertyListToString(tradeableProperties)[i])) {
						chosenProperty = tradeableProperties.get(i);
					}
				}
				int moneySelect = gui.getUserInteger("How much is the buyer paying? ");
				if(buyer.getBalance() < moneySelect) {
					this.obtainCash(buyer, moneySelect - buyer.getBalance());
				}
				this.tradeProperty(seller, chosenProperty, buyer, moneySelect);
			}
	}
	
	/**
	 * Reforms the Set<Property> to as list of all the players properties, that are mortgage able.
	 * @param player
	 * @return A list of all the players properties, that are mortgage able.
	 * 
	 * @author Sebastian
	 */
	public List<Property> computeMortgageAblePropertyList(Player player){
		Set<Property> ownedProperties = player.getOwnedProperties();
		ArrayList<Property> mortgageAblePropertyList = new ArrayList<Property>();
		for (Property property: ownedProperties) {
			if (!property.getIsDeveloped() && !property.getIsMortgaged())
			mortgageAblePropertyList.add(property);
		}
		return mortgageAblePropertyList;
	}
	
	
	/**
	 * Mortgage a property of a player, and sets the property as mortgaged, and gives the player the mortgage amount.
	 * @param player
	 * @param property
	 * 
	 * @author Sebastian
	 */
	public void mortgageProperty(Player player, Property property) {
		property.setIsMortgaged(true);
		player.setBalance(player.getBalance()+property.getCost()/2);
	}
	
	/**
	 * Gives the current player the possibility to mortgage any owned properties, that are mortgage able, and lists those for the player.
	 * The player then chooses which property to mortgage.
	 * @param player
	 * 
	 * @author Sebastian
	 */
	private void mortgageUserSelection(Player player) {
		do {
			if(this.computeMortgageAblePropertyList(player).size()>0) {
				String[] mortageAblePropertyArray = this.fromPropertyListToString(this.computeMortgageAblePropertyList(player));
				String select = gui.getUserSelection("Do you " + player.getName() + " want to mortgage any owned properties?",
						"no",
						"yes");
				if (select.equals("yes")) {
					String mortgageSelection = gui.getUserSelection("Which property do you want to mortgage?", mortageAblePropertyArray);
					for(int i = 0; i<mortageAblePropertyArray.length; i++)
						if(mortgageSelection.equals(mortageAblePropertyArray[i])) {
							Property chosenProperty = computeMortgageAblePropertyList(player).get(i);
							this.mortgageProperty(player, chosenProperty);
						}
				} else {
					break;
				}
			} else {
				break;
			}
		} while (true);
	}
	
	/**
	 * This method buys back a mortgaged property, the player also pays 10% of the mortgaged value as a fee.
	 * @param player
	 * @param property
	 * @author Sebastian
	 */
	private void buyBackMortagedProperty(Player player, Property property) {
		property.setIsMortgaged(false);
		player.payMoney(property.getCost()/2-property.getCost()/20);
	}

	/**
	 * This method makes the user selection, for buying back a mortgaged property
	 * @param player
	 * @author Sebastian Bilde
	 */
	private void buyBackMortagedPropertiesUserSelect(Player player) {
		do {
			if(this.computePlayersMortagedPropertiesList(player).size()>0) {
				String[] mortagedProperties = this.fromPropertyListToString(this.computePlayersMortagedPropertiesList(player));
				String select = gui.getUserSelection("Do you " + player.getName() + " want to buy back mortgaged properties?",
						"no",
						"yes");
				if(select.equals("yes")) {
					String mortgageSelection = gui.getUserSelection("Which property do you want to buy back?", mortagedProperties);
					for(int i = 0; i<mortagedProperties.length; i++)
						if(mortgageSelection.equals(mortagedProperties[i])) {
							Property chosenProperty = this.computePlayersMortagedPropertiesList(player).get(i);
							this.buyBackMortagedProperty(player, chosenProperty);
						}
				}
				else break;
			}
			else {
				break;
			}
		} while (true);
	}
	
	/**
	 * Computes a list of all the players properties that are mortgaged
	 * @param player
	 * @return A list of all the players properties that are mortgaged
	 * @author Sebastian Bilde
	 */
	private List<Property> computePlayersMortagedPropertiesList(Player player) {
		Set<Property> ownedProperties = player.getOwnedProperties();
		ArrayList<Property> mortgagedPropertyList = new ArrayList<Property>();
		for (Property property: ownedProperties) {
			if (property.getIsMortgaged())
			mortgagedPropertyList.add(property);
		}
		return mortgagedPropertyList;
	}
	
	/**
	 * Converts a List of type Property, to a String Array with each property's name.
	 * @param list
	 * @return a String Array holding each property's name.
	 * @author Sebastian Bilde
	 */
	private String[] fromPropertyListToString(List<Property> list) {
		String[] stringArray = new String[list.size()];
		for(int i = 0; i<list.size(); i++) {
			stringArray[i] = list.get(i).getName();
		}
		return stringArray;
	}
	
	/**
	 * Converts an ArrayList of type Player, to a String Array with each players name
	 * @param arrayList
	 * @return a String Array holding each players name.
	 * @author Sebastian Bilde
	 */
	private String[] fromPlayerArrayListToStringArray(ArrayList<Player> arrayList) {
		String[] stringArray = new String[arrayList.size()];
		for(int i = 0; i<arrayList.size(); i++) {
			stringArray[i] = arrayList.get(i).getName();
		}
		return stringArray;
	}	
	
	/**
	 * Converts a List of Player, to an ArrayList of Player
	 * @param list
	 * @return An ArrayList of type Player
	 * @author Sebastian Bilde
	 */
	private ArrayList<Player> fromPlayerListToArrayList(List<Player> list) {
		ArrayList<Player> arrayList = new ArrayList<Player>();
		for(int i = 0; i<list.size(); i++) {
			arrayList.add(list.get(i));
		}
		return arrayList;
	}
	
	/**
	 * Converts a List of players to a string Array with each players name.
	 * @param list
	 * @return a string array with each players name
	 * @author Sebastian Bilde
	 */
	private String[] fromPlayerListToString(List<Player> list) {
		String[] stringArray = new String[list.size()];
		for(int i = 0; i<list.size(); i++) {
			stringArray[i] = list.get(i).getName();
		}
		return stringArray;
	}
	
	/**
	 * Method for disposing of this controller and cleaning up its resources.
	 */
	public void dispose() {
		if (!disposed) {
			disposed = true;
			view.dispose();
			// TODO we should also dispose of the GUI here. But this works only
			//      for my private version on the GUI and not for the GUI currently
			//      deployed via Maven (or other  official versions)
		}
	}

	/**
	 * Creates String[] from a List of strings
	 *
	 * @param list
	 * @return
	 *
	 * @author Jaafar Mahdi
	 */
	private String[] stringListToStringArray(List<String> list) {
		String[] stringArray = new String[list.size()];

		for (int i = 0; i < list.size(); i++) {
			stringArray[i] = list.get(i);
		}

		return stringArray;
	}

	/**
	 *
	 * @param player
	 * @param color
	 * @return boolean that tells wether the player owns all the properties of a given color
	 *
	 * @author Jaafar Mahdi
	 */
	public boolean checkWetherPlayerOwnAllInColor(Player player, int color) {
		List<Property> properties = game.getColorToPropertyMap().get(color);
		boolean ownsAll = false;
		int numberOfOwnedProperties = 0;
		for (Property property : properties) {
			if (property.getOwner() == player) {
				numberOfOwnedProperties++;
			}
		}

		if (numberOfOwnedProperties == properties.size()) {
			ownsAll = true;
		}

		return ownsAll;
	}

	/**
	 *
	 * @param player
	 * @param color
	 * @return integer that tells how many of a given utility that a player owns
	 *
	 * @author Jaafar Mahdi
	 */
	public int checkNumberOfOwnedUtilities(Player player, int color) {
		List<Property> properties = game.getColorToPropertyMap().get(color);
		int numberOfOwnedUtilities = 0;
		for (Property property : properties) {
			if (property.getOwner() == player) {
				numberOfOwnedUtilities++;
			}
		}

		return numberOfOwnedUtilities;
	}

    /**
     * Roll the dice
     *
     * @author Oliver Køppen
     */
	private void rollDice(){
	    dieOne = (int)(1+6.0*Math.random());
        dieTwo = (int)(1+6.0*Math.random());
	}

	private boolean castDouble(){
	    return dieOne==dieTwo;
    }

	public int getDiceValue(){
	    return dieOne+dieTwo;
    }

	/**
	 *
	 * @param player
	 * @return List with all the RealEstates that can be build upon
	 *
	 * @author Jaafar Mahdi
	 */
	private List<RealEstate> buildableHousesList(Player player) {
		List<RealEstate> realEstateList = actionableRealEstates(player, true);

		for (RealEstate realEstate : realEstateList) {
			if (realEstate.getNumberOfHouses() > 4) {
				realEstateList.remove(realEstate);
			}
		}

		return realEstateList;
	}

	/**
	 *
	 * @param player
	 * @return List with all the RealEstates that can be sold houses from
	 *
	 * @author Jaafar Mahdi
	 */
	private List<RealEstate> sellableHousesList(Player player) {
		List<RealEstate> realEstateList = actionableRealEstates(player, false);

		for (RealEstate realEstate : realEstateList) {
			if (realEstate.getNumberOfHouses() < 1) {
				realEstateList.remove(realEstate);
			}
		}

		return realEstateList;
	}

	/**
	 *
	 * @param player
	 * @param buildable
	 * @return List with RealEstates that can be perfomed actions on. Either build upon, or sell houses from.
	 *
	 * @author Jaafar Mahdi
	 */
	private List<RealEstate> actionableRealEstates(Player player, boolean buildable) {
		List<List<RealEstate>> listOfRealEstateLists = generateOwnedRealEstateLists(player);
		//Populates a List with all the RealEstates that the Player can do actions on
		List<RealEstate> realEstateList = new ArrayList<>();
		for (List<RealEstate> realEstates : listOfRealEstateLists) {

			//For buyable and sellable RealE
			if (buildable) {
				Collections.sort(realEstates);
			} else {
				Collections.reverse(realEstates);
			}

			int high = realEstates.get(0).getNumberOfHouses();
			int middle = realEstates.get(1).getNumberOfHouses();
			int low = realEstates.get(realEstates.size() - 1).getNumberOfHouses();

			if (high == low) {
				for (RealEstate realEstate : realEstates) {
					realEstateList.add(realEstate);
				}
			} else {
				if (high == middle) {
					for (int i = 0; i < realEstates.size() - 2; i++) {
						realEstateList.add(realEstates.get(i));
					}
				} else {
					realEstateList.add(realEstates.get(0));
				}
			}
		}

		return realEstateList;
	}

	/**
	 *
	 * @param player
	 * @return List with Lists that contain RealEstates with same colorCode, and which the player owns all of.
	 *
	 * @author Jaafar Mahdi
	 */
	private List<List<RealEstate>> generateOwnedRealEstateLists(Player player) {
		//Computes a Set of the colors that is owned by the Player
		Set<Integer> ownedAllColorCodeList = null;
		for (Property property : player.getOwnedProperties()) {
			if (property instanceof RealEstate) {
				if (checkWetherPlayerOwnAllInColor(player, property.getColorCode())) {
					ownedAllColorCodeList.add(property.getColorCode());
				}
			}
		}

		//Generates a List that contains Lists of Properties
		List<List<Property>> listOfPropertyLists = new ArrayList<>();
		Map<Integer, List<Property>> colorToPropertyMap = game.getColorToPropertyMap();
		for (Integer color : ownedAllColorCodeList) {
			listOfPropertyLists.add(colorToPropertyMap.get(color));
		}

		//Converts all Properties to RealEstates
		//It is known that all object are RealEstates, we made sure of that
		//In the first loop.
		List<List<RealEstate>> listOfRealEstateLists = new ArrayList<>();
		for (List<Property> propertyList : listOfPropertyLists) {
			List<RealEstate> realEstateList = new ArrayList<>();
			for (Property property : propertyList) {
				RealEstate realEstate = (RealEstate) property;
				realEstateList.add(realEstate);
			}
			listOfRealEstateLists.add(realEstateList);
		}

		return listOfRealEstateLists;


	}

	/**
	 * Creates a new Array of a List of RealEstates
	 *
	 * @param realEstateList
	 * @return StringArray with all the names of the RealEstates in the list.
	 *
	 * @author Jaafar Mahdi
	 */
	private String[] realEstateToStringArray(List<RealEstate> realEstateList) {
		String[] stringArray = new String[realEstateList.size()];

		for (int i = 0; i < realEstateList.size(); i++) {
			stringArray[i] = realEstateList.get(i).getName();
		}

		return stringArray;
	}

	/**
	 * Creates new Array of a List of Players
	 *
	 * @param playerList
	 * @return StringArray with all the names of the RealEstates in the list.
	 *
	 * @author Jaafar Mahdi
	 */
	private String[] playerToStringArray(List<Player> playerList) {
		String[] stringArray = new String[playerList.size()];

		for (int i = 0; i < playerList.size(); i++) {
			stringArray[i] = playerList.get(i).getName();
		}

		return stringArray;
	}

	/**
	 * Sells a house and gets half of the building cost
	 *
	 * @param player
	 * @param realEstate
	 *
	 * @author Jaafar Mahdi
	 */
	private void sellHouse(Player player, RealEstate realEstate) {
		if (realEstate.getNumberOfHouses() > 0) {
			realEstate.decrementHouses();
		}
		player.receiveMoney((realEstate.getHousePrice() / 2));
	}

	/**
	 * Builds a house on top of a RealEstate and withdraws money from the Player
	 *
	 * @param player
	 * @param realEstate
	 *
	 * @author Jaafar Mahdi
	 */
	private void buyHouse(Player player, RealEstate realEstate) {
		if (realEstate.getNumberOfHouses() < 5) {
			realEstate.incrementHouses();
		}
		player.payMoney(realEstate.getHousePrice());
	}

	/**
	 * Manages the user-selection, if the Player wants to sell a house on a
	 * specific RealEstate
	 *
	 * @param player
	 *
	 * @author Jaafar Mahdi
	 */
	private void sellHousesUserSelection(Player player) {
		List<RealEstate> realEstateList = sellableHousesList(player);
		String[] realEstateStringList = realEstateToStringArray(realEstateList);
		String selection = gui.getUserSelection("Which house do you want to sell?", realEstateStringList);

		for (RealEstate realEstate : realEstateList) {
			if (realEstate.getName().equals(selection)) {
				sellHouse(player, realEstate);
				break;
			}
		}
	}

	/**
	 * Manages the user-selection, if the Player wants to build
	 * a house on a RealEstate
	 *
	 * @param player
	 *
	 * @author Jaafar Mahdi
	 */
	private void buildHousesUserSelection(Player player) {
		List<RealEstate> realEstateList = buildableHousesList(player);
		String[] realEstateStringList = realEstateToStringArray(realEstateList);
		String selection = gui.getUserSelection("Which Real Estate do you want to build on?", realEstateStringList);

		for (RealEstate realEstate : realEstateList) {
			if (realEstate.getName().equals(selection)) {
				buyHouse(player, realEstate);
				break;
			}
		}
	}

	/**
	 * The Player makes a payment to the bank and gets the property.
	 * The Player becomes the owner of that property
	 *
	 * @param property
	 * @param player
	 * @param price
	 *
	 * @author Jaafar Mahdi
	 */
	private void buyPropertyOnAuction(Property property, Player player, int price) {
		try {
			paymentToBank(player, price);
			player.addOwnedProperty(property);
			property.setOwner(player);
		} catch (PlayerBrokeException e) {
			playerBrokeToBank(player);
		}
	}

	private List<String> generateObtainCashList(Player player) {
		List<String> options = new ArrayList<>();

		if (player.getOwnedProperties().size() > 0) {
			options.add(OPTION_1);
		}

		if (sellableHousesList(player).size() > 0) {
			options.add(OPTION_2);
		}

		if (computeMortgageAblePropertyList(player).size() > 0) {
			options.add(OPTION_3);
		}

		return options;
	}
}
