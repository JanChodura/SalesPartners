package com.jzchodura.salespartners.model;


import java.util.List;

public final class SalesPartner {

    private Long id;

    private List<Identifier> identifiers;

    private String name;

    private List<Address> addresses;

    private PartnerStatus state;

    private List<Contact> contacts;

}
