package org.crawler.util;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import org.crawler.Crawler;
import org.crawler.model.WebScraperConfig;

import java.io.FileReader;
import java.io.IOException;
import java.util.logging.Logger;

public class CrawlerUtil {
    private static final Logger log = Logger.getLogger(CrawlerUtil.class.getName());

    public CrawlerUtil() {
        // no instances
    }

    public static Crawler[] loadCrawlers(String configFilePath) {
        WebScraperConfig[] configs;
        try (FileReader fReader = new FileReader(configFilePath)) {
            configs = new Gson().fromJson(new JsonReader(fReader), WebScraperConfig[].class);
        } catch (IOException e) {
            log.warning(String.format("Config file not found at: %s\n", configFilePath));
            throw new RuntimeException(e);
        }
        log.info(String.format("Loaded %d crawler definitions", configs.length));
        Crawler[] crawlers = new Crawler[configs.length];
        for (int i = 0; i < configs.length; i++) {
            crawlers[i] = new Crawler(configs[i]);
        }
        return crawlers;

    }


}
