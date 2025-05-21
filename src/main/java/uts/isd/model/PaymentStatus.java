package uts.isd.model;

public enum PaymentStatus {
    PENDING("Pending"),
    COMPLETED("Completed"),
    CANCELLED("Cancelled"),
    FAILED("Failed"),
    REFUNDED("Refunded");

    private final String displayName;

    PaymentStatus(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

    public static PaymentStatus fromString(String status) {
        for (PaymentStatus paymentStatus : PaymentStatus.values()) {
            if (paymentStatus.displayName.equalsIgnoreCase(status)) {
                return paymentStatus;
            }
        }
        return PENDING; // Default to PENDING if status is not recognized
    }
} 