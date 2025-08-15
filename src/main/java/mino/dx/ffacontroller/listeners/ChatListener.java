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

        Component c;
        if(player.hasPermission("ffacontroller.chatcolor")) {
            String colored = format.replace("{message}", rawMessage);
            c = Component.text(colored); // Format các kí tự &c thành red color nếu có quyền chatcolor
        } else {
            // Không có quyền: giữ nguyên rawMessage, chỉ format phần ngoài
            String tempFormat = format.replace("{message}", "%msg%"); // chèn placeholder tạm
            Component outer = ColorUtil.formatMessage(tempFormat.replace("%msg%", "")); // format màu phần ngoài

            // Ghép lại: outer + raw message gốc (không format)
            // outer lúc này là kiểu Component, cần nối thêm text thô vào chỗ %msg%
            String outerPlain = PlainTextComponentSerializer.plainText().serialize(outer);
            outerPlain = outerPlain.replace("%msg%", rawMessage);
            c = ColorUtil.formatMessage(outerPlain);
        }

        final Component finalC = c;
        event.renderer((source, sourceDisplayName, message, viewer) -> finalC);
    }

    private String setPlaceholders(Player player, String message) {
        return isPlaceholderAPIEnabled ? PlaceholderAPI.setPlaceholders(player, message) : message;
    }
}
