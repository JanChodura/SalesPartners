package com.jzchodura.salespartners.mapper.api;

import com.jzchodura.salespartners.generated.dto.AddressDTO;
import com.jzchodura.salespartners.generated.dto.CreatePartnerRequest;
import com.jzchodura.salespartners.generated.dto.IdentifierDTO;
import com.jzchodura.salespartners.generated.dto.IdentifierTypeDTO;
import com.jzchodura.salespartners.generated.dto.PartnerDetailResponse;
import com.jzchodura.salespartners.generated.dto.PartnerListItemDTO;
import com.jzchodura.salespartners.generated.dto.PartnerStatusDTO;
import com.jzchodura.salespartners.generated.dto.UpdatePartnerRequest;
import com.jzchodura.salespartners.model.Address;
import com.jzchodura.salespartners.model.Identifier;
import com.jzchodura.salespartners.model.IdentifierType;
import com.jzchodura.salespartners.model.PartnerStatus;
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

    public PartnerListItemDTO toListItem(SalesPartner partner) {
        PartnerListItemDTO item = new PartnerListItemDTO();
        item.setId(partner.id());
        item.setName(partner.name());
        item.setPartnerStatus(toDto(partner.state()));
        return item;
    }

    private List<Identifier> mapIdentifiersToDomain(List<IdentifierDTO> identifiers) {
        return safeList(identifiers).stream()
            .map(identifier -> new Identifier(
                identifier.getValue(),
                identifier.getType() == null
                    ? null
                    : IdentifierType.valueOf(identifier.getType().getValue())
            ))
            .toList();
    }

    private List<IdentifierDTO> mapIdentifiersToDto(List<Identifier> identifiers) {
        return safeList(identifiers).stream()
            .map(identifier -> new IdentifierDTO(
                identifier.value(),
                identifier.type() == null
                    ? null
                    : IdentifierTypeDTO.fromValue(identifier.type().name())
            ))
            .toList();
    }

    private List<Address> mapAddressesToDomain(List<AddressDTO> addresses) {
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

    private List<AddressDTO> mapAddressesToDto(List<Address> addresses) {
        return safeList(addresses).stream()
            .map(address -> new AddressDTO(
                address.country(),
                address.city()
            )
                .zipCode(address.zipCode())
                .street(address.street())
                .streetNumber(address.streetNumber()))
            .toList();
    }

    private PartnerStatus toDomain(PartnerStatusDTO status) {
        return status == null ? null : PartnerStatus.valueOf(status.getValue());
    }

    private PartnerStatusDTO toDto(PartnerStatus status) {
        return status == null ? null : PartnerStatusDTO.fromValue(status.name());
    }

    private <T> List<T> safeList(List<T> values) {
        return values == null ? Collections.emptyList() : values;
    }
}
