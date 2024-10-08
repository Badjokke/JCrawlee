package org.src;

import org.src.crawler.constants.Constants;
import org.src.crawler.io.IOManager;
import org.src.crawler.util.CrawlerUtil;
import org.src.crawler.Crawler;

public class FancyCrawly {

    public static void main(String[] args) {
        IOManager.createDocumentStorage(true);
        Crawler[] crawlers = CrawlerUtil.loadCrawlers(Constants.crawlerConfigPath);
        for( Crawler crawler : crawlers){
            crawler.run();
        }

    }

}
