package dk.dtu.compute.se.pisd.monopoly.mini;

import dk.dtu.compute.se.pisd.monopoly.mini.model.Game;
import dk.dtu.compute.se.pisd.monopoly.mini.model.Player;
import dk.dtu.compute.se.pisd.monopoly.mini.model.Property;
import dk.dtu.compute.se.pisd.monopoly.mini.model.Space;
import dk.dtu.compute.se.pisd.monopoly.mini.model.properties.RealEstate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class GameControllerTest {

    Game game = MiniMonopoly.createGame();
    GameController controller = new GameController(game);
    Player p1 = new Player();
    Player p2 = new Player();

    @BeforeEach
    void setUp() {
        game.addPlayer(p1);
        game.addPlayer(p2);
    }

    @Test
    void checkWetherPlayerOwnAllInColorTest() {
        Map<Integer, List<Property>> map = game.getColorToPropertyMap();
        List<Property> properties = map.get(9);

        for (Property property : properties) {
            property.setOwner(p1);
        }

        boolean expected = true;
        boolean actaul = controller.checkWetherPlayerOwnAllInColor(p1, 9);

        assertEquals(expected, actaul);
    }

    @Test
    void checkNumberOfOwnedUtilitiesTest() {
        Map<Integer, List<Property>> map = game.getColorToPropertyMap();
        List<Property> properties = map.get(1);

        for (Property property : properties) {
            property.setOwner(p1);
        }

        int expected = 4;
        int actual = controller.checkNumberOfOwnedUtilities(p1, 1);

        assertEquals(expected, actual);
    }

    @Test
    void sellHouseTest() {
        Map<Integer, List<Property>> map = game.getColorToPropertyMap();
        List<Property> properties = map.get(9);

        for (Property property : properties) {
            property.setOwner(p1);
        }

        RealEstate realEstate = controller.buildableHousesList(p1).get(0);

        controller.buyHouse(p1, realEstate);
        controller.sellHouse(p1, realEstate);

        int expected = 0;
        int actual = realEstate.getNumberOfHouses();
    }

    @Test
    void buyHouseTest() {
        Map<Integer, List<Property>> map = game.getColorToPropertyMap();
        List<Property> properties = map.get(9);

        for (Property property : properties) {
            property.setOwner(p1);
        }

        RealEstate realEstate = controller.buildableHousesList(p1).get(0);

        controller.buyHouse(p1, realEstate);

        int expected = 1;
        int actual = realEstate.getNumberOfHouses();

        assertEquals(expected, actual);
    }

    @Test
    void tradePropertyTest() {
        Map<Integer, List<Property>> map = game.getColorToPropertyMap();
        List<Property> properties = map.get(9);

        for (Property property : properties) {
            property.setOwner(p1);
        }

        controller.tradeProperty(p1, properties.get(0), p2, 15000);

        int expected = 1;
        int actual = p2.getOwnedProperties().size();

        assertEquals(expected, actual);

    }

    @Test
    void tradePropertyTest2() {
        Map<Integer, List<Property>> map = game.getColorToPropertyMap();
        List<Property> properties = map.get(9);

        for (Property property : properties) {
            property.setOwner(p1);
        }

        controller.tradeProperty(p1, properties.get(0), p2, 15000);

        int expected = 45000;
        int actual = p1.getBalance();

        assertEquals(expected, actual);

    }

    @Test
    void tradePropertyTest3() {
        Map<Integer, List<Property>> map = game.getColorToPropertyMap();
        List<Property> properties = map.get(9);

        for (Property property : properties) {
            property.setOwner(p1);
        }

        controller.tradeProperty(p1, properties.get(0), p2, 15000);

        int expected = 15000;
        int actual = p2.getBalance();

        assertEquals(expected, actual);

    }

    @Test
    void tradePropertyTest4() {
        Map<Integer, List<Property>> map = game.getColorToPropertyMap();
        List<Property> properties = map.get(9);

        for (Property property : properties) {
            property.setOwner(p1);
        }

        controller.tradeProperty(p1, properties.get(0), p2, 15000);

        int expected = 1;
        int actual = p1.getOwnedProperties().size();

        assertEquals(expected, actual);

    }

    @Test
    void mortgagePropertyTest() {
        Map<Integer, List<Property>> map = game.getColorToPropertyMap();
        List<Property> properties = map.get(9);

        for (Property property : properties) {
            property.setOwner(p1);
        }

        controller.mortgageProperty(p1, properties.get(0));

        int expected = 30600;
        int acutal = p1.getBalance();

        assertEquals(expected, acutal);
    }

    @Test
    void computeTotalWorthTest() {
        Map<Integer, List<Property>> map = game.getColorToPropertyMap();
        List<Property> properties = map.get(9);

        for (Property property : properties) {
            property.setOwner(p1);
        }

        properties = map.get(10);

        for (Property property : properties) {
            property.setOwner(p1);
        }

        List<Property> playerProperties = new ArrayList<>();
        for (Property property : p1.getOwnedProperties()) {
            playerProperties.add(property);
        }

        int expected = 38800;
        int actual = p1.computeTotalWorth(p1);

        assertEquals(expected, actual);
    }
}