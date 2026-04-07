package com.jzchodura.salespartners.controller;

import com.jzchodura.salespartners.generated.api.PartnerApiDelegate;
import com.jzchodura.salespartners.generated.dto.CreatePartnerRequest;
import com.jzchodura.salespartners.generated.dto.PartnerDetailResponse;
import com.jzchodura.salespartners.generated.dto.PartnerListItemDTO;
import com.jzchodura.salespartners.generated.dto.UpdatePartnerRequest;
import com.jzchodura.salespartners.mapper.api.SalesPartnerDtoMapper;
import com.jzchodura.salespartners.service.SalesPartnerService;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.UUID;

@Component
@ConditionalOnBean(SalesPartnerService.class)
public class PartnerApiDelegateAdapter implements PartnerApiDelegate {

    private final SalesPartnerService salesPartnerService;
    private final SalesPartnerDtoMapper salesPartnerDtoMapper;

    public PartnerApiDelegateAdapter(
        SalesPartnerService salesPartnerService,
        SalesPartnerDtoMapper salesPartnerDtoMapper
    ) {
        this.salesPartnerService = salesPartnerService;
        this.salesPartnerDtoMapper = salesPartnerDtoMapper;
    }

    @Override
    public ResponseEntity<PartnerDetailResponse> createPartner(CreatePartnerRequest createPartnerRequest) {
        PartnerDetailResponse response = salesPartnerDtoMapper.toDetailResponse(
            salesPartnerService.create(salesPartnerDtoMapper.toDomain(createPartnerRequest))
        );
        return ResponseEntity.created(partnerUri(response.getId())).body(response);
    }

    @Override
    public ResponseEntity<List<PartnerListItemDTO>> getPartners() {
        List<PartnerListItemDTO> response = salesPartnerService.getPartners().stream()
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

    private URI partnerUri(UUID partnerId) {
        return ServletUriComponentsBuilder.fromCurrentRequestUri()
            .path("/{partnerId}")
            .buildAndExpand(partnerId)
            .toUri();
    }
}
