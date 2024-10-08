package org.src.crawler.model;

import java.util.List;
import java.util.Map;

public class ScrapedDocument {

    private final Map<String, List<String>> content;

    public Map<String, List<String>> getContent() {
        return content;
    }

    public ScrapedDocument(Map<String, List<String>> content) {
        this.content = content;
    }


}
