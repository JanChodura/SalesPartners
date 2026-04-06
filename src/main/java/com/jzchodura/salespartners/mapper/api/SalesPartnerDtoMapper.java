package com.jzchodura.salespartners.mapper.api;

import com.jzchodura.salespartners.generated.dto.CreatePartnerRequest;
import com.jzchodura.salespartners.generated.dto.PartnerDetailResponse;
import com.jzchodura.salespartners.generated.dto.PartnerListItem;
import com.jzchodura.salespartners.generated.dto.UpdatePartnerRequest;
import com.jzchodura.salespartners.model.Address;
import com.jzchodura.salespartners.model.Identifier;
import com.jzchodura.salespartners.model.SalesPartner;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;

@Component
public class SalesPartnerDtoMapper {

    private final ContactDtoMapper contactDtoMapper;

    public SalesPartnerDtoMapper(ContactDtoMapper contactDtoMapper) {
        this.contactDtoMapper = contactDtoMapper;
    }

    public SalesPartner toDomain(CreatePartnerRequest request) {
        return new SalesPartner(
            null,
            mapIdentifiersToDomain(request.getIdentifiers()),
            request.getName(),
            mapAddressesToDomain(request.getAddresses()),
            null,
            List.of()
        );
    }

    public SalesPartner toDomain(UpdatePartnerRequest request) {
        return new SalesPartner(
            null,
            mapIdentifiersToDomain(request.getIdentifiers()),
            request.getName(),
            mapAddressesToDomain(request.getAddresses()),
            toDomain(request.getPartnerStatus()),
            List.of()
        );
    }

    public PartnerDetailResponse toDetailResponse(SalesPartner partner) {
        PartnerDetailResponse response = new PartnerDetailResponse();
        response.setId(partner.id());
        response.setName(partner.name());
        response.setPartnerStatus(toDto(partner.state()));
        response.setIdentifiers(mapIdentifiersToDto(partner.identifiers()));
        response.setAddresses(mapAddressesToDto(partner.addresses()));
        response.setContacts(
            safeList(partner.contacts()).stream()
                .map(contactDtoMapper::toResponse)
                .toList()
        );
        return response;
    }

    public PartnerListItem toListItem(SalesPartner partner) {
        PartnerListItem item = new PartnerListItem();
        item.setId(partner.id());
        item.setName(partner.name());
        item.setPartnerStatus(toDto(partner.state()));
        return item;
    }

    private List<Identifier> mapIdentifiersToDomain(List<com.jzchodura.salespartners.generated.dto.Identifier> identifiers) {
        return safeList(identifiers).stream()
            .map(identifier -> new Identifier(
                identifier.getValue(),
                identifier.getType() == null
                    ? null
                    : com.jzchodura.salespartners.model.IdentifierType.valueOf(identifier.getType().getValue())
            ))
            .toList();
    }

    private List<com.jzchodura.salespartners.generated.dto.Identifier> mapIdentifiersToDto(List<Identifier> identifiers) {
        return safeList(identifiers).stream()
            .map(identifier -> new com.jzchodura.salespartners.generated.dto.Identifier(
                identifier.value(),
                identifier.type() == null
                    ? null
                    : com.jzchodura.salespartners.generated.dto.IdentifierType.fromValue(identifier.type().name())
            ))
            .toList();
    }

    private List<Address> mapAddressesToDomain(List<com.jzchodura.salespartners.generated.dto.Address> addresses) {
        return safeList(addresses).stream()
            .map(address -> new Address(
                address.getCountry(),
                address.getCity(),
                address.getZipCode(),
                address.getStreet(),
                address.getStreetNumber()
            ))
            .toList();
    }

    private List<com.jzchodura.salespartners.generated.dto.Address> mapAddressesToDto(List<Address> addresses) {
        return safeList(addresses).stream()
            .map(address -> new com.jzchodura.salespartners.generated.dto.Address(
                address.country(),
                address.city()
            )
                .zipCode(address.zipCode())
                .street(address.street())
                .streetNumber(address.streetNumber()))
            .toList();
    }

    private com.jzchodura.salespartners.model.PartnerStatus toDomain(
        com.jzchodura.salespartners.generated.dto.PartnerStatus status
    ) {
        return status == null ? null : com.jzchodura.salespartners.model.PartnerStatus.valueOf(status.getValue());
    }

    private com.jzchodura.salespartners.generated.dto.PartnerStatus toDto(
        com.jzchodura.salespartners.model.PartnerStatus status
    ) {
        return status == null ? null : com.jzchodura.salespartners.generated.dto.PartnerStatus.fromValue(status.name());
    }

    private <T> List<T> safeList(List<T> values) {
        return values == null ? Collections.emptyList() : values;
    }
}
