package mino.dx.ffacontroller.commands;

import me.clip.placeholderapi.PlaceholderAPI;
import mino.dx.ffacontroller.FFAController;
import mino.dx.ffacontroller.menus.PlayerStatsMenu;
import mino.dx.ffacontroller.models.PlayerStats;
import net.j4c0b3y.api.command.annotation.command.Command;
import net.j4c0b3y.api.command.annotation.parameter.Named;
import net.j4c0b3y.api.command.annotation.parameter.classifier.Sender;
import net.j4c0b3y.api.command.annotation.registration.Register;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;

@Register(name = "playerstats", description = "Xem chỉ số của người chơi")
@SuppressWarnings({"ClassCanBeRecord", "DuplicatedCode"})
public class PlayerStatsCmd {

    @SuppressWarnings({"all"})
    private final FFAController controller;

    public PlayerStatsCmd(FFAController controller) {
        this.controller = controller;
    }

    @Command(name = "")
    @SuppressWarnings("unused")
    public void statsCommand(
            @Sender Player player,
            @Nullable @Named("ingame_name") String playerName
    ) {
        if(playerName == null) {
            playerName = player.getName();
        }

        OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(playerName);

        // Get stats
        String kills = PlaceholderAPI.setPlaceholders(offlinePlayer, "%statistic_player_kills%");
        String deaths = PlaceholderAPI.setPlaceholders(offlinePlayer, "%statistic_deaths%");
        String streak = PlaceholderAPI.setPlaceholders(offlinePlayer, "%ffacontroller_killstreak%");

        String daysPlayed = PlaceholderAPI.setPlaceholders(offlinePlayer, "%statistic_time_played:days%");
        String hoursPlayed = PlaceholderAPI.setPlaceholders(offlinePlayer, "%statistic_time_played:hours%");
        String minutesPlayed = PlaceholderAPI.setPlaceholders(offlinePlayer, "%statistic_time_played:minutes%");

        String dhm = daysPlayed + "d " + hoursPlayed + "h " + minutesPlayed + "m";

        String elo = PlaceholderAPI.setPlaceholders(offlinePlayer, "%flamepractice_elo%");
        String eloPrefix = PlaceholderAPI.setPlaceholders(offlinePlayer, "%flamepractice_elo_prefix%");

        String eloContext = elo + " - Rank: " + eloPrefix;

        PlayerStats stats = new PlayerStats(kills, deaths, streak, dhm, eloContext);

        new PlayerStatsMenu(player, offlinePlayer, stats).open();
    }
}
