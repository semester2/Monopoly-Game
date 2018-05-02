package dk.dtu.compute.se.pisd.monopoly.mini.dal;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * Methods for reading and writing data into the database
 * 
 * @author Jaafar Mahdi
 */

public class SQLMethods {
    private Connector connector = new Connector();
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

    public void createGame(String gameName) throws SQLException {
        try {
            Connection connection = connector.getConnection();
            CallableStatement cs = connection.prepareCall("{CALL createGame(?)}");
            cs.setString(GAME_NAME, gameName);
            cs.execute();
        } catch (SQLException e) {
            System.out.println(e);
        }
    }

    public void createPlayer(int gameID, String name) throws SQLException {
        try {
            Connection connection = connector.getConnection();
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
            Connection connection = connector.getConnection();
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
            Connection connection = connector.getConnection();
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
            Connection connection = connector.getConnection();
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
            Connection connection = connector.getConnection();
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
            Connection connection = connector.getConnection();
            CallableStatement cs = connection.prepareCall("{CALL setBroke(?)}");
            cs.setInt(PLAYER_ID, playerID);
            cs.execute();
        } catch (SQLException e) {
            System.out.println(e);
        }
    }

    public void setInPrison(boolean b, int playerID) throws SQLException {
        try {
            Connection connection = connector.getConnection();
            CallableStatement cs = connection.prepareCall("{CALL setInPrison(?, ?)}");
            cs.setBoolean(IN_PRISON, b);
            cs.setInt(PLAYER_ID, playerID);
            cs.execute();
        } catch (SQLException e) {
            System.out.println(e);
        }
    }

    public void updatePlayerBalance(int balance, int playerID) throws SQLException {
        try {
            Connection connection = connector.getConnection();
            CallableStatement cs = connection.prepareCall("{CALL updatePlayerBalance(?, ?)}");
            cs.setInt(BALANCE, balance);
            cs.setInt(PLAYER_ID, playerID);
            cs.execute();
        } catch (SQLException e) {
            System.out.println(e);
        }
    }

    public void updatePlayerPlacement(int gameID, int playerID, int index) throws SQLException {
        try {
            Connection connection = connector.getConnection();
            CallableStatement cs = connection.prepareCall("{CALL updatePlayerPlacement(?, ?, ?)}");
            cs.setInt(GAME_ID, gameID);
            cs.setInt(PLAYER_ID, playerID);
            cs.setInt(PLACEMENT, index);
            cs.execute();
        } catch (SQLException e) {
            System.out.println(e);
        }
    }
}
