package com.origin.takehome.domain;

import com.origin.takehome.util.TokenGenerator;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class ShortUriMap {
    
    @Id
    private String shortUri;
    
    private String originalUrl;
    
    public ShortUriMap() {
    }
    
    public ShortUriMap(String originalUrl) {
        this(TokenGenerator.token(), originalUrl);
    }
    
    public ShortUriMap(String shortUri, String originalUrl) {
        this.shortUri = shortUri;
        this.originalUrl = originalUrl;
    }

    public String getShortUri() {
        return shortUri;
    }

    public String getOriginalUrl() {
        return originalUrl;
    }

}
