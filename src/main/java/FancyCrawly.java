import crawler.Crawler;
import crawler.util.CrawlerUtil;

public class FancyCrawly {

    public static void main(String[] args) {

        Crawler[] crawlers = CrawlerUtil.loadCrawlers("src/main/resources/config.json");
        for( Crawler crawler : crawlers){
            crawler.crawlSeedPage();
        }
        System.out.println("dsakdaskjdsa");

    }

}
