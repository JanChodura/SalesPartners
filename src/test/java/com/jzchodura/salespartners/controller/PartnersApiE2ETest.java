package com.jzchodura.salespartners.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jzchodura.salespartners.SalesPartnersApp;
import com.jzchodura.salespartners.generated.api.ContactApiDelegate;
import com.jzchodura.salespartners.generated.api.PartnerApiDelegate;
import com.jzchodura.salespartners.mapper.api.ContactDtoMapper;
import com.jzchodura.salespartners.mapper.api.SalesPartnerDtoMapper;
import com.jzchodura.salespartners.model.Contact;
import com.jzchodura.salespartners.model.SalesPartner;
import com.jzchodura.salespartners.service.ContactService;
import com.jzchodura.salespartners.service.SalesPartnerService;
import com.jzchodura.salespartners.util.ContactRequestUtil;
import com.jzchodura.salespartners.util.ContactUtil;
import com.jzchodura.salespartners.util.PartnerRequestUtil;
import com.jzchodura.salespartners.util.SalesPartnerUtil;
import com.jzchodura.salespartners.util.TestIdsUtil;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = {SalesPartnersApp.class, PartnersApiE2ETest.FakeServicesConfig.class})
@AutoConfigureMockMvc
class PartnersApiE2ETest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void createPartner_returnsCreatedPartner() throws Exception {
        mockMvc.perform(post("/partners")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(PartnerRequestUtil.createPartnerRequest())))
            .andExpect(status().isCreated())
            .andExpect(header().string("Location", "http://localhost/partners/" + TestIdsUtil.PARTNER_ID))
            .andExpect(jsonPath("$.id").value(TestIdsUtil.PARTNER_ID.toString()))
            .andExpect(jsonPath("$.name").value("Acme"))
            .andExpect(jsonPath("$.partnerStatus").value("NEW"))
            .andExpect(jsonPath("$.identifiers[0].value").value("CZ12345678"))
            .andExpect(jsonPath("$.addresses[0].city").value("Prague"))
            .andExpect(jsonPath("$.contacts[0].firstName").value("Jan"));
    }

    @Test
    void getPartners_returnsPartnerList() throws Exception {
        mockMvc.perform(get("/partners"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[0].id").value(TestIdsUtil.PARTNER_ID.toString()))
            .andExpect(jsonPath("$[0].name").value("Acme"))
            .andExpect(jsonPath("$[0].partnerStatus").value("NEW"));
    }

    @Test
    void getPartnerDetail_returnsPartnerDetail() throws Exception {
        mockMvc.perform(get("/partners/{partnerId}", TestIdsUtil.PARTNER_ID))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(TestIdsUtil.PARTNER_ID.toString()))
            .andExpect(jsonPath("$.name").value("Acme"))
            .andExpect(jsonPath("$.partnerStatus").value("NEW"))
            .andExpect(jsonPath("$.contacts[0].email").value("jan.novak@example.com"));
    }

    @Test
    void updatePartner_returnsUpdatedPartner() throws Exception {
        mockMvc.perform(put("/partners/{partnerId}", TestIdsUtil.PARTNER_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(PartnerRequestUtil.updatePartnerRequest())))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(TestIdsUtil.PARTNER_ID.toString()))
            .andExpect(jsonPath("$.name").value("Acme Gold"))
            .andExpect(jsonPath("$.partnerStatus").value("GOLD"))
            .andExpect(jsonPath("$.addresses[0].city").value("Brno"))
            .andExpect(jsonPath("$.contacts[0].firstName").value("Petr"));
    }

    @Test
    void addContact_returnsCreatedContact() throws Exception {
        mockMvc.perform(post("/partners/{partnerId}/contacts", TestIdsUtil.PARTNER_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(ContactRequestUtil.createContactRequest())))
            .andExpect(status().isCreated())
            .andExpect(header().string("Location", "http://localhost/partners/" + TestIdsUtil.PARTNER_ID + "/contacts/" + TestIdsUtil.CONTACT_ID))
            .andExpect(jsonPath("$.id").value(TestIdsUtil.CONTACT_ID.toString()))
            .andExpect(jsonPath("$.firstName").value("Jan"))
            .andExpect(jsonPath("$.position").value("SALES"))
            .andExpect(jsonPath("$.primary").value(true));
    }

    @Test
    void updateContact_returnsUpdatedContact() throws Exception {
        mockMvc.perform(put("/partners/{partnerId}/contacts/{contactId}", TestIdsUtil.PARTNER_ID, TestIdsUtil.CONTACT_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(ContactRequestUtil.updateContactRequest())))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(TestIdsUtil.CONTACT_ID.toString()))
            .andExpect(jsonPath("$.firstName").value("Petr"))
            .andExpect(jsonPath("$.position").value("ACCOUNT_MANAGER"))
            .andExpect(jsonPath("$.primary").value(false));
    }

    @Test
    void deleteContact_returnsNoContent() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/partners/{partnerId}/contacts/{contactId}", TestIdsUtil.PARTNER_ID, TestIdsUtil.CONTACT_ID))
            .andExpect(status().isNoContent());
    }

    @TestConfiguration
    static class FakeServicesConfig {

        @Bean
        @Primary
        PartnerApiDelegate partnerApiDelegate(
            SalesPartnerService salesPartnerService,
            SalesPartnerDtoMapper salesPartnerDtoMapper
        ) {
            return new PartnerApiDelegateAdapter(
                salesPartnerService,
                salesPartnerDtoMapper
            );
        }

        @Bean
        @Primary
        ContactApiDelegate contactApiDelegate(
            ContactService contactService,
            ContactDtoMapper contactDtoMapper
        ) {
            return new ContactApiDelegateAdapter(
                contactService,
                contactDtoMapper
            );
        }

        @Bean
        @Primary
        SalesPartnerService salesPartnerService() {
            return new SalesPartnerService() {
                @Override
                public SalesPartner create(SalesPartner partner) {
                    return SalesPartnerUtil.createdPartner();
                }

                @Override
                public List<SalesPartner> getPartners() {
                    return List.of(SalesPartnerUtil.createdPartner());
                }

                @Override
                public SalesPartner getPartnerDetail(java.util.UUID partnerId) {
                    return SalesPartnerUtil.createdPartner();
                }

                @Override
                public SalesPartner updatePartner(java.util.UUID partnerId, SalesPartner partner) {
                    return SalesPartnerUtil.updatedPartner();
                }
            };
        }

        @Bean
        @Primary
        ContactService contactService() {
            return new ContactService() {
                @Override
                public Contact add(java.util.UUID partnerId, Contact contact) {
                    return ContactUtil.createdContact();
                }

                @Override
                public Contact update(java.util.UUID partnerId, java.util.UUID contactId, Contact contact) {
                    return ContactUtil.updatedContact();
                }

                @Override
                public void delete(java.util.UUID partnerId, java.util.UUID contactId) {
                }
            };
        }
    }
}
