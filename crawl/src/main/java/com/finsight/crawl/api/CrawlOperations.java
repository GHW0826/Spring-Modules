package com.finsight.crawl.api;

public interface CrawlOperations {
    CrawlDispatchResult dispatch(CrawlRequest request);

    CrawlResult execute(CrawlRequest request);

    void enqueue(CrawlRequest request);
}
