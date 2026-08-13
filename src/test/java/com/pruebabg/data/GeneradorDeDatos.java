package com.pruebabg.data;

import com.pruebabg.model.Usuario;

import java.security.SecureRandom;

public final class GeneradorDeDatos {

    private static final SecureRandom RANDOM = new SecureRandom();
    private static final String ALPHABET = "abcdefghijklmnopqrstuvwxyz";

    private GeneradorDeDatos() {
    }

    public static Usuario newUser() {
        return new Usuario(
                "Rebeca",
                "Mata",
                "Av. Principal 123",
                "Guayaquil",
                "Guayas",
                "090101",
                "0999999999",
                "123456789",
                uniqueUsername(),
                "Password1");
    }

    public static String uniqueUsername() {
        return "bg" + Long.toString(System.currentTimeMillis(), 36) + randomSuffix(3);
    }

    private static String randomSuffix(int length) {
        StringBuilder suffix = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            suffix.append(ALPHABET.charAt(RANDOM.nextInt(ALPHABET.length())));
        }
        return suffix.toString();
    }
}
