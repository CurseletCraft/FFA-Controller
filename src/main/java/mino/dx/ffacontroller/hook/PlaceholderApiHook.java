package mino.dx.ffacontroller.hook;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import mino.dx.ffacontroller.FFAController;
import mino.dx.ffacontroller.manager.DeathMessageManager;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.file.FileConfiguration;
import org.jetbrains.annotations.NotNull;

public class PlaceholderApiHook extends PlaceholderExpansion {
    private final FFAController plugin;

    public PlaceholderApiHook(FFAController plugin) {
        this.plugin = plugin;
    }

    @Override
    public String onRequest(OfflinePlayer player, @NotNull String placeholder) {
        FileConfiguration config = plugin.getConfig();
        DeathMessageManager deathMessageManager = plugin.getDeathMessageManager();

        switch (placeholder.toLowerCase()) {
            case "website":
                return String.valueOf(config.getString("website-ads"));
            case "serverip":
                return String.valueOf(config.getString("server-ip"));
            case "toggledeathmessage":
                // Nếu player null hoặc chưa từng vào server thì UUID sẽ là null
                if (player == null) return "Không xác định";
                boolean enabled = deathMessageManager.isDeathMessageEnabled(player.getUniqueId());
                return enabled ? "Bật" : "Tắt";
            case "killstreak": // %ffacontroller_killstreak%
                if (player == null) return "0";
                return String.valueOf(plugin.getStreakManager().getCurrentStreak(player.getUniqueId()));
            default:
                return null;
        }
    }

    // other functions
    @Override
    public boolean persist() {
        return true;
    }

    @Override
    @NotNull
    @SuppressWarnings("all")
    public String getAuthor() {
        return plugin.getDescription().getAuthors().get(0);
    }

    @Override
    @NotNull
    public String getIdentifier() {
        return "ffacontroller";
    }

    @Override
    @NotNull
    @SuppressWarnings("all")
    public String getVersion() {
        return plugin.getDescription().getVersion();
    }
}
