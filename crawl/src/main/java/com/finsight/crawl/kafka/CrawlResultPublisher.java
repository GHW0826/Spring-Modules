package com.finsight.crawl.kafka;

import com.finsight.crawl.api.CrawlResult;

public interface CrawlResultPublisher {
    void publish(CrawlResult result);
}
