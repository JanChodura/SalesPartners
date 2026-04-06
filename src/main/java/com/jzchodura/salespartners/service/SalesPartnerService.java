package com.jzchodura.salespartners.service;

import com.jzchodura.salespartners.model.SalesPartner;

import java.util.List;

public interface SalesPartnerService {

    SalesPartner createPartner(SalesPartner partner);

    List<SalesPartner> getPartners();

    SalesPartner getPartnerDetail(Long partnerId);

    SalesPartner updatePartner(Long partnerId, SalesPartner partner);
}
