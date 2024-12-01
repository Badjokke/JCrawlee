package crawlee.org.src.crawler.model;

import java.util.List;
import java.util.Map;

public record ScrapedDocument(Map<String, List<String>> content) { }
