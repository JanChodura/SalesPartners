package com.jzchodura.salespartners.service;

import com.jzchodura.salespartners.model.Contact;

public interface ContactService {

    Contact addContact(Long partnerId, Contact contact);

    Contact updateContact(Long partnerId, Long contactId, Contact contact);

    void deleteContact(Long partnerId, Long contactId);
}
