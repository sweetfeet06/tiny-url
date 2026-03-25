package com.origin.takehome.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.jpa.test.autoconfigure.TestEntityManager;

import com.origin.takehome.domain.ShortUriMap;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.ANY)
class ShortUriRepositoryTest {

    @Autowired
    private ShortUriRepository repository;

    @Autowired
    private TestEntityManager entityManager;

    @Test
    void testFindById() {
        // Given
        String originalUrl = "http://anaddress.com/some/large/uri";
        ShortUriMap shortUriMap = new ShortUriMap(originalUrl);
        entityManager.persist(shortUriMap); // Use TestEntityManager to save data

        // When
        Optional<ShortUriMap> foundShortUri = repository.findById(shortUriMap.getShortUri());

        // Then
        assertThat(foundShortUri).isNotEmpty();
        assertThat(foundShortUri.get().getOriginalUrl()).isEqualTo(originalUrl);
    }

    @Test
    void testFindByOriginalUrl() {
        // Given
        ShortUriMap shortUriMap1 = new ShortUriMap("http://anaddress.com/some/large/uri");
        ShortUriMap shortUriMap2 = new ShortUriMap("http://anotheraddress.com/another/large/uri");
        entityManager.persist(shortUriMap1);
        entityManager.persist(shortUriMap2);

        // When
        List<ShortUriMap> shortUris = repository.findByOriginalUrl("http://anotheraddress.com/another/large/uri"); // Assuming you have this custom method

        // Then
        assertThat(shortUris).hasSize(1);
        assertThat(shortUris.get(0))
            .hasFieldOrPropertyWithValue("shortUri", shortUriMap2.getShortUri())
            .hasFieldOrPropertyWithValue("originalUrl", shortUriMap2.getOriginalUrl());
    }
    
    @Test
    void testSaveShortUriMap() {
        // Given
        ShortUriMap shortUriMap = new ShortUriMap("http://anaddress.com/some/large/uri");
        
        //When
        repository.save(shortUriMap);
        
        //Then
        var result = entityManager.find(ShortUriMap.class, shortUriMap.getShortUri());
        assertThat(result)
            .hasFieldOrPropertyWithValue("shortUri", shortUriMap.getShortUri())
            .hasFieldOrPropertyWithValue("originalUrl", shortUriMap.getOriginalUrl());
    }
}
