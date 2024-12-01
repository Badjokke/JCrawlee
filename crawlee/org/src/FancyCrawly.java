package org.src;

import org.src.common.constants.Constants;
import org.src.common.io.IOManager;
import org.src.crawler.Crawler;
import org.src.crawler.util.CrawlerUtil;
import org.src.etl.AbstractRestEtl;
import org.src.etl.ConfigLoader;
import org.src.etl.YitRestEtl;
import org.src.etl.model.RestEtlConfig;

import java.util.logging.Logger;

public class FancyCrawly {
    private static final Logger LOG = Logger.getLogger(FancyCrawly.class.getCanonicalName());

    private static Runnable crawlerTask(Crawler[] crawlers) {
        return () -> {
            for (Crawler crawler : crawlers) {
                crawler.run();
            }
        };
    }

    private static Runnable ETLTask(RestEtlConfig[] etls) {
        return () -> {
            for (RestEtlConfig config : etls) {
                AbstractRestEtl yit = new YitRestEtl(config);
                yit.extractAndSaveTransformedData();
            }
        };
    }

    public static void main(String[] args) {
        IOManager.createDocumentStorage(true);
        LOG.info("Loading crawler config.");
        Thread crawlerThread = new Thread(crawlerTask(CrawlerUtil.loadCrawlers(Constants.CRAWLER_CONFIG_PATH)));
        LOG.info("Loading ETL config.");
        Thread etlThread = new Thread(ETLTask(ConfigLoader.loadEtlConfig(Constants.ETL_CONFIG_PATH)));

        LOG.info("Starting crawler thread");
        crawlerThread.start();
        LOG.info("Starting ETL thread");
        etlThread.start();
        try {
            crawlerThread.join();
            LOG.info("Crawler thread finished");
            etlThread.join();
            LOG.info("ETL thread finished");
        } catch (InterruptedException exception) {
            LOG.severe("Interrupt exception");
            throw new RuntimeException(exception);
        }
        LOG.info("Finished");
    }

}
