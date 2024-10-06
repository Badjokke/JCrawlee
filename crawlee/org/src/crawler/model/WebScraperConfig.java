package org.src.crawler.model;

import java.util.Map;

public class WebScraperConfig {
    private int politeness;
    private String rootPage;
    private int workerCount;
    private Map<String, String> xpaths;

    public WebScraperConfig(int politeness, String rootPage, int workerCount, Map<String, String> xpaths) {
        this.politeness = politeness;
        this.rootPage = rootPage;
        this.workerCount = workerCount;
        this.xpaths = xpaths;
    }

    // Getters and Setters
    public int getPoliteness() {
        return politeness;
    }

    public void setPoliteness(int politeness) {
        this.politeness = politeness;
    }

    public String getRootPage() {
        return rootPage;
    }

    public void setRootPage(String rootPage) {
        this.rootPage = rootPage;
    }

    public int getWorkerCount() {
        return workerCount;
    }

    public void setWorkerCount(int workerCount) {
        this.workerCount = workerCount;
    }

    public Map<String, String> getXpaths() {
        return xpaths;
    }

    public void setXpaths(Map<String, String> xpaths) {
        this.xpaths = xpaths;
    }

    @Override
    public String toString() {
        return "WebScraperConfig{" +
                "politeness=" + politeness +
                ", rootPage='" + rootPage + '\'' +
                ", workerCount=" + workerCount +
                ", xpaths=" + xpaths +
                '}';
    }

}
