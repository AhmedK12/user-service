package com.healthaiharbor.ai.userservice.auth;
public enum PasswordEncoderType {
    BCRYPT("bcrypt"),
    ARGON2("argon2"),
    SCRYPT("scrypt"),
    PBKDF2("pbkdf2");

    private final String key;

    PasswordEncoderType(String key) {
        this.key = key;
    }

    public String getKey() {
        return key;
    }

    public static PasswordEncoderType fromKey(String key) {
        for (PasswordEncoderType type : values()) {
            if (type.getKey().equalsIgnoreCase(key)) {
                return type;
            }
        }
        throw new IllegalArgumentException("Invalid password encoder: " + key);
    }
}