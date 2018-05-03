package dk.dtu.compute.se.pisd.monopoly.mini.dal;

/**
 * GameDAO is used to access Games in the database
 *
 * @author Jaafar Mahdi
 */
public class GameDAO {
    private int gameID;
    private String gameName;

    public GameDAO(int gameID, String gameName) {
        this.gameID = gameID;
        this.gameName = gameName;
    }

    public int getGameID() {
        return gameID;
    }

    public String getGameName() {
        return gameName;
    }
}
