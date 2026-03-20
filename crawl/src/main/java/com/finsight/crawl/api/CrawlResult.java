package com.finsight.crawl.api;

import java.time.Instant;
import java.util.Map;
import java.util.Objects;

public record CrawlResult(
        String source,
        String url,
        String correlationId,
        String title,
        String content,
        int statusCode,
        boolean success,
        String errorMessage,
        Instant crawledAt,
        Map<String, String> metadata
) {
    public CrawlResult {
        source = source == null ? "" : source;
        url = url == null ? "" : url;
        correlationId = correlationId == null ? "" : correlationId;
        title = title == null ? "" : title;
        content = content == null ? "" : content;
        errorMessage = errorMessage == null ? "" : errorMessage;
        crawledAt = Objects.requireNonNullElseGet(crawledAt, Instant::now);
        metadata = metadata == null ? Map.of() : Map.copyOf(metadata);
    }

    public static CrawlResult success(
            CrawlRequest request,
            String title,
            String content,
            int statusCode
    ) {
        return new CrawlResult(
                request.source(),
                request.url(),
                request.correlationId(),
                title,
                content,
                statusCode,
                true,
                "",
                Instant.now(),
                request.metadata()
        );
    }

    public static CrawlResult failure(
            CrawlRequest request,
            int statusCode,
            String errorMessage
    ) {
        return new CrawlResult(
                request.source(),
                request.url(),
                request.correlationId(),
                "",
                "",
                statusCode,
                false,
                errorMessage,
                Instant.now(),
                request.metadata()
        );
    }
}
