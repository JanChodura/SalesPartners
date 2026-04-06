package com.jzchodura.salespartners.service;

import com.jzchodura.salespartners.SalesPartnersApp;
import com.jzchodura.salespartners.model.SalesPartner;
import com.jzchodura.salespartners.util.SalesPartnerUtil;
import com.jzchodura.salespartners.util.TestIdsUtil;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@Disabled("Enable after SalesPartnerService implementation is registered as a Spring bean.")
@SpringBootTest(classes = SalesPartnersApp.class)
class SalesPartnerServiceIntegrationTest {

    @Autowired
    private SalesPartnerService salesPartnerService;

    @Test
    void createPartner_returnsCreated() {
        SalesPartner createdPartner = salesPartnerService.create(SalesPartnerUtil.createdPartner());

        assertNotNull(createdPartner);
        assertEquals(TestIdsUtil.PARTNER_ID, createdPartner.id());
    }

    @Test
    void getPartners_returnsPartnerList() {
        List<SalesPartner> partners = salesPartnerService.getPartners();

        assertNotNull(partners);
    }

    @Test
    void getPartnerDetail_returnsPartnerDetail() {
        SalesPartner partner = salesPartnerService.getPartnerDetail(TestIdsUtil.PARTNER_ID);

        assertNotNull(partner);
        assertEquals(TestIdsUtil.PARTNER_ID, partner.id());
    }

    @Test
    void updatePartner_returnsUpdatedPartner() {
        SalesPartner updatedPartner = salesPartnerService.updatePartner(
            TestIdsUtil.PARTNER_ID,
            SalesPartnerUtil.updatedPartner()
        );

        assertNotNull(updatedPartner);
        assertEquals(TestIdsUtil.PARTNER_ID, updatedPartner.id());
    }
}
