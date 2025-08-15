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
             String messageContent = format.replace("{message}", rawMessage);
             return ColorUtil.formatMessage(messageContent);
         }
         else {
             int msgIndex = format.indexOf("{message}");
             String prefix = "";
             String suffix = "";

             if (msgIndex != -1) {
                 prefix = format.substring(0, msgIndex);
                 suffix = format.substring(msgIndex + "{message}".length());
             } else {
                 // Không tìm thấy {message}, dùng nguyên format làm prefix
                 prefix = format;
             }

             // Capitalize nếu có suffix
             String finalMsg = rawMessage;
             if (!suffix.isEmpty() && !rawMessage.isEmpty()) {
                 finalMsg = Character.toUpperCase(rawMessage.charAt(0)) + rawMessage.substring(1);
             }

             // Format phần ngoài
             Component outer = ColorUtil.formatMessage(prefix);
             Component end = ColorUtil.formatMessage(suffix);

             // Ghép: outer + rawMessage (text thường) + suffix
             return outer.append(Component.text(finalMsg)).append(end);
         }
    }
}
