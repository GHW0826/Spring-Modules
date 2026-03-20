package com.finsight.crawl.kafka;

import com.finsight.crawl.api.CrawlRequest;
import com.finsight.crawl.api.CrawlResult;
import com.finsight.crawl.api.Crawler;
import com.finsight.crawl.core.CrawlerRegistry;
import com.finsight.crawl.core.DirectCrawlExecutor;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class KafkaCrawlJobListenerTest {

    @Test
    void listenerExecutesCrawlerAndPublishesResult() {
        RecordingResultPublisher publisher = new RecordingResultPublisher();
        KafkaCrawlJobListener listener = new KafkaCrawlJobListener(
                new DirectCrawlExecutor(new CrawlerRegistry(List.of(new StubCrawler())), List.of()),
                publisher
        );

        listener.consume(new CrawlJobMessage("naver", "https://example.com", "cid-1", null));

        assertThat(publisher.lastResult).isNotNull();
        assertThat(publisher.lastResult.source()).isEqualTo("naver");
        assertThat(publisher.lastResult.correlationId()).isEqualTo("cid-1");
    }

    private static final class StubCrawler implements Crawler {
        @Override
        public String supports() {
            return "naver";
        }

        @Override
        public CrawlResult crawl(CrawlRequest request) {
            return CrawlResult.success(request, "title", "body", 200);
        }
    }

    private static final class RecordingResultPublisher implements CrawlResultPublisher {
        private CrawlResult lastResult;

        @Override
        public void publish(CrawlResult result) {
            this.lastResult = result;
        }
    }
}
