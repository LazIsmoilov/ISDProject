package uts.isd.test;

import org.junit.Test;
import uts.isd.model.User;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertThrows;

public class MyTest {
    @Test
    public void testExample() {
        assertTrue("JUnit test is running!", true);
    }

    @Test
    public void testUserCreation() {
        User user = new User("Alice Smith", "alice@example.com", "securePass123", "1990-01-01",
                "Female", "1234567890", "CUSTOMER");
        assertEquals("Alice Smith", user.getFullName());
        assertEquals("alice@example.com", user.getEmail());
        assertEquals("securePass123", user.getPassword());
        assertEquals("1990-01-01", user.getDob());
        assertEquals("Female", user.getGender());
        assertEquals("1234567890", user.getPhone());
        assertEquals("CUSTOMER", user.getRole());
        assertTrue(user.getIsActive());
    }

    @Test
    public void testSetters() {
        User user = new User("Alice Smith", "alice@example.com", "securePass123", "1990-01-01",
                "Female", "1234567890", "CUSTOMER");
        user.setFullName("Bob Johnson");
        user.setEmail("bob@example.com");
        user.setPassword("newPass123");
        user.setDob("1985-05-05");
        user.setGender("Male");
        user.setPhone("0987654321");
        user.setRole("ADMIN");

        assertEquals("Bob Johnson", user.getFullName());
        assertEquals("bob@example.com", user.getEmail());
        assertEquals("newPass123", user.getPassword());
        assertEquals("1985-05-05", user.getDob());
        assertEquals("Male", user.getGender());
        assertEquals("0987654321", user.getPhone());
        assertEquals("ADMIN", user.getRole());
    }

    @Test
    public void testInvalidEmail() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            new User("Alice Smith", "invalid-email", "securePass123", "1990-01-01",
                    "Female", "1234567890", "CUSTOMER");
        });
        assertEquals("Invalid email format.", exception.getMessage());
    }

    @Test
    public void testShortPassword() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            new User("Alice Smith", "alice@example.com", "123", "1990-01-01",
                    "Female", "1234567890", "CUSTOMER");
        });
        assertEquals("Password must be at least 6 characters.", exception.getMessage());
    }

    @Test
    public void testInvalidGender() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            new User("Alice Smith", "alice@example.com", "securePass123", "1990-01-01",
                    "Invalid", "1234567890", "CUSTOMER");
        });
        assertEquals("Invalid gender value.", exception.getMessage());
    }

    @Test
    public void testInvalidRole() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            new User("Alice Smith", "alice@example.com", "securePass123", "1990-01-01",
                    "Female", "1234567890", "INVALID");
        });
        assertEquals("Invalid role value: INVALID", exception.getMessage());
    }

    @Test
    public void testInvalidPhone() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            new User("Alice Smith", "alice@example.com", "securePass123", "1990-01-01",
                    "Female", "123", "CUSTOMER");
        });
        assertEquals("Phone number must be 10 digits.", exception.getMessage());
    }
}