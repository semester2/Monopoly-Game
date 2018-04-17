package dk.dtu.compute.se.pisd.monopoly.mini.model.properties;

import dk.dtu.compute.se.pisd.monopoly.mini.MiniMonopoly;
import dk.dtu.compute.se.pisd.monopoly.mini.model.Game;
import dk.dtu.compute.se.pisd.monopoly.mini.model.Player;
import dk.dtu.compute.se.pisd.monopoly.mini.model.Property;
import dk.dtu.compute.se.pisd.monopoly.mini.model.Space;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class RealEstateTest {

    @BeforeEach
    void setUp() {

    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void TestComputeRent() {
    }

    @Test
    void TestGetOwnedSameColor() {
        Player p = new Player();
        Property r1 = new Property();
        Property r2 = new Property();
        r1.setOwner(p);
        r2.setOwner(p);

        int actual = p.getOwnedProperties().size();

        assertEquals(2, actual);
    }

    @Test
    void TestGetOwnAll() {

    }

    @Test
    void TestCalculateRent() {
    }

}