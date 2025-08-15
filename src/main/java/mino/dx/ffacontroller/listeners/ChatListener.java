package mino.dx.ffacontroller.listeners;

import io.papermc.paper.event.player.AsyncChatEvent;
import me.clip.placeholderapi.PlaceholderAPI;
import mino.dx.ffacontroller.FFAController;
import mino.dx.ffacontroller.utils.ColorUtil;
import mino.dx.ffacontroller.utils.MessageParts;
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

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onAsyncChat(AsyncChatEvent event) {
        Player player = event.getPlayer();
        String rawMessage = PlainTextComponentSerializer.plainText().serialize(event.message());

        String format = plugin.getConfig().getString("chat-format", "{name}: {message}")
                .replace("{name}", player.getName());

        format = setPlaceholders(player, format);

        final Component finalC = componentBuilder(player, format, rawMessage);
        event.renderer((source, sourceDisplayName, message, viewer) -> finalC);
    }

    private String setPlaceholders(Player player, String message) {
        return isPlaceholderAPIEnabled ? PlaceholderAPI.setPlaceholders(player, message) : message;
    }

    private Component componentBuilder(Player player, String format, String rawMessage) {
         if(player.hasPermission("ffacontroller.chatcolor")) {
             return ColorUtil.formatMessage(format.replace("{message}", rawMessage));
         }
         else {
             // TODO: bug ở suffix cần sửa lại
             MessageParts parts = MessageParts.split(format, "{message}");

             // Capitalize nếu có suffix
             String finalMsg = rawMessage;
             if (!parts.getSuffix().isEmpty() && !rawMessage.isEmpty()) {
                 finalMsg = Character.toUpperCase(rawMessage.charAt(0)) + rawMessage.substring(1);
             }

             return parts.build(Component.text(finalMsg));
         }
    }
}
