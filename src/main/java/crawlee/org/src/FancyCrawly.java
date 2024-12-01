package crawlee.org.src;


import crawlee.org.src.common.constants.Constants;
import crawlee.org.src.common.io.IOManager;
import crawlee.org.src.crawler.Crawler;
import crawlee.org.src.crawler.util.CrawlerUtil;
import crawlee.org.src.etl.AbstractRestEtl;
import crawlee.org.src.etl.ConfigLoader;
import crawlee.org.src.etl.YitRestEtl;
import crawlee.org.src.etl.model.RestEtlConfig;

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
