package org.src;

import org.src.common.constants.Constants;
import org.src.common.io.IOManager;
import org.src.crawler.util.CrawlerUtil;
import org.src.crawler.Crawler;

public class FancyCrawly {

    public static void main(String[] args) {
        IOManager.createDocumentStorage(true);
        Crawler[] crawlers = CrawlerUtil.loadCrawlers(Constants.CRAWLER_CONFIG_PATH);
        for( Crawler crawler : crawlers){
            crawler.run();
        }

    }

}
