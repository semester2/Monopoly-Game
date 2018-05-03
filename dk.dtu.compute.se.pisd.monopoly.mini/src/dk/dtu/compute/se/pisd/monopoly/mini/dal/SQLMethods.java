package dk.dtu.compute.se.pisd.monopoly.mini.dal;

import dk.dtu.compute.se.pisd.monopoly.mini.model.Game;
import dk.dtu.compute.se.pisd.monopoly.mini.model.Player;
import dk.dtu.compute.se.pisd.monopoly.mini.model.Property;
import dk.dtu.compute.se.pisd.monopoly.mini.model.Space;
import dk.dtu.compute.se.pisd.monopoly.mini.model.properties.RealEstate;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Methods for reading and writing data into the database
 *
 * @author Jaafar Mahdi
 * @author Ali Moussa
 */

public class SQLMethods {
    private Connector connector = new Connector();
    private Connection connection;
    public final String GAME_NAME = "gameName";
    public final String GAME_ID = "gameID";
    public final String PLAYER_NAME = "playerName";
    public final String PLAYER_ID = "playerID";
    public final String PROPERTY_ID = "propertyID";
    public final String MORTGAGED = "mortgated";
    public final String NUMBER_OF_HOUSES = "amountHouse";
    public final String IN_PRISON = "inPrison";
    public final String BALANCE = "balance";
    public final String PLACEMENT = "placement";
    public final String COUNT = "count";
    public final String CURRENT_POSITION = "placement";
    public final String IS_BROKE = "isBroke";

    public SQLMethods() {
        connection = connector.getConnection();
    }

    public void createGame(String gameName) throws SQLException {
        try {
            CallableStatement cs = connection.prepareCall("{CALL createGame(?)}");
            cs.setString(GAME_NAME, gameName);
            cs.execute();
        } catch (SQLException e) {
            System.out.println(e);
        }
    }

    public void createPlayer(int gameID, String name) throws SQLException {
        try {
            CallableStatement cs = connection.prepareCall("{CALL createPlayer(?, ?)}");
            cs.setInt(GAME_ID, gameID);
            cs.setString(PLAYER_NAME, name);
            cs.execute();
        } catch (SQLException e) {
            System.out.println(e);
        }
    }

    public void setPropertyOwner(int gameID, int propertyID, int playerID) throws SQLException {
        try {
            CallableStatement cs = connection.prepareCall("{CALL setPropertyOwnerID(?, ?, ?)}");
            cs.setInt(GAME_ID, gameID);
            cs.setInt(PROPERTY_ID, propertyID);
            cs.setInt(PLAYER_ID, playerID);
            cs.execute();
        } catch (SQLException e) {
            System.out.println(e);
        }
    }

    public void updatePropertyOwner(int gameID, int propertyID, int playerID) throws SQLException {
        try {
            CallableStatement cs = connection.prepareCall("{CALL updatePropertyOwnerID(?, ?, ?)}");
            cs.setInt(GAME_ID, gameID);
            cs.setInt(PROPERTY_ID, propertyID);
            cs.setInt(MORTGAGED, playerID);
            cs.execute();
        } catch (SQLException e) {
            System.out.println(e);
        }
    }

    public void mortgageProperty(int gameID, int propertyID, boolean b) throws SQLException {
        try {
            CallableStatement cs = connection.prepareCall("{CALL mortgageProperty(?, ?, ?)}");
            cs.setInt(GAME_ID, gameID);
            cs.setInt(PROPERTY_ID, propertyID);
            cs.setBoolean(MORTGAGED, b);
            cs.execute();
        } catch (SQLException e) {
            System.out.println(e);
        }
    }

    public void updateNumberOfHouses(int gameID, int propertyID, int numberOfHouses) throws SQLException {
        try {
            CallableStatement cs = connection.prepareCall("{CALL updateNumberOfHouses(?, ?, ?)}");
            cs.setInt(GAME_ID, gameID);
            cs.setInt(PROPERTY_ID, propertyID);
            cs.setInt(NUMBER_OF_HOUSES, numberOfHouses);
            cs.execute();
        } catch (SQLException e) {
            System.out.println(e);
        }
    }

    public void setBroke(int playerID) throws SQLException {
        try {
            CallableStatement cs = connection.prepareCall("{CALL setBroke(?)}");
            cs.setInt(PLAYER_ID, playerID);
            cs.execute();
        } catch (SQLException e) {
            System.out.println(e);
        }
    }

    public void setInPrison(boolean b, int playerID) throws SQLException {
        try {
            CallableStatement cs = connection.prepareCall("{CALL setInPrison(?, ?)}");
            cs.setBoolean(IN_PRISON, b);
            cs.setInt(PLAYER_ID, playerID);
            cs.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updatePlayerBalance(int balance, int playerID) throws SQLException {
        try {
            CallableStatement cs = connection.prepareCall("{CALL updatePlayerBalance(?, ?)}");
            cs.setInt(BALANCE, balance);
            cs.setInt(PLAYER_ID, playerID);
            cs.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updatePlayerPlacement(int gameID, int playerID, int index) throws SQLException {
        try {
            CallableStatement cs = connection.prepareCall("{CALL updatePlayerPlacement(?, ?, ?)}");
            cs.setInt(GAME_ID, gameID);
            cs.setInt(PLAYER_ID, playerID);
            cs.setInt(PLACEMENT, index);
            cs.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Player> loadPlayers(int gameID, Game game) throws SQLException {
        List<Player> playerList = new ArrayList<>();
        try {
            Statement statement = connection.createStatement();
            ResultSet set;
            set = statement.executeQuery("SELECT COUNT(*) AS count FROM playerviewbygameid WHERE gameID = " + gameID);
            int numberOfPlayers = 0;

            if (set.next()) {
                numberOfPlayers = set.getInt(COUNT);
                System.out.println("NOP: " + numberOfPlayers);
            }

            set = statement.executeQuery("SELECT * FROM playerviewbygameid WHERE gameID = " + gameID);

            for (int i = 0; i < numberOfPlayers; i++) {
                if (set.next()) {
                    Player player = new Player(
                            set.getInt(PLAYER_ID),
                            set.getString(PLAYER_NAME),
                            set.getInt(BALANCE),
                            game.getSpaces().get(set.getInt(CURRENT_POSITION)),
                            set.getBoolean(IN_PRISON),
                            set.getBoolean(IS_BROKE),
                            game.getColorList().get(i)
                    );

                    playerList.add(player);
                }
            }

        } catch (SQLException e) {
            //System.out.println(e);
            e.printStackTrace();
        } finally {
            return playerList;
        }
    }

    public List<GameDAO> loadGames() throws SQLException {
        List<GameDAO> gameDAOList = new ArrayList<>();
        try {
            Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery("SELECT * FROM Game");

            while (rs.next()) {
                gameDAOList.add(new GameDAO(rs.getInt(GAME_ID), rs.getString(GAME_NAME)));
            }
        } catch (SQLException e) {
            System.out.println(e);
        } finally {
            return gameDAOList;
        }
    }

    public void loadOwnedProperties(int gameID, Game game) throws SQLException {
        List<Player> players = game.getPlayers();
        List<Space> spaces = game.getModifiableSpaceList();

        try {
            Statement statement = connection.createStatement();
            ResultSet set = statement.executeQuery("SELECT * FROM Monopoly.propertyviewbygameid WHERE gameID = " + gameID);

            while (set.next()) {
                int propertyID = set.getInt(PROPERTY_ID);
                int playerID = set.getInt(PLAYER_ID);
                int houses = set.getInt(NUMBER_OF_HOUSES);
                boolean mortgaged = set.getBoolean(MORTGAGED);

                for (Player player : players) {
                    if (player.getPlayerID() == playerID) {
                        if (spaces.get(propertyID) instanceof Property) {
                            ((Property) spaces.get(propertyID)).setOwner(player);
                            ((Property) spaces.get(propertyID)).setIsMortgaged(mortgaged);

                            if (spaces.get(propertyID) instanceof RealEstate) {
                                ((RealEstate) spaces.get(propertyID)).setNumberOfHouses(houses);
                            }
                        }
                    }
                }
            }
        } catch (SQLException e) {
            System.out.println(e);
        }
    }

    public Integer getGameID() throws SQLException {
        Integer gameID = null;

        try {
            Statement statement = connection.createStatement();
            ResultSet set = statement.executeQuery("SELECT gameID FROM Monopoly.Game ORDER BY date DESC LIMIT 1");
            if (set.next()) {
                gameID = set.getInt(GAME_ID);
            }
        } catch (SQLException e) {
            System.out.println(e);
        } finally {
            return gameID;
        }
    }

    public Integer getPlayerID() throws SQLException {
        Integer playerID = null;

        try {
            Statement statement = connection.createStatement();
            ResultSet set = statement.executeQuery("SELECT playerID FROM Monopoly.Player ORDER BY playerID DESC LIMIT 1;");
            if (set.next()) {
                playerID = set.getInt(PLAYER_ID);
            }
        } catch (SQLException e) {
            System.out.println(e);
        } finally {
            return playerID;
        }
    }
}
