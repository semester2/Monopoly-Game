package dk.dtu.compute.se.pisd.monopoly.mini.dal;

/**
 * GameDTO is used to access Games in the database
 *
 * @author Jaafar Mahdi
 */
public class GameDTO {
    private int gameID;
    private String gameName;

    public GameDTO(int gameID, String gameName) {
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
