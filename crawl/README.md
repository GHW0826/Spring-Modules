# Crawl Module

`spring-modules:crawl` is a reusable crawling template module for Spring projects.

## Features

- Single entry point via `CrawlOperations`
- `DIRECT` execution mode
- `KAFKA` execution mode
- Module-monolith deployment with producer and consumer in one application
- Custom `Crawler` implementations
- Result post-processing via `CrawlResultHandler`
- `JsoupDocumentFetcher` and `AbstractJsoupCrawler` helpers
- Spring Boot auto-configuration

## Dependency

Import only this module from another project.

```gradle
implementation project(":spring-modules:crawl")
```

Site-specific crawlers should be implemented in the consuming project.

```java
package com.example.news;

import com.finsight.crawl.api.CrawlRequest;
import com.finsight.crawl.api.CrawlResult;
import com.finsight.crawl.support.AbstractJsoupCrawler;
import com.finsight.crawl.support.JsoupDocumentFetcher;
import org.jsoup.nodes.Document;
import org.springframework.stereotype.Component;

@Component
public class NaverNewsCrawler extends AbstractJsoupCrawler {

    public NaverNewsCrawler(JsoupDocumentFetcher documentFetcher) {
        super(documentFetcher, 10000);
    }

    @Override
    public String supports() {
        return "naver";
    }

    @Override
    public CrawlResult crawl(CrawlRequest request) {
        try {
            Document document = fetch(request);
            return success(request, document.title(), extractMainText(document), 200);
        } catch (Exception exception) {
            return failure(request, -1, exception);
        }
    }
}
```

Add a result handler when you need persistence or downstream processing.

```java
package com.example.news;

import com.finsight.crawl.api.CrawlRequest;
import com.finsight.crawl.api.CrawlResult;
import com.finsight.crawl.api.CrawlResultHandler;
import org.springframework.stereotype.Component;

@Component
public class CrawlPersistenceHandler implements CrawlResultHandler {
    @Override
    public void handle(CrawlRequest request, CrawlResult result) {
        // save result
    }
}
```

## Configuration

Direct execution:

```yaml
finsight:
  crawl:
    mode: DIRECT
```

Kafka queue-based execution:

```yaml
finsight:
  crawl:
    mode: KAFKA
    kafka:
      enabled: true
      consumer-enabled: true
      publish-result: true
      job-topic: crawl.jobs
      result-topic: crawl.results
      consumer-group: crawl-worker
```

## Usage

```java
package com.example.news;

import com.finsight.crawl.api.CrawlOperations;
import com.finsight.crawl.api.CrawlRequest;
import org.springframework.stereotype.Service;

@Service
public class NewsCrawlService {
    private final CrawlOperations crawlOperations;

    public NewsCrawlService(CrawlOperations crawlOperations) {
        this.crawlOperations = crawlOperations;
    }

    public void crawl(String url) {
        crawlOperations.dispatch(new CrawlRequest("naver", url));
    }
}
```
