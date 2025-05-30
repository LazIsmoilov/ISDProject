package uts.isd.test;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import uts.isd.model.User;
import uts.isd.model.User.UserType;
import uts.isd.model.dao.UserDBManager;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;

import static org.junit.Assert.*;

public class AdminTest {
    private UserDBManager dbManager;
    private Connection connection;

    @Before
    public void setUp() throws SQLException {
        // Connect to an in-memory SQLite database
        connection = DriverManager.getConnection("jdbc:sqlite::memory:");
        dbManager = new UserDBManager(connection);

        // Create USERS table matching your production schema
        String createTableSQL = "CREATE TABLE IF NOT EXISTS USERS (" +
                "userId INTEGER PRIMARY KEY AUTOINCREMENT," +
                "fullName VARCHAR(50) NOT NULL," +
                "email VARCHAR(100) NOT NULL UNIQUE," +
                "password VARCHAR(100) NOT NULL," +
                "dob DATE," +
                "gender VARCHAR(20) CHECK(gender IN ('Male', 'Female', 'Other'))," +
                "type VARCHAR(20) NOT NULL CHECK(type IN ('customer', 'staff', 'admin'))," +
                "phoneNumber VARCHAR(15)," +
                "isActive INTEGER DEFAULT 1 CHECK(isActive IN (0, 1)))";
        connection.createStatement().execute(createTableSQL);
    }

    @After
    public void tearDown() throws SQLException {
        // Clean up users between tests
        List<User> allUsers = dbManager.getAllUsers();
        for (User user : allUsers) {
            dbManager.delete(user);
        }
        if (connection != null) {
            connection.close();
        }
    }

    @Test
    public void testCreateCustomerUser() throws SQLException {
        User user = new User("Customer Name", "customer@example.com", "password123", "1990-01-01",
                "Male", "1234567890", "customer");
        User createdUser = dbManager.add(user);
        assertNotNull(createdUser);
        assertTrue(createdUser.getUserId() > 0);

        User found = dbManager.get(createdUser);
        assertNotNull(found);
        assertEquals("customer@example.com", found.getEmail());
        assertEquals(UserType.CUSTOMER, found.getType());
    }

    @Test
    public void testCreateStaffUser() throws SQLException {
        User user = new User("Staff Name", "staff@example.com", "password123", "1985-05-05",
                "Female", "0987654321", "staff");
        User createdUser = dbManager.add(user);
        assertNotNull(createdUser);
        assertTrue(createdUser.getUserId() > 0);

        User found = dbManager.get(createdUser);
        assertNotNull(found);
        assertEquals("staff@example.com", found.getEmail());
        assertEquals(UserType.STAFF, found.getType());
    }

    @Test
    public void testAdminEditUser() throws SQLException {
        User user = new User("Original Name", "original@example.com", "password123", "1990-01-01",
                "Male", "1234567890", "customer");
        User createdUser = dbManager.add(user);

        User updatedUser = new User(createdUser.getUserId(), "Updated Name", "updated@example.com",
                "newpassword123", "1990-01-01", "Male", "1234567890", "customer", true);
        dbManager.update(createdUser, updatedUser);

        User found = dbManager.get(createdUser);
        assertEquals("updated@example.com", found.getEmail());
        assertEquals("Updated Name", found.getFullName());
    }

    @Test
    public void testToggleActiveStatus() throws SQLException {
        User user = new User("User Name", "user@example.com", "password123", "1990-01-01",
                "Male", "1234567890", "customer");
        User createdUser = dbManager.add(user);

        boolean originalStatus = createdUser.getIsActive();
        User toggledUser = dbManager.toggleActiveStatus(createdUser.getUserId());
        assertEquals(!originalStatus, toggledUser.getIsActive());

        // Toggle again
        toggledUser = dbManager.toggleActiveStatus(createdUser.getUserId());
        assertEquals(originalStatus, toggledUser.getIsActive());
    }

    @Test
    public void testDeleteUser() throws SQLException {
        User user = new User("User To Delete", "delete@example.com", "password123", "1990-01-01",
                "Female", "1234567890", "staff");
        User createdUser = dbManager.add(user);

        dbManager.delete(createdUser);
        User found = dbManager.get(createdUser);
        assertNull(found);
    }
}
