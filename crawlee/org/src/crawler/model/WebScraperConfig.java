package org.src.crawler.model;

import java.util.Map;

public class WebScraperConfig {
    private int politeness;
    private String rootPage;
    private int workerCount;
    private String outputFilePrefix;


    private String outputFileDirectory;
    private Map<String, String> xpaths;

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

    public String getOutputFilePrefix() {
        return outputFilePrefix;
    }

    public void setOutputFilePrefix(String outputFilePrefix) {
        this.outputFilePrefix = outputFilePrefix;
    }

    public String getOutputFileDirectory() {
        return outputFileDirectory;
    }

    public void setOutputFileDirectory(String outputFileDirectory) {
        this.outputFileDirectory = outputFileDirectory;
    }

    @Override
    public String toString() {
        return "WebScraperConfig{" +
                "politeness=" + politeness +
                ", rootPage='" + rootPage + '\'' +
                ", workerCount=" + workerCount +
                ", outputFilePrefix=" + outputFilePrefix +
                ", outputFileDirectory=" + outputFileDirectory +
                ", xpaths=" + xpaths +
                '}';
    }

}
