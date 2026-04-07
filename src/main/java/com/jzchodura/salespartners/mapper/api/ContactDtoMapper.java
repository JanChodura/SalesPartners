package com.jzchodura.salespartners.mapper.api;

import com.jzchodura.salespartners.generated.dto.ContactResponse;
import com.jzchodura.salespartners.generated.dto.ContactPositionDTO;
import com.jzchodura.salespartners.generated.dto.CreateContactRequest;
import com.jzchodura.salespartners.model.Contact;
import com.jzchodura.salespartners.model.ContactPosition;
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

    private ContactPosition toDomain(ContactPositionDTO position) {
        return position == null ? null : ContactPosition.valueOf(position.getValue());
    }

    private ContactPositionDTO toDto(ContactPosition position) {
        return position == null ? null : ContactPositionDTO.fromValue(position.name());
    }
}
