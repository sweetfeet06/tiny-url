package com.origin.takehome.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.Optional;
import java.util.UUID;

import org.apache.commons.validator.routines.UrlValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.origin.takehome.domain.ShortUriMap;
import com.origin.takehome.exception.NotFoundException;
import com.origin.takehome.repository.ShortUriRepository;

@ExtendWith(MockitoExtension.class)
class UrlServiceTest {
    
    @Mock
    private ShortUriRepository repository;

    @Mock
    private UrlValidator validator;
    
    @InjectMocks
    private UrlService service;

    @BeforeEach
    void setUp() throws Exception {
        service = new UrlService(repository, validator);
        when(repository.save(any(ShortUriMap.class))).thenAnswer(i -> i.getArguments()[0]);
        when(validator.isValid(any(String.class))).thenReturn(true);
    }

    @Test
    void testShortenWithNewUrl() {
        //Given
        var originalUrl = "http://some.address/for/test";
        
        //When
        var result = service.shorten(originalUrl);
        
        //Then
        assertThat(result)
            .hasFieldOrProperty("shortUri").isNotNull()
            .hasFieldOrPropertyWithValue("originalUrl", originalUrl);
    }
    
    @Test
    void testExpandWithNonExistentShortUri() {
        //Given
        var shortUri = UUID.randomUUID().toString();
        
        //When then
        assertThrows(
            NotFoundException.class, 
            () -> {
                service.expand(shortUri); 
            }
        );
        
    }
    
    @Test
    void testExpandWithExistingShortUri() {
        //Given
        var originalUrl = "http://some.address/for/test";
        var shortUriMap = new ShortUriMap(originalUrl);
        when(repository.findById(any(String.class))).thenReturn(Optional.of(shortUriMap));
        
        //When
        var result = service.expand(shortUriMap.getShortUri());
        
        //Then
        assertThat(result)
            .hasFieldOrPropertyWithValue("shortUri", shortUriMap.getShortUri())
            .hasFieldOrPropertyWithValue("originalUrl", shortUriMap.getOriginalUrl());
    }

}
