package org.src.crawler.model;

import java.util.Map;

public class ScrapedDocument {

    private final Map<String, String> content;

    public Map<String, String> getContent() {
        return content;
    }

    public ScrapedDocument(Map<String, String> content){
        this.content = content;
    }


}
