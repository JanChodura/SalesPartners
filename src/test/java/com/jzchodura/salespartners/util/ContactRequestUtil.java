package com.jzchodura.salespartners.util;

import com.jzchodura.salespartners.generated.dto.ContactPosition;
import com.jzchodura.salespartners.generated.dto.CreateContactRequest;

public final class ContactRequestUtil {

    private ContactRequestUtil() {
    }

    public static CreateContactRequest createContactRequest() {
        CreateContactRequest request = new CreateContactRequest("Jan", "Novák");
        request.setPosition(ContactPosition.SALES);
        request.setIsPrimary(true);
        request.setCountryCallingCode("+420");
        request.setPhoneNumber("777888999");
        request.setEmail("jan.novak@example.com");
        return request;
    }

    public static CreateContactRequest updateContactRequest() {
        CreateContactRequest request = new CreateContactRequest("Petr", "Svoboda");
        request.setPosition(ContactPosition.ACCOUNT_MANAGER);
        request.setIsPrimary(false);
        request.setCountryCallingCode("+420");
        request.setPhoneNumber("111222333");
        request.setEmail("petr.svoboda@example.com");
        return request;
    }
}
