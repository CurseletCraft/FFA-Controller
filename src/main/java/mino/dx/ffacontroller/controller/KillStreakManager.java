package mino.dx.ffacontroller.controller;

import mino.dx.ffacontroller.FFAController;
import mino.dx.ffacontroller.api.interfaces.IStreak;
import mino.dx.ffacontroller.utils.LoggerUtil;
import org.jetbrains.annotations.ApiStatus.*;

import java.io.File;
import java.sql.*;
import java.time.Instant;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

@NonExtendable
public class KillStreakManager implements IStreak {

    private final File databaseFile;
    private Connection connection;
    private final boolean isAsync = false; // TODO: constructor của KillStreakManager nên getConfig

    public KillStreakManager(FFAController plugin) {
        this.databaseFile = new File(plugin.getDataFolder(), "killstreaks.db");
        initDatabase();
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    private void initDatabase() {
        try {
            if (!databaseFile.exists()) {
                databaseFile.getParentFile().mkdirs();
                databaseFile.createNewFile();
            }

            connection = DriverManager.getConnection("jdbc:sqlite:" + databaseFile.getAbsolutePath());

            try (Statement stmt = connection.createStatement()) {
                stmt.executeUpdate(
                        "CREATE TABLE IF NOT EXISTS killstreaks (" +
                                "uuid TEXT PRIMARY KEY," +
                                "current_streak INTEGER NOT NULL," +
                                "best_streak INTEGER NOT NULL," +
                                "last_update TIMESTAMP NOT NULL" +
                                ");"
                );
            }

        } catch (Exception e) {
            LoggerUtil.throwException(this.getClass().getName(), "Lỗi khi khởi tạo SQLite", e);
        }
    }

    // ======== Guard methods ========
    private void requireSync() {
        if (isAsync) {
            throw new UnsupportedOperationException("This method can only be used in sync mode.");
        }
    }

    private void requireAsync() {
        if (!isAsync) {
            throw new UnsupportedOperationException("This method can only be used in async mode.");
        }
    }

    // ======== Queries ========
    private int queryCurrentStreak(UUID uuid) {
        try (PreparedStatement ps = connection.prepareStatement(
                "SELECT current_streak FROM killstreaks WHERE uuid = ?")) {
            ps.setString(1, uuid.toString());
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt("current_streak");
            }
        } catch (SQLException e) {
            LoggerUtil.severe(this.getClass().getName(), e);
        }
        return 0;
    }

    private int queryBestStreak(UUID uuid) {
        try (PreparedStatement ps = connection.prepareStatement(
                "SELECT best_streak FROM killstreaks WHERE uuid = ?")) {
            ps.setString(1, uuid.toString());
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt("best_streak");
            }
        } catch (SQLException e) {
            LoggerUtil.severe(this.getClass().getName(), e);
        }
        return 0;
    }

    private void executeUpdateStreak(UUID uuid, int current, int best) {
        try (PreparedStatement ps = connection.prepareStatement(
                "INSERT INTO killstreaks (uuid, current_streak, best_streak, last_update) " +
                        "VALUES (?, ?, ?, ?) " +
                        "ON CONFLICT(uuid) DO UPDATE SET " +
                        "current_streak = excluded.current_streak, " +
                        "best_streak = excluded.best_streak, " +
                        "last_update = excluded.last_update")) {

            ps.setString(1, uuid.toString());
            ps.setInt(2, current);
            ps.setInt(3, best);
            ps.setTimestamp(4, Timestamp.from(Instant.now()));
            ps.executeUpdate();

        } catch (SQLException e) {
            LoggerUtil.severe(this.getClass().getName(), e);
        }
    }

    // ======== Sync methods ========
    @Override
    public int getCurrentStreak(UUID uuid) {
        requireSync();
        return queryCurrentStreak(uuid);
    }

    @Override
    public int getBestStreak(UUID uuid) {
        requireSync();
        return queryBestStreak(uuid);
    }

    @Override
    public void addKill(UUID uuid) {
        requireSync();
        int current = queryCurrentStreak(uuid) + 1;
        int best = Math.max(current, queryBestStreak(uuid));
        executeUpdateStreak(uuid, current, best);
    }

    @Override
    public void resetStreak(UUID uuid) {
        requireSync();
        int current = 0;
        int best = queryBestStreak(uuid); // giữ nguyên best streak
        executeUpdateStreak(uuid, current, best);
    }

    @Override
    public void updateStreak(UUID uuid, int current, int best) {
        requireSync();
        executeUpdateStreak(uuid, current, best);
    }

    // ======== Async methods ========
    @Override
    public CompletableFuture<Integer> getCurrentStreakAsync(UUID uuid, Consumer<Throwable> errorHandler) {
        requireAsync();
        return CompletableFuture.supplyAsync(() -> queryCurrentStreak(uuid))
                .exceptionally(ex -> {
                    errorHandler.accept(ex);
                    return 0;
                });
    }

    @Override
    public CompletableFuture<Integer> getBestStreakAsync(UUID uuid, Consumer<Throwable> errorHandler) {
        requireAsync();
        return CompletableFuture.supplyAsync(() -> queryBestStreak(uuid))
                .exceptionally(ex -> {
                    errorHandler.accept(ex);
                    return 0;
                });
    }

    @Override
    public CompletableFuture<Void> addKillAsync(UUID uuid, Consumer<Throwable> errorHandler) {
        requireAsync();
        return CompletableFuture.runAsync(() -> {
            int current = queryCurrentStreak(uuid) + 1;
            int best = Math.max(current, queryBestStreak(uuid));
            executeUpdateStreak(uuid, current, best);
        }).exceptionally(ex -> {
            errorHandler.accept(ex);
            return null;
        });
    }

    @Override
    public CompletableFuture<Void> resetStreakAsync(UUID uuid, Consumer<Throwable> errorHandler) {
        requireAsync();
        return CompletableFuture.runAsync(() -> {
            int current = 0;
            int best = queryBestStreak(uuid);
            executeUpdateStreak(uuid, current, best);
        }).exceptionally(ex -> {
            errorHandler.accept(ex);
            return null;
        });
    }

    @Override
    public CompletableFuture<Void> updateStreakAsync(UUID uuid, int current, int best, Consumer<Throwable> errorHandler) {
        requireAsync();
        return CompletableFuture.runAsync(() -> executeUpdateStreak(uuid, current, best))
                .exceptionally(ex -> {
                    errorHandler.accept(ex);
                    return null;
                });
    }

    // ======== Close ========
    public void close() {
        try {
            if (connection != null && !connection.isClosed())
                connection.close();
        } catch (SQLException e) {
            LoggerUtil.severe(this.getClass().getName(), e);
        }
    }
}
