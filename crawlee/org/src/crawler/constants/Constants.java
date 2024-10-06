package org.src.crawler.constants;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Constants {

    private Constants(){}

    public static final String storageRoot = "./storage";
    public static final String crawlerFileStorage = storageRoot + "./document_cache";
    public static final Set<String> xPathKeywords = new HashSet<>(List.of("traverse_expression"));
    public static final String crawlerConfigPath = "crawlee/org/src/resources/config.json";

}