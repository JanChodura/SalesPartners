package com.jzchodura.salespartners.service;

import com.jzchodura.salespartners.SalesPartnersApp;
import com.jzchodura.salespartners.model.Contact;
import com.jzchodura.salespartners.util.ContactUtil;
import com.jzchodura.salespartners.util.TestIdsUtil;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@Disabled("Enable after ContactService implementation is registered as a Spring bean.")
@SpringBootTest(classes = SalesPartnersApp.class)
class ContactServiceIntegrationTest {

    @Autowired
    private ContactService contactService;

    @Test
    void addContact_returnsCreatedContact() {
        Contact createdContact = contactService.addContact(TestIdsUtil.PARTNER_ID, ContactUtil.createdContact());

        assertNotNull(createdContact);
        assertEquals(TestIdsUtil.CONTACT_ID, createdContact.id());
    }

    @Test
    void updateContact_returnsUpdatedContact() {
        Contact updatedContact = contactService.updateContact(
            TestIdsUtil.PARTNER_ID,
            TestIdsUtil.CONTACT_ID,
            ContactUtil.updatedContact()
        );

        assertNotNull(updatedContact);
        assertEquals(TestIdsUtil.CONTACT_ID, updatedContact.id());
    }

    @Test
    void deleteContact_completesSuccessfully() {
        assertDoesNotThrow(() -> contactService.deleteContact(TestIdsUtil.PARTNER_ID, TestIdsUtil.CONTACT_ID));
    }
}
