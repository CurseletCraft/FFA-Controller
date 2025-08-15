package mino.dx.ffacontroller.utils;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;

public class ColorUtil {

    public static Component formatMessage(String message) {

        // Xử lý ký tự & trước
        Component comp = LegacyComponentSerializer.legacyAmpersand().deserialize(message);

        // Serialize sang MiniMessage format
        String miniMsgString = MiniMessage.miniMessage().serialize(comp);

        // Deserialize bằng MiniMessage (áp dụng tag <red>, hex, etc.)
        return MiniMessage.miniMessage().deserialize(miniMsgString);
    }
}
