package com.jzchodura.salespartners.model;

import java.util.List;

public record SalesPartner(
    Long id,
    List<Identifier> identifiers,
    String name,
    List<Address> addresses,
    PartnerStatus state,
    List<Contact> contacts
) {
}
