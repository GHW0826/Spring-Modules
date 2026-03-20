package com.finsight.crawl.autoconfigure;

import com.finsight.crawl.api.CrawlOperations;
import com.finsight.crawl.api.CrawlResultHandler;
import com.finsight.crawl.api.Crawler;
import com.finsight.crawl.config.CrawlProperties;
import com.finsight.crawl.core.CrawlJobPublisher;
import com.finsight.crawl.core.CrawlerRegistry;
import com.finsight.crawl.core.DefaultCrawlOperations;
import com.finsight.crawl.core.DirectCrawlExecutor;
import com.finsight.crawl.kafka.CrawlResultPublisher;
import com.finsight.crawl.kafka.KafkaCrawlJobListener;
import com.finsight.crawl.kafka.KafkaCrawlJobPublisher;
import com.finsight.crawl.kafka.KafkaCrawlResultPublisher;
import com.finsight.crawl.support.JsoupDocumentFetcher;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.core.KafkaOperations;

import java.util.List;

@AutoConfiguration
@EnableConfigurationProperties(CrawlProperties.class)
public class CrawlAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public CrawlerRegistry crawlerRegistry(List<Crawler> crawlers) {
        return new CrawlerRegistry(crawlers);
    }

    @Bean
    @ConditionalOnMissingBean
    public DirectCrawlExecutor directCrawlExecutor(
            CrawlerRegistry crawlerRegistry,
            ObjectProvider<CrawlResultHandler> resultHandlersProvider
    ) {
        List<CrawlResultHandler> resultHandlers = resultHandlersProvider.orderedStream().toList();
        return new DirectCrawlExecutor(crawlerRegistry, resultHandlers);
    }

    @Bean
    @ConditionalOnMissingBean
    public CrawlOperations crawlOperations(
            CrawlProperties crawlProperties,
            DirectCrawlExecutor directCrawlExecutor,
            ObjectProvider<CrawlJobPublisher> crawlJobPublisherProvider
    ) {
        return new DefaultCrawlOperations(crawlProperties, directCrawlExecutor, crawlJobPublisherProvider);
    }

    @Bean
    @ConditionalOnMissingBean
    public JsoupDocumentFetcher jsoupDocumentFetcher(CrawlProperties crawlProperties) {
        return new JsoupDocumentFetcher(crawlProperties);
    }

    @Bean
    @ConditionalOnClass(KafkaOperations.class)
    @ConditionalOnProperty(prefix = "finsight.crawl.kafka", name = "enabled", havingValue = "true")
    @ConditionalOnMissingBean
    public CrawlJobPublisher crawlJobPublisher(
            KafkaOperations<Object, Object> kafkaOperations,
            CrawlProperties crawlProperties
    ) {
        return new KafkaCrawlJobPublisher(kafkaOperations, crawlProperties);
    }

    @Bean
    @ConditionalOnClass(KafkaOperations.class)
    @ConditionalOnProperty(prefix = "finsight.crawl.kafka", name = "enabled", havingValue = "true")
    @ConditionalOnMissingBean
    public CrawlResultPublisher crawlResultPublisher(
            KafkaOperations<Object, Object> kafkaOperations,
            CrawlProperties crawlProperties
    ) {
        return new KafkaCrawlResultPublisher(kafkaOperations, crawlProperties);
    }

    @Bean
    @ConditionalOnClass(KafkaOperations.class)
    @ConditionalOnProperty(
            prefix = "finsight.crawl.kafka",
            name = {"enabled", "consumer-enabled"},
            havingValue = "true"
    )
    @ConditionalOnMissingBean
    public KafkaCrawlJobListener kafkaCrawlJobListener(
            DirectCrawlExecutor directCrawlExecutor,
            ObjectProvider<CrawlResultPublisher> crawlResultPublisherProvider
    ) {
        return new KafkaCrawlJobListener(directCrawlExecutor, crawlResultPublisherProvider.getIfAvailable());
    }
}
