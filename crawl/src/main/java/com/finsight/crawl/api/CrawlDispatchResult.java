package com.finsight.crawl.api;

import java.util.Objects;

public record CrawlDispatchResult(
        CrawlDispatchStatus status,
        String source,
        String url,
        String correlationId,
        CrawlResult result
) {
    public CrawlDispatchResult {
        Objects.requireNonNull(status, "status must not be null");
        source = source == null ? "" : source;
        url = url == null ? "" : url;
        correlationId = correlationId == null ? "" : correlationId;
    }

    public static CrawlDispatchResult executed(CrawlRequest request, CrawlResult result) {
        return new CrawlDispatchResult(
                CrawlDispatchStatus.EXECUTED,
                request.source(),
                request.url(),
                request.correlationId(),
                result
        );
    }

    public static CrawlDispatchResult queued(CrawlRequest request) {
        return new CrawlDispatchResult(
                CrawlDispatchStatus.QUEUED,
                request.source(),
                request.url(),
                request.correlationId(),
                null
        );
    }
}
