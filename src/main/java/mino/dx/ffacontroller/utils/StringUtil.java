package mino.dx.ffacontroller.utils;

import java.util.HashMap;
import java.util.Map;

@SuppressWarnings("unused")
public class StringUtil {

    private static final Map<Character, Character> SMALL_FONT_MAP = new HashMap<>();

    static {
        SMALL_FONT_MAP.put('A', 'ᴀ');
        SMALL_FONT_MAP.put('B', 'ʙ');
        SMALL_FONT_MAP.put('C', 'ᴄ');
        SMALL_FONT_MAP.put('D', 'ᴅ');
        SMALL_FONT_MAP.put('E', 'ᴇ');
        SMALL_FONT_MAP.put('F', 'ꜰ');
        SMALL_FONT_MAP.put('G', 'ɢ');
        SMALL_FONT_MAP.put('H', 'ʜ');
        SMALL_FONT_MAP.put('I', 'ɪ');
        SMALL_FONT_MAP.put('J', 'ᴊ');
        SMALL_FONT_MAP.put('K', 'ᴋ');
        SMALL_FONT_MAP.put('L', 'ʟ');
        SMALL_FONT_MAP.put('M', 'ᴍ');
        SMALL_FONT_MAP.put('N', 'ɴ');
        SMALL_FONT_MAP.put('O', 'ᴏ');
        SMALL_FONT_MAP.put('P', 'ᴘ');
        SMALL_FONT_MAP.put('Q', 'ǫ');
        SMALL_FONT_MAP.put('R', 'ʀ');
        SMALL_FONT_MAP.put('S', 'ꜱ');
        SMALL_FONT_MAP.put('T', 'ᴛ');
        SMALL_FONT_MAP.put('U', 'ᴜ');
        SMALL_FONT_MAP.put('V', 'ᴠ');
        SMALL_FONT_MAP.put('W', 'ᴡ');
        SMALL_FONT_MAP.put('X', 'х');
        SMALL_FONT_MAP.put('Y', 'ʏ');
        SMALL_FONT_MAP.put('Z', 'ᴢ');

        // lowercase cũng map sang như uppercase
        for (char c = 'a'; c <= 'z'; c++) {
            char upper = Character.toUpperCase(c);
            if (SMALL_FONT_MAP.containsKey(upper)) {
                SMALL_FONT_MAP.put(c, SMALL_FONT_MAP.get(upper));
            }
        }
    }

    public static String toSmallFont(String input) {
        StringBuilder sb = new StringBuilder();
        for (char c : input.toCharArray()) {
            sb.append(SMALL_FONT_MAP.getOrDefault(c, c));
        }
        return sb.toString();
    }

    public static String capitalize(String input){
        return input.substring(0, 1).toUpperCase() + input.substring(1).toLowerCase();
    }

}
