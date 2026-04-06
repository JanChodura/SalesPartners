package com.jzchodura.salespartners.service;

import com.jzchodura.salespartners.model.Contact;
import com.jzchodura.salespartners.repository.SalesPartnerRepository;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class ContactServiceImpl implements ContactService {

    private final SalesPartnerRepository salesPartnerRepository;

    public ContactServiceImpl(SalesPartnerRepository salesPartnerRepository) {
        this.salesPartnerRepository = salesPartnerRepository;
    }

    @Override
    public Contact add(UUID partnerId, Contact contact) {
        return salesPartnerRepository.addContact(partnerId, contact);
    }

    @Override
    public Contact update(UUID partnerId, UUID contactId, Contact contact) {
        return salesPartnerRepository.updateContact(partnerId, contactId, contact);
    }

    @Override
    public void delete(UUID partnerId, UUID contactId) {
        salesPartnerRepository.deleteContact(partnerId, contactId);
    }
}
