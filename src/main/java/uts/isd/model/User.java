package uts.isd.model;

import java.io.Serializable;

public class User implements Serializable {

    public enum UserType {
        CUSTOMER, STAFF, ADMIN;

        public static UserType fromString(String role) {
            try {
                return UserType.valueOf(role.toUpperCase());
            } catch (IllegalArgumentException e) {
                return null;
            }
        }
    }

    private int userId;
    private String fullName;
    private String email;
    private String password;
    private String dob;
    private String gender;
    private String phone;
    private UserType type;
    private boolean isActive;

    // Default constructor
    public User() {
        this.isActive = true;
    }

    // Constructor with all fields
    public User(int userId, String fullName, String email, String password, String dob, String gender,
                String phone, String role, boolean isActive) {
        this.userId = userId;
        setFullName(fullName);
        setEmail(email);
        setPassword(password);
        this.dob = dob;
        setGender(gender);
        setPhone(phone);
        setRole(role);
        this.isActive = isActive;
    }

    // Constructor without ID
    public User(String fullName, String email, String password, String dob, String gender,
                String phone, String role) {
        setFullName(fullName);
        setEmail(email);
        setPassword(password);
        this.dob = dob;
        setGender(gender);
        setPhone(phone);
        setRole(role);
        this.isActive = true;
    }

    // Getters and setters
    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        if (fullName != null) {
            this.fullName = fullName;
        } else {
            throw new IllegalArgumentException("Full name cannot be empty.");
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
        if (password != null && password.length() >= 6) {
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
        if (gender == null || gender.isEmpty()) {
            throw new IllegalArgumentException("Gender cannot be empty.");
        }
        if (gender.equalsIgnoreCase("Male") || gender.equalsIgnoreCase("Female") || gender.equalsIgnoreCase("Other")) {
            this.gender = gender;
        } else {
            throw new IllegalArgumentException("Invalid gender value.");
        }
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        if (phone != null && phone.matches("\\d{10}")) {
            this.phone = phone;
        } else {
            throw new IllegalArgumentException("Phone number must be 10 digits.");
        }
    }

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

    public void setRole(String role) {
        UserType parsed = UserType.fromString(role);
        if (parsed != null) {
            this.type = parsed;
        } else {
            throw new IllegalArgumentException("Invalid role value: " + role);
        }
    }

    public String getRole() {
        return (type != null) ? type.name() : null;
    }

    public boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(boolean isActive) {
        this.isActive = isActive;
    }

    // Role checks
    public boolean isAdmin() {
        return type == UserType.ADMIN;
    }

    public boolean isCustomer() {
        return type == UserType.CUSTOMER;
    }

    public boolean isStaff() {
        return type == UserType.STAFF;
    }

    @Override
    public String toString() {
        return "User {" +
                "ID=" + userId +
                ", FullName='" + fullName + '\'' +
                ", Email='" + email + '\'' +
                ", DOB='" + dob + '\'' +
                ", Gender='" + gender + '\'' +
                ", Phone='" + phone + '\'' +
                ", Role='" + getRole() + '\'' +
                ", IsActive=" + isActive +
                '}';
    }
}
