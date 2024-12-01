package crawlee.org.src.crawler.worker;

import crawlee.org.src.common.constants.Constants;
import crawlee.org.src.common.util.SetsUtil;
import crawlee.org.src.crawler.model.ScrapedDocument;
import crawlee.org.src.crawler.util.JsoupXpathUtil;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

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
        keys = SetsUtil.difference(Constants.X_PATH_KEYWORDS, xPaths.keySet());
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
        String xPathTraversalExpression = xPaths.get(Constants.TRAVERSAL_XPATH_KEYWORD);
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
