package com.origin.takehome.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.origin.takehome.exception.InvalidUrlException;
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
        String shortUri = UUID.randomUUID().toString();
        
        when(urlService.shorten(any(String.class))).thenReturn(shortUri);
        

        // When & Then
        mockMvc.perform(
                post("/v1/short-url").content("http://some.address/uri")
        ).andExpect(status().isCreated())
            .andExpect(content().contentType("text/plain;charset=UTF-8"))
            .andExpect(content().string("http://localhost/" + shortUri));
    }

}
