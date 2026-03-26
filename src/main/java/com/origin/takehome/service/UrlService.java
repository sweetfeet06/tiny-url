package com.origin.takehome.service;

import org.apache.commons.validator.routines.UrlValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.origin.takehome.domain.ShortUriMap;
import com.origin.takehome.exception.InvalidUrlException;
import com.origin.takehome.exception.NotFoundException;
import com.origin.takehome.repository.ShortUriRepository;

@Service
public class UrlService {
    
    private ShortUriRepository repository;
    
    private UrlValidator validator;

    @Autowired
    public UrlService(
        ShortUriRepository repository, 
        UrlValidator urlValidator
    ) {
        this.repository = repository;
        validator = urlValidator;
    }

    public ShortUriMap shorten(String originalUrl) {
        if (!validator.isValid(originalUrl)) {
            throw new InvalidUrlException("url = " + originalUrl);
        }
        
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
