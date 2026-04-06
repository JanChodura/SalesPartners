package com.jzchodura.salespartners.service;

import com.jzchodura.salespartners.exception.ResourceNotFoundException;
import com.jzchodura.salespartners.model.PartnerStatus;
import com.jzchodura.salespartners.model.SalesPartner;
import com.jzchodura.salespartners.repository.SalesPartnerRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class SalesPartnerServiceImpl implements SalesPartnerService {

    private final SalesPartnerRepository salesPartnerRepository;

    public SalesPartnerServiceImpl(SalesPartnerRepository salesPartnerRepository) {
        this.salesPartnerRepository = salesPartnerRepository;
    }

    @Override
    public SalesPartner create(SalesPartner partner) {
        validate(partner);

        SalesPartner partnerToCreate = new SalesPartner(
            null,
            partner.identifiers(),
            partner.name(),
            partner.addresses(),
            partner.state() == null ? PartnerStatus.NEW : partner.state(),
            List.of()
        );
        return salesPartnerRepository.createPartner(partnerToCreate);
    }

    @Override
    public List<SalesPartner> getPartners() {
        return salesPartnerRepository.findAllPartners();
    }

    @Override
    public SalesPartner getPartnerDetail(UUID partnerId) {
        return salesPartnerRepository.findPartnerById(partnerId)
            .orElseThrow(() -> new ResourceNotFoundException("Partner with id %s was not found.".formatted(partnerId)));
    }

    @Override
    public SalesPartner updatePartner(UUID partnerId, SalesPartner partner) {
        validate(partner);
        return salesPartnerRepository.updatePartner(partnerId, partner);
    }

    private void validate(SalesPartner partner) {
        if (partner.name() == null || partner.name().isBlank()) {
            throw new IllegalArgumentException("Partner name must not be blank.");
        }
        if (partner.identifiers() == null || partner.identifiers().isEmpty()) {
            throw new IllegalArgumentException("Partner must contain at least one identifier.");
        }
        if (partner.addresses() == null || partner.addresses().isEmpty()) {
            throw new IllegalArgumentException("Partner must contain at least one address.");
        }
    }
}
