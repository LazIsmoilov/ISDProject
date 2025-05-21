package uts.isd.model.dao;

import uts.isd.model.User;

import java.sql.*;
import java.util.List;

public class UserDBManager {
    private Connection connection;
    private UserDAO userDAO;

    public UserDBManager(Connection conn) {
        this.connection = conn;
        this.userDAO = new UserDAO(conn); // Uses the constructor that accepts a connection
    }

    // ✅ Add a user (register)
    public void addUser(User user) throws SQLException {
        userDAO.registerUser(user);
    }

    // ✅ Find a user by email and password (login)
    public User findUser(String email, String password) throws SQLException {
        return userDAO.authenticate(email, password);
    }

    // ✅ Get total number of registered users (for index.jsp)
    public int getUserCount() throws SQLException {
        String query = "SELECT COUNT(*) FROM users";
        PreparedStatement stmt = connection.prepareStatement(query);
        ResultSet rs = stmt.executeQuery();

        int count = 0;
        if (rs.next()) {
            count = rs.getInt(1);
        }

        rs.close();
        stmt.close();
        return count;
    }


    public List<User> searchUsers(String name, String phone) {

        return List.of();
    }

    public void toggleActiveStatus(int userId) {

    }

    public List<User> getAllUsers() {

        return List.of();
    }

    public void delete(User user) {

    }
}
