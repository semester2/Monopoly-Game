package dk.dtu.compute.se.pisd.monopoly.mini.model.properties;

import dk.dtu.compute.se.pisd.monopoly.mini.MiniMonopoly;
import dk.dtu.compute.se.pisd.monopoly.mini.model.Game;
import dk.dtu.compute.se.pisd.monopoly.mini.model.Player;
import dk.dtu.compute.se.pisd.monopoly.mini.model.Property;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PropertyTest {

    Player p = new Player();

    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void TestComputeRent() {
        System.out.print(p);
        RealEstate r = new RealEstate();
        r.setOwner(p);
        r.setRent(100);
        r.setColorCode(1);
        RealEstate r2 = new RealEstate();
        r2.setColorCode(1);
        r2.setOwner(p);
        r2.setRent(300);
        r.computeRent(p);
        assertEquals(200, r.getRent());
    }
}