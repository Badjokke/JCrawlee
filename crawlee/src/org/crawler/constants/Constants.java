package org.crawler.constants;

public class Constants {

    public static final String storageRoot = "./storage";
    public static final String indexRoot = "./index";
    public static final String titleIndexPath = "/title_index.bin";
    public static final String contentIndexPath = "/content_index.bin";
    public static final String collectionTermFrequencyPath = "/collection_term_frequency.bin";
    public static final String documentTermFrequencyPath = "/document_term_frequency.bin";
    public static final String documentSizePath = "/document_size.bin";

    public static final String crawlerFileStorage = storageRoot + "./document_cache";
    public static final String queryResultsFileStorage = storageRoot + "./query_cache";
    public static final String CRAWLER_CONFIG_PATH = "./crawler_config/config.json";

    public static final String STOP_WORDS_FILE_PATH = "./nlp/stopwords_en.txt";

    public static final int PAGE_SNIPPET_SIZE = 50;

}