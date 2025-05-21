package uts.isd.test;

import org.junit.Test;
import uts.isd.model.User;

import static junit.framework.Assert.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

public class MyTest {
    @Test
    public void testExample() {
        assertTrue("JUnit test is running!", true);
    }

    @Test
    public void testUserCreation() {
        User user = new User("Alice", "alice@example.com", "securePass", "1234567890", "customer");
        assertEquals("Alice", user.getFullName());
        assertEquals("alice@example.com", user.getEmail());
        assertEquals("securePass", user.getPassword());
        assertEquals("1234567890", user.getPhone());
        assertEquals("customer", user.getRole());
    }

    @Test
    public void testSetters() {
        User user = new User("Alice", "alice@example.com", "securePass", "1234567890", "customer");
        user.setFullName("Bob");
        user.setEmail("bob@example.com");
        user.setPhone("0987654321");
        user.setRole("admin");

        assertEquals("Bob", user.getFullName());
        assertEquals("bob@example.com", user.getEmail());
        assertEquals("0987654321", user.getPhone());
        assertEquals("admin", user.getRole());
    }

    @Test
    public void testInvalidEmail() {
        try {
            User user = new User("Alice", "invalid-email", "securePass", "1234567890", "customer");
            // 如果构造函数未抛出异常，这里是失败
            assertFalse("Email should be invalid", true);
        } catch (IllegalArgumentException e) {
            assertTrue("Caught expected exception for invalid email", true);
        }
    }

    @Test
    public void testShortPassword() {
        try {
            User user = new User("Alice", "alice@example.com", "123", "1234567890", "customer");
            assertFalse("Password should be too short", true);
        } catch (IllegalArgumentException e) {
            assertTrue("Caught expected exception for short password", true);
        }
    }
}

//package uts.isd.test;
//
//import org.junit.Test;
//import uts.isd.model.User;
//
//import static junit.framework.Assert.assertTrue;
//import static org.junit.Assert.assertEquals;
//import static org.junit.Assert.assertFalse;
//
//public class MyTest {
//    @Test
//    public void testExample() {
//        assertTrue("JUnit test is running!", true);
//    }
//
//    @Test
//    public void testUserCreation() {
//        User user = new User("Alice", "alice@example.com", "securePass", "1234567890", "customer");
//        assertEquals("Alice", user.getFullName());
//        assertEquals("alice@example.com", user.getEmail());
//        assertEquals("securePass", user.getPassword());
//        assertEquals("1234567890", user.getPhone());
//        assertEquals("customer", user.getRole());
//    }
//
//    @Test
//    public void testSetters() {
//        User user = new User("Alice", "alice@example.com", "securePass", "1234567890", "customer");
//        user.setFullName("Bob");
//        user.setEmail("bob@example.com");
//        user.setPhone("0987654321");
//        user.setRole("admin");
//
//        assertEquals("Bob", user.getFullName());
//        assertEquals("bob@example.com", user.getEmail());
//        assertEquals("0987654321", user.getPhone());
//        assertEquals("admin", user.getRole());
//    }
//
//    @Test
//    public void testInvalidEmail() {
//        try {
//            User user = new User("Alice", "invalid-email", "securePass", "1234567890", "customer");
//            assertFalse("Email should be invalid", true);
//        } catch (IllegalArgumentException e) {
//            assertTrue("Caught expected exception for invalid email", true);
//        }
//    }
//
//    @Test
//    public void testShortPassword() {
//        try {
//            User user = new User("Alice", "alice@example.com", "123", "1234567890", "customer");
//            assertFalse("Password should be too short", true);
//        } catch (IllegalArgumentException e) {
//            assertTrue("Caught expected exception for short password", true);
//        }
//    }
//}