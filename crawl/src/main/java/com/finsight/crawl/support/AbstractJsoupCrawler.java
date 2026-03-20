package com.finsight.crawl.support;

import com.finsight.crawl.api.CrawlRequest;
import com.finsight.crawl.api.CrawlResult;
import com.finsight.crawl.api.Crawler;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

public abstract class AbstractJsoupCrawler implements Crawler {

    private final JsoupDocumentFetcher documentFetcher;
    private final int maxContentLength;

    protected AbstractJsoupCrawler(
            JsoupDocumentFetcher documentFetcher,
            int maxContentLength
    ) {
        this.documentFetcher = documentFetcher;
        this.maxContentLength = maxContentLength;
    }

    protected Document fetch(CrawlRequest request) throws Exception {
        return documentFetcher.fetch(request.url());
    }

    protected String extractMainText(Document document) {
        Element article = document.selectFirst("article");
        String text;
        if (article != null) {
            text = article.text();
        } else if (document.body() != null) {
            text = document.body().text();
        } else {
            text = "";
        }

        if (text.length() > maxContentLength) {
            return text.substring(0, maxContentLength);
        }
        return text;
    }

    protected CrawlResult success(CrawlRequest request, String title, String content, int statusCode) {
        return CrawlResult.success(request, title, content, statusCode);
    }

    protected CrawlResult failure(CrawlRequest request, int statusCode, Exception exception) {
        String message = exception == null ? "crawl failed" : exception.getMessage();
        return CrawlResult.failure(request, statusCode, message);
    }
}
