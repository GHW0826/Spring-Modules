package com.finsight.crawl.core;

import com.finsight.crawl.api.CrawlRequest;

public interface CrawlJobPublisher {
    void publish(CrawlRequest request);
}
