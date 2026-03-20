package com.finsight.crawl.api;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

public record CrawlRequest(
        String source,
        String url,
        String correlationId,
        Map<String, String> metadata
) {
    public CrawlRequest {
        source = normalize(source, "source");
        url = normalize(url, "url");
        correlationId = correlationId == null || correlationId.isBlank()
                ? UUID.randomUUID().toString()
                : correlationId;
        metadata = metadata == null ? Map.of() : Map.copyOf(new LinkedHashMap<>(metadata));
    }

    public CrawlRequest(String source, String url) {
        this(source, url, null, Map.of());
    }

    public CrawlRequest(String source, String url, Map<String, String> metadata) {
        this(source, url, null, metadata);
    }

    private static String normalize(String value, String fieldName) {
        Objects.requireNonNull(value, fieldName + " must not be null");

        String normalized = value.trim();
        if (normalized.isEmpty()) {
            throw new IllegalArgumentException(fieldName + " must not be blank");
        }
        return normalized;
    }
}
