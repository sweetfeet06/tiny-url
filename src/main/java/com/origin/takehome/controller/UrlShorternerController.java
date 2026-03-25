package com.origin.takehome.controller;

import java.net.URI;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.origin.takehome.domain.ShortUriMap;
import com.origin.takehome.service.UrlService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

@RestController
public class UrlShorternerController {
    
    @Autowired
    private UrlService urlService;
    
    @PostMapping("/v1/short-url")
    @Operation(summary = "Create new short url", description = "Create a new short url in the system")
    @ApiResponses(
        value = {
            @ApiResponse(responseCode = "201", description = "short url created successfully",
                    content = @Content(mediaType = "plain/text")),
            @ApiResponse(responseCode = "400", description = "Invalid URL format")
    })
    public ResponseEntity<String> createShortUrl(
            @Parameter(description = "Original URL to be shortened", required = true)
            @RequestBody String originalUrl
    ) {
        ShortUriMap shortUri = urlService.shorten(originalUrl);
        URI baseUrl = ServletUriComponentsBuilder.fromCurrentContextPath().build().toUri();
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(String.format("http://%s/%s", baseUrl.getHost(), shortUri.getShortUri()));
    }
}
