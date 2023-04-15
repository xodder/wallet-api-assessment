package com.fundquest.assessment.lib.helpers;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.fasterxml.jackson.datatype.hibernate5.jakarta.Hibernate5JakartaModule;

@Configuration
public class JacksonConfig {
    @Bean
    public com.fasterxml.jackson.databind.Module hibernate5JakartaModule() {
        return new Hibernate5JakartaModule();
    }
}
