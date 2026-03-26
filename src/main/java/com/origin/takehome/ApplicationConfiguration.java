package com.origin.takehome;

import org.apache.commons.validator.routines.UrlValidator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ApplicationConfiguration {
    
    private String[] validSchemes = {"http","https"};
    
    @Bean
    public UrlValidator urlValidator() {
        var options = UrlValidator.ALLOW_LOCAL_URLS + UrlValidator.ALLOW_2_SLASHES;
        return new UrlValidator(validSchemes, options);
    }

}
