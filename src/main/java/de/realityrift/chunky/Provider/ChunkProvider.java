package de.realityrift.chunky.Provider;

import de.realityrift.chunky.SQL.MySQL;
import org.bukkit.Chunk;
import org.bukkit.entity.Player;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class ChunkProvider
{
    public static void createChunkdb() throws SQLException
    {
        PreparedStatement ps = MySQL.getConnection().prepareStatement("CREATE TABLE IF NOT EXISTS claimed_chunks (player_name VARCHAR(100), UUID VARCHAR(100), trusted VARCHAR(100), flags VARCHAR(100), ChunkX INT, ChunkZ INT)");
        ps.executeUpdate();
    }

    public static boolean getChunkFromdb(Chunk chunk) {
        try {
            String query = "SELECT ChunkX, ChunkZ FROM claimed_chunks WHERE ChunkX = ? AND ChunkZ = ?";
            try (PreparedStatement statement = MySQL.getConnection().prepareStatement(query)) {
                statement.setInt(1, chunk.getX());
                statement.setInt(2, chunk.getZ());

                try (ResultSet resultSet = statement.executeQuery()) {
                    return resultSet.next();
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static Map<String, Object> getAllInfosAboutChunk(Chunk chunk) {
        Map<String, Object> resultData = new HashMap<>();

        try {
            String query = "SELECT * FROM claimed_chunks WHERE ChunkX = ? AND ChunkZ = ?";
            try (PreparedStatement statement = MySQL.getConnection().prepareStatement(query)) {
                statement.setInt(1, chunk.getX());
                statement.setInt(2, chunk.getZ());

                try (ResultSet resultSet = statement.executeQuery()) {
                    if (resultSet.next()) {
                        String[] columns = {"player_name", "UUID", "trusted", "flags", "ChunkX", "ChunkZ"};

                        for (String columnName : columns) {
                            resultData.put(columnName, resultSet.getObject(columnName));
                        }

                        return resultData;
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("SQLException: " + e.getMessage());
        }
        return resultData;
    }

    public static String getPlayerNameForChunk(Chunk chunk) {
        try {
            String query = "SELECT player_name FROM claimed_chunks WHERE ChunkX = ? AND ChunkZ = ?";
            try (PreparedStatement statement = MySQL.getConnection().prepareStatement(query)) {
                statement.setInt(1, chunk.getX());
                statement.setInt(2, chunk.getZ());

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


    public static void insertChunk(Player player, Chunk chunk, String trusted, String flags) {
        try {
            String query = "INSERT IGNORE INTO claimed_chunks (player_name, UUID, trusted, flags, ChunkX, ChunkZ) values (?,?,?,?,?,?)";
            try (PreparedStatement statement = MySQL.getConnection().prepareStatement(query)) {
                statement.setString(1, player.getName());
                statement.setString(2, String.valueOf(player.getUniqueId()));
                statement.setString(3, trusted);
                statement.setString(4, flags);
                statement.setInt(5, chunk.getX());
                statement.setInt(6, chunk.getZ());

                statement.executeUpdate();

            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void removeChunk(Chunk chunk)
    {
        try {
            String query = "DELETE FROM claimed_chunks WHERE ChunkX = ? AND ChunkZ = ?";
            try (PreparedStatement statement = MySQL.getConnection().prepareStatement(query)) {
                statement.setInt(1, chunk.getX());
                statement.setInt(2, chunk.getZ());

                statement.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void createTheTalbe()
    {
        try {
            PreparedStatement ps = MySQL.getConnection().prepareStatement("CREATE TABLE IF NOT EXISTS PaymentCheck (wert VARCHAR(255))");
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static String getValueFromTheTalbe()
    {
        try {
            String query = "SELECT wert FROM PaymentCheck";
            try (PreparedStatement statement = MySQL.getConnection().prepareStatement(query)) {

                try (ResultSet resultSet = statement.executeQuery()) {
                    if (resultSet.next()) {
                        return resultSet.getString("wert");
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("SQLException: " + e.getMessage());
        }
        return "";
    }

    public static void insertInTheTalbe()
    {
        try {
            String query = "INSERT IGNORE INTO PaymentCheck (wert) values (?)";
            try (PreparedStatement statement = MySQL.getConnection().prepareStatement(query)) {
                statement.setString(1, "check");

                statement.executeUpdate();

            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void removefromTheTalbe()
    {
        try {
            String query = "DELETE FROM PaymentCheck WHERE wert = ?";
            try (PreparedStatement statement = MySQL.getConnection().prepareStatement(query)) {
                statement.setString(1, "check");

                statement.executeUpdate();

            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
