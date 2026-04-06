package com.jzchodura.salespartners.config;

import com.jzchodura.salespartners.model.Address;
import com.jzchodura.salespartners.model.Contact;
import com.jzchodura.salespartners.model.ContactPosition;
import com.jzchodura.salespartners.model.Identifier;
import com.jzchodura.salespartners.model.IdentifierType;
import com.jzchodura.salespartners.model.PartnerStatus;
import com.jzchodura.salespartners.model.SalesPartner;

import java.util.List;

final class PreparedData {

    private PreparedData() {
    }

    static SalesPartner northwindPartner() {
        return new SalesPartner(
            null,
            List.of(
                new Identifier("CZ55667788", IdentifierType.ICO),
                new Identifier("CZ55667788", IdentifierType.VAT)
            ),
            "Northwind Traders s.r.o.",
            List.of(new Address("CZ", "Olomouc", "77900", "Masarykova", "27")),
            PartnerStatus.STANDARD,
            List.of()
        );
    }

    static Contact northwindContact() {
        return new Contact(
            null,
            "Lucie",
            "Kralova",
            ContactPosition.EXECUTIVE,
            true,
            "+420",
            "605112233",
            "lucie.kralova@northwind.example"
        );
    }

    static SalesPartner initechPartner() {
        return new SalesPartner(
            null,
            List.of(
                new Identifier("CZ88990011", IdentifierType.ICO),
                new Identifier("CZ88990011", IdentifierType.VAT)
            ),
            "Initech Solutions a.s.",
            List.of(new Address("CZ", "Plzen", "30100", "Americka", "44")),
            PartnerStatus.GOLD,
            List.of()
        );
    }

    static Contact initechContact() {
        return new Contact(
            null,
            "Marek",
            "Benes",
            ContactPosition.TECHNICAL,
            true,
            "+420",
            "604998877",
            "marek.benes@initech.example"
        );
    }
}
