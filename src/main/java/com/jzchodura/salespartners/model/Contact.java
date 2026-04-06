package com.jzchodura.salespartners.model;

import java.util.UUID;

public record Contact(
    UUID id,
    String firstName,
    String lastName,
    ContactPosition position,
    boolean primary,
    String countryCallingCode,
    String phoneNumber,
    String email
) {
}
