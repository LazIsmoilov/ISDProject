package uts.isd.model.dao;

import uts.isd.model.User;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDBManager extends DBManager<User> {

    public UserDBManager(Connection connection) throws SQLException {
        super(connection);
    }

    // Get total user count
    public int getUserCount() throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement("SELECT COUNT(*) FROM users");
        ResultSet resultSet = preparedStatement.executeQuery();
        resultSet.next();
        return resultSet.getInt(1);
    }

    // CREATE: Add a new user
    public User add(User user) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement(
                "INSERT INTO users (fullName, email, password, dob, gender, phoneNumber, type, isActive) VALUES (?, ?, ?, ?, ?, ?, ?, ?)"
        );
        preparedStatement.setString(1, user.getFullName());
        preparedStatement.setString(2, user.getEmail());
        preparedStatement.setString(3, user.getPassword());
        preparedStatement.setString(4, user.getDob());
        preparedStatement.setString(5, user.getGender());
        preparedStatement.setString(6, user.getPhone());
        preparedStatement.setString(7, user.getRole().toLowerCase());
        preparedStatement.setInt(8, user.getIsActive() ? 1 : 0);
        preparedStatement.executeUpdate();

        preparedStatement = connection.prepareStatement("SELECT MAX(userId) FROM users");
        ResultSet resultSet = preparedStatement.executeQuery();
        if (resultSet.next()) {
            user.setUserId(resultSet.getInt(1));
        }
        return user;
    }

    public User get(User user) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM users WHERE userId = ?");
        preparedStatement.setInt(1, user.getUserId());
        ResultSet resultSet = preparedStatement.executeQuery();
        if (resultSet.next()) {
            return mapResultSetToUser(resultSet);
        }
        return null;
    }

    // READ: Get all users
    public List<User> getAllUsers() throws SQLException {
        List<User> users = new ArrayList<>();
        PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM users");
        ResultSet resultSet = preparedStatement.executeQuery();
        while (resultSet.next()) {
            users.add(mapResultSetToUser(resultSet));
        }
        return users;
    }

    // READ: Search users by name and phone number
    public List<User> searchUsers(String name, String phoneNumber) throws SQLException {
        List<User> users = new ArrayList<>();
        StringBuilder query = new StringBuilder("SELECT * FROM users WHERE 1=1");
        List<String> params = new ArrayList<>();

        if (name != null && !name.isEmpty()) {
            query.append(" AND fullName LIKE ?");
            params.add("%" + name + "%");
        }
        if (phoneNumber != null && !phoneNumber.isEmpty()) {
            query.append(" AND phoneNumber LIKE ?");
            params.add("%" + phoneNumber + "%");
        }

        PreparedStatement preparedStatement = connection.prepareStatement(query.toString());
        for (int i = 0; i < params.size(); i++) {
            preparedStatement.setString(i + 1, params.get(i));
        }

        ResultSet resultSet = preparedStatement.executeQuery();
        while (resultSet.next()) {
            users.add(mapResultSetToUser(resultSet));
        }
        return users;
    }

    // UPDATE: Update user details
    public void update(User oldUser, User newUser) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement(
                "UPDATE users SET fullName = ?, email = ?, password = ?, dob = ?, gender = ?, phoneNumber = ?, type = ?, isActive = ? WHERE userId = ?"
        );
        preparedStatement.setString(1, newUser.getFullName());
        preparedStatement.setString(2, newUser.getEmail());
        preparedStatement.setString(3, newUser.getPassword());
        preparedStatement.setString(4, newUser.getDob());
        preparedStatement.setString(5, newUser.getGender());
        preparedStatement.setString(6, newUser.getPhone());
        preparedStatement.setString(7, newUser.getRole().toLowerCase());
        preparedStatement.setInt(8, newUser.getIsActive() ? 1 : 0);
        preparedStatement.setInt(9, oldUser.getUserId());
        preparedStatement.executeUpdate();
    }

    // UPDATE: Toggle active status (Admin feature)
    public User toggleActiveStatus(int userId) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement(
                "UPDATE users SET isActive = CASE WHEN isActive = 1 THEN 0 ELSE 1 END WHERE userId = ?"
        );
        preparedStatement.setInt(1, userId);
        preparedStatement.executeUpdate();

        User user = new User();
        user.setUserId(userId);
        return get(user);
    }

    // DELETE: Delete a user
    public void delete(User user) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM users WHERE userId = ?");
        preparedStatement.setInt(1, user.getUserId());
        preparedStatement.executeUpdate();
    }

    // Utility method to map ResultSet to User object
    private User mapResultSetToUser(ResultSet resultSet) throws SQLException {
        return new User(
                resultSet.getInt("userId"),
                resultSet.getString("fullName"),
                resultSet.getString("email"),
                resultSet.getString("password"),
                resultSet.getString("dob"),
                resultSet.getString("gender"),
                resultSet.getString("phoneNumber"),
                resultSet.getString("type"),
                resultSet.getInt("isActive") == 1
        );
    }

    // Find user by email and password for login
    public User find(String email, String password) throws SQLException {
        String sql = "SELECT * FROM users WHERE email = ? AND password = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setString(1, email);
        preparedStatement.setString(2, password);
        ResultSet resultSet = preparedStatement.executeQuery();
        if (resultSet.next()) {
            return mapResultSetToUser(resultSet);
        }
        return null;  // no matching user found
    }

    public void updatePassword(int userId, String newPassword) throws SQLException {
        String sql = "UPDATE users SET password = ? WHERE userId = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, newPassword);
            stmt.setInt(2, userId);
            int rowsUpdated = stmt.executeUpdate();

            if (rowsUpdated == 0) {
                throw new SQLException("No user found with id " + userId);
            }
        }
    }

    // READ: Get a user by ID
    public User getById(int id) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM users WHERE userId = ?");
        preparedStatement.setInt(1, id);
        ResultSet resultSet = preparedStatement.executeQuery();
        if (resultSet.next()) {
            return mapResultSetToUser(resultSet);
        }
        return null;
    }

}