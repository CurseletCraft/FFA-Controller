package mino.dx.ffacontroller.menus;

import mino.dx.ffacontroller.models.PlayerStats;
import mino.dx.ffacontroller.utils.StringUtil;
import net.j4c0b3y.api.menu.Menu;
import net.j4c0b3y.api.menu.MenuSize;
import net.j4c0b3y.api.menu.button.Button;
import net.j4c0b3y.api.menu.layer.impl.BackgroundLayer;
import net.j4c0b3y.api.menu.layer.impl.ForegroundLayer;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.List;
import java.util.Objects;

public class PlayerStatsMenu extends Menu {

    private final OfflinePlayer offlinePlayer;
    private final PlayerStats playerStats;

    public PlayerStatsMenu(Player player, OfflinePlayer offlinePlayer, PlayerStats stats) {
        super(StringUtil.toSmallFont(offlinePlayer.getName() + "'s Stats"), MenuSize.THREE, player);
        this.offlinePlayer = offlinePlayer;
        this.playerStats = stats;
    }

    @Override
    public void setup(BackgroundLayer bg, ForegroundLayer fg) {
        bg.border(new PlaceholderButton());

        // fg.set(pos x, pos y, class)
        fg.set(1, 1, new HeadItem(offlinePlayer));

        fg.set(3,1, new KillsItem(playerStats));
        fg.set(4,1, new DeathsItem(playerStats));
        fg.set(5,1, new StreakItem(playerStats));
        fg.set(6,1, new TimePlayedItem(playerStats));
        fg.set(7,1, new EloItem(playerStats));
    }

    @Override
    public void onClose() {
        this.getPlayer().sendMessage(Component.text("You closed the player stats menu.")
                .color(NamedTextColor.GREEN)
                .decorate(TextDecoration.BOLD));
    }

    // Local item field classes
    private static class PlaceholderButton extends Button {
        @Override
        public ItemStack getIcon() {
            ItemStack icon = new ItemStack(Material.GRAY_STAINED_GLASS_PANE);
            ItemMeta meta = icon.getItemMeta();
            meta.displayName(Component.text(" "));
            icon.setItemMeta(meta);
            return icon;
        }
    }

    // TODO: lấy head thật
    private static class HeadItem extends Button {

        private final OfflinePlayer offlinePlayer;

        public HeadItem(OfflinePlayer offlinePlayer) {
            this.offlinePlayer = offlinePlayer;
        }

        @Override
        public ItemStack getIcon() {
            ItemStack icon = new ItemStack(Material.PLAYER_HEAD);
            ItemMeta meta = icon.getItemMeta();

            if(meta instanceof SkullMeta skullMeta) {
                skullMeta.setOwningPlayer(offlinePlayer);
            }

            meta.displayName(Component.text(Objects.requireNonNull(offlinePlayer.getName()))
                    .color(NamedTextColor.AQUA)
                    .decorate(TextDecoration.BOLD)
                    .decoration(TextDecoration.ITALIC, false));
            icon.setItemMeta(meta);
            return icon;

        }
    }

    private static class KillsItem extends Button {

        private final String kills;

        public KillsItem(PlayerStats playerStats) {
            this.kills = playerStats.getKills();
        }

        @Override
        public ItemStack getIcon() {
            ItemStack icon = new ItemStack(Material.IRON_SWORD);
            ItemMeta meta = icon.getItemMeta();
            meta.displayName(Component.text("Số kill")
                    .color(NamedTextColor.GREEN)
                    .decorate(TextDecoration.BOLD)
                    .decoration(TextDecoration.ITALIC, false));
            meta.lore(List.of(
                    Component.text(kills).color(NamedTextColor.WHITE).decoration(TextDecoration.ITALIC, false)
                    ));
            icon.setItemMeta(meta);
            return icon;
        }
    }

    private static class DeathsItem extends Button {

        private final String deaths;

        public DeathsItem(PlayerStats playerStats) {
            this.deaths = playerStats.getDeaths();
        }

        @Override
        public ItemStack getIcon() {
            ItemStack icon = new ItemStack(Material.SKELETON_SKULL);
            ItemMeta meta = icon.getItemMeta();
            meta.displayName(Component.text("Số Deaths")
                    .color(NamedTextColor.RED)
                    .decorate(TextDecoration.BOLD)
                    .decoration(TextDecoration.ITALIC, false));
            meta.lore(List.of(
                    Component.text(deaths).color(NamedTextColor.WHITE)
                            .decoration(TextDecoration.ITALIC, false)
            ));
            icon.setItemMeta(meta);
            return icon;
        }
    }

    private static class StreakItem extends Button {

        private final String streak;

        public StreakItem(PlayerStats playerStats) {
            this.streak = playerStats.getStreak();
        }

        @Override
        public ItemStack getIcon() {
            ItemStack icon = new ItemStack(Material.BLAZE_POWDER);
            ItemMeta meta = icon.getItemMeta();
            meta.displayName(Component.text("Streak")
                    .color(NamedTextColor.GOLD)
                    .decorate(TextDecoration.BOLD)
                    .decoration(TextDecoration.ITALIC, false));
            meta.lore(List.of(
                    Component.text(streak).color(NamedTextColor.WHITE)
                            .decoration(TextDecoration.ITALIC, false)
            ));
            icon.setItemMeta(meta);
            return icon;
        }
    }

    private static class TimePlayedItem extends Button {

        private final String timePlayed;

        public TimePlayedItem(PlayerStats playerStats) {
            this.timePlayed = playerStats.getTimePlayed();
        }

        @Override
        public ItemStack getIcon() {
            ItemStack icon = new ItemStack(Material.CLOCK);
            ItemMeta meta = icon.getItemMeta();
            meta.displayName(Component.text("Thời gian chơi")
                    .color(NamedTextColor.AQUA)
                    .decorate(TextDecoration.BOLD)
                    .decoration(TextDecoration.ITALIC, false));
            meta.lore(List.of(
                    Component.text(timePlayed).color(NamedTextColor.WHITE)
                            .decoration(TextDecoration.ITALIC, false)
            ));
            icon.setItemMeta(meta);
            return icon;
        }
    }

    private static class EloItem extends Button {

        private final String elo;

        public EloItem(PlayerStats playerStats) {
            this.elo = playerStats.getEloContext();
        }

        @Override
        public ItemStack getIcon() {
            ItemStack icon = new ItemStack(Material.NETHER_STAR);
            ItemMeta meta = icon.getItemMeta();
            meta.displayName(Component.text("Elo Ranking")
                    .color(NamedTextColor.LIGHT_PURPLE)
                    .decorate(TextDecoration.BOLD)
                    .decoration(TextDecoration.ITALIC, false));
            meta.lore(List.of(
                    Component.text("Elo: " + elo).color(NamedTextColor.WHITE)
                            .decoration(TextDecoration.ITALIC, false)
            ));
            icon.setItemMeta(meta);
            return icon;
        }
    }
}
