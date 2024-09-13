public class FancyCrawly {

    public static void main(String[] args){
        Crawler crawler = new Crawler(5);
        crawler.config();
        crawler.crawlSeedPage();
        System.out.println("screee");
    }

}
