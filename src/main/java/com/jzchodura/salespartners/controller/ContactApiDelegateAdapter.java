package com.jzchodura.salespartners.controller;

import com.jzchodura.salespartners.generated.api.ContactApiDelegate;
import com.jzchodura.salespartners.generated.dto.ContactResponse;
import com.jzchodura.salespartners.generated.dto.CreateContactRequest;
import com.jzchodura.salespartners.mapper.api.ContactDtoMapper;
import com.jzchodura.salespartners.service.ContactService;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.UUID;

@Component
@ConditionalOnBean(ContactService.class)
public class ContactApiDelegateAdapter implements ContactApiDelegate {

    private final ContactService contactService;
    private final ContactDtoMapper contactDtoMapper;

    public ContactApiDelegateAdapter(
        ContactService contactService,
        ContactDtoMapper contactDtoMapper
    ) {
        this.contactService = contactService;
        this.contactDtoMapper = contactDtoMapper;
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

    private URI contactUri(UUID contactId) {
        return ServletUriComponentsBuilder.fromCurrentRequestUri()
            .path("/{contactId}")
            .buildAndExpand(contactId)
            .toUri();
    }
}
