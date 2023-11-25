package de.realityrift.chunky.SQL;


import de.realityrift.chunky.Lang.Language;
import org.bukkit.Bukkit;

import de.realityrift.chunky.Main.Main;
import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class MySQL
{


    static Language language = new Language(new File(Bukkit.getPluginManager() + "/lang.ini"));

    public static String host = Main.config.getString("host");
    public static String port = Main.config.getString("port");
    public static String username = Main.config.getString("user");
    public static String password = Main.config.getString("pwd");
    public static Connection con;

    // connect
    public static void connect(String database) {
        if (!isConnected()) {
            try {
                String url = "jdbc:mysql://" + host + ":" + port + "/" + database + "?autoReconnect=true";

                con = DriverManager.getConnection(url, username, password);
                System.out.println(language.translateString("connection.established", database));
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    // disconnect
    public static void disconnect() {
        if (isConnected()) {
            try {
                con.close();
                System.out.println(language.get("connection.dc"));
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    // isConnected
    public static boolean isConnected() {
        return (con != null);
    }

    // getConnection
    public static Connection getConnection() {
        return con;
    }


}

