package com.jzchodura.salespartners.util;

import com.jzchodura.salespartners.generated.dto.AddressDTO;
import com.jzchodura.salespartners.generated.dto.CreatePartnerRequest;
import com.jzchodura.salespartners.generated.dto.IdentifierDTO;
import com.jzchodura.salespartners.generated.dto.IdentifierTypeDTO;
import com.jzchodura.salespartners.generated.dto.PartnerStatusDTO;
import com.jzchodura.salespartners.generated.dto.UpdatePartnerRequest;

import java.util.List;

public final class PartnerRequestUtil {

    private PartnerRequestUtil() {
    }

    public static CreatePartnerRequest createPartnerRequest() {
        CreatePartnerRequest request = new CreatePartnerRequest("Acme");
        request.setIdentifiers(List.of(new IdentifierDTO("CZ12345678", IdentifierTypeDTO.ICO)));
        request.setAddresses(List.of(new AddressDTO("CZ", "Praha").street("Main").streetNumber("12").zipCode("11000")));
        return request;
    }

    public static UpdatePartnerRequest updatePartnerRequest() {
        UpdatePartnerRequest request = new UpdatePartnerRequest("Acme Gold");
        request.setIdentifiers(List.of(new IdentifierDTO("CZ12345678", IdentifierTypeDTO.ICO)));
        request.setAddresses(List.of(new AddressDTO("CZ", "Brno").street("Updated").streetNumber("99").zipCode("60200")));
        request.setPartnerStatus(PartnerStatusDTO.GOLD);
        return request;
    }
}
