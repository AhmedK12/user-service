package com.healthaiharbor.ai.userservice.kafka;

/**
 * Enum representing different types of user activities that can be logged.
 */
public enum UserActivityType {

    LOGIN,                 // User logged in
    LOGOUT,                // User logged out
    REGISTER,              // New user registered
    PASSWORD_CHANGE,       // User changed their password
    PROFILE_UPDATE,        // User updated their profile
    TOKEN_REFRESH,         // User refreshed their authentication token
    ROLE_ASSIGNED,         // A role was assigned to the user
    PERMISSION_GRANTED,    // A permission was granted to the user
    ACCOUNT_LOCKED,        // The user's account was locked
    ACCOUNT_UNLOCKED,      // The user's account was unlocked
    ACCOUNT_DELETE;        // The user's account was deleted

    /**
     * Returns a human-readable label for the activity type.
     *
     * @return A formatted string representation of the activity type.
     */
    public String getLabel() {
        return this.name().replace("_", " ").toLowerCase();
    }
}
