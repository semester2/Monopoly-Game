package dk.dtu.compute.se.pisd.monopoly.mini;

import java.awt.Color;
import java.util.List;

import dk.dtu.compute.se.pisd.monopoly.mini.model.Card;
import dk.dtu.compute.se.pisd.monopoly.mini.model.Game;
import dk.dtu.compute.se.pisd.monopoly.mini.model.Player;
import dk.dtu.compute.se.pisd.monopoly.mini.model.Property;
import dk.dtu.compute.se.pisd.monopoly.mini.model.Space;
import dk.dtu.compute.se.pisd.monopoly.mini.model.exceptions.PlayerBrokeException;
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

			// TODO offer all players the options to trade etc.

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

	/**
	 * This method implements a activity of asingle move of the given player.
	 * It throws a {@link dk.dtu.compute.se.pisd.monopoly.mini.model.exceptions.PlayerBrokeException}
	 * if the player goes broke in this move. Note that this is still a very
	 * basic implementation of the move of a player; many aspects are still
	 * missing.
	 * 
	 * @param player the player making the move
	 * @throws PlayerBrokeException if the player goes broke during the move
	 */
	public void makeMove(Player player) throws PlayerBrokeException {

		boolean castDouble;
		int doublesCount = 0;
		do {
			int die1 = (int) (1 + 3.0*Math.random());
			int die2 = (int) (1 + 3.0*Math.random());
			castDouble = (die1 == die2);
			gui.setDice(die1, die2);
			
			if (player.isInPrison() && castDouble) {
				player.setInPrison(false);
				gui.showMessage("Player " + player.getName() + " leaves prison now since he cast a double!");
			} else if (player.isInPrison()) {
				gui.showMessage("Player " + player.getName() + " stays in prison since he did not cast a double!");
			}
			// TODO note that the player could also pay to get out of prison,
			//      which is not yet implemented 
			if (castDouble) {
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
				int newPos = (pos + die1 + die2) % spaces.size();
				Space space = spaces.get(newPos);
				moveToSpace(player, space);
				if (castDouble) {
					gui.showMessage("Player " + player.getName() + " cast a double and makes another move.");
				}
			}
		} while (castDouble);
	}
	
	/**
	 * This method implements the activity of moving the player to the new position,
	 * including all actions associated with moving the player to the new position.
	 * 
	 * @param player the moved player
	 * @param space the space to which the player moves
	 * @throws PlayerBrokeException when the player goes broke doing the action on that space
	 */
	public void moveToSpace(Player player, Space space) throws PlayerBrokeException {
		int posOld = player.getCurrentPosition().getIndex();
		player.setCurrentPosition(space);

		if (posOld > player.getCurrentPosition().getIndex()) {
			// Note that this assumes that the game has more than 12 spaces here!
			// TODO: the amount of 2000$ should not be a fixed constant here (could also
			//       be configured in the Game class.
			gui.showMessage("Player " + player.getName() + " receives 2000$ for passing Go!");
			this.paymentFromBank(player, 2000);
		}		
		gui.showMessage("Player " + player.getName() + " arrives at " + space.getIndex() + ": " +  space.getName() + ".");
		
		// Execute the action associated with the respective space. Note
		// that this is delegated to the field, which implements this action
		space.doAction(this, player);
	}	

	/**
	 * The method implements the action of a player going directly to jail.
	 * 
	 * @param player the player going to jail
	 */
	public void gotoJail(Player player) {
		// TODO the 10 should not be hard coded
		player.setCurrentPosition(game.getSpaces().get(10));
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
		// TODO implement
	}
	
	/**
	 * This method implements the activity of offering a player to buy
	 * a property. This is typically triggered by a player arriving on
	 * an property that is not sold yet. If the player chooses not to
	 * buy, the property will be set for auction.
	 * 
	 * @param property the property to be sold
	 * @param player the player the property is offered to
	 * @throws PlayerBrokeException when the player chooses to buy but could not afford it
	 */
	public void offerToBuy(Property property, Player player) throws PlayerBrokeException {
		// TODO We might also allow the player to obtainCash before
		// the actual offer, to see whether he can free enough cash
		// for the sale.
	
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
    			auction(property);
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
		auction(property);
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
	public void auction(Property property) {
		// TODO auction needs to be implemented
		gui.showMessage("Now, there would be an auction of " + property.getName() + ".");
	}
	
	/**
	 * Action handling the situation when one player is broke to another
	 * player. All money and properties are the other player.
	 *  
	 * @param brokePlayer the broke player
	 * @param benificiary the player who receives the money and assets
	 */
	public void playerBrokeTo(Player brokePlayer, Player benificiary) {
		int amount = brokePlayer.getBalance();
		benificiary.receiveMoney(amount);
		brokePlayer.setBalance(0);
		brokePlayer.setBroke(true);

		// We assume here, that the broke player has already sold all his houses! But, if
		// not, we could make sure at this point that all houses are removed from
		// properties (properties with houses on are not supposed to be transferred, neither
		// in a trade between players, nor when  player goes broke to another player)
		for (Property property: brokePlayer.getOwnedProperties()) {
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
	 */
	public void playerBrokeToBank(Player player) {

		player.setBalance(0);
		player.setBroke(true);
		
		// TODO we also need to remove the houses and the mortgage from the properties 

		for (Property property: player.getOwnedProperties()) {
			property.setOwner(null);
		}
		player.removeAllProperties();
		
		gui.showMessage("Player " + player.getName() + " went broke");
		
		while (!player.getOwnedCards().isEmpty()) {
			game.returnCardToDeck(player.getOwnedCards().get(0));
		}
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

}
