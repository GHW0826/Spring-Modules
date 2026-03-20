package com.finsight.crawl.kafka;

import com.finsight.crawl.api.CrawlRequest;
import com.finsight.crawl.config.CrawlProperties;
import com.finsight.crawl.core.CrawlJobPublisher;
import org.springframework.kafka.core.KafkaOperations;

public class KafkaCrawlJobPublisher implements CrawlJobPublisher {

    private final KafkaOperations<Object, Object> kafkaOperations;
    private final CrawlProperties crawlProperties;

    public KafkaCrawlJobPublisher(
            KafkaOperations<Object, Object> kafkaOperations,
            CrawlProperties crawlProperties
    ) {
        this.kafkaOperations = kafkaOperations;
        this.crawlProperties = crawlProperties;
    }

    @Override
    public void publish(CrawlRequest request) {
        kafkaOperations.send(
                crawlProperties.getKafka().getJobTopic(),
                request.source(),
                CrawlJobMessage.from(request)
        );
    }
}
