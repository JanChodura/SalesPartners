package com.jzchodura.salespartners.service;

import com.jzchodura.salespartners.SalesPartnersApp;
import com.jzchodura.salespartners.model.SalesPartner;
import com.jzchodura.salespartners.util.SalesPartnerUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(classes = SalesPartnersApp.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class SalesPartnerServiceIntegrationTest {

    @Autowired
    private SalesPartnerService salesPartnerService;

    @Test
    void createPartner_returnsCreated() {
        SalesPartner createdPartner = salesPartnerService.create(SalesPartnerUtil.createdPartner());

        assertNotNull(createdPartner);
        assertNotNull(createdPartner.id());
        assertEquals("Acme", createdPartner.name());
    }

    @Test
    void getPartners_returnsPartnerList() {
        salesPartnerService.create(SalesPartnerUtil.createdPartner());

        List<SalesPartner> partners = salesPartnerService.getPartners();

        assertNotNull(partners);
        assertFalse(partners.isEmpty());
    }

    @Test
    void getPartnerDetail_returnsPartnerDetail() {
        SalesPartner createdPartner = salesPartnerService.create(SalesPartnerUtil.createdPartner());
        SalesPartner partner = salesPartnerService.getPartnerDetail(createdPartner.id());

        assertNotNull(partner);
        assertEquals(createdPartner.id(), partner.id());
    }

    @Test
    void updatePartner_returnsUpdatedPartner() {
        SalesPartner createdPartner = salesPartnerService.create(SalesPartnerUtil.createdPartner());

        SalesPartner updatedPartner = salesPartnerService.updatePartner(
            createdPartner.id(),
            SalesPartnerUtil.updatedPartner()
        );

        assertNotNull(updatedPartner);
        assertEquals(createdPartner.id(), updatedPartner.id());
        assertEquals("Acme Gold", updatedPartner.name());
    }
}
