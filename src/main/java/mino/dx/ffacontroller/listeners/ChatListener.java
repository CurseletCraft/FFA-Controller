package mino.dx.ffacontroller.listeners;

import io.papermc.paper.event.player.AsyncChatEvent;
import me.clip.placeholderapi.PlaceholderAPI;
import mino.dx.ffacontroller.FFAController;
import mino.dx.ffacontroller.utils.ColorUtil;
import net.kyori.adventure.text.Component;
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
        Player player = event.getPlayer();
        String rawMessage = PlainTextComponentSerializer.plainText().serialize(event.message());

        String format = plugin.getConfig().getString("chat-format", "{name}&r: {message}")
                .replace("{name}", player.getName())
                .replace("{message}", rawMessage);

        format = setPlaceholders(player, format);

        if(player.hasPermission("ffacontroller.chatcolor")) {
            sendComponent(Component.text(format)); // Format các kí tự &c thành red color nếu có quyền chatcolor
        } else {
            sendComponent(ColorUtil.formatMessage(format));
        }
    }

    private String setPlaceholders(Player player, String message) {
        return isPlaceholderAPIEnabled ? PlaceholderAPI.setPlaceholders(player, message) : message;
    }

    private void sendComponent(Component message) {
        for(Player player : plugin.getServer().getOnlinePlayers()) {
            player.sendMessage(message);
        }
    }
}
