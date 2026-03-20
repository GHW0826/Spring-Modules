package com.finsight.crawl.core;

import com.finsight.crawl.api.Crawler;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class CrawlerRegistry {

    private final Map<String, Crawler> crawlers = new LinkedHashMap<>();

    public CrawlerRegistry(List<Crawler> crawlers) {
        for (Crawler crawler : crawlers) {
            String source = normalize(crawler.supports());
            if (this.crawlers.containsKey(source)) {
                throw new IllegalStateException("Duplicate crawler for source: " + source);
            }
            this.crawlers.put(source, crawler);
        }
    }

    public Crawler get(String source) {
        String normalized = normalize(source);
        Crawler crawler = crawlers.get(normalized);
        if (crawler == null) {
            throw new IllegalArgumentException("No crawler registered for source: " + source);
        }
        return crawler;
    }

    public boolean supports(String source) {
        return crawlers.containsKey(normalize(source));
    }

    private String normalize(String source) {
        if (source == null || source.isBlank()) {
            throw new IllegalArgumentException("source must not be blank");
        }
        return source.trim().toLowerCase(Locale.ROOT);
    }
}
