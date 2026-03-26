package com.origin.takehome.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.origin.takehome.domain.ShortUriMap;
import com.origin.takehome.exception.InvalidUrlException;
import com.origin.takehome.exception.NotFoundException;
import com.origin.takehome.service.UrlService;

@WebMvcTest(controllers = {UrlShorternerController.class})
class UrlShortenerControllerTest {
    
    @Autowired
    private MockMvc mockMvc;
    
    @MockitoBean
    private UrlService urlService;
    
    @Test 
    void shouldReturn400ForInvalidUrl() throws Exception {
        when(urlService.shorten(any(String.class))).thenThrow(new InvalidUrlException("Invalid URL"));
        
        mockMvc.perform(
            post("/v1/short-url").content("some.address/uri")
        ).andExpect(status().isBadRequest());
    }

    @Test
    void shouldReturnShortUriForLongUrl() throws Exception {
        String originalUrl = "http://some.address/uri";
        String shortUri = UUID.randomUUID().toString();
        ShortUriMap expectedShortUri = new ShortUriMap(shortUri, originalUrl);
        
        when(urlService.shorten(any(String.class))).thenReturn(expectedShortUri);
        

        // When & Then
        mockMvc.perform(
           post("/v1/short-url").content(originalUrl)
        ).andExpect(status().isCreated())
            .andExpect(content().contentType("text/plain;charset=UTF-8"))
            .andExpect(content().string("http://localhost/" + shortUri));
    }
    
    @Test
    void shouldReturn404ForShortUriWhenNotPresent() throws Exception {
        String shortUri = UUID.randomUUID().toString();
        
        
        when(urlService.expand(shortUri)).thenThrow(new NotFoundException("No short URI [" + shortUri +"] found in system"));
        

        mockMvc.perform(
           get("/v1/short-url").param("shortUri", shortUri)
        ).andExpect(status().isNotFound());
    }
    
    @Test
    void shouldReturnShortUriMapForShortUriWhenPresent() throws Exception {
        String originalUrl = "http://some.other.address/uri";
        String shortUri = UUID.randomUUID().toString();
        ShortUriMap expectedShortUriMap = new ShortUriMap(shortUri, originalUrl);
        
        
        when(urlService.expand(shortUri)).thenReturn(expectedShortUriMap);
        

        mockMvc.perform(
           get("/v1/short-url")
               .param("shortUri", shortUri)
               .accept(MediaType.APPLICATION_JSON)
        ).andExpect(status().isCreated())
            .andExpect(content().contentType("application/json"))
            .andExpect(content().string(String.format("{\"originalUrl\":\"%s\",\"shortUri\":\"%s\"}", originalUrl , shortUri)));
    }

}
