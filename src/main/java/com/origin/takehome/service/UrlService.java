package com.origin.takehome.service;

import org.springframework.stereotype.Service;

import com.origin.takehome.domain.ShortUriMap;
import com.origin.takehome.exception.NotFoundException;
import com.origin.takehome.repository.ShortUriRepository;

@Service
public class UrlService {
    
    private ShortUriRepository repository;

    public UrlService(ShortUriRepository repository) {
        this.repository = repository;
    }

    public ShortUriMap shorten(String originalUrl) {
        var shortUri = new ShortUriMap(originalUrl);
        repository.save(shortUri);
        return shortUri;
    }

    public ShortUriMap expand(String id) {
        var shortUri = repository.findById(id);
        if (shortUri.isEmpty()) {
            throw new NotFoundException("No shortUri for [" + id + "]");
        }
        return shortUri.get();
    }

}
