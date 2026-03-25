package com.origin.takehome.domain;

import java.util.UUID;

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
        this(UUID.randomUUID().toString(), originalUrl);
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
