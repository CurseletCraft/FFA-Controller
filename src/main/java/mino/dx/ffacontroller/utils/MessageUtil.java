package mino.dx.ffacontroller.utils;

import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class MessageUtil {

    public static void broadcastMessage(Component component) {
        for(Player player : Bukkit.getOnlinePlayers()) {
            player.sendMessage(component);
        }
    }
}
