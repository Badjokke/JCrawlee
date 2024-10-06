package org.src.crawler.worker;


import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.src.crawler.constants.Constants;
import org.src.crawler.model.ScrapedDocument;
import org.src.crawler.util.SetsUtil;

import java.io.IOException;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.logging.Logger;

public class CrawlerWorker extends Thread {
    private final Logger log = Logger.getLogger(CrawlerWorker.class.getCanonicalName());
    private final Map<String, String> xPaths;
    private final Supplier<String> urlSupplier;
    private final Consumer<String> urlConsumer;
    private final Consumer<ScrapedDocument> documentConsumer;




    public CrawlerWorker(Map<String, String> xPaths, Supplier<String> urlSupplier, Consumer<String> urlConsumer, Consumer<ScrapedDocument> documentConsumer) {
        this.xPaths = xPaths;
        this.urlSupplier = urlSupplier;
        this.documentConsumer = documentConsumer;
        this.urlConsumer = urlConsumer;
    }


    @Override
    public void run() {
        String url = urlSupplier.get();
        log.info(String.format("Worker: %d parsing url: %s", Thread.currentThread().threadId(), url));
        try {
            Document document = Jsoup.connect(url).get();
            documentConsumer.accept(parseDocument(document));
        } catch (IOException e) {
            log.warning(String.format("IO exception when parsing url: %s\n message: %s", url, e.getMessage()));
            e.printStackTrace();
        }

    }

    private ScrapedDocument parseDocument(Document document) {
        Set<String> xPaths = SetsUtil.difference(Constants.xPathKeywords,this.xPaths.keySet());
        for(String xPathExpression : xPaths){
            Elements elements = document.selectXpath(this.xPaths.get(xPathExpression));
            System.out.println(elements);
        }
        return new ScrapedDocument(null);
    }


}
