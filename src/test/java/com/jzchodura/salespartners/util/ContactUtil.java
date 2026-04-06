package com.jzchodura.salespartners.util;

import com.jzchodura.salespartners.model.Contact;
import com.jzchodura.salespartners.model.ContactPosition;

public final class ContactUtil {

    private ContactUtil() {
    }

    public static Contact createdContact() {
        return new Contact(
            TestIdsUtil.CONTACT_ID,
            "Jan",
            "Novák",
            ContactPosition.SALES,
            true,
            "+420",
            "777888999",
            "jan.novak@example.com"
        );
    }

    public static Contact updatedContact() {
        return new Contact(
            TestIdsUtil.CONTACT_ID,
            "Petr",
            "Svoboda",
            ContactPosition.ACCOUNT_MANAGER,
            false,
            "+420",
            "111222333",
            "petr.svoboda@example.com"
        );
    }
}
