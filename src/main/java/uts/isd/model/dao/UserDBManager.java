package uts.isd.model.dao;

import uts.isd.model.User;
import uts.isd.model.User.UserType;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UserDBManager extends DBManager<User> {

    public UserDBManager(Connection connection) throws SQLException {
        super(connection);
    }

    // Get total user count
    public int getUserCount() throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement("SELECT COUNT(*) FROM USERS");
        ResultSet resultSet = preparedStatement.executeQuery();
        resultSet.next();
        return resultSet.getInt(1);
    }

    // CREATE: Add a new user
    public User add(User user) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement(
                "INSERT INTO USERS (Name, Email, Password, DOB, Gender, Type, PhoneNumber, IsActive) VALUES (?, ?, ?, ?, ?, ?, ?, ?)"
        );
        preparedStatement.setString(1, user.getName());
        preparedStatement.setString(2, user.getEmail());
        preparedStatement.setString(3, user.getPassword());
        preparedStatement.setString(4, user.getDob());
        preparedStatement.setString(5, user.getGender());
        preparedStatement.setString(6, user.getType().name());
        preparedStatement.setString(7, user.getPhoneNumber());
        preparedStatement.setInt(8, user.getIsActive() ? 1 : 0);
        preparedStatement.executeUpdate();

        // Retrieve the auto-generated ID
        preparedStatement = connection.prepareStatement("SELECT MAX(Id) FROM USERS");
        ResultSet resultSet = preparedStatement.executeQuery();
        resultSet.next();
        int userId = resultSet.getInt(1);
        user.setId(userId);
        return user;
    }

    // READ: Get a user by ID
    public User get(User user) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement(
                "SELECT * FROM USERS WHERE Id = ?"
        );
        preparedStatement.setInt(1, user.getId());
        ResultSet resultSet = preparedStatement.executeQuery();
        if (resultSet.next()) {
            return new User(
                    resultSet.getInt("Id"),
                    resultSet.getString("Name"),
                    resultSet.getString("Email"),
                    resultSet.getString("Password"),
                    resultSet.getString("DOB"),
                    resultSet.getString("Gender"),
                    UserType.valueOf(resultSet.getString("Type")),
                    resultSet.getString("PhoneNumber"),
                    resultSet.getInt("IsActive") == 1
            );
        }
        return null;
    }

    // READ: Get all users
    public List<User> getAllUsers() throws SQLException {
        List<User> users = new ArrayList<>();
        PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM USERS");
        ResultSet resultSet = preparedStatement.executeQuery();
        while (resultSet.next()) {
            users.add(new User(
                    resultSet.getInt("Id"),
                    resultSet.getString("Name"),
                    resultSet.getString("Email"),
                    resultSet.getString("Password"),
                    resultSet.getString("DOB"),
                    resultSet.getString("Gender"),
                    UserType.valueOf(resultSet.getString("Type")),
                    resultSet.getString("PhoneNumber"),
                    resultSet.getInt("IsActive") == 1
            ));
        }
        return users;
    }

    // READ: Search users by name and phone number
    public List<User> searchUsers(String name, String phoneNumber) throws SQLException {
        List<User> users = new ArrayList<>();
        StringBuilder query = new StringBuilder("SELECT * FROM USERS WHERE 1=1");
        List<String> params = new ArrayList<>();
        if (name != null && !name.isEmpty()) {
            query.append(" AND Name LIKE ?");
            params.add("%" + name + "%");
        }
        if (phoneNumber != null && !phoneNumber.isEmpty()) {
            query.append(" AND PhoneNumber LIKE ?");
            params.add("%" + phoneNumber + "%");
        }
        PreparedStatement preparedStatement = connection.prepareStatement(query.toString());
        for (int i = 0; i < params.size(); i++) {
            preparedStatement.setString(i + 1, params.get(i));
        }
        ResultSet resultSet = preparedStatement.executeQuery();
        while (resultSet.next()) {
            users.add(new User(
                    resultSet.getInt("Id"),
                    resultSet.getString("Name"),
                    resultSet.getString("Email"),
                    resultSet.getString("Password"),
                    resultSet.getString("DOB"),
                    resultSet.getString("Gender"),
                    UserType.valueOf(resultSet.getString("Type")),
                    resultSet.getString("PhoneNumber"),
                    resultSet.getInt("IsActive") == 1
            ));
        }
        return users;
    }

    // UPDATE: Update user details
    public void update(User oldUser, User newUser) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement(
                "UPDATE USERS SET Name = ?, Email = ?, Password = ?, DOB = ?, Gender = ?, Type = ?, PhoneNumber = ?, IsActive = ? WHERE Id = ?"
        );
        preparedStatement.setString(1, newUser.getName());
        preparedStatement.setString(2, newUser.getEmail());
        preparedStatement.setString(3, newUser.getPassword());
        preparedStatement.setString(4, newUser.getDob());
        preparedStatement.setString(5, newUser.getGender());
        preparedStatement.setString(6, newUser.getType().name());
        preparedStatement.setString(7, newUser.getPhoneNumber());
        preparedStatement.setInt(8, newUser.getIsActive() ? 1 : 0);
        preparedStatement.setInt(9, oldUser.getId());
        preparedStatement.executeUpdate();
    }

    // UPDATE: Toggle active status
    public User toggleActiveStatus(int userId) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement(
                "UPDATE USERS SET IsActive = CASE WHEN IsActive = 1 THEN 0 ELSE 1 END WHERE Id = ?"
        );
        preparedStatement.setInt(1, userId);
        preparedStatement.executeUpdate();
        User user = new User();
        user.setId(userId);
        return get(user); // Fetch updated user
    }

    // DELETE: Delete a user
    public void delete(User user) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement(
                "DELETE FROM USERS WHERE Id = ?"
        );
        preparedStatement.setInt(1, user.getId());
        preparedStatement.executeUpdate();
    }

    // READ: Find user by email and password
    public User find(String email, String password) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement(
                "SELECT * FROM USERS WHERE Email = ? AND Password = ?"
        );
        preparedStatement.setString(1, email);
        preparedStatement.setString(2, password);
        ResultSet resultSet = preparedStatement.executeQuery();
        if (resultSet.next()) {
            return new User(
                    resultSet.getInt("Id"),
                    resultSet.getString("Name"),
                    resultSet.getString("Email"),
                    resultSet.getString("Password"),
                    resultSet.getString("DOB"),
                    resultSet.getString("Gender"),
                    UserType.valueOf(resultSet.getString("Type")),
                    resultSet.getString("PhoneNumber"),
                    resultSet.getInt("IsActive") == 1
            );
        }
        return null;
    }
}
