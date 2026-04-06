package com.jzchodura.salespartners.repository;

import com.jzchodura.salespartners.model.Contact;
import com.jzchodura.salespartners.model.SalesPartner;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface SalesPartnerRepository {

    SalesPartner createPartner(SalesPartner partner);

    List<SalesPartner> findAllPartners();

    Optional<SalesPartner> findPartnerById(UUID partnerId);

    SalesPartner updatePartner(UUID partnerId, SalesPartner partner);

    Contact addContact(UUID partnerId, Contact contact);

    Contact updateContact(UUID partnerId, UUID contactId, Contact contact);

    void deleteContact(UUID partnerId, UUID contactId);
}
