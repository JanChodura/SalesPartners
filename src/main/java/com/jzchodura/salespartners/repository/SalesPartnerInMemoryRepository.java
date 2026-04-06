package com.jzchodura.salespartners.repository;

import com.jzchodura.salespartners.model.Contact;
import com.jzchodura.salespartners.model.SalesPartner;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

@Repository
public class SalesPartnerInMemoryRepository implements SalesPartnerRepository {

    private final Map<Long, SalesPartner> partners = new HashMap<>();
    private long partnerSequence = 0L;
    private long contactSequence = 0L;

    @Override
    public synchronized SalesPartner createPartner(SalesPartner partner) {
        long partnerId = ++partnerSequence;
        SalesPartner createdPartner = new SalesPartner(
            partnerId,
            safeList(partner.identifiers()),
            partner.name(),
            safeList(partner.addresses()),
            partner.state(),
            safeList(partner.contacts())
        );
        partners.put(partnerId, createdPartner);
        return createdPartner;
    }

    @Override
    public synchronized List<SalesPartner> findAllPartners() {
        return new ArrayList<>(partners.values());
    }

    @Override
    public synchronized java.util.Optional<SalesPartner> findPartnerById(Long partnerId) {
        return java.util.Optional.ofNullable(partners.get(partnerId));
    }

    @Override
    public synchronized SalesPartner updatePartner(Long partnerId, SalesPartner partner) {
        SalesPartner existingPartner = getRequiredPartner(partnerId);
        SalesPartner updatedPartner = new SalesPartner(
            partnerId,
            safeList(partner.identifiers()),
            partner.name(),
            safeList(partner.addresses()),
            partner.state(),
            existingPartner.contacts()
        );
        partners.put(partnerId, updatedPartner);
        return updatedPartner;
    }

    @Override
    public synchronized Contact addContact(Long partnerId, Contact contact) {
        SalesPartner partner = getRequiredPartner(partnerId);
        Contact createdContact = new Contact(
            ++contactSequence,
            contact.firstName(),
            contact.lastName(),
            contact.position(),
            contact.primary(),
            contact.countryCallingCode(),
            contact.phoneNumber(),
            contact.email()
        );

        List<Contact> updatedContacts = new ArrayList<>(safeList(partner.contacts()));
        updatedContacts.add(createdContact);
        partners.put(partnerId, withContacts(partner, updatedContacts));
        return createdContact;
    }

    @Override
    public synchronized Contact updateContact(Long partnerId, Long contactId, Contact contact) {
        SalesPartner partner = getRequiredPartner(partnerId);
        List<Contact> updatedContacts = new ArrayList<>(safeList(partner.contacts()));

        for (int i = 0; i < updatedContacts.size(); i++) {
            Contact existingContact = updatedContacts.get(i);
            if (existingContact.id().equals(contactId)) {
                Contact updatedContact = new Contact(
                    contactId,
                    contact.firstName(),
                    contact.lastName(),
                    contact.position(),
                    contact.primary(),
                    contact.countryCallingCode(),
                    contact.phoneNumber(),
                    contact.email()
                );
                updatedContacts.set(i, updatedContact);
                partners.put(partnerId, withContacts(partner, updatedContacts));
                return updatedContact;
            }
        }

        throw new NoSuchElementException("Contact with id %s was not found for partner %s.".formatted(contactId, partnerId));
    }

    @Override
    public synchronized void deleteContact(Long partnerId, Long contactId) {
        SalesPartner partner = getRequiredPartner(partnerId);
        List<Contact> updatedContacts = safeList(partner.contacts()).stream()
            .filter(contact -> !contact.id().equals(contactId))
            .toList();

        if (updatedContacts.size() == safeList(partner.contacts()).size()) {
            throw new NoSuchElementException("Contact with id %s was not found for partner %s.".formatted(contactId, partnerId));
        }

        partners.put(partnerId, withContacts(partner, updatedContacts));
    }

    private SalesPartner getRequiredPartner(Long partnerId) {
        SalesPartner partner = partners.get(partnerId);
        if (partner == null) {
            throw new NoSuchElementException("Partner with id %s was not found.".formatted(partnerId));
        }
        return partner;
    }

    private SalesPartner withContacts(SalesPartner partner, List<Contact> contacts) {
        return new SalesPartner(
            partner.id(),
            partner.identifiers(),
            partner.name(),
            partner.addresses(),
            partner.state(),
            List.copyOf(contacts)
        );
    }

    private <T> List<T> safeList(List<T> values) {
        return values == null ? List.of() : List.copyOf(values);
    }
}
