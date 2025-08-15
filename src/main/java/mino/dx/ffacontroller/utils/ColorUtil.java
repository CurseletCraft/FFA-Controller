package mino.dx.ffacontroller.utils;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;

public class ColorUtil {

    @SuppressWarnings("unused")
    public static Component formatMessage(String message) {
        // Xử lý ký tự &
        Component comp = LegacyComponentSerializer.legacyAmpersand().deserialize(message);

        // Serialize sang MiniMessage format
        String miniMsgString = MiniMessage.miniMessage().serialize(comp);

        // Deserialize bằng MiniMessage (áp dụng tag <red>, hex, etc.)
        return MiniMessage.miniMessage().deserialize(miniMsgString);
    }

    @SuppressWarnings("unused")
    public static Component legacyFormatMessage(String message) {
        return LegacyComponentSerializer.legacyAmpersand().deserialize(message);
    }

    @SuppressWarnings("unused")
    public static Component tagsFormatMessage(String message) {
        return MiniMessage.miniMessage().deserialize(message);
    }
}
