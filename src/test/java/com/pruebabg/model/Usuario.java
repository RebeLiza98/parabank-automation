package com.pruebabg.model;

public record Usuario(
        String firstName,
        String lastName,
        String address,
        String city,
        String state,
        String zipCode,
        String phoneNumber,
        String ssn,
        String username,
        String password) {
}
