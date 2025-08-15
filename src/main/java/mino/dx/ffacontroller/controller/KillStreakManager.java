package mino.dx.ffacontroller.controller;

import mino.dx.ffacontroller.FFAController;
import mino.dx.ffacontroller.api.interfaces.IStreak;
import mino.dx.ffacontroller.utils.ExceptionUtil;

import java.io.File;
import java.sql.*;
import java.time.Instant;
import java.util.UUID;

public class KillStreakManager implements IStreak {

    private final File databaseFile;
    private Connection connection;

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
            ExceptionUtil.throwException(this.getClass().getName(), "Lỗi khi khởi tạo SQLite", e);
        }
    }

    @Override
    public int getCurrentStreak(UUID uuid) {
        try (PreparedStatement ps = connection.prepareStatement(
                "SELECT current_streak FROM killstreaks WHERE uuid = ?")) {
            ps.setString(1, uuid.toString());
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt("current_streak");
            }
        } catch (SQLException e) {
            ExceptionUtil.throwException(this.getClass().getName(), e);
        }
        return 0;
    }

    @Override
    public int getBestStreak(UUID uuid) {
        try (PreparedStatement ps = connection.prepareStatement(
                "SELECT best_streak FROM killstreaks WHERE uuid = ?")) {
            ps.setString(1, uuid.toString());
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt("best_streak");
            }
        } catch (SQLException e) {
            ExceptionUtil.throwException(this.getClass().getName(), e);
        }
        return 0;
    }

    @Override
    public void addKill(UUID uuid) {
        int current = getCurrentStreak(uuid) + 1;
        int best = Math.max(current, getBestStreak(uuid));
        updateStreak(uuid, current, best);
    }

    @Override
    public void resetStreak(UUID uuid) {
        int current = 0;
        int best = getBestStreak(uuid); // giữ nguyên best streak
        updateStreak(uuid, current, best);
    }

    @Override
    public void updateStreak(UUID uuid, int current, int best) {
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
            ExceptionUtil.throwException(this.getClass().getName(), e);
        }
    }

    public void close() {
        try {
            if (connection != null && !connection.isClosed())
                connection.close();
        } catch (SQLException e) {
            ExceptionUtil.throwException(this.getClass().getName(), e);
        }
    }
}
