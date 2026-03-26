package com.origin.takehome.service;

import org.apache.commons.validator.routines.UrlValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.origin.takehome.domain.ShortUriMap;
import com.origin.takehome.exception.InvalidUrlException;
import com.origin.takehome.exception.NotFoundException;
import com.origin.takehome.repository.ShortUriRepository;

@Service
public class UrlService {
    private static final Logger logger = LoggerFactory.getLogger(UrlService.class);
    
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
            logger.error(String.format("Invalid URL [%s]", originalUrl));
            throw new InvalidUrlException("url = " + originalUrl);
        }
        
        var shortUri = new ShortUriMap(originalUrl);
        repository.save(shortUri);
        return shortUri;
    }

    public ShortUriMap expand(String id) {
        logger.info(String.format("Finding URL for shortUri [%s]", id));
        var shortUri = repository.findById(id);
        if (shortUri.isEmpty()) {
            throw new NotFoundException("No shortUri for [" + id + "]");
        }
        return shortUri.get();
    }

}
