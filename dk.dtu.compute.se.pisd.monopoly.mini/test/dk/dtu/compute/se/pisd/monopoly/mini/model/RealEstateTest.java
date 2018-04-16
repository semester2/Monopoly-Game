package dk.dtu.compute.se.pisd.monopoly.mini.model.properties;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import dk.dtu.compute.se.pisd.monopoly.mini.model.Game;

public class RealEstateTest {
	
	Game game = MiniMonopoly.createGame();
	List<Card> cardDeck = game.getCardDeck();
	List<Space> spaceList = game.getSpaces();
	Player player = game.getPlayers().get(0);
	
	spaceList.get(1).setOwner(player);
	
	
	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testComputeRent() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetOwnedSameColor() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetOwnAll() {
		fail("Not yet implemented");
	}

	@Test
	public void testCalculateRent() {
		fail("Not yet implemented");
	}

}
