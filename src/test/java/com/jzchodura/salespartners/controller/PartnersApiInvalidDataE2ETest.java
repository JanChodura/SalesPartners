package com.jzchodura.salespartners.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jzchodura.salespartners.SalesPartnersApp;
import com.jzchodura.salespartners.generated.dto.CreateContactRequest;
import com.jzchodura.salespartners.generated.dto.CreatePartnerRequest;
import com.jzchodura.salespartners.generated.dto.UpdatePartnerRequest;
import com.jzchodura.salespartners.util.ContactRequestUtil;
import com.jzchodura.salespartners.util.PartnerRequestUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.List;
import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = SalesPartnersApp.class)
@AutoConfigureMockMvc
class PartnersApiInvalidDataE2ETest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void createPartner_returnsBadRequest_whenNameIsBlank() throws Exception {
        CreatePartnerRequest request = PartnerRequestUtil.createPartnerRequest();
        request.setName(" ");

        mockMvc.perform(post("/partners")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.code").value("VALIDATION_ERROR"));
    }

    @Test
    void createPartner_returnsBadRequest_whenIdentifiersAreMissing() throws Exception {
        CreatePartnerRequest request = PartnerRequestUtil.createPartnerRequest();
        request.setIdentifiers(List.of());

        mockMvc.perform(post("/partners")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.code").value("BUSINESS_VALIDATION_ERROR"))
            .andExpect(jsonPath("$.message").value("Partner must contain at least one identifier."));
    }

    @Test
    void addContact_returnsBadRequest_whenEmailIsInvalid() throws Exception {
        CreateContactRequest request = ContactRequestUtil.createContactRequest();
        request.setEmail("not-an-email");

        mockMvc.perform(post("/partners/{partnerId}/contacts", UUID.randomUUID())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.code").value("VALIDATION_ERROR"));
    }

    @Test
    void getPartnerDetail_returnsBadRequest_whenPartnerIdIsNotUuid() throws Exception {
        mockMvc.perform(get("/partners/{partnerId}", "not-a-uuid"))
            .andExpect(status().isBadRequest());
    }

    @Test
    void updatePartner_returnsNotFound_whenPartnerDoesNotExist() throws Exception {
        UpdatePartnerRequest request = PartnerRequestUtil.updatePartnerRequest();

        mockMvc.perform(put("/partners/{partnerId}", UUID.randomUUID())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isNotFound())
            .andExpect(jsonPath("$.code").value("NOT_FOUND"));
    }

    @Test
    void addContact_returnsBadRequest_whenSecondPrimaryContactIsAdded() throws Exception {
        UUID partnerId = createPartnerAndReturnId();
        CreateContactRequest firstPrimaryContact = ContactRequestUtil.createContactRequest();
        CreateContactRequest secondPrimaryContact = ContactRequestUtil.createContactRequest();
        secondPrimaryContact.setEmail("second.primary@example.com");
        secondPrimaryContact.setPhoneNumber("123456789");

        mockMvc.perform(post("/partners/{partnerId}/contacts", partnerId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(firstPrimaryContact)))
            .andExpect(status().isCreated());

        mockMvc.perform(post("/partners/{partnerId}/contacts", partnerId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(secondPrimaryContact)))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.code").value("BUSINESS_VALIDATION_ERROR"))
            .andExpect(jsonPath("$.message").value("Partner can have only one primary contact."));
    }

    private UUID createPartnerAndReturnId() throws Exception {
        MvcResult result = mockMvc.perform(post("/partners")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(PartnerRequestUtil.createPartnerRequest())))
            .andExpect(status().isCreated())
            .andReturn();

        JsonNode responseBody = objectMapper.readTree(result.getResponse().getContentAsString());
        return UUID.fromString(responseBody.get("id").asText());
    }
}
