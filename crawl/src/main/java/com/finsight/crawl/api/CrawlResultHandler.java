package com.finsight.crawl.api;

public interface CrawlResultHandler {
    void handle(CrawlRequest request, CrawlResult result);
}
