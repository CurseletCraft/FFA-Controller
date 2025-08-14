package mino.dx.ffacontroller.listeners;

import mino.dx.ffacontroller.FFAController;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

import java.util.HashMap;
import java.util.Map;

public class StreakListener implements Listener {

    private final Map<Player, Integer> streaks = new HashMap<>();
    private final FFAController controller;

    public StreakListener(FFAController controller) {
        this.controller = controller;
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        Player victim = event.getEntity();
        Player killer = victim.getKiller();

        // Nếu có killer và killer không phải victim (tự sát)
        if (killer != null && killer != victim) {
            // Cập nhật streak của killer
            int newStreak = streaks.getOrDefault(killer, 0) + 1;
            streaks.put(killer, newStreak);

            int every = controller.getConfig().getInt("killstreak.every_n_kills");
            if (every > 0 && newStreak % every == 0) {
                String msg = controller.getConfig().getString("killstreak.message", "")
                        .replace("{prefix}", color(controller.getConfig().getString("killstreak.prefix", "")))
                        .replace("%player%", killer.getName())
                        .replace("%streak%", String.valueOf(newStreak));
                Bukkit.broadcastMessage(color(msg));
            }
        }

        // Nếu victim có streak > 0 => thông báo shutdown
        int victimStreak = streaks.getOrDefault(victim, 0);
        if (victimStreak > 0 && controller.getConfig().getBoolean("killstreak.shutdown.enable")) {
            String shutdownMsg = controller.getConfig().getString("killstreak.shutdown.message", "")
                    .replace("{prefix}", color(controller.getConfig().getString("killstreak.prefix", "")))
                    .replace("%player%", victim.getName())
                    .replace("%streak%", String.valueOf(victimStreak));
            Bukkit.broadcastMessage(color(shutdownMsg));
        }

        // Reset streak của victim
        streaks.put(victim, 0);
    }

    private String color(String msg) {
        return msg.replace("&", "§");
    }
}
