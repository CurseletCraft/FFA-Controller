package mino.dx.ffacontroller.utils;

import mino.dx.ffacontroller.FFAController;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Level;

@SuppressWarnings("unused")
public class LoggerUtil {
    private static final FFAController plugin = JavaPlugin.getPlugin(FFAController.class);

    @Deprecated
    @SuppressWarnings("CallToPrintStackTrace")
    public static void throwExceptionOld(String className, Exception e) {
        String ctx = className + " has thrown an exception: " + e.getMessage();
        plugin.getLogger().severe(ctx);
        e.printStackTrace();
    }

    @Deprecated
    @SuppressWarnings("unused")
    public static void throwException(String className, Exception e) {
        throwException(className, "has thrown an exception", e);
    }

    /**
     * @deprecated chuyển đến dùng <code>LoggerUtil.severex(Throwable, String)</code>
     * severex là phương thức mới viết từ severe (thêm chữ "x" đằng sau) thể hiện phương thức mới
     * để dễ debug trong trường hợp các ngoại lệ được thả ra (ví dụ IOException...)
     */
    @Deprecated
    public static void throwException(String className, String ctx, Exception e) {
        plugin.getLogger().log(Level.SEVERE, className + " " + ctx, e); // ghép chuỗi className với ctx
    }

    @Deprecated
    @SuppressWarnings("unused")
    public static void warningException(String className, Exception e) {
        warningException(className, "has thrown an exception", e);
    }

    @Deprecated
    public static void warningException(String className, String ctx, Exception e) {
        plugin.getLogger().log(Level.WARNING, className + " " + ctx, e);
    }

    public static void log(String ctx) {
        plugin.getLogger().log(Level.INFO, ctx);
    }

    public static void log(String ctx, Throwable throwable) {
        plugin.getLogger().log(Level.INFO, ctx, throwable);
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
