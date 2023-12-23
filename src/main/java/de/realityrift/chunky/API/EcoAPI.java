package de.realityrift.chunky.API;

import de.realityrift.chunky.Provider.EcoProvider;
import org.bukkit.entity.Player;

import java.sql.SQLException;
import java.util.UUID;

public class EcoAPI
{
    public static String transferMoney(Player sender, Player reciever, int updatevalue)
    {
        if (EcoProvider.getPlayerMoney(String.valueOf(sender.getUniqueId())) < updatevalue)
            return "NotEnough";

        int senderbank = EcoProvider.getPlayerMoney(String.valueOf(sender.getUniqueId()));
        int recieverbank = EcoProvider.getPlayerMoney(String.valueOf(reciever.getUniqueId()));

        int moneyfromsender = senderbank - updatevalue;
        EcoProvider.updateMoney(String.valueOf(sender.getUniqueId()), moneyfromsender);
        int moneytoreciever = recieverbank + updatevalue;
        EcoProvider.updateMoney(String.valueOf(reciever.getUniqueId()), moneytoreciever);


        return "null";
    }

    public static void addMoney(String uuid, int updatevalue)
    {
        int playerbank = EcoProvider.getPlayerMoney(String.valueOf(uuid));
        int newvalue = playerbank + updatevalue;
        EcoProvider.updateMoney(String.valueOf(uuid), newvalue);
        return;
    }

    public static void removeMoney(String uuid, int updatevalue)
    {
        int playerbank = EcoProvider.getPlayerMoney(uuid);
        int newvalue = playerbank - updatevalue;
        EcoProvider.updateMoney(uuid, newvalue);
    }

}
