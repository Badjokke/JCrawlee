package org;

import org.crawler.util.CrawlerUtil;
import org.crawler.Crawler;

public class FancyCrawly {

    public static void main(String[] args) {

        Crawler[] crawlers = CrawlerUtil.loadCrawlers("org/src/org/resources/config.json");
        for( Crawler crawler : crawlers){
            crawler.crawlSeedPage();
        }
        System.out.println("dsakdaskjdsa");

    }

}
