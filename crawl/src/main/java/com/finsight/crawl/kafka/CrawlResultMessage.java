package com.finsight.crawl.kafka;

import com.finsight.crawl.api.CrawlResult;

import java.time.Instant;
import java.util.Map;

public record CrawlResultMessage(
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
    public static CrawlResultMessage from(CrawlResult result) {
        return new CrawlResultMessage(
                result.source(),
                result.url(),
                result.correlationId(),
                result.title(),
                result.content(),
                result.statusCode(),
                result.success(),
                result.errorMessage(),
                result.crawledAt(),
                result.metadata()
        );
    }
}
