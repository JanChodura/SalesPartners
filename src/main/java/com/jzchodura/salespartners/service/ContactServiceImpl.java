package com.jzchodura.salespartners.service;

import com.jzchodura.salespartners.exception.ResourceNotFoundException;
import com.jzchodura.salespartners.model.Contact;
import com.jzchodura.salespartners.model.SalesPartner;
import com.jzchodura.salespartners.repository.SalesPartnerRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class ContactServiceImpl implements ContactService {

    private final SalesPartnerRepository salesPartnerRepository;

    public ContactServiceImpl(SalesPartnerRepository salesPartnerRepository) {
        this.salesPartnerRepository = salesPartnerRepository;
    }

    @Override
    public Contact add(UUID partnerId, Contact contact) {
        validate(contact);
        validatePrimaryContactRule(partnerId, contact, null);
        return salesPartnerRepository.addContact(partnerId, contact);
    }

    @Override
    public Contact update(UUID partnerId, UUID contactId, Contact contact) {
        validate(contact);
        validatePrimaryContactRule(partnerId, contact, contactId);
        return salesPartnerRepository.updateContact(partnerId, contactId, contact);
    }

    @Override
    public void delete(UUID partnerId, UUID contactId) {
        salesPartnerRepository.deleteContact(partnerId, contactId);
    }

    private void validate(Contact contact) {
        if (contact.firstName() == null || contact.firstName().isBlank()) {
            throw new IllegalArgumentException("Contact firstName must not be blank.");
        }
        if (contact.lastName() == null || contact.lastName().isBlank()) {
            throw new IllegalArgumentException("Contact lastName must not be blank.");
        }
    }

    private void validatePrimaryContactRule(UUID partnerId, Contact contact, UUID currentContactId) {
        if (!contact.primary()) {
            return;
        }

        SalesPartner partner = salesPartnerRepository.findPartnerById(partnerId)
            .orElseThrow(() -> new ResourceNotFoundException("Partner with id %s was not found.".formatted(partnerId)));

        boolean hasAnotherPrimary = safeList(partner.contacts()).stream()
            .filter(existingContact -> !existingContact.id().equals(currentContactId))
            .anyMatch(Contact::primary);

        if (hasAnotherPrimary) {
            throw new IllegalArgumentException("Partner can have only one primary contact.");
        }
    }

    private <T> List<T> safeList(List<T> values) {
        return values == null ? List.of() : values;
    }
}
