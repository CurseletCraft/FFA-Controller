package mino.dx.ffacontroller.commands;

import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import me.clip.placeholderapi.PlaceholderAPI;
import mino.dx.ffacontroller.FFAController;
import mino.dx.ffacontroller.menus.PlayerStatsMenu;
import mino.dx.ffacontroller.models.PlayerStats;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.concurrent.CompletableFuture;

import static io.papermc.paper.command.brigadier.Commands.argument;
import static io.papermc.paper.command.brigadier.Commands.literal;

@SuppressWarnings("DuplicatedCode")
public class PlayerStatsBrigadierCmd {

    public static LiteralArgumentBuilder<CommandSourceStack> build(@SuppressWarnings("unused") FFAController plugin) {
        return literal("stats")
                .then(argument("target-player", StringArgumentType.word())
                        .suggests(PlayerStatsBrigadierCmd::getPlayersSuggestions)
                        .executes(ctx -> {
                            CommandSender sender = ctx.getSource().getSender();
                            if(!(sender instanceof Player player)) {
                                sender.sendMessage("Only players can execute this command");
                                return 1;
                            }
                            String targetPlayer = ctx.getArgument("target-player", String.class);
                            if(targetPlayer == null) {
                                targetPlayer = player.getName();
                            }
                            
                            OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(targetPlayer);

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
                            return 1;
                        })
                );
    }

    private static CompletableFuture<Suggestions> getPlayersSuggestions(
            final CommandContext<CommandSourceStack> ctx,
            final SuggestionsBuilder suggestionsBuilder) {
        for (Player player  : Bukkit.getOnlinePlayers()) {
            suggestionsBuilder.suggest(player.getName());
        }
        return suggestionsBuilder.buildFuture();
    }
}
