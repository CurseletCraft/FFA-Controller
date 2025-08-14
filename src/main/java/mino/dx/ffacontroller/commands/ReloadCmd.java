package mino.dx.ffacontroller.commands;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import mino.dx.ffacontroller.FFAController;

import static io.papermc.paper.command.brigadier.Commands.literal;

public class ReloadCmd {

    public static LiteralArgumentBuilder<CommandSourceStack> build(FFAController plugin) {
        // Usage: "/ffacontroller reload"
        return literal("ffacontroller")
                .requires(src -> src.getSender().hasPermission("ffacontroller.reload"))
                .then(literal("reload")
                        .executes(ctx -> {
                            plugin.reloadConfig();
                            plugin.getDeathMessageManager().reload();
                            ctx.getSource().getSender().sendMessage("Config reloaded!");
                            return 1;
                        }));
    }
}
