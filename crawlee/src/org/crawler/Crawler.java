package org.crawler;

import org.crawler.model.WebScraperConfig;

import java.util.*;

public class Crawler {
    /**
     * initialized in constructor
     */
    //private ParserWorker[] parserWorkerWorkers;
    private Set<String> urls;
    //urls we scrape from the article for additional context information
    private Set<String> nestedUrls;

    private Set<String> visitedUrls;

    /**
     * initialized from config file
     * if config is not loaded, the crawler will not work
     */
    private int politenessInterval;
    private Map<String, String> xPaths;
    private String rootPage;
    private Iterator<String> iterator;
    private int articleNumber;


    public Crawler(WebScraperConfig config) {
        //this.parserWorkerWorkers = new ParserWorker[config.getWorkerCount()];
        this.urls = new HashSet<>();
        this.xPaths = config.getXpaths();
        this.rootPage = config.getRootPage();
        this.politenessInterval = config.getPoliteness();
        this.visitedUrls = new HashSet<>();
    }

    public String getRootPage() {
        return this.rootPage;
    }

    /**
     * First access point to crawling utility of application
     * crawls BBC articles
     * and creates storage folders if they dont exists
     */
    public void crawlSeedPage() {
        run();
    }

    public int getCurrentArticleId() {
        return this.articleNumber;
    }

    public synchronized int getArticleNumber() {
        return this.articleNumber++;
    }


    /**
     * initialization of pools and starting the threads
     */
    private void run() {
        fetchAllUrls();
        //init pool and run the threads


    }


    /**
     * Adds url to set of urls we will process
     *
     * @param url url of a page we want to parse
     */
    public void addUrlToQueue(String url) {
        if (this.visitedUrls.contains(url))
            return;
        this.visitedUrls.add(url);

        if (!url.contains(rootPage))
            url = rootPage + url;

        this.urls.add(url);

    }

    public synchronized void addUrlToNestedQueue(String url) {
        if (this.visitedUrls.contains(url)) return;
        this.visitedUrls.add(url);
        if (!url.contains(rootPage))
            url = rootPage + url;

        this.nestedUrls.add(url);
    }


    //give url to worker
    public synchronized String getUrl() {
        if (iterator.hasNext())
            return iterator.next();
        return null;
    }


    //necessary evil - first we need to fetch all urls we will later parse
    //only one thread is used for this job
    private void fetchAllUrls() {
        addUrlToQueue(this.rootPage);
        /*List<String> xpaths = new ArrayList<>();
        xpaths.add("//nav[@class='sc-44f1f005-1 cexzQM']/ul/li/div/a/@href");
        ParserWorker urlParser = new ParserWorker(this,xpaths,0);
        //extract all seed pages from given page subcategory
        List<List<String>> parsedPage = urlParser.crawlUrls(this.subCategoryPage);
        //only one xpath is provided, therefore we know only one list with urls will be returned in the wrapper
        List<String> seedUrls = parsedPage.get(0);
        xpaths = new ArrayList<>();

        //xpath to retrieve links to various articles from the bcc news
        xpaths.add("//article//h3//a[starts-with(@href,'/news')]//@href");
        urlParser.setXPaths(xpaths);

        for(String seedUrl : seedUrls){
            if(this.visitedUrls.contains(seedUrl))
                continue;
            visitedUrls.add(seedUrl);
            if(!seedUrl.contains(rootPage))
                seedUrl = rootPage + seedUrl;

            parsedPage = urlParser.crawlUrls(seedUrl);
            List<String> tmp = parsedPage.get(0);
            for(String articleUrl : tmp){

                addUrlToQueue(articleUrl);

            }
        }*/


    }


}
