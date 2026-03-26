package com.origin.takehome.controller;

import java.net.URI;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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
@RequestMapping("/v1/short-url")
public class UrlShorternerController {
    private static final Logger logger = LoggerFactory.getLogger(UrlShorternerController.class);
    
    @Autowired
    private UrlService urlService;
    
    @PostMapping
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
        logger.info("shortening " + originalUrl);
        ShortUriMap shortUri = urlService.shorten(originalUrl);
        URI baseUrl = baseUrl();
        String port = port();

        String shortUrl = shortUrl(shortUri, baseUrl, port);
        
        logger.info(String.format("shortened url for %s [%s]", originalUrl, shortUrl));
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(shortUrl);
    }
    
    @GetMapping
    @Operation(
            summary = "Ger short uri information", 
            description = "Returns the data for the short URI map if present"
    )
    @ApiResponses(
        value = {
            @ApiResponse(responseCode = "200", description = "Short uri map retreived successfuly",
                    content = @Content(mediaType = "plain/text")),
            @ApiResponse(responseCode = "404", description = "Short uri map not found")
    })
    public ResponseEntity<ShortUriMap> getShortUrl(
            @Parameter(description = "Short uri that mapping is looked for", required = true)
            @RequestParam(value = "shortUri", required = true) String shortUri
    ) {
        logger.info(String.format("Looking for short URI mapping to [%s]", shortUri));
        ShortUriMap shortUriMap = urlService.expand(shortUri);
        
        logger.info(String.format("shortened url for %s [%s]", shortUriMap.getOriginalUrl(), shortUriMap.getShortUri()));
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(shortUriMap);
    }

    private String shortUrl(ShortUriMap shortUri, URI baseUrl, String port) {
        return String.format("http://%s%s/%s", baseUrl.getHost(), port, shortUri.getShortUri());
    }

    private URI baseUrl() {
        return ServletUriComponentsBuilder.fromCurrentContextPath().build().toUri();
    }

    private String port() {
        var uriComponent = ServletUriComponentsBuilder.fromCurrentRequest().build();
        int port = uriComponent.getPort();
        String portString = "";
        if (port == -1) {
            String scheme = uriComponent.getScheme();
            if ("https".equalsIgnoreCase(scheme)) {
                portString = "443";
            }
        } else {
            portString = ":" + Integer.toString(port);
        } 
        return portString;
    }
}
