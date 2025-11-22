package com.blbilink.blbilogin.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;

public class PasswordUtil {

    private static final int SALT_LENGTH = 16;
    private static final String HASH_ALGORITHM = "SHA-256";

    /**
     * Generates a salt and hashes the password with it.
     * Returns a string in the format: salt$hash
     *
     * @param password The plain text password.
     * @return The salted and hashed password.
     */
    public static String hashPassword(String password) {
        byte[] salt = generateSalt();
        String hash = hash(password, salt);
        return Base64.getEncoder().encodeToString(salt) + "$" + hash;
    }

    /**
     * Verifies the password against the stored salted hash.
     *
     * @param password   The plain text password.
     * @param storedHash The stored password string (salt$hash).
     * @return True if the password matches, false otherwise.
     */
    public static boolean checkPassword(String password, String storedHash) {
        String[] parts = storedHash.split("\\$");
        if (parts.length != 2) {
            // Fallback for old plain text passwords (migration strategy)
            return password.equals(storedHash);
        }
        byte[] salt = Base64.getDecoder().decode(parts[0]);
        String hash = hash(password, salt);
        return hash.equals(parts[1]);
    }

    private static byte[] generateSalt() {
        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[SALT_LENGTH];
        random.nextBytes(salt);
        return salt;
    }

    private static String hash(String password, byte[] salt) {
        try {
            MessageDigest md = MessageDigest.getInstance(HASH_ALGORITHM);
            md.update(salt);
            byte[] hashedPassword = md.digest(password.getBytes());
            return Base64.getEncoder().encodeToString(hashedPassword);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Hashing algorithm not found", e);
        }
    }
}
