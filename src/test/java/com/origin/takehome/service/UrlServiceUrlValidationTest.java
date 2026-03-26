package com.origin.takehome.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.stream.Stream;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import com.origin.takehome.exception.InvalidUrlException;
import com.origin.takehome.repository.ShortUriRepository;

@SpringBootTest
public class UrlServiceUrlValidationTest {
    
    @MockitoBean
    private ShortUriRepository repository;
    
    @Autowired
    private UrlService service;
    
    @ParameterizedTest
    @MethodSource("urlToResult")
    void testValidatesUrlWithValidUrl(String originalUrl, boolean isValidUrl) {
        if (isValidUrl) {
            //When
            var result = service.shorten(originalUrl);
            
            //Then
            assertThat(result)
                .hasFieldOrProperty("shortUri").isNotNull()
                .hasFieldOrPropertyWithValue("originalUrl", originalUrl);
        } else {
            assertThrows(
                InvalidUrlException.class, 
                () -> {
                    service.shorten(originalUrl); 
                }
            );
        }
    }
    
    @SuppressWarnings("unused")
    private static Stream<Arguments> urlToResult() {
        return Stream.of(
          Arguments.of("http://www.google.com", true),
          Arguments.of("https://example.com", true),
          Arguments.of("http://192.168.0.1", true), // URLs with IP addresses
          Arguments.of("http://localhost:8080", true), //local URL
          Arguments.of("http://localhost:8080", true), //external with port
          
          Arguments.of("", false), // Empty string
          Arguments.of("justAString", false), // Not a URL format
          Arguments.of("http:/missing/slash", false), // Malformed protocol
          Arguments.of("ftp://ftp.site.com", false), // Ftp is not in the allowed schemes
          Arguments.of("http://.com", false), // Invalid domain format
          Arguments.of("http://invalid_url", false), // Invalid hostname (e.g., no TLD)
          Arguments.of("http://www.site with spaces.com", false), // Spaces are invalid in hostname
          Arguments.of("http://example.com:99999", false), // Invalid port number (out of range)
          Arguments.of(null, false) // Null input
        );
    }

}
