package com.jzchodura.salespartners.mapper.api;

import com.jzchodura.salespartners.generated.dto.ContactResponse;
import com.jzchodura.salespartners.generated.dto.CreateContactRequest;
import com.jzchodura.salespartners.model.Contact;
import org.springframework.stereotype.Component;

@Component
public class ContactDtoMapper {

    public Contact toDomain(CreateContactRequest request) {
        return new Contact(
            null,
            request.getFirstName(),
            request.getLastName(),
            toDomain(request.getPosition()),
            Boolean.TRUE.equals(request.getIsPrimary()),
            request.getCountryCallingCode(),
            request.getPhoneNumber(),
            request.getEmail()
        );
    }

    public ContactResponse toResponse(Contact contact) {
        ContactResponse response = new ContactResponse();
        response.setId(contact.id());
        response.setFirstName(contact.firstName());
        response.setLastName(contact.lastName());
        response.setPosition(toDto(contact.position()));
        response.setPrimary(contact.primary());
        response.setCountryCallingCode(contact.countryCallingCode());
        response.setPhoneNumber(contact.phoneNumber());
        response.setEmail(contact.email());
        return response;
    }

    private com.jzchodura.salespartners.model.ContactPosition toDomain(
        com.jzchodura.salespartners.generated.dto.ContactPosition position
    ) {
        return position == null ? null : com.jzchodura.salespartners.model.ContactPosition.valueOf(position.getValue());
    }

    private com.jzchodura.salespartners.generated.dto.ContactPosition toDto(
        com.jzchodura.salespartners.model.ContactPosition position
    ) {
        return position == null ? null : com.jzchodura.salespartners.generated.dto.ContactPosition.fromValue(position.name());
    }
}
