package org.src.crawler;

import org.src.crawler.model.ScrapedDocument;
import org.src.crawler.model.WebScraperConfig;
import org.src.crawler.worker.CrawlerWorker;

import java.util.*;
import java.util.logging.Logger;

public class Crawler {

    private static final Logger log = Logger.getLogger(Crawler.class.getCanonicalName());


    /**
     * initialized in constructor
     */
    private final CrawlerWorker[] crawlerWorkers;
    private final List<String> urls;


    private Set<String> visitedUrls;

    /**
     * initialized from config file
     * if config is not loaded, the crawler will not work
     */
    private int politenessInterval;
    private Map<String, String> xPaths;
    private String rootPage;
    private int urlIndex;
    private final WebScraperConfig config;


    public Crawler(WebScraperConfig config) {
        this.config = config;
        this.crawlerWorkers = new CrawlerWorker[config.getWorkerCount()];
        this.urls = new ArrayList<>();
        this.urlIndex = 0;
        this.xPaths = config.getXpaths();
        this.rootPage = config.getRootPage();
        this.politenessInterval = config.getPoliteness();
        this.visitedUrls = new HashSet<>();
    }

    public String getRootPage() {
        return this.rootPage;
    }


    /**
     * initialization of pools and starting the threads
     */
    public void run() {
        log.info(String.format("Starting crawler on root page: %s with worker count: %d\n.", rootPage, config.getWorkerCount()));
        addUrlToQueue(rootPage);
        log.finest("Starting workers.");
        for(int i = 0; i < config.getWorkerCount(); i++){
            crawlerWorkers[i] = new CrawlerWorker(xPaths, this::getUrl,this::addUrlToQueue, this::saveParsedDocument);
            crawlerWorkers[i].start();
        }
        for(int i = 0; i < config.getWorkerCount(); i++){
            try {
                crawlerWorkers[i].join();
            }
            catch (InterruptedException e){
                log.warning("Interrupt exception while waiting for workers to finish! %s");
                e.printStackTrace();
            }
        }
    }


    /**
     * Adds url to set of urls we will process
     *
     * @param url url of a page we want to parse
     */
    public synchronized void addUrlToQueue(String url) {
        if (this.visitedUrls.contains(url)){
            log.info(String.format("Url: %s already visited, skipping.", url));
            return;
        }
        this.visitedUrls.add(url);

        if (!url.startsWith(rootPage))
            url = rootPage + url;
        log.info(String.format("Adding url: %s to queue", url));
        this.urls.add(url);
        notify();
    }


    //give url to worker
    public synchronized String getUrl() {
        while(urlIndex == urls.size()){
            try{
                wait();
            }
            catch (InterruptedException e){
                log.warning("Interrupted exception in worker thread!");
                e.printStackTrace();
            }
        }
        String availableUrl = urls.get(urlIndex);
        urlIndex++;
        return availableUrl;
    }





    public synchronized void saveParsedDocument(ScrapedDocument scrapedDocument){
        log.info(String.format("Saving document with content: %s", scrapedDocument.getContent().toString()));
        System.out.println(scrapedDocument);
    }


}