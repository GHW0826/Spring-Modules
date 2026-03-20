package com.finsight.crawl.api;

public interface Crawler {
    String supports();

    CrawlResult crawl(CrawlRequest request);
}
