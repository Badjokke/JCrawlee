package org.src.crawler.worker;


import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.src.crawler.constants.Constants;
import org.src.crawler.model.ScrapedDocument;
import org.src.crawler.util.JsoupXpathUtil;
import org.src.crawler.util.SetsUtil;

import java.io.IOException;
import java.util.*;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.logging.Logger;

public class CrawlerWorker extends Thread {
    private final Logger log = Logger.getLogger(CrawlerWorker.class.getCanonicalName());
    private final Map<String, String> xPaths;
    private final Supplier<String> urlSupplier;
    private final Consumer<String> urlConsumer;
    private final Consumer<ScrapedDocument> documentConsumer;

    private final Set<String> keys;


    public CrawlerWorker(Map<String, String> xPaths, Supplier<String> urlSupplier,
                         Consumer<String> urlConsumer, Consumer<ScrapedDocument> documentConsumer) {
        this.xPaths = xPaths;
        this.urlSupplier = urlSupplier;
        this.documentConsumer = documentConsumer;
        this.urlConsumer = urlConsumer;
        keys = SetsUtil.difference(Constants.xPathKeywords, xPaths.keySet());
    }


    @Override
    public void run() {
        while (true) {
            String url = urlSupplier.get();
            if (url == null) {
                break;
            }
            log.info(String.format("Worker: %d parsing url: %s", Thread.currentThread().threadId(), url));
            try {
                Document document = Jsoup.connect(url).get();
                extractTraversalUrls(document);
                documentConsumer.accept(parseDocument(document));
            } catch (IOException e) {
                log.warning(String.format("IO exception when parsing url: %s\n message: %s", url, e.getMessage()));
                e.printStackTrace();
            }
        }


    }

    private ScrapedDocument parseDocument(Document document) {
        Map<String, List<String>> content = new HashMap<>();
        for (String xPathExpression : keys) {
            Elements elements = document.selectXpath(this.xPaths.get(xPathExpression));
            if (elements.isEmpty()) {
                log.info(String.format("Xpath expression: %s did not match any elements.", this.xPaths.get(xPathExpression)));
            }
            List<String> values = new ArrayList<>();
            for (Element element : elements) {
                values.add(JsoupXpathUtil.readNodeValueBasedOnTag(element));
            }
            content.put(xPathExpression, values);

        }
        return new ScrapedDocument(content);
    }

    private void extractTraversalUrls(Document document) {
        String xPathTraversalExpression = xPaths.get(Constants.traversalXpathKeyword);
        if (xPathTraversalExpression == null) {
            log.info("No traversal expression specified for worker.");
            return;
        }
        document.selectXpath(xPathTraversalExpression)
                .forEach(element -> {
                    String extractedPath = JsoupXpathUtil.readNodeValueBasedOnTag(element);
                    log.info(String.format("Extracted traversal path: %s", extractedPath));
                    urlConsumer.accept(extractedPath);
                });
    }


}
