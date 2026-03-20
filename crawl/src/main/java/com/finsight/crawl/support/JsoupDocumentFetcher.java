package com.finsight.crawl.support;

import com.finsight.crawl.config.CrawlProperties;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;

public class JsoupDocumentFetcher {

    private final CrawlProperties crawlProperties;

    public JsoupDocumentFetcher(CrawlProperties crawlProperties) {
        this.crawlProperties = crawlProperties;
    }

    public Document fetch(String url) throws IOException {
        CrawlProperties.Jsoup jsoup = crawlProperties.getJsoup();
        Connection.Response response = Jsoup.connect(url)
                .userAgent(jsoup.getUserAgent())
                .referrer(jsoup.getReferrer())
                .timeout(jsoup.getTimeoutMillis())
                .followRedirects(jsoup.isFollowRedirects())
                .execute();

        return response.parse();
    }
}
