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

    /**
     * login time
     */
    public boolean logLogin(int userId) throws SQLException {
        String sql = "INSERT INTO access_logs (user_id, login_time) VALUES (?, datetime('now', 'localtime'))";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, userId);
            return ps.executeUpdate() > 0;
        }
    }

    /**
     * logout time
     */
    public boolean logLogout(int userId) throws SQLException {
        String sql = "UPDATE access_logs SET logout_time = datetime('now', 'localtime') "
                + "WHERE user_id = ? AND logout_time IS NULL ";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, userId);
            return ps.executeUpdate() > 0;
        }
    }

    /**
     * get access log
     */
    public List<AccessLog> getAccessLogs(int userId, Date startDate, Date endDate)
            throws SQLException {

        List<AccessLog> logs = new ArrayList<>();
        String sql = "SELECT * FROM access_logs WHERE user_id = ? "
                + "AND login_time BETWEEN ? AND ? "
                + "ORDER BY login_time DESC";

        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ps.setTimestamp(2, new Timestamp(startDate.getTime()));
            ps.setTimestamp(3, new Timestamp(endDate.getTime()));

            ResultSet rs = ps.executeQuery();
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

    public List<AccessLog> getLogsByUser(int userId, Date filterDate) throws SQLException {
        List<AccessLog> logs = new ArrayList<>();
        PreparedStatement stmt = null;
        ResultSet rs = null;



        String sql = "SELECT log_id, login_time, logout_time FROM access_logs "
                + "WHERE user_id = ?";

        if (filterDate != null) {
            sql += " AND login_time >= ? AND login_time < ?";
        }

        sql += " ORDER BY login_time DESC";

        stmt = con.prepareStatement(sql);
        stmt.setInt(1, userId);


        if (filterDate != null) {
            Calendar cal = Calendar.getInstance();
            cal.setTime(filterDate);

            cal.set(Calendar.HOUR_OF_DAY, 0);
            cal.set(Calendar.MINUTE, 0);
            cal.set(Calendar.SECOND, 0);
            Timestamp startDate = new Timestamp(cal.getTimeInMillis());


            cal.add(Calendar.DATE, 1);
            Timestamp endDate = new Timestamp(cal.getTimeInMillis());

            stmt.setTimestamp(2, startDate);
            stmt.setTimestamp(3, endDate);
        }

        rs = stmt.executeQuery();

        while (rs.next()) {
            AccessLog log = new AccessLog();
            log.setLogId(rs.getInt("log_id"));
            log.setUserId(userId);

            Timestamp loginTs = rs.getTimestamp("login_time");
            log.setLoginTime(loginTs != null ? new Date(loginTs.getTime()) : null);

            Timestamp logoutTs = rs.getTimestamp("logout_time");
            log.setLogoutTime(logoutTs != null ? new Date(logoutTs.getTime()) : null);

            logs.add(log);
        }

        return logs;
    }
}
