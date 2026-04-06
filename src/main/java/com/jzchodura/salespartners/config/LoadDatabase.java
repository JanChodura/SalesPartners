package com.jzchodura.salespartners.config;

import com.jzchodura.salespartners.model.SalesPartner;
import com.jzchodura.salespartners.service.ContactService;
import com.jzchodura.salespartners.service.SalesPartnerService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile("app")
class LoadDatabase {

    @Bean
    CommandLineRunner initDatabase(
        SalesPartnerService salesPartnerService,
        ContactService contactService
    ) {
        return args -> {
            if (!salesPartnerService.getPartners().isEmpty()) {
                return;
            }

            SalesPartner northwind = salesPartnerService.create(PreparedData.northwindPartner());
            contactService.add(northwind.id(), PreparedData.northwindContact());

            SalesPartner initech = salesPartnerService.create(PreparedData.initechPartner());
            contactService.add(initech.id(), PreparedData.initechContact());
        };
    }
}
