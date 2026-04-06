package com.jzchodura.salespartners.service;

import com.jzchodura.salespartners.model.Contact;
import com.jzchodura.salespartners.repository.SalesPartnerRepository;
import org.springframework.stereotype.Service;

@Service
public class ContactServiceImpl implements ContactService {

    private final SalesPartnerRepository salesPartnerRepository;

    public ContactServiceImpl(SalesPartnerRepository salesPartnerRepository) {
        this.salesPartnerRepository = salesPartnerRepository;
    }

    @Override
    public Contact add(Long partnerId, Contact contact) {
        return salesPartnerRepository.addContact(partnerId, contact);
    }

    @Override
    public Contact update(Long partnerId, Long contactId, Contact contact) {
        return salesPartnerRepository.updateContact(partnerId, contactId, contact);
    }

    @Override
    public void delete(Long partnerId, Long contactId) {
        salesPartnerRepository.deleteContact(partnerId, contactId);
    }
}
