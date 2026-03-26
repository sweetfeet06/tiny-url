package com.origin.takehome.controller;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.origin.takehome.domain.ShortUriMap;
import com.origin.takehome.exception.NotFoundException;
import com.origin.takehome.service.UrlService;
import com.origin.takehome.util.TokenGenerator;

@WebMvcTest(controllers = {UrlRedirectController.class})
class UrlRedirectControllerTest {
    
    @Autowired
    private MockMvc mockMvc;
    
    @MockitoBean
    private UrlService urlService;
    
    @Test
    void shouldReturn404WithShortUriNotInSystem() throws Exception {
        String shortUri = TokenGenerator.token();
        
        when(urlService.expand(shortUri)).thenThrow(new NotFoundException("No short URI [" + shortUri +"] found in system"));
        
        mockMvc.perform(
                get("/" + shortUri)
        ).andExpect(status().isNotFound());
    }
    
    @Test
    void shouldRedirectIfGetWithExistingShortUri() throws Exception {
        String shortUri = TokenGenerator.token();
        String originalUrl = "http://localhost/" + shortUri;
        ShortUriMap expectedShortUri = new ShortUriMap(shortUri, originalUrl);
        
        when(urlService.expand(shortUri)).thenReturn(expectedShortUri);
        

        // When & Then
        mockMvc.perform(
                get("/" + shortUri)
        ).andExpect(status().is3xxRedirection())
        .andExpect(redirectedUrl("http://localhost/" + shortUri));;
    }

}
