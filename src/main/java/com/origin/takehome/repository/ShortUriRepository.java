package com.origin.takehome.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.origin.takehome.domain.ShortUriMap;

@Repository
public interface ShortUriRepository extends CrudRepository<ShortUriMap, String> {

    List<ShortUriMap> findByOriginalUrl(String originalUrl);

}
