package mino.dx.ffacontroller.utils;

import mino.dx.ffacontroller.FFAController;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Level;

@Deprecated
@SuppressWarnings("all")
public class ExceptionUtil {

    // No cast NullPointerException
    private static final FFAController plugin = JavaPlugin.getPlugin(FFAController.class);

    public static void throwExceptionOld(String className, Exception e) {
        String ctx = className + " has thrown an exception: " + e.getMessage();
        plugin.getLogger().severe(ctx);
        e.printStackTrace();
    }

    public static void throwException(String className, Exception e) {
        throwException(className, "has thrown an exception", e);
    }

    public static void throwException(String className, String ctx, Exception e) {
        plugin.getLogger().log(Level.SEVERE, className + " " + ctx, e); // ghép chuỗi className với ctx
    }

    public static void warningException(String className, Exception e) {
        warningException(className, "has thrown an exception", e);
    }

    public static void warningException(String className, String ctx, Exception e) {
        plugin.getLogger().log(Level.WARNING, className + " " + ctx, e);
    }
}
