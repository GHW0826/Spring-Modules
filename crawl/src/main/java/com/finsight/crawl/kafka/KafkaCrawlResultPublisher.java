package com.finsight.crawl.kafka;

import com.finsight.crawl.api.CrawlResult;
import com.finsight.crawl.config.CrawlProperties;
import org.springframework.kafka.core.KafkaOperations;

public class KafkaCrawlResultPublisher implements CrawlResultPublisher {

    private final KafkaOperations<Object, Object> kafkaOperations;
    private final CrawlProperties crawlProperties;

    public KafkaCrawlResultPublisher(
            KafkaOperations<Object, Object> kafkaOperations,
            CrawlProperties crawlProperties
    ) {
        this.kafkaOperations = kafkaOperations;
        this.crawlProperties = crawlProperties;
    }

    @Override
    public void publish(CrawlResult result) {
        if (!crawlProperties.getKafka().isPublishResult()) {
            return;
        }

        kafkaOperations.send(
                crawlProperties.getKafka().getResultTopic(),
                result.source(),
                CrawlResultMessage.from(result)
        );
    }
}
