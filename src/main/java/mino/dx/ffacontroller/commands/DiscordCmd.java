package mino.dx.ffacontroller.commands;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import mino.dx.ffacontroller.FFAController;
import mino.dx.ffacontroller.utils.MessageParts;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.minimessage.Context;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.Tag;
import net.kyori.adventure.text.minimessage.tag.resolver.ArgumentQueue;

import static io.papermc.paper.command.brigadier.Commands.literal;

public class DiscordCmd {

    public static LiteralArgumentBuilder<CommandSourceStack> build(FFAController plugin) {
        // Usage: "/discord"
        return literal("discord")
                .executes(ctx -> {
                    String link = plugin.getConfig().getString("discord-link");
                    String rawMessage = plugin.getConfig().getString("discord-link-message", "&fTham gia link discord tại đây: &b{link}");
                    String actionText = plugin.getConfig().getString("discord-link-action");

                    ctx.getSource().getSender().sendMessage(componentBuilder(rawMessage, link, actionText));
                    return 1;
                });
    }

    private static Component build(String link, String actionText) {
        String ctx = "<a:" + link + ">" + actionText + "</a>.";
        MiniMessage miniMessage = MiniMessage.builder()
                .editTags(adder -> adder.tag("a", DiscordCmd::openLink))
                .build();

        return miniMessage.deserialize(ctx);
    }

    // hoàn chỉnh, không cần sửa
    private static Tag openLink(ArgumentQueue args, Context ctx) {
        final String link = args.popOr("The <a> tag requires exactly one argument, the link to open").value();
        return Tag.styling(
                NamedTextColor.AQUA,
                TextDecoration.UNDERLINED,
                ClickEvent.openUrl(link),
                HoverEvent.showText(Component.text("Mở " + link))
        );
    }

    private static Component componentBuilder(String format, String link, String actionText) {

        MessageParts parts = MessageParts.split(format, "{link}");

        // link placeholder handler
        Component middle = build(link, actionText);

        // Component wrapper
        return parts.build(middle);
    }
}
