package com.healthaiharbor.ai.userservice.auth;

/**
 * Enum representing different password encoding algorithms.
 * Provides a method to get the key associated with each encoder
 * and a utility method to retrieve an encoder type from a string key.
 */
public enum PasswordEncoderType {

    BCRYPT("bcrypt"),
    ARGON2("argon2"),
    SCRYPT("scrypt"),
    PBKDF2("pbkdf2");

    private final String key;

    /**
     * Constructor to associate a key with the encoder type.
     * @param key The string representation of the encoder.
     */
    PasswordEncoderType(String key) {
        this.key = key;
    }

    /**
     * Retrieves the key of the encoder type.
     * @return The string key associated with the encoder.
     */
    public String getKey() {
        return key;
    }

    /**
     * Converts a string key to a corresponding PasswordEncoderType.
     * @param key The string representation of the encoder.
     * @return The corresponding PasswordEncoderType.
     * @throws IllegalArgumentException if the key does not match any encoder type.
     */
    public static PasswordEncoderType fromKey(String key) {
        for (PasswordEncoderType type : values()) {
            if (type.getKey().equalsIgnoreCase(key)) {
                return type;
            }
        }
        throw new IllegalArgumentException("Invalid password encoder: " + key);
    }
}