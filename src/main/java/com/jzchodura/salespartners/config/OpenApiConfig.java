package com.jzchodura.salespartners.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.tags.Tag;
import org.springdoc.core.customizers.OpenApiCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Comparator;
import java.util.List;
import java.util.Map;

@Configuration
public class OpenApiConfig {

    private static final Map<String, Integer> TAG_ORDER = Map.of(
        "partner", 0,
        "contact", 1
    );

    @Bean
    OpenApiCustomizer tagOrderCustomizer() {
        return openApi -> openApi.setTags(sortedTags(openApi));
    }

    private List<Tag> sortedTags(OpenAPI openApi) {
        List<Tag> tags = openApi.getTags();
        if (tags == null || tags.isEmpty()) {
            return tags;
        }

        return tags.stream()
            .sorted(Comparator
                .comparingInt((Tag tag) -> TAG_ORDER.getOrDefault(tag.getName(), Integer.MAX_VALUE))
                .thenComparing(Tag::getName))
            .toList();
    }
}
