package uts.isd.model.dao;

import uts.isd.model.User;

import java.sql.*;

public class UserDBManager extends DBManager<User> {

    public UserDBManager(Connection connection) throws SQLException {
        super(connection);
    }

    // get user amount
    public int getUserCount() throws SQLException {
        ResultSet resultSet = statement.executeQuery("SELECT COUNT(*) FROM users");
        resultSet.next();
        return resultSet.getInt(1);
    }

    // CREATE: add user
    public User add(User user) throws SQLException {
        String sql = "INSERT INTO users (full_name, email, password, phone, role) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, user.getFullName());
            ps.setString(2, user.getEmail());
            ps.setString(3, user.getPassword());
            ps.setString(4, user.getPhone());
            ps.setString(5, user.getRole());
            ps.executeUpdate();

            ResultSet keys = ps.getGeneratedKeys();
            if (keys.next()) {
                user.setUserId(keys.getInt(1));
            }
            return user;
        }
    }

    // READ: get user from userid
    @Override
    public User get(User user) throws SQLException {
        String sql = "SELECT * FROM users WHERE user_id = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, user.getUserId());
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                User result = new User();
                result.setUserId(rs.getInt("user_id"));
                result.setFullName(rs.getString("full_name"));
                result.setEmail(rs.getString("email"));
                result.setPassword(rs.getString("password"));
                result.setPhone(rs.getString("phone"));
                result.setRole(rs.getString("role"));
                return result;
            }
            return null;
        }
    }

    // UPDATE: change user info
    public void update(User oldUser, User newUser) throws SQLException {
        String sql = "UPDATE users SET full_name = ?, email = ?, password = ?, phone = ?, role = ? WHERE user_id = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, newUser.getFullName());
            ps.setString(2, newUser.getEmail());
            ps.setString(3, newUser.getPassword());
            ps.setString(4, newUser.getPhone());
            ps.setString(5, newUser.getRole());
            ps.setInt(6, oldUser.getUserId());
            ps.executeUpdate();
        }
    }

    // DELETE: delete user
    public void delete(User user) throws SQLException {
        String sql = "DELETE FROM users WHERE user_id = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, user.getUserId());
            ps.executeUpdate();
        }
    }

    // UPDATE: update password
    public boolean updatePassword(int userId, String newPassword) throws SQLException {
        String sql = "UPDATE users SET password = ? WHERE user_id = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, newPassword);
            ps.setInt(2, userId);
            return ps.executeUpdate() > 0;
        }
    }
}