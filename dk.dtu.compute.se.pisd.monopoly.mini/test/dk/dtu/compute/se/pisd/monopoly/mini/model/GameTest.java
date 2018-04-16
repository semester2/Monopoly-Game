package dk.dtu.compute.se.pisd.monopoly.mini.model;

import dk.dtu.compute.se.pisd.monopoly.mini.MiniMonopoly;
import dk.dtu.compute.se.pisd.monopoly.mini.model.cards.CardMove;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class GameTest {
    Game game = MiniMonopoly.createGame();
    List<Card> cardDeck = game.getCardDeck();
    @BeforeEach
    void setUp() {

    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void getSpaces() {
    }

    @Test
    void setSpaces() {
    }

    @Test
    void addSpace() {
    }

    @Test
    void getCardDeck() {
    }


    @Test
    void drawCardFromDeckTest() {
        for (int i = 0; i<50;i++){
            game.drawCardFromDeck();
        }
        assertEquals(3, cardDeck.size());
    }

    @Test
    void returnCardToDeck() {
    }

    @Test
    void setCardDeck() {
    }

    @Test
    void shuffleCardDeck() {
    }

    @Test
    void getPlayers() {
    }

    @Test
    void setPlayers() {
    }

    @Test
    void addPlayer() {
    }

    @Test
    void getCurrentPlayer() {
    }

    @Test
    void setCurrentPlayer() {
    }

}