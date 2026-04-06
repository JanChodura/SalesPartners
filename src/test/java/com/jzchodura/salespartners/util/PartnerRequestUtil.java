package com.jzchodura.salespartners.util;

import com.jzchodura.salespartners.generated.dto.Address;
import com.jzchodura.salespartners.generated.dto.CreatePartnerRequest;
import com.jzchodura.salespartners.generated.dto.Identifier;
import com.jzchodura.salespartners.generated.dto.IdentifierType;
import com.jzchodura.salespartners.generated.dto.PartnerStatus;
import com.jzchodura.salespartners.generated.dto.UpdatePartnerRequest;

import java.util.List;

public final class PartnerRequestUtil {

    private PartnerRequestUtil() {
    }

    public static CreatePartnerRequest createPartnerRequest() {
        CreatePartnerRequest request = new CreatePartnerRequest("Acme");
        request.setIdentifiers(List.of(new Identifier("CZ12345678", IdentifierType.ICO)));
        request.setAddresses(List.of(new Address("CZ", "Prague").street("Main").streetNumber("12").zipCode("11000")));
        return request;
    }

    public static UpdatePartnerRequest updatePartnerRequest() {
        UpdatePartnerRequest request = new UpdatePartnerRequest("Acme Gold");
        request.setIdentifiers(List.of(new Identifier("CZ12345678", IdentifierType.ICO)));
        request.setAddresses(List.of(new Address("CZ", "Brno").street("Updated").streetNumber("99").zipCode("60200")));
        request.setPartnerStatus(PartnerStatus.GOLD);
        return request;
    }
}
