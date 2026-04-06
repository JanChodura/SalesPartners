package com.jzchodura.salespartners.service;

import com.jzchodura.salespartners.model.Contact;

import java.util.UUID;

public interface ContactService {

    Contact add(UUID partnerId, Contact contact);

    Contact update(UUID partnerId, UUID contactId, Contact contact);

    void delete(UUID partnerId, UUID contactId);
}
