package mino.dx.ffacontroller.listeners;

import mino.dx.ffacontroller.FFAController;
import mino.dx.ffacontroller.api.events.KillstreakChangeEvent;
import mino.dx.ffacontroller.api.interfaces.IStreak;
import mino.dx.ffacontroller.utils.LoggerUtil;
import mino.dx.ffacontroller.utils.StringUtil;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

@SuppressWarnings("FieldMayBeFinal")
public class StreakListener implements Listener {

    private final FFAController plugin;
    private final IStreak streakManager;
    private boolean isShutdownMessageEnabled;
    private final boolean isAsync;

    public StreakListener(FFAController plugin) {
        this.plugin = plugin;
        this.streakManager = plugin.getStreakManager();
        this.isShutdownMessageEnabled = plugin.getConfig().getBoolean("killstreak.shutdown.enable", true);
        this.isAsync = plugin.getConfig().getBoolean("killstreak.async", false);
    }

    // MONITOR vì bạn chỉ đọc dữ liệu và xử lý riêng (không thay đổi logic chết/kill).
    // ignoreCancelled = true để tránh trigger khi event bị cancel bởi plugin khác.
    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onPlayerDeath(PlayerDeathEvent event) {
        Player victim = event.getEntity();
        Player killer = victim.getKiller();

        // Nếu có killer và killer không phải victim (tự sát)
        if (killer != null && killer != victim) {
            if(isAsync) {
                streakManager.addKillAsync(killer.getUniqueId(),
                                error -> LoggerUtil.severe("StreakListener error", error))
                        .thenRun(() -> streakManager.getCurrentStreakAsync(killer.getUniqueId())
                                .thenAccept(newStreak -> {
                                    int oldStreak = newStreak - 1;
                                    Bukkit.getScheduler().runTask(plugin,
                                            () -> handleStreakChange(killer, oldStreak, newStreak));
                                }));
            } else {
                int oldStreak = streakManager.getCurrentStreak(killer.getUniqueId());
                streakManager.addKill(killer.getUniqueId()); // Cộng kill streak cho killer

                // Cập nhật streak của killer
                int newStreak = streakManager.getCurrentStreak(killer.getUniqueId());
                handleStreakChange(killer, oldStreak, newStreak);
            }
        }

        // Nếu victim có streak > 0 => thông báo shutdown
        if (isAsync) {
            streakManager.getCurrentStreakAsync(victim.getUniqueId())
                    .thenAccept(victimStreak ->
                            Bukkit.getScheduler().runTask(plugin,
                                    () -> handleShutdown(victim, killer, victimStreak)
                ));
        } else {
            int victimStreak = streakManager.getCurrentStreak(victim.getUniqueId());
            handleShutdown(victim, killer, victimStreak);
        }
    }

    // Bukkit.broadcastMessage(color(msg));
    // method trên bị deprecated, sử dụng Bukkit.broadcast(Component) thay vì Bukkit.broadcast(String)
    private void broadcast(Component cpn) {
        Bukkit.broadcast(cpn);
    }

    private void handleShutdown(Player victim, Player killer, int victimStreak) {
        if (victimStreak > 0 && isShutdownMessageEnabled) {
            KillstreakChangeEvent event = new KillstreakChangeEvent(victim, killer, victimStreak, 0);
            Bukkit.getPluginManager().callEvent(event);
            if (event.isCancelled()) return;

            String shutdownMsg = plugin.getConfig().getString("killstreak.shutdown.message", "")
                    .replace("{prefix}", StringUtil.reformatCode(plugin.getConfig().getString("killstreak.prefix", "")))
                    .replace("%player%", victim.getName())
                    .replace("%streak%", String.valueOf(victimStreak));
            broadcast(Component.text(StringUtil.reformatCode(shutdownMsg)));
        }
        if (isAsync) {
            streakManager.resetStreakAsync(victim.getUniqueId());
        } else {
            streakManager.resetStreak(victim.getUniqueId());
        }
    }

    private void handleStreakChange(Player killer, int oldStreak, int newStreak) {
        // Bắn event
        KillstreakChangeEvent ksEvent = new KillstreakChangeEvent(killer, null, oldStreak, newStreak);
        Bukkit.getPluginManager().callEvent(ksEvent);
        if (ksEvent.isCancelled()) return;

        int every = plugin.getConfig().getInt("killstreak.every_n_kills");
        if (every > 0 && newStreak % every == 0) {
            String msg = plugin.getConfig().getString("killstreak.message", "")
                    .replace("{prefix}", StringUtil.reformatCode(plugin.getConfig().getString("killstreak.prefix", "")))
                    .replace("%player%", killer.getName())
                    .replace("%streak%", String.valueOf(newStreak));
            broadcast(Component.text(StringUtil.reformatCode(msg)));
        }
    }
}
