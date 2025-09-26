package mino.dx.ffacontroller.commands;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import mino.dx.ffacontroller.FFAController;

import static io.papermc.paper.command.brigadier.Commands.literal;

public class ReportCmd {

    public static LiteralArgumentBuilder<CommandSourceStack> build(FFAController plugin) {
        return literal("report");
    }
}
