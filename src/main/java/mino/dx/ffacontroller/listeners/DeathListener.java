package mino.dx.ffacontroller.listeners;

import mino.dx.ffacontroller.FFAController;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

import java.util.List;
import java.util.Random;

public class DeathListener implements Listener {

    private final FFAController plugin;
    private final Random random = new Random();

    public DeathListener(FFAController plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onDeath(PlayerDeathEvent e) {
        e.setDroppedExp(0); // Không rơi kinh nghiệm khi chết
        e.deathMessage(null); // Xóa tin nhắn death

        Player victim = e.getEntity();
        Player killer = victim.getKiller(); // Null nếu không bị giết bởi player

        FileConfiguration config = plugin.getConfig();
        String msg;

        if (killer != null) {
            // Lấy danh sách các death-messages từ config
            List<String> messages = config.getStringList("death-messages");
            if (!messages.isEmpty()) {
                // Chọn ngẫu nhiên 1 câu
                String rawMsg = messages.get(random.nextInt(messages.size()));
                // Thay thế placeholder
                msg = rawMsg
                        .replace("%victim%", victim.getName())
                        .replace("%killer%", killer.getName())
                        .replace("%health%", String.format("%.1f", killer.getHealth() / 2.0)); // Hiển thị theo tim
            } else {
                msg = "§c" + victim.getName() + " was slain by " + killer.getName();
            }
        } else {
            List<String> unknownMessages = config.getStringList("unknown-death-messages");
            if (!unknownMessages.isEmpty()) {
                String rawMsg = unknownMessages.get(random.nextInt(unknownMessages.size()));
                msg = rawMsg.replace("%victim%", victim.getName());
            } else {
                msg = "§c" + victim.getName() + " died mysteriously.";
            }
        }

        // Gửi đến tất cả người chơi
        for (Player online : Bukkit.getOnlinePlayers()) {
            if (plugin.getDeathMessageManager().isDeathMessageEnabled(online.getUniqueId())) {
                online.sendMessage(msg);
            }
        }
    }
}
