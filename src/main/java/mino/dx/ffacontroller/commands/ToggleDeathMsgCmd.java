package mino.dx.ffacontroller.commands;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import mino.dx.ffacontroller.FFAController;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import static io.papermc.paper.command.brigadier.Commands.literal;

public class ToggleDeathMsgCmd {

    // Paper Brigadier
    public static LiteralArgumentBuilder<CommandSourceStack> build(FFAController plugin) {
        // Usage: "/toggledeathmsg"
        return literal("toggledeathmsg")
                .requires(src -> src.getSender().hasPermission("ffacontroller.toggledeathmsg"))
                .executes(ctx -> {
                    CommandSender sender = ctx.getSource().getSender();

                    if (!(sender instanceof Player player)) {
                        sender.sendMessage("Chỉ người chơi mới dùng được lệnh này.");
                        return 1;
                    }

                    boolean current = plugin.getDeathMessageManager().isDeathMessageEnabled(player.getUniqueId());
                    plugin.getDeathMessageManager().setDeathMessage(player.getUniqueId(), !current);

                    player.sendMessage("§aDeath message của bạn đã được " + (!current ? "§aBẬT" : "§cTẮT") + "§f.");
                    return 1;
                });
    }
}
