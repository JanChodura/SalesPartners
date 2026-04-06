package com.jzchodura.salespartners.service;

import com.jzchodura.salespartners.SalesPartnersApp;
import com.jzchodura.salespartners.exception.ResourceNotFoundException;
import com.jzchodura.salespartners.model.Address;
import com.jzchodura.salespartners.model.Contact;
import com.jzchodura.salespartners.model.Identifier;
import com.jzchodura.salespartners.model.IdentifierType;
import com.jzchodura.salespartners.model.PartnerStatus;
import com.jzchodura.salespartners.model.SalesPartner;
import com.jzchodura.salespartners.util.ContactUtil;
import com.jzchodura.salespartners.util.SalesPartnerUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(classes = SalesPartnersApp.class)
class ServiceDomainValidationIntegrationTest {

    @Autowired
    private SalesPartnerService salesPartnerService;

    @Autowired
    private ContactService contactService;

    @Test
    void createPartner_throws_whenNameIsBlank() {
        SalesPartner invalidPartner = new SalesPartner(
            null,
            List.of(new Identifier("CZ12345678", IdentifierType.ICO)),
            " ",
            List.of(new Address("CZ", "Prague", "11000", "Hlavni", "12")),
            PartnerStatus.NEW,
            List.of()
        );

        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> salesPartnerService.create(invalidPartner)
        );

        assertEquals("Partner name must not be blank.", exception.getMessage());
    }

    @Test
    void createPartner_throws_whenIdentifiersAreMissing() {
        SalesPartner invalidPartner = new SalesPartner(
            null,
            List.of(),
            "Acme",
            List.of(new Address("CZ", "Prague", "11000", "Hlavni", "12")),
            PartnerStatus.NEW,
            List.of()
        );

        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> salesPartnerService.create(invalidPartner)
        );

        assertEquals("Partner must contain at least one identifier.", exception.getMessage());
    }

    @Test
    void createPartner_throws_whenAddressesAreMissing() {
        SalesPartner invalidPartner = new SalesPartner(
            null,
            List.of(new Identifier("CZ12345678", IdentifierType.ICO)),
            "Acme",
            List.of(),
            PartnerStatus.NEW,
            List.of()
        );

        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> salesPartnerService.create(invalidPartner)
        );

        assertEquals("Partner must contain at least one address.", exception.getMessage());
    }

    @Test
    void addContact_throws_whenFirstNameIsBlank() {
        SalesPartner createdPartner = salesPartnerService.create(SalesPartnerUtil.createdPartner());
        Contact invalidContact = new Contact(
            null,
            " ",
            "Novak",
            ContactUtil.createdContact().position(),
            true,
            "+420",
            "777888999",
            "jan.novak@example.com"
        );

        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> contactService.add(createdPartner.id(), invalidContact)
        );

        assertEquals("Contact firstName must not be blank.", exception.getMessage());
    }

    @Test
    void addContact_throws_whenSecondPrimaryContactIsAdded() {
        SalesPartner createdPartner = salesPartnerService.create(SalesPartnerUtil.createdPartner());

        contactService.add(createdPartner.id(), ContactUtil.createdContact());

        Contact anotherPrimaryContact = new Contact(
            null,
            "Petr",
            "Svoboda",
            ContactUtil.updatedContact().position(),
            true,
            "+420",
            "123456789",
            "petr.svoboda@example.com"
        );

        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> contactService.add(createdPartner.id(), anotherPrimaryContact)
        );

        assertEquals("Partner can have only one primary contact.", exception.getMessage());
    }

    @Test
    void getPartnerDetail_throws_whenPartnerDoesNotExist() {
        ResourceNotFoundException exception = assertThrows(
            ResourceNotFoundException.class,
            () -> salesPartnerService.getPartnerDetail(UUID.randomUUID())
        );

        assertTrue(exception.getMessage().startsWith("Partner with id "));
    }
}
