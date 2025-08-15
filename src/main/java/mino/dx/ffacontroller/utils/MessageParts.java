package mino.dx.ffacontroller.utils;

import net.kyori.adventure.text.Component;

public class MessageParts {

    private final String prefix;
    private final String middle;
    private final String suffix;

    public MessageParts(String prefix, String middle, String suffix) {
        this.prefix = prefix;
        this.middle = middle;
        this.suffix = suffix;
    }

    @SuppressWarnings("unused")
    public String getPrefix() {
        return prefix;
    }

    @SuppressWarnings("unused")
    public String getMiddle() {
        return middle;
    }

    @SuppressWarnings("unused")
    public String getSuffix() {
        return suffix;
    }

    /**
     * Formats all parts with ColorUtil and returns a full Component.
     * Middle part can be pre-formatted or plain depending on your needs.
     */
    public Component build(Component middleComponent) {
        Component prefixC = ColorUtil.formatMessage(prefix);
        Component suffixC = ColorUtil.formatMessage(suffix);

        return prefixC.append(middleComponent).append(suffixC);
    }

    /**
     * Splits a string by a placeholder, returning prefix/suffix around it.
     * Middle is NOT stored here, you provide it later in build().
     */
    public static MessageParts split(String format, String placeholder) {
        int index = format.indexOf(placeholder);
        String prefix, suffix = "";

        if(index != -1) {
            prefix = format.substring(0, index);
            suffix = format.substring(index + placeholder.length());
        } else {
            // Không tìm thấy {message}, dùng nguyên format làm prefix
            prefix = format;
        }

        return new MessageParts(prefix, "", suffix);
    }
}
