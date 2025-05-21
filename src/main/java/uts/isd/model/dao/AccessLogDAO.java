package uts.isd.model.dao;

import uts.isd.model.AccessLog;
import uts.isd.model.dao.DBConnector;

import java.util.*;
import java.sql.*;
import java.util.Date;

public class AccessLogDAO {
    private Connection con;

    public AccessLogDAO() {
        DBConnector connector = new DBConnector();
        con = connector.getConnection();
    }


    //    login time
    public boolean logLogin(int userId) throws SQLException {
        String sql = "INSERT INTO access_logs (user_id, login_time) VALUES (?, ?)";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ps.setTimestamp(2, new Timestamp(System.currentTimeMillis()));
            return ps.executeUpdate() > 0;
        }
    }


    //    logout time  when logout, change the last logout time is null to current time
    public boolean logLogout(int userId) throws SQLException {
        String sql = "UPDATE access_logs SET logout_time = ? "
                + "WHERE user_id = ? AND logout_time IS NULL";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setTimestamp(1, new Timestamp(System.currentTimeMillis()));
            ps.setInt(2, userId);
            return ps.executeUpdate() > 0;
        }
    }


    //select user log by sql
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

                // set time from 0 as current day
                cal.set(Calendar.HOUR_OF_DAY, 0);
                cal.set(Calendar.MINUTE, 0);
                cal.set(Calendar.SECOND, 0);
                cal.set(Calendar.MILLISECOND, 0);
                long startMillis = cal.getTimeInMillis();

                // second day start
                cal.add(Calendar.DATE, 1);
                long endMillis = cal.getTimeInMillis();

                stmt.setLong(2, startMillis);
                stmt.setLong(3, endMillis);
            }

//            go through every record
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                AccessLog log = new AccessLog();
                log.setLogId(rs.getInt("log_id"));
                log.setUserId(userId);

//change to java time
                long loginMillis = rs.getLong("login_time");
                long logoutMillis = rs.getLong("logout_time");

                log.setLoginTime(loginMillis > 0 ? new Date(loginMillis) : null);
                log.setLogoutTime(logoutMillis > 0 ? new Date(logoutMillis) : null);

                logs.add(log);
            }
        }

        return logs;
    }
}