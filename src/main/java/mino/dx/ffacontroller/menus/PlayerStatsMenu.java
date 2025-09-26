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
        // x ở đây có thể là từ 0-8, chỉ ra vị trí ô đồ theo hàng ngang từ 1 đến 9
        // y ở đây có thể là từ 0-5, chỉ ra vị trí ô đồ theo hàng dọc từ 1 đến 6
        fg.set(1, 1, new HeadItem(offlinePlayer));

        fg.set(3,1, new StatsItem(playerStats.getKills(), Material.IRON_SWORD, "Số Kill", NamedTextColor.GREEN, ""));
        fg.set(4,1, new StatsItem(playerStats.getDeaths(), Material.SKELETON_SKULL, "Số Deaths", NamedTextColor.RED, ""));
        fg.set(5,1, new StatsItem(playerStats.getStreak(), Material.BLAZE_POWDER, "Streak", NamedTextColor.GOLD, ""));
        fg.set(6,1, new StatsItem(playerStats.getTimePlayed(), Material.CLOCK, "Thời gian chơi", NamedTextColor.AQUA, ""));
        fg.set(7,1, new StatsItem(playerStats.getEloContext(), Material.NETHER_STAR, "Elo Ranking", NamedTextColor.LIGHT_PURPLE, "Elo: "));

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

    private static class StatsItem extends Button {

        private final String stats;
        private final Material material;
        private final String title;
        private final NamedTextColor color;
        private final String prefix;

        public StatsItem(String stats, Material material, String title, NamedTextColor color, String prefix) {
            this.stats = stats;
            this.material = material;
            this.title = title;
            this.color = color;
            this.prefix = prefix;
        }

        @Override
        public ItemStack getIcon() {
            ItemStack icon = new ItemStack(material);
            ItemMeta meta = icon.getItemMeta();

            meta.displayName(Component.text(title)
                    .color(color)
                    .decorate(TextDecoration.BOLD)
                    .decoration(TextDecoration.ITALIC, false));

            meta.lore(List.of(
                    Component.text(prefix + stats)
                            .color(NamedTextColor.WHITE)
                            .decoration(TextDecoration.ITALIC, false)
            ));

            icon.setItemMeta(meta);
            return icon;
        }
    }
}
