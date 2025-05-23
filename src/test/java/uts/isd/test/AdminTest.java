package uts.isd.test;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import uts.isd.model.User;
import uts.isd.model.User.UserType;
import uts.isd.model.dao.UserDBManager;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class AdminTest {
    private Connection connection;
    private UserDBManager dbManager;

    @Before
    public void setUp() throws SQLException {
        String dbPath = new File("test_iotBayDatabase.db").getAbsolutePath();
        connection = DriverManager.getConnection("jdbc:sqlite:" + dbPath);
        dbManager = new UserDBManager(connection);
        // Create users table if not exists
        connection.createStatement().execute(
                "CREATE TABLE IF NOT EXISTS users (" +
                        "Id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        "Name TEXT NOT NULL, " +
                        "Email TEXT NOT NULL, " +
                        "Password TEXT NOT NULL, " +
                        "DOB TEXT, " +
                        "Gender TEXT, " +
                        "Type TEXT NOT NULL, " +
                        "PhoneNumber TEXT, " +
                        "IsActive INTEGER NOT NULL)"
        );
        // Clear users table
        connection.createStatement().execute("DELETE FROM users");
    }

    @After
    public void tearDown() throws SQLException {
        if (connection != null) {
            connection.close();
        }
    }

    @Test
    public void testAdminLogin() throws SQLException {
        User admin = new User("Admin", "admin@system.com", "admin123", null, null, UserType.ADMIN, null);
        admin.setIsActive(true);
        dbManager.add(admin);

        User found = dbManager.find("admin@system.com", "admin123");
        assertNotNull(found);
        assertEquals(UserType.ADMIN, found.getType());
        assertTrue(found.getIsActive());
    }

    @Test
    public void testCreateCustomerUser() throws SQLException {
        User user = new User("John Doe", "customer@example.com", "pass1234", "1990-01-01", "Male", UserType.CUSTOMER, "1234567890");
        user.setIsActive(true);
        dbManager.add(user);

        User found = dbManager.getById(user.getId());
        assertNotNull(found);
        assertEquals("customer@example.com", found.getEmail());
        assertEquals(UserType.CUSTOMER, found.getType());
    }

    @Test
    public void testCreateStaffUser() throws SQLException {
        User user = new User("Jane Smith", "staff@example.com", "pass1234", "1985-02-02", "Female", UserType.STAFF, "9876543210");
        user.setIsActive(true);
        dbManager.add(user);

        User found = dbManager.getById(user.getId());
        assertNotNull(found);
        assertEquals("staff@example.com", found.getEmail());
        assertEquals(UserType.STAFF, found.getType());
    }

    @Test
    public void testAdminEditUser() throws SQLException {
        User user = new User("John Doe", "customer@example.com", "pass1234", "1990-01-01", "Male", UserType.CUSTOMER, "1234567890");
        user.setIsActive(true);
        dbManager.add(user);

        User updatedUser = new User(user.getId(), "John Updated", "updated@example.com", "pass1234", "1990-01-01", "Male", UserType.CUSTOMER, "1234567890", true);
        dbManager.update(user, updatedUser);

        User found = dbManager.getById(user.getId());
        assertEquals("John Updated", found.getName());
        assertEquals("updated@example.com", found.getEmail());
    }

    @Test
    public void testSearchUsersByName() throws SQLException {
        User user = new User("John Doe", "customer@example.com", "pass1234", "1990-01-01", "Male", UserType.CUSTOMER, "1234567890");
        dbManager.add(user);

        List<User> users = dbManager.searchUsers("John", null);
        assertFalse(users.isEmpty());
        assertEquals("John Doe", users.get(0).getName());
    }

    @Test
    public void testToggleActiveStatus() throws SQLException {
        User user = new User("John Doe", "customer@example.com", "pass1234", "1990-01-01", "Male", UserType.CUSTOMER, "1234567890");
        user.setIsActive(true);
        dbManager.add(user);

        dbManager.toggleActiveStatus(user.getId());
        User found = dbManager.getById(user.getId());
        assertFalse(found.getIsActive());
    }

    @Test
    public void testDeleteUser() throws SQLException {
        User user = new User("John Doe", "customer@example.com", "pass1234", "1990-01-01", "Male", UserType.CUSTOMER, "1234567890");
        dbManager.add(user);

        dbManager.delete(user);
        User found = dbManager.getById(user.getId());
        assertNull(found);
    }
}