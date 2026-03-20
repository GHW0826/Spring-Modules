package com.finsight.crawl.core;

import com.finsight.crawl.api.CrawlDispatchStatus;
import com.finsight.crawl.api.CrawlRequest;
import com.finsight.crawl.api.CrawlResult;
import com.finsight.crawl.api.CrawlResultHandler;
import com.finsight.crawl.api.Crawler;
import com.finsight.crawl.config.CrawlMode;
import com.finsight.crawl.config.CrawlProperties;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.ObjectProvider;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class DefaultCrawlOperationsTest {

    @Test
    void dispatchExecutesImmediatelyInDirectMode() {
        CrawlProperties properties = new CrawlProperties();
        properties.setMode(CrawlMode.DIRECT);

        RecordingResultHandler handler = new RecordingResultHandler();
        DefaultCrawlOperations operations = new DefaultCrawlOperations(
                properties,
                new DirectCrawlExecutor(
                        new CrawlerRegistry(List.of(new StubCrawler())),
                        List.of(handler)
                ),
                emptyProvider()
        );

        var response = operations.dispatch(new CrawlRequest("naver", "https://example.com"));

        assertThat(response.status()).isEqualTo(CrawlDispatchStatus.EXECUTED);
        assertThat(response.result()).isNotNull();
        assertThat(response.result().success()).isTrue();
        assertThat(handler.handled).isTrue();
    }

    @Test
    void dispatchQueuesInKafkaMode() {
        CrawlProperties properties = new CrawlProperties();
        properties.setMode(CrawlMode.KAFKA);

        RecordingPublisher publisher = new RecordingPublisher();
        DefaultCrawlOperations operations = new DefaultCrawlOperations(
                properties,
                new DirectCrawlExecutor(new CrawlerRegistry(List.of(new StubCrawler())), List.of()),
                singleProvider(publisher)
        );

        CrawlRequest request = new CrawlRequest("naver", "https://example.com", Map.of("page", "1"));
        var response = operations.dispatch(request);

        assertThat(response.status()).isEqualTo(CrawlDispatchStatus.QUEUED);
        assertThat(response.result()).isNull();
        assertThat(publisher.lastRequest).isEqualTo(request);
    }

    private static ObjectProvider<CrawlJobPublisher> emptyProvider() {
        ObjectProvider<CrawlJobPublisher> provider = mock(ObjectProvider.class);
        when(provider.getIfAvailable()).thenReturn(null);
        return provider;
    }

    private static ObjectProvider<CrawlJobPublisher> singleProvider(CrawlJobPublisher publisher) {
        ObjectProvider<CrawlJobPublisher> provider = mock(ObjectProvider.class);
        when(provider.getIfAvailable()).thenReturn(publisher);
        return provider;
    }

    private static final class StubCrawler implements Crawler {
        @Override
        public String supports() {
            return "naver";
        }

        @Override
        public CrawlResult crawl(CrawlRequest request) {
            return CrawlResult.success(request, "title", "content", 200);
        }
    }

    private static final class RecordingResultHandler implements CrawlResultHandler {
        private boolean handled;

        @Override
        public void handle(CrawlRequest request, CrawlResult result) {
            this.handled = true;
        }
    }

    private static final class RecordingPublisher implements CrawlJobPublisher {
        private CrawlRequest lastRequest;

        @Override
        public void publish(CrawlRequest request) {
            this.lastRequest = request;
        }
    }
}
