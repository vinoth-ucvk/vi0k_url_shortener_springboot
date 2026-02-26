package com.example.urlshortner.controller;

import com.example.urlshortner.service.UrlService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequiredArgsConstructor
public class UrlController {
    private final UrlService service;


    @PostMapping("/shorten")
    public ResponseEntity<String> shorten(@RequestParam String url) {
        String shortUrl = service.shortenUrl(url);
        return ResponseEntity.ok(shortUrl);
    }

    @GetMapping("/{shortCode}")
    public ResponseEntity<Void> redirect(@PathVariable String shortCode) {
        String originalUrl = service.getOriginalUrl(shortCode);

        return ResponseEntity
                .status(HttpStatus.FOUND)
                .location(URI.create(originalUrl))
                .build();
    }
}
