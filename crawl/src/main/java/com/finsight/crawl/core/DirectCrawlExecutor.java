package com.finsight.crawl.core;

import com.finsight.crawl.api.CrawlRequest;
import com.finsight.crawl.api.CrawlResult;
import com.finsight.crawl.api.CrawlResultHandler;
import com.finsight.crawl.api.Crawler;

import java.util.List;

public class DirectCrawlExecutor {

    private final CrawlerRegistry crawlerRegistry;
    private final List<CrawlResultHandler> resultHandlers;

    public DirectCrawlExecutor(
            CrawlerRegistry crawlerRegistry,
            List<CrawlResultHandler> resultHandlers
    ) {
        this.crawlerRegistry = crawlerRegistry;
        this.resultHandlers = List.copyOf(resultHandlers);
    }

    public CrawlResult execute(CrawlRequest request) {
        Crawler crawler = crawlerRegistry.get(request.source());
        CrawlResult result = crawler.crawl(request);
        for (CrawlResultHandler resultHandler : resultHandlers) {
            resultHandler.handle(request, result);
        }
        return result;
    }
}
