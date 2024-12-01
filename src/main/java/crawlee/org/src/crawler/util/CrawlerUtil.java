package crawlee.org.src.crawler.util;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import crawlee.org.src.crawler.Crawler;
import crawlee.org.src.crawler.model.WebScraperConfig;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.logging.Logger;

public class CrawlerUtil {
    private static final Logger log = Logger.getLogger(CrawlerUtil.class.getName());

    public CrawlerUtil() {
        // no instances
    }

    public static Crawler[] loadCrawlers(String configFilePath) {
        WebScraperConfig[] configs;
        try (InputStream inputStream = CrawlerUtil.class.getClassLoader().getResourceAsStream(configFilePath)) {
            assert inputStream != null : "Resource not found at path: " + configFilePath;
            configs = new Gson().fromJson(new JsonReader(new InputStreamReader(inputStream)), WebScraperConfig[].class);
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
