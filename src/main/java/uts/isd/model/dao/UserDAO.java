package uts.isd.model.dao;

import uts.isd.model.User;

import java.sql.*;

public class UserDAO {
    private Connection con;

    // Constructor that receives a connection (used by UserDBManager)
    public UserDAO(Connection conn) {
        this.con = conn;
    }

    // Optional legacy constructor (if you use it somewhere else)
    public UserDAO() {
        DBConnector connector = new DBConnector();
        con = connector.getConnection();
    }

    /** Register a new user */
    public boolean registerUser(User user) throws SQLException {
        String sql = "INSERT INTO users (fullName, email, password, phone, role) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, user.getFullName());
            ps.setString(2, user.getEmail());
            ps.setString(3, user.getPassword());
            ps.setString(4, user.getPhone());
            ps.setString(5, user.getRole());
            return ps.executeUpdate() > 0;
        }
    }

    /** Authenticate user for login */
    public User authenticate(String email, String password) throws SQLException {
        String sql = "SELECT * FROM users WHERE email = ? AND password = ?";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, email);
            ps.setString(2, password);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                User user = new User();
                user.setUserId(rs.getInt("id"));  // ✅ Fix: use "id" not "user_id"
                user.setFullName(rs.getString("fullName"));
                user.setEmail(rs.getString("email"));
                user.setPassword(rs.getString("password"));
                user.setPhone(rs.getString("phone"));
                user.setRole(rs.getString("role"));
                return user;
            }

            return null;
        }
    }

    /** Update user details */
    public boolean updateUser(User user) throws SQLException {
        String sql = "UPDATE users SET fullName = ?, phone = ? WHERE id = ?";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, user.getFullName());
            ps.setString(2, user.getPhone());
            ps.setInt(3, user.getUserId());
            return ps.executeUpdate() > 0;
        }
    }

    /** Delete a user */
    public boolean deleteUser(int userId) throws SQLException {
        String sql = "DELETE FROM users WHERE id = ?";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, userId);
            return ps.executeUpdate() > 0;
        }
    }

    /** Update password */
    public boolean updatePassword(int userId, String newPassword) throws SQLException {
        String sql = "UPDATE users SET password = ? WHERE id = ?";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, newPassword);
            ps.setInt(2, userId);
            return ps.executeUpdate() > 0;
        }
    }
}
