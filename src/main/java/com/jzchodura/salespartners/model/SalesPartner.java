package com.jzchodura.salespartners.model;

import java.util.List;
import java.util.UUID;

public record SalesPartner(
    UUID id,
    List<Identifier> identifiers,
    String name,
    List<Address> addresses,
    PartnerStatus state,
    List<Contact> contacts
) {
}
