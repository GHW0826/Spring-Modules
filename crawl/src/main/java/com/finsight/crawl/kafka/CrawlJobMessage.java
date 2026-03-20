package com.finsight.crawl.kafka;

import com.finsight.crawl.api.CrawlRequest;

import java.util.Map;

public record CrawlJobMessage(
        String source,
        String url,
        String correlationId,
        Map<String, String> metadata
) {
    public static CrawlJobMessage from(CrawlRequest request) {
        return new CrawlJobMessage(
                request.source(),
                request.url(),
                request.correlationId(),
                request.metadata()
        );
    }

    public CrawlRequest toRequest() {
        return new CrawlRequest(source, url, correlationId, metadata);
    }
}
