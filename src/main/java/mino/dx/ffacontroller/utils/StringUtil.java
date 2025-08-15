package mino.dx.ffacontroller.utils;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.jetbrains.annotations.ApiStatus.NonExtendable;

@Deprecated
public class StringUtil {

    @Deprecated
    @NonExtendable
    public static String serializeMessage(Component component) {
        return MiniMessage.miniMessage().serialize(component);
    }

    @Deprecated
    @NonExtendable
    public static Component deserializeMessage(String message) {
        return MiniMessage.miniMessage().deserialize(message);
    }

    /**
     * @deprecated Khuyến nghị chuyển sang method <code>ColorUtil.formatMessage(String)</code>
     */
    @Deprecated
    @NonExtendable
    public static Component ampersandFormatMessage(String message) {
        return LegacyComponentSerializer.legacyAmpersand().deserialize(message);
    }
}
