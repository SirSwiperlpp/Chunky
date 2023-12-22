package de.realityrift.chunky.Provider;

import de.realityrift.chunky.SQL.EcoSQL;
import de.realityrift.chunky.SQL.MySQL;
import org.bukkit.entity.Player;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public class EcoProvider
{
    public static void createEcoTable() throws SQLException
    {
        PreparedStatement ps = EcoSQL.getConnection().prepareStatement("CREATE TABLE IF NOT EXISTS ecotable (player_name VARCHAR(100), UUID VARCHAR(100), money INT)");
        ps.executeUpdate();
    }

    public static String checkForPlayer(Player player) throws SQLException
    {
        try {
            String query = "SELECT player_name FROM ecotable WHERE UUID = ?";
            try (PreparedStatement statement = EcoSQL.getConnection().prepareStatement(query)) {
                statement.setString(1, String.valueOf(player.getUniqueId()));

                try (ResultSet resultSet = statement.executeQuery()) {
                    if (resultSet.next()) {
                        return resultSet.getString("player_name");
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("SQLException: " + e.getMessage());
        }
        return "";
    }

    public static void insertPlayer(Player player)
    {
        try {
            String query = "INSERT IGNORE INTO ecotable (player_name, UUID, money) values (?,?,?)";
            try (PreparedStatement statement = EcoSQL.getConnection().prepareStatement(query)) {
                statement.setString(1, player.getName());
                statement.setString(2, String.valueOf(player.getUniqueId()));
                statement.setInt(3, 200);

                statement.executeUpdate();

            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static int getPlayerMoney(String uuid)
    {
        try {
            String query = "SELECT money FROM ecotable WHERE UUID = ?";
            try (PreparedStatement statement = EcoSQL.getConnection().prepareStatement(query)) {
                statement.setString(1, String.valueOf(uuid));

                try (ResultSet resultSet = statement.executeQuery()) {
                    if (resultSet.next()) {
                        return resultSet.getInt("money");
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("SQLException: " + e.getMessage());
        }
        return -1;
    }



    public static void updateMoney(String uuid, int updatevalue)
    {
        try {
            String query = "UPDATE ecotable SET money = ? WHERE UUID = ?";
            try (PreparedStatement statement = EcoSQL.getConnection().prepareStatement(query)) {
                statement.setInt(1, updatevalue);
                statement.setString(2, String.valueOf(uuid));

                statement.executeUpdate();

            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
