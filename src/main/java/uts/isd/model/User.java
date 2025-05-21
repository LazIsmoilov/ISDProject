package uts.isd.model;

import java.io.Serializable;

public class User implements Serializable {
    public enum UserType {
        CUSTOMER, STAFF, ADMIN
    }

    private int id;
    private String name;
    private String email;
    private String password;
    private String dob;
    private String gender;
    private UserType type; // Enum instead of String
    private String phoneNumber;
    private boolean isActive;

    // Default constructor
    public User() {
        this.isActive = true; // Default to active
    }

    // Constructor with all fields
    public User(int id, String name, String email, String password, String dob, String gender,
                UserType type, String phoneNumber, boolean isActive) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
        this.dob = dob;
        this.gender = gender;
        this.type = type;
        this.phoneNumber = phoneNumber;
        this.isActive = isActive;
    }

    // Constructor without ID
    public User(String name, String email, String password, String dob, String gender,
                UserType type, String phoneNumber) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.dob = dob;
        this.gender = gender;
        this.type = type;
        this.phoneNumber = phoneNumber;
        this.isActive = true; // Default to active
    }

    public User(String name, String email, String password, String dob, String gender) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.dob = dob;
        this.gender = gender;
    }

    public User(int id, String name, String email, String password, String dob, String gender) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
        this.dob = dob;
        this.gender = gender;
    }

    // Getters and Setters
    public UserType getType() {
        return type;
    }

    public void setType(UserType type) {
        if (type != null) {
            this.type = type;
        } else {
            throw new IllegalArgumentException("User type cannot be null.");
        }
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        if (phoneNumber != null && phoneNumber.matches("\\d{10}")) { // Example: 10-digit phone
            this.phoneNumber = phoneNumber;
        } else {
            throw new IllegalArgumentException("Invalid phone number format.");
        }
    }

    public boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(boolean isActive) {
        this.isActive = isActive;
    }

    // Existing getters/setters (id, name, email, password, dob, gender) remain unchanged
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        if (name != null && !name.trim().isEmpty()) {
            this.name = name;
        } else {
            throw new IllegalArgumentException("Name cannot be empty.");
        }
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        if (email != null && email.contains("@")) {
            this.email = email;
        } else {
            throw new IllegalArgumentException("Invalid email format.");
        }
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        if (password != null && password.length() >= 6) { // Basic validation
            this.password = password;
        } else {
            throw new IllegalArgumentException("Password must be at least 6 characters.");
        }
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        if (gender.equalsIgnoreCase("Male") || gender.equalsIgnoreCase("Female") || gender.equalsIgnoreCase("Other")) {
            this.gender = gender;
        } else {
            throw new IllegalArgumentException("Invalid gender value.");
        }
    }

    // Role checking methods
    public boolean isAdmin() {
        return type == UserType.ADMIN;
    }

    public boolean isCustomer() {
        return type == UserType.CUSTOMER;
    }

    public boolean isStaff() {
        return type == UserType.STAFF;
    }

    // Update toString() to include new fields
    @Override
    public String toString() {
        return "User { " +
                "ID=" + id +
                ", Name='" + name + '\'' +
                ", Email='" + email + '\'' +
                ", DOB='" + dob + '\'' +
                ", Gender='" + gender + '\'' +
                ", Type='" + type + '\'' +
                ", PhoneNumber='" + phoneNumber + '\'' +
                ", IsActive=" + isActive +
                " }";
    }

    public void setId(int userId) {
    }
}


//package uts.isd.model;
//
//public class User {
//    private int userId;
//    private String fullName;
//    private String email;
//    private String password;
//    private String phone;
//    private String role;
//
//    public User() {
//    }
//
//    public User(String fullName, String email, String password, String phone, String role) {
//        if (!email.contains("@")) {
//            throw new IllegalArgumentException("Email must contain '@'");
//        }
//        if (password.length() < 8) {
//            throw new IllegalArgumentException("Password must longer than 8 characters ");
//        }
//        this.fullName = fullName;
//        this.email = email;
//        this.password = password;
//        this.phone = phone;
//        this.role = role;
//    }
//
//    public int getUserId() {
//        return userId;
//    }
//
//    public void setUserId(int userId) {
//        this.userId = userId;
//    }
//
//    public String getFullName() {
//        return fullName;
//    }
//
//    public void setFullName(String fullName) {
//        this.fullName = fullName;
//    }
//
//    public String getEmail() {
//        return email;
//    }
//
//    public void setEmail(String email) {
//        this.email = email;
//    }
//
//    public String getPassword() {
//        return password;
//    }
//
//    public void setPassword(String password) {
//        this.password = password;
//    }
//
//    public String getPhone() {
//        return phone;
//    }
//
//    public void setPhone(String phone) {
//        this.phone = phone;
//    }
//
//    public String getRole() {
//        return role;
//    }
//
//    public void setRole(String role) {
//        this.role = role;
//    }
//}