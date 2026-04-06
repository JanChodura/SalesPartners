package com.jzchodura.salespartners.util;

import com.jzchodura.salespartners.model.Address;
import com.jzchodura.salespartners.model.Identifier;
import com.jzchodura.salespartners.model.IdentifierType;
import com.jzchodura.salespartners.model.PartnerStatus;
import com.jzchodura.salespartners.model.SalesPartner;

import java.util.List;

public final class SalesPartnerUtil {

    private SalesPartnerUtil() {
    }

    public static SalesPartner createdPartner() {
        return new SalesPartner(
            TestIdsUtil.PARTNER_ID,
            List.of(new Identifier("CZ12345678", IdentifierType.ICO)),
            "Acme",
            List.of(new Address("CZ", "Prague", "11000", "Hlavni", "12")),
            PartnerStatus.NEW,
            List.of(ContactUtil.createdContact())
        );
    }

    public static SalesPartner updatedPartner() {
        return new SalesPartner(
            TestIdsUtil.PARTNER_ID,
            List.of(new Identifier("CZ12345678", IdentifierType.ICO)),
            "Acme Gold",
            List.of(new Address("CZ", "Brno", "60200", "Havlíčkova", "99")),
            PartnerStatus.GOLD,
            List.of(ContactUtil.updatedContact())
        );
    }
}
