package com.jzchodura.salespartners.service;

import com.jzchodura.salespartners.model.PartnerStatus;
import com.jzchodura.salespartners.model.SalesPartner;
import com.jzchodura.salespartners.repository.SalesPartnerRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

@Service
public class SalesPartnerServiceImpl implements SalesPartnerService {

    private final SalesPartnerRepository salesPartnerRepository;

    public SalesPartnerServiceImpl(SalesPartnerRepository salesPartnerRepository) {
        this.salesPartnerRepository = salesPartnerRepository;
    }

    @Override
    public SalesPartner create(SalesPartner partner) {
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
            .orElseThrow(() -> new NoSuchElementException("Partner with id %s was not found.".formatted(partnerId)));
    }

    @Override
    public SalesPartner updatePartner(UUID partnerId, SalesPartner partner) {
        return salesPartnerRepository.updatePartner(partnerId, partner);
    }
}
