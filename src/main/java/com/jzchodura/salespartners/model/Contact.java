package com.jzchodura.salespartners.model;

public record Contact(
    Long id,
    String firstName,
    String lastName,
    ContactPosition position,
    boolean primary,
    String countryCallingCode,
    String phoneNumber,
    String email
) {
}
