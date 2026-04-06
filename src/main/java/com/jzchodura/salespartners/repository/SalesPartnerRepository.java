package com.jzchodura.salespartners.repository;

import com.jzchodura.salespartners.model.Contact;
import com.jzchodura.salespartners.model.SalesPartner;

import java.util.List;
import java.util.Optional;

public interface SalesPartnerRepository {

    SalesPartner createPartner(SalesPartner partner);

    List<SalesPartner> findAllPartners();

    Optional<SalesPartner> findPartnerById(Long partnerId);

    SalesPartner updatePartner(Long partnerId, SalesPartner partner);

    Contact addContact(Long partnerId, Contact contact);

    Contact updateContact(Long partnerId, Long contactId, Contact contact);

    void deleteContact(Long partnerId, Long contactId);
}
