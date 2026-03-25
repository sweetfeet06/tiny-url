package com.origin.takehome.controller;

import java.net.URI;
import java.net.URISyntaxException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.origin.takehome.service.UrlService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

@RestController
public class UrlRedirectController {
    
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
//        String originalUrl = urlService.expand(shortUri);
        String originalUrl = urlService.expand(shortUri);
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setLocation(new URI(originalUrl));
        return new ResponseEntity<>(httpHeaders, HttpStatus.SEE_OTHER);
    }

}
