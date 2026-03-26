package com.origin.takehome.controller;

import java.net.URI;
import java.net.URISyntaxException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.origin.takehome.domain.ShortUriMap;
import com.origin.takehome.service.UrlService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

@RestController
public class UrlRedirectController {
    private static final Logger logger = LoggerFactory.getLogger(UrlRedirectController.class);
    
    @Autowired
    private UrlService urlService;
    
    @GetMapping("/{shortUri}")
    @Operation(
            summary = "Redirect caller to URL associated with given short Uri", 
            description = "Retrieve URL by short URI and redirect user"
    )
    @ApiResponses(
        value = {
            @ApiResponse(responseCode = "200", description = "Short URI found and redirection to orginal URL",
                    content=@Content(mediaType = "plain/text")
            ),
            @ApiResponse(responseCode = "404", description = "Short URI not found")
    })
    public ResponseEntity<Void> redirectToUrl(@PathVariable String shortUri) throws URISyntaxException {
        logger.info(String.format("finding URL for uri [%s]", shortUri));
        ShortUriMap shortUriMapping = urlService.expand(shortUri);
        
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setLocation(new URI(shortUriMapping.getOriginalUrl()));
        
        logger.info(String.format("redirecting to[%s]", shortUriMapping.getOriginalUrl()));
        return new ResponseEntity<>(httpHeaders, HttpStatus.SEE_OTHER);
    }

}
