package de.realityrift.chunky.Provider;

import de.realityrift.chunky.SQL.MySQL;
import org.bukkit.Chunk;
import org.bukkit.entity.Player;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ChunkProvider
{
    public static void createChunkdb() throws SQLException
    {
        PreparedStatement ps = MySQL.getConnection().prepareStatement("CREATE TABLE IF NOT EXISTS claimed_chunks (player_name VARCHAR(100), UUID VARCHAR(100), ChunkX INT, ChunkZ INT)");
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


    public static void insertChunk(Player player, Chunk chunk) {
        try {
            String query = "INSERT IGNORE INTO claimed_chunks (player_name, UUID, ChunkX, ChunkZ) values (?,?,?,?)";
            try (PreparedStatement statement = MySQL.getConnection().prepareStatement(query)) {
                statement.setString(1, player.getName());
                statement.setString(2, String.valueOf(player.getUniqueId()));
                statement.setInt(3, chunk.getX());
                statement.setInt(4, chunk.getZ());

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
}
