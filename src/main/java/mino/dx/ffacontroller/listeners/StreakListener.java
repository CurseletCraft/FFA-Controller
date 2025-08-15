package mino.dx.ffacontroller.listeners;

import mino.dx.ffacontroller.FFAController;
import mino.dx.ffacontroller.api.interfaces.IStreak;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

@SuppressWarnings("FieldMayBeFinal")
public class StreakListener implements Listener {

    private final FFAController plugin;
    private boolean isShutdownMessageEnabled;

    public StreakListener(FFAController controller) {
        this.plugin = controller;
        this.isShutdownMessageEnabled = controller.getConfig().getBoolean("killstreak.shutdown.enable", true);
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        IStreak iStreak = plugin.getStreakManager();

        Player victim = event.getEntity();
        Player killer = victim.getKiller();

        // Nếu có killer và killer không phải victim (tự sát)
        if (killer != null && killer != victim) {

            // Cộng kill streak cho killer
            iStreak.addKill(killer.getUniqueId());

            // Cập nhật streak của killer
            int newStreak = iStreak.getCurrentStreak(killer.getUniqueId());
            int every = plugin.getConfig().getInt("killstreak.every_n_kills");

            if (every > 0 && newStreak % every == 0) {
                String msg = plugin.getConfig().getString("killstreak.message", "")
                        .replace("{prefix}", color(plugin.getConfig().getString("killstreak.prefix", "")))
                        .replace("%player%", killer.getName())
                        .replace("%streak%", String.valueOf(newStreak));
                Bukkit.broadcastMessage(color(msg)); // bị deprecated, sử dụng Bukkit.broadcast(Component) thay vì Bukkit.broadcast(String)
                Bukkit.broadcast(Component.text(color(msg)));
            }
        }

        // Nếu victim có streak > 0 => thông báo shutdown
        int victimStreak = iStreak.getCurrentStreak(victim.getUniqueId());

        if (victimStreak > 0 && isShutdownMessageEnabled) {
            String shutdownMsg = plugin.getConfig().getString("killstreak.shutdown.message", "")
                    .replace("{prefix}", color(plugin.getConfig().getString("killstreak.prefix", "")))
                    .replace("%player%", victim.getName())
                    .replace("%streak%", String.valueOf(victimStreak));
            Bukkit.broadcastMessage(color(shutdownMsg));
        }

        // Reset streak của victim
        iStreak.resetStreak(victim.getUniqueId());
    }

    private String color(String msg) {
        return msg.replace("&", "§");
    }
}
