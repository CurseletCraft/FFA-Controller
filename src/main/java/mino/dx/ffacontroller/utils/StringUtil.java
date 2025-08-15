package mino.dx.ffacontroller.utils;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;

public class StringUtil {

    public static String serializeMessage(Component c) {
        return MiniMessage.miniMessage().serialize(c);
    }

    public static Component deserializeMessage(String message) {
        return MiniMessage.miniMessage().deserialize(message);
    }

    public static Component ampersandFormatMessage(String message) {
        return LegacyComponentSerializer.legacyAmpersand().deserialize(message);
    }
}
