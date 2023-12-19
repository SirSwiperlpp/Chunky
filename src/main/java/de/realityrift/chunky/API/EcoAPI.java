package de.realityrift.chunky.API;

import de.realityrift.chunky.Provider.EcoProvider;
import org.bukkit.entity.Player;

import java.sql.SQLException;

public class EcoAPI
{
    public static String transferMoney(Player sender, Player reciever, int updatevalue)
    {
        if (EcoProvider.getPlayerMoney(sender) < updatevalue)
            return "NotEnough";

        int senderbank = EcoProvider.getPlayerMoney(sender);
        int recieverbank = EcoProvider.getPlayerMoney(reciever);

        int moneyfromsender = senderbank - updatevalue;
        EcoProvider.updateMoney(sender, moneyfromsender);
        int moneytoreciever = recieverbank + updatevalue;
        EcoProvider.updateMoney(reciever, moneytoreciever);


        return "null";
    }

    public static void addMoney(Player player, int updatevalue)
    {
        int playerbank = EcoProvider.getPlayerMoney(player);
        int newvalue = playerbank + updatevalue;
        EcoProvider.updateMoney(player, newvalue);
        return;
    }

    public static void removeMoney(Player player, int updatevalue)
    {
        int playerbank = EcoProvider.getPlayerMoney(player);
        int newvalue = playerbank - updatevalue;
        EcoProvider.updateMoney(player, newvalue);
    }

}
