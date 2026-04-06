package com.jzchodura.salespartners.controller;

import com.jzchodura.salespartners.generated.api.PartnersApiDelegate;
import com.jzchodura.salespartners.generated.dto.ContactResponse;
import com.jzchodura.salespartners.generated.dto.CreateContactRequest;
import com.jzchodura.salespartners.generated.dto.CreatePartnerRequest;
import com.jzchodura.salespartners.generated.dto.PartnerDetailResponse;
import com.jzchodura.salespartners.generated.dto.PartnerListItem;
import com.jzchodura.salespartners.generated.dto.UpdatePartnerRequest;
import com.jzchodura.salespartners.mapper.api.ContactDtoMapper;
import com.jzchodura.salespartners.mapper.api.SalesPartnerDtoMapper;
import com.jzchodura.salespartners.service.ContactService;
import com.jzchodura.salespartners.service.SalesPartnerService;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.UUID;

@Component
@ConditionalOnBean({SalesPartnerService.class, ContactService.class})
public class PartnersApiDelegateAdapter implements PartnersApiDelegate {

    private final SalesPartnerService salesPartnerService;
    private final ContactService contactService;
    private final SalesPartnerDtoMapper salesPartnerDtoMapper;
    private final ContactDtoMapper contactDtoMapper;

    public PartnersApiDelegateAdapter(
        SalesPartnerService salesPartnerService,
        ContactService contactService,
        SalesPartnerDtoMapper salesPartnerDtoMapper,
        ContactDtoMapper contactDtoMapper
    ) {
        this.salesPartnerService = salesPartnerService;
        this.contactService = contactService;
        this.salesPartnerDtoMapper = salesPartnerDtoMapper;
        this.contactDtoMapper = contactDtoMapper;
    }

    @Override
    public ResponseEntity<PartnerDetailResponse> createPartner(CreatePartnerRequest createPartnerRequest) {
        PartnerDetailResponse response = salesPartnerDtoMapper.toDetailResponse(
            salesPartnerService.create(salesPartnerDtoMapper.toDomain(createPartnerRequest))
        );
        return ResponseEntity.created(partnerUri(response.getId())).body(response);
    }

    @Override
    public ResponseEntity<List<PartnerListItem>> getPartners() {
        List<PartnerListItem> response = salesPartnerService.getPartners().stream()
            .map(salesPartnerDtoMapper::toListItem)
            .toList();
        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<PartnerDetailResponse> getPartnerDetail(UUID partnerId) {
        PartnerDetailResponse response = salesPartnerDtoMapper.toDetailResponse(
            salesPartnerService.getPartnerDetail(partnerId)
        );
        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<PartnerDetailResponse> updatePartner(UUID partnerId, UpdatePartnerRequest updatePartnerRequest) {
        PartnerDetailResponse response = salesPartnerDtoMapper.toDetailResponse(
            salesPartnerService.updatePartner(partnerId, salesPartnerDtoMapper.toDomain(updatePartnerRequest))
        );
        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<ContactResponse> addContact(UUID partnerId, CreateContactRequest createContactRequest) {
        ContactResponse response = contactDtoMapper.toResponse(
            contactService.add(partnerId, contactDtoMapper.toDomain(createContactRequest))
        );
        return ResponseEntity.created(contactUri(response.getId())).body(response);
    }

    @Override
    public ResponseEntity<ContactResponse> updateContact(UUID partnerId, UUID contactId, CreateContactRequest body) {
        ContactResponse response = contactDtoMapper.toResponse(
            contactService.update(partnerId, contactId, contactDtoMapper.toDomain(body))
        );
        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<Void> deleteContact(UUID partnerId, UUID contactId) {
        contactService.delete(partnerId, contactId);
        return ResponseEntity.noContent().build();
    }

    private URI partnerUri(UUID partnerId) {
        return ServletUriComponentsBuilder.fromCurrentRequestUri()
            .path("/{partnerId}")
            .buildAndExpand(partnerId)
            .toUri();
    }

    private URI contactUri(UUID contactId) {
        return ServletUriComponentsBuilder.fromCurrentRequestUri()
            .path("/{contactId}")
            .buildAndExpand(contactId)
            .toUri();
    }
}
