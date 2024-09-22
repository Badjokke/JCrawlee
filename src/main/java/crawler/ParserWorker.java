package crawler;

import edu.uci.ics.crawler4j.crawler.CrawlConfig;
import edu.uci.ics.crawler4j.crawler.Page;
import edu.uci.ics.crawler4j.crawler.exceptions.PageBiggerThanMaxSizeException;
import edu.uci.ics.crawler4j.crawler.exceptions.ParseException;
import edu.uci.ics.crawler4j.fetcher.PageFetchResult;
import edu.uci.ics.crawler4j.fetcher.PageFetcher;
import edu.uci.ics.crawler4j.parser.HtmlParseData;
import edu.uci.ics.crawler4j.parser.NotAllowedContentException;
import edu.uci.ics.crawler4j.parser.ParseData;
import edu.uci.ics.crawler4j.parser.Parser;
import edu.uci.ics.crawler4j.url.WebURL;
import org.apache.http.HttpStatus;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import us.codecraft.xsoup.Xsoup;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Worker thread for crawling
 * This class asks crawler.Crawler for url address and then proceeds to parse it with xPath expressions
 * at the end of run method, workers adds page into crawler.Crawler parsedQueue
 */
public class ParserWorker extends Thread {
    private Map<String, String> xpathExpressions;
    private final Crawler manager;
    private final int politenessInterval;
    private final PageFetcher pageFetcher;
    private final Parser parser;

    public ParserWorker(Crawler manager, Map<String, String> xpathExpressions, int politenessInterval) {
        this.manager = manager;
        this.xpathExpressions = xpathExpressions;
        this.politenessInterval = politenessInterval;
        //default crawler lib config
        CrawlConfig config = new CrawlConfig();
        this.pageFetcher = new PageFetcher(config);
        this.parser = new Parser(config);
        config.setMaxDepthOfCrawling(0);
        config.setResumableCrawling(false);
    }

    @Override
    public void run() {
        while (true) {
            String url = this.manager.getUrl();
            //no url is available for parsing, end the thread
            if (url == null) break;
            Page p = parse(url);
            //some faulty url, continue
            if (p == null) continue;
            List<List<String>> parsedData = evalPage(p);
            processArticle(parsedData);

        }
    }


    private void processArticle(List<List<String>> parsedData) {


    }


    private Page parse(String url) {
        WebURL curURL = new WebURL();
        curURL.setURL(url);
        Page page = null;
        PageFetchResult fetchResult = null;
        try {
            fetchResult = pageFetcher.fetchPage(curURL);
            if (fetchResult.getStatusCode() == HttpStatus.SC_MOVED_PERMANENTLY) {
                curURL.setURL(fetchResult.getMovedToUrl());
                fetchResult = pageFetcher.fetchPage(curURL);
            }
            if (fetchResult.getStatusCode() == HttpStatus.SC_OK) {

                page = new Page(curURL);
                fetchResult.fetchContent(page);
                parser.parse(page, curURL.getURL());
                return page;

            }

        } catch (PageBiggerThanMaxSizeException | IOException | InterruptedException | NotAllowedContentException |
                 ParseException e) {
            e.printStackTrace();
        }


        return null;
    }

    private List<List<String>> evalPage(Page page) {
        ParseData parseData = page.getParseData();
        List<List<String>> data = new ArrayList<>();
        if (parseData != null) {
            if (parseData instanceof HtmlParseData) {

                Document document = Jsoup.parse(((HtmlParseData) parseData).getHtml());
                for (String attribute : this.xpathExpressions.keySet()) {
                    String xPathExpression = this.xpathExpressions.get(attribute);
                    List<String> xlist = Xsoup.compile(xPathExpression).evaluate(document).list();
                    data.add(xlist);
                }
            }
        }
        return data;
    }


}
