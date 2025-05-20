package uts.isd.test;

import org.junit.Test;
import uts.isd.model.User;
import uts.isd.model.Device;

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

    @Test
    public void testDeviceCreation() {
        Device device = new Device(1, "Smart Plug", "Energy", "A smart power plug", "pcs", 19.99, 100);

        assertEquals(1, device.getDeviceId());
        assertEquals("Smart Plug", device.getName());
        assertEquals("Energy", device.getType());
        assertEquals("A smart power plug", device.getDescription());
        assertEquals("pcs", device.getUnit());
        assertEquals(19.99, device.getPrice(), 0.01);
        assertEquals(100, device.getQuantity());
    }

    @Test
    public void testSettersAndGetters() {
        Device device = new Device();
        device.setDeviceId(2);
        device.setName("Door Sensor");
        device.setType("Security");
        device.setDescription("A sensor for doors");
        device.setUnit("pcs");
        device.setPrice(9.99);
        device.setQuantity(50);

        assertEquals(2, device.getDeviceId());
        assertEquals("Door Sensor", device.getName());
        assertEquals("Security", device.getType());
        assertEquals("A sensor for doors", device.getDescription());
        assertEquals("pcs", device.getUnit());
        assertEquals(9.99, device.getPrice(), 0.01);
        assertEquals(50, device.getQuantity());
    }

    @Test
    public void testDeviceIsInStock() {
        Device device = new Device(3, "Heart Monitor", "Health", "A heart tracking monitor", "pcs", 129.00, 25);
        assertTrue(device.getQuantity() > 0);
    }

    @Test
    public void testDeviceOutOfStock() {
        Device device = new Device(4, "Old Sensor", "Sensors", "Discontinued", "pcs", 15.00, 0);
        assertEquals(0, device.getQuantity());
        assertFalse(device.getQuantity() > 0);
    }

}