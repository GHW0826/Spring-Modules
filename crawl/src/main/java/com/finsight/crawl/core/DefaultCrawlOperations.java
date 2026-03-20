package com.finsight.crawl.core;

import com.finsight.crawl.api.CrawlDispatchResult;
import com.finsight.crawl.api.CrawlOperations;
import com.finsight.crawl.api.CrawlRequest;
import com.finsight.crawl.api.CrawlResult;
import com.finsight.crawl.config.CrawlMode;
import com.finsight.crawl.config.CrawlProperties;
import org.springframework.beans.factory.ObjectProvider;

public class DefaultCrawlOperations implements CrawlOperations {

    private final CrawlProperties crawlProperties;
    private final DirectCrawlExecutor directCrawlExecutor;
    private final ObjectProvider<CrawlJobPublisher> crawlJobPublisherProvider;

    public DefaultCrawlOperations(
            CrawlProperties crawlProperties,
            DirectCrawlExecutor directCrawlExecutor,
            ObjectProvider<CrawlJobPublisher> crawlJobPublisherProvider
    ) {
        this.crawlProperties = crawlProperties;
        this.directCrawlExecutor = directCrawlExecutor;
        this.crawlJobPublisherProvider = crawlJobPublisherProvider;
    }

    @Override
    public CrawlDispatchResult dispatch(CrawlRequest request) {
        if (crawlProperties.getMode() == CrawlMode.KAFKA) {
            enqueue(request);
            return CrawlDispatchResult.queued(request);
        }

        return CrawlDispatchResult.executed(request, execute(request));
    }

    @Override
    public CrawlResult execute(CrawlRequest request) {
        return directCrawlExecutor.execute(request);
    }

    @Override
    public void enqueue(CrawlRequest request) {
        CrawlJobPublisher publisher = crawlJobPublisherProvider.getIfAvailable();
        if (publisher == null) {
            throw new IllegalStateException(
                    "Kafka crawl mode requires a CrawlJobPublisher bean. " +
                            "Enable finsight.crawl.kafka.enabled=true and configure KafkaTemplate."
            );
        }
        publisher.publish(request);
    }
}
