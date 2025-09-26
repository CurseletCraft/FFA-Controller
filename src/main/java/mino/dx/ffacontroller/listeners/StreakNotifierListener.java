package mino.dx.ffacontroller.listeners;

import mino.dx.ffacontroller.FFAController;
import mino.dx.ffacontroller.api.events.KillstreakChangeEvent;
import mino.dx.ffacontroller.utils.StringUtil;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

@SuppressWarnings("ClassCanBeRecord")
public class StreakNotifierListener implements Listener {

    @SuppressWarnings("all") private final FFAController plugin;

    public StreakNotifierListener(FFAController plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onStreakChanged(KillstreakChangeEvent event) {
        Player player = event.getPlayer();
        Player killer = event.getKiller();
        int newStreak = event.getNewStreak();

        // Nếu reset trước (newStreak == 0) xử lý trước — tránh bị bắt bởi %5 check
        if (newStreak == 0) {
            int oldStreak = event.getOldStreak();
            String killerString = killer != null ? killer.getName() : "không rõ nguyên nhân";
            String ctx = "&c" + player.getName() + " đã bị chấm dứt chuỗi kill " + oldStreak + " bởi " + killerString;
            broadcast(Component.text(StringUtil.reformatCode(ctx)));
            return;
        }

        // Thưởng milestone chỉ khi newStreak > 0 và chia hết cho 5
        if (newStreak > 0 && newStreak % 5 == 0) {
            String ctx = "&c" + player.getName() + " đang là kẻ hủy diệt! Hắn đang ở chuỗi kill " + newStreak + ".";
            broadcast(Component.text(StringUtil.reformatCode(ctx)));
        }
    }

    private void broadcast(Component cpn) {
        Bukkit.broadcast(cpn);
    }
}
