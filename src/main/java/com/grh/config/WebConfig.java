package com.grh.config;

import jakarta.persistence.EntityManager;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    private final EntityManager entityManager;

    public WebConfig(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public void addFormatters(FormatterRegistry registry) {
        registry.addConverter(new EntiteConverter(entityManager));
    }
}
