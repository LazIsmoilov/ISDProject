package uts.isd.model.dao;

import uts.isd.model.AccessLog;

import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Date;

public class AccessLogDBManager {
    private Connection con;

    public AccessLogDBManager(Connection connection) {
        this.con = connection;
    }

    // login time - using DATETIME string
    public boolean logLogin(int userId) throws SQLException {
        String sql = "INSERT INTO access_logs (user_id, login_time) VALUES (?, datetime('now', 'localtime'))";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, userId);
            return ps.executeUpdate() > 0;
        }
    }

    // logout time - using DATETIME string
    public boolean logLogout(int userId) throws SQLException {
        String sql = "UPDATE access_logs SET logout_time = datetime('now', 'localtime') WHERE user_id = ? AND logout_time IS NULL";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, userId);
            return ps.executeUpdate() > 0;
        }
    }

    // get logs by user ID and optional date filter
    public List<AccessLog> getLogsByUser(int userId, Date filterDate) throws SQLException {
        List<AccessLog> logs = new ArrayList<>();

        String sql = "SELECT log_id, login_time, logout_time FROM access_logs WHERE user_id = ?";
        if (filterDate != null) {
            sql += " AND login_time >= ? AND login_time < ?";
        }
        sql += " ORDER BY login_time DESC";

        try (PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setInt(1, userId);

            if (filterDate != null) {
                Calendar cal = Calendar.getInstance();
                cal.setTime(filterDate);
                cal.set(Calendar.HOUR_OF_DAY, 0);
                cal.set(Calendar.MINUTE, 0);
                cal.set(Calendar.SECOND, 0);
                cal.set(Calendar.MILLISECOND, 0);
                Date start = cal.getTime();

                cal.add(Calendar.DATE, 1);
                Date end = cal.getTime();


                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                stmt.setString(2, sdf.format(start));
                stmt.setString(3, sdf.format(end));
            }

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                AccessLog log = new AccessLog();
                log.setLogId(rs.getInt("log_id"));
                log.setUserId(userId);
                log.setLoginTime(rs.getTimestamp("login_time"));
                log.setLogoutTime(rs.getTimestamp("logout_time"));
                logs.add(log);
            }
        }
        return logs;
    }
}
