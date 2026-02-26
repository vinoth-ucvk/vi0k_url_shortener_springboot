package com.example.urlshortner.service;

import com.example.urlshortner.entity.UrlMapping;
import com.example.urlshortner.repository.UrlRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UrlService {

    private final UrlRepository repository;
    private static final String BASE_URL = "http://localhost:8080/";
    private static final String BASE62 = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    private static final int SHORT_CODE_LENGTH = 6;

    public UrlService(UrlRepository repository) {
        this.repository = repository;
    }

    @Transactional
    public String shortenUrl(String longUrl) {
        return repository.findByLongUrl(longUrl)
                .map(url -> BASE_URL + url.getShortCode())
                .orElseGet(() -> {
                    UrlMapping mapping = new UrlMapping();
                    mapping.setLongUrl(longUrl);

                    // Persist entity; Hibernate assigns ID immediately
                    repository.save(mapping);

                    // ID is now available
                    String shortCode = padLeft(encodeBase62(mapping.getId()));
                    mapping.setShortCode(shortCode);

                    // No need to call save() again; flush will update before commit
                    return BASE_URL + shortCode;
                });
    }

    public String getOriginalUrl(String shortCode) {
        return repository.findByShortCode(shortCode)
                .map(UrlMapping::getLongUrl)
                .orElseThrow(() -> new RuntimeException("URL not found"));
    }

    private String encodeBase62(Long id) {
        StringBuilder sb = new StringBuilder();

        while (id > 0) {
            sb.append(BASE62.charAt((int) (id % 62)));
            id /= 62;
        }

        return sb.reverse().toString();
    }

    private String padLeft(String input) {
        StringBuilder sb = new StringBuilder();

        for (int i = input.length(); i < SHORT_CODE_LENGTH; i++) {
            sb.append('0');   // padding character
        }

        sb.append(input);
        return sb.toString();
    }
}