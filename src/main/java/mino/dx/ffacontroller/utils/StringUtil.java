package mino.dx.ffacontroller.utils;

@SuppressWarnings("unused")
public class StringUtil {

    public static String capitalize(String input){
        return input.substring(0, 1).toUpperCase() + input.substring(1).toLowerCase();
    }

}
