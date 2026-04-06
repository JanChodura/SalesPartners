package com.jzchodura.salespartners.service;

import com.jzchodura.salespartners.SalesPartnersApp;
import com.jzchodura.salespartners.model.Contact;
import com.jzchodura.salespartners.model.SalesPartner;
import com.jzchodura.salespartners.util.ContactUtil;
import com.jzchodura.salespartners.util.SalesPartnerUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(classes = SalesPartnersApp.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class ContactServiceIntegrationTest {

    @Autowired
    private ContactService contactService;

    @Autowired
    private SalesPartnerService salesPartnerService;

    @Test
    void addContact_returnsCreated() {
        SalesPartner createdPartner = salesPartnerService.create(SalesPartnerUtil.createdPartner());
        Contact createdContact = contactService.add(createdPartner.id(), ContactUtil.createdContact());

        assertNotNull(createdContact);
        assertNotNull(createdContact.id());
        assertEquals("Jan", createdContact.firstName());
    }

    @Test
    void updateContact_returnsUpdated() {
        SalesPartner createdPartner = salesPartnerService.create(SalesPartnerUtil.createdPartner());
        Contact createdContact = contactService.add(createdPartner.id(), ContactUtil.createdContact());

        Contact updatedContact = contactService.update(
            createdPartner.id(),
            createdContact.id(),
            ContactUtil.updatedContact()
        );

        assertNotNull(updatedContact);
        assertEquals(createdContact.id(), updatedContact.id());
        assertEquals("Petr", updatedContact.firstName());
    }

    @Test
    void delete_completesSuccessfully() {
        SalesPartner createdPartner = salesPartnerService.create(SalesPartnerUtil.createdPartner());
        Contact createdContact = contactService.add(createdPartner.id(), ContactUtil.createdContact());

        assertDoesNotThrow(() -> contactService.delete(createdPartner.id(), createdContact.id()));
    }
}
