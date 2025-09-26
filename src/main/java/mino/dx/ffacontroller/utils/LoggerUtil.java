package mino.dx.ffacontroller.utils;

import mino.dx.ffacontroller.FFAController;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Level;

@SuppressWarnings("unused")
public class LoggerUtil {
    private static final FFAController plugin = JavaPlugin.getPlugin(FFAController.class);

    public static void log(String ctx) {
        plugin.getLogger().log(Level.INFO, ctx);
    }

    public static void log(String ctx, Throwable throwable) {
        plugin.getLogger().log(Level.INFO, ctx, throwable);
    }

    public static void logx(Throwable throwable, String ctx) {
        StackTraceElement caller = Thread.currentThread().getStackTrace()[2];
        // [0] = getStackTrace, [1] = throwException, [2] = nơi gọi LoggerUtil

        String className = caller.getClassName();
        String methodName = caller.getMethodName();

        // Lấy tên class ngắn gọn, bỏ package
        String simpleClassName = className.substring(className.lastIndexOf('.') + 1);
        String location = simpleClassName + "#" + methodName;

        plugin.getLogger().log(Level.INFO, location + " " + ctx, throwable);
    }

    public static void severe(String ctx) {
        plugin.getLogger().log(Level.SEVERE, ctx);
    }

    public static void severe(String ctx, Throwable throwable) {
        plugin.getLogger().log(Level.SEVERE, ctx, throwable);
    }

    public static void severex(Throwable throwable, String ctx) {
        StackTraceElement caller = Thread.currentThread().getStackTrace()[2];
        // [0] = getStackTrace, [1] = throwException, [2] = nơi gọi LoggerUtil

        String className = caller.getClassName();
        String methodName = caller.getMethodName();

        // Lấy tên class ngắn gọn, bỏ package
        String simpleClassName = className.substring(className.lastIndexOf('.') + 1);
        String location = simpleClassName + "#" + methodName;

        plugin.getLogger().log(Level.SEVERE, location + " " + ctx, throwable);
    }

    public static void warning(String ctx) {
        plugin.getLogger().log(Level.WARNING, ctx);
    }

    public static void warning(String ctx, Throwable throwable) {
        plugin.getLogger().log(Level.WARNING, ctx, throwable);
    }

    public static void warningx(Throwable throwable, String ctx) {
        StackTraceElement caller = Thread.currentThread().getStackTrace()[2];
        // [0] = getStackTrace, [1] = throwException, [2] = nơi gọi LoggerUtil

        String className = caller.getClassName();
        String methodName = caller.getMethodName();

        // Lấy tên class ngắn gọn, bỏ package
        String simpleClassName = className.substring(className.lastIndexOf('.') + 1);
        String location = simpleClassName + "#" + methodName;

        plugin.getLogger().log(Level.WARNING, location + " " + ctx, throwable);
    }
}
