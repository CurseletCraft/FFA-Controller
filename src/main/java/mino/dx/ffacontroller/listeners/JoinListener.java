package mino.dx.ffacontroller.listeners;

import me.clip.placeholderapi.PlaceholderAPI;
import mino.dx.ffacontroller.FFAController;
import mino.dx.ffacontroller.utils.StringUtil;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

@SuppressWarnings("ClassCanBeRecord")
public class JoinListener implements Listener {

    private final FFAController plugin;

    public JoinListener(FFAController plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        Player player = e.getPlayer();
        String rawJoinMessage = plugin.getConfig().getString("join-message", "&a[+] &f%player%");
        String joinMessage = StringUtil
                .convertAmpersandToMiniMessage(rawJoinMessage)
                .replace("%player%", player.getName());

        PlaceholderAPI.setPlaceholders(player, joinMessage);

        Component component = MiniMessage.miniMessage().deserialize(joinMessage);
        e.joinMessage(component);
    }

    @EventHandler
    public void onLeave(PlayerQuitEvent e) {
        Player player = e.getPlayer();
        String rawLeaveMessage = plugin.getConfig().getString("leave-message", "&c[-] &f%player%");
        String leaveMessage = StringUtil
                .convertAmpersandToMiniMessage(rawLeaveMessage)
                .replace("%player%", player.getName());

        PlaceholderAPI.setPlaceholders(player, leaveMessage);

        Component component = MiniMessage.miniMessage().deserialize(leaveMessage);
        e.quitMessage(component);
    }
}
