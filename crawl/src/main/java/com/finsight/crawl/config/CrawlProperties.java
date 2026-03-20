package com.finsight.crawl.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "finsight.crawl")
public class CrawlProperties {

    private CrawlMode mode = CrawlMode.DIRECT;
    private int maxContentLength = 10000;
    private Jsoup jsoup = new Jsoup();
    private Kafka kafka = new Kafka();

    public CrawlMode getMode() {
        return mode;
    }

    public void setMode(CrawlMode mode) {
        this.mode = mode;
    }

    public int getMaxContentLength() {
        return maxContentLength;
    }

    public void setMaxContentLength(int maxContentLength) {
        this.maxContentLength = maxContentLength;
    }

    public Jsoup getJsoup() {
        return jsoup;
    }

    public void setJsoup(Jsoup jsoup) {
        this.jsoup = jsoup;
    }

    public Kafka getKafka() {
        return kafka;
    }

    public void setKafka(Kafka kafka) {
        this.kafka = kafka;
    }

    public static class Jsoup {
        private int timeoutMillis = 5000;
        private String userAgent = "Mozilla/5.0 (compatible; FinSightCrawler/1.0)";
        private String referrer = "https://www.google.com";
        private boolean followRedirects = true;

        public int getTimeoutMillis() {
            return timeoutMillis;
        }

        public void setTimeoutMillis(int timeoutMillis) {
            this.timeoutMillis = timeoutMillis;
        }

        public String getUserAgent() {
            return userAgent;
        }

        public void setUserAgent(String userAgent) {
            this.userAgent = userAgent;
        }

        public String getReferrer() {
            return referrer;
        }

        public void setReferrer(String referrer) {
            this.referrer = referrer;
        }

        public boolean isFollowRedirects() {
            return followRedirects;
        }

        public void setFollowRedirects(boolean followRedirects) {
            this.followRedirects = followRedirects;
        }
    }

    public static class Kafka {
        private boolean enabled;
        private boolean consumerEnabled = true;
        private boolean publishResult = true;
        private String jobTopic = "crawl.jobs";
        private String resultTopic = "crawl.results";
        private String consumerGroup = "finsight-crawl";

        public boolean isEnabled() {
            return enabled;
        }

        public void setEnabled(boolean enabled) {
            this.enabled = enabled;
        }

        public boolean isConsumerEnabled() {
            return consumerEnabled;
        }

        public void setConsumerEnabled(boolean consumerEnabled) {
            this.consumerEnabled = consumerEnabled;
        }

        public boolean isPublishResult() {
            return publishResult;
        }

        public void setPublishResult(boolean publishResult) {
            this.publishResult = publishResult;
        }

        public String getJobTopic() {
            return jobTopic;
        }

        public void setJobTopic(String jobTopic) {
            this.jobTopic = jobTopic;
        }

        public String getResultTopic() {
            return resultTopic;
        }

        public void setResultTopic(String resultTopic) {
            this.resultTopic = resultTopic;
        }

        public String getConsumerGroup() {
            return consumerGroup;
        }

        public void setConsumerGroup(String consumerGroup) {
            this.consumerGroup = consumerGroup;
        }
    }
}
