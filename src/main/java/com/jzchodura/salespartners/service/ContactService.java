package com.jzchodura.salespartners.service;

import com.jzchodura.salespartners.model.Contact;

public interface ContactService {

    Contact add(Long partnerId, Contact contact);

    Contact update(Long partnerId, Long contactId, Contact contact);

    void delete(Long partnerId, Long contactId);
}
