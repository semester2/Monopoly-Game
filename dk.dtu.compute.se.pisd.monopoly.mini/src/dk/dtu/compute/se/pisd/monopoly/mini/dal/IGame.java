package dk.dtu.compute.se.pisd.monopoly.mini.dal;

import dk.dtu.compute.se.pisd.monopoly.mini.model.Game;

public interface IGame {
    boolean createGame(Game game);
    boolean updateGame(Game game);
    Game loadGame(int id);
}
