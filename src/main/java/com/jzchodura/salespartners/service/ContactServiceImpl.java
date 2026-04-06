package com.jzchodura.salespartners.service;

import com.jzchodura.salespartners.exception.DuplicateResourceException;
import com.jzchodura.salespartners.exception.ResourceNotFoundException;
import com.jzchodura.salespartners.model.Contact;
import com.jzchodura.salespartners.model.SalesPartner;
import com.jzchodura.salespartners.repository.SalesPartnerRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Locale;
import java.util.Objects;
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
        validateDuplicateContact(partnerId, contact, null);
        validatePrimaryContactRule(partnerId, contact, null);
        return salesPartnerRepository.addContact(partnerId, contact);
    }

    @Override
    public Contact update(UUID partnerId, UUID contactId, Contact contact) {
        validate(contact);
        validateDuplicateContact(partnerId, contact, contactId);
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
        SalesPartner partner = getRequiredPartner(partnerId);

        if (!contact.primary()) {
            return;
        }

        boolean hasAnotherPrimary = safeList(partner.contacts()).stream()
            .filter(existingContact -> !existingContact.id().equals(currentContactId))
            .anyMatch(Contact::primary);

        if (hasAnotherPrimary) {
            throw new IllegalArgumentException("Partner can have only one primary contact.");
        }
    }

    private void validateDuplicateContact(UUID partnerId, Contact contact, UUID currentContactId) {
        SalesPartner partner = getRequiredPartner(partnerId);

        boolean duplicateExists = safeList(partner.contacts()).stream()
            .filter(existingContact -> !Objects.equals(existingContact.id(), currentContactId))
            .anyMatch(existingContact ->
                hasSameEmail(existingContact, contact) || hasSamePhone(existingContact, contact)
            );

        if (duplicateExists) {
            throw new DuplicateResourceException("Contact already exists for partner %s.".formatted(partnerId));
        }
    }

    private boolean hasSameEmail(Contact existingContact, Contact candidateContact) {
        String existingEmail = normalize(existingContact.email());
        String candidateEmail = normalize(candidateContact.email());
        return existingEmail != null && existingEmail.equals(candidateEmail);
    }

    private boolean hasSamePhone(Contact existingContact, Contact candidateContact) {
        String existingPhone = normalizePhone(existingContact);
        String candidatePhone = normalizePhone(candidateContact);
        return existingPhone != null && existingPhone.equals(candidatePhone);
    }

    private String normalizePhone(Contact contact) {
        String countryCallingCode = normalize(contact.countryCallingCode());
        String phoneNumber = normalize(contact.phoneNumber());
        if (countryCallingCode == null || phoneNumber == null) {
            return null;
        }
        return countryCallingCode + ":" + phoneNumber;
    }

    private String normalize(String value) {
        if (value == null) {
            return null;
        }
        String normalized = value.trim().toLowerCase(Locale.ROOT);
        return normalized.isEmpty() ? null : normalized;
    }

    private SalesPartner getRequiredPartner(UUID partnerId) {
        return salesPartnerRepository.findPartnerById(partnerId)
            .orElseThrow(() -> new ResourceNotFoundException("Partner with id %s was not found.".formatted(partnerId)));
    }

    private <T> List<T> safeList(List<T> values) {
        return values == null ? List.of() : values;
    }
}
