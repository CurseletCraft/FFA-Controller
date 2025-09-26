package mino.dx.ffacontroller.controller;

import mino.dx.ffacontroller.FFAController;
import mino.dx.ffacontroller.models.ReportModel;
import mino.dx.ffacontroller.models.ReportStatus;
import mino.dx.ffacontroller.utils.ExceptionUtil;

import java.io.File;
import java.sql.*;
import java.util.*;
import java.util.logging.Level;

public class ReportManager {

    @SuppressWarnings("all")
    private final FFAController plugin;
    private final File file;
    private Connection connection;
    private static final String className = ReportManager.class.getName();

    public ReportManager(FFAController plugin) {
        this.plugin = plugin;
        this.file = new File(plugin.getDataFolder(), "reports.db");
        initDatabase();
    }

    @SuppressWarnings({"ResultOfMethodCallIgnored","DuplicatedCode"})
    private void initDatabase() {
        try {
            if (!file.exists()) {
                file.getParentFile().mkdirs();
                file.createNewFile();
            }

            connection = DriverManager.getConnection("jdbc:sqlite:" + file.getAbsolutePath());

            try (Statement stmt = connection.createStatement()) {
                stmt.executeUpdate(
                        "CREATE TABLE IF NOT EXISTS reports (" +
                                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                                "uuid_reporter TEXT NOT NULL," +
                                "uuid_member TEXT NOT NULL," +
                                "reason TEXT NOT NULL," +
                                "status TEXT DEFAULT 'PENDING'," +
                                "last_added DATETIME DEFAULT CURRENT_TIMESTAMP," +
                                "last_processed DATETIME" +
                                ");"
                );
            }

        } catch (Exception e) {
            ExceptionUtil.throwException(className, "Lỗi khi khởi tạo SQLite", e);
        }
    }

    public void addReport(UUID reporter, UUID member, String reason) {
        String sql = "INSERT INTO reports (uuid_reporter, uuid_member, reason) VALUES (?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, reporter.toString());
            stmt.setString(2, member.toString());
            stmt.setString(3, reason);
            stmt.executeUpdate();
        } catch (SQLException e) {
            ExceptionUtil.throwException(className, "Error while adding report", e);
        }
    }

    public void removeReport(int id) {
        String sql = "DELETE FROM reports WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            ExceptionUtil.throwException(className, "Error while removing report", e);
        }
    }

    private ReportModel mapResult(ResultSet rs) throws SQLException {
        return new ReportModel(
                rs.getInt("id"),
                UUID.fromString(rs.getString("uuid_reporter")),
                UUID.fromString(rs.getString("uuid_member")),
                rs.getString("reason"),
                ReportStatus.valueOf(rs.getString("status")),
                rs.getTimestamp("last_added").toLocalDateTime(),
                rs.getTimestamp("last_processed") != null ? rs.getTimestamp("last_processed").toLocalDateTime() : null
        );
    }

    public Optional<ReportModel> getReport(int id) {
        String sql = "SELECT * FROM reports WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) return Optional.of(mapResult(rs));
            }
        } catch (SQLException e) {
            ExceptionUtil.throwException(className, "Error while fetching report", e);
        }
        return Optional.empty();
    }

    public List<ReportModel> getReportsByStatus(ReportStatus status) {
        List<ReportModel> list = new ArrayList<>();
        String sql = "SELECT * FROM reports WHERE status = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, status.name());
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) list.add(mapResult(rs));
            }
        } catch (SQLException e) {
            ExceptionUtil.throwException(className, "Error while fetching reports", e);
        }
        return list;
    }

    public List<ReportModel> getReportsByStatus(ReportStatus status, int page, int pageSize) {
        List<ReportModel> list = new ArrayList<>();
        String sql = "SELECT * FROM reports WHERE status = ? ORDER BY id DESC LIMIT ? OFFSET ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, status.name());
            stmt.setInt(2, pageSize);
            stmt.setInt(3, (page - 1) * pageSize);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    list.add(mapResult(rs));
                }
            }
        } catch (SQLException e) {
            ExceptionUtil.throwException(className, "Error while fetching paged reports by status", e);
        }
        return list;
    }

    public List<ReportModel> getAllReports() {
        List<ReportModel> list = new ArrayList<>();
        String sql = "SELECT * FROM reports";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) list.add(mapResult(rs));
        } catch (SQLException e) {
            ExceptionUtil.throwException(className, "Error while fetching all reports", e);
        }
        return list;
    }

    public List<ReportModel> getAllReports(int page, int pageSize) {
        List<ReportModel> list = new ArrayList<>();
        String sql = "SELECT * FROM reports ORDER BY id DESC LIMIT ? OFFSET ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, pageSize);
            stmt.setInt(2, (page - 1) * pageSize);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    list.add(mapResult(rs));
                }
            }
        } catch (SQLException e) {
            ExceptionUtil.throwException(className, "Error while fetching paged reports", e);
        }
        return list;
    }

    public void updateReportStatus(int id, ReportStatus status) {
        String sql = "UPDATE reports SET status = ?, last_processed = CURRENT_TIMESTAMP WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, status.name());
            stmt.setInt(2, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            ExceptionUtil.throwException(className, "Error while updating report status", e);
        }
    }

    public void close() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
                plugin.getLogger().info("ReportManager connection closed.");
            }
        } catch (SQLException e) {
            plugin.getLogger().log(Level.SEVERE, "Failed to close SQLite connection.", e);
        }
    }
}
