package com.jzchodura.salespartners.model;

public record Address(
    String country,
    String city,
    String zipCode,
    String street,
    String streetNumber
) {
}
