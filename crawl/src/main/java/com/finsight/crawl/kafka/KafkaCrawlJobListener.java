package com.finsight.crawl.kafka;

import com.finsight.crawl.api.CrawlResult;
import com.finsight.crawl.core.DirectCrawlExecutor;
import org.springframework.kafka.annotation.KafkaListener;

public class KafkaCrawlJobListener {

    private final DirectCrawlExecutor directCrawlExecutor;
    private final CrawlResultPublisher crawlResultPublisher;

    public KafkaCrawlJobListener(
            DirectCrawlExecutor directCrawlExecutor,
            CrawlResultPublisher crawlResultPublisher
    ) {
        this.directCrawlExecutor = directCrawlExecutor;
        this.crawlResultPublisher = crawlResultPublisher;
    }

    @KafkaListener(
            topics = "${finsight.crawl.kafka.job-topic:crawl.jobs}",
            groupId = "${finsight.crawl.kafka.consumer-group:finsight-crawl}"
    )
    public void consume(CrawlJobMessage message) {
        CrawlResult result = directCrawlExecutor.execute(message.toRequest());
        if (crawlResultPublisher != null) {
            crawlResultPublisher.publish(result);
        }
    }
}
