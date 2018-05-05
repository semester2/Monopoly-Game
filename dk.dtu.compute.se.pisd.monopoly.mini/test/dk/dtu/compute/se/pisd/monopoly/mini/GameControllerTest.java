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
    void buildableHousesListTest() {
        Map<Integer, List<Property>> map = game.getColorToPropertyMap();
        List<Property> properties = map.get(10);

        for (Property property : properties) {
            property.setOwner(p1);
        }

        int expected = 3;
        int actual = controller.buildableHousesList(p1).size();

        for (RealEstate realEstate : controller.buildableHousesList(p1)) {
            System.out.println(realEstate.getName());
        }

        assertEquals(expected, actual);
    }

    @Test
    void buildableHousesListTest2() {
        Map<Integer, List<Property>> map = game.getColorToPropertyMap();
        List<Property> properties = map.get(9);

        for (Property property : properties) {
            property.setOwner(p1);
        }

        properties = map.get(10);

        for (Property property : properties) {
            property.setOwner(p1);
        }

        int expected = 5;
        int actual = controller.buildableHousesList(p1).size();

        assertEquals(expected, actual);
    }

    @Test
    void sellableHousesListTest() {
        Map<Integer, List<Property>> map = game.getColorToPropertyMap();
        List<Property> properties = map.get(9);

        for (Property property : properties) {
            property.setOwner(p1);

            if (property instanceof RealEstate) {
                controller.buyHouse(p1, (RealEstate) property);
                System.out.println(property.getName() + " " + ((RealEstate) property).getNumberOfHouses());
            }
        }

        controller.sellHouse(p1, controller.sellableHousesList(p1).get(0));

        for (Property property : properties) {
            if (property instanceof RealEstate) {
                System.out.println(property.getName() + " " + ((RealEstate) property).getNumberOfHouses());
            }
        }

        int expected = 1;
        int actual = controller.sellableHousesList(p1).size();

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
    void buyPropertyOnAuctionTest() {
        Map<Integer, List<Property>> map = game.getColorToPropertyMap();
        List<Property> properties = map.get(9);
        RealEstate realEstate = (RealEstate) properties.get(0);
        controller.buyPropertyOnAuction(realEstate, p1, 5000);

        int expected = 1;
        int actual = p1.getOwnedProperties().size();

        System.out.println(p1.getBalance());

        assertEquals(expected, actual);
    }

    @Test
    void buildHouseUserSelectionTest() {
        Map<Integer, List<Property>> map = game.getColorToPropertyMap();
        List<Property> properties = map.get(9);

        for (Property property : properties) {
            property.setOwner(p1);
        }

        controller.buildHousesUserSelection(p1);

    }
}