package com.jzchodura.salespartners.service;

import com.jzchodura.salespartners.model.SalesPartner;

import java.util.List;
import java.util.UUID;

public interface SalesPartnerService {

    SalesPartner create(SalesPartner partner);

    List<SalesPartner> getPartners();

    SalesPartner getPartnerDetail(UUID partnerId);

    SalesPartner updatePartner(UUID partnerId, SalesPartner partner);
}
