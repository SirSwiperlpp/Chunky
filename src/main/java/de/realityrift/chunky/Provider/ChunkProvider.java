package de.realityrift.chunky.Provider;

import de.realityrift.chunky.SQL.EcoSQL;
import de.realityrift.chunky.SQL.MySQL;
import org.bukkit.Chunk;
import org.bukkit.entity.Player;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ChunkProvider {
    public static void createChunkdb() throws SQLException {
        PreparedStatement ps = MySQL.getConnection().prepareStatement("CREATE TABLE IF NOT EXISTS claimed_chunks (player_name VARCHAR(100), UUID VARCHAR(100), trusted VARCHAR(100), flags VARCHAR(100), chunktyp VARCHAR(100), ChunkX INT, ChunkZ INT, world VARCHAR(255))");
        ps.executeUpdate();
    }

    public static boolean getChunkFromdb(Chunk chunk, String world) {
        try {
            String query = "SELECT ChunkX, ChunkZ FROM claimed_chunks WHERE ChunkX = ? AND ChunkZ = ? AND world = ?";
            try (PreparedStatement statement = MySQL.getConnection().prepareStatement(query)) {
                statement.setInt(1, chunk.getX());
                statement.setInt(2, chunk.getZ());
                statement.setString(3, world);

                try (ResultSet resultSet = statement.executeQuery()) {
                    return resultSet.next();
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static Map<String, Object> getAllInfosAboutChunk(Chunk chunk, String world) {
        Map<String, Object> resultData = new HashMap<>();

        try {
            String query = "SELECT * FROM claimed_chunks WHERE ChunkX = ? AND ChunkZ = ? AND world = ?";
            try (PreparedStatement statement = MySQL.getConnection().prepareStatement(query)) {
                statement.setInt(1, chunk.getX());
                statement.setInt(2, chunk.getZ());
                statement.setString(3, world);

                try (ResultSet resultSet = statement.executeQuery()) {
                    if (resultSet.next()) {
                        String[] columns = {"player_name", "UUID", "trusted", "flags", "ChunkX", "ChunkZ", "world"};

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

    public static String getPlayerNameForChunk(Chunk chunk, String world) {
        try {
            String query = "SELECT player_name FROM claimed_chunks WHERE ChunkX = ? AND ChunkZ = ? AND world = ?";
            try (PreparedStatement statement = MySQL.getConnection().prepareStatement(query)) {
                statement.setInt(1, chunk.getX());
                statement.setInt(2, chunk.getZ());
                statement.setString(3, world);

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

    public static int countPlayerChunks(String pname)
    {
        try {
            String query = "SELECT COUNT(player_name) AS result_count FROM claimed_chunks WHERE player_name = ?";
            try (PreparedStatement statement = MySQL.getConnection().prepareStatement(query)) {
                statement.setString(1, pname);

                try (ResultSet resultSet = statement.executeQuery()) {
                    if (resultSet.next()) {
                        return resultSet.getInt("result_count");
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("SQLException: " + e.getMessage());
        }
        return 0;
    }


    public static void insertChunk(Player player, Chunk chunk, String trusted, String flags) {
        try {
            String query = "INSERT IGNORE INTO claimed_chunks (player_name, UUID, trusted, flags, ChunkX, ChunkZ, world) values (?,?,?,?,?,?,?)";
            try (PreparedStatement statement = MySQL.getConnection().prepareStatement(query)) {
                statement.setString(1, player.getName());
                statement.setString(2, String.valueOf(player.getUniqueId()));
                statement.setString(3, trusted);
                statement.setString(4, flags);
                statement.setInt(5, chunk.getX());
                statement.setInt(6, chunk.getZ());
                statement.setString(7, player.getWorld().getName());

                statement.executeUpdate();

            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void addTruted(String trusted, Player owner) {
        try {
            String query = "UPDATE claimed_chunks SET trusted = ? WHERE player_name = ?";
            try (PreparedStatement statement = MySQL.getConnection().prepareStatement(query)) {
                statement.setString(1, trusted);
                statement.setString(2, owner.getName());
                statement.executeUpdate();

            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static String getTrusted(Player player, Chunk chunk) {
        try {
            String query = "SELECT trusted FROM claimed_chunks WHERE player_name = ? AND ChunkX = ? AND ChunkZ = ? AND world = ?";
            try (PreparedStatement statement = MySQL.getConnection().prepareStatement(query)) {
                statement.setString(1, player.getName());
                statement.setString(2, String.valueOf(chunk.getX()));
                statement.setString(3, String.valueOf(chunk.getZ()));
                statement.setString(4, player.getWorld().getName());

                try (ResultSet resultSet = statement.executeQuery()) {
                    if (resultSet.next()) {
                        return resultSet.getString("trusted");
                    }
                }

            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return "";
    }

    public static void removeChunk(Chunk chunk, String world) {
        try {
            String query = "DELETE FROM claimed_chunks WHERE ChunkX = ? AND ChunkZ = ? AND world = ?";
            try (PreparedStatement statement = MySQL.getConnection().prepareStatement(query)) {
                statement.setInt(1, chunk.getX());
                statement.setInt(2, chunk.getZ());
                statement.setString(3, world);

                statement.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
