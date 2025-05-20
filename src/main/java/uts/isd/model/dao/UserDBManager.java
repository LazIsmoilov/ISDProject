package uts.isd.model.dao;

import uts.isd.model.User;

import java.sql.*;

public class UserDBManager {
    private Connection conn;
    private UserDAO userDAO;

    public UserDBManager(Connection conn) {
        this.conn = conn;
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
        PreparedStatement stmt = conn.prepareStatement(query);
        ResultSet rs = stmt.executeQuery();

        int count = 0;
        if (rs.next()) {
            count = rs.getInt(1);
        }

        rs.close();
        stmt.close();
        return count;
    }
}
