package mino.dx.ffacontroller.listeners;

import io.papermc.paper.event.player.AsyncChatEvent;
import me.clip.placeholderapi.PlaceholderAPI;
import mino.dx.ffacontroller.FFAController;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

public class ChatListener implements Listener {

    private final FFAController plugin;
    private final boolean isPlaceholderAPIEnabled;

    public ChatListener(FFAController plugin) {
        this.plugin = plugin;
        this.isPlaceholderAPIEnabled = plugin.getServer().getPluginManager().getPlugin("PlaceholderAPI") != null;
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onAsyncChat(AsyncChatEvent event) {
        Component message = event.message();
        String rawMessage = PlainTextComponentSerializer.plainText().serialize(message);

        Player player = event.getPlayer();

        String format = plugin.getConfig().getString("chat-format");
        if(format == null) {
            sendComponent(formatMessage(player.getName() + "&r: " + rawMessage));
            return;
        }

        // config: chat-format: "{name}&r: {message}"
        format = format
                .replace("{name}", player.getName())
                .replace("{message}", rawMessage);

        if(isPlaceholderAPIEnabled) {
            PlaceholderAPI.setPlaceholders(player, format);
        }

        sendComponent(formatMessage(format));
    }

    private void sendComponent(Component message) {
        for(Player player : plugin.getServer().getOnlinePlayers()) {
            player.sendMessage(message);
        }
    }

    public static Component formatMessage(String message) {
        return LegacyComponentSerializer.legacyAmpersand().deserialize(message);
    }
}
